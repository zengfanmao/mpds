using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using System.Web.Mvc;
using VIMS.DataModel.Judge;
using VIMS.DataModel.LiveCommand;
using VIMS.Services.Judge;
using VIMS.Services.LiveCommand;
using VIMS.Services.SystemManagement;

namespace VIMS.Web.Controllers.LiveVideo
{
    public class VideoInterFaceController : Controller
    {
        //
        // GET: /VideoInterface/
        private ILiveVideoService c = null;
        private ICaseLiveService caseService = null;
        private IUserService userService = null;
        private IJudgeService judgeService = null;
        //
        // GET: /LiveVideo/LiveVideo/-视频直播

        public VideoInterFaceController(ILiveVideoService _c, IUserService _userService, ICaseLiveService _caseService, IJudgeService _judgeService)
        {
            //HttpContext.Response.AppendHeader("Access-Control-Allow-Origin", "*");
            //HttpContext.Response.AppendHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");

            //HttpContext.Response.AppendHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
            c = _c;
            userService = _userService;
            caseService = _caseService;
            this.judgeService = _judgeService;
        }
        public string Options()
        {

            return null; // HTTP 200 response with empty body

        }

        /// <summary>
        /// 获取所有直播
        /// </summary>
        /// <returns></returns>
        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        public string GetLiveVideo(string userCode, string password)
        //public string GetLiveVideo([FromBody]ApiParams apiParams)
        {
            var result = new { Message = "系统错误，请联系管理员", IsSuccess = false, ResultCode = "-1" };
            try
            {
                string mdPassword = JQSoft.RuntimeUtils.MD5(password).ToUpper();
                var userId = userService.ValidateUser(userCode, mdPassword);
                var role = userService.GetUser(userId).ToString();
                if (role == string.Empty)
                {
                    result = new { Message = "用户名或密码错误", IsSuccess = false, ResultCode = "-2" };
                    return JsonConvert.SerializeObject(result);
                }
                string[] roles = { role };
                var results = c.GetLiveVideo(userCode, roles);

                var models = new List<LiveVideoSDK.Model.IncomingStream>();
                try
                {
                    models = VIMS.LiveVideoSDK.Bll.Common.Utils.getIncomingStream();
                    if (models != null)
                    {
                        //foreach (VIMS.DataModel.LivingVideo.LiveVideo item in results)
                        for (int i = 0; i < results.Count(); i++)
                        {
                            if (results[i].LivingState == "True" && results[i].DevType != "其他" && models.Where(t => results[i].RtmpUrl.Contains(t.Name)).Count() == 0)
                            {
                                results[i].LivingState = "False";
                            }
                            //重复的直播地址
                            if (results[i].DevType == "其他" && models.Where(t => results[i].RtmpUrl.Contains(t.Name)).Count() == 0)
                            {
                                results.RemoveAt(i);
                                i--;
                                continue;
                            }
                            //第三方直播设备(已分配的设备)
                            foreach (var item in models)
                            {
                                if (item.Name == results[i].DevSN)
                                {
                                    results[i].LivingState = "True";
                                    results[i].RtmpUrl = VIMS.LiveVideoSDK.Bll.Common.Utils.getRrtmUrlBySreamName(item.Name);
                                }
                            }
                        }
                    }
                    else
                    {
                        //ViewData["NoVideoServer"] = "true";
                        result = new { Message = "获取直播失败，请联系管理员", IsSuccess = false, ResultCode = "-3" };
                        return JsonConvert.SerializeObject(result);
                    }
                }
                catch (Exception)
                {
                    return JsonConvert.SerializeObject(result);
                }
                return JsonConvert.SerializeObject(results);
            }
            catch (Exception)
            {
                return JsonConvert.SerializeObject(result);
            }

        }
        /// <summary>
        /// 获取讨论组及成员
        /// </summary>
        /// <param name="csCode"></param>
        /// <param name="userCode"></param>
        /// <returns></returns>
        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        public string GetCaseDiscussionGroups(string csCode, string userCode)
        {
            var result = new { Message = "系统错误，请联系管理员", IsSuccess = false, ResultCode = "-1" };
            try
            {
                var results = caseService.GetCaseDiscussionGroups(csCode, userCode);
                if (results.Count == 0)
                {
                    result = new { Message = "没有找到匹配的记录", IsSuccess = false, ResultCode = "-2" };
                    return JsonConvert.SerializeObject(result);
                }
                foreach (var item in results)
                {
                    var members = caseService.GetDiscussionGroupMembers(item.DiscussionCode);
                    item.Members = new List<string>();
                    foreach (var user in members)
                    {
                        item.Members.Add(user.UserName);
                    }
                }
                return JsonConvert.SerializeObject(results);
            }
            catch (Exception)
            {
                return JsonConvert.SerializeObject(result);
            }

        }
        /// <summary>
        /// 取人员最新位置信息
        /// </summary>
        /// <param name="csName"></param>
        /// <returns></returns>
        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        public string GetPeoploPositionInfo_Inner(string csName)
        {
            var result = new { Message = "系统错误，请联系管理员", IsSuccess = false, ResultCode = "-1" };
            try
            {
                var results = judgeService.GetPeoploPositionInfo(csName, "", DateTime.Now.ToString());
                if (results.Count == 0)
                {
                    result = new { Message = "没有找到匹配的记录", IsSuccess = false, ResultCode = "-2" };
                    return JsonConvert.SerializeObject(result);
                }
                return JsonConvert.SerializeObject(results);
            }
            catch (Exception)
            {
                return JsonConvert.SerializeObject(result);
            }
        }
    }
    public class ApiParams
    {
        public ApiParams()
        {
        }
        public string userName { get; set; }
        public string role { get; set; }

    }
}
