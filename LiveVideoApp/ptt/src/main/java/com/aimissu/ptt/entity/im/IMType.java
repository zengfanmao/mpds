package com.aimissu.ptt.entity.im;

/**
 */
public class IMType {

    public static class Params {
        public static final String MSG_DATA = "MSG_DATA";
        public static final String MSG_TO_CODE = "MSG_TO_CODE";

        public static final String MSG_GROUP_SUM = "MSG_GROUP_SUM";
        public static final String MSG_PERSON_SUM = "MSG_PERSON_SUM";

        public static final String MSG_GROUP_TITLE = "MSG_GROUP_TITLE";
        public static final String MSG_PERSONAL_TITLE = "MSG_PERSONAL_TITLE";

        public static final String CALL_CONNETING= "CALL_CONNETING";
        public static final String USER_TYPE= "USER_TYPE";
        public static final String VIDEO_CALL= "VIDEO_CALL";
        public static final String DEVICE_ID= "DEVICE_ID";

        //传入部门参数 by cuizh,0402
        public static final String USER_DEPARTMENT = "USER_DEPARTMENT";

        public static final String TYPE_APP= "APP";
        public static final String TYPE_PDT= "PDT";


        public static final String ON_LINE= "在线";
        public static final String OFF_LINE= "离线";
        public static final String REMOTE_DIZZY= "遥晕";
        public static final String REMOTE_KILL= "遥毙";
        public static final String ACTIVATE= "激活";
        public static final String DISABLE= "禁用";

        public static final String PAGE_FROM_ID = "PAGE_FROM_ID";
        public static final String PAGE_FROM_POSITION = "PAGE_FROM_POSITION";

        /**
         * 加入会议通话
         */
        public static final String JOIN_CALL_CONFERENCE = "JOIN_CALL_CONFERENCE";
        public static String CONFERENCE_CREATOR="CONFERENCE_CREATOR";
        public static String CONFERENCE_NAME="CONFERENCE_NAME";
    }

    public enum UserAction {
        /**
         * 遥晕
         */
        stop,
        /**
         * 遥毙
         */
        kill,
        /**
         * 禁用
         */
        disable,
        /**
         * 激活
         */
        enable
    }

    /**
     * 消息来源
     */
    public enum MsgFromType {
        Person, Group, Case,
    }

    /// <summary>
    /// 消息分类
    /// </summary>
    public enum MsgType {
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
        /**
         * 文件消息
         */
        File,

        /**
         * 命令消息
         */
        Command,

        /**
         * text
         */
        txt,


    }


    public enum UploadType{
        File,
        Image,
        Audio,
        Video,
    }
}
