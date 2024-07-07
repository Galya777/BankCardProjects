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

namespace TokenProcessorUserControl
{
    public partial class TokenProcessorUserControl : UserControl
    {
        public event GenerateEventHandler TokenRequested;
        public event GenerateEventHandler CardIDRequested;

        public string From
        {
            get
            {
                return txtRequest.Text;
            }
            set
            {
                if (value != null)
                    txtRequest.Text = value;
                else
                    txtRequest.Text = String.Empty;
            }
        }

        public string Result
        {
            get
            {
                return txtResult.Text;
            }
            set
            {
                if (value != null)
                    txtResult.Text = value;
                else
                    txtResult.Text = String.Empty;
            }
        }

        public TokenProcessorUserControl()
        {
            InitializeComponent();
        }

        private void btnToken_Click(object sender, RoutedEventArgs e)
        {
            if(TokenRequested != null)
            {
                TokenRequested(this, new GenerateEventArgs(From));
            }
        }

        private void btnID_Click(object sender, RoutedEventArgs e)
        {
            if(CardIDRequested != null)
            {
                CardIDRequested(this, new GenerateEventArgs(From));
            }
        }
    }
}
