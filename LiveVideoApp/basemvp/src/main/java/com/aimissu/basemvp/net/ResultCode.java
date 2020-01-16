package com.aimissu.basemvp.net;

import com.aimissu.basemvp.base.BaseJsonData;


import java.util.HashMap;
import java.util.Map;

/**

 * 动态url的通用Http请求接口
 */
public class ResultCode {
    /**
     * 请求成功
     */
    public static final int CODE_SUCCESS = 200;
    /**
     * 业务异常，提示消息在msg中取
     */
    public static final int CODE_BUSINESS_ERROR = 201;

    /**
     * token过期
     */
    public static final int CODE_TOKEN_EXPIRE = 202;
    /**
     * 频繁操作
     */
    public static final int CODE_REQUEST_FREQUENT = 207;
    /**
     * 登录失败
     */
    public static final int CODE_LOGIN_FAILED = 303;
    /**
     * 请求参数出错
     */
    public static final int CODE_REQUEST_PARAM_ERROR = 400;
    /**
     * 没有登录
     */
    public static final int CODE_NOT_LOGIN = 401;
    /**
     * 没有权限
     */
    public static final int CODE_NOT_AUTHORITY = 403;
    /**
     * 请求超时
     */
    public static final int CODE_REQUEST_TIMEOUT = 408;
    /**
     * 服务器出错
     */
    public static final int CODE_SERVER_ERROR = 500;
    /**
     * ACCESS_TOKEN过期
     */
    public static final int CODE_IAM_TOKEN_EXPIRE = 499;
    /**
     * ACCESS_TOKEN解析失败
     */
    public static final int CODE_IAM_PARSING_FAILURE = 498;
    /**
     * 非法的登出参数
     */
    public static final int CODE_IAM_LOGINOUT_ILLEGAL = 460;
    /**
     * 登出错误，用户会话异常
     */
    public static final int CODE_IAM_SESSION_EXCEPTION = 461;
    /**
     * 非法的登录参数
     */
    public static final int CODE_IAM_LOGIN_ILLEGAL = 470;
    /**
     * 账号或者密码错误
     */
    public static final int CODE_IAM_PASSWORD_ERROR = 471;
    /**
     * 非法的REFRESH_TOKEN
     */
    public static final int CODE_IAM_TOKEN_ILLEGAL = 480;
    /**
     * 服务器错误
     */
    public static final int CODE_IAM_SERVER_ERROR = 500;
    /**
     * 找不到
     */
    public static final int CODE_IAM_NOT_FIND = 404;

    /**
     * 连接错误
     */
    public static final int CODE_IAM_NET_ERROR = 0;

    /**
     * 业务数据异常-密码错误
     */
    public static final int BUSINESS_ERROR_PIN_ERROR = 201001;

    /**
     * 业务数据异常-密码锁住
     */
    public static final int BUSINESS_ERROR_PIN_LOCK = 201002;

    public static boolean check(BaseJsonData result) {
        if (result != null && result.getHttpCode() == ResultCode.CODE_SUCCESS) {
            return true;
        }
        return false;
    }

    private static Map<Integer, String> sErrorCodeMap;

    static {
        sErrorCodeMap = new HashMap<>();
        sErrorCodeMap.put(CODE_IAM_TOKEN_EXPIRE, "ACCESS_TOKEN过期");
        sErrorCodeMap.put(CODE_IAM_PARSING_FAILURE, "ACCESS_TOKEN解析失败");
        sErrorCodeMap.put(CODE_IAM_LOGINOUT_ILLEGAL, "非法的登出参数");
        sErrorCodeMap.put(CODE_IAM_SESSION_EXCEPTION, "登出错误，用户会话异常");
        sErrorCodeMap.put(CODE_IAM_LOGIN_ILLEGAL, "非法的登录参数");
        sErrorCodeMap.put(CODE_IAM_PASSWORD_ERROR, "账号或者密码错误");
        sErrorCodeMap.put(CODE_IAM_TOKEN_ILLEGAL, "非法的REFRESH_TOKEN");
        sErrorCodeMap.put(CODE_IAM_SERVER_ERROR, "服务器错误");
        sErrorCodeMap.put(CODE_IAM_NOT_FIND, "找不到");
        sErrorCodeMap.put(CODE_IAM_NET_ERROR, "连接错误");
    }

    public static String getByErrorCode(int errorCode) {
        return sErrorCodeMap.get(errorCode);
    }
}