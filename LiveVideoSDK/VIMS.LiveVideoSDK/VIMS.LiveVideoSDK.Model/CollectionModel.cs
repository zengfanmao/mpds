using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class CollectionModel
    {
        /// <summary>
        /// 发送人
        /// </summary>
        public string SendUserId { get; set; }
        /// <summary>
        /// 发送人名字
        /// </summary>
        public string SendUserName { get; set; }

        public string SendUserCode { get; set; }


        /// <summary>
        /// 发送人纬度
        /// </summary>
        public string SendUserLatitude { get; set; }
        /// <summary>
        /// 发送人经度
        /// </summary>
        public string SendUserLongitude { get; set; }
        /// <summary>
        /// 发送人位置名字
        /// </summary>
        public string SendPositionName { get; set; }
        /// <summary>


        /// <summary>
        /// 发送人职位
        /// </summary>
        public string SendUserDuty { get; set; }
        /// <summary>
        /// 发送人头像
        /// </summary>
        public string SendUserHeadPortrait { get; set; }
        /// <summary>
        /// 发送人部门
        /// </summary>
        public string SendUserDepartment { get; set; }
        /// <summary>
        /// 发送人部门编号
        /// </summary>
        public string SendUserDepartmentCode { get; set; }
        /// <summary>
        /// 消息编号
        /// </summary>
        public string MsgId { get; set; }

        /// <summary>
        /// 案件id
        /// </summary>
        public string CaseId { get; set; }
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
        /// <summary>
        /// msgAbstract 消息内容
        /// </summary>
        public object MsgContent { get; set; }

        /// <summary>
        /// 消息具体的文件，列如音频、视频、图片
        /// </summary>
        public string MsgFile { get; set; }


        public Nullable<System.DateTime> CreatedTime { get; set; }
        public Nullable<System.DateTime> _MsgTime { get; set; }

        /// <summary>
        /// 消息来源,是Person还是Group
        /// </summary>
        public string MsgFromType { get; set; }
        /// <summary>
        /// 消息接收人
        /// </summary>
        public string MsgToCode { get; set; }
        /// <summary>
        /// 比如A-B B-A
        /// </summary>
        public string PersonGroup { get; set; }
        /// <summary>
        /// 比如A-B B-A
        /// </summary>
        public string PersonGroupName { get; set; }
        /// <summary>
        /// 讨论组消息
        /// </summary>
        public string GroupName { get; set; }



        public Nullable<System.DateTime> _CollectionTime { get; set; }
        public string CollectionTime { get; set; }
        public string Reamrk { get; set; }
        public string MessageId { get; set; }

        public string Id { get; set; }

    }
}
