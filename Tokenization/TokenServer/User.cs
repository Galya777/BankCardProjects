using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using Tokenization.Access;

namespace TokenServer
{
    public class User
    {
        // Class that represents a User in the system
        // The user has a Username, Password and AccessLevel
        private string username;
        private string password;
        private AccessLevel access;

        public string Username
        {
            get
            {
                return username;
            }
            set
            {
                if (value != null)
                    username = value;
                else
                    username = String.Empty;
            }
        }
        public string Password
        {
            get
            {
                return password;
            }
            set
            {
                if (value != null)
                    password = value; // Needs hash
                else
                    password = String.Empty;
            }
        }

        public AccessLevel Access
        {
            get
            {
                return access;
            }
            set
            {
                if (value >= AccessLevel.NONE && value <= AccessLevel.MASTER)
                    access = value;
                else
                    access = AccessLevel.NONE;
            }
        }

        // General purpose c-tor
        public User(string un, string pw, AccessLevel level)
        {
            Username = un;
            Password = pw;
            Access = level;
        }

        // Default c-tor
        public User() : this(String.Empty, String.Empty, AccessLevel.NONE)
        {   }

        // Copy c-tor
        public User(User other) : this(other.Username, other.Password, other.Access)
        {   }

    }
}
