using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JQSoft.Data;
using JQSoft.Logging;
using JQSoft.Web.Mvc;
using VIMS.DataModel;
using VIMS.DataModel.SystemManagement;
using VIMS.Services.SystemManagement;
using VIMS.Web.Infrastructure.Helpers;
using VIMS.Web.Infrastructure.Models;
using VIMS.Web.Models;

namespace VIMS.Web.Controllers
{
    public class UserController : BaseController
    {
        private readonly IUserService _userService;
        private readonly IPermissionService _permissionService;

        public UserController(IUserService userService, IPermissionService permissionService)
        {
            this._userService = userService;
            this._permissionService = permissionService;
        }

        public ActionResult UserManage()
        {
            return View();
        }

        public ActionResult UserEdit(string id)
        {
            User user = new User();
            if (!string.IsNullOrEmpty(id))
            {
                user = this._userService.GetUser(id);
            }
            else
            {
                user = new User();
            }
            //性别
            ViewData["Sexs"] = new List<SelectListItem>() 
            {
                new SelectListItem(){ Value = "0", Text = "男" },
                new SelectListItem(){ Value = "1", Text = "女" },
            };
            //角色
            ViewData["Roles"] = this._permissionService.GetRoles()
                .Select(r => new SelectListItem() { Value = r.Name, Text = r.Name });

            return View(user);
        }

        public ActionResult ModifyPassword()
        {
            return View();
        }

        #region Ajax-Request

        [HttpPost, AllowAnonymous]
        public JsonResult GetUserList(nPageInfo pageInfo, nSearchCondition condition)
        {
            JqGridData<User> gridData = null;
            try
            {
                IPagedList<User> userPagedList = this._userService.GetUserPagedList(pageInfo, condition);
                gridData = JqGridData<User>.Create(userPagedList);
            }
            catch (Exception ex)
            {
                gridData = new JqGridData<User>();
                Logger.Current.Log(LogType.Error, ex.ToString());
            }
            return this.Json(gridData);
        }

        [HttpPost, AllowAnonymous]
        public JsonResult DeleteUser(string id)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                this._userService.DeleteUser(id);
                ajaxResult.IsSuccess = true;
            }
            catch (Exception ex)
            {
                Logger.Current.Log(LogType.Error, ex.ToString());
                //string text = Res.Instance.GetItem("Format_Delete_Error", true).Format(new object[]
                //{
                //    ex.Message
                //});
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = ex.Message;
            }
            return Json(ajaxResult);
        }

        [HttpPost, AllowAnonymous]
        public JsonResult SaveUser(User user)
        {
            AjaxResult ajaxResult = new AjaxResult();
            DataOperationStatus status = DataOperationStatus.Unknown;
            try
            {
                if (string.IsNullOrEmpty(user.Id))  //添加用户
                {
                    user.Password = AppSettings.DefaultUserPassword;
                    status = this._userService.AddUser(user);
                }
                else  //修改用户
                {
                    status = this._userService.UpdateUser(user);
                }
            }
            catch (Exception ex)
            {
                //string msg = Res.Instance.GetItem("Format_Update_Error", true).Format(new object[]
                //{
                //    ex.Message
                //});
                Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = ex.Message;
            }

            switch (status)
            {
                case DataOperationStatus.OK:
                    ajaxResult.IsSuccess = true;
                    ajaxResult.Msg = string.Empty;
                    break;

                case DataOperationStatus.DataDuplicated:
                    ajaxResult.IsSuccess = false;
                    ajaxResult.Msg = "登录名已存在！";
                    break;

                case DataOperationStatus.DataNotFound:
                    ajaxResult.IsSuccess = false;
                    ajaxResult.Msg = "数据未找到！";
                    break;
            }
            return Json(ajaxResult);
        }

        /// <summary>
        /// 上传用户头像。
        /// </summary>
        [HttpPost, AllowAnonymous]
        public JsonResult UploadHeadPortrait()
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                string targetPath = FileUploadManager.UploadUserHeadPortrait(Request.Files[0]);
                ajaxResult.IsSuccess = true;
                ajaxResult.Data = targetPath;
            }
            catch (Exception ex)
            {
                Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = string.Format("上传失败，原因：{0}", ex.Message);
            }
            return Json(ajaxResult);
        }

        /// <summary>
        /// 重置密码。
        /// </summary>
        [HttpPost, AllowAnonymous]
        public JsonResult ResetPassword(string id)
        {
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                this._userService.ModifyPassword(id, AppSettings.DefaultUserPassword);
                ajaxResult.IsSuccess = true;
            }
            catch (Exception ex)
            {
                Logger.Current.Log(LogType.Error, ex.ToString());
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = ex.Message;
            }
            return Json(ajaxResult);
        }

        /// <summary>
        /// 修改密码。
        /// </summary>
        [HttpPost, AllowAnonymous]
        public JsonResult ModifyPassword(string oldPassword, string password)
        {
            var user = JQSoft.AppContext.Current.User;
            AjaxResult ajaxResult = new AjaxResult();
            try
            {
                string md5OldPassword = JQSoft.RuntimeUtils.MD5(oldPassword);
                if (string.Equals(md5OldPassword, user.Password, StringComparison.OrdinalIgnoreCase))
                {
                    string md5Password = JQSoft.RuntimeUtils.MD5(password).ToUpper();
                    DataOperationStatus status = this._userService.ModifyPassword(user.UserId, md5Password);
                    ajaxResult.IsSuccess = true;
                }
                else
                {
                    ajaxResult.IsSuccess = false;
                    ajaxResult.Msg = "旧密码输入不正确！";
                }
            }
            catch (Exception ex)
            {
                ajaxResult.IsSuccess = false;
                ajaxResult.Msg = ex.Message;
            }
            return Json(ajaxResult);
        }
        #endregion
    }
}