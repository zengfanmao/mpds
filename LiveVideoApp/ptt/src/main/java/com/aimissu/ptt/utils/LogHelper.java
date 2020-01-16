package com.aimissu.ptt.utils;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;

import com.aimissu.basemvp.net.rx.JsonResponse;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.PackageUtils;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.grgbanking.video.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import io.reactivex.schedulers.Schedulers;

/**
 * @author：dz-hexiang on 2018-11-20.
 * @email：472482006@qq.com
 */
public class LogHelper {
    public static final String TAG = LogHelper.class.getSimpleName();


    public static volatile UserEntity userEntity;

    /**
     * 上传用户日志
     *
     * @param
     */
    public static void sendLog(final String actionn,final String message) {
        try{
            RxConfig.getExecutorService().submit(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i(TAG, " run threadName："+Thread.currentThread().getName());
                    if (!AppManager.getLogIsUpload()) {
                        LogUtil.i(TAG, "没有开启上传IM日志功能");
                        return;
                    }
                    String api = AppManager.getLogApi();
                    if (TextUtils.isEmpty(api)) {
                        LogUtil.i(TAG, "日志api地址不能为空");
                        return;
                    }
                    Map<String, String> map = new HashMap<>();

                    if (userEntity == null) {
                        userEntity = AppManager.getUserData();
                    }
                    map.put("action", actionn);
                    map.put("message", message);
                    map.put("userCode", userEntity.getUserCode());
                    map.put("userName", userEntity.getUserName());
                    map.put("android_system", Build.MODEL);//AMOI N828
                    map.put("android_version", Build.VERSION.RELEASE);//4.2.1
                    map.put("app_version", PackageUtils.getVersionName(RxConfig.getContext()));

                    try {
                        RetrofitClient.getInstance().postAsync(null,
                                api,
                                map
                        ).unsubscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(RxUtils.getJsonResponseSubscriber(new RxCallBack<JsonResponse>() {
                                    @Override
                                    public void onSucessed(JsonResponse jsonResponse) {
                                        LogUtil.i(TAG, "threadName："+Thread.currentThread().getName()+",上传成功 api " + api + "：jsonResponse.data" + jsonResponse.data);

                                    }

                                    @Override
                                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                        LogUtil.i(TAG, "threadName："+Thread.currentThread().getName()+",上传失败 api " + api + "：err:" + e.message);
                                    }
                                }));
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.i(TAG, "sendLog：api " + api + " err:" + e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     * 上传错误日志  by cuizh,0423
     * @param throwable
     */
    public static void sendErrorLog(final Throwable throwable){

        sendLog("Error",getStackTrace(throwable));
    }


    /**
     * 获取异常的堆栈信息  by cuizh,0423
     * @param throwable
     * @return
     */
    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }

}
