using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class MissionModel
    {
        /// <summary>
        /// 任务id
        /// </summary>
        public string messionId { get; set; }
        /// <summary>
        /// 案件编号
        /// </summary>
        public string caseCode { get; set; }
        /// <summary>
        /// 任务创建时间
        /// </summary>
        public string createdTime { get; set; }
        /// <summary>
        ///  任务总的状态
        /// </summary>
        public Nullable<int> missionStatus { get; set; }
        /// <summary>
        /// 任务限制时间
        /// </summary>
        public Nullable<int> missionLimiTime { get; set; }
        /// <summary>
        /// 目标地点
        /// </summary>
        public string toPositionName { get; set; }
        /// <summary>
        /// 目标纬度
        /// </summary>
        public Nullable<decimal> toPositionLatitude { get; set; }
        /// <summary>
        /// 目标经度
        /// </summary>
        public Nullable<decimal> toPositionLongitude { get; set; }
        /// <summary>
        /// 任务备注
        /// </summary>
        public string missionRemark { get; set; }
        /// <summary>
        /// 距离
        /// </summary>
        public Nullable<decimal> routeDistance { get; set; }
        /// <summary>
        ///  创建人
        /// </summary>
        public string createdUserCode { get; set; }
        public string createdUserName { get; set; }

        /// <summary>
        /// 创建人的位置
        /// </summary>
        public string createdPositionName { get; set; }
        /// <summary>
        /// 创建人的纬度
        /// </summary>
        public Nullable<decimal> createdPositionLatitude { get; set; }
        /// <summary>
        /// 创建人的经度
        /// </summary>
        public Nullable<decimal> createdPositionLongitude { get; set; }
        /// <summary>
        /// 任务类型
        /// </summary>
        public string missionType { get; set; }
        /// <summary>
        /// 任务完成时间
        /// </summary>
        public Nullable<System.DateTime> finishTime { get; set; }
        /// <summary>
        /// 任务分配id
        /// </summary>
        public string messionPersonId { get; set; }
        /// <summary>
        /// 分配任务的时间
        /// </summary>
        public Nullable<System.DateTime> messioPersonCreatedTime { get; set; }
        /// <summary>
        /// 个人任务的状态
        /// </summary>
        public Nullable<int> missionPersonStatus { get; set; }
        /// <summary>
        /// 个人任务完成的时间
        /// </summary>
        public Nullable<System.DateTime> persionFinishTime { get; set; }
        /// <summary>
        /// 任务完成地点
        /// </summary>
        public string personFinishPositionName { get; set; }
        /// <summary>
        /// 任务完成纬度
        /// </summary>
        public Nullable<decimal> personFinishPositionLatitude { get; set; }
        /// <summary>
        /// 任务完成经度
        /// </summary>
        public Nullable<decimal> personFinishPositionLongitude { get; set; }
        /// <summary>
        /// 任务执行人
        /// </summary>
        public string userCode { get; set; }

        /// <summary>
        /// 执行任务人的头像
        /// </summary>
        public string headPortrait { get; set; }

        /// <summary>
        /// 执行任务人的职位
        /// </summary>
        public string userDuty { get; set; }


        /// <summary>
        /// 性别
        /// </summary>
        public string userSex { get; set; }

        /// <summary>
        /// 角色的名字
        /// </summary>
        public string roleName { get; set; }

        /// <summary>
        /// 任务执行人的名字
        /// </summary>
        public string userName { get; set; }


        public Nullable<System.DateTime> _createdTime { get; set; }

    }
}
