package com.aimissu.ptt.utils;

import android.text.TextUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class CommonUtils {

    public static Date strToDate(String strDate) {
        return strToDate(strDate, "yyyy-MM-dd HH:mm:ss");
    }


    public static Double toDouble(String value) {
        try {
            Double result = Double.parseDouble(value);
            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {

        }
        return 0d;
    }

    public static Date strToDate(String strDate, String formatStr) {
        Date strtodate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(strDate, pos);
        } catch (Exception ex) {
        }
        return strtodate;
    }

    public static String formatDateMinute(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String formatDate(Date date, String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String emptyIfNull(String str) {
        return defaultValueIfNull(str, "");
    }

    public static String defaultValueIfNull(String str, String defaultValue) {
        return str == null ? defaultValue : str;
    }


    public static int toInt(String number) {
        int result = 0;
        try {

            if (!TextUtils.isEmpty(number))
                result = Integer.valueOf(number);
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }

        return result;
    }

    public static String getFileNameFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            return filename;
        }

        return "";
    }

    /**
     * 转换系统返返回来的时间
     *
     * @param data
     * @return
     */
    public static String convertData(String data) {
        if (data == null) {
            return null;
        }

        if (data.contains("T")) {
            data = (String) data.replace("T", " ");
        }
        if (data.contains(".")) {

            data = (String) data.subSequence(0, data.indexOf("."));
        }

//        LogUtil.i(TAG, "消息时间：" + data);

        if (data.length() >= 16) {

            String timeStamp = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                    .format(new Date());
            String nowDay = timeStamp.substring(8, 10);
            // 获取日
            String messageDay = data.substring(8, 10);
            // 获取小时
            String messageHour = data.substring(11, 13);

            int nowday = Integer.valueOf(nowDay.trim());
            int messageday = Integer.valueOf(messageDay.trim());
            int messagehour = Integer.valueOf(messageHour.trim());

            if (nowday == messageday) {
                data = data.substring(11, 16);
            } else if (nowday - messageday == 1) {
                data = "昨天 " + data.substring(11, 16);
            } else if (nowday - messageday == 2) {
                data = "前天 " + data.substring(11, 16);
            } else {
                if (messagehour >= 0 && messagehour < 6) {
                    data = data.substring(5, 7) + "月" + data.substring(8, 10)
                            + "日" + " " + "凌晨 " + data.substring(11, 16);
                } else if (messagehour >= 6 && messagehour < 12) {
                    data = data.substring(5, 7) + "月" + data.substring(8, 10)
                            + "日" + " " + "早上 " + data.substring(11, 16);
                } else if (messagehour >= 12 && messagehour < 18) {
                    data = data.substring(5, 7) + "月" + data.substring(8, 10)
                            + "日" + " " + "下午 " + data.substring(11, 16);
                } else if (messagehour >= 18 && messagehour < 24) {
                    data = data.substring(5, 7) + "月" + data.substring(8, 10)
                            + "日" + " " + "晚上 " + data.substring(11, 16);
                } else {
                    data = data.substring(5, 7) + "月" + data.substring(8, 10)
                            + "日" + " " + data.substring(11, 16);
                }
            }

            // LogUtil.i(TAG, "当前日："+nowDay);
            // LogUtil.i(TAG, "消息日："+messageDay);
            //
            // LogUtil.i(TAG, "当前时间："+timeStamp);
//            LogUtil.i(TAG, "消息时间：" + data);
        }

        return data;
    }

    /**
     * 验证网址Url
     *
     * @param
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*|(:[0-9]{1,4}))?";
        return match(regex, str);
    }
    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
