using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class UserModel
    {
        /// <summary>
        /// 登录账号
        /// </summary>
        public string LoginName { get; set; }
        /// <summary>
        /// 登录密码
        /// </summary>
        public string UserPwd { get; set; }
        /// <summary>
        /// 登录秘钥
        /// </summary>
        public string ApiToken { get; set; }
        /// <summary>
        /// 用户姓名
        /// </summary>
        public string UserName { get; set; }
        /// <summary>
        /// 用户电话
        /// </summary>
        public string UserPhone { get; set; }
        /// <summary>
        /// 部门
        /// </summary>
        public string Department { get; set; }
        /// <summary>
        /// 头像地址
        /// </summary>
        public string HeadPortrait { get; set; }
        /// <summary>
        /// 用户uCode
        /// </summary>
        public string UserCode { get; set; }

        /// <summary>
        /// 用户UserId
        /// </summary>
        public string UserId { get; set; }

        /// <summary>
        /// 性别
        /// </summary>
        public string UserSex { get; set; }
        /// <summary>
        /// 职责
        /// </summary>
        public string UserDuty { get; set; }
        /// <summary>
        /// 部门编号
        /// </summary>
        public string DepartmentCode { get; set; }
        /// <summary>
        /// 用户短号
        /// </summary>
        public string UserShortNum { get; set; }

        /// <summary>
        /// 警员编号
        /// </summary>
        public string UserPCNum { get; set; }

        /// <summary>
        /// 角色的名字
        /// </summary>
        public string RoleName { get; set; }
        public string roleId { get; set; }


        public Nullable<System.DateTime> _CreatedTime { get; set; }

        /// <summary>
        /// 羚羊cid
        /// </summary>
        public string LYCID { get; set; }

        public string videoUrl { get; set; }

        public string videoPort { get; set; }

        public string discussionCode { get; set; }

        public string discussionName { get; set; }

        public string dCode { get; set; }
        public string dName { get; set; }

        public string deviceId { get; set; }

        public int page { get; set; }
        public int pageSize { get; set; }
        public string UserStatus { get; set; }
    }
}
