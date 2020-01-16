using System;
using System.Collections.Generic;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;

using VIMS.Services.SystemManagement;

namespace VIMS.Web.Controllers
{
    public class LoginController : Controller
    {
        private readonly IUserService _userService;

        public LoginController(IUserService userService)
        {
            this._userService = userService;
        }

        public ActionResult Index()
        {
            //查看是否需要直接进入案情直播界面
            if (Request.Url.ToString().Contains("caseId=")
                || Request.Url.ToString().Contains("userName=")
               || Request.Url.ToString().Contains("password="))
            {
                string caseId = Request.QueryString["caseId"];
                string userName = Request.QueryString["userName"];
                string password = Request.QueryString["password"];
                if (!string.IsNullOrEmpty(caseId)
                    && !string.IsNullOrEmpty(userName)
                    && !string.IsNullOrEmpty(password))
                {
                    string viewStr = "/CaseLive/CaseCommunication?caseId=" + caseId + "&userName=" + userName + "&password=" + password;
                    return Redirect(viewStr);
                }
                else
                {
                    string viewStr = "/CaseLive/CaseCommunicationError";
                    return Redirect(viewStr);
                }
            }

            return View();
        }

        /// <summary>
        /// 用户登录。
        /// </summary>
        /// <param name="userCode">用户名。</param>
        /// <param name="password">密码。</param>
        /// <returns></returns>
        [HttpPost, AllowAnonymous]
        public JsonResult Login(string userCode, string password)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                string md5Password = JQSoft.RuntimeUtils.MD5(password);
                string userId = this._userService.ValidateUser(userCode, md5Password);
                if (!string.IsNullOrEmpty(userId))
                {
                    FormsAuthentication.SetAuthCookie(userId, false);
                    //Session["UseInfo"] = userService.GetUserInfo(userId);
                    //Session["Roles"] = "Admin";

                    //this.SetUserCookie(new UserCookie
                    //{
                    //    UserId = userId.ToString(),
                    //    IsGuest = true
                    //});
                    ajaxResult.IsSuccess = true;
                }
                else
                {
                    ajaxResult.IsSuccess = false;
                    ajaxResult.Msg = "用户名或密码输入错误！";
                }
            }
            catch (Exception e)
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "登录失败！" + e.Message;
            }
            return Json(ajaxResult);
        }

        public ActionResult Logout()
        {
            Session.Abandon();
            FormsAuthentication.SignOut();
            return Redirect(FormsAuthentication.LoginUrl);
        }

        //private void SetUserCookie(UserCookie userCookie)
        //{
        //    if (userCookie != null)
        //    {
        //        HttpCookie cookie = new HttpCookie("Np.Ivl.v1.1510.User")
        //        {
        //            HttpOnly = true,
        //            Value = userCookie.ToString(),
        //            Expires = DateTime.Now.AddHours(2160.0)
        //        };
        //        base.Response.Cookies.Remove("Np.Ivl.v1.1510.User");
        //        base.Response.Cookies.Add(cookie);
        //    }
        //}
    }
}
