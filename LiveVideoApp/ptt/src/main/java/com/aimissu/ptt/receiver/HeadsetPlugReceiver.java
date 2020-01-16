package com.aimissu.ptt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.ptt.config.AppManager;
import com.grgbanking.video.VideoCore;

import java.lang.annotation.Target;

public class HeadsetPlugReceiver extends BroadcastReceiver {

    private static final String TAG = "HeadsetPlugReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra("state")) {

            if (intent.getIntExtra("state", 0) == 0) {

                AppManager.HEADSET_PLUG_STATE = 0;
                VideoCore.getInstance().enableSpeaker(true);
                LogUtil.i(TAG,"headset not connected");

            } else if (intent.getIntExtra("state", 0) == 1) {

                AppManager.HEADSET_PLUG_STATE = 1;
                VideoCore.getInstance().enableSpeaker(false);
                LogUtil.i(TAG,"headset has connected");

            }
        }

    }
}
