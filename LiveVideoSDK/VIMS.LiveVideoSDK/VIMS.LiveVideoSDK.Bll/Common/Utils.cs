using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using VIMS.LiveVideoSDK.Model;
using Newtonsoft.Json;
using System.IO;
using System.Text.RegularExpressions;
using System.Web;
using System.Xml;
using System.Net.Http;
namespace VIMS.LiveVideoSDK.Bll.Common
{
    public  abstract class Utils
    {
        public static int ToInt(object obj,int defaultValue=0)
        {
            var result = 0;

            try
            {
                result = Convert.ToInt32(obj);
                
            }
            catch (Exception ex)
            {
                result = defaultValue;
            }

            return result;
        }

        public static double ToDouble(object obj, double defaultValue = 0)
        {
            var result = 0.0;

            try
            {
                result = Convert.ToDouble(obj);

            }
            catch (Exception ex)
            {
                result = defaultValue;
            }

            return result;
        }

        public static decimal ToDecimal(double d, decimal defaultValue = 0)
        {
            try
            {
               return Convert.ToDecimal(d);

            }
            catch (Exception ex)
            {
                return defaultValue;
            }
        }
        public static decimal ToDecimal(double d)
        {
            try
            {
                return Convert.ToDecimal(d);

            }
            catch (Exception ex)
            {
                return 0;
            }
        }
        public static decimal ToDecimal(string value)
        {
            if (string.IsNullOrEmpty(value))
                return 0;
            try
            {
                return Convert.ToDecimal(ToDouble(value));

            }
            catch (Exception ex)
            {
                return 0;
            }
        }
        public static double ToDouble(string value)
        {
            if (string.IsNullOrEmpty(value))
                return 0;
            try
            {
                return Convert.ToDouble(value);

            }
            catch (Exception ex)
            {
                return 0;
            }
        }
        public static DateTime toDatetime(string timeStr)
        {
            if (string.IsNullOrEmpty(timeStr) || timeStr.Equals("null"))
                return DateTime.Now;
            var time = DateTime.Now;
            try
            {
                time = Convert.ToDateTime(timeStr);
            }
            catch (Exception)
            {
                time = DateTime.Now;
            }
            return time;
        }


        /// <summary>
        /// 对象类型转换String 类型
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public static string toString(object obj,string defaultValue="")
        {
            if (obj == null)
                return defaultValue;
            var time = defaultValue;
            try
            {
                time = Convert.ToString(obj);
            }
            catch (Exception)
            {
                time = defaultValue;
            }
            return time;

        }
        public static string toString(object obj)
        {
            return toString(obj,"");

        }
        public static string timeToString( Nullable<System.DateTime> time )
        {
            if (time == null)
            {
                return "";
            }else
            {
     
                try {
                    return ((DateTime)time).ToString("yyyy-MM-dd HH:mm:ss");
                }
                catch (Exception ex) { }
            }
            return "";

        }
        /// <summary>
        /// 程序执行时间测试
        /// </summary>
        /// <param name="dateBegin">开始时间</param>
        /// <param name="dateEnd">结束时间</param>
        /// <returns>返回(秒)单位，比如: 0.00239秒</returns>
        public static double DateDiff(DateTime dateBegin, DateTime dateEnd)
        {
            var ts1 = new TimeSpan(dateBegin.Ticks);
            var ts2 = new TimeSpan(dateEnd.Ticks);
            var ts3 = ts1.Subtract(ts2).Duration();
            //你想转的格式
            return ts3.TotalMilliseconds;
        }

        public static string GetAppSetting(string appKey)
        {
            var result = string.Empty;

            try {

                result = System.Configuration.ConfigurationManager.AppSettings[appKey].ToString();
            }
            catch { }
            return result;
        }
        public static string GetUniqueId()
        {

            long i = 1;

            foreach (byte b in Guid.NewGuid().ToByteArray())
            {

                i *= ((int)b + 1);

            }

            return string.Format("{0:x}", i - DateTime.Now.Ticks);

        }

        public static string toJson(ResultEx model)
        {
            var result = string.Empty;
            if (model == null)
                return result;

            result += "{";
            result += "{Exception:\""+model.Exception+"\",";
            result += "{IsSuccess:\"" + model.IsSuccess + "\",";
            result += "{Message:\"" + model.Message + "\",";
            result += "{ResultCode:\"" + model.ResultCode + "\",";
            result += "{Entity:" + JsonConvert.SerializeObject(model.Entity) + ",";
            result += "}";
            return result;
        
        }


