using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tokenization
{
    namespace Consts
    {
        // Class that holds the constants for the solution
        public static class Constants
        {
            public const string LOCALHOST = "127.0.0.1";
            public const int PORT = 10000;
            public const int VALID_CARD_LENGTH = 16;
            public const string LOGIN_SUCCESSFUL = "Welcome";
            public const string REGISTER_SUCCESSFUL = "Registration successful!";
            public const string INVALID_CARD_ID = "The ID of the card is not valid.";
            public const string TOKEN_CREATE_FAILED = "Could not create token.";
            public const string NAME_HAS_CREATED_TOKEN = "{0} created Token {1}";
            public const string ACCESS_DENIED = "Your access level is not high enough.";
            public const string ID_NOT_FOUND = "There's no ID associated to this token.";
            public const string NAME_HAS_REQUESTED = "{0} has requested {1}";
            public const string USERNAME_EXISTS = "Username already exists!";
            public const string NAME_HAS_REGISTERED = "{0} has registered successfully.";
            public const string WELCOME_BACK_NAME = "Welcome back, {0}!";
            public const string NAME_HAS_LOGGED_IN = "{0} has logged in.";
            public const string INCORRECT_INPUT = "Username or password was incorrect.";
            public const string WAITING_FOR_CONNECTION = "Server loaded. Waiting for connection.";
            public const string CONNECTION_ACCEPTED = "Connection received.";
            public const string CARDS_FILE = "cards.xml";
            public const string USERS_FILE = "users.xml";
            public const string DESERIALIZE_SUCCESSFUL = "Deserialization successful.\nLoaded {0} card(s) and {1} user(s).";
            public const string LISTS_RESET = "Lists have been reset.";
            public const string CARD_VS_TOKEN = "Card: {0} < - > {1} :Token";
            public const string TOKEN_VS_CARD = "Token: {0} < - > {1} :Card";
            public const string FATAL_ERROR = "Fatal error!";
            public const string ACCESS_NOT_SELECTED = "Please select an access level";
            public const string USERNAME_INCORRECT = "Username must be between 6-20 characters.";
            public const string INCORRECT_TITLE = "Incorrect!";
            public const string ATTENTION_TITLE = "Attention!";
        }
    }
}
