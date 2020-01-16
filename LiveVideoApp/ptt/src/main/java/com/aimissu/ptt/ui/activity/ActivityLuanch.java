package com.aimissu.ptt.ui.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.event.ReConnectEvent;
import com.aimissu.ptt.entity.im.BaseMsg;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.VideoModel;
import com.aimissu.ptt.service.MqttService;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.JsonUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.xdandroid.hellodaemon.ServiceUtils;
import com.xdandroid.hellodaemon.WatchDogService;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 */
public class ActivityLuanch {
    private static Intent MqttServiceIntent;

    public static void viewMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void viewLocationActivity(Context context) {
        context.startActivity(new Intent(context, LocationActivity.class));
    }

    public static void startMqttService(Context context) {
//        MqttServiceIntent = new Intent(context, MqttService.class);
//        context.startService(MqttServiceIntent);
        AppManager.setStop(String.valueOf(false));
        Intent notifyIntent = new Intent(ReConnectEvent.class.getName());
//        notifyIntent.putExtra("stop",false);
        context.sendBroadcast(notifyIntent);
//        DaemonEnv.startServiceMayBind(MqttService.class);
        context.startService(new Intent(context, MqttService.class));
    }

    public static void stopMqttService(Context context) {
        AppManager.setStop(String.valueOf(true));
        ServiceUtils.unBindMqttService(context);
        Intent notifyIntent = new Intent(ReConnectEvent.class.getName());
//        notifyIntent.putExtra("stop",true);
        context.sendBroadcast(notifyIntent);
        context.stopService(new Intent(context, WatchDogService.class));
        context.stopService(new Intent(context, MqttService.class));
    }

    /**
     * 广播消息
     *
     * @param msg
     */
    public static void broadCastMsg(Context context, String action, String msg) {
        Intent notifyIntent = new Intent(action);
        notifyIntent.putExtra(IMType.Params.MSG_DATA, msg);
        context.sendBroadcast(notifyIntent);
    }

    private static NotificationManager notificationManager;

