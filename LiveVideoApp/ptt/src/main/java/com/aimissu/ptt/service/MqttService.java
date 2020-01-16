package com.aimissu.ptt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.event.ConnectTimeOutEvent;
import com.aimissu.ptt.entity.event.ReConnectEvent;
import com.aimissu.ptt.entity.im.BaseMsg;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.xdandroid.hellodaemon.AbsWorkService;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 另起进程监听推送服务
 *
 * @author hexiang
 */
public class MqttService extends AbsWorkService implements MqttCallback, IMqtt, IMqttActionListener {
    private static final String TAG = MqttService.class.getSimpleName();
    private static volatile IMqttAsyncClient iMqttAsyncClient;
    private static volatile IMqttToken iMqttToken;
    private static volatile int mReConnentCount = 0;
    private static volatile ReConnectReceiver mReConnectReceiver;
//    private final Messenger mMessenger = new Messenger(new ServiceHandler());

    @Override
    public IBinder onBind(Intent intent) {
//        return mMessenger.getBinder();
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, " onCreate");
        registReConnectReceiver();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        boolean isStopService = isStopListener();
        LogUtil.i(TAG, " shouldStopService:" + isStopService + ",thread:" + Thread.currentThread().getName());
        return isStopService;
    }


    public void stopService() {
        LogUtil.i(TAG, " stopService - stop :,thread:" + Thread.currentThread().getName());
        myHandler.removeCallbacksAndMessages(null);
        stopSelf();
        AbsWorkService.cancelJobAlarmSub();
        disconnect();
    }

    @Override
    protected int onStart(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, "onStart(Intent intent, int flags, int startId)");
        return super.onStart(intent, flags, startId);
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, " startWork");
        connect();
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, "stopWork");
        disconnect();
    }

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, "isWorkRunning");
        return getMqttClient().isConnected();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        LogUtil.i(TAG, " onBind");
        return null;
    }

    @Override
    protected void onEnd(Intent rootIntent) {
        LogUtil.i(TAG, "onEnd");
        if (isStopListener()) {
            onServiceKilled(rootIntent);
        } else {
            super.onEnd(rootIntent);
        }

    }

    //服务被杀时调用, 可以在这里面保存数据.
    @Override
    public void onServiceKilled(Intent rootIntent) {
        LogUtil.i(TAG, "onServiceKilled");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, " onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void connectionLost(Throwable cause) {
        LogUtil.i(TAG, "connectionLost: connection was lost......");
        while (true) {
            try {//如果没有发生异常说明连接成功，如果发生异常，则死循环
                Thread.sleep(1000);
                connect();
                break;
            } catch (Exception e) {
                continue;
            }
        }
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        RxConfig.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                if (message != null) {
                    try {
                        String msg = message.toString();
                        LogUtil.i(TAG, " messageArrived: topic:" + topic + ",mesage:" + msg);
                        ActivityLuanch.broadCastMsg(MqttService.this, BaseMsg.class.getName(), msg);
                        ActivityLuanch.showNotify(MqttService.this, msg);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        token.isComplete();
    }

    public String formatServer(String server) {
        String newServer = "tcp://" + server + ":1883";
        LogUtil.i(TAG, "formatServer:" + newServer);
        return newServer;
    }


    @Override
    public String getServiceUrl() {
        LogUtil.i(TAG, " getServiceUrl");
        return AppManager.getServerIpInOtherProcess();
    }

    @Override
    public String getDeviceId() {
        return MqttAsyncClient.generateClientId();
    }

    @Override
    public IMqttAsyncClient getMqttClient() {
        if (this.iMqttAsyncClient == null) {
            synchronized (MqttService.class) {
                if (this.iMqttAsyncClient == null) {
                    try {
                        String server = getServiceUrl();
                        if (!TextUtils.isEmpty(server)) {
                            this.iMqttAsyncClient = new MqttAsyncClient(formatServer(server), getDeviceId(), new MemoryPersistence());
                            this.iMqttAsyncClient.setCallback(this);
                        }
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this.iMqttAsyncClient;
    }

    public boolean isStopListener() {
        return AppManager.isStop();
    }

    @Override
    public void connect() {

        RxConfig.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                if (isStopListener()) {
                    disconnect();
                    return;
                }
                LogUtil.i(TAG, " connect .isConnected: " + (getMqttClient() == null ? false : getMqttClient().isConnected() + " mReConnentCount :   " + mReConnentCount));
                LogUtil.i(TAG, " getMqttClient() ==  null:" + String.valueOf(getMqttClient() == null) + " ,connect .isConnected: " + (getMqttClient() == null ? false : getMqttClient().isConnected()));
                if (getMqttClient() == null)
                    return;
                synchronized (this) {
                    if (!getMqttClient().isConnected()) {
                        try {
                            LogUtil.i(TAG, " connect getServerURI:" + getMqttClient().getServerURI());
                            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                            mqttConnectOptions.setAutomaticReconnect(true);
                            mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                            getMqttClient().connect(mqttConnectOptions, this, MqttService.this).waitForCompletion(3000);

                        } catch (MqttSecurityException e) {
                            LogUtil.i(TAG, " 服务器安全认证出错 " + e.getMessage());
                            e.printStackTrace();
                        } catch (MqttException e) {
                            switch (e.getReasonCode()) {
                                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
                                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                                case MqttException.REASON_CODE_CONNECTION_LOST:
                                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                                    LogUtil.i(TAG, "连接服务器出错:" + e.getMessage());
                                    e.printStackTrace();
                                    reConnect();
                                    break;
                                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                                    LogUtil.i(TAG, "连接服务器认证出错:" + e.getMessage());
                                    reConnect();
                                    break;
                                default:
                                    LogUtil.i(TAG, "连接出错其他错误 :" + e.getMessage());
                                    reConnect();
                            }
                        }
                    } else {
                        if (iMqttToken != null) {
                            try {
                                String[] topics = iMqttToken.getTopics();
                                if (topics == null) {
                                    subscribeTopics();
                                } else {
                                    String loginName = getLoginName();
                                    boolean isSubcribe = false;
                                    for (String topic : topics) {
                                        if (topic.equals(loginName)) {
                                            isSubcribe = true;
                                        }
                                    }
                                    LogUtil.i(TAG, "loginName:" + loginName + " is connected ,has sub topics :" + Arrays.toString(topics));
                                    if (!isSubcribe) {
                                        subscribeTopics();
                                    }
                                }

                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }
        });

    }

    static class MyHandler extends Handler {

    }

    MyHandler myHandler = new MyHandler();

    private void reConnect() {
        if (mReConnentCount < 600) {
            disconnect();
            myHandler.removeCallbacksAndMessages(null);
            //2秒后尝试重连接
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    disconnect();
                    connect();
                    mReConnentCount++;
                }
            }, 500);
        } else {
            ActivityLuanch.broadConnectTimeOut(this, ConnectTimeOutEvent.class.getName(), -1);
            LogUtil.i(TAG, "mqtt重连停止了");
        }
    }

    private void disconnect() {
        try {
            synchronized (this){
                if (iMqttAsyncClient != null&&iMqttAsyncClient.isConnected()) {
                    iMqttAsyncClient.disconnect();
                }
            }
//            if (getMqttClient() != null) {
//                if (getMqttClient().isConnected()) {
//                    getMqttClient().disconnect();
//                    LogUtil.i(TAG, "disconnect successed");
//                } else {
//                    LogUtil.i(TAG, "disconnect already is disconnect");
//                }
//
//            }
            iMqttAsyncClient = null;
            LogUtil.i(TAG, "disconnect successed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribeTopics() {
        RxConfig.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isStopListener()) {
                        disconnect();
                        return;
                    }
                    List<String> topics = new ArrayList<String>();
                    topics.add("livevideo/all");
                    if (isLogin()) {
                        topics.add(getLoginName());
                    }
                    String[] subTopics = topics.toArray(new String[topics.size()]);
                    int[] subQoss = new int[topics.size()];
                    for (int i = 0; i < subTopics.length; i++) {
                        subQoss[i] = 0;
                    }
                    try {
                        synchronized (this) {
                            iMqttToken = getMqttClient().subscribe(subTopics, subQoss);
                            LogUtil.i(TAG, " subscribeTopics : " + Arrays.toString(iMqttToken.getTopics()));
                        }

                    } catch (MqttException e) {
                        e.printStackTrace();
                        LogUtil.i(TAG, " subscribeTopics MqttException: " + e.getMessage());
                    }
                } catch (Exception ex) {
                    LogUtil.i(TAG, " subscribeTopics : ex" + ex.getMessage());
                }

            }
        });

    }

    @Override
    public boolean isLogin() {
        return AppManager.isLogin();
    }

    @Override
    public String getLoginName() {
        LogUtil.i(TAG, " getLoginName");
        return String.format("user/%1$s", AppManager.getLoginNameInOtherProcess());
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        LogUtil.i(TAG, " connect .onSuccess");
        synchronized (this) {
            this.iMqttToken = asyncActionToken;
        }
        subscribeTopics();
        mReConnentCount = 0;
        ActivityLuanch.broadConnectTimeOut(this, ConnectTimeOutEvent.class.getName(), 0);
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        LogUtil.i(TAG, " connect .onFailure:" + exception.getMessage());
        synchronized (this) {
            this.iMqttToken = null;
        }
    }


//    class ServiceHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    Bundle bundle = msg.getData();
//                    if (bundle != null) {
//                        String mServerUrl = bundle.getString(Configs.SERVERURL);
//                        LogUtil.i(TAG, "mServerUrl:" + mServerUrl);
//                        AppManager.setIMserver(mServerUrl);
//                        connect();
//                    }
//                    break;
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG, "MQTT销毁···onDestroy");
        super.onDestroy();
        disconnect();
        unRegistReConnectReceiver();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReConnectEvent(ReConnectEvent event) {
        if (event != null) {
            mReConnentCount = 0;
            reConnect();
        }
    }

    private void registReConnectReceiver() {
        if (mReConnectReceiver == null) {
            mReConnectReceiver = new ReConnectReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ReConnectEvent.class.getName());
        registerReceiver(mReConnectReceiver, filter);
    }

    private void unRegistReConnectReceiver() {
        if (mReConnectReceiver != null) {
            unregisterReceiver(mReConnectReceiver);
        }
    }

    class ReConnectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "ReConnectReceiver:" + intent.toString());
            if (intent != null) {
                if (isStopListener()) {
                    //收到停止服务直接停止
                    stopService();
                    return;
                }
            }
            mReConnentCount = 0;
            reConnect();
        }
    }
}