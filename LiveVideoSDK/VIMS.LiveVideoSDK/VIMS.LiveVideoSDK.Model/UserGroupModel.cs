using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class UserGroupModel
    {
        public int id { get; set; }
        public string groupid { get; set; }
        public string type { get; set; }
        public string account { get; set; }
        public string name { get; set; }
        public string dept { get; set; }
        public string deptid { get; set; }
        public Nullable<System.DateTime> createtime { get; set; }
        public Nullable<System.DateTime> updatetime { get; set; }
        public string groupName { get; set; }
        public int userCount { get; set; }
        public int uIsUnilt { get; set; }
        public string related { get; set; }
        public string isdefault { get; set; }
        public int page { get; set; }
        public int pageSize { get; set; }
        public string rankNo { get; set; }
        public string rankName { get; set; }
        public string relativegroupid { get; set; }
    }
}
