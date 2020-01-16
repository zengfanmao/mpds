package com.aimissu.basemvp.net.rx;

import android.net.ParseException;
import android.support.annotation.NonNull;

import com.aimissu.basemvp.net.ResultCode;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;


/**

 * 统一错误处理工具类
 */
public class ResultExceptionUtils {

    public static final String TAG = ResultExceptionUtils.class.getSimpleName();

    /**
     * 根据错误编号，进行分类，及不同的错误提示，抛出不同的异常，
     *
     * @param e
     * @return
     */
    public static <T> ResponseThrowable handleException(Throwable e, RxBuilder.Builder<T> rxBuilder) {
        try {
            System.out.println(">>>  Rx handleException  --> " + (e != null ? e.getCause().toString() : ""));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ResponseThrowable ex = null;
        try {
            ex = new ResponseThrowable(e);
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                rxBuilder.setMessage(getErrMsg(httpException.code()));
            } else if (e instanceof ServerException) {
                rxBuilder.setMessage("网络错误");
            } else if (e instanceof RxJsonParRxseException) {
                rxBuilder.setResponseData(((RxJsonParRxseException) e).getResponseData());
                rxBuilder.setMessage("解析错误");
            } else if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                rxBuilder.setMessage("解析错误");
            } else if (e instanceof ConnectException
                    || e instanceof java.net.UnknownHostException) {
                rxBuilder.setMessage("网络错误");
            } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
                rxBuilder.setMessage("证书验证失败");
            } else if (e instanceof ConnectTimeoutException) {
                rxBuilder.setMessage("连接超时");
            } else if (e instanceof java.net.SocketTimeoutException) {
                rxBuilder.setMessage("连接超时");
            } else if (e instanceof ResponseThrowable) {
                rxBuilder.setMessage(getErrMsg(rxBuilder.getCode()));
            } else {
                rxBuilder.setMessage("未知错误:" + e.getMessage());
            }
        } catch (Exception ee) {
            rxBuilder.setMessage("未知错误");
        } finally {
            ex.setBuilder(rxBuilder);
            rxBuilder.build().printLog();
        }
        return ex;
    }

    public static String getErrMsg(@NonNull int code) {
        switch (code) {
            case ResultCode.CODE_SUCCESS:
                return "请求成功";
            case ResultCode.CODE_BUSINESS_ERROR:
                return " 业务异常，提示消息在msg中取";
            case ResultCode.CODE_TOKEN_EXPIRE:
                return "token过期";
            case ResultCode.CODE_REQUEST_FREQUENT:
                return "频繁操作";
            case ResultCode.CODE_LOGIN_FAILED:
                return "登录失败";
            case ResultCode.CODE_REQUEST_PARAM_ERROR:
                return "请求参数出错";
            case ResultCode.CODE_NOT_LOGIN:
                return "没有登录";
            case ResultCode.CODE_NOT_AUTHORITY:
                return "没有权限";
            case ResultCode.CODE_REQUEST_TIMEOUT:
                return "请求超时";
            case ResultCode.CODE_IAM_TOKEN_EXPIRE:
                return "ACCESS_TOKEN过期";
            case ResultCode.CODE_IAM_PARSING_FAILURE:
                return "ACCESS_TOKEN解析失败";
            case ResultCode.CODE_IAM_LOGINOUT_ILLEGAL:
                return "非法的登出参数";
            case ResultCode.CODE_IAM_SESSION_EXCEPTION:
                return "登出错误，用户会话异常";
            case ResultCode.CODE_IAM_LOGIN_ILLEGAL:
                return "非法的登录参数";
            case ResultCode.CODE_IAM_PASSWORD_ERROR:
                return "账号或者密码错误";
            case ResultCode.CODE_IAM_TOKEN_ILLEGAL:
                return "非法的REFRESH_TOKEN";
            case ResultCode.CODE_IAM_SERVER_ERROR:
                return "服务器错误";
            case ResultCode.CODE_IAM_NOT_FIND:
                return "没找到页面";
            case ResultCode.CODE_IAM_NET_ERROR:
                return "连接错误";
            case OtherResultCode.CODE_GATEWAY_TIMEOUT:
                return "请求超时";
            case OtherResultCode.CODE_BAD_GATEWAY:
                return "错误网关";
            case OtherResultCode.CODE_SERVICE_UNAVAILABLE:
                return "服务器停止服务";
            default:
                return "网络错误";
        }
    }


    /**
     * 自定义统一处理异常类
     */
    public static class ResponseThrowable extends Exception {

        public RxBuilder.Builder builder;
        /**
         * 错误提示是给用户看的
         */
        public String message;
        /**
         * http请求返回的body内容
         */
        public int code;

        public RxBuilder.Builder getBuilder() {
            return builder;
        }

        public void setBuilder(RxBuilder.Builder builder) {
            this.builder = builder;
            if (builder != null) {
                setMessage(builder.getMessage());
                setCode(builder.getCode());
            }
        }

        public ResponseThrowable(Throwable throwable) {
            super(throwable);
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class RxJsonParRxseException extends JsonParseException {

        public String responseData;

        public RxJsonParRxseException(Throwable throwable, String responseData) {
            super(throwable);
            this.responseData = responseData;

        }

        public RxJsonParRxseException(String responseData) {
            super(new JsonParseException(responseData));
            this.responseData = responseData;

        }

        public String getResponseData() {
            return responseData;
        }
    }


    /**
     * 自定义服务器异常类
     */
    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}

