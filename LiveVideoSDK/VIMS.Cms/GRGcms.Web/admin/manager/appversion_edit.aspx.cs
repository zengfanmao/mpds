using System;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;
using System.Configuration;

namespace GRGcms.Web.admin.manager
{
    public partial class appversion_edit : Web.UI.ManagePage
    {

        private string action = DTEnums.ActionEnum.Add.ToString(); //操作类型
        private int id = 0;

        private string downloadPrefix = ConfigurationManager.AppSettings["apkdownload"].ToString();

        protected void Page_Load(object sender, EventArgs ID)
        {
            string _action = DTRequest.GetQueryString("action");
            if (!string.IsNullOrEmpty(_action) && _action == DTEnums.ActionEnum.Edit.ToString())
            {
                this.action = DTEnums.ActionEnum.Edit.ToString();//修改类型
                if (!int.TryParse(Request.QueryString["id"] as string, out this.id))
                {
                    JscriptMsg("传输参数不正确！", "back");
                    return;
                }
                if (!new BLL.appversion().Exists(this.id))
                {
                    JscriptMsg("记录不存在或已被删除！", "back");
                    return;
                }
            }
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("manager_appversion", DTEnums.ActionEnum.View.ToString()); //检查权限
                Model.user model = GetAdminInfo(); //取得管理员信息
                if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
                {
                    ShowInfo(this.id);
                }
            }
        }

       
        #region 赋值操作=================================
        private void ShowInfo(int _id)
        {
            BLL.appversion bll = new BLL.appversion();
            Model.appversion model = bll.GetModel(_id);
            if (model.isDel == 0)
            {
                cbIsLock.Checked = true;
            }
            else
            {
                cbIsLock.Checked = false;
            }
          
            txtAppTitle.Text = model.appTitle;
            txtVersionName.Text = model.appVersionName;
            txtVersionNo.Text = model.appVersionNo.ToString();
            txtAppUrl.Text = model.appDownloadUrl;
            txtFeatures.Text = model.appFeatures;

           
        }
        #endregion

        #region 增加操作=================================
        private bool DoAdd()
        {
            Model.appversion model = new Model.appversion();
            BLL.appversion bll = new BLL.appversion();
           
            if (cbIsLock.Checked == true)
            {
                model.isDel = 0;
            }
            else
            {
                model.isDel = 1;
            }
           
            model.appTitle = txtAppTitle.Text.Trim();
            
            model.appVersionName = txtVersionName.Text.Trim();
            model.appVersionNo =int.Parse(txtVersionNo.Text.Trim());
            model.appFeatures = txtFeatures.Text.Trim();
            model.appPublishTime = DateTime.Now;
            Model.user uModel = GetAdminInfo(); //取得管理员信息
            model.createuser = uModel.uName;
            model.appDownloadUrl = downloadPrefix + txtAppUrl.Text.Trim();
            if (bll.Add(model) > 0)
            {
                AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "添加APP版本:" + model.appVersionName); //记录日志
                return true;
            }
            return false;
        }
        #endregion

        #region 修改操作=================================
        private bool DoEdit(int _id)
        {
            bool result = false;
            BLL.appversion bll = new BLL.appversion();
            Model.appversion model = bll.GetModel(_id);
            model.appTitle = txtAppTitle.Text.Trim();       
            model.appDownloadUrl = downloadPrefix + txtAppUrl.Text.Trim();
            model.appVersionName = txtVersionName.Text.Trim();
            model.appVersionNo = int.Parse(txtVersionNo.Text.Trim());
            model.appFeatures = txtFeatures.Text.Trim();
            if (cbIsLock.Checked == true)
            {
                model.isDel = 0;
            }
            else
            {
                model.isDel = 1;
            }
            if (bll.Update(model))
            {
                AddAdminLog(DTEnums.ActionEnum.Edit.ToString(), "修改APP版本:" + model.appVersionName); //记录日志
                result = true;
            }

            return result;
        }
        #endregion

        //保存
        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
            {
                ChkAdminLevel("manager_appversion", DTEnums.ActionEnum.Edit.ToString()); //检查权限
                if (!DoEdit(this.id))
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("修改成功！", "appversion_list.aspx");
            }
            else //添加
            {
                ChkAdminLevel("manager_appversion", DTEnums.ActionEnum.Add.ToString()); //检查权限
                if (!DoAdd())
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("添加成功！", "appversion_list.aspx");
            }
        }

    }
}