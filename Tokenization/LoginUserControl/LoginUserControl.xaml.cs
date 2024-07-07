using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace LoginUserControl
{
    public partial class LoginUserControl : UserControl
    {
        public event LoginEventHandler Login;
        public event LoginEventHandler Register;
        public string Username
        {
            get
            {
                return txtUsername.Text;
            }
            set
            {
                if (value != null)
                    txtUsername.Text = value;
                else
                    txtUsername.Text = String.Empty;
            }
        }

        public string Password
        {
            get
            {
                return txtPassword.Password;
            }
            set
            {
                if (value != null)
                    txtPassword.Password = value;
                else
                    txtPassword.Password = String.Empty;
            }
        }

        public int ListBoxMarked
        {
            get
            {
                ListBoxItem current = lbxAccess.SelectedItem as ListBoxItem;
                if (current == null)
                    return -1;
                
                return Convert.ToInt32(current.Tag.ToString());
            }
        }

        public LoginUserControl()
        {
            InitializeComponent();
        }

        private void btnLogin_Click(object sender, RoutedEventArgs e)
        {
            if(Login != null)
            {
                Login(this, new LoginEventArgs(Username, Password, ListBoxMarked));
            }
        }

        private void btnRegister_Click(object sender, RoutedEventArgs e)
        {
            if (Register != null)
            {
                Register(this, new LoginEventArgs(Username, Password, ListBoxMarked));
            }
        }

        private void chkIsRegister_Checked(object sender, RoutedEventArgs e)
        {
            lbxAccess.Visibility = Visibility.Visible;
            btnRegister.Visibility = Visibility.Visible;
        }

        private void chkIsRegister_Unchecked(object sender, RoutedEventArgs e)
        {
            lbxAccess.Visibility = Visibility.Hidden;
            btnRegister.Visibility = Visibility.Hidden;
        }
    }
}
