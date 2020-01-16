package com.aimissu.ptt.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 */

public class LoginSharedPreferences {
    private static final String PREF_ACCOUNT = "grg_phone_account";
    private static final String PREF_DISPLAY_NAME = "grg_phone_display_name";
    private static final String PREF_PASSWORD = "grg_phone_password";
    private static final String PREF_DOMAIN = "grg_phone_domain";
    private static final String PREF_TRANS_PRO = "grg_phone_trans_pro";
    private static final String H264Mode = "h264mode";
    private static final String VP8Mode = "VP8Mode";
    private static final String cameraBackSupportedVideoSizes = "cameraBackSupportedVideoSizes";
    private static final String cameraFrontSupportedVideoSizes = "cameraFrontSupportedVideoSizes";
    private static final String FEC = "FEC";
    private static final String VIDEO_SERVER_IP = "VIDEO_SERVER_IP";
    private static final String IM_SERVER_IP = "IM_SERVER_IP";

    public static String getAccount(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_ACCOUNT, "");
    }

    public static void setAccount(Context context, String account) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ACCOUNT, account)
                .apply();
    }

    public static String getDisplayName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_DISPLAY_NAME, "");
    }

    public static void setDisplayName(Context context, String displayName) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_DISPLAY_NAME, displayName)
                .apply();
    }

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_PASSWORD, "");
    }

    public static void setPassword(Context context, String password) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_PASSWORD, password)
                .apply();
    }


//    public static String getVideoServerIp(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context)
//                .getString(VIDEO_SERVER_IP, "120.78.67.146:5560");
//    }
//
//    public static void setVideoServerIp(Context context, String videoServerIp) {
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putString(VIDEO_SERVER_IP, videoServerIp)
//                .apply();
//    }
//    public static String getImserVerIp(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context)
//                .getString(IM_SERVER_IP, "120.78.67.146");
//    }

    public static void setImserVerIp(Context context, String imserVerIp) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(IM_SERVER_IP, imserVerIp)
                .apply();
    }

    public static int getTransPro(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_TRANS_PRO, 0);
    }

    public static void setTransPro(Context context, int protocol) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_TRANS_PRO, protocol)
                .apply();
    }

    public static String getH264Mode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(H264Mode, "Auto");
    }

    public static void setH264Mode(Context context, String h264Mode) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(H264Mode, h264Mode)
                .apply();
    }

    public static Boolean getVP8Mode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(VP8Mode, false);
    }

    public static void setVP8Mode(Context context, boolean vp8Mode) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(VP8Mode, vp8Mode)
                .apply();
    }

    public static Boolean getFEC(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(FEC, false);
    }

    public static void setFEC(Context context, boolean vp8Mode) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(FEC, vp8Mode)
                .apply();
    }

    public static String getcameraBackSupportedVideoSizes(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(cameraBackSupportedVideoSizes, "320x240");
    }

    public static void setcameraBackSupportedVideoSizes(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(cameraBackSupportedVideoSizes, value)
                .apply();
    }

    public static String getcameraFrontSupportedVideoSizes(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(cameraFrontSupportedVideoSizes, "320x240");
    }

    public static void setcameraFrontSupportedVideoSizes(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(cameraFrontSupportedVideoSizes, value)
                .apply();
    }

    public static Boolean getLoginCheck(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("LOGIN_CHECK", true);
    }

    public static void setLoginCheck(Context context, boolean domain) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("LOGIN_CHECK", domain)
                .apply();
    }
}
