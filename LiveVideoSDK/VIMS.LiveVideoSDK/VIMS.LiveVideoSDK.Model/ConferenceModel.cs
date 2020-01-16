using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class ConferenceModel
    {
        public string conference_Creator  { get; set; }
        public string conference_CreatorIsPdt { get; set; }
        public string conference_name { get; set; }
        public string conference_uuid { get; set; }
        public string EventName { get; set; }
        public string IsPdt { get; set; }
        public string ptt_type { get; set; }
        public string CalledSSI { get; set; }
        public string CallerSSI { get; set; }
    }
}
