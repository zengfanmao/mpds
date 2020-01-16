package com.aimissu.ptt.app;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.utils.PackageUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.service.MqttService;
import com.aimissu.ptt.ui.activity.MainActivity;
import com.aimissu.ptt.utils.Global;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.xdandroid.hellodaemon.DaemonEnv;

//import com.squareup.leakcanary.LeakCanary;

/**
 */
public class MyApp extends MultiDexApplication {
    public static MainActivity mainActivity;

    @Override
    public void onCreate() {
        initCrashHandler();
        super.onCreate();
        //RxConfig.setBaseApiUrl("http://aimissu.xicp.net:13366/");
        //局域网服务器
        //RxConfig.setBaseApiUrl("http://192.168.11.16/");
        //RxConfig.setBaseApiUrl("http://aimissu.xicp.net:11695/");
        //个人服务器
        //RxConfig.setBaseApiUrl("http://47.94.104.229/");
        //外包服务器
//        RxConfig.setBaseApiUrl("http://120.78.67.146/");
        //内存分析
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // Normal app init code...

        RxConfig.setContext(this);
        AppManager.setApp(MyApp.this);
        if (PackageUtils.isUiProcess(this)) {
            FileDownloader.setupOnApplicationOnCreate(this);
            Fresco.initialize(this);
            AppManager.initDirectory();
            AppManager.initSmallVideo();
            AppManager.initDaoSession();
            Global.initialize(RxConfig.getContext());
            CrashReport.initCrashReport(getApplicationContext(), "2a5f955669", true);
            //百度地图
            SDKInitializer.initialize(getApplicationContext());
        }
        //定时重启下service
        DaemonEnv.initialize(
                this,  //Application Context.
                MqttService.class, //刚才创建的 Service 对应的 Class 对象.
                4 * 60 * 1000);  //定时唤醒的时间间隔(ms), 默认 5 分钟.

        // Android 7.0 FileUriExposedException 解决
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

//        Thread.setDefaultUncaughtExceptionHandler(this);
        MultiDex.install(this);
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }

    private void initCrashHandler() {
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
    }
//    @Override
//    public void uncaughtException(Thread thread, Throwable throwable) {
//        LogUtil.i("app","全局异常。。。。。。。");
//        throwable.printStackTrace();
//        ActivityLuanch.stopMqttService(this);
//    }


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        MyApp.mainActivity = mainActivity;

    }

    public boolean isMainActvityRunning() {
        if (getMainActivity() != null) {
            if (!getMainActivity().isFinishing() && !getMainActivity().isDestroyed()) {
                return true;
            }
        }
        return false;
    }

}