        public static string Format(string originFileName, string pathFormat)
        {
            if (String.IsNullOrWhiteSpace(pathFormat))
            {
                pathFormat = "{filename}{rand:6}";
            }

            var invalidPattern = new Regex(@"[\\\/\:\*\?\042\<\>\|]");
            originFileName = invalidPattern.Replace(originFileName, "");

            string extension = Path.GetExtension(originFileName);
            string filename = Path.GetFileNameWithoutExtension(originFileName);

            pathFormat = pathFormat.Replace("{filename}", filename);
            pathFormat = new Regex(@"\{rand(\:?)(\d+)\}", RegexOptions.Compiled).Replace(pathFormat, new MatchEvaluator(delegate(Match match)
            {
                var digit = 6;
                if (match.Groups.Count > 2)
                {
                    digit = Convert.ToInt32(match.Groups[2].Value);
                }
                var rand = new Random();
                return rand.Next((int)Math.Pow(10, digit), (int)Math.Pow(10, digit + 1)).ToString();
            }));

            pathFormat = pathFormat.Replace("{time}", DateTime.Now.Ticks.ToString());
            pathFormat = pathFormat.Replace("{yyyy}", DateTime.Now.Year.ToString());
            pathFormat = pathFormat.Replace("{yy}", (DateTime.Now.Year % 100).ToString("D2"));
            pathFormat = pathFormat.Replace("{mm}", DateTime.Now.Month.ToString("D2"));
            pathFormat = pathFormat.Replace("{dd}", DateTime.Now.Day.ToString("D2"));
            pathFormat = pathFormat.Replace("{hh}", DateTime.Now.Hour.ToString("D2"));
            pathFormat = pathFormat.Replace("{ii}", DateTime.Now.Minute.ToString("D2"));
            pathFormat = pathFormat.Replace("{ss}", DateTime.Now.Second.ToString("D2"));

            return pathFormat + extension;
        }

        /// <summary>  
        /// 获取时间戳  
        /// </summary>  
        /// <returns></returns>  
        public static string GetTimeStamp()
        {
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
            return Convert.ToInt64(ts.TotalSeconds).ToString();
        }
        public static byte[] GetTimeStampBits()
        {
            DateTime now = DateTime.Now;

            byte[] bts = BitConverter.GetBytes(now.ToBinary());
            return bts;
        }


        public static string toIntFromByte(byte data)
        {
            byte[] shi = System.BitConverter.GetBytes(data);
            int sh = System.BitConverter.ToInt32(shi, 0);
            return toString(sh);
        
        }

        /// <summary>
        /// 相对路径变为网站路径url
        /// </summary>
        /// <param name="relativePath"></param>
        /// <returns></returns>
        public static string toSiteUrl(string relativePath)
        {
            var  url=string.Empty;
            if (string.IsNullOrEmpty(relativePath))
                return url;
            if (relativePath.StartsWith("http:")|| relativePath.StartsWith("https:"))
            {
                return relativePath;
            }
            //var siteRoot = HttpContext.Current.Request.Url.GetLeftPart(UriPartial.Authority).ToString();
            var siteRoot =GetAppSetting("LiveVideoSDK_Image_Url");
            if (String.IsNullOrEmpty(siteRoot))
            {
                siteRoot = HttpContext.Current.Request.Url.GetLeftPart(UriPartial.Authority).ToString();
            }
            url = siteRoot + relativePath;
            return url;

        }


        public static string getMp4NameUrlFromRtmp(string rtmpUrl,int counts)
        {
            //rtmp://192.168.8.138:1935/vod/mp4:hx8e5_0.mp4
            var mp4Url = string.Empty;
            var streamName = string.Empty;
            if (string.IsNullOrEmpty(rtmpUrl))
                return mp4Url;

            if (rtmpUrl.LastIndexOf("/") > 0 && rtmpUrl.Length > (rtmpUrl.LastIndexOf("/")+1))
            {
                streamName = rtmpUrl.Substring(rtmpUrl.LastIndexOf("/")+1);
            }
            if (counts > 0)
            {
                streamName = streamName + "_" + Common.Utils.toString(counts - 1);
            }
   

            mp4Url = GetAppSetting("LiveVideoSDK_RtmpUrl") + "vod/mp4:" + streamName + ".mp4";

            return mp4Url;
        }


