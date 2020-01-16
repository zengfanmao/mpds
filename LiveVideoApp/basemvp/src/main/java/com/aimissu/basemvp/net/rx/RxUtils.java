package com.aimissu.basemvp.net.rx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.aimissu.basemvp.utils.FileUtils;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**

 * rx工具类
 */

public class RxUtils {

    /**
     * 获取默认的消费者，监听者，设置好的回调
     * 也可以自己
     * new BaseSubscriber<T>() {
     *
     * @param rxCallBack
     * @param <T>
     * @return
     * @Override public void onSucessed(T model) {
     * rxCallBack.onSucessed(model);
     * }
     * @Override public void onError(ResultExceptionUtils.ResponseThrowable e) {
     * rxCallBack.onFailed(e);
     * }
     * }
     */
    public static <T> BaseSubscriber<EntityResponse<T>> getDefaultSubscriber(final RxCallBack<T> rxCallBack) {
        return new BaseSubscriber<EntityResponse<T>>() {
            @Override
            public void onSucessed(EntityResponse<T> result) {
                rxCallBack.onSucessed(result.getEntity());
                rxCallBack.handResult(result.getEntity(), result.getBuilder(), true);
            }

            @Override
            public void onError(ResultExceptionUtils.ResponseThrowable e) {
                rxCallBack.onFailed(e);
                rxCallBack.handResult(null, e.getBuilder(), false);
            }
        };
    }

    public static DisposableSubscriber getJsonResponseSubscriber(final RxCallBack<JsonResponse> rxCallBack) {
        return new DisposableSubscriber() {
            @Override
            public void onNext(Object data) {
                JsonResponse jsonResponse = (JsonResponse) data;
                rxCallBack.onSucessed(jsonResponse);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultExceptionUtils.ResponseThrowable) {
                    ResultExceptionUtils.ResponseThrowable ex = ((ResultExceptionUtils.ResponseThrowable) e);
                    rxCallBack.onFailed(ex);
                } else {
                    rxCallBack.onFailed(new ResultExceptionUtils.ResponseThrowable(e));
                }


            }

            @Override
            public void onComplete() {

            }
        };
    }


    /**
     * 转换成html5需要的json成功对象
     *
     * @param data
     * @return
     */
    public static JSONObject toResponseSucessed(String httpcode, String data) {

        JSONObject response = new JSONObject();
        try {
            response.put("status", httpcode);
            response.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 转换成html5需要的json错误对象
     *
     * @param data
     * @return
     */
    public static JSONObject toResponseFailed(String httpcode, String data) {
        JSONObject response = new JSONObject();
        try {
            response.put("status", httpcode);
            response.put("error", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 构建上传的requestBody内容
     *
     * @param filePath
     * @param name
     * @return
     */
    public static RequestBody buildRequestBody(String filePath, String name) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Uri uri = Uri.fromFile(new File(filePath));
            int index = filePath.lastIndexOf('/');
            String filename = filePath.substring(index + 1);
            index = filePath.lastIndexOf('.');
            String ext = filePath.substring(index + 1);


            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext);
            LogUtil.i("hexiang", "mineType:" + mimeType + ",MediaType.parse(mimeType)：" + MediaType.parse(mimeType) + ",filename:" + filename);
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + name + "\";filename=\"" + filename + "\"")
                    , RequestBody.create(MediaType.parse(mimeType), new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri))));
            RequestBody requestBody = builder.build();

            return requestBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static MultipartBody filesToMultipartBody(List<File> files) {
        if (files == null || files.size() <= 0)
            return null;

        MultipartBody.Builder builder = new MultipartBody.Builder();
        int part = 0;
        for (File file : files) {
            if (file != null) {
                try {

                    String ext = FileUtils.getFileExtensionFromUrl(file.getPath());
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext.toLowerCase());
                    // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
                    RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
                    builder.addFormDataPart("file", file.getName(), requestBody);
                    part++;
                } catch (Exception e) {
                    LogUtil.i("hexiang", "uploadFile file addFormDataPart err:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (part <= 0)
            return null;
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }


    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        if (files != null) {
            for (File file : files) {
                parts.add(filesToMultipartBodyParts(file));
            }
        }
        return parts;
    }

    public static MultipartBody.Part filesToMultipartBodyParts(File file) {
        // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
        int index = file.getName().lastIndexOf('.');
        String ext = file.getName().substring(index + 1);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext);
        // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestBody);
    }

    /**
     * 线程调度，消费者在主线程，生产者在io线程（新起线程优于Schedulers.newThread()）
     *
     * @return
     */
    public static ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        };
    }

    /**
     * 执行异步
     *
     * @param runnableAsync 线程执行耗时
     * @param runnableSync  耗时成功运行之后主线程执行
     */
    public static void asyncRun(final Runnable runnableAsync, final Runnable runnableSync) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                if (runnableAsync != null)
                    runnableAsync.run();
                e.onNext("sucessed");
            }
        }).compose(schedulersTransformer())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (runnableSync != null)
                            runnableSync.run();
                    }
                });
    }

    /**
     * 执行异步
     *
     * @param runnableAsync 线程执行耗时
     */
    public static void asyncRun(final Runnable runnableAsync) {
        asyncRun(runnableAsync, null);
    }

    /**
     * 打印当前网络请求数
     *
     * @param logTag log tag
     */
    public static void printCurrentRequestUrl(String logTag) {
        try {
            LogUtil.i(logTag, "当前网络请求数：" + RetrofitClient.getInstance().getCurrentRequestSize());
            String reqUrl = "";
            HashMap<String, Headers> headerMaps = RetrofitClient.getInstance().getRequestCacheHeaders();
            if (headerMaps != null && headerMaps.size() > 0) {
                for (Map.Entry<String, Headers> entry : headerMaps.entrySet()) {
                    Headers header = entry.getValue();
                    String reqHead = String.valueOf(header.get("reqHead"));
                    JSONObject jsonObject = new JSONObject(reqHead);
                    reqUrl = String.valueOf(jsonObject.has("reqUrl") ? jsonObject.get("reqUrl") : "");
                    LogUtil.i(logTag, "网络正在请求，reqUrl:" + reqUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null)
            return false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED
                                || info[i].getState() == NetworkInfo.State.CONNECTING) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static MultipartBody headFilesToMultipartBody(List<File> files) {
        if (files == null || files.size() <= 0)
            return null;

        MultipartBody.Builder builder = new MultipartBody.Builder();
        int part = 0;
        for (File file : files) {
            if (file != null) {
                try {

                    String ext = FileUtils.getFileExtensionFromUrl(file.getPath());
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext.toLowerCase());
                    // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
                    RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
                    builder.addFormDataPart("portrait", file.getName(), requestBody);
                    part++;
                } catch (Exception e) {
                    LogUtil.i("hexiang", "uploadFile file addFormDataPart err:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (part <= 0)
            return null;
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

}
