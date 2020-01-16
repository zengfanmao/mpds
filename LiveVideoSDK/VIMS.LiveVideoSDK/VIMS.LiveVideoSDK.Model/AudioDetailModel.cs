using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class AudioDetailModel
    {
        public string conference_uuid { get; set; }
        public string conference_name { get; set; }
        public string startor { get; set; }
        public string listener { get; set; }
        public string event_name { get; set; }
        public Nullable<System.DateTime> event_time { get; set; }
        public string content { get; set; }
    }
}
