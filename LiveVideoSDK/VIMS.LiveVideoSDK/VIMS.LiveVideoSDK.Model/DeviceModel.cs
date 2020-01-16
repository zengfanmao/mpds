using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class DeviceModel
    {
        public Nullable<bool> isDel { get; set; }
        public byte[] recSN { get; set; }
        public string devCode { get; set; }
        public string devName { get; set; }
        public string devType { get; set; }
        public string devBrand { get; set; }
        public string devModel { get; set; }
        public Nullable<System.DateTime> devPDate { get; set; }
        public Nullable<int> devGPeriod { get; set; }
        public Nullable<System.DateTime> devSTime { get; set; }
        public string uCode { get; set; }
        public string devStatus { get; set; }
        public string devPhoto { get; set; }
        public string devRemark { get; set; }
        public String ID { get; set; }
        public string devSN { get; set; }
        /// <summary>
        /// 直播状态
        /// </summary>
        public string LivingState { get; set; }
        /// <summary>
        /// 直播id
        /// </summary>
        public string LivingID { get; set; }


        public string CaseName { get; set; }
        public string CaseCode { get; set; }
    }
}
