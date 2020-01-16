using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using GRGcms.Common;
using GRGcms.Model;
using GRGcms.DBUtility;
using System.Configuration;
using GRGcms.Web.UI;

namespace GRGcms.Web.tools
{
    /// <summary>
    /// webserivce 的摘要说明
    /// </summary>
    public class webserivce : IHttpHandler
    {
        private readonly mpdsServices.CityUserInterfaceClient serviceClient = new mpdsServices.CityUserInterfaceClient("CityUserServicePort");

        private string downloadPrefix = ConfigurationManager.AppSettings["apkdownload"].ToString();
        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "application/json";
            string action = DTRequest.GetQueryString("action");
            switch (action)
            {
                case "user":
                    try
                    {
                        this.handleSyncUser();
                    }
                    catch (Exception e)
                    {
                        LogHelper.WriteError(typeof(webserivce), "Sync User Error: ", e);
                    }
                    break;
                case "group":
                    try
                    {
                        this.handleSyncGroup();
                    }
                    catch (Exception e)
                    {
                        LogHelper.WriteError(typeof(webserivce), "Sync User Error: ", e);
                    }
                    break;
                case "dept":
                    this.handleSyncDept();
                    try
                    {
                        this.handleSyncDept();
                    }
                    catch (Exception e)
                    {
                        LogHelper.WriteError(typeof(webserivce), "Sync User Error: ", e);
                    }
                    break;
                case "portrait":
                    this.handlePortrait();
                    return;
            }
            context.Response.Write("{\"status\": 0, \"msg\": \"数据更新成功\"}");
            return;
        }

        public void handleSyncUser()
        {
            try
            {
                log l = new log();
                l.account = "webservice";
                l.name = "syncuser";
                l.eventName = "syncuser";
                l.status = "0";
                MyBatisHelper.InsertObject("insert_event_log", l);
                string startDate = DTRequest.GetQueryString("startDate");
                string endDate = DTRequest.GetQueryString("endDate");
                if (String.IsNullOrEmpty(startDate) || String.IsNullOrEmpty(endDate))
                {
                    LogHelper.WriteInfo(typeof(webserivce), "输入时间参数有误");
                    return;
                }
                string queryStr = "{\"startDate\":" + startDate + ",\"endDate\":" + endDate + "}";
                string userList = serviceClient.userInfoQuery(queryStr);
                LogHelper.WriteInfo(typeof(webserivce), "同步的用户数据为：" + userList);
                Dictionary<string, List<pdt_user>> userDic = JsonHelper.JSONToObject<Dictionary<string, List<pdt_user>>>(userList);
                List<pdt_user> users = userDic["userList"];
                if (users == null || users.Count < 1)
                {
                    return;
                }
                string policeImg = ConfigurationManager.AppSettings["policeImg"].ToString();
                foreach (pdt_user user in users)
                {
                    user.useDesc = policeImg;
                    LogHelper.WriteInfo(typeof(webserivce), "用户数据插入数据库：" + JsonHelper.ObjectToJSON(user));
                    MyBatisHelper.DeleteObject("delete_user_pdt", user);
                    MyBatisHelper.InsertObject("insert_user_pdt", user);
                }
                l = new log();
                l.account = "webservice";
                l.name = "syncuser";
                l.eventName = "syncuser";
                l.status = "1";
                MyBatisHelper.InsertObject("insert_event_log", l);
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(webserivce), "import user error: ", e);
            }
        }

        private void handleSyncGroup()
        {
            string groupList = serviceClient.groupInfoQuery("");
            LogHelper.WriteInfo(typeof(webserivce), "同步的通信组数据为：" + groupList);
            Dictionary<string, List<pdt_group>> groudDic = JsonHelper.JSONToObject<Dictionary<string, List<pdt_group>>>(groupList);
            List<pdt_group> groups = groudDic["groupList"];
            if (groups == null || groups.Count < 1) 
            {
                return;
            }
            foreach(pdt_group group in groups)
            {
                LogHelper.WriteInfo(typeof(webserivce), "组数据插入数据库：" + JsonHelper.ObjectToJSON(group));
                MyBatisHelper.DeleteObject("delete_group_pdt", group);
                MyBatisHelper.InsertObject("insert_group_pdt", group);
            }
        }

        private void handleSyncDept()
        {
            string deptList = serviceClient.orgInfoQuery("");
            LogHelper.WriteInfo(typeof(webserivce), "同步的组织机构数据为：" + deptList);
            Dictionary<string, List<pdt_dept>> deptDic = JsonHelper.JSONToObject<Dictionary<string, List<pdt_dept>>>(deptList);
            List<pdt_dept> depts = deptDic["orgList"];
            if (depts == null || depts.Count < 1)
            {
                return;
            }
            foreach (pdt_dept dept in depts)
            {
                LogHelper.WriteInfo(typeof(webserivce), "组织机构数据插入数据库：" + JsonHelper.ObjectToJSON(dept));
                MyBatisHelper.DeleteObject("delete_dept_pdt", dept);
                MyBatisHelper.InsertObject("insert_dept_pdt", dept);
            }
        }

        public void handlePortrait()
        {
            HttpPostedFile portrait = HttpContext.Current.Request.Files["portrait"];
            string uCode = DTRequest.GetQueryString("uCode");
            string fileName = portrait.FileName;
            byte[] byteData = FileHelper.ConvertStreamToByteBuffer(portrait.InputStream); //获取文件流
            //开始上传
            string remsg = new UpLoad().FileSaveAs(byteData, fileName, false, false);
            Dictionary<string, object> dic = JsonHelper.DataRowFromJSON(remsg);
            string status = dic["status"].ToString();
            string msg = dic["msg"].ToString();
            if (status == "0")
            {
                HttpContext.Current.Response.Write("{\"IsSuccess\": \"false\", \"ResultCode\": -1, \"Message\": \"头像更新失败\"}");
                return;
            }
            string filePath = dic["path"].ToString(); //取得上传后的路径
            filePath = downloadPrefix + filePath;
            LogHelper.WriteInfo(typeof(webserivce), "为用户" + uCode + "更新头像为：" + filePath);
            pdt_user user = new pdt_user();
            user.msi = uCode;
            user.useDesc = filePath;
            MyBatisHelper.UpdateObject("update_user_portrait", user);
            HttpContext.Current.Response.Write("{\"IsSuccess\": \"true\", \"ResultCode\": 1, \"Message\": \"头像更新成功\", \"Entity\": \"" + filePath + "\"}"); //输出成功提示
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}