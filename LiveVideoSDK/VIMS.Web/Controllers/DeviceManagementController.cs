using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using VIMS.DataModel.CaseInvestigation;
using VIMS.DataModel.SystemManagement;
using VIMS.Services.SystemManagement;
using VIMS.Web.Infrastructure.Models;
using VIMS.DataModel.Judge;

namespace VIMS.Web.Controllers
{
    public class DeviceManagementController : Controller
    {
        private IDeviceManageService _deviceManage;

        public DeviceManagementController(IDeviceManageService deviceManage)
        {
            this._deviceManage = deviceManage;
        }

        //
        // GET: /DeviceManagement/

        /// <summary>
        /// "设备管理"页面
        /// </summary>
        /// <returns></returns>
        public ActionResult Index()
        {
            return View();
        }

        /// <summary>
        /// "添加设备"页面
        /// </summary>
        /// <returns></returns>
        public ActionResult DeviceAdd()
        {
            //Device device = _deviceManage.GetDevice(Id);

            ViewData["devTypeList"] = GetDictInfo("devType");
            ViewData["devStatusList"] = GetDictInfo("devStatus");

            return View();
        }

        /// <summary>
        /// "编辑设备"页面
        /// </summary>
        /// <returns></returns>
        public ActionResult DeviceEdit(string Id)
        {
            Device device = _deviceManage.GetDevice(Id);

            // 2016-11-7 前端日期控件显示需要
            device.strPDate = System.Convert.ToDateTime(device.devPDate).ToString("yyyy-MM-dd");
            device.strSTime = System.Convert.ToDateTime(device.devSTime).ToString("yyyy-MM-dd");
            
            ViewData["devTypeList"] = GetDictInfo("devType");
            ViewData["devStatusList"] = GetDictInfo("devStatus");

            return View(device);
        }


        public JsonResult DeviceSave(Device device)     // 这方法 什么作用？
        {


            return Json(null);
        }

        /// <summary>
        /// 获取设备信息 - 绑定分页列表
        /// </summary>
        /// <returns></returns>
        public JsonResult GetDevices(int limit, int offset, string _devType, string _uCode, string _devName)
        //public JsonResult GetDevices(nPageInfo pageInfo, nSearchCondition condition)
        {
            try
            {
                //var result = this._deviceManage.GetDevices(pageInfo, condition);

                //return Json(result);

                ///*
                var result = this._deviceManage.GetDevices(_devType, _uCode, _devName);

                var total = result.Count;
                var rows = result.Skip(offset).Take(limit).ToList();

                // 2016-11-7 前端日期控件显示需要
                for (var i = 0; i < result.Count; ++i)
                {
                    result[i].strPDate = System.Convert.ToDateTime(result[i].devPDate).ToString("yyyy-MM-dd");
                    result[i].strSTime = System.Convert.ToDateTime(result[i].devSTime).ToString("yyyy-MM-dd");
                }
               
                return Json(new { total = result.Count, rows = rows }, JsonRequestBehavior.AllowGet);
                //*/
            }
            catch (Exception e)
            {
                return Json(e);
            }
            
        }
        
        /// <summary>
        /// 获取设备信息
        /// </summary>
        /// <returns></returns>
        public JsonResult GetDeviceByCode(string devCode)
        {
            try
            {
                ///*
                var result = this._deviceManage.GetDevices();
                var ret = result[0];

                // 2016-11-7 前端日期控件显示需要
                for (var i = 0; i < result.Count; ++i)
                {
                    if (result[i].devCode == devCode)
                    {
                        ret = result[i];
                        ret.strPDate = System.Convert.ToDateTime(ret.devPDate).ToString("yyyy-MM-dd");
                        ret.strSTime = System.Convert.ToDateTime(ret.devSTime).ToString("yyyy-MM-dd");
                    }
                }

                return Json(ret);
                //*/
            }catch(Exception e)
            {
                return Json(e);
            }
        }

        /// <summary>
        /// 获取字典项
        /// </summary>
        /// <returns></returns>
        private List<SelectListItem> GetDictInfo(string field)
        {
            List<SelectListItem> list = new List<SelectListItem>();

            var dicts = this._deviceManage.GetDictData(field);

            for (var i = 0; i < dicts.Count; ++i)
            {
                list.Add(new SelectListItem { Text = dicts[i].dicValue, Value = dicts[i].dicValue });
            }

            return list;
        }

        /// <summary>
        /// 添加设备
        /// </summary>
        /// <returns></returns>
        public JsonResult AddDevice(string _devCode, string _devType, string _devName, string _devBrand, string _devModel, string _devPDate, string _devGPeriod, string _devSTime, string _devuCode, string _devStatus, string _devSN, string _devPhoto, string _devRemark)
        {
            try
            {
                Device device = new Device();

                device.Id = Guid.NewGuid();
                device.devCode = _devCode;
                device.devType = _devType;
                device.devName = _devName;
                device.devBrand = _devBrand;
                device.devModel = _devModel;
                device.devPDate = DateTime.Parse(_devPDate);
                device.devGPeriod = Convert.ToByte(_devGPeriod);
                device.devSTime = DateTime.Parse(_devSTime);
                device.uCode = _devuCode;
                device.devStatus = _devStatus;
                device.devSN = _devSN;
                device.devPhoto = _devPhoto;
                device.devRemark = _devRemark;

                var result = this._deviceManage.DeviceAdd(device);

                return Json(result);
            }
            catch (Exception e)
            {
                return Json(e);
            }
        }

        /// <summary>
        /// 编辑设备
        /// </summary>
        /// <returns></returns>
        public JsonResult EditDevice(string _Id, string _devCode, string _devType, string _devName, string _devBrand, string _devModel, string _devPDate, string _devGPeriod, string _devSTime, string _devuCode, string _devStatus, string _devSN, string _devPhoto, string _devRemark)
        {
            try
            {
                Device device = new Device();

                device.Id = Guid.Parse(_Id);
                device.devCode = _devCode;
                device.devType = _devType;
                device.devName = _devName;
                device.devBrand = _devBrand;
                device.devModel = _devModel;
                device.devPDate = DateTime.Parse(_devPDate);
                device.devGPeriod = Convert.ToByte(_devGPeriod);
                device.devSTime = DateTime.Parse(_devSTime);
                device.uCode = _devuCode;
                device.devStatus = _devStatus;
                device.devSN = _devSN;
                device.devPhoto = _devPhoto;
                device.devRemark = _devRemark;

                var result = this._deviceManage.DeviceEdit(device);

                return Json(result);
            }
            catch (Exception e)
            {
                return Json(e);
            }
        }

        /// <summary>
        /// 删除设备
        /// </summary>
        /// <returns></returns>
        public JsonResult DeleteDevice(string Id)
        {
            try
            {
                var ret = this._deviceManage.DeviceDelete(Id);

                return Json(ret);
            }catch(Exception e)
            {
                return Json(e);
            }
        }




        public JsonResult GetuCodeuName()
        {
            IList<TrackRecord> _ilistjudge = _deviceManage.GetuCodeuNameInfo();
            return Json(_ilistjudge, JsonRequestBehavior.AllowGet);
        }

    }
}
