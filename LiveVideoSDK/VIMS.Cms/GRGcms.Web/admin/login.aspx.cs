using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;

namespace GRGcms.Web.admin
{
    public partial class login : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                txtUserName.Text = Utils.GetCookie("DTRememberName");
            }
        }

        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            string userName = txtUserName.Text.Trim();
            string userPwd = txtPassword.Text.Trim();
            string code = txtCode.Text.Trim().ToLower();
            HttpCookie cookie = Request.Cookies["ImageV"];
            string imageCode = cookie.Value.ToLower();

            
            if (!code.Equals(imageCode))
            {
                msgtip.InnerHtml = "验证码不正确，请重新输入";
                return;
            }
            

            if (userName.Equals("") || userPwd.Equals(""))
            {
                msgtip.InnerHtml = "请输入用户名或密码";
                return;
            }
            if (Session["AdminLoginSun"] == null)
            {
                Session["AdminLoginSun"] = 1;
            }
            else
            {
                Session["AdminLoginSun"] = Convert.ToInt32(Session["AdminLoginSun"]) + 1;
            }
            //判断登录错误次数
            if (Session["AdminLoginSun"] != null && Convert.ToInt32(Session["AdminLoginSun"]) > 5)
            {
                msgtip.InnerHtml = "错误超过5次，关闭浏览器重新登录！";
                return;
            }
            BLL.user bll = new BLL.user();
            Model.user model = bll.GetModel(userName, userPwd, true);
            if (model == null)
            {
                msgtip.InnerHtml = "用户名或密码有误，请重试！";
                return;
            }
            Session[DTKeys.SESSION_ADMIN_INFO] = model;
            Session.Timeout = 45;
            //写入登录日志
            Model.sysconfig sysConfig = new BLL.sysconfig().loadConfig();
            if (sysConfig.logstatus > 0)
            {
                new BLL.log().Add(model.uCode, model.uName, DTEnums.ActionEnum.Login.ToString(), "用户登录");
            }
            //写入Cookies
            Utils.WriteCookie("DTRememberName", model.uCode, 14400);
            Utils.WriteCookie("AdminName", "GRGcms", model.uCode);
            Utils.WriteCookie("AdminPwd", "GRGcms", model.uPassword);
            Response.Redirect("index.aspx");
            return;
        }
    }
}