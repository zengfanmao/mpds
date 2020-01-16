package com.xdandroid.hellodaemon;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;

import com.aimissu.ptt.service.MqttService;

public class ServiceUtils {

    public static void unBindMqttService(@NonNull Context context) {
        if (DaemonEnv.BIND_STATE_MAP.containsKey(DaemonEnv.sServiceClass)) {
            context.unbindService(DaemonEnv.BIND_STATE_MAP.get(DaemonEnv.sServiceClass) );
        }
        if (DaemonEnv.BIND_STATE_MAP.containsKey(WatchDogService.class)) {
            context.unbindService(DaemonEnv.BIND_STATE_MAP.get(WatchDogService.class) );
        }
        if (DaemonEnv.BIND_STATE_MAP.containsKey(MqttService.class)) {
            context.unbindService(DaemonEnv.BIND_STATE_MAP.get(MqttService.class) );
        }
    }
}