        /// <summary>
        /// 获取直播流
        /// </summary>
        /// <returns></returns>
        public static List<IncomingStream> getIncomingStream()
        {
            var models = new List<IncomingStream>();

            try {
    
                var rtmpIp = Utils.GetAppSetting("RtmpServerIp");
                rtmpIp = rtmpIp.ToLower().Replace("rtmp://", "");
                if (rtmpIp.IndexOf(":") > 0)
                {
                    rtmpIp = rtmpIp.Substring(0, rtmpIp.IndexOf(":"));
                }
                HttpClient httpClient = new HttpClient();
                HttpResponseMessage response = httpClient.GetAsync(new Uri("http://" + rtmpIp + ":8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_definst_")).Result;
                String result = response.Content.ReadAsStringAsync().Result;

                var xmlDoc = new XmlDocument();
                xmlDoc.LoadXml(result); //加载xml文件

                var nodelRoot = xmlDoc.SelectSingleNode("Instance");
                if (nodelRoot == null)
                    return null;
                nodelRoot = nodelRoot.SelectSingleNode("IncomingStreams");
                if (nodelRoot == null)
                    return null;
                XmlNodeList xnl = nodelRoot.SelectNodes("IncomingStream");

                foreach (XmlNode xnf in xnl)
                {
                    var model = new IncomingStream();
                    foreach (XmlNode node in xnf.ChildNodes)
                    {

                        if (node.Name.Equals("ApplicationInstance"))
                            model.ApplicationInstance = node.InnerText;
                        if (node.Name.Equals("IsConnected"))
                            model.IsConnected = node.InnerText;
                        if (node.Name.Equals("IsPTZEnabled"))
                            model.IsPTZEnabled = node.InnerText;
                        if (node.Name.Equals("IsPublishedToVOD"))
                            model.IsPublishedToVOD = node.InnerText;
                        if (node.Name.Equals("IsRecordingSet"))
                            model.IsRecordingSet = node.InnerText;
                        if (node.Name.Equals("IsStreamManagerStream"))
                            model.IsStreamManagerStream = node.InnerText;
                        if (node.Name.Equals("Name"))
                            model.Name = node.InnerText;
                        if (node.Name.Equals("PtzPollingInterval"))
                            model.PtzPollingInterval = node.InnerText;
                        if (node.Name.Equals("SourceIp"))
                            model.SourceIp = node.InnerText;

                    }


                    if (!string.IsNullOrEmpty(model.Name))
                        models.Add(model);
                }
            
            }
            catch {
                return null;
            }
            return models;
        }

        /// <summary>
        /// 获取历史视频
        /// </summary>
        /// <returns></returns>
        public static List<VideoModel> getVideoNames()
        {
            return getVideoNames("","");
        }
        /// <summary>
        /// 获取历史视频
        /// </summary>
        /// <param name="streamName">直播流的名字</param>
        /// <param name="suffix">去除视频后缀</param>
        /// <returns></returns>
        public static List<VideoModel> getVideoNames(string streamName, string suffix)
        {
  
            var results=new List<VideoModel>();

            try
            {
                streamName = (string.IsNullOrEmpty(streamName) ? "" : streamName);
                suffix = (string.IsNullOrEmpty(suffix) ? "" : suffix);
                var rtmpIp = Utils.GetAppSetting("RtmpServerIp");
                rtmpIp = rtmpIp.ToLower().Replace("rtmp://", "");
                if (rtmpIp.IndexOf(":") > 0)
                {
                    rtmpIp = rtmpIp.Substring(0, rtmpIp.IndexOf(":"));
                }
                HttpClient httpClient = new HttpClient();
                HttpResponseMessage response = httpClient.GetAsync(new Uri("http://" + rtmpIp + ":8086/getvideonames?stream="+streamName+"&suffix="+suffix)).Result;
                String result = response.Content.ReadAsStringAsync().Result;

                var xmlDoc = new XmlDocument();
                xmlDoc.LoadXml(result); //加载xml文件

                var nodelRoot = xmlDoc.SelectSingleNode("VideoNameRoot");
                if (nodelRoot == null)
                    return null;
                //nodelRoot = nodelRoot.SelectSingleNode("IncomingStreams");
                //if (nodelRoot == null)
                //    return null;
                XmlNodeList xnl = nodelRoot.SelectNodes("VideoName");

                foreach (XmlNode xnf in xnl)
                {
                    var model = new IncomingStream();
       
                        var videoSize = string.Empty;
                        var videoTime = string.Empty;
                        try {
                            videoSize = xnf.Attributes["VideoSize"].Value;
                            videoTime = xnf.Attributes["VideoTime"].Value;
                        }
                        catch { }
                        results.Add(new VideoModel() {

                            VideLength = Utils.toString(videoSize),
                            VideoEndTime = Utils.toString(videoTime),
                            LastModifyTime = Utils.toString(videoTime),
                            VideoName = Utils.toString(xnf.InnerText),
                            VideoUrl = getRtmpMp4UrlBySreamName(xnf.InnerText),
                            StreamName=getStreamName(xnf.InnerText),
                        });
                    }
                

            }
            catch
            {
                return null;
            }
            return results;
        }

