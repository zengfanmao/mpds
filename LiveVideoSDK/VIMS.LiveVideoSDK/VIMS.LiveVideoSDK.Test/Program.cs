using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Net.Http.Headers;
using Newtonsoft.Json;
using VIMS.LiveVideoSDK.Core;
using VIMS.LiveVideoSDK.Model;
using VIMS.LiveVideoSDK.Bll;
using VIMS.LiveVideoSDK.Bll.Entity;
using System.Xml;
namespace VIMS.LiveVideoSDK.Test
{
    class Program
    {
        static void Main(string[] args)
        {
            //var basemanage =new VIMS.LiveVideoSDK.Bll.BaseManage();
            //var models = basemanage.GetAllCases("", "", "");

            //var models = basemanage.GetMyChatLists("hexiang", "s2MB60HqYpjhAg3SGZuxvw9e");
            //var models = basemanage.getCaseUsers("s2MB60HqYpjhAg3SGZuxvw9e");

            //Console.WriteLine(models);



            //var models = VIMS.LiveVideoSDK.Bll.Common.Utils.getVideoNames();
            //var models = VIMS.LiveVideoSDK.Bll.Common.Utils.getVideoNames("", "");
            //var models = basemanage.getCaseUsers("A4400000000002016120073");
            //Console.WriteLine(JsonConvert.SerializeObject(models));

            //Console.ReadLine();


            //var models = VIMS.LiveVideoSDK.Bll.Common.Utils.getIncomingStream();
            //if(models!=null)
            //foreach (var model in models)
            //{
            //    Console.WriteLine(model.Name);
            //}

            //Console.ReadLine();
            //HttpClient httpClient = new HttpClient();
            //HttpResponseMessage response = httpClient.GetAsync(new Uri("http://localhost:8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_definst_")).Result;
            //String result = response.Content.ReadAsStringAsync().Result;

            //HttpWebRequest request = (HttpWebRequest)WebRequest.Create
            //    ("http://localhost:8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_definst_");
            //var streamName = "http://localhost:8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_definst_";
            //streamName = streamName.Substring(streamName.LastIndexOf("/") + 1);
            //Console.WriteLine(streamName);


            //var rtmpUrl = "rtmp://192.168.8.138:1935";
            //rtmpUrl = rtmpUrl.ToLower().Replace("rtmp://", "");
            //if (rtmpUrl.IndexOf(":") > 0)
            //{
            //    rtmpUrl = rtmpUrl.Substring(0, rtmpUrl.IndexOf(":"));
            //}
            //Console.WriteLine(rtmpUrl);


            testUploadFileAsync();


            //获取直播列表
            //http://localhost:8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_definst_
            //request.Method = "GET";
            //request.Proxy = new WebProxy("127.0.0.1", 8087);
            //request.Credentials = new NetworkCredential("hexiang", "zhejisuhimima123");
            //request.ContentType = "application/json";
            //request.AllowAutoRedirect = false;
            //HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            //Stream stm = response.GetResponseStream(); 
            //Console.WriteLine(StreamToString(stm));·

            //Console.WriteLine(JsonConvert.SerializeObject(Bll.Common.Utils.getIncomingStream(result)));


            //var chatmodel=new ChatModel();
            //chatmodel.MsgContent="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 IaeqbS4SLuKVRn+ThMAVkIOa9t21cZuA";
            //JsonConvert.SerializeObject(chatmodel);



            //var savePath = livevideoSDK.Bll.Common.Utils.Format("", "/{yyyy}{mm}{dd}/{time}{rand:6}");
            //Console.WriteLine(Guid.NewGuid().ToString("N"));
            //Console.WriteLine(Convert.ToBase64String(Guid.NewGuid().ToByteArray()));
            //Console.WriteLine(Bll.Common.RandomIdGenerator.GetBase62(12));
            //Console.WriteLine(savePath);
            //testUploadFile();
            //TestMqqt();
            //AddUser();
            //AddCase();
            //new BaseManage().AddCaseLivingShow(new tbCaseLivingShow() { 
            //caseCode="1",
            //userCode="2",
            //livingState=false,
            //endTime = DateTime.Now,
            //startTime=DateTime.Now,
            //rtmpUrl="4",
            //});


            Console.ReadLine();
        }
        //显示xml数据
       

