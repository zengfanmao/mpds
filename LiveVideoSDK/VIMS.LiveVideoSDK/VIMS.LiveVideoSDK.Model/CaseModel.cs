using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class CaseModel
    {
        /// <summary>
        /// 案件显示app当的索引图片
        /// </summary>
        public string ImageUrl { get; set; }

        public string Department { get; set; }
        /// <summary>
        /// 创建时间
        /// </summary>
        public string CaseCreatedTime { get; set; }
        public Nullable<System.DateTime> _CaseCreatedTime { get; set; }
        /// <summary>
        /// 发案件时间
        /// </summary>
        public Nullable<System.DateTime> csTime { get; set; }
        public Nullable<System.DateTime> csTime_h { get; set; }
        public string csEndTime { get; set; }
        public Nullable<System.DateTime> _csEndTime { get; set; }
        /// <summary>
        /// 受理单位
        /// </summary>
        public string CaseAccept { get; set; }
        /// <summary>
        /// 案件id
        /// </summary>
        public string CaseId { get; set; }
        /// <summary>
        /// 案件编号
        /// </summary>
        public string CaseCode { get; set; }
        /// <summary>
        /// 案件名字
        /// </summary>
        public string CaseName { get; set; }



        /// <summary>
        /// 案件类别
        /// </summary>
        public string CaseType { get; set; }
        /// <summary>
        /// 案件地点
        /// </summary>
        public string CaseAddr { get; set; }
        /// <summary>
        /// 案件时间
        /// </summary>
        public string CaseTime { get; set; }
        /// <summary>
        /// 案件时间上线
        /// </summary>
        public string CaseMaxTime { get; set; }
        /// <summary>
        /// 案件状态
        /// </summary>
        public string CaseState { get; set; }
        /// <summary>
        /// 简要案情
        /// </summary>
        public string CaseSummary { get; set; }


         /// <summary>
        /// 录入时间
        /// </summary>
        public Nullable<System.DateTime> cgCreateTime { get; set; }

        public Nullable<System.DateTime> _csRegDate { get; set; }
        /// <summary>
        /// 立案时间
        /// </summary>
        public string  CsRegDate { get; set; }
        /// <summary>
        /// 指挥官用户编号
        /// </summary>
        public string CaseCommanderUserCode { get; set; }
        /// <summary>
        /// 指挥官的名字
        /// </summary>
        public string CaseCommanderUserName { get; set; }

        public string CaseAddres { get; set; }

        public string CaseAddresLatitude { get; set; }
        public string CaseAddresLongitude { get; set; }
    }
}