    public static void showNotify(Context context, String msg) {
        try {
            BaseMsg baseMsg = JsonUtils.toModel(msg, BaseMsg.class);

            //APP被禁用时不弹出通知  by cuizh.0410
            if (baseMsg != null && baseMsg.getMsgContent() != null && baseMsg.getMsgContent().equals("offline")) {
                return;
            }

            ChatMsg chatMsg = null;

            if (baseMsg != null) {
                int notifyId = 1;
                String content = "";
                String notifyTitle = "";
                Intent mResultIntent = new Intent(context, MainActivity.class);
                if (!TextUtils.isEmpty(baseMsg.getMsgType())) {

                    if (!TextUtils.isEmpty(baseMsg.getMsgContent())) {
                        try {
                            chatMsg = JsonUtils.toModel(baseMsg.getMsgContent(), ChatMsg.class);
                        } catch (Exception ex) {
                        }
                    }
                    if (chatMsg != null) {

                        notifyTitle = TextUtils.isEmpty(chatMsg.getGroupName())?chatMsg.getSendUserName():chatMsg.getGroupName();

                        content = TextUtils.isEmpty(chatMsg.getGroupName()) ? "" : (chatMsg.getSendUserName() + ":" + " ");

//                        content = TextUtils.isEmpty(chatMsg.getSendUserName()) ? "" : chatMsg.getGroupName() + " " + chatMsg.getSendUserName() + ":";

//                        notifyTitle = TextUtils.isEmpty(chatMsg.getMsgContent())? "": (new JSONObject(chatMsg.getMsgContent())).getString("Text");

                    }

                    if (IMType.MsgType.Text.toString().equals(baseMsg.getMsgType())) {
//                        content += "[文字]";
                        content += TextUtils.isEmpty(chatMsg.getMsgContent())? "": (new JSONObject(chatMsg.getMsgContent())).getString("Text");
                    } else if (IMType.MsgType.Map.toString().equals(baseMsg.getMsgType())) {
                        content += "[地图]";
                    } else if (IMType.MsgType.Image.toString().equals(baseMsg.getMsgType())) {
                        content += "[图片]";
                    } else if (IMType.MsgType.Video.toString().equals(baseMsg.getMsgType())) {
                        content += "[视频]";
                    } else if (IMType.MsgType.File.toString().equals(baseMsg.getMsgType())) {
                        content += "[文件]";
                    } else if (IMType.MsgType.Audio.toString().equals(baseMsg.getMsgType())) {
                        content += "[语音]";
                    } else if (IMType.MsgType.OUserLogin.toString().equals(baseMsg.getMsgType())) {
                        notifyId = 2;
                        mResultIntent = new Intent(context, LoginActivity.class);
                        content = "[退出登陆]";
                    }
                    if (chatMsg != null && AppManager.getLoginName().equals(chatMsg.getSendUserCode().trim())) {
                        //自己发的不推送通知
                        return;
                    }
                }

                Bitmap HeadPortraitBitmap = null;

                HeadPortraitBitmap = getBitmapFromUrl(chatMsg.getSendUserHeadPortrait());

                if (notificationManager == null) {
                    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                }


                //兼容android 8.0以上版本的Notification通知 by cuizh,0403
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    NotificationChannel channel1 = new NotificationChannel("ptt.notification",
                            "消息通知", NotificationManager.IMPORTANCE_HIGH);
                    channel1.setDescription("消息通知渠道");
                    channel1.enableLights(true);
                    channel1.setLightColor(Color.WHITE);
                    channel1.enableVibration(true);
                    channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    notificationManager.createNotificationChannel(channel1);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ptt.notification");
//                    builder.setContentTitle(context.getString(R.string.app_name))
                    builder.setContentTitle(notifyTitle)
                            .setContentText(content)
                            .setSmallIcon(R.mipmap.ic_launcher_pdt_cloud_trunking)
                            .setLargeIcon(HeadPortraitBitmap)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(System.currentTimeMillis())
//                            .setDefaults(Notification.DEFAULT_ALL)
                            .setVisibility(Notification.VISIBILITY_PRIVATE);

                    mResultIntent.putExtra(IMType.Params.MSG_DATA, msg);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(context, 0, mResultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(resultPendingIntent);

                    notificationManager.notify(1, builder.build());


                } else {
                    NotificationCompat.Builder notifyBuilder =
                            new NotificationCompat.Builder(context, "default")
//                                    .setContentTitle(context.getString(R.string.app_name))
                                    .setContentTitle(notifyTitle)
                                    .setContentText(content)
                                    .setSmallIcon(R.mipmap.ic_launcher_pdt_cloud_trunking)
                                    .setLargeIcon(HeadPortraitBitmap)
                                    .setAutoCancel(true)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setWhen(System.currentTimeMillis())
                                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND);
                    mResultIntent.putExtra(IMType.Params.MSG_DATA, msg);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(context, 0, mResultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notifyBuilder.setContentIntent(resultPendingIntent);
                    notificationManager.notify(notifyId, notifyBuilder.build());
                }
            }
        } catch (Exception ex) {
            LogHelper.sendErrorLog(ex);
        }

    }

    public static void viewVideoPlay(Context context, VideoModel videoModel) {
        context.startActivity(new Intent(context, VideoPlayActivity.class).putExtra(IMType.Params.MSG_DATA, new Gson().toJson(videoModel)));
    }

    public static void broadConnectTimeOut(MqttService mqttService, String action, int state) {
        Intent notifyIntent = new Intent(action);
        notifyIntent.putExtra(Configs.netWorkState, state);
        mqttService.sendBroadcast(notifyIntent);
    }

    public static void broadReConnect(Context context, String action) {
        Intent notifyIntent = new Intent(action);
        context.sendBroadcast(notifyIntent);
    }


    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    private static Bitmap getBitmapFromUrl(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(1000);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
        return bitmap;

    }
}

