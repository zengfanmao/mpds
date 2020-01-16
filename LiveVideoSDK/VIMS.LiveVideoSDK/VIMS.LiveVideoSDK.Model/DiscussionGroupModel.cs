using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class DiscussionGroupModel
    {

        public string ID { get; set; }
        public string createdTime { get; set; }
        public Nullable<System.DateTime> _createdTime { get; set; }

        public string discussionCode { get; set; }
        public string discussionName { get; set; }
        public string createdUserCode { get; set; }
        public string createdUserName { get; set; }
        public string csCode { get; set; }
    }
}
