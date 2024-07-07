using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.Net.Sockets;
using System.Net;
using System.IO;
using System.Xml.Serialization;
using Tokenization.Consts;

namespace TokenServer
{
    public class Server
    {
        // Class represeting the Server
        private Thread readThread;
        private List<User> clients;
        private List<BankCard> bankCards;
        private Action<object> DisplayMethod;
        private Action<object> DisplayError;

        // C-tor, that accepts two actions, so the server can write to the UI
        public Server(Action<object> message, Action<object> error)
        {
            if (null != message && null != error)
            {
                DisplayMethod = message;
                DisplayError = error;
                Load();
            }
            else
                System.Environment.Exit(System.Environment.ExitCode);
        }

        // Destructor, Serializes the data
        ~Server()
        {
            Serialize();
        }

        // Loads the data upon startup and starts the read thread
        public void Load()
        {
            clients = new List<User>();
            bankCards = new List<BankCard>();

            readThread = new Thread(new ThreadStart(RunServer));
            readThread.Start();
            Deserialize();
        }

        // Main method, Distributes Threads to the new connections
        private void RunServer()
        {
            TcpListener listener;
            try
            {
                IPAddress localhost = IPAddress.Parse(Constants.LOCALHOST);
                listener = new TcpListener(localhost, Constants.PORT);

                listener.Start();
                DisplayMethod(Constants.WAITING_FOR_CONNECTION);

                while (true)
                {
                    ThreadPool.QueueUserWorkItem(new WaitCallback(new ClientProcessor(
                                                 DisplayMethod, clients, bankCards).Process),
                                                 listener.AcceptSocket());

                    DisplayMethod(Constants.CONNECTION_ACCEPTED);
                }
            }
            catch (Exception e)
            {
                DisplayError(e.Message);
            }
        }

        // Serializes the card and user lists to XML files
        // The files are defined in the Constants class
        public void Serialize()
        {
            try
            {
                XmlSerializer cardSer = new XmlSerializer(typeof(List<BankCard>));
                XmlSerializer userSer = new XmlSerializer(typeof(List<User>));
                using (FileStream cards = new FileStream(Constants.CARDS_FILE, FileMode.Create, FileAccess.Write))
                using (FileStream users = new FileStream(Constants.USERS_FILE, FileMode.Create, FileAccess.Write))
                {

                    cardSer.Serialize(cards, bankCards);
                    userSer.Serialize(users, clients);
                }
            }
            catch (Exception e)
            {
                DisplayMethod(e.Message);
            }
        }

        // Deserializes the card and user lists to XML files
        // The files are defined in the Constants class
        private void Deserialize()
        {
            try
            {
                XmlSerializer cardDes = new XmlSerializer(typeof(List<BankCard>));
                XmlSerializer userDes = new XmlSerializer(typeof(List<User>));
                using (FileStream cards = new FileStream(Constants.CARDS_FILE, FileMode.Open, FileAccess.Read))
                using (FileStream users = new FileStream(Constants.USERS_FILE, FileMode.Open, FileAccess.Read))
                {

                    bankCards = (List<BankCard>)cardDes.Deserialize(cards);
                    clients = (List<User>)userDes.Deserialize(users);
                }

                DisplayMethod(String.Format(
                    Constants.DESERIALIZE_SUCCESSFUL,
                    bankCards.Count, clients.Count));
            }
            catch(Exception e)
            {
                DisplayMethod(e.Message);
                bankCards = new List<BankCard>();
                clients = new List<User>();
                DisplayMethod(Constants.LISTS_RESET);
            }
        }

        // Exports all Card <-> Token pairs to a text file, sorted by Card ID
        // Accepts an Action that opens the SaveFileDialog from the UI Thread
        public bool ExportSortedByCard()
        {
            string fileName = String.Empty;
            using(System.Windows.Forms.SaveFileDialog dialog = new System.Windows.Forms.SaveFileDialog())
            {
                dialog.ShowDialog(); 
                if (dialog.FileName == String.Empty)
                    return false;

                fileName = dialog.FileName;
            }
            try
            {
                using (FileStream output = new FileStream(fileName, FileMode.Create, FileAccess.Write))
                using (StreamWriter writer = new StreamWriter(output))
                {
                    IEnumerable<BankCard> sorted = bankCards.OrderBy(card => card.ID);
                    foreach (BankCard card in sorted)
                    {
                        foreach (Token token in card.Tokens)
                            writer.WriteLine(String.Format(Constants.CARD_VS_TOKEN, card.ID, token.ID));
                    }
                }
            }
            catch(Exception e)
            {
                DisplayMethod(e.Message);
                return false;
            }
            return true;
            
        }

        // Exports all Token <-> Card pairs to a text file, sorted by Token ID
        public bool ExportSortedByToken()
        {
            string fileName = String.Empty;
            using (System.Windows.Forms.SaveFileDialog dialog = new System.Windows.Forms.SaveFileDialog())
            {
                dialog.ShowDialog();
                if (dialog.FileName == String.Empty)
                    return false;

                fileName = dialog.FileName;
            }
            try
            {
                using (FileStream output = new FileStream(fileName, FileMode.Create, FileAccess.Write))
                using (StreamWriter writer = new StreamWriter(output))
                {
                    List<Token> tokens = new List<Token>();

                    foreach (BankCard card in bankCards)
                    {
                        foreach (Token token in card.Tokens)
                            tokens.Add(token);
                    }
                    IEnumerable<Token> sorted = tokens.OrderBy(token => token.ID);

                    foreach (Token token in sorted)
                        writer.WriteLine(String.Format(Constants.TOKEN_VS_CARD, token.ID, token.Owner));
                }
            }
            catch(Exception e)
            {
                DisplayMethod(e.Message);
                return false;
            }
            return true;
        }
    }
}
