package com.aimissu.basemvp.keeper;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import com.aimissu.basemvp.event.PixelEvent;
import com.aimissu.basemvp.net.rx.LogUtil;

import org.greenrobot.eventbus.EventBus;


public class KeepLiveManager {

    /**
     * 前台进程的NotificationId
     */
    private final static int GRAY_SERVICE_ID = 1001;

    /**
     * 单例模式
     */
    private static KeepLiveManager instance = new KeepLiveManager();


    /**
     * 监听锁屏/解锁的广播（必须动态注册）
     */
    private LockReceiver lockReceiver;

    public static KeepLiveManager getInstance() {
        return instance;
    }

    /**
     * 注册锁屏/解锁广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {
        lockReceiver = new LockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(lockReceiver, filter);
    }

    /**
     * 注销锁屏/解锁广播
     *
     * @param context
     */
    public void unRegisterReceiver(Context context) {

        if (lockReceiver != null) {
            context.unregisterReceiver(lockReceiver);
        }
    }

    /**
     * 设置服务为前台服务
     *
     * @param service
     */
    public void setServiceForeground(Service service) {
        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，此方法能有效隐藏Notification上的图标
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，此方法能有效隐藏Notification上的图标
            Intent innerIntent = new Intent(service, GrayInnerService.class);
            service.startService(innerIntent);
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        } else {
            //Android7.1 google修复了此漏洞，暂无解决方法（现状：Android7.1以上app启动后通知栏会出现一条"正在运行"的通知消息）
            service.startForeground(GRAY_SERVICE_ID, new Notification());
        }
    }

    private void startLiveActivity(Context context) {
        LogUtil.i("KeepLiveManager", "GrayInnerService startLiveActivity");
        Intent intent = new Intent(context, PixelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void destroyLiveActivity() {
        LogUtil.i("KeepLiveManager", "GrayInnerService destroyLiveActivity");
        EventBus.getDefault().post(new PixelEvent(true));
    }

    class LockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case Intent.ACTION_SCREEN_OFF:
                        startLiveActivity(context);
                        break;
                    case Intent.ACTION_SCREEN_ON:
                    case Intent.ACTION_USER_PRESENT:
                        destroyLiveActivity();
                        break;
                }
                LogUtil.i("KeepLiveManager", "onReceive action" + intent.getAction());
            }
        }
    }

    /**
     * 辅助Service
     */
    public static class GrayInnerService extends Service {
        @Override
        public void onCreate() {
            super.onCreate();
            LogUtil.i("KeepLiveManager", "GrayInnerService onCreate");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                startForeground(GRAY_SERVICE_ID, new Notification());
                stopForeground(true);
                stopSelf();
            } catch (Exception ex) {
                LogUtil.i("KeepLiveManager", "GrayInnerService onStartCommand err" + ex.getMessage());
            }
            LogUtil.i("KeepLiveManager", "GrayInnerService onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            LogUtil.i("KeepLiveManager", "GrayInnerService action onDestroy");
        }
    }
}