        /// <summary>
        /// 统计此设备的历史视频数量
        /// </summary>
        /// <param name="streamName"></param>
        /// <returns></returns>
        public static int getVideoCountByStreamName(string streamName)
        {
            var count = 0;
            var videoModels = getVideoNames(streamName, ".mp4");
            if (videoModels == null)
            {
                return count;
            }
            count = videoModels.Where(m => !m.VideoName.Contains("_360p")&& !m.VideoName.Contains("_720p") && !m.VideoName.Contains("_240p") && !m.VideoName.Contains("_160p") && !m.VideoName.Contains(".tmp")).Count();
            return count;
        }
        public static string getStreamName(string videoName)
        {
            string result = string.Empty;

                if(string.IsNullOrEmpty(videoName))
                    return result;
                var lastIndexDot = videoName.LastIndexOf('.');
                var lastHuaXian = videoName.LastIndexOf('_');
                if (lastHuaXian > 0)
                {
                    result = videoName.Substring(0,lastHuaXian);
                }
                else if (lastIndexDot > 0)
                {
                    result = videoName.Substring(0,lastIndexDot);
                }
            return result;
        
        }
        /// <summary>
        /// 获取直播流名字从推流地址里面
        /// </summary>
        /// <param name="rtmpUrl"></param>
        /// <returns></returns>
        public static string getStreamNameFromRtmpUrl(string rtmpUrl)
        {
            var streamName = string.Empty;
            if (string.IsNullOrEmpty(rtmpUrl))
                return streamName;
            if (rtmpUrl.LastIndexOf("/") > 0 && (rtmpUrl.LastIndexOf("/") + 1) < rtmpUrl.Length)
                streamName = rtmpUrl.Substring(rtmpUrl.LastIndexOf("/") + 1);

            return streamName;
        }
        /// <summary>
        /// 获取直播流的名字
        /// </summary>
        /// <param name="rtmpUrl"></param>
        /// <returns></returns>
        public static List<string> getIncomingStreamNames()
        {
            var listKeys = new List<string>();
            var livingModels = getIncomingStream();
            if (livingModels == null)
                return listKeys;
            foreach (var model in livingModels)
            {
                if (!string.IsNullOrEmpty(model.Name))
                    listKeys.Add(model.Name);
            }

            return listKeys;
        }
         public static Boolean isConatinMsgType(string msgType)
        {
            var listMsgTyps = new List<string>();

            foreach (MsgTypes c in (MsgTypes[])Enum.GetValues(typeof(MsgTypes)))
            {
                listMsgTyps.Add(c.ToString());
            }
            if (string.IsNullOrEmpty(msgType) || !listMsgTyps.Contains(msgType.Trim()))
                return false;

            return true;
        }
         public static MsgTypes getDefaultMsgTYpe()
        {
            return MsgTypes.Text;
        }
        /// <summary>
        /// 把流的名字直接拼成直播地址
        /// </summary>
        /// <returns></returns>
         public static string getRrtmUrlBySreamName(string streamName)
         {
             var streamRtmpUrl = string.Empty;

             if (string.IsNullOrEmpty(streamName))
                 return streamRtmpUrl;

             var rtmpIp = Utils.GetAppSetting("LiveVideoSDK_RtmpUrl");
             if (!string.IsNullOrEmpty(rtmpIp))
             {
                 streamRtmpUrl = rtmpIp + "live/" + streamName;
             }

             return streamRtmpUrl;
         }

         public static string getRtmpMp4UrlBySreamName(string videoName)
         {
             var streamRtmpUrl = string.Empty;

             if (string.IsNullOrEmpty(videoName))
                 return streamRtmpUrl;

             var rtmpIp = Utils.GetAppSetting("LiveVideoSDK_RtmpUrl");
             if (!string.IsNullOrEmpty(rtmpIp))
             {
                 streamRtmpUrl = rtmpIp + "vod/mp4:" + videoName;
             }

             return streamRtmpUrl;
         }


        public static string emptyIfNulll(string value)
        {
            return string.IsNullOrEmpty(value) ? string.Empty : value;
        }
    }
}
