package com.aimissu.basemvp.keeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.aimissu.basemvp.event.PixelEvent;
import com.aimissu.basemvp.net.rx.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class PixelActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        LogUtil.i("KeepLiveManager", "PixelEvent onCreate");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PixelEvent event) {
        LogUtil.i("KeepLiveManager", "PixelEvent onMessageEvent");
        if (event != null && event.isDestory()) {
            LogUtil.i("KeepLiveManager", "PixelEvent onDestroy");
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtil.i("KeepLiveManager", "PixelActivity onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
