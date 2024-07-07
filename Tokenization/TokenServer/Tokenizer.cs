using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tokenization.Consts;

namespace TokenServer
{
    public static class Tokenizer
    {
        // Main function that starts the tokenization
        public static string MakeToken(string ID)
        {
            if (!IsIDValid(ID))
                return null;

            string lastFourDigits = ID.Substring(ID.Length - 4, 4);

            return BuildToken(ID) + lastFourDigits;
        }

        // Creates the new token
        private static string BuildToken(string ID)
        {
            int[] token = new int[ID.Length - 4];
            Random random = new Random((int)DateTime.Now.Ticks);
            int sum = 0;

            do
                token[0] = GenerateCorrectNumber(ID[0], random);
            while(!IsNewStartDigitValid(token[0]));

            for (int i = 1; i < token.Length; ++i)
            {
                token[i] = GenerateCorrectNumber(ID[i], random);
                sum += token[i];
            }

            while (sum % 10 == 0)
            {
                sum -= token[token.Length - 1];
                token[token.Length - 1] = GenerateCorrectNumber(ID[token.Length - 1], random);
                sum += token[token.Length - 1];
            }


            char[] result = new char[token.Length];
            for (int i = 0; i < token.Length; ++i)
                result[i] = token[i].ToString()[0];

            return new string(result);
        }

        // Generates a number that isnt equal to the given char
        private static int GenerateCorrectNumber(char c, Random random)
        {
            int rand = 0;

            do
            {
                rand = random.Next(1, 10);
            } while (rand == Convert.ToInt32(c.ToString()));

            return rand;
        }

        // Checks if a given Card ID is valid
        private static bool IsIDValid(string ID)
        {
            return ID.Length == Constants.VALID_CARD_LENGTH && 
                   !IsNewStartDigitValid(Convert.ToInt32(ID[0].ToString())) && 
                   LuhnTest(ID);
        }

        // Runs a test using the Luhn Algorithm for validation
        private static bool LuhnTest(string ID)
        {
            int[] numberArray = new int[ID.Length];

            for (int i = 0; i < ID.Length; ++i)
                numberArray[i] = Convert.ToInt32(ID[i].ToString());

            int sum = 0;

            for (int i = 0; i < numberArray.Length; ++i)
            {
                if (i % 2 != 0)
                    numberArray[i] *= 2;

                if (numberArray[i] > 9)
                    numberArray[i] = (numberArray[i] % 10) + (numberArray[i] / 10);

                sum += numberArray[i];
            }
            return (sum % 10 == 0);
        }

        // Generates a digit that isn't equal to 3, 4, 5 or 6
        private static bool IsNewStartDigitValid(int digit)
        {
            return (digit != 3 && digit != 4 && digit != 5 && digit != 6);
        }
    }
}
