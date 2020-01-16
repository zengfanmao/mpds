using System;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;

namespace GRGcms.Web.admin.manager
{
    public partial class rank_edit : Web.UI.ManagePage
    {

        private string action = DTEnums.ActionEnum.Add.ToString(); //操作类型
        private int id = 0;

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
                if (!new BLL.rank().Exists(this.id))
                {
                    JscriptMsg("记录不存在或已被删除！", "back");
                    return;
                }
            }
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("sys_rank", DTEnums.ActionEnum.View.ToString()); //检查权限
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
            BLL.rank bll = new BLL.rank();
            Model.rankModel model = bll.GetModel(_id);


            txtRank.Text = model.rank;
            txtRankName.Text = model.rankName;

        }
        #endregion

        #region 增加操作=================================
        private bool DoAdd()
        {
            Model.rankModel model = new Model.rankModel();
            BLL.rank bll = new BLL.rank();
            //检测组名是否重复
            if (bll.Exists(txtRankName.Text.Trim(), txtRank.Text.Trim()))
            {
                JscriptMsg("组群名/组群编号不能重复！", "");
                return false;
            }

            model.rank = txtRank.Text.Trim();
            model.rankName = txtRankName.Text.Trim();
            model.createtime = DateTime.Now;
            
            if (bll.Add(model) > 0)
            {
                AddAdminLog(DTEnums.ActionEnum.Add.ToString(), "添加通话组群:" + model.rankName); //记录日志
                return true;
            }
            return false;
        }
        #endregion

        #region 修改操作=================================
        private bool DoEdit(int _id)
        {
            bool result = false;
            BLL.rank bll = new BLL.rank();
            Model.rankModel model = bll.GetModel(_id);
            //检测组名是否重复
            if (bll.Exists(txtRankName.Text.Trim(), txtRank.Text.Trim()))
            {
                JscriptMsg("组群名/组群编号不能重复！", "");
                return false;
            }

            model.rank = txtRank.Text.Trim();
            model.rankName = txtRankName.Text.Trim();

            if (bll.Update(model))
            {
                AddAdminLog(DTEnums.ActionEnum.Edit.ToString(), "修改通话组:" + model.rankName); //记录日志
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
                ChkAdminLevel("sys_rank", DTEnums.ActionEnum.Edit.ToString()); //检查权限
                if (!DoEdit(this.id))
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("修改成功！", "rank_list.aspx");
            }
            else //添加
            {
                ChkAdminLevel("sys_rank", DTEnums.ActionEnum.Add.ToString()); //检查权限
                if (!DoAdd())
                {
                    JscriptMsg("保存过程中发生错误！", "");
                    return;
                }
                JscriptMsg("添加成功！", "rank_list.aspx");
            }
        }
    }
}