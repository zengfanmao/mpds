using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class LivingModel
    {
        /// <summary>
        /// 直播id
        /// </summary>
        public string LiveId { get; set; }
        /// <summary>
        /// 直播地址
        /// </summary>
        public string RtmpUrl { get; set; }
        /// <summary>
        /// 直播状态
        /// </summary>
        public string LivingState { get; set; }
        /// <summary>
        /// 发布人的用户名字
        /// </summary>
        public string PublishUserName { get; set; }
        /// <summary>
        /// 发布的用户编号
        /// </summary>
        public string PublishUserCode { get; set; }
        /// <summary>
        /// 发布的案件代号
        /// </summary>
        public string PublishCaseCode { get; set; }
        /// <summary>
        /// 发布案件名字
        /// </summary>
        public string PublishCaseName { get; set; }
        /// <summary>
        /// 发布人部门
        /// </summary>
        public string PublishUserDepartment { get; set; }
        /// <summary>
        /// 发布人职位
        /// </summary>
        public string PublishUserDuty { get; set; }

        public string PublishUserHeadPortrait { get; set; }
        /// <summary>
        /// 发布时间
        /// </summary>
        public string PublishTime { get; set; }

        public Nullable<System.DateTime> createdTime { get; set; }

        /// <summary>
        /// 视频名字累加
        /// </summary>
        public string Cumulative { get; set; }


        /// <summary>
        /// 发布人所在纬度
        /// </summary>
        public string PublishUserLatitude { get; set; }
        /// <summary>
        /// 发布人所在经度
        /// </summary>
        public string PublishUserLongitude { get; set; }
        /// <summary>
        /// 发布人所在位置的名字
        /// </summary>
        public string PublishUserPositionName { get; set; }

        /// <summary>
        /// 首张图片的地址
        /// </summary>
        public string FirstFramePicUrl { get; set; }
    }
}
