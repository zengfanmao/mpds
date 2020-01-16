using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GRGcms.Model
{
    [Serializable]
    public class mq_msg
    {
        public string cmd
        {
            get;
            set;
        }
        public string cmdx
        {
            get;
            set;
        }
        public Dictionary<string, string> data
        {
            get;
            set;
        }
        public string len
        {
            get;
            set;
        }
        public string sync
        {
            get;
            set;
        }
        public string type
        {
            get;
            set;
        }
        public string ver
        {
            get;
            set;
        }
    }
}
