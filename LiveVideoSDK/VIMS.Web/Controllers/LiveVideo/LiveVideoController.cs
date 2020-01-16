using JQSoft.Data.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using VIMS.Entity;
using VIMS.LiveVideoSDK.Bll;
using VIMS.Services.CaseInvestigation;
using VIMS.Services.Impl.LiveCommand;
using VIMS.Services.LiveCommand;

namespace VIMS.Web.Controllers
{
    public class LiveVideoController : Controller
    {
        private ILiveVideoService c = null;
        //
        // GET: /LiveVideo/LiveVideo/-视频直播

        public LiveVideoController(ILiveVideoService _c)
        {
            c = _c;
        }
        public ActionResult Index()
        {
            return View();
        }
        /// <summary>
        /// 获取所有直播
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public JsonResult GetLiveVideo()
        {
            var results = c.GetLiveVideo(JQSoft.AppContext.Current.User.SystemName, JQSoft.AppContext.Current.User.Roles);

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
            }
            catch (Exception)
            {

            }
            return Json(results);
        }
    }
}
