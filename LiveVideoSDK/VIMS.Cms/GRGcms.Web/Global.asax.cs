using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.SessionState;
using GRGcms.BLL;
using log4net.Config;
using System.Threading;
using GRGcms.Common;
using System.Net.Http;
using System.Configuration;
using System.Threading.Tasks;

namespace GRGcms.Web.tools
{
    public class Global : System.Web.HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            log4net.Config.XmlConfigurator.Configure();
            /**
            try
            {
                string maxThread = ConfigurationManager.AppSettings["maxThread"].ToString();
                int threadNum = Convert.ToInt32(maxThread);
                LogHelper.WriteInfo(typeof(Global), "Application Start...");
                XmlConfigurator.Configure();

                user_gps gps = new user_gps("ConnectionStringMQ");
                System.Threading.Thread gpsServiceData = new System.Threading.Thread(new System.Threading.ThreadStart(gps.gpsConsume));
                gpsServiceData.Start();

                user_gps state = new user_gps("ConnectionStringMQ_State");
                System.Threading.Thread stateServiceData = new System.Threading.Thread(new System.Threading.ThreadStart(state.gpsConsume));
                stateServiceData.Start();
            }
            catch (System.Exception ex)
            {
                LogHelper.WriteError(typeof(Global), "Application Error...", ex);
            }
            **/
        }

        protected void Session_Start(object sender, EventArgs e)
        {

        }

        protected void Application_BeginRequest(object sender, EventArgs e)
        {

        }

        protected void Application_AuthenticateRequest(object sender, EventArgs e)
        {

        }

        protected void Application_Error(object sender, EventArgs e)
        {

        }

        protected void Session_End(object sender, EventArgs e)
        {

        }

        protected void Application_End(object sender, EventArgs e)
        {
            LogHelper.WriteInfo(typeof(Global), "Application End...");
            GRGcms.BLL.user_gps.semaphore.Set();
            HttpClient httpClient = new HttpClient();
            string requesturi = ConfigurationManager.AppSettings["apkdownload"].ToString();
            string command = requesturi + "/admin/settings/ImageCode.aspx";
            LogHelper.WriteInfo(typeof(Global), "Request for Start : " + command);
            Task<HttpResponseMessage> response = httpClient.GetAsync(command);
            string respType = response.Result.Content.GetType().ToString();
        }
    }
}