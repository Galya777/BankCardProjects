using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;
using Tokenization.Activities;
using Tokenization.Access;
using Tokenization.Consts;
using System.Text.RegularExpressions;

namespace TokenClient
{
    public class Client
    {
        private TcpClient tcpClient;
        private NetworkStream networkStream;
        private BinaryReader reader;
        private BinaryWriter writer;
        private Action<object> DisplayMessage;
        private Action<object> DisplayBox;

        // Accepts 2 Actions, so the class can write to the UI
        public Client(Action<object> DisplayM, Action<object> Box)
        {
            DisplayMessage = DisplayM;
            DisplayBox = Box;
            Connect();
        }

        // Requests a token from the server
        public void RequestToken(string from)
        {
            writer.Write((int)Activity.REGISTER_TOKEN);
            if ((Activity)reader.ReadInt32() == Activity.DENIED)
                DisplayMessage(Constants.ACCESS_DENIED);
            else
            {
                writer.Write(from);
                DisplayMessage(reader.ReadString());
            }
        }

        // Requests card ID from the server
        public void RequestCardID(string from)
        {
            writer.Write((int)Activity.REQUEST_CARD);
            if ((Activity)reader.ReadInt32() == Activity.DENIED)
                DisplayMessage(Constants.ACCESS_DENIED);
            else
            {
                writer.Write(from);
                DisplayMessage(reader.ReadString());
            }
        }

        // Connects to the server
        private void Connect()
        {
            try
            {
                tcpClient = new TcpClient();
                tcpClient.Connect(Constants.LOCALHOST, Constants.PORT);

                networkStream = tcpClient.GetStream();
                reader = new BinaryReader(networkStream);
                writer = new BinaryWriter(networkStream);
            }
            catch (Exception e)
            {
                DisplayBox(e.Message);
                System.Environment.Exit(System.Environment.ExitCode);
            }
        }

        // Username validation
        // Username must be between 6 and 20 characters
        // Username can contain letters and numbers, dots and underscores
        // Dot and underscore cannot be next to each other
        // Username must not start or end with a dot or underscore
        public bool IsUsernameValid(string username)
        {
            return Regex.Match(username, "^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$").Success;
        }

        // Sends a registration request to the server
        public bool Register(string username, string password, AccessLevel access)
        {
            writer.Write((int)Activity.REGISTER);
            writer.Write(username);
            writer.Write(password);
            writer.Write((int)access);

            string returnedMessage = reader.ReadString();
            DisplayBox(returnedMessage);

            if (returnedMessage == Constants.REGISTER_SUCCESSFUL)
                return true;

            return false;
        }

        // Sends a login request to the server
        public bool LogIn(string username, string password)
        {
            writer.Write((int)Activity.LOGIN);
            writer.Write(username);
            writer.Write(password);

            string returnedMessage = reader.ReadString();
            DisplayBox(returnedMessage);

            if (returnedMessage.StartsWith(Constants.LOGIN_SUCCESSFUL))
                return true;

            return false;
        }
    }
}
