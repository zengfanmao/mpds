using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using VIMS.LiveVideoSDK.Bll.Entity;
using VIMS.LiveVideoSDK.Model;

namespace VIMS.LiveVideoSDK.Bll
{
    public class DeptTree
    {
        public DeptModel dept { get; set;}
        public int olUser { get; set; }
        public int allUser { get; set; }
        public IList<DeptTree> children { get; set; }
    }
}
