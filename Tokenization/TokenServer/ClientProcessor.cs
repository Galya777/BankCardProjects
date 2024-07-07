using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;
using System.Net;
using Tokenization.Access;
using Tokenization.Activities;
using Tokenization.Consts;


namespace TokenServer
{
    public class ClientProcessor
    {
        // Class, given to each thread to run
        // It represents every user's connection
        private Socket currentSocket;
        private BinaryReader reader;
        private BinaryWriter writer;
        private NetworkStream networkStream;
        private User client;
        private List<User> clientsRef;
        private List<BankCard> bankCardsRef;
        private Action<object> DisplayMethod;
        

        // C-tor, that accepts a Action<object>, so it can print to the UI
        // Also The lists of users and cards, which keeps as references only
        public ClientProcessor(Action<object> message, List<User> clients, List<BankCard> cards)
        {
            DisplayMethod = message;
            clientsRef = clients;
            bankCardsRef = cards;
        }

        // On ending a connection, all streams are being shut down
        ~ClientProcessor()
        {
            if (client != null)
            {
                reader.Close();
                writer.Close();
                networkStream.Close();
                currentSocket.Close();
            }
        }

        // Method, that adds a token to the card array
        // It's being locked to prevent thread misunderstanding
        private void AddToCards(string cardID, string token)
        {
            lock(bankCardsRef)
            {
                BankCard current = null;
                try
                {
                    current = bankCardsRef.Single(card => card.ID == cardID);
                    current.Tokens.Add(new Token(token, current.ID));
                }
                catch(InvalidOperationException)
                {
                    bankCardsRef.Add(new BankCard(cardID, new Token(token, cardID)));
                }
            }
        }

        // The main method, that is given to the Thread to launch
        public void Process(object socket)
        {
            currentSocket = socket as Socket;
            if (currentSocket == null)
                throw new InvalidDataException();

            networkStream = new NetworkStream(currentSocket);
            reader = new BinaryReader(networkStream);
            writer = new BinaryWriter(networkStream);

            try
            {
                while (client == null && currentSocket.Connected)
                    client = DetermineLogin();
            }
            catch (Exception e)
            {
                DisplayMethod(e.Message);
            }

            while(currentSocket.Connected)
            {
                try
                {
                    ProcessRequest();
                }
                catch(Exception e)
                {
                    DisplayMethod(e.Message);
                }
            }
        }

        // Reads the flag, sent from the client and determines what to do
        private User DetermineLogin()
        {
            Activity response = (Activity)reader.ReadInt32();

            if (response == Activity.REGISTER)
                return RegisterClient();

            if (response == Activity.LOGIN)
                return LogClientIn();

            throw new InvalidDataException();
        }

        // Generates token and sends it back to the client
        private void RequestToken()
        {
            writer.Write((int)Activity.ACCEPTED);
            string cardID = reader.ReadString();
            string token = String.Empty;
            if ((token = Tokenizer.MakeToken(cardID)) == null)
            {
                writer.Write(Constants.INVALID_CARD_ID);
                return;
            }

            int attempts = 0;
            while(!IsTokenAvailable(token))
            {
                if (attempts++ == 100000)
                {
                    writer.Write(Constants.TOKEN_CREATE_FAILED);
                    return;
                }
                token = Tokenizer.MakeToken(cardID);
            }

            AddToCards(cardID, token);
            writer.Write(token);
            DisplayMethod(String.Format(Constants.NAME_HAS_CREATED_TOKEN, client.Username, token));
        }

        // Reads the flag, sent from the client and determines what to do
        private void ProcessRequest()
        {
            Activity response = (Activity)reader.ReadInt32();

            if (response == Activity.REQUEST_CARD && client.Access >= AccessLevel.REQUEST)
                RequestCardID();
            else if (response == Activity.REGISTER_TOKEN && client.Access >= AccessLevel.REGISTER)
                RequestToken();
            else
                writer.Write((int)Activity.DENIED);
        }

        // Checks if the given token doesn't exist already in the database
        private bool IsTokenAvailable(string token)
        {
            foreach(BankCard card in bankCardsRef)
            {
                if (card.Tokens.Any(tk => tk.ID == token))
                    return false;
            }
            return true;
        }

        // Gets a Card ID, based on the read token and sends it back to the client
        private void RequestCardID()
        {
            writer.Write((int)Activity.ACCEPTED);
            string token = reader.ReadString();
            string cardID = Constants.ID_NOT_FOUND;

            foreach (BankCard card in bankCardsRef)
            {
                if (card.Tokens.Any(tk => tk.ID == token))
                {
                    cardID = card.ID;
                    break;
                }
            }

            writer.Write(cardID);
            DisplayMethod(String.Format(Constants.NAME_HAS_REQUESTED, client.Username, cardID));
        }

        // Registers a User in the database
        private User RegisterClient()
        {
            string username = String.Empty;
            string password = String.Empty;
            AccessLevel access = AccessLevel.NONE;

            username = reader.ReadString();
            password = reader.ReadString();
            access = (AccessLevel)reader.ReadInt32();

            if (clientsRef.Any(cl => cl.Username.Equals(username)))
            {
                writer.Write(Constants.USERNAME_EXISTS);
                return null;
            }
            
            writer.Write(Constants.REGISTER_SUCCESSFUL);
            User current = new User(username.Trim(), password.Trim(), (AccessLevel)Convert.ToInt32(access));
            lock(clientsRef)
            {
                // locking the object to prevent multiple addings and misunderstanding
                clientsRef.Add(current);
            }
            DisplayMethod(String.Format(Constants.NAME_HAS_REGISTERED, username));

            return current;
        }

        // Logs a client into the system
        private User LogClientIn()
        {
            string username = String.Empty;
            string password = String.Empty;
            User current = null;

            try
            {
                username = reader.ReadString();
                password = reader.ReadString();
                current = clientsRef.Single(cl => cl.Username.Equals(username) && cl.Password.Equals(password));

                writer.Write(String.Format(Constants.WELCOME_BACK_NAME, current.Username));
                DisplayMethod(String.Format(Constants.NAME_HAS_LOGGED_IN, username));
            }
            catch (InvalidOperationException)
            {
                writer.Write(Constants.INCORRECT_INPUT);
            }

            return current;
        }
    }
}
