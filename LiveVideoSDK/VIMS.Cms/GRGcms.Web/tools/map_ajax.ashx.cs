using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using GRGcms.Common;
using GRGcms.Model;
using System.Data;
using GRGcms.DBUtility;
using System.Net;
using System.Configuration;
using System.IO;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.UI;

namespace GRGcms.Web.tools
{
    /// <summary>
    /// map_ajax 的摘要说明
    /// </summary>
    public class map_ajax : IHttpHandler, System.Web.SessionState.IRequiresSessionState
    {

        private string pdtbiscommand = ConfigurationManager.AppSettings["pdtbiscommand"];

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "application/json";
            //取得处事类型
            string action = DTRequest.GetQueryString("action");
            var adminInfo = this.GetAdminInfo(context);
            switch(action)
            {
                case "user":
                    get_user_gps(context);
                    break;
                case "user_count":
                    get_user_gps_count(context);
                    break;
                case "stop": //遥晕
                    var log = new log() { account = adminInfo.uCode, name = adminInfo.uName, eventName = "遥晕", remark = "遥晕", type = "Console", user_ip = context.Request.UserHostAddress};
                    MyBatisHelper.InsertObject("insert_event_log", log);
                    this.stop_user(context);
                    break;
                case "kill": //遥毙
                    var log1 = new log() { account = adminInfo.uCode, name = adminInfo.uName, eventName = "遥毙", remark = "遥毙", type = "Console", user_ip = context.Request.UserHostAddress };
                    MyBatisHelper.InsertObject("insert_event_log", log1);
                    this.kill_user(context);
                    break;
                case "disable": //禁用
                    var log2 = new log() { account = adminInfo.uCode, name = adminInfo.uName, eventName = "禁用", remark = "禁用", type = "Console", user_ip = context.Request.UserHostAddress };
                    MyBatisHelper.InsertObject("insert_event_log", log2);
                    this.disable_user(context);
                    break;
                case "enable": //激活
                    var log3 = new log() { account = adminInfo.uCode, name = adminInfo.uName, eventName = "激活", remark = "激活", type = "Console", user_ip = context.Request.UserHostAddress };
                    MyBatisHelper.InsertObject("insert_event_log", log3);
                    this.enable_user(context);
                    break;
            }
            return;
        }

        private void get_user_gps(HttpContext context)
        {
            user_gps param = this.get_user_param(context);
            IList<GRGcms.Model.user_gps> gpsList = GRGcms.DBUtility.MyBatisHelper.QueryForList<GRGcms.Model.user_gps>("find_map_user", param);
            string result = JsonHelper.ObjectToJSON(gpsList);
            context.Response.Write(result);
            return;
        }

        private void get_user_gps_count(HttpContext context)
        {
            user_gps param = this.get_user_param(context);
            System.Int32 count = MyBatisHelper.QueryForObject<System.Int32>("find_map_user_count", param);
            context.Response.Write(count);
            return;
        }

        private user_gps get_user_param(HttpContext context)
        {
            string keywords = DTRequest.GetQueryString("keywords");
            int pageSize = DTRequest.GetQueryInt("pageSize", 10);
            int page = DTRequest.GetQueryInt("page", 1);
            user_gps param = new user_gps();
            param.name = keywords;
            param.page = (page - 1) * pageSize;
            param.pageSize = pageSize;
            return param;
        }

        private void stop_user(HttpContext context)
        {
            string account = DTRequest.GetQueryString("account");
            string command = String.Format(pdtbiscommand, account, 0);
            HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
            WebResponse webResp = webReq.GetResponse();
            string respResult = this.getRespResult(webResp);
            int resp = this.parseResp(respResult);
            if (resp == 0)
            {
                user_gps user = new user_gps();
                user.account = account;
                user.status = "遥晕";
                MyBatisHelper.UpdateObject("update_pdt_user_status", user);
            }
            context.Response.Write(respResult);
        }

        private void kill_user(HttpContext context)
        {
            string account = DTRequest.GetQueryString("account");
            string command = String.Format(pdtbiscommand, account, 1);
            HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
            WebResponse webResp = webReq.GetResponse();
            string respResult = this.getRespResult(webResp);
            int resp = this.parseResp(respResult);
            if (resp == 0)
            {
                user_gps user = new user_gps();
                user.account = account;
                user.status = "摇毙";
                MyBatisHelper.UpdateObject("update_pdt_user_status", user);
            }
            context.Response.Write(respResult);
        }

        private void disable_user(HttpContext context)
        {
            string account = DTRequest.GetQueryString("account");
            user_gps user = new user_gps();
            user.account = account;
            user.status = "禁用";
            MyBatisHelper.UpdateObject("update_app_user_status", user);
            HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["imcommand"].ToString();
            var values = new List<KeyValuePair<string, string>>();
            var adminInfo = GetAdminInfo(context);
            values.Add(new KeyValuePair<string, string>("UserCode", adminInfo.uCode));
            values.Add(new KeyValuePair<string, string>("MsgType", "Command"));
            values.Add(new KeyValuePair<string, string>("MsgContent", "offline"));
            values.Add(new KeyValuePair<string, string>("MsgFromType", "Person"));
            values.Add(new KeyValuePair<string, string>("MsgToCode", account));
            HttpContent content = new FormUrlEncodedContent(values);
            Task<HttpResponseMessage> response = httpClient.PostAsync(requesturi, content);
            string result = response.Result.Content.ReadAsStringAsync().Result;
            context.Response.Write("{\"status\":\"0\", \"msg\":\"状态更新成功！\"}");
        }

        private void enable_user(HttpContext context)
        {
            user_gps user = new user_gps();
            user.account = DTRequest.GetQueryString("account");
            user_gps result = MyBatisHelper.QueryForObject<user_gps>("select_pdt_user_by_account", user);
            if (result != null)
            {
                string command = String.Format(pdtbiscommand, user.account, 2);
                HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
                WebResponse webResp = webReq.GetResponse();
                string respResult = this.getRespResult(webResp);
                int resp = this.parseResp(respResult);
                if (resp == 0)
                {
                    user.status = "在线";
                    MyBatisHelper.UpdateObject("update_pdt_user_status", user);
                }
                context.Response.Write(respResult);
            }
            else
            {
                user.status = "离线";
                MyBatisHelper.UpdateObject("update_app_user_status", user);
                context.Response.Write("{\"status\":\"0\", \"msg\":\"状态更新成功！\"}");
            }
        }

        private string getRespResult(WebResponse webResp)
        {
            Stream stream = webResp.GetResponseStream();
            StreamReader sr = new StreamReader(stream);
            string resp = sr.ReadToEnd();
            return resp;
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

        public Model.user GetAdminInfo(HttpContext context)
        {
            Model.user model = context.Session[DTKeys.SESSION_ADMIN_INFO] as Model.user;
            if (model != null)
            {
                return model;
            }
            else
            {
                return new user() { uCode = "system" };
            }
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