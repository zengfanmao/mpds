using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class ApiParams
    {
        /// <summary>
        ///登录账号
        /// </summary>
        public string LoginName { get; set; }
        /// <summary>
        /// 登录密码
        /// </summary>
        public string UserPwd { get; set; }
        public string UserOldPwd { get; set; }
        public string UserId { get; set; }
        public string UserName { get; set; }
        public string UserCode { get; set; }
        public string UserCodes { get; set; }
        public string MsgText { get; set; }
        public string ApiToken { get; set; }
        public string MsgType { get; set; }
        public string MsgContent { get; set; }
        public string NoticeType { get; set; }
        public string Notice { get; set; }
        public string MsgFromUserCode { get; set; }
        public string MsgToUserCode { get; set; }
        public string FromUserName { get; set; }
        public string CaseId { get; set; }
        public string CaseCode { get; set; }
        public string Take { get; set; }
        public string Skip { get; set; }
        public string FileSize { get; set; }
        public string RtmpUrl { get; set; }

        public string MsgFile { get; set; }
        public string LiveId { get; set; }
        public string LiveState { get; set; }

        public string VideoTime { get; set; }
        public string AudioTime { get; set; }

        public string MaxTime { get; set; }

        public string FirstMsgId { get; set; }

        public string StartTime { get; set; }
        public string EndTime { get; set; }

        public string PicCode { get; set; }
        /// <summary>
        /// 发送人位置纬度
        /// </summary>
        public string UserLatitude { get; set; }
        /// <summary>
        /// 发送人位置经度
        /// </summary>
        public string UserLongitude { get; set; }
        /// <summary>
        /// 发送人位置名字
        /// </summary>
       
        public string UserPositionName { get; set; }
        /// <summary>
        /// 目标类别
        /// </summary>
        public string GpsTargetType { get; set; }
        /// <summary>
        /// 设备编号
        /// </summary>
        public string DevCode { get; set; }


        public string SourceType { get; set; }
        public string DeviceCode { get; set; }
        public string DeviceSN { get; set; }

        public string DeviceId { get; set; }

        public string AppType { get; set; }


        public string AppVersionCode { get; set; }
        
        public string AppApiUrl { get; set; }


        /// <summary>
        /// 消息来源,是Person还是Group
        /// </summary>
        public string MsgFromType { get; set; }
        /// <summary>
        /// 消息接收人
        /// </summary>
        public string MsgToCode { get; set; }

        public string DisscusionName { get; set; }
        public string UserMenbers { get; set; }
        public string DisscusionCode { get; set; }
        public string DisscusionUserCode { get; set; }
        /// <summary>
        /// 首帧图片的编号
        /// </summary>
        public string FirstFrameCode { get; set; }

        /// <summary>
        /// 案件状态
        /// </summary>
        public string CaseStatus { get; set; }
        /// <summary>
        /// 案件的名字
        /// </summary>
        public string CaseName { get; set; }

        /// <summary>
        /// 案件类别
        /// </summary>
        public string CaseType { get; set; }

        /// <summary>
        /// 用户角色
        /// </summary>
        public string RName { get; set; }

        /// <summary>
        /// 海拔 单位m
        /// </summary>
        public string Altitude { get; set; }
        /// <summary>
        ///-速度 单位m/s
        /// </summary>
        public string Speed { get; set; }
        /// <summary>
        /// -x轴加速度 单位m/s^2
        /// </summary>
        public string AccelerationX { get; set; }
             /// <summary>
        /// --y轴加速度 单位m/s^2
        /// </summary>
        public string AccelerationY { get; set; }
             /// <summary>
        /// z轴加速度 单位m/s^2
        /// </summary>
        public string AccelerationZ { get; set; }

        /// <summary>
        /// 任务状态
        /// </summary>
        public string TaskStatus { get; set; }

        /// <summary>
        /// 设备类型 如单边、巡逻车、无人机
        /// </summary>
        public string DevType { get; set; }

        /// <summary>
        /// 设备名字
        /// </summary>
        public string DevName { get; set; }

        /// <summary>
        /// 设备领用状态
        /// </summary>
        public string DevStatus { get; set; }

        /// <summary>
        /// 任务执行人可以是多个如hexinag,baiyuanwei
        /// </summary>
        public string MissionUserCodes { get; set; }
        /// <summary>
        /// 任务限制时间单位分钟
        /// </summary>
        public string MissionLimitTime { get; set; }
        /// <summary>
        /// 任务备注
        /// </summary>
        public string MissionRemark { get; set; }

        /// <summary>
        /// 纬度
        /// </summary>

        public string ToLatitude { get; set; }
        /// <summary>
        /// 经度
        /// </summary>
        public string ToLongitude { get; set; }
        /// <summary>
        /// 目标位置地名
        /// </summary>
        public string ToPositionName { get; set; }

        /// <summary>
        /// 任务类型
        /// </summary>
        public string MissionType { get; set; }
        /// <summary>
        /// 到达任务地点距离
        /// </summary>
        public string RouteDistance { get; set; }

        public string SearchType { get; set; }

        public string SearchText { get; set; }


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

        public string NoticeId { get; set; }
        public string virtualId { get; set; }


        public string CollectionId { get; set; }
        public string MessageId { get; set; }
        public string CollectionLabel { get; set; }

        public string CollectionLabelName { get; set; }

        public string MissionId { get; set; }

        public string PersonMissionId { get; set; }

		public string Duration { get; set; }
		
        public string dCode { get; set; }
        public int PageNum { get; set; }
        public int PageSize { get; set; }

        public Int32 second { get; set; }

        public string userAction { get; set; }

        public string pdtSSI { get; set; }
        public string type { get; set; }
        public string Isdefault { get; set; }
        public string Isrank { get; set; }
        public string rankNo { get; set; }
        public string rankName { get; set; }
        public string UserStatus { get; set; }
        public string status { get; set; }
        public string clazz { get; set; }
        public string deptId { get; set; }
        public string deptName { get; set; }
        public string relativegroupid { get; set; }
        public string relativegroup { get; set; }
    }
}
