using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JQSoft.Web.Mvc;
using VIMS.Services.SystemManagement;
using VIMS.DataModel.SystemManagement;
using VIMS.Services.Impl.SystemManagement;
using System.Text;
using VIMS.Entity;



namespace VIMS.Web.Controllers
{
    public class OrganizationController : Controller
    {

        private IOrganizationService _idispatchservice = null;

        //构造函数
        public OrganizationController(IOrganizationService idispatchservice)
        {
            _idispatchservice = idispatchservice;

        }

        //路由信息
        public ActionResult Index()
        {
            return View();
        }

        public JsonResult GetDevInfo(string status)
        {

          List<Nodes> _listpolicedispatch = _idispatchservice.GetDeviceNodeTreeInfo(status);
            //创建jsondata对象
            return Json(_listpolicedispatch);
        }

        //增加组织架构部门
        public JsonResult AddOra()
        {
            AjaxResult result = new AjaxResult();
            try
            {
                string dCode = Request.Params["_dCode"].ToString(),
                       dName = Request["_dName"].ToString(),
                       dFather = Request.Form["_dFather"].ToString(),
                       dDesc = Request.Params["_dDesc"].ToString();

                Department cSubModel = new Department();
                cSubModel.Id = Guid.NewGuid();
                cSubModel.dCode = dCode;
                cSubModel.dName = dName;
                cSubModel.dFather = dFather;
                cSubModel.dDesc = dDesc;
                _idispatchservice.InsertOrganization(cSubModel);
                result.IsSuccess = true;
                result.Msg = cSubModel.Id.ToString();
            }
            catch
            {
                result.IsSuccess = false;
                result.Msg = "";
            }
            return Json(result);
        }
        //删除组织架构部门
        public JsonResult DelOra(string ids)
        {
            AjaxResult result = new AjaxResult();
            try
            {
                _idispatchservice.DeleteOrganization(ids);
                result.IsSuccess = true;
            }
            catch 
            {
                result.IsSuccess = false;
                result.Msg = "";
            }
            return Json(result);
        }
        //更新组织架构部门
        public JsonResult UpdateOra()
        {
            AjaxResult result = new AjaxResult();
            try
            {
                string  dId = Request.Params["_dId"].ToString(),
                        dCode = Request.Params["_dCode"].ToString(),
                        dName = Request["_dName"].ToString(),
                        dFather = Request.Form["_dFather"].ToString(),
                        dDesc = Request.Params["_dDesc"].ToString();
                _idispatchservice.UpdateOrganization(dId,dCode,dName,dFather,dDesc);
                result.IsSuccess = true;
            }
            catch
            {
                result.IsSuccess = false;
                result.Msg = "";
            }
            return Json(result);
        }

        //绑定table表格数据及更新表格数据
        public JsonResult Getorganization(int limit, int offset, string ids)
        {
            var list = _idispatchservice.GetOraInfo(ids);
            var total = list.Count;
            var rows = list.Skip(offset).Take(limit).ToList();
            return Json(new { total = list.Count, rows = rows }, JsonRequestBehavior.AllowGet);
        }
    



    }
}
