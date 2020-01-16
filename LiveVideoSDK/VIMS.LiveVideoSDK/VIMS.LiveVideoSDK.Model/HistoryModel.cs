using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class HistoryModel
    {
        /// <summary>
        /// 展示图片
        /// </summary>
        public string ShowPicutre { get; set; }

        /// <summary>
        /// 视频信息
        /// </summary>
        public string VideoModel { get; set; }

        /// <summary>
        /// 视频信息
        /// </summary>
        public string ImageModel { get; set; }
        /// <summary>
        /// 消息类型
        /// </summary>
        public string NoticeType { get; set; }
        /// <summary>
        /// 发消息人
        /// </summary>
        public string UserName { get; set; }


        public string HeadPortrait { get; set; }
        public string CreatedTime { get; set; }

        /// <summary>
        /// 发送人
        /// </summary>
        public string UserCode { get; set; }
        /// <summary>
        /// 发送消息公告时间
        /// </summary>
        public string NoticeTime { get; set; }
        /// <summary>
        /// 推送公告消息内容
        /// </summary>
        public string NotcieContent { get; set; }
        /// <summary>
        /// 位置精度
        /// </summary>
        public string PositionLatitude { get; set; }
        /// <summary>
        /// 位置经度
        /// </summary>
        public string PositionLongitude { get; set; }
        public string PositionName { get; set; }
        /// <summary>
        /// 位置备注
        /// </summary>
        public string PositionRemark { get; set; }
        /// <summary>
        /// 目标人无图片都好分开
        /// </summary>
        public string UserPics { get; set; }

        public string SearchType { get; set; }

        public string Id { get; set; }
    }

    public enum SearchType {
        video,
        image,
        notice,

    }
}
