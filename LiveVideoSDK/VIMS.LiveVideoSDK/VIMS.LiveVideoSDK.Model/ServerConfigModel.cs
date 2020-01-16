using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class ServerConfigModel
    {
        /// <summary>
        /// 推流地址
        /// </summary>
        public string RtmpUrl { get; set; }
        /// <summary>
        /// 通信代理服务器ip
        /// </summary>
        public string ImIp { get; set; }
        /// <summary>
        /// 语音通话用于实时语音，视频通话
        /// </summary>
        public string CallIp { get; set; }

        /// <summary>
        /// API地址
        /// </summary>
        public string ApiUrl { get; set; }
    }
}
