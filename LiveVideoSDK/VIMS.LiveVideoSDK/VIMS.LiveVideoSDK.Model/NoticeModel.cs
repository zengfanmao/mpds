using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class NoticeModel
    {
        /// <summary>
        /// 消息Id
        /// </summary>
        public string NoticeId { get; set; }
        /// <summary>
        /// 消息内容
        /// </summary>
        public string Notice { get; set; }
        /// <summary>
        /// 推送时间
        /// </summary>
        public string NoticeTime { get; set; }
        /// <summary>
        /// 消息类别，这个跟推送消息类别不是一个意思的
        /// </summary>
        public string NoticeType { get; set; }
        /// <summary>
        /// 用户编号
        /// </summary>
        public string UserCode { get; set; }
        /// <summary>
        /// 发通告人的姓名
        /// </summary>
        public string UserName { get; set; }
        /// <summary>
        /// 案件编号
        /// </summary>
        public string CaseCode { get; set; }
        /// <summary>
        /// 案件名字
        /// </summary>
        public string CaseName { get; set; }
        /// <summary>
        /// 相关图片
        /// </summary>
        public string UserPics { get; set; }


        /// <summary>
        /// 位置精度
        /// </summary>
        public string PositionLatitude { get; set; }
        /// <summary>
        /// 位置经度
        /// </summary>
        public string PositionLongitude { get; set; }
        /// <summary>
        /// 位置备注
        /// </summary>
        public string PositionRemark { get; set; }

    }
    /// <summary>
    /// 公告类型
    /// </summary>
    public enum NoticeType {
        /// <summary>
        /// 个人公告
        /// </summary>
        person,
        /// <summary>
        /// 文字公告
        /// </summary>
        text,
        /// <summary>
        ///  地图公告
        /// </summary>
        map
    }
}
