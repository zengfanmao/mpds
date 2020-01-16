using System;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;

namespace GRGcms.Web.admin.manager
{
    public partial class dept_edit : Web.UI.ManagePage
    {
        private string action = DTEnums.ActionEnum.Add.ToString(); //操作类型
        private int id = 0;

        protected void Page_Load(object sender, EventArgs e)
        {
            string _action = DTRequest.GetQueryString("action");
            this.id = DTRequest.GetQueryInt("ID");

            if (!string.IsNullOrEmpty(_action) && _action == DTEnums.ActionEnum.Edit.ToString())
            {
                this.action = DTEnums.ActionEnum.Edit.ToString();//修改类型
                if (this.id == 0)
                {
                    JscriptMsg("传输参数不正确！", "back");
                    return;
                }
                if (!new BLL.dept().Exists(this.id))
                {
                    JscriptMsg("此机构不存在或已被删除！", "back");
                    return;
                }
            }
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("sys_dept", DTEnums.ActionEnum.View.ToString()); //检查权限
                TreeBind(); //绑定树级机构
                if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
                {
                    ShowInfo(this.id);
                }
                else
                {
                    if (this.id > 0)
                    {
                        this.ddlParentId.SelectedValue = this.id.ToString();
                    }
                    txtName.Attributes.Add("ajaxurl", "../../tools/admin_ajax.ashx?action=dept_validate");
                }
            }
        }

        #region 绑定导航菜单=============================
        private void TreeBind()
        {
            BLL.dept bll = new BLL.dept();
            DataTable dt = bll.GetList("");

            this.ddlParentId.Items.Clear();
            this.ddlParentId.Items.Add(new ListItem("无父级机构", "0"));
            foreach (DataRow dr in dt.Rows)
            {
                string Id = dr["dCode"].ToString();
                int ClassLayer = int.Parse(dr["class_layer"].ToString());
                string Title = dr["dName"].ToString().Trim();

                if (ClassLayer == 1)
                {
                    this.ddlParentId.Items.Add(new ListItem(Title, Id));
                }
                else
                {
                    Title = "├ " + Title;
                    Title = Utils.StringOfChar(ClassLayer - 1, "　") + Title;
                    this.ddlParentId.Items.Add(new ListItem(Title, Id));
                }
            }
        }
        #endregion

        

        #region 赋值操作=================================
        private void ShowInfo(int _id)
        {
            BLL.dept bll = new BLL.dept();
            Model.dept model = bll.GetModel(_id);

            ddlParentId.SelectedValue = model.dFather.ToString();
            txtCode.Text = model.dCode;
            txtName.Text = model.dName;
            txtName.Attributes.Add("ajaxurl", "../../tools/admin_ajax.ashx?action=dept_validate&old_name=" + Utils.UrlEncode(model.dName));
            txtName.Focus(); //设置焦点，防止JS无法提交
           
        }
        #endregion

        #region 增加操作=================================
        private bool DoAdd()
        {
            try
            {
                Model.dept model = new Model.dept();
                BLL.dept bll = new BLL.dept();

                model.dCode = txtCode.Text.Trim();
                model.dName = txtName.Text.Trim();
                model.sort_id = int.Parse(txtSortId.Text.Trim());
                model.isDel = 0;
                model.dabbr = txtAbbrName.Text.Trim();

                ////if (cbIsLock.Checked == true)
                ////{
                ////    model.status = 1;
                ////}
                model.dFather = ddlParentId.SelectedValue;
                model.farther = ddlParentId.SelectedItem.Text.Trim();
            
                if (bll.Add(model) > 0)
                {
                    AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "添加组织机构:" + model.dName); //记录日志
                    return true;
                }
            }
            catch
            {
                return false;
            }
            return false;
        }
        #endregion

        #region 修改操作=================================
        private bool DoEdit(int _id)
        {
            try
            {
                BLL.dept bll = new BLL.dept();
                Model.dept model = bll.GetModel(_id);

               
                model.dCode = txtCode.Text.Trim();
                model.dName = txtName.Text.Trim();
                model.sort_id = int.Parse(txtSortId.Text.Trim());
                model.dFather = ddlParentId.SelectedValue;
                model.farther = ddlParentId.SelectedItem.Text.Trim();
                model.dabbr = txtAbbrName.Text.Trim();

                if (bll.Update(model))
                {
                    AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "修改组织机构:" + model.dName); //记录日志
                    return true;
                }
            }
            catch
            {
                return false;
            }
            return false;
        }
        #endregion

        //保存
        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            if (action == DTEnums.ActionEnum.Edit.ToString()) //修改
            {
                ChkAdminLevel("sys_dept", DTEnums.ActionEnum.Edit.ToString()); //检查权限
                if (!DoEdit(this.id))
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("修改成功！", "dept_list.aspx", "parent.loadMenuTree");
            }
            else //添加
            {
                ChkAdminLevel("sys_dept", DTEnums.ActionEnum.Add.ToString()); //检查权限
                if (!DoAdd())
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("添加成功！", "dept_list.aspx", "parent.loadMenuTree");
            }
        }

    }
}