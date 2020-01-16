using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{


    /// <summary>
    /// 消息实体
    /// </summary>
    public class MsgModel
    {
        public MsgModel()
        {
            
            this.MsgType =MsgTypes.Text.ToString();
            this.MsgErr = "发生错误";
            this.MsgContent = "";
            this.MsgCode = "-1";
        }
        /// <summary>
        /// 消息类型
        /// </summary>
        public string MsgType { get; set; }
        /// <summary>
        /// 消息代号
        /// </summary>
        public string MsgCode { get; set; }
        /// <summary> 
        /// 消息实体
        /// </summary>
        public Object MsgContent { get; set; }
        /// <summary>
        /// 消息错误
        /// </summary>
        public string MsgErr { get; set; }

        
    }
    /// <summary>
    /// 消息分类
    /// </summary>
    public enum MsgTypes { 
        /// <summary>
        /// 图片消息
        /// </summary>
        Image,
        /// <summary>
        /// 文字消息
        /// </summary>
        Text,
        /// <summary>
        /// 音频消息
        /// </summary>
        Audio,
        /// <summary>
        /// 视频消息
        /// </summary>
        Video,
        /// <summary>
        /// 直播消息
        /// </summary>
        Living,
        /// <summary>
        /// 推送公告
        /// </summary>
        Notice,
        /// <summary>
        /// 地图位置
        /// </summary>
        Map,

        /// <summary>
        /// 登陆消息
        /// </summary>
        OUserLogin,

        /// <summary>
        /// 委派新任务
        /// </summary>
       NewMission,
       /// <summary>
       /// 文件
       /// </summary>
       File,

       Recording,

       Command
    }

    /// <summary>
    /// 直播状态
    /// </summary>
    public enum VideoLivingStatus { 
        /// <summary>
        /// 正在直播
        /// </summary>
        On=1,
        /// <summary>
        /// 直播关闭
        /// </summary>
        Off=0
    }

    public enum DeloyType
    {
        Person,Camera
    }


    public enum MsgFromTypes
    {
        Person, Group,Case,
    }
    /// <summary>
    /// 直播类型，直播手机
    /// </summary>
    public enum SourceType
    {
        Camera,Phone
    }
}
