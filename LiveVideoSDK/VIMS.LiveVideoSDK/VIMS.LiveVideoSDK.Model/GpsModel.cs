using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
   
    public class GpsModel
    {





        public string CaseName { get; set; }
        public string CaseCode { get; set; }

        public string UserCode { get; set; }
        public string UserName { get; set; }
        public string UserHeadPortrait { get; set; }

        /// <summary>
        /// 精确到分钟
        /// </summary>
        public int MinuteTime { get; set; }
        public string DayTime { get; set; }
        /// <summary>
        /// 取间隔多少的数据
        /// </summary>
        public int Interval { get; set; }


        public string DevCode { get; set; }
        public string DiscussionCode { get; set; }
        public string DiscussionName { get; set; }

        public string GpsTime { get; set; }
        public Nullable<System.DateTime> _CreatedTime { get; set; }
        public string Longitude { get; set; }
        public string Latitdue { get; set; }
        public string ID { get; set; }
        public string GpsTargetType { get; set; }
        public string status { get; set; }
        public string dCode { get; set; }
        public string dName { get; set; }
        public string uDepartment { get; set; }
        public string purpose { get; set; }

        public int page { get; set; }
        public int pageSize { get; set; }
    }
}
