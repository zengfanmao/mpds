using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GRGcms.Model
{
    public class rankGroupModel
    {
        public string ID{get;set;}
        public string rank { get; set; }
        public string rankName { get; set; }
        public string discussionCode { get; set; }
        public string discussionName { get; set; }
        public DateTime createtime { get; set; }

    }
}
