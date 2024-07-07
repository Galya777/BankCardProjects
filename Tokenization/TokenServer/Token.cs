using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TokenServer
{
    public class Token
    {
        // Class, represeting a Token in the system
        // A token has an ID and also holds its owner's (BankCard's) ID
        private string id;
        private string owner;

        public string ID
        {
            get
            {
                return id;
            }
            set
            {
                if (value != null)
                    id = value;
                else
                    id = String.Empty;
            }
        }

        public string Owner
        {
            get
            {
                return owner;
            }
            set
            {
                if (value != null)
                    owner = value;
            }
        }

        // General Purpose c-tor
        public Token(string numbers, string own)
        {
            ID = numbers;
            Owner = own;
        }

        // Default c-tor
        public Token() : this(String.Empty, String.Empty)
        {   }

        // Copy c-tor
        public Token(Token other) : this(other.ID, other.Owner)
        {   }
    }
}
