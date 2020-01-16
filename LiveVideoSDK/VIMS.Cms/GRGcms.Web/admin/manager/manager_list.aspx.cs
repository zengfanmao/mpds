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
using GRGcms.DBUtility;
using GRGcms.Model;

namespace GRGcms.Web.admin.manager
{
    public partial class manager_list : Web.UI.ManagePage
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
                ChkAdminLevel("manager_list", DTEnums.ActionEnum.View.ToString()); //检查权限
                Model.user model = GetAdminInfo(); //取得当前管理员信息
                RptBind("1=1"  + CombSqlTxt(keywords), "Createtime asc,ID desc");

                log l = new log();
                log r = MyBatisHelper.QueryForObject<log>("select_user_import_log", l);
                if (r != null)
                {
                    if ("0".Equals(r.status))
                    {
                        this.importResult.Text = r.createtime + " 用户导入进行中";
                    }
                    else
                    {
                        this.importResult.Text = r.createtime + " 用户导入成功";
                    }
                }
            }
        }

        #region 数据绑定=================================
        private void RptBind(string _strWhere, string _orderby)
        {
            this.page = DTRequest.GetQueryInt("page", 1);
            txtKeywords.Text = this.keywords;
            BLL.user bll = new BLL.user();
            this.rptList.DataSource = bll.GetList(this.pageSize, this.page, _strWhere, _orderby, out this.totalCount);
            this.rptList.DataBind();

            //绑定页码
            txtPageNum.Text = this.pageSize.ToString();
            string pageUrl = Utils.CombUrlTxt("manager_list.aspx", "keywords={0}&page={1}", this.keywords, "__id__");
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
                strTemp.Append(" and (uCode like  '%" + _keywords + "%' or uName like '%" + _keywords + "%')");
            }

            return strTemp.ToString();
        }
        #endregion

        #region 返回每页数量=============================
        private int GetPageSize(int _default_size)
        {
            int _pagesize;
            if (int.TryParse(Utils.GetCookie("manager_page_size", "GRGcmsPage"), out _pagesize))
            {
                if (_pagesize > 0)
                {
                    return _pagesize;
                }
            }
            return _default_size;
        }
        #endregion

        //关健字查询
        protected void btnSearch_Click(object sender, EventArgs e)
        {
            Response.Redirect(Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", txtKeywords.Text));
        }

        //设置分页数量
        protected void txtPageNum_TextChanged(object sender, EventArgs e)
        {
            int _pagesize;
            if (int.TryParse(txtPageNum.Text.Trim(), out _pagesize))
            {
                if (_pagesize > 0)
                {
                    Utils.WriteCookie("manager_page_size", "GRGcmsPage", _pagesize.ToString(), 14400);
                }
            }
            Response.Redirect(Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
        }

        //批量删除
        protected void btnDelete_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Delete.ToString()); //检查权限
            int sucCount = 0;
            int errorCount = 0;
            BLL.user bll = new BLL.user();
            for (int i = 0; i < rptList.Items.Count; i++)
            {
                int id = Convert.ToInt32(((HiddenField)rptList.Items[i].FindControl("hidId")).Value);
                CheckBox cb = (CheckBox)rptList.Items[i].FindControl("chkId");
                if (cb.Checked)
                {
                    if (bll.Delete(id))
                    {
                        sucCount += 1;
                    }
                    else
                    {
                        errorCount += 1;
                    }
                }
            }
            AddAdminLog(DTEnums.ActionEnum.Delete.ToString(), "删除会员" + sucCount + "条，失败" + errorCount + "条"); //记录日志
            JscriptMsg("删除成功" + sucCount + "条，失败" + errorCount + "条！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
        }


        //导入PDT数据
        protected void btnImport_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Synchronize.ToString()); //检查权限
            //HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["webservice"].ToString();
            requesturi = requesturi + "&startDate={1}&endDate={2}";
            string startDate = this.startDt.Text.ToString();
            string endDate = this.endDt.Text.ToString();
            if (String.IsNullOrEmpty(startDate) || String.IsNullOrEmpty(endDate))
            {
                JscriptMsg("请选择对应时间段！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", ""));
                return;
            }
            string command = String.Format(requesturi, "user", startDate, endDate);
            UserImport import = new UserImport(command);
            System.Threading.Thread userImport = new System.Threading.Thread(new System.Threading.ThreadStart(import.import));
            userImport.Start();
            JscriptMsg("PDT用户数据执行成功！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
            //Task<HttpResponseMessage> response = httpClient.GetAsync(command);
            //string result = response.Result.Content.ReadAsStringAsync().Result;
            //int resp = this.parseResp(result);
            //if (resp == 0)
            //{
            //    JscriptMsg("PDT用户数据导入成功！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
            //}
            //else
            //{
            //    JscriptMsg("PDT用户数据导入失败！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
            //}
        }

        public partial class UserImport
        {
            string command;
            public UserImport(string command)
            {
                this.command = command;
            }

            public void import()
            {
                HttpClient httpClient = new HttpClient();
                Task<HttpResponseMessage> response = httpClient.GetAsync(command);
                string result = response.Result.Content.ReadAsStringAsync().Result;
                int resp = parseResp(result);
                if (resp == 0)
                {
                    LogHelper.WriteInfo(typeof(manager_list), "PDT用户数据导入成功！");
                }
                else
                {
                    LogHelper.WriteInfo(typeof(manager_list), "PDT用户数据导入失败！");
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
        

        protected void btnStop_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Edit.ToString()); //检查权限
            this.updateStatus("stop", "遥晕");
        }

        protected void btnKill_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Edit.ToString()); //检查权限
            this.updateStatus("kill", "遥毙");
        }

        protected void btnDisable_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Edit.ToString()); //检查权限
            this.updateStatus("disable", "禁用");
        }

        protected void btnEnable_Click(object sender, EventArgs e)
        {
            ChkAdminLevel("manager_list", DTEnums.ActionEnum.Edit.ToString()); //检查权限
            this.updateStatus("enable", "启用");
        }

        private void updateStatus(string action, string actionName)
        {
            BLL.user bll = new BLL.user();
            Model.user user = null;
            for (int i = 0; i < rptList.Items.Count; i++)
            {
                int id = Convert.ToInt32(((HiddenField)rptList.Items[i].FindControl("hidId")).Value);
                CheckBox cb = (CheckBox)rptList.Items[i].FindControl("chkId");
                if (cb.Checked)
                {
                    user = bll.GetModel(id);
                    break;
                }
            }
            if (user == null)
            {
                JscriptMsg(actionName + "|请选择用户！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
                return;
            }
            if (String.IsNullOrEmpty(user.devicetype))
            {
                JscriptMsg(actionName + "|请选择PDT/APP用户！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
                return;
            }
            if (("stop".Equals(action) || "kill".Equals(action))
                && "APP".Equals(user.devicetype))
            {
                JscriptMsg(actionName + "|请选择PDT用户！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
                return;
            }
            if ("disable".Equals(action) && "PDT".Equals(user.devicetype))
            {
                JscriptMsg(actionName + "|请选择APP用户！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
                return;
            }
            HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["mapAjax"].ToString();
            string command = String.Format(requesturi, action, user.uCode);
            Task<HttpResponseMessage> response = httpClient.GetAsync(command);
            string result = response.Result.Content.ReadAsStringAsync().Result;
            int resp = this.parseResp(result);
            if (resp == 0)
            {
                JscriptMsg(actionName + "|命令操作成功！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
            }
            else
            {
                JscriptMsg(actionName + "|命令操作失败！", Utils.CombUrlTxt("manager_list.aspx", "keywords={0}", this.keywords));
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