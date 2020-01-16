using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class DeployModel
    {
        public byte[] recSN { get; set; }
        /// <summary>
        /// 部署类，别是人还是物品
        /// </summary>
        public string DeployType { get; set; }
        /// <summary>
        /// 部署人的显示姓名
        /// </summary>
        public string DeployUserName { get; set; }
        /// <summary>
        /// 部署人的编码
        /// </summary>
        public string DeployUserCode { get; set; }
        /// <summary>
        /// 部署人的角色
        /// </summary>
        public string DeployUserRName { get; set; }
        /// <summary>
        /// 部署人的职位
        /// </summary>
        public string DeployUserDuty { get; set; }

        /// <summary>
        /// 部署人的头像
        /// </summary>
        public string DeployUserPhoto { get; set; }

        /// <summary>
        /// 部署设备的名字
        /// </summary>
        public string DeployDeviceName { get; set; }
        /// <summary>
        /// 部署设备编码
        /// </summary>
        public string DeployDeviceCode{ get; set; }
        /// <summary>
        /// 部署设备使用人
        /// </summary>
        public string DeployDeviceUserName { get; set; }
         /// <summary>
        /// 部署设备使用人编码
        /// </summary>
        public string DeployDeviceUserCode { get; set; }

        /// <summary>
        /// 设备图像
        /// </summary>
        public string DeployDevicePhoto { get; set; }


        public int Level { get; set; }



        /// <summary>
        /// 发布人所在纬度
        /// </summary>
        public string DeployUserLatitude { get; set; }
        /// <summary>
        /// 发布人所在经度
        /// </summary>
        public string DeployUserLongitude { get; set; }
        /// <summary>
        /// 发布人所在位置的名字
        /// </summary>
        public string DeployUserPositionName { get; set; }


        public string DevSN { get; set; }

        public string CaseCode { get; set; }
    }
}
