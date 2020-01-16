using System;
using System.Text;
using System.Data;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using GRGcms.Common;
using System.Net.Http;
using System.Configuration;
using System.Threading.Tasks;

namespace GRGcms.Web.admin.manager
{
    public partial class dept_list : Web.UI.ManagePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("sys_dept", DTEnums.ActionEnum.View.ToString()); //检查权限
                RptBind();
            }
        }

        //数据绑定
        private void RptBind()
        {
            BLL.dept bll = new BLL.dept();
            DataTable dt = bll.GetList("");
            this.rptList.DataSource = dt;
            this.rptList.DataBind();
        }

        //保存排序
        protected void btnSave_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("sys_dept", DTEnums.ActionEnum.Edit.ToString()); //检查权限
            BLL.dept bll = new BLL.dept();
            for (int i = 0; i < rptList.Items.Count; i++)
            {
                int id = Convert.ToInt32(((HiddenField)rptList.Items[i].FindControl("hidId")).Value);
                int sortId;
                if (!int.TryParse(((TextBox)rptList.Items[i].FindControl("txtSortId")).Text.Trim(), out sortId))
                {
                    sortId = 99;
                }
                bll.UpdateField(id, "sort_id=" + sortId.ToString());
            }
            AddAdminLog(DTEnums.ActionEnum.Edit.ToString(), "保存机构排序"); //记录日志
            JscriptMsg("保存排序成功！", "dept_list.aspx");
        }

        //删除部门
        protected void btnDelete_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("sys_dept", DTEnums.ActionEnum.Delete.ToString()); //检查权限
            BLL.dept bll = new BLL.dept();
            for (int i = 0; i < rptList.Items.Count; i++)
            {
                int id = Convert.ToInt32(((HiddenField)rptList.Items[i].FindControl("hidId")).Value);
                CheckBox cb = (CheckBox)rptList.Items[i].FindControl("chkId");
                if (cb.Checked)
                {
                    bll.Delete(id);
                }
            }
            AddAdminLog(DTEnums.ActionEnum.Delete.ToString(), "删除机构"); //记录日志
            JscriptMsg("删除数据成功！", "dept_list.aspx", "parent.loadMenuTree");
        }

        protected void btnImport_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("sys_dept", DTEnums.ActionEnum.Synchronize.ToString()); //检查权限
            HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["webservice"].ToString();
            string command = String.Format(requesturi, "dept");
            Task<HttpResponseMessage> response = httpClient.GetAsync(command);
            string result = response.Result.Content.ReadAsStringAsync().Result;
            int resp = this.parseResp(result);
            if (resp == 0)
            {
                JscriptMsg("PDT组织机构数据导入成功！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", "PDT"));
            }
            else
            {
                JscriptMsg("PDT组织机构数据导入失败！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", "PDT"));
            }
        }

        private int parseResp(string respResult)
        {
            Dictionary<string, string> result = JsonHelper.JSONToObject<Dictionary<string, string>>(respResult);
            if (result == null)
            {
                return -1;
            }
            string status = result["status"];
            return Convert.ToInt32(status);
        }
    }
}