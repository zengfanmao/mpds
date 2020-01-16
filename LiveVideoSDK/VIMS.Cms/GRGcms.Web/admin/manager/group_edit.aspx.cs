using System;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;

namespace GRGcms.Web.admin.manager
{
    public partial class groud_edit : Web.UI.ManagePage
    {

        private string action = DTEnums.ActionEnum.Add.ToString(); //操作类型
        private int id = 0;
        protected string relatedGroup = "";
        protected string relatedGroupTxt = "";

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
                if (!new BLL.group().Exists(this.id))
                {
                    JscriptMsg("记录不存在或已被删除！", "back");
                    return;
                }
            }
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("manager_group", DTEnums.ActionEnum.View.ToString()); //检查权限
                Model.user model = GetAdminInfo(); //取得管理员信息
                TreeBind(); //绑定树级机构
                GroupBind(DTEnums.GroupTypeEnum.PDT.ToString());
                if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
                {
                    ShowInfo(this.id);
                }
            }
        }

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

                if (ClassLayer == 1)
                {
                    this.ddlDept.Items.Add(new ListItem(Title, Id));
                }
                else
                {
                    Title = "├ " + Title;
                    Title = Utils.StringOfChar(ClassLayer - 1, "　") + Title;
                    this.ddlDept.Items.Add(new ListItem(Title, Id));
                }
            }
        }
        private void GroupBind(string type)
        {
            BLL.group bll = new BLL.group();
            DataTable dt = bll.GetList(0, "1=1 and type='"+type+"'", "createdTime desc").Tables[0];
            int count = dt.Rows.Count;
            this.groupList.DataSource = dt.DataSet;
            this.groupList.DataBind();
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
            BLL.group bll = new BLL.group();
            Model.group model = bll.GetModel(_id);


            txtGroupName.Text = model.discussionName;
            txtCode.Text = model.discussionCode;
            ddlDept.SelectedValue = model.deptid;
            ddtType.SelectedValue = model.type;
            ddtClass.SelectedValue = model.clazz.ToString();

            this.relatedGroup = model.relativegroupid;
            this.relatedGroupTxt = model.relativegroup;
            //ddlGroup.SelectedValue = model.relativegroupid;
            if (model.isDel == 0)
            {
                cbIsLock.Checked = true;
            }
            else
            {
                cbIsLock.Checked = false;
            }

            //txtUserName.Text = model.uCode;
            //txtUserName.ReadOnly = true;
            //txtUserName.Attributes.Remove("ajaxurl");
            //txtAvatar.Text = model.uHeadPortrait;
            //txtRealName.Text = model.uName;
            //txtTelephone.Text = model.uTel;

        }
        #endregion

        #region 增加操作=================================
        private bool DoAdd()
        {
            Model.group model = new Model.group();
            BLL.group bll = new BLL.group();
            //检测组名是否重复
            if (bll.Exists(txtCode.Text.Trim(), txtGroupName.Text.Trim(), ddtType.SelectedValue.Trim()))
            {
                JscriptMsg("组名/组编号不能重复！", "");
                return false;
            }
            model.discussionCode = txtCode.Text.Trim();
            model.discussionName = txtGroupName.Text.Trim();
            model.type = ddtType.SelectedValue.Trim();
            model.clazz = Convert.ToInt32(ddtClass.SelectedValue.Trim());
            if (cbIsLock.Checked == true)
            {
                model.isDel = 0;
                model.status = 0;
            }
            else
            {
                model.isDel = 1;
                model.status = 1;
            }
            //部门
            if (!string.IsNullOrEmpty(ddlDept.SelectedValue))
            {
                model.deptid = ddlDept.SelectedValue;
                model.dept = ddlDept.SelectedItem.Text.Replace("├ ", "").Trim();

            }
            Model.user uModel = GetAdminInfo(); //取得管理员信息
            model.createdUserCode = uModel.uCode;
            model.createdUserName = uModel.uName;
            model.createdTime = DateTime.Now;
            model.updatetime = DateTime.Now;

            //融合组
            if (ddtType.SelectedValue == "APP")
            {
                string selectedGroup = Request.Form.Get("selectedGroup");
                if (!string.IsNullOrEmpty(selectedGroup))
                {
                    model.relativegroupid = selectedGroup;
                    //model.relativegroup = ddlGroup.SelectedItem.Text.Trim();
                    model.relativegroup = Request.Form.Get("groupText").ToString();
                }
            }
            
            if (bll.Add(model) > 0)
            {
                AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "添加通话组:" + model.discussionName); //记录日志
                return true;
            }
            return false;
        }
        #endregion

        #region 修改操作=================================
        private bool DoEdit(int _id)
        {
            bool result = false;
            BLL.group bll = new BLL.group();
            Model.group model = bll.GetModel(_id);
            //检测组名是否重复
            if (model.discussionCode != txtCode.Text.Trim() || model.discussionName != txtGroupName.Text.Trim())
            {
                if (bll.Exists(txtCode.Text.Trim(), txtGroupName.Text.Trim(), ddtType.SelectedValue.Trim()))
                {
                    JscriptMsg("组名/组编号不能重复！", "");
                    return false;
                }
            }
            model.discussionCode = txtCode.Text.Trim();
            model.discussionName = txtGroupName.Text.Trim();
            model.type = ddtType.SelectedValue.Trim();
            model.clazz = Convert.ToInt32(ddtClass.SelectedValue.Trim());
            if (cbIsLock.Checked == true)
            {
                model.isDel = 0;
                model.status = 0;
            }
            else
            {
                model.isDel = 1;
                model.status = 1;
            }
            //部门
            if (!string.IsNullOrEmpty(ddlDept.SelectedValue))
            {
                model.deptid = ddlDept.SelectedValue;
                model.dept = ddlDept.SelectedItem.Text.Replace("├ ", "").Trim();

            }
            Model.user uModel = GetAdminInfo(); //取得管理员信息
            model.createdUserCode = uModel.uCode;
            model.createdUserName = uModel.uName;
            model.updatetime = DateTime.Now;

            //融合组
            if (ddtType.SelectedValue == "APP")
            {
                string selectedGroup = Request.Form.Get("selectedGroup");
                if (!string.IsNullOrEmpty(selectedGroup))
                {
                    model.relativegroupid = selectedGroup;
                    //model.relativegroup = ddlGroup.SelectedItem.Text.Trim();
                    model.relativegroup = Request.Form.Get("groupText").ToString();
                }
                else
                {
                    model.relativegroupid = null;
                    model.relativegroup = null;
                }
            }


            if (bll.Update(model))
            {
                AddAdminLog(DTEnums.ActionEnum.Edit.ToString(), "修改通话组:" + model.discussionName); //记录日志
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
                ChkAdminLevel("manager_group", DTEnums.ActionEnum.Edit.ToString()); //检查权限
                if (!DoEdit(this.id))
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("修改成功！", "group_list.aspx");
            }
            else //添加
            {
                ChkAdminLevel("manager_group", DTEnums.ActionEnum.Add.ToString()); //检查权限
                if (!DoAdd())
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("添加成功！", "group_list.aspx");
            }
        }

        protected string getRelatedGroup()
        {
            return this.relatedGroupTxt;
        }

        protected Boolean getSelectedGroup(string group)
        {
            return this.relatedGroup.Equals(group);
        }
    }
}