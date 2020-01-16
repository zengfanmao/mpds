using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using VIMS.LiveVideoSDK.Model;
using VIMS.Services.LiveCommand;
using JQSoft;
using JQSoft.Web.Mvc;
using VIMS.Web.Models;
using VIMS.DataModel.SystemManagement;
using VIMS.DataModel.LiveCommand;
using VIMS.DataModel;
using VIMS.DataModel.CaseInvestigation;
using VIMS.LiveVideoSDK.Bll.Common;

namespace VIMS.Web.Controllers
{
    public class CaseLiveController : Controller
    {
        private ICaseLiveService _caseLive;

        public CaseLiveController(ICaseLiveService caseLive)
        {
            this._caseLive = caseLive;
        }

        //
        // GET: /CaseLive/

        public ActionResult CaseLive()
        {
            ViewData["_id"] = Request.QueryString["_id"];

            return View();
        }

        /// <summary>
        /// 警情通报 页面
        /// </summary>
        /// <returns></returns>
        public ActionResult PoliceNotice()
        {
            return View();
        }

        /// <summary>
        /// 案情记录 页面
        /// </summary>
        /// <returns></returns>
        public ActionResult CaseRecord()
        {
            return View();
        }

        public ActionResult CaseCommunication()
        {
            ViewData["caseId"] = Request.QueryString["caseId"];
            ViewData["userName"] = Request.QueryString["userName"];
            ViewData["password"] = Request.QueryString["password"];
            return View();

        }


        public ActionResult CaseCommunicationError()
        {
            return View();
        }

        /// <summary>
        /// 获取案件中成员列表
        /// </summary>
        /// <param name="csCode"></param>
        /// <returns></returns>
        [HttpPost]
        public JsonResult GetCaseUsers(string csCode)
        {
            IList<User> caseUsers = null;
            try
            {
                caseUsers = this._caseLive.GetCaseMembers(csCode);

            }
            catch (Exception ex)
            {
                caseUsers = new List<User>();
            }
            return Json(new { total = caseUsers.Count, rows = caseUsers });
        }

        /// <summary>
        /// 获取通信代理服务器ip
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public string GetCommunicationProxyServerIp()
        {
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "获取失败" };
            try
            {
                string ImIP = Utils.GetAppSetting("LiveVideoSDK_IM_Ip");
                if (!string.IsNullOrEmpty(ImIP))
                {
                    result.Entity = JsonConvert.SerializeObject(ImIP);
                    result.IsSuccess = true;
                    result.Message = "获取成功";
                    result.ResultCode = "1";
                }
            }
            catch
            {
            }
            return JsonConvert.SerializeObject(result);
        }

        #region 讨论组

