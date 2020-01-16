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
    public partial class group_list : Web.UI.ManagePage
    {
        protected int totalCount;
        protected int page;
        protected int pageSize;
        protected string keywords = string.Empty;

        protected void Page_Load(object sender, EventArgs e)
        {
            this.keywords = DTRequest.GetQueryString("keywords");
            this.pageSize = GetPageSize(10); //每页数量
            if (!Page.IsPostBack)
            {
                ChkAdminLevel("sys_group", DTEnums.ActionEnum.View.ToString()); //检查权限
                Model.user model = GetAdminInfo(); //取得当前管理员信息
                RptBind("1=1" + CombSqlTxt(keywords), "createdTime desc");
            }
        }

        #region 数据绑定=================================
        private void RptBind(string _strWhere, string _orderby)
        {
            this.page = DTRequest.GetQueryInt("page", 1);
            txtKeywords.Text = this.keywords;
            BLL.group bll = new BLL.group();
            this.rptList.DataSource = bll.GetList(this.pageSize, this.page, _strWhere, _orderby, out this.totalCount);
            this.rptList.DataBind();

            //绑定页码
            txtPageNum.Text = this.pageSize.ToString();
            string pageUrl = Utils.CombUrlTxt("group_list.aspx", "keywords={0}&page={1}", this.keywords, "__id__");
            PageContent.InnerHtml = Utils.OutPageList(this.pageSize, this.page, this.totalCount, pageUrl, 8);
        }
        #endregion

        #region 组合SQL查询语句==========================
        protected string CombSqlTxt(string _keywords)
        {
            StringBuilder strTemp = new StringBuilder();
            _keywords = _keywords.Replace("'", "");
            if (!string.IsNullOrEmpty(_keywords))
            {
                strTemp.Append(" and discussionName like '%" + _keywords + "%'");
            }

            return strTemp.ToString();
        }
        #endregion

        #region 返回每页数量=============================
        private int GetPageSize(int _default_size)
        {
            int _pagesize;
            if (int.TryParse(Utils.GetCookie("group_page_size", "GRGcmsPage"), out _pagesize))
            {
                if (_pagesize > 0)
                {
                    return _pagesize;
                }
            }
            return _default_size;
        }
        #endregion
        //查询操作
        protected void btnSearch_Click(object sender, EventArgs e)
        {
            Response.Redirect(Utils.CombUrlTxt("group_list.aspx", "keywords={0}", txtKeywords.Text.Trim()));
        }
        //设置分页数量
        protected void txtPageNum_TextChanged(object sender, EventArgs e)
        {
            int _pagesize;
            if (int.TryParse(txtPageNum.Text.Trim(), out _pagesize))
            {
                if (_pagesize > 0)
                {
                    Utils.WriteCookie("group_list", "GRGcmsPage", _pagesize.ToString(), 14400);
                }
            }
            Response.Redirect(Utils.CombUrlTxt("group_list.aspx", "keywords={0}", this.keywords));
        }
        //批量删除
        protected void btnDelete_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_group", DTEnums.ActionEnum.Delete.ToString()); //检查权限
            int sucCount = 0; //成功数量
            int errorCount = 0; //失败数量
            BLL.group bll = new BLL.group();
            for (int i = 0; i < rptList.Items.Count; i++)
            {
                int id = Convert.ToInt32(((HiddenField)rptList.Items[i].FindControl("hidId")).Value);
                CheckBox cb = (CheckBox)rptList.Items[i].FindControl("chkId");
                if (cb.Checked)
                {
                    if (bll.Delete(id))
                    {
                        sucCount++;
                    }
                    else
                    {
                        errorCount++;
                    }
                }
            }
            AddAdminLog(DTEnums.ActionEnum.Delete.ToString(), "删除通讯组" + sucCount + "条，失败" + errorCount + "条"); //记录日志
            JscriptMsg("删除成功" + sucCount + "条，失败" + errorCount + "条！", Utils.CombUrlTxt("group_list.aspx", "keywords={0}", this.keywords));
        }
        //导入PDT数据
        protected void btnImport_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_group", DTEnums.ActionEnum.Synchronize.ToString()); //检查权限
            HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["webservice"].ToString();
            string command = String.Format(requesturi, "group");
            Task<HttpResponseMessage> response = httpClient.GetAsync(command);
            string result = response.Result.Content.ReadAsStringAsync().Result;
            int resp = this.parseResp(result);
            if (resp == 0)
            {
                JscriptMsg("PDT通话组数据导入成功！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
            }
            else
            {
                JscriptMsg("PDT通话组数据导入失败！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
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