using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    /// <summary>
    /// 个人消息列表
    /// </summary>
    public  class MsgList
    {
        /// <summary>
        /// 消息id
        /// </summary>
        public string MsgId { get; set; }
        /// <summary>
        /// 消息来源,是Person还是Group
        /// </summary>
        public string MsgFromType { get; set; }
        /// <summary>
        /// 消息接收人
        /// </summary>
        public string MsgToCode { get; set; }
        /// <summary>
        /// 案件编号
        /// </summary>
        public string CaseCode { get; set; }
        /// <summary>
        /// 案件名字
        /// </summary>
        public string CaseName { get; set; }
        /// <summary>
        /// 发消息时间
        /// </summary>
        public string MsgTime { get; set; }
        /// <summary>
        /// 消息类型
        /// </summary>
        public string MsgType { get; set; }


        public string SendUserCode { get; set; }

        /// <summary>
        /// 发送人名字
        /// </summary>
        public string SendUserName { get; set; }

        /// <summary>
        /// 发送人头像
        /// </summary>
        public string SendUserHeadPortrait { get; set; }

        /// <summary>
        /// msgAbstract 消息内容
        /// </summary>
        public object MsgContent { get; set; }

        /// <summary>
        /// 消息具体的文件，列如音频、视频、图片
        /// </summary>
        public string MsgFile { get; set; }
        public string MsgCount { get; set; }
        public Nullable<System.DateTime> CreatedTime { get; set; }

        /// <summary>
        /// PersonGroup 组合
        /// </summary>
        public string PersonGroup { get; set; }


        public string OtherUserName { get; set; }
        public string OtherUserCode { get; set; }
        public string OtherUserHeadPortrait { get; set; }

        public string MsgGroupName { get; set; }
        public string MsgGroupCode { get; set; }


        public string CreatedMsgGroupUserName { get; set; }
        public string CreatedMsgGroupUserCode { get; set; }


        public Nullable<System.DateTime> _OrderTime { get; set; }
    }
}
