using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Text;
using GRGcms.Common;
using Apache.NMS;
using Apache.NMS.Util;
using Apache.NMS.ActiveMQ;
using System.Text.RegularExpressions;
using System.Threading;

namespace GRGcms.BLL
{
    /// <summary>
    ///管理员信息表
    /// </summary>
    public partial class user_gps
    {
        private readonly Model.sysconfig sysConfig = new BLL.sysconfig().loadConfig(); //获得系统配置信息
        private string connectionStringMQ = ConfigurationManager.ConnectionStrings["ConnectionStringMQ"].ConnectionString;
        private string queueName = ConfigurationManager.ConnectionStrings["ConnectionStringMQ"].ProviderName;
        private Regex r = new Regex("\\d+\\.?\\d*");
        public static readonly AutoResetEvent semaphore = new AutoResetEvent(false);

        public user_gps(string connectStr)
        {
            connectionStringMQ = ConfigurationManager.ConnectionStrings[connectStr].ConnectionString;
            queueName = ConfigurationManager.ConnectionStrings[connectStr].ProviderName;
        }

        public void gpsConsume()
        {
            while (true)
            {
                IConnection connection = null;
                try
                {
                    //Uri connecturi = new Uri("activemq:tcp://127.0.0.1:61616");
                    Uri connecturi = new Uri(connectionStringMQ + "");
                    //IConnectionFactory factory = new ConnectionFactory(connecturi);
                    IConnectionFactory factory = new NMSConnectionFactory(connecturi);
                    //Create the connection  
                    using (connection = factory.CreateConnection())
                    {
                        //Create the Session  
                        using (ISession session = connection.CreateSession())
                        {
                            //IDestination destination = SessionUtil.GetDestination(session, "queue://pdt_msg_queue");
                            IDestination destination = SessionUtil.GetDestination(session, queueName);
                            //Create the Consumer  
                            IMessageConsumer consumer = session.CreateConsumer(destination);
                            
                            connection.Start();
                            //ITextMessage msg = consumer.Receive() as ITextMessage;

                            consumer.Listener += new MessageListener(consumer_Listener);
                            semaphore.WaitOne();
                        }
                        LogHelper.WriteInfo(typeof(user_gps), "MQ Finish...");
                        connection.Stop();
                        connection.Close();
                    }
                }
                catch (System.Exception e)
                {
                    if (connection != null)
                    {
                        connection.Stop();
                        connection.Close();
                    }
                    LogHelper.WriteError(typeof(user_gps), "Cannot Connect to MQ", e);
                }
                LogHelper.WriteInfo(typeof(user_gps), "Thread Sleep, try another connection ...");
                Thread.Sleep(10000);
            }
        }

        private void consumer_Listener(IMessage message)
        {
            try
            {
                ITextMessage msg = (ITextMessage)message;
                string gpsText = msg.Text;
                GRGcms.Model.mq_msg mqMsg = JsonHelper.JSONToObject<GRGcms.Model.mq_msg>(gpsText);
                string cmdx = mqMsg.cmdx;
                switch (cmdx)
                {
                    case "03":   //处理GPS信息
                        this.processGPS(mqMsg);
                        LogHelper.WriteInfo(typeof(user_gps), Thread.CurrentThread.Name + " Receive Message : " + gpsText);
                        break;
                    case "04":   //处理对讲机状态
                        LogHelper.WriteInfo(typeof(user_gps), Thread.CurrentThread.Name + " Receive Message : " + gpsText);
                        this.processState(mqMsg);
                        break;
                    case "07":   //处理对讲机所属组
                        LogHelper.WriteInfo(typeof(user_gps), Thread.CurrentThread.Name + " Receive Message : " + gpsText);
                        this.processGroup(mqMsg);
                        break;
                    default:
                        break;
                }
            }
            catch (System.Exception e)
            {
                LogHelper.WriteError(typeof(user_gps), "MQ Message Handler with Error: ", e);
            }
        }

        private void processGPS(GRGcms.Model.mq_msg mqMsg)
        {
            Dictionary<string, string> dic = mqMsg.data;
            string nmea = dic["NMEA"];
            string ssi = dic["SSI"];

            Match m = r.Match(nmea);
            string n = m.ToString();
            string e = m.NextMatch().ToString();
            float nVal = int.Parse(n.Substring(0, 2)) + float.Parse(n.Substring(2)) / 60;
            float eVal = int.Parse(e.Substring(0, 3)) + float.Parse(e.Substring(3)) / 60;

            GRGcms.Model.user_gps gps = new GRGcms.Model.user_gps();
            gps.account = ssi;
            gps.longtitude = eVal;
            gps.latitude = nVal;
            gps.type = "PDT";
            gps.updatetime = DateTime.Now;

            this.gpsClean(gps);
            object result = GRGcms.DBUtility.MyBatisHelper.InsertObject("insert_user_gps", gps);
        }

        private void processGroup(GRGcms.Model.mq_msg mqMsg)
        {
            Dictionary<string, string> dic = mqMsg.data;
            string groupid = dic["GrpSSI"];
            string ssi = dic["RadioSSI"];

            GRGcms.Model.user_gps gps = new GRGcms.Model.user_gps();
            gps.account = ssi;
            gps.groupid = groupid;

            object result = GRGcms.DBUtility.MyBatisHelper.UpdateObject("update_pdt_user_group", gps);
        }

        private void processState(GRGcms.Model.mq_msg mqMsg)
        {
            Dictionary<string, string> dic = mqMsg.data;
            string state = dic["State"];
            string ssi = dic["SSI"];

            GRGcms.Model.user_gps gps = new GRGcms.Model.user_gps();
            gps.account = ssi;
            gps.status = state == "00" ? "在线" : "离线";

            object result = GRGcms.DBUtility.MyBatisHelper.UpdateObject("update_pdt_user_status", gps);
        }

        public void gpsClean(GRGcms.Model.user_gps gps)
        {
            GRGcms.DBUtility.MyBatisHelper.DeleteObject("delete_expire_gps", gps);
        }
    }
}