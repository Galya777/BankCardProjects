using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LoginUserControl
{
    public delegate void LoginEventHandler(object sender, LoginEventArgs args);
    public class LoginEventArgs : EventArgs
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public int ListBoxMarked { get; set; }

        public LoginEventArgs(string un, string pw, int lbItem)
        {
            Username = un;
            Password = pw;
            ListBoxMarked = lbItem;
        }
    }
}
