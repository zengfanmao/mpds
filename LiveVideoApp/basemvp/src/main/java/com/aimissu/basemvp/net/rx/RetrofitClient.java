package com.aimissu.basemvp.net.rx;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**

 * 升级成xjava2
 * 使用方式跟以前的RetrofiRtUtil很相似，以它为基础改的的
 * RxJava2+ Retrofit
 * 结合RxJava变成观察者模式，请求的call回调变成Flowable观察者模式，使用起来更加灵活，比如自由控制线程、变换数据等
 * 数据变换过程中封装了请求过程中的错误
 * <p>
 * 使用例子
 * 步骤1： postAsync返回一个被观察对象 Flowable（获得之后可以用操作符对数据进行处理赛选，变换等等，有丰富的api）
 * 步骤2： 通过subscribe方法 使被观察者对象和观察者实现的关联
 * <p>
 * 具体实现例子，获取配置
 * RetrofitClient.getInstance()
 * .postAsync(DataListKey.class, HttpConfig.RequestUrl.getListKey(), (Map<String, String>) null)
 * .subscribe(new BaseSubscriber<DataListKey>(){
 *
 * @Override public void onNext(DataListKey dataListKey) {
 * rxCallBack.onSucessed(dataListKey);
 * }
 * @Override public void onError(ResultExceptionUtils.ResponseThrowable e) {
 * LogUtil.e(">>>","RxCallBack Err Code:"+e.code+", Err MSg:"+e.message);
 * rxCallBack.onFailed(e);
 * }
 * });
 * <p>
 * <p>
 * 或者这样写
 * RetrofitClient.getInstance()
 * .postAsync(DataListKey.class, HttpConfig.RequestUrl.getListKey())
 * .subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataListKey>() {
 * @Override public void onSucessed(DataListKey o) {
 * <p>
 * }
 * @Override public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
 * <p>
 * }
 * }
 * ));
 * <p>
 * <p>
 * 或者这样，参数回调自由配置
 * RetrofitClient.getInstance()
 * .postAsync(DataListKey.class, HttpConfig.RequestUrl.getListKey())
 * .subscribe(RxUtils.getDefaultSubscriber(rxCallBack);
 */
public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static final String CANCELED = "Canceled";
    private Retrofit mRetrofit;
    private BaseApiService mHttpApiService;
    public static final String REQUEST_ID = "REQUEST_ID";

    private enum RequestMethod {GET, POST, DELETE}

    /**
     * 请求头缓存
     */
    private volatile HashMap<String, Headers> mRequestCacheHeaders;

    /**
     * 单利模式，比Dcl 和饿汉的单利模式都好
     */
    private static class SingletonHolder {
        public static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        mRetrofit = new Retrofit.Builder()
                .callFactory(obtainOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(RxConfig.BASE_API_URL)
                .build();
        mHttpApiService = mRetrofit.create(BaseApiService.class);
    }

    public final <T> Flowable getAsync(Class<T> cla, String url, Map<String, String> params) {
        return getAsync(cla, url, params, null);
    }

    public final <T> Flowable getAsync(Class<T> cla, String url) {
        Map<String, String> headers = securityHeaders(null);
        return mHttpApiService
                .executeGet(url, headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(false)
                                .setRequestMethod(RequestMethod.GET.toString())
                ));
    }

    /**
     * 根据get请求方式构造被观察者对象Flowable
     *
     * @param cla     类
     * @param url     请求地址
     * @param params  参数
     * @param headers http请求头
     * @param <T>     Json数据类型实体
     * @return
     */
    public final <T> Flowable getAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers) {
        return getAsync(cla, url, params, headers, false);
    }

    public final <T> Flowable getAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers, boolean requestFromH5) {
        headers = securityHeaders(headers);
        return mHttpApiService
                .executeGet(url, securityMap(params), headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(params)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.GET.toString())
                ));
    }

    /**
     * 根据get请求方式构造被观察者对象Flowable
     *
     * @param cla    类
     * @param url    请求地址
     * @param params 参数
     * @param <T>    Json数据类型实体
     * @return
     */
    public final <T> Flowable postAsync(Class<T> cla, String url, Map<String, String> params) {
        return postAsync(cla, url, params, null, false);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, Map<String, String> params, boolean requestFromH5) {
        return postAsync(cla, url, params, null, requestFromH5);
    }

    /**
     * 根据post请求方式构造被观察者对象Flowable
     *
     * @param cla     类
     * @param url     请求地址
     * @param params  参数
     * @param headers http请求头
     * @param <T>     Json数据类型实体
     * @return
     */
    public final <T> Flowable postAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers) {
        return postAsync(cla, url, params, headers, false);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers, boolean requestFromH5) {
        headers = securityHeaders(headers);
        return mHttpApiService
                .executePost(url, securityMap(params), headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(params)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.POST.toString())
                ));
    }

    private Map<String, String> securityMap(Map<String, String> map) {
        HashMap<String, String> params = new HashMap<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey())) {
                    //retrofit 值不允许 值为null
                    params.put(entry.getKey(), TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue());
                }
            }
        }
        return params;
    }

    private Map<String, String> securityHeaders(Map<String, String> map) {
        Map<String, String> headers = securityMap(map);

        headers.put(REQUEST_ID, String.valueOf(System.currentTimeMillis() + "-" + UUID.randomUUID().toString()));
        return headers;
    }

    private String getCurrentRequestId(Map<String, String> map) {
        return (map != null && map.containsKey(REQUEST_ID)) ? map.get(REQUEST_ID) : null;
    }

    //同步请求
    public final okhttp3.ResponseBody post(String url, Map<String, String> headers) {
        Call<ResponseBody> call = mHttpApiService.postNoParams(url, headers);
        try {
            Response<ResponseBody> responseBody = call.execute();
            return responseBody.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final Flowable postAsync(String url, Map<String, String> params, Map<String, String> headers) {
        return postAsync(null, url, params, headers, false);
    }

    public final Flowable postAsync(String url, Map<String, String> params, Map<String, String> headers, boolean requestFromH5) {
        return postAsync(null, url, params, headers, requestFromH5);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url) {
        return postAsync(cla, url, (Map<String, String>) null, null, false);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, boolean requestFromH5) {
        return postAsync(cla, url, (Map<String, String>) null, null, requestFromH5);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, RequestBody jsonStr) {
        return postAsync(cla, url, jsonStr, null, false);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, RequestBody jsonStr, boolean requestFromH5) {
        return postAsync(cla, url, jsonStr, null, requestFromH5);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, RequestBody jsonStr, Map<String, String> headers) {
        return postAsync(cla, url, jsonStr, headers, false);
    }

    public final <T> Flowable postAsync(Class<T> cla, String url, RequestBody jsonStr, Map<String, String> headers, boolean requestFromH5) {
        headers = securityHeaders(headers);
        return mHttpApiService.executePostJson(url, jsonStr, headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(jsonStr)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.POST.toString())
                ));
    }


    public final <T> Flowable deleteAsync(Class<T> cla, String url, Map<String, String> headers) {
        return deleteAsync(cla, url, headers, false);
    }

    public final <T> Flowable deleteAsync(Class<T> cla, String url, Map<String, String> headers, boolean requestFromH5) {
        headers = securityHeaders(headers);
        return mHttpApiService
                .executeDeleteNoParams(url, headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.DELETE.toString())
                ));
    }

    public final <T> Flowable deleteAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers) {
        return deleteAsync(cla, url, params, headers, false);
    }

    public final <T> Flowable deleteAsync(Class<T> cla, String url, Map<String, String> params, Map<String, String> headers, boolean requestFromH5) {
        headers = securityHeaders(headers);
        return mHttpApiService
                .executeDelete(url, securityMap(params), headers)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(params)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.DELETE.toString())
                ));
    }


    public <T> Flowable upload(Class<T> cla, String url, List<MultipartBody.Part> parts) {
        Map<String, String> headers = securityHeaders(null);
        return mHttpApiService.uploadFilesWithParts(url, parts)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(parts)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(false)
                                .setRequestMethod(RequestMethod.POST.toString())
                ));

    }

    public <T> Flowable upload(Class<T> cla, String url, MultipartBody requestBody) {
        Map<String, String> headers = securityHeaders(null);
        headers = securityHeaders(headers);
        return mHttpApiService.uploadFileWithRequestBody(url, requestBody)
                .compose(schedulersTransformer())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(requestBody)
                                .setRequestJsons(null)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(false)
                                .setRequestMethod(RequestMethod.POST.toString())
                ));
    }


    public <T> void download(Class<T> cla, Context context, String url, DownloadCallBack downloadCallBack) {
        download(cla, context, url, downloadCallBack, false);
    }

    public <T> void download(Class<T> cla, Context context, String url, DownloadCallBack downloadCallBack, boolean requestFromH5) {
        Map<String, String> headers = securityHeaders(null);
        mHttpApiService.downloadFile(url, headers)
                .compose(schedulersTransformernewThread())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.GET.toString())
                ))
                .subscribe(new DownloadSubscriber<Response<ResponseBody>>(downloadCallBack, context));
    }

    public <T> void download(Class<T> cla, Context context, String url, String filePath, Map<String, String> params, Map<String, String> headers, DownloadCallBack downloadCallBack) {
        download(cla, context, url, filePath, params, headers, downloadCallBack, false);
    }

    public <T> void download(Class<T> cla, Context context, String url, String filePath, Map<String, String> params, Map<String, String> headers, DownloadCallBack downloadCallBack, boolean requestFromH5) {
        headers = securityHeaders(headers);
        mHttpApiService.downloadFile(url, securityMap(params), headers)
                .compose(schedulersTransformernewThread())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestJsons(params)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.GET.toString())
                ))
                .subscribe(new DownloadSubscriber<Response<ResponseBody>>(downloadCallBack, context, filePath));
    }


    public <T> Flowable download(Class<T> cla, String url) {
        return download(cla, url, false);

    }

    public <T> Flowable download(Class<T> cla, String url, boolean requestFromH5) {
        Map<String, String> headers = securityHeaders(null);
        return mHttpApiService.downloadFile(url, headers)
                .compose(schedulersTransformernewThread())
                .compose(transformer(cla,
                        new RxBuilder.Builder<T>().setParseClass(cla)
                                .setRequestUrl(url)
                                .setRequestID(getCurrentRequestId(headers))
                                .setRequestFromH5(requestFromH5)
                                .setRequestMethod(RequestMethod.GET.toString())
                ));

    }

    /**
     * 线程调度，消费者在主线程，生产者在io线程（薪起线程优于Schedulers.newThread()）
     *
     * @return
     */
    FlowableTransformer schedulersTransformer() {
        return new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 变换数据
     *
     * @param cla
     * @param <T>
     * @return
     */
    public <T> FlowableTransformer<Response<ResponseBody>, T> transformer(final Class<T> cla, final RxBuilder.Builder<T> rxBuilder) {

        return new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull Flowable upstream) {
//                if (cla == null) {//解析成字符串
//                    return upstream.map(new ParseToString(rxBuilder)).onErrorResumeNext(new HttpResponseFunc<T>(rxBuilder));
//                } else {//解析成实体
//                    return upstream.map(new ParseToEntity<T>(cla, rxBuilder)).onErrorResumeNext(new HttpResponseFunc<T>(rxBuilder));
//                }
                return upstream.map(new ParseToEntity<T>(cla, rxBuilder)).onErrorResumeNext(new HttpResponseFunc<T>(rxBuilder));
            }

           /*
            //rxjava 1.x版本
            @Override
            public Object call(Object flowable) {
                return flowable.map(new ChangeToEntity<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }*/
        };
    }

    /**
     * 线程调度生产者和消费者都在另起线程
     */
    FlowableTransformer schedulersTransformernewThread() {
        return new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }

    public <T> Flowable<T> switchSchedulersIo(Flowable<T> flowable) {
        return flowable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /**
     * 统一处理请求过程中的错误
     *
     * @param <T>
     */
    private static class HttpResponseFunc<T> implements Function<Throwable, Flowable<T>> {
        RxBuilder.Builder<T> rxBuilder;

        public HttpResponseFunc(final RxBuilder.Builder<T> rxBuilder) {
            this.rxBuilder = rxBuilder;
        }

        @Override
        public Flowable<T> apply(@NonNull Throwable t) throws Exception {
            String requestId = rxBuilder.getRequestID();
            rxBuilder.setSucessed(false);
            if (rxBuilder.getRequestHeaders() == null && !TextUtils.isEmpty(requestId) && getInstance().getRequestCacheHeaders() != null) {
                if (getInstance().getRequestCacheHeaders().containsKey(requestId)) {
                    //服务器没返回的时候设置缓存请求头
                    rxBuilder.setRequestHeaders(getInstance().getRequestCacheHeaders().get(requestId));
                    //清除请求头缓存
                    getInstance().removeRequestCacheHeader(requestId);
                }
            }
            return Flowable.error(ResultExceptionUtils.handleException(t, rxBuilder));
        }
    }

    private static final MediaType jsonMediaType = MediaType.parse("application/json");
    private static final MediaType xmlMediaType = MediaType.parse("application/xml");
    private static final MediaType formMediaType = MediaType.parse("application/x-www-form-urlencoded");

    /**
     * 解析成实体，当parseClass为null的时候解析成字符串
     *
     * @param <T>
     */
    private static class ParseToEntity<T> implements Function<Response<ResponseBody>, IParseResponse<T>> {
        RxBuilder.Builder<T> builder;
        Class<T> parseClass;

        public ParseToEntity(final Class<T> parseClass, final RxBuilder.Builder<T> builder) {
            this.builder = builder;
            this.parseClass = parseClass;
        }

        @Override
        public IParseResponse<T> apply(@NonNull Response<ResponseBody> response) throws Exception {
            boolean isSuccessful = response.isSuccessful();
            if (response.raw() != null) {//加入请求时候正在的header打印
                Request request = response.raw().request();
                if (request != null) {
                    builder.setRequestHeaders(request.headers());
                }
            }
            //确保有请求头
            String requestId = builder.getRequestID();
            if (!TextUtils.isEmpty(requestId) && getInstance().getRequestCacheHeaders() != null) {
                if (getInstance().getRequestCacheHeaders().containsKey(requestId)) {
                    if (builder.getRequestHeaders() == null) {
                        //服务器没返回的时候设置缓存请求头
                        builder.setRequestHeaders(getInstance().getRequestCacheHeaders().get(requestId));
                    }
                    //清除请求头缓存
                    getInstance().removeRequestCacheHeader(requestId);
                }
            }


            if (!isSuccessful) {
                builder.setResponseData(response.errorBody().string())
                        .setSucessed(false)
                        .setCode(response.code());
                //统一处理其他接口请求时候，token失效问题
                int errorCode = response.code();

                throw new ResultExceptionUtils.ResponseThrowable(new Throwable());
            }
            // 1.parseClass == null && code is in [200..300) ，会调成功方法
            // 2.parseClass != null && code is in [200..300) && 解析成功 ，会调成功方法
            ResponseBody responseBody = response.body();
            String data = "";
            try {
//                private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                if (jsonMediaType.equals(responseBody.contentType()) || MediaType.parse("application/json; charset=utf-8").equals(responseBody.contentType()) || xmlMediaType.equals(responseBody.contentType()) || formMediaType.equals(responseBody.contentType())) {
                    data = responseBody == null ? null : responseBody.string();
                } else {
                    builder.build().printLog();
                    return new BodyResponse(responseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtil.i("hexiang", "json:" + data);
            builder.setResponseData(data).setCode(response.code());
            if (parseClass == null) {
                builder.build().printLog();
                return new JsonResponse(String.valueOf(response.code()), data);
            }

            T model;
            try {
                model = new Gson().fromJson(data, parseClass);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResultExceptionUtils.RxJsonParRxseException(data);
            }
            if (TextUtils.isEmpty(data)) {
                throw new ResultExceptionUtils.RxJsonParRxseException(data);
            }
            if (model == null) {
                throw new ResultExceptionUtils.RxJsonParRxseException(data);
            }
            builder.build().printLog();
            return new EntityResponse<>(model, builder);
        }
    }

    /**
     * 配置okhttp
     *
     * @return
     */
    private OkHttpClient obtainOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 15, TimeUnit.MINUTES))
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor());


