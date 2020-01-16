using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class RecordingModel:BaseFile
    {

        /// <summary>
        /// 音频地址
        /// </summary>
        public string RecordingUrl { get; set; }
        /// <summary>
        /// 音频开始时间
        /// </summary>
        public Nullable<System.DateTime> RecordingTime { get; set; }
        /// <summary>
        /// 音频结束时间
        /// </summary>
        public string AudioEndTime { get; set; }

        /// <summary>
        /// 音频描述
        /// </summary>
        public string AudioDescription { get; set; }
        public string sender { get; set; }
        public string receiver { get; set; }
        public string fromType { get; set; }
        public string dName { get; set; }
        public string second { get; set; }
        public string uuid { get; set; }
        public DateTime startDt { get; set; }
        public DateTime endDt { get; set; }
        public int page { get; set; }
        public int pageSize { get; set; }
    }
}