        public static string StreamToString(Stream stream)
        {
            stream.Position = 0;
            using (StreamReader stremReader = new StreamReader(stream, Encoding.UTF8))
            {
                return stremReader.ReadToEnd();
            }
        }
        public static async Task testUploadFileAsync()
        {
            using (var client = new HttpClient())
            using (var content = new MultipartFormDataContent())
            {
                // Make sure to change API address  
                client.BaseAddress = new Uri(" http://47.94.104.229");
                //client.BaseAddress = new Uri("http://localhost:2298");

                // Add first file content   
                var fileContent1 = new ByteArrayContent(File.ReadAllBytes(@"E:\temp\com.bonade.h5.mall.zip"));
                fileContent1.Headers.ContentDisposition = new ContentDispositionHeaderValue("attachment")
                {
                    FileName = "com.bonade.h5.mall.zip"
                };
                content.Add(fileContent1);

                // Make a call to Web API  
                var result = await client.PostAsync("/api/do/PostFile", content);
                var objectResult = result.Content.ReadAsStringAsync().Result;
                Console.WriteLine(objectResult);
                Console.WriteLine(result.StatusCode);
                Console.ReadLine();
            }
        }
        /// <summary>
        /// 测试以mqqt 协议编写的通信类库
        /// </summary>
        public static void TestMqqt()
        {
            // PUBLISHER
            Console.WriteLine("this is publish start");
            // create client instance
            //MqttClient client = new MqttClient(IPAddress.Parse(Dns.GetHostAddresses(MqttSettings.MQQT_BROKER_DEFAULT_DOMAINNAME)[0].ToString()));
            var client = new MqttClient(IPAddress.Parse(MqttSettings.MQQT_BROKER_DEFAULT_IP));
            string clientId = Guid.NewGuid().ToString();
            //client.Connect(clientId,"dzhexiang","123456");
            client.Connect(clientId);

            string strValue = string.Empty;
            // strValue = Convert.ToString("这个是我推送的消息hehe!"); 
            //// publish a message on "/home/temperature" topic with QoS 2
            //client.Publish("mJiaQi-Baobiao-B/All", Encoding.UTF8.GetBytes(strValue));
            strValue = Convert.ToString("this  is the second message from server by push myself for appversion!");
            //client.Publish("mJiaQi-Baobiao-C/All", Encoding.UTF8.GetBytes(strValue));
            //var entity = new BodyGuard.Bll.BaseManage().BGetUserByUserId("8f49e0c7-1dc1-4a07-a4c1-4453d253f800");

            //var result = new BodyGuard.Model.ResultEx() { Entity = null, Message = "测试11", IsSuccess = false, Exception = "123", ResultCode = "33333333" };
            //PushMessageModel model = new PushMessageModel() { MsgAction = "CancelOrder", MsgData = JsonConvert.SerializeObject(result) };

            //client.Publish("testtopic", Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(model)));
            //var publishResult = client.Publish("mJiaQi-Baobiao-B/All", Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(model)));
            //var publishResult = client.Publish(string.Format("mJiaQi-Baobiao-B/Phone-{0}", "18665569506"), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(model)));
            //var publishResult = client.Publish(string.Format("mJiaQi-Baobiao-C/Phone-{0}", "18665569506"), Encoding.UTF8.GetBytes(strValue));
            var publishResult = client.Publish("user/77704b2114a217ae", Encoding.UTF8.GetBytes(strValue));
            //var publishResult = client.Publish("mJiaQi-Baobiao-B/All", Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(model)));

            //client.Publish(string.Format("user{0}", "18665569506"), Encoding.UTF8.GetBytes(strValue));

            Console.WriteLine(string.Format("this is publish! connect status is{0} end publishResult is :{1}", client.IsConnected, publishResult.ToString()));

        }
        /// <summary>
        /// 新增用户
        /// </summary>
        public static void AddUser()
        {

            //--delete  from tbDepartments
            //--delete from tbUsers
            //--delete from tbCases
            //--delete from tbCaseDeploys
            //--delete from tbRoles
          

            var basemanage = new BaseManage();
            var modelDepartment = new tbDepartment()
            {
                dCode = Bll.Common.RandomIdGenerator.GetBase62(16),
                dName = "刑警队"
            };
            var modelRole = new tbRole() {
                rName="admin",
                rDesc="管理员账号",
                rEditTime=DateTime.Now,
                uCode="hexiang"
            };
            basemanage.AddRole(modelRole);
            basemanage.AddDepartment(modelDepartment);
            basemanage.AddUser(new tbUser() { 
                uName="贺祥",
                uCode="hexiang",
                uTel = "18000000000",
                dCode=modelDepartment.dCode,
                uDuty="普通警员",
                //uSex=(byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum="001",
                rName=modelRole.rName
                
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "白垣伟",
                uCode = "baiyuanwei",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "普通警员",
                //uSex = (byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum = "002",
                rName = modelRole.rName
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "王连富",
                uCode = "wanglianfu",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "普通警员",
              //  uSex = (byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum = "003",
                rName = modelRole.rName
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "陈新梅",
                uCode = "chenxinmei",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "刑警副队长",
               // uSex = (byte)Bll.Common.Configs.USsex.Female.GetHashCode(),
                pcNum = "004",
                rName = modelRole.rName
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "梁汗裕",
                uCode = "lianghanyu",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "普通警员",
              //  uSex = (byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum = "005",
                rName = modelRole.rName
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "王信",
                uCode = "wangxin",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "刑警队长",
              //  uSex = (byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum = "006",
                rName = modelRole.rName
            });
            basemanage.AddUser(new tbUser()
            {
                uName = "唐志平",
                uCode = "tangzhiping",
                uTel = "18000000000",
                dCode = modelDepartment.dCode,
                uDuty = "普通警员",
              //  uSex = (byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
                pcNum = "007",
                rName = modelRole.rName
            });

        }