//        Cache cache = new Cache(RxConfig.getContext().getCacheDir(), 20 * 1024 * 1024);
//        builder.retryOnConnectionFailure(true);
//        builder.cache(cache);
//        SSLSocketFactory sslSocketFactory = null;
//        try {
////            sslSocketFactory = getCertificates(application.getAssets().open("CA.cer"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (sslSocketFactory != null) {
//            builder.sslSocketFactory(sslSocketFactory);
//        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }

    /**
     * 证书
     *
     * @param certificates
     * @return
     */
    private SSLSocketFactory getCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void cancelTag(Object tag) {
        if (mRetrofit == null) {
            return;
        }
        okhttp3.Call.Factory factory = mRetrofit.callFactory();
        if (factory instanceof OkHttpClient) {
            OkHttpClient client = ((OkHttpClient) factory);
            for (okhttp3.Call call : client.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (okhttp3.Call call : client.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }

    public HashMap<String, Headers> getRequestCacheHeaders() {
        return mRequestCacheHeaders;
    }

    public int getCurrentRequestSize() {
        return mRequestCacheHeaders != null ? mRequestCacheHeaders.size() : 0;
    }

    public void setRequestCacheHeader(String key, Headers value) {
        if (this.mRequestCacheHeaders == null) {
            synchronized (this) {
                this.mRequestCacheHeaders = new HashMap<>();
            }
        }
        synchronized (this) {
            this.mRequestCacheHeaders.put(key, value);
        }
    }

    public void removeRequestCacheHeader(String key) {
        if (this.mRequestCacheHeaders != null) {
            synchronized (this) {
                if (this.mRequestCacheHeaders.containsKey(key)) {
                    this.mRequestCacheHeaders.remove(key);
                }
            }
        }
    }


}
