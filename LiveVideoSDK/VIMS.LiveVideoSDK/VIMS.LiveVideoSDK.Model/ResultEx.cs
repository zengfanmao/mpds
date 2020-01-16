using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class ResultEx
    {
        public ResultEx()
        {
            this.Exception = "";
            this.IsSuccess = false;
            this.ResultCode = "-1";
            this.Message = "初始化错误";
            this.Entity = null;
        }
        public ResultEx(String msg)
        {
            this.Exception = "";
            this.IsSuccess = false;
            this.ResultCode = "-1";
            this.Message = msg;
            this.Entity = null;
        }
        /// <summary>
        /// 错误
        /// </summary>
        public string Exception { get; set; }
        /// <summary>
        /// 是否成功
        /// </summary>
        public bool IsSuccess { get; set; }
        /// <summary>
        /// 返回的消息
        /// </summary>
        public string Message { get; set; }
        /// <summary>
        /// 返回的代码
        /// </summary>
        public string ResultCode { get; set; }
        /// <summary>
        /// 返回的数据可以是文本或者json字符串
        /// </summary>
        public Object Entity { get; set; }
    }
}
