using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tokenization
{
    namespace Access
    {
        // Holds the AccessLevels for the users
        public enum AccessLevel
        {
            NONE = 0,
            REGISTER = 1,
            REQUEST = 2,
            MASTER = 3
        };
    }

    namespace Activities
    {
        // Holds the Activities that users can prompt from the server
        // e.g Register an account, Login, Request Token and Request Card ID
        public enum Activity
        {
            ACCEPTED = 8000,
            DENIED = 10000,
            REGISTER = 12000,
            LOGIN = 14000,
            REGISTER_TOKEN = 15000,
            REQUEST_CARD = 16000
        };
    }
}
