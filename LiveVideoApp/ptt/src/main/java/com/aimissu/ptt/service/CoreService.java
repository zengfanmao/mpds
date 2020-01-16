package com.aimissu.ptt.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.aimissu.basemvp.keeper.KeepliveService;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.call.CallConnectedEvent;
import com.aimissu.ptt.entity.call.CallDisconnectedEvent;
import com.aimissu.ptt.entity.call.CallIncomingReceivedEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.activity.LoginActivity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.SystemHelper;
import com.aimissu.ptt.view.widget.audio.MediaManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.grgbanking.video.Message;
import com.grgbanking.video.MessageListener;
import com.grgbanking.video.VideoCore;
import com.grgbanking.video.VideoStateListener;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneProxyConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bywei on 2017/8/10.
 */

public class CoreService extends KeepliveService implements VideoStateListener, MessageListener {
    private String tag = "CoreService";
    private IntentFilter mKeepAliveIntentFilter;
    private int mLastSate=-2;
    private int callActionState = VideoStateListener.OUTGOING_INIT;
    private String basePath;
    private String callStartShound;
    private String callEndtShound;
    public static String anxiaShound;
    public static String errorShound;
    private  Vibrator vibrator;
//    private KeepAliveReceiver mKeepAliveReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        //initTimer();
        LogUtil.i(tag, "服务启动了·····.Thread.currentThread().getId():" + Thread.currentThread().getId());
    }


    /**
     * 初始化定时器，定时登录账号
     */
    private void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (VideoCore.getInstance() != null) {
                    LinphoneCore lc = VideoCore.getInstance().getLc();
                    if (lc == null) return;
                    lc.refreshRegisters();
                    lc.enableKeepAlive(true);
                    LinphoneProxyConfig proxy = lc.getDefaultProxyConfig();

                    if (proxy == null) {
                        LogUtil.i(tag, "proxy == null...........");
                    } else {
                        LogUtil.i(tag, "proxy != null...........");
                    }
                }
            }
        }, 1000, 1000 * 10);
    }


    /**
     * 初始化
     */
    private void init() {
        // 配置声音文件
        basePath = getFilesDir().getAbsolutePath();
        LogUtil.i(tag,"basePath的路径 : "+basePath+"   getExternalFilesDir() :"+ getExternalFilesDir("") );
        callStartShound = basePath + "/hujiaochenggong60ms.wav";
        callEndtShound = basePath + "/jieshu180ms.wav";
        anxiaShound = basePath + "/anxiaptt120ms.wav";
        errorShound = basePath + "/hujiaoshibai300ms.wav";
        String ringSoundFile = basePath + "/ringtone.mkv";
        String ringbackSoundFile = basePath + "/ringback.wav";
        String pauseSoundFile = basePath + "/hold.mkv";
        String errorToneFile = basePath + "/error.wav";
        //String linphoneFactoryConfigFile = basePath + "/linphonerc";
        //String linphoneConfigFile = basePath + "/.linphonerc";

        try {
            copyIfNotExist(R.raw.notes_of_the_optimistic, ringSoundFile);
            copyIfNotExist(R.raw.ringback, ringbackSoundFile);
            copyIfNotExist(R.raw.hold, pauseSoundFile);
            copyIfNotExist(R.raw.oldphone_mono, errorToneFile);
            copyIfNotExist(R.raw.hujiaochenggong60ms, callStartShound);
            copyIfNotExist(R.raw.jieshu180ms, callEndtShound);
            copyIfNotExist(R.raw.anxiaptt120ms, anxiaShound);
            copyIfNotExist(R.raw.hujiaoshibai300ms, errorShound);
            //copyIfNotExist(R.raw.coreconfig, linphoneConfigFile);
            //copyFromPackage(R.raw.linphonerc_factory, new File(linphoneFactoryConfigFile).getName());

        } catch (IOException e) {

        }
        VideoCore.createAndStart(this, this, ringSoundFile, ringbackSoundFile, pauseSoundFile, errorToneFile, false, false);
        //        VideoCore.createAndStart(this, this, ringSoundFile, ringbackSoundFile, pauseSoundFile, errorToneFile, linphoneFactoryConfigFile, linphoneConfigFile, false);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

    }

    private void copyIfNotExist(int ressourceId, String target) throws IOException {
        File lFileToCopy = new File(target);

        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.getName());
        }
    }

    private void copyFromPackage(int ressourceId, String target) throws IOException {
        FileOutputStream lOutputStream = openFileOutput(target, 0);
        InputStream lInputStream = getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while ((readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff, 0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.i(tag, "CoreService......服务启动了···onStart··");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(tag, "CoreService.....onDestroy..··");

        try {
            startService(new Intent(this, CoreService.class));
        }catch (Exception e){
            LogUtil.i(tag, "CoreService.....onDestroy..startService..."+e.getMessage());
        }
    }

    /**
     * 音视频电话呼叫的状态监听
     * @param state 状态码
     * @param message 信息
     */
    @Override
    public void onStateChanged(int state, String message) {
        //防止多次重复的监听消息
        if (state == mLastSate){
            return;
        }
        LogUtil.i(tag, "****************CoreService.........state : " + state + "    message:" + message +"     mLastSate: "+mLastSate);

        switch (state){
            case VideoStateListener.ERROR:
            case VideoStateListener.REGISTER_FAILED:
                EventBus.getDefault().post(new CallDisconnectedEvent());
                break;

            case VideoStateListener.GLOBAL_ON:

                break;

            case VideoStateListener.REGISTER_OK:

                break;

            case VideoStateListener.INCOMING_RECEIVED:
                handleInComingRecevied();
                LogUtil.i(tag,"INCOMING_RECEIVED.....");
                callActionState = VideoStateListener.INCOMING_RECEIVED;
                SystemHelper.setTopApp(this);
                break;

            case VideoStateListener.OUTGOING_INIT:
                callActionState = VideoStateListener.OUTGOING_INIT;

                break;

            case VideoStateListener.OUTGOING_PROCESS:

                break;

            case VideoStateListener.OUTGOING_RINGING:
                callActionState = VideoStateListener.OUTGOING_RINGING;
                break;

            case VideoStateListener.CONNECTED:
                vibrator.vibrate(100);
                MediaManager.playSound(callStartShound);
                Global.getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        MediaManager.playSound(callStartShound);
                        LogUtil.i(tag,"播放建立成功的通话。。。");
                    }
                },2000);

//                sendCallConnectedBroadcast();
                if (callActionState == VideoStateListener.INCOMING_RECEIVED || callActionState == VideoStateListener.OUTGOING_RINGING  || AppManager.personPdtCall){
                    EventBus.getDefault().post(new CallConnectedEvent(true));
                    LogUtil.i(tag,"CONNECTED.....true");
                }else {
                    EventBus.getDefault().post(new CallConnectedEvent());
                    LogUtil.i(tag,"CONNECTED.....false");
                }

                LogUtil.i(tag,"CONNECTED.....");


                break;

            case VideoStateListener.CALL_END:

                break;
            case VideoStateListener.CALL_RELEASED:
                EventBus.getDefault().post(new CallDisconnectedEvent());
                MediaManager.playSound(callEndtShound);

                break;

            case VideoStateListener.STREAMS_RUNNING:

                break;


        }

        mLastSate=state;
    }

    /**
     * 来电响应
     */
    private void handleInComingRecevied() {
        EventBus.getDefault().post(new CallIncomingReceivedEvent());
    }


    @Override
    public void onReceived(Message message) {

    }

}
