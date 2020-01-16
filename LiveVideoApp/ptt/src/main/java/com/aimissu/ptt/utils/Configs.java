package com.aimissu.ptt.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by bywei on 2017/8/3.
 */

public class Configs {
    /***SIPURI的key**/
    public static String SIPURI = "SIPURI";
    /***ACCOUNT的key**/
    public static String ACCOUNT = "ACCOUNT";
    public static boolean IsInCallOutgoingActivity = false;
    public static int mCurrentState = -1;
    /**
     * 接收到广播的action
     **/
    public static String MESSAGERECEIVED_ACTION = "MESSAGERECEIVED_KEY";
    /**
     * 发送聊天实例类的key
     **/
    public static String LINPHONECHATMESSAGE = "LINPHONECHATMESSAGE";
    /*MainActivitykey**/
    public static String MAINACTIVITYSTARTKEY = "MAINACTIVITYSTARTKEY";
    public static String STARTCALLINCOMINGACTIVITY = "STARTCALLINCOMINGACTIVITY";

    public static final String rootDirName = "ptt";//根文件件名字
    public static final String Root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootDirName + File.separator;
    public static final String linPhoneLog = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "grgvideo" + File.separator;

    public static final String Root1 = Environment.getRootDirectory().getAbsolutePath() + File.separator + rootDirName + File.separator;
    public static final String Root2 = Environment.getDownloadCacheDirectory().getAbsolutePath() + File.separator + rootDirName + File.separator;
    public static String ImgRoot = Root + "images/";
    public static String MapRoot = Root + "maps/";
    public static String AudioRoot = Root + "audios/";
    public static String FileRoot = Root + "files/";
    public static String LogRoot = Root + "log/";
    public static final String PNG_FILE_PREFIX = "PNG_";
    public static final String PNG_FILE_SUFFIX = ".png";

    public static final String CALL_CONNECTED="CALL_CONNECTED";
    public static final String CALL_END="CALL_END";

    public static String sipNumber="1005";

    public static String SERVERURL= "ServerURL";

    public static String LONGITUDE="LONGITUDE";
    public static String LATITDUE="LATITDUE";
    public static String USERNAME="USERNAME";
    public static String GPSTARGET_TYPE="GPSTARGET_TYPE";
    public static String USERCODE="USERCODE";
    public static String netWorkState="netWorkState";

    //120.78.67.146
    //sip和emqttd的ip
    public static String DEFAULT_SERVER_IP="183.234.62.119";
//    public static String DEFAULT_SERVER_IP="20.97.50.137";
//    public static String DEFAULT_SERVER_IP="20.97.50.138";
    public static String DEFAULT_SERVER_PORT="5560";

    //后台vims的IP
    public static String BG_SERVER_IP ="183.234.62.119";
//    public static String BG_SERVER_IP ="20.97.50.137";

    //发送实时发送log的接口
//    public static String DEFAULT_LOG_API="http://20.97.50.137:999/api/do/sendLog";
    public static String DEFAULT_LOG_API="http://183.234.62.119:999/api/do/sendLog";
}
