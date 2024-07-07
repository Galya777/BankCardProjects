using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TokenProcessorUserControl
{
    public delegate void GenerateEventHandler(object sender, GenerateEventArgs args);
    public class GenerateEventArgs
    {
        public string From { get; set; }

        public GenerateEventArgs(string id)
        {
            From = id;
        }
    }
}