        /// <summary>
        /// 创建案件并且部署警力
        /// </summary>
        //public static void AddCase()
        //{
        //    var basemange = new BaseManage();
        //    //创建案件
        //    var model = basemange.AddCase(new tbCas()
        //    {
        //        csName = "钓鱼岛事件",
        //        uCode="chengxinmei",
        //        csCode=Bll.Common.RandomIdGenerator.GetBase62(24),
        //        csRptName="奥巴马",
        //        csRptSex=(byte)Bll.Common.Configs.USsex.Man.GetHashCode(),
        //        csRptAge=(byte)31,
        //        csSuspCount = (byte)20,
        //        csHurtCount = (byte)10,
        //        csDeadCount = (byte)0,
        //        csLoseVal=999999999,
        //        csCaptureVal=1000,
        //        csCaptCount=20,
        //        csGrpCount=20,
        //        csGrpInvCount=10,
        //        csRescueCount=1000,
        //        csEndChainCount=10
        //    });

        //    var Users = basemange.GetUsers();
        //    var caseModel = (tbCas)model.Entity;
        //    if (model.IsSuccess)
        //    {
        //        foreach (tbUser user in Users)
        //        {
        //            //为案件部署警力
        //            basemange.AddCaseDeploy(new tbCaseDeploy()
        //            {
        //                uCode =user.uCode,
        //                csCode=caseModel.csCode
        //            });
        //        }
        //    }

        //}

    }
}
