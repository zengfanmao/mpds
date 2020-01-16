package com.aimissu.basemvp.net.rx;

/**
 * 新增的一些ResultCode中没有的错误类型
 */
public class OtherResultCode {
    /**
     * 未知错误
     */
    public static final int CODE_UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int CODE_PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int CODE_NETWORD_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int CODE_HTTP_ERROR = 1003;

    /**
     * 证书出错
     */
    public static final int CODE_SSL_ERROR = 1005;

    /**
     * 连接超时
     */
    public static final int CODE_TIMEOUT_ERROR = 1006;


    /**
     * 额外新增3个错误类型
     * 错误网关，无效网关
     */
    public static final int CODE_BAD_GATEWAY = 502;
    /**
     * 服务器停止服务
     */
    public static final int CODE_SERVICE_UNAVAILABLE = 503;
    /**
     * 请求超时
     */
    public static final int CODE_GATEWAY_TIMEOUT = 504;
}