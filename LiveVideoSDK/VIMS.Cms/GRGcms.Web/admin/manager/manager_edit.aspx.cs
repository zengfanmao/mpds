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
    public partial class manager_edit : Web.UI.ManagePage
    {
        string defaultpassword = "0|0|0|0"; //默认显示密码
        private string action = DTEnums.ActionEnum.Add.ToString(); //操作类型
        private int id = 0;
        private string downloadPrefix = ConfigurationManager.AppSettings["apkdownload"].ToString();

        protected void Page_Load(object sender, EventArgs e)
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
                if (!new BLL.user().Exists(this.id))
                {
                    JscriptMsg("记录不存在或已被删除！", "back");
                    return;
                }
            }
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("manager_list", DTEnums.ActionEnum.View.ToString()); //检查权限
                Model.user model = GetAdminInfo(); //取得管理员信息
                TreeBind(); //绑定树级机构
                RoleBind(ddlRoleId, model.roleType);
                GroupBind();
                if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
                {
                    ShowInfo(this.id);
                } else
                {
                    txtPassword.Attributes["value"] = txtPassword.Text;
                    txtPassword1.Attributes["value"] = txtPassword1.Text;
                }
            }
        }

        #region 角色类型=================================
        private void RoleBind(DropDownList ddl, int role_type)
        {
            BLL.role bll = new BLL.role();
            DataTable dt = bll.GetList("").Tables[0];

            ddl.Items.Clear();
            ddl.Items.Add(new ListItem("请选择角色...", ""));
            foreach (DataRow dr in dt.Rows)
            {
                if (Convert.ToInt32(dr["role_type"]) >= role_type)
                {
                    ddl.Items.Add(new ListItem(dr["role_name"].ToString(), dr["id"].ToString()));
                }
            }
        }
        #endregion
        private void TreeBind()
        {
            BLL.dept bll = new BLL.dept();
            DataTable dt = bll.GetList("");

            this.ddlDept.Items.Clear();
            this.ddlDept.Items.Add(new ListItem("暂无部门", ""));
            foreach (DataRow dr in dt.Rows)
            {
                string Id = dr["dCode"].ToString();
                int ClassLayer = int.Parse(dr["class_layer"].ToString());
                string Title = dr["dName"].ToString().Trim();
                string abbr = dr["dAbbr"].ToString().Trim();

                if (ClassLayer == 1)
                {
                    this.ddlDept.Items.Add(new ListItem(Title + " | " + abbr, Id));
                }
                else
                {
                    Title = "├ " + Title + " | " + abbr;
                    Title = Utils.StringOfChar(ClassLayer - 1, "　") + Title;
                    this.ddlDept.Items.Add(new ListItem(Title, Id));
                }
            }
        }
        private void GroupBind()
        {
            BLL.group bll = new BLL.group();
            DataTable dt = bll.GetList(0,"1=1","createdTime desc").Tables[0];

            /*
            ddlGroup.Items.Clear();
            ddlGroup.Items.Add(new ListItem("请选择组名...", ""));
            foreach (DataRow dr in dt.Rows)
            {
                ddlGroup.Items.Add(new ListItem(dr["discussionName"].ToString(), dr["discussionCode"].ToString()));
            }
            */
        }
        #region 赋值操作=================================
        private void ShowInfo(int _id)
        {
            BLL.user bll = new BLL.user();
            Model.user model = bll.GetModel(_id);
            ddlRoleId.SelectedValue = model.roleid.ToString();

            //部门        
            ddlDept.SelectedValue = model.dCode.ToString();
            ddlSex.SelectedValue = model.uSex;
            ddlAccountType.SelectedValue = model.accountType; 
            ddlDuty.SelectedValue = model.uDuty;
            txtPcNum.Text= model.pcNum;
            txtDeviceId.Text = model.deviceid.ToString();
            txtDeviceESN.Text = model.deviceESN;
            deviceType.SelectedValue = model.devicetype;
            //ddlGroup.SelectedValue = model.groupid;
            ddlStatus.SelectedValue = model.status;
            if (model.isDel == 0)
            {
                cbIsLock.Checked = true;
            }
            else
            {
                cbIsLock.Checked = false;
            }
          
            txtUserName.Text = model.uCode;
            txtUserName.ReadOnly = true;
            txtUserName.Attributes.Remove("ajaxurl");
            if (!string.IsNullOrEmpty(model.uPassword))
            {
                txtPassword.Attributes["value"] = txtPassword1.Attributes["value"] = defaultpassword;
            }
            txtAvatar.Text = model.uHeadPortrait;
            txtRealName.Text = model.uName;
            txtTelephone.Text = model.uTel;
           
        }
        #endregion

        #region 增加操作=================================
        private bool DoAdd()
        {
            Model.user model = new Model.user();
            BLL.user bll = new BLL.user();
            model.roleid = int.Parse(ddlRoleId.SelectedValue);
            model.roleType = new BLL.role().GetModel(model.roleid).role_type;
            model.rName = ddlRoleId.SelectedItem.Text.Trim();
            if (bll.ExitsDeviceId(int.Parse(this.txtDeviceId.Text.Trim())))
            {
                JscriptMsg("移动台号不能重复！", "");
                return false;
            }
            
            //部门
            if (!string.IsNullOrEmpty(ddlDept.SelectedValue))
            {
                model.dCode = ddlDept.SelectedValue;
                model.uUnitCode = ddlDept.SelectedValue;
                model.uBelong = "";
                string dept = ddlDept.SelectedItem.Text.Replace("├ ", "").Trim();
                model.dName = dept.Split('|')[0].Trim();
                model.uDepartment = dept.Split('|')[1].Trim();
            }
            model.uSex = ddlSex.SelectedValue.Trim();
            model.accountType = ddlAccountType.SelectedValue.Trim();
            model.uDuty = ddlDuty.SelectedValue.Trim();
            model.pcNum = txtPcNum.Text.Trim();
            model.devicetype = deviceType.SelectedValue;
            try
            {
                model.deviceid = Convert.ToInt32(txtDeviceId.Text.Trim());
            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(manager_edit), "移动台号只能为数字", ex);
                return false;
            }
            
            model.deviceESN = txtDeviceESN.Text.Trim();
            //部门
            /*
            if (!string.IsNullOrEmpty(ddlGroup.SelectedValue))
            {
                model.groupid = ddlGroup.SelectedValue;
                model.groupName = ddlGroup.SelectedItem.Text.Trim();
            }
            */
           
           
            if (cbIsLock.Checked == true)
            {
                model.isDel = 0;
                model.uIsActive = 0;
            }
            else
            {
                model.isDel = 1;
                model.uIsActive = 1;
            }
            //检测用户名是否重复
            if (bll.Exists(txtUserName.Text.Trim()))
            {
                return false;
            }
            model.uCode = txtUserName.Text.Trim();
            //获得6位的salt加密字符串
            model.uSalt = Utils.GetCheckCode(6);
            //以随机生成的6位字符串做为密钥加密
            model.uPassword = DESEncrypt.Encrypt(txtPassword.Text.Trim(), model.uSalt);
            model.uHeadPortrait = downloadPrefix + txtAvatar.Text.Trim();
            model.uName = txtRealName.Text.Trim();
            model.uTel = txtTelephone.Text.Trim();
            model.Createtime = DateTime.Now;
            model.status = ddlStatus.SelectedValue.Trim();
            model.purpose = purpose.SelectedValue;
            if (bll.Add(model) > 0)
            {
                AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "添加用户:" + model.uCode); //记录日志
                return true;
            }
            return false;
        }
        #endregion

        #region 修改操作=================================
        private bool DoEdit(int _id)
        {
            bool result = false;
            BLL.user bll = new BLL.user();
            Model.user model = bll.GetModel(_id);

            model.roleid = int.Parse(ddlRoleId.SelectedValue);
            model.roleType = new BLL.role().GetModel(model.roleid).role_type;
            model.rName = ddlRoleId.SelectedItem.Text.Trim();
            //部门
            if (!string.IsNullOrEmpty(ddlDept.SelectedValue))
            {
                model.dCode = ddlDept.SelectedValue;
                model.uUnitCode = ddlDept.SelectedValue;
                model.uBelong = "";
                string dept = ddlDept.SelectedItem.Text.Replace("├ ", "").Trim();
                model.dName = dept.Split('|')[0].Trim();
                model.uDepartment = dept.Split('|')[1].Trim();
            }
            model.uSex = ddlSex.SelectedValue.Trim();
            model.accountType = ddlAccountType.SelectedValue.Trim();
            model.uDuty = ddlDuty.SelectedValue.Trim();
            model.pcNum = txtPcNum.Text.Trim();
            model.devicetype = deviceType.SelectedValue;
            model.purpose = purpose.SelectedValue;
            try
            {
                model.deviceid = Convert.ToInt32(txtDeviceId.Text.Trim());
            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(manager_edit), "移动台号只能为数字", ex);
                return false;
            }
            model.deviceESN = txtDeviceESN.Text.Trim();
            //model.status = ddlStatus.SelectedValue.Trim();
            //部门
            /*
            if (!string.IsNullOrEmpty(ddlGroup.SelectedValue))
            {
                model.groupid = ddlGroup.SelectedValue;
                model.groupName = ddlGroup.SelectedItem.Text.Trim();
            }
            */

            //判断密码是否更改
            if (txtPassword.Text.Trim() != defaultpassword)
            {
                //获取用户已生成的salt作为密钥加密
                model.uPassword = DESEncrypt.Encrypt(txtPassword.Text.Trim(), model.uSalt);
            }
            model.uHeadPortrait = txtAvatar.Text.Trim();
            if (!model.uHeadPortrait.StartsWith("http"))
            {
                model.uHeadPortrait = downloadPrefix + txtAvatar.Text.Trim();
            }
            model.uName = txtRealName.Text.Trim();
            model.uTel = txtTelephone.Text.Trim();

            if (bll.Update(model))
            {
                AddAdminLog(DTEnums.ActionEnum.Edit.ToString(), "修改用户:" + model.uCode); //记录日志
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
                ChkAdminLevel("manager_list", DTEnums.ActionEnum.Edit.ToString()); //检查权限
                if (!DoEdit(this.id))
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("修改会员信息成功！", "manager_list.aspx");
            }
            else //添加
            {
                ChkAdminLevel("manager_list", DTEnums.ActionEnum.Add.ToString()); //检查权限
                if (!DoAdd())
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("添加会员信息成功！", "manager_list.aspx");
            }
        }

    }
}