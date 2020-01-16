package com.aimissu.basemvp.net.rx;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**

 * OKHTTP 自定义拦截
 */
public class BaseInterceptor implements Interceptor {
    private String okHttpUserAgent;
    private JSONArray mRequestRoute;//和服务端配合的请求head头route

//    private Map<String, String> headers;
//
//    public BaseInterceptor(Map<String, String> headers) {
//        this.headers = headers;
//    }

    public BaseInterceptor() {
        mRequestRoute = obtainReqHead();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Request.Builder requestBuilder = chainRequest.newBuilder();
        requestBuilder.header("channel", "MOBILE");

        addReqHeader(chainRequest, requestBuilder);

//        if (headers != null && headers.size() > 0) {
//            Set<String> keys = headers.keySet();
//            for (String headerKey : keys) {
//                requestBuilder.header(headerKey, headers.get(headerKey)).build();
//            }
//        }

        try {
//            if (okHttpUserAgent == null)
//                okHttpUserAgent = HttpConfig.getUserAgent(null);
//            requestBuilder.header("User-Agent", okHttpUserAgent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = requestBuilder.build();
        String requestID = request.headers().get(RetrofitClient.REQUEST_ID);
        RetrofitClient.getInstance().setRequestCacheHeader(requestID, request.headers());
        return chain.proceed(request);
    }

    /**
     * 获取reqHead头RequestRoute的基本参数
     *
     * @return
     */
    private JSONArray obtainReqHead() {
        try {
            JSONArray requestRoute = new JSONArray();

            //android sdk
            JSONObject route = new JSONObject();
            route.put("system", "android");
            route.put("version", Build.VERSION.SDK);//17
            requestRoute.put(route);
            //手机型号
            route = new JSONObject();
            route.put("system", Build.MODEL);//AMOI N828
            route.put("version", Build.VERSION.RELEASE);//4.2.1
            requestRoute.put(route);

            return requestRoute;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加入reqHead头
     *
     * @param chainRequest
     * @param builder
     */
    private void addReqHeader(Request chainRequest, Request.Builder builder) {
        if (mRequestRoute == null) {
            mRequestRoute = obtainReqHead();
        }

        JSONObject reqHead = new JSONObject();
        try {
//            reqHead.put("user", account);
            reqHead.put("reqTime", String.valueOf(System.currentTimeMillis()));
//            reqHead.put("requestDevice", deviceToken);
            if (chainRequest != null) {
                reqHead.put("reqUrl", chainRequest.url().url());
            }
            reqHead.put("RequestRoute", mRequestRoute);
        } catch (JSONException e) {
            LogUtil.i("RxLog", e.toString());
            e.printStackTrace();
        }
        builder.header("reqHead", reqHead.toString());
    }
}