using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JQSoft.Web.Mvc;
using VIMS.DataModel;
using VIMS.DataModel.SystemManagement;
using VIMS.Services.SystemManagement;
using VIMS.Web.Models;

namespace VIMS.Web.Test.Controllers
{
    /// <summary>
    /// 权限功能项、菜单
    /// </summary>
    public class PermissionController : BaseController
    {
        private IPermissionService _permissionService;

        public PermissionController(IPermissionService permissionService)
        {
            this._permissionService = permissionService;
        }

        public ActionResult MenuManage()
        {
            return View();
        }

        public ActionResult RoleFunction()
        {
            return View();
        }

        #region Ajax-Request

        /// <summary>
        /// 获取角色列表。
        /// </summary>
        /// <returns></returns>
        [HttpPost, AllowAnonymous]
        public JsonResult GetRoles()
        {
            IList<Role> roleList = null;
            try
            {
                roleList = this._permissionService.GetRoles();
            }
            catch
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                roleList = new List<Role>();
            }
            return Json(new { rows = roleList });
        }

        /// <summary>
        /// 获取功能树数据。
        /// </summary>
        /// <returns></returns>
        [HttpPost, AllowAnonymous]
        public JsonResult GetFunctions()
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                var modules = this._permissionService.GetModules();
                var functions = this._permissionService.GetFunctions();

                List<JsonTreeNode> nodeList = new List<JsonTreeNode>(modules.Count);
                foreach (var module in modules)
                {
                    var node = BuildTreeNodeForModule(module, functions.Where(f => f.ModuleId == module.Id));
                    nodeList.Add(node);
                }

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

        /// <summary>
        /// 获取角色功能项ID。
        /// </summary>
        /// <param name="role"></param>
        /// <returns></returns>
        [HttpPost, AllowAnonymous]
        public JsonResult GetRoleFuncs(string role)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                var funcs = this._permissionService.GetFunctionsByRole(role);
                var funcIds = funcs.Select(f => f.Id).ToList();
                ajaxResult.Data = funcIds;
                ajaxResult.IsSuccess = true;
            }
            catch
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "内部异常！";
            }
            return Json(ajaxResult);
        }

        /// <summary>
        /// 保存角色功能设置。
        /// </summary>
        /// <param name="role">角色。</param>
        /// <param name="funcIds">功能Id（多个之间以英文字符','分隔）。</param>
        [HttpPost, AllowAnonymous]
        public JsonResult SaveRoleFuncs(string role, string funcIds)
        {
            AjaxResult ajaxResult = new AjaxResult();
            if (string.IsNullOrEmpty(role) || string.IsNullOrEmpty(funcIds))
            {
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "数据异常！";
                return Json(ajaxResult);
            }

            DataOperationStatus status = DataOperationStatus.Unknown;
            try
            {
                status = this._permissionService.SetRoleFuncs(role, funcIds);
            }
            catch
            {
                //Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = "内部异常！";
            }
            switch (status)
            { 
                case DataOperationStatus.OK:
                    ajaxResult.IsSuccess = true;
                    break;

                default:
                    ajaxResult.IsSuccess = false;
                    ajaxResult.Msg = "其它验证问题！";
                    break;
            }
            return Json(ajaxResult);
        }













        //代英磊增加角色的增删改
        [HttpPost, AllowAnonymous]
        public JsonResult EditRole()
        {
            AjaxResult result = new AjaxResult();
            try
            {
                string dId = Request.Params["_rId"].ToString(),
                       rName = Request["_rName"].ToString();

                _permissionService.EditRoleInfo(dId, rName);
                result.IsSuccess = true;
            }
            catch
            {
                result.IsSuccess = false;
                result.Msg = "角色名不能重复";
            }
            return Json(result);

        }

        [HttpPost, AllowAnonymous]
        public JsonResult AddRole()
        {
            AjaxResult result = new AjaxResult();
            try
            {
                string rName = Request["_rName"].ToString();
                _permissionService.AddRoleInfo(rName);
                result.IsSuccess = true;
            }
            catch
            {
                result.IsSuccess = false;
                result.Msg = "角色名不能重复";
            }
            return Json(result);

        }

        [HttpPost, AllowAnonymous]
        public JsonResult DelRole(string ids)
        {
            AjaxResult result = new AjaxResult();
            try
            {
                _permissionService.DelRoleInfo(ids);
                result.IsSuccess = true;
            }
            catch
            {
                result.IsSuccess = false;
                result.Msg = "";
            }
            return Json(result);
        }
        #endregion

        #region Private-Methods

        private JsonTreeNode BuildTreeNodeForModule(Module module, IEnumerable<Function> funcs)
        {
            var funcNodeList = funcs.Select(f => new JsonTreeNode()
            {
                id = f.Id,
                name = f.Name,
                text = f.Name
            });

            JsonTreeNode moduleNode = new JsonTreeNode()
            {
                id = module.Id,
                name = module.Name,
                text = module.Name,
                children = new List<JsonTreeNode>()
            };
            moduleNode.children.AddRange(funcNodeList);

            return moduleNode;
        }
        #endregion
    }
}