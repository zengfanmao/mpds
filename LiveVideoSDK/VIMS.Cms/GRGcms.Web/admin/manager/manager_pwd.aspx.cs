using System;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;

namespace GRGcms.Web.admin.manager
{
    public partial class manager_pwd : Web.UI.ManagePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                Model.user model = GetAdminInfo();
                ShowInfo(model.ID);
            }
        }

        #region 赋值操作=================================
        private void ShowInfo(int _id)
        {
            BLL.user bll = new BLL.user();
            Model.user model = bll.GetModel(_id);
            txtAvatar.Text = model.uHeadPortrait;
            txtUserName.Text = model.uCode;
            txtRealName.Text = model.uName;
            txtTelephone.Text = model.uTel;
            txtEmail.Text =string.Empty;
        }
        #endregion

        //保存
        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            BLL.user bll = new BLL.user();
            Model.user model = GetAdminInfo();

            if (DESEncrypt.Encrypt(txtOldPassword.Text.Trim(), model.uSalt) != model.uPassword)
            {
                JscriptMsg("旧密码不正确！", "");
                return;
            }
            if (txtPassword.Text.Trim() != txtPassword1.Text.Trim())
            {
                JscriptMsg("两次密码不一致！", "");
                return;
            }
            model.uPassword = DESEncrypt.Encrypt(txtPassword.Text.Trim(), model.uSalt);
            model.uHeadPortrait = txtAvatar.Text.Trim();
            model.uName = txtRealName.Text.Trim();
            model.uTel = txtTelephone.Text.Trim();
            //model. = txtEmail.Text.Trim();

            if (!bll.Update(model))
            {
                JscriptMsg("保存过程中发生错误！", "");
                return;
            }
            Session[DTKeys.SESSION_ADMIN_INFO] = null;
            JscriptMsg("密码修改成功！", "manager_pwd.aspx");
        }
    }
}