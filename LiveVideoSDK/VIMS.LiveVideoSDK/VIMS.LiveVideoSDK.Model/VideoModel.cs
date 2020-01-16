using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class VideoModel :BaseFile
    {
        /// <summary>
        /// 展示图片
        /// </summary>
        public string ShowPicutre { get; set; }

        /// <summary>
        /// 视频地址
        /// </summary>
        public string VideoUrl { get; set; }
        /// <summary>
        /// 视频开始时间
        /// </summary>
        public string VideoStartTime { get; set; }
        /// <summary>
        /// 视频结束时间
        /// </summary>
        public string VideoEndTime { get; set; }

        /// <summary>
        /// 视频描述
        /// </summary>
        public string VideoDescription { get; set; }

        /// <summary>
        /// 视频文件名字
        /// </summary>
        public string VideoName { get; set; }

        /// <summary>
        /// 视频时长
        /// </summary>
        public string VideLength { get; set; }


        /// <summary>
        /// 时长
        /// </summary>
        public string Duration { get; set; }
        /// <summary>
        /// 视频流的名字
        /// </summary>
        public string StreamName { get; set; }


        /// <summary>
        /// 视频最后修改时间
        /// </summary>
        public string LastModifyTime { get; set; }

        public string FileId { get; set; }

    }
}
