using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using VIMS.LiveVideoSDK.Model;
using VIMS.LiveVideoSDK.Bll.Entity;
namespace VIMS.LiveVideoSDK.Bll
{
    public  interface IBaseManage
    {
   
        #region 注登陆
        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="userName">登录账号</param>
        /// <param name="userPwd">登陆密码</param>
        /// <returns></returns>
        ResultEx Login(string LoginName, string UserPwd);

        #endregion

        #region 发送消息
        /// <summary>
        /// 发送普通消息
        /// </summary>
        /// <param name="ToUserId"></param>
        /// <param name="MsgType"></param>
        /// <param name="MsgConent"></param>
        /// <returns></returns>
        //ResultEx PushMsgCase(string FromUserId, string FromUserName, string CaseId, string MsgType, string MsgConent, string VideoTime, string AudioTime);



        #endregion

        List<CaseModel> GetMyCases(string userId, string skip, string take);


        ResultEx AddUser(tbUser model);
    }
}
