using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JQSoft.Web.Mvc;
using VIMS.Services.Judge;
using VIMS.DataModel.Judge;
using VIMS.Services.Impl;
using System.Text;

namespace VIMS.Web.Controllers
{
    public class JudgeController : Controller
    {

        private IJudgeService _ijudgeService = null;

        public JudgeController(IJudgeService ijudgeService)
        {
            _ijudgeService = ijudgeService;

        }

        public ActionResult Index()
        {
            return View("Trackrecord");
        }

        //绑定列表
        public JsonResult GetCases(int limit, int offset, string csName, string starttime, string endtime)
        {
            var list = _ijudgeService.GetCaseInfo(csName, starttime, endtime);
            var total = list.Count;
            var rows = list.Skip(offset).Take(limit).ToList();
            return Json(new { total = list.Count, rows = rows }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        //绑定模糊查询事件
        public JsonResult GetCaseInfo(string csName)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            IList<TrackRecord> _ilistjudge = _ijudgeService.GetCaseInfoByCaseName(csName,role,ucode);
            return Json(_ilistjudge);
        }

        [HttpPost]
        //显示人物最后位置
        public JsonResult GetPeoploPosition(string startTime, string endTime, string csName)
        {
            var list = _ijudgeService.GetPeoploPositionInfo(csName, startTime, endTime);
            //return Json(list, JsonRequestBehavior.AllowGet);
            return Json(list);
        }

        [HttpPost]
        //播放人物位置
        public JsonResult GetPeoploPositionlatest(string startTime, string endTime, string csName, string uCode)
        {
            if (uCode.Length > 0)
            {
                uCode = uCode.Substring(0, uCode.Length - 1);
            }
            List<TrackRecord> query = new List<TrackRecord>();
            var list = _ijudgeService.GetPeoploPositionInfo(csName, startTime, endTime);
            var ll = uCode.Split(',').Length;

            for (var i = 0; i < ll; i++)
            {
                string a = uCode.Split(',')[i];
                for (var j = 0; j < list.Count; j++)
                {

                    string b = (list[j].uCoded).Trim();
                    if (b == a)
                    {
                        TrackRecord item = new TrackRecord();
                        item.lastTimed = list[j].lastTimed;
                        item.uCoded = list[j].uCoded;
                        item.gpsLongituded = list[j].gpsLongituded;
                        item.gpsLatituded = list[j].gpsLatituded;
                        query.Add(item);
                        break;
                    }
                }
            }
            return Json(query);
        }

        [HttpPost]
        //获得人物轨迹
        public JsonResult GetRecordByuCode(string startTime, string endTime, string csName, string uCode)
        {
            DateTime StartTime = DateTime.Parse(startTime);
            DateTime EndTime = DateTime.Parse(endTime);
            IList<TrackRecord> _ilistjudge = _ijudgeService.GetRecordInfoByuCodecsName(csName, StartTime, EndTime, uCode);
            var result = Json(_ilistjudge);
            result.MaxJsonLength = int.MaxValue;
            return result;
        }

        [HttpPost]
        //获取案件轨迹的开始和结束时间
        public JsonResult GetCaseTimeInfo(string csName)
        {
            IList<TrackRecord> _ilistjudge = _ijudgeService.GetCaseTimeInfoByCaseName(csName);
            return Json(_ilistjudge);
        }

        [HttpPost]
        //通过案件名取得所有的轨迹数据
        public JsonResult GetRecordBycsName(string startTime, string endTime, string csName)
        {
            DateTime StartTime = DateTime.Parse(startTime);
            DateTime EndTime = DateTime.Parse(endTime);
            IList<TrackRecord> _ilistjudge = _ijudgeService.GetRecordInfoBycsName(csName, StartTime, EndTime);
            _ilistjudge.OrderBy(p => p.uCoded);
            var result = Json(_ilistjudge);
            result.MaxJsonLength = int.MaxValue;
            return result;
        }
    }


   
    

}
