using JQSoft.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using VIMS.DataModel.LiveVideo;
using VIMS.Services.LiveCommand;
using VIMS.Web.Infrastructure.Models;
using VIMS.Web.Models;

namespace VIMS.Web.Controllers.LiveVideo
{
    public class VideoPlayBackController : Controller
    {
        //
        // GET: /VideoPlayback/

        private ILiveVideoService c = null;
        //
        // GET: /LiveVideo/LiveVideo/-视频直播
        public VideoPlayBackController(ILiveVideoService _c)
        {
            c = _c;
        }

        public ActionResult Index()
        {
            nPageInfo pageinfo = new nPageInfo();
            pageinfo.SortExpression = "CreatedTime asc";
            var results = c.GetVideoHistory(pageinfo, null, JQSoft.AppContext.Current.User.SystemName, JQSoft.AppContext.Current.User.Roles);
            ViewData["historyVideo"] = results;
            return View();
        }
        /// <summary>
        /// 获取历史视频
        /// </summary>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        //[HttpPost]
        //public ActionResult GetVideoHistory(string skip, string take)
        //{
        //    var results = c.GetVideoHistory(skip, take);
        //    ViewData["historyVideo"] = results;
        //    return View();

        //}

        //public ActionResult GetVideoHistory(int limit, int offset, string csCode,string csType,string csName)
        //{
        //    var list = c.GetVideoHistory(csCode, csType, csName);
        //    var total = list.Count;
        //    var rows = list.Skip(offset).Take(limit).ToList();
        //    return Json(new { total = list.Count, rows = rows }, JsonRequestBehavior.AllowGet);
        //}
        [HttpPost, AllowAnonymous]
        public JsonResult GetVideoHistory(nPageInfo pageInfo, nSearchCondition condition)
        {
            JqGridData<LiveVideoHistoryModel> gridData = null;
            try
            {
                IPagedList<LiveVideoHistoryModel> userPagedList = c.GetVideoHistory(pageInfo, condition, JQSoft.AppContext.Current.User.SystemName, JQSoft.AppContext.Current.User.Roles);
                gridData = JqGridData<LiveVideoHistoryModel>.Create(userPagedList);
                ViewBag.SignPage = pageInfo.PageIndex;
            }
            catch (Exception ex)
            {
                gridData = new JqGridData<LiveVideoHistoryModel>();
                //Logger.Current.Log(LogType.Error, ex.ToString());
            }
            return this.Json(gridData);
        }
        [HttpPost]
        public ActionResult Edit(LiveVideoHistoryModel historyModel, int page = 1)
        {
            c.Edit(historyModel);
            if (ViewBag.SignPage != null)
            {
                page = ViewBag.SignPage;
            }
            //Response.Write("<script>alert('您的信息已提交成功，等待后台审核');</script>");
            return RedirectToAction("Index", new { page });
            return View(historyModel);
        }
        public ActionResult Edit(string id, int page = 1)
        {
            var modelInfo = c.GetById(id);
            ViewBag.SignPage = page;
            return View(modelInfo);
        }
        public ActionResult VideoPlay(string id)
        {
            return View();
            var modelInfo = c.GetById(id);
            return View(modelInfo);
        }
        public string GetRtmpUrlById(string id)
        {
            var modelInfo = c.GetById(id);
            return modelInfo.RtmpUrl;
        }
        public string GetCaseIdById(string id)
        {
            var modelInfo = c.GetById(id);
            return modelInfo.CaseId;
        }
        //根据Id获取涉案人员记录
        public JsonResult GetVideoHistoryById()
        {
            var id = Request.QueryString["id"];
            var list = c.GetById(id);
            return Json(list);
        }
        //预览案件详情
        public ActionResult CaseView()
        {
            var a = Request.QueryString["_csId"];
            ViewData["_csId"] = a;
            return View("CaseView");
        }
    }
}
