using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class AudioModel:BaseFile
    {

        /// <summary>
        /// 音频地址
        /// </summary>
        public string AudioUrl { get; set; }
        /// <summary>
        /// 音频开始时间
        /// </summary>
        public string AudioStartTime { get; set; }
        /// <summary>
        /// 音频结束时间
        /// </summary>
        public string AudioEndTime { get; set; }

        /// <summary>
        /// 音频描述
        /// </summary>
        public string AudioDescription { get; set; }
        public string FileId { get; set; }
        /// <summary>
        /// 时长
        /// </summary>
        public string Duration { get; set; }
    }
}