        /// <summary>
        /// 删除讨论组成员
        /// </summary>
        /// <param name="groupCode"></param>
        /// <param name="userCode"></param>
        /// <returns></returns>
        [HttpPost]
        public JsonResult DeleteDiscussionGroupMember(string groupCode, string userCode)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                this._caseLive.DeleteDiscussionGroupMember(groupCode, userCode);
                ajaxResult.IsSuccess = true;
            }
            catch (Exception e)
            {
                ajaxResult.IsSuccess = false;
            }
            return Json(ajaxResult);
        }


        /// <summary>
        /// 删除讨论组
        /// </summary>
        /// <param name="Id"></param>
        /// <returns></returns>
        [HttpPost]
        public JsonResult DeleteDiscussionGroup(string groupCode)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                this._caseLive.DeleteDiscussionGroup(groupCode);
                ajaxResult.IsSuccess = true;
            }
            catch (Exception e)
            {
                ajaxResult.IsSuccess = false;
            }
            return Json(ajaxResult);
        }

        [HttpPost]
        public JsonResult GetGroupMembers(string groupCode)
        {
            IList<User> groupUsers = null;
            try
            {
                groupUsers = this._caseLive.GetDiscussionGroupMembers(groupCode);
            }
            catch (Exception ex)
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                groupUsers = new List<User>();
            }
            return Json(new { total = groupUsers.Count, rows = groupUsers });
        }

        [HttpPost]
        public JsonResult GetCaseDiscussionGroups(string csCode, string userCode)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                var groups = this._caseLive.GetCaseDiscussionGroups(csCode, userCode);
                ajaxResult.Data = groups;
                ajaxResult.IsSuccess = true;
            }
            catch (Exception ex)
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                // ajaxResult.Msg = "获取案件讨论组失败";
            }
            return Json(ajaxResult);
        }

        [HttpPost]
        public JsonResult GetCaseGroupMembers(string csCode, string gCode)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                var memberUsers = this._caseLive.GetCaseUsers(csCode);

                List<JsonTreeNode> nodeList = new List<JsonTreeNode>(1);
                JsonTreeNode caseNode = new JsonTreeNode()
                {
                    id = csCode,
                    text = "",
                    children = new List<JsonTreeNode>(memberUsers.Count)
                };
                if (!string.IsNullOrEmpty(gCode))
                {
                    var groupUsers = this._caseLive.GetDiscussionGroupMembers(gCode);
                    var userNodes = memberUsers.Select(f => new JsonTreeNode()
                    {
                        id = f.uCode,
                        name = f.uName + "  " + f.rName,
                        text = f.uName + "  " + f.rName,
                        checkedState = groupUsers.Any(u => u.Code == f.uCode) ? true : false,
                    });
                    caseNode.children.AddRange(userNodes);
                }
                else
                {
                    var userNodes = memberUsers.Select(f => new JsonTreeNode()
                    {
                        id = f.uCode,
                        name = f.uName + "  " + f.rName,
                        text = f.uName + "  " + f.rName,
                        checkedState = false,
                    });
                    caseNode.children.AddRange(userNodes);
                }
                nodeList.Add(caseNode);
                ajaxResult.Data = nodeList;
                ajaxResult.IsSuccess = true;
            }
            catch (Exception ex)
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "菜单树加载失败";
            }
            return Json(ajaxResult);
        }


        [HttpPost]
        public JsonResult SaveCaseGroupMembers(string caseCode, string groupCode, string groupName, string memberCodes)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                DiscussionGroup disGroup = new DiscussionGroup()
                {
                    //Id = groupId,
                    DiscussionCode = groupCode,
                    CaseCode = caseCode,
                    DiscussionName = groupName,
                    Members = memberCodes.Split(',').ToList()
                };
                DiscussionGroup group = this._caseLive.SaveDiscussionGroup(disGroup);
                switch (group.DataOptionStatus)
                {
                    case DataOperationStatus.OK:
                        ajaxResult.Data = disGroup;
                        ajaxResult.IsSuccess = true;
                        ajaxResult.Msg = "";
                        break;

                    case DataOperationStatus.DataDuplicated:
                        ajaxResult.IsSuccess = false;
                        ajaxResult.Msg = "讨论组已存在！";
                        break;

                    case DataOperationStatus.DataNotFound:
                        ajaxResult.IsSuccess = false;
                        ajaxResult.Msg = "数据未找到！";
                        break;
                }
            }
            catch (Exception ex)
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "菜单树保存失败";
            }
            return Json(ajaxResult);
        }
        #endregion


        /// <summary>
        /// 获取当前案件的通报
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCaseNotices(string csCode)
        {
            try
            {
                var result = new ResultEx() { Message = "getCaseNotices return NNNN." };

                var models = this._caseLive.GetCaseNotices(csCode);

                for (var i = 0; i < models.Count; ++i)
                {
                    models[i].CreateTime = System.Convert.ToDateTime(models[i].cgCreateTime).ToString("yyyy-MM-dd HH:mm:ss");
                }

                if (models != null && models.Any())
                {
                    result.IsSuccess = true;
                    result.ResultCode = "1";
                    result.Message = "获取成功";
                    result.Entity = JsonConvert.SerializeObject(models);
                }
                else
                {
                    result.Entity = "";
                    result.IsSuccess = false;
                    result.Message = "没有数据";
                }

                return JsonConvert.SerializeObject(result);
            }
            catch (Exception ex)
            {
                var result = new ResultEx() { Message = "getCaseNotices return NNNN." };
                result.Entity = ex.ToString();
                result.IsSuccess = false;
                result.Message = "getCaseNotices failed.";

                return JsonConvert.SerializeObject(result);
            }
        }

        /// <summary>
        /// 删除通报
        /// </summary>
        /// <param name="Id">通报ID</param>
        /// <returns></returns>
        public String DeleteNotice(string Id)
        {
            try
            {
                var result = new ResultEx() { Message = "删除失败." };

                this._caseLive.DeleteNotice(Id);

                result.IsSuccess = true;
                result.ResultCode = "1";
                result.Message = "删除通报成功";
                result.Entity = "";

                return JsonConvert.SerializeObject(result);
            }
            catch (Exception e)
            {
                var result = new ResultEx() { Message = "删除失败." };

                result.Entity = "";
                result.IsSuccess = false;
                result.Message += e.ToString();

                return JsonConvert.SerializeObject(result);
            }
        }

        /// <summary>
        /// 查询登录用户信息
        /// </summary>
        /// <returns></returns>
        public String QueryUserInfo()
        {
            try
            {
                var result = new ResultEx() { Message = "QueryUserInfo return NNNN." };
                string user = AppContext.Current.User.SystemName;
                string password = AppContext.Current.User.Password;

                if ((null != user) && (null != password))
                {
                    string entity = "{\"UserName\":\"" + user.Trim() + "\",\"Password\":\"" + password.Trim() + "\"}";

                    result.IsSuccess = true;
                    result.ResultCode = "1";
                    result.Message = "获取成功";
                    result.Entity = entity;
                }
                else
                {
                    result.Entity = "";
                    result.IsSuccess = false;
                    result.Message = "获取用户信息失败";
                }

                return JsonConvert.SerializeObject(result);
            }
            catch (Exception ex)
            {
                var result = new ResultEx() { };
                result.Entity = ex.ToString();
                result.IsSuccess = false;
                result.Message = "获取用户信息失败";
                return JsonConvert.SerializeObject(result);
            }
        }
    }
}
