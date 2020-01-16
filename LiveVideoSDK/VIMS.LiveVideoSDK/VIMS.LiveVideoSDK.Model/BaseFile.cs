using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public  class BaseFile
    {
        /// <summary>
        /// 文件名字
        /// </summary>
        public string FileName { get; set; }
        /// <summary>
        /// 文件编号
        /// </summary>
        public string FileCode { get; set; }
        /// <summary>
        /// 文件大小
        /// </summary>
        public string FileSize { get; set; }
    }
}
