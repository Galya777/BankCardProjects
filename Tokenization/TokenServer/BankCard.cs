using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TokenServer
{
    public class BankCard
    {
        // A BankCard has its ID and a List of its registered tokens
        private string id;
        private List<Token> tokens;

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

        public List<Token> Tokens
        {
            get
            {
                return tokens;
            }
            set
            {
                if (value != null)
                    tokens = value;
            }
        }

        // Constructor, that accepts an ID and a single Token to add to the list
        public BankCard(string num, Token token)
        {
            Tokens = new List<Token>();
            ID = num;

            Tokens.Add(token);
        }
        
        // Default c-tor
        public BankCard()
        {
            ID = String.Empty;
            Tokens = new List<Token>();
        }
        
        // Copy c-tor, we don't want it to be accessed
        private BankCard(BankCard other)
        {   }
    }
}
