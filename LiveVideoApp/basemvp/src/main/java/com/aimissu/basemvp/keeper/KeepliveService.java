package com.aimissu.basemvp.keeper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class KeepliveService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        KeepLiveManager.getInstance().registerReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        KeepLiveManager.getInstance().setServiceForeground(this);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        KeepLiveManager.getInstance().unRegisterReceiver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
