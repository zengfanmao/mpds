using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class RankGroupModel
    {
        public string rank { get; set; }
        public string rankName { get; set; }
        public List<UserGroupModel> groupList { get; set; }
    }
}
