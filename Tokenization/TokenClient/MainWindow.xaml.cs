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
using Tokenization.Consts;
using Tokenization.Access;

namespace TokenClient
{
    public partial class MainWindow : Window
    {
        private Client client;
        public MainWindow()
        {
            InitializeComponent();
            client = new Client(DisplayMessage, DisplayBox);
        }

        private void DisplayMessage(object message)
        {
            if (!Dispatcher.CheckAccess())
                Dispatcher.Invoke(new Action<string>(DisplayMessage), message);
            else
                ucRequest.Result = (string)message;
        }

        private void DisplayBox(object message)
        {
            if (!Dispatcher.CheckAccess())
                Dispatcher.Invoke(new Action<string>(DisplayMessage), message);
            else
                MessageBox.Show((string)message, Constants.ATTENTION_TITLE, MessageBoxButton.OK, MessageBoxImage.Information);
        }

        
        private void mainWindow_Closed(object sender, EventArgs e)
        {
            System.Environment.Exit(System.Environment.ExitCode);
        }

        private void ucLogin_Login(object sender, LoginUserControl.LoginEventArgs args)
        {
            try
            {
                if (client.LogIn(args.Username, args.Password.GetHashCode().ToString()))
                    ShowRequestUC();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, Constants.FATAL_ERROR, MessageBoxButton.OK, MessageBoxImage.Error);
                System.Environment.Exit(System.Environment.ExitCode);
            } 
        }

        private void ucLogin_Register(object sender, LoginUserControl.LoginEventArgs args)
        {
            try
            {
                if (!client.IsUsernameValid(args.Username))
                    MessageBox.Show(Constants.USERNAME_INCORRECT, Constants.INCORRECT_TITLE,
                                    MessageBoxButton.OK, MessageBoxImage.Information);
                else if (args.ListBoxMarked == -1)
                    MessageBox.Show(Constants.ACCESS_NOT_SELECTED, Constants.INCORRECT_TITLE,
                                    MessageBoxButton.OK, MessageBoxImage.Information);

                else if (client.Register(args.Username, args.Password.GetHashCode().ToString(),
                        (AccessLevel)args.ListBoxMarked))
                    ShowRequestUC();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, Constants.FATAL_ERROR, MessageBoxButton.OK, MessageBoxImage.Error);
                System.Environment.Exit(System.Environment.ExitCode);
            }
        }

       
        private void ShowRequestUC()
        {
            ucLogin.Visibility = Visibility.Hidden;
            ucRequest.Visibility = Visibility.Visible;
        }

        private void ucRequest_TokenRequested(object sender, TokenProcessorUserControl.GenerateEventArgs args)
        {
            try
            {
                client.RequestToken(args.From);
            }
            catch(Exception e)
            {
                MessageBox.Show(e.Message, Constants.FATAL_ERROR, MessageBoxButton.OK, MessageBoxImage.Error);
                System.Environment.Exit(System.Environment.ExitCode);
            }
        }

        private void ucRequest_CardIDRequested(object sender, TokenProcessorUserControl.GenerateEventArgs args)
        {
            try
            {
                client.RequestCardID(args.From);
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, Constants.FATAL_ERROR, MessageBoxButton.OK, MessageBoxImage.Error);
                System.Environment.Exit(System.Environment.ExitCode);
            }
        }

    }
}
