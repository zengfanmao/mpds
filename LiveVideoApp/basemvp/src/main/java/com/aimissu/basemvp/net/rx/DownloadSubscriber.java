package com.aimissu.basemvp.net.rx;

import android.content.Context;

import java.lang.ref.WeakReference;

import io.reactivex.subscribers.DisposableSubscriber;

/**

 * 下载的回调， 观察者的实现
 */
public class DownloadSubscriber<ResponseBody> extends DisposableSubscriber<ResponseBody> {
    DownloadCallBack downloadCallBack;
    public static WeakReference<Context> mContextReference;
    public String filePath;

    public DownloadSubscriber(DownloadCallBack callBack, Context context) {
        this.downloadCallBack = callBack;
        if (context != null) {
            mContextReference = new WeakReference<>(context.getApplicationContext());
        }
    }

    public DownloadSubscriber(DownloadCallBack callBack, Context context, String filePath) {
        this.downloadCallBack = callBack;
        this.filePath = filePath;
        if (context != null) {
            mContextReference = new WeakReference<>(context.getApplicationContext());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (downloadCallBack != null) {
            downloadCallBack.onStart();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (downloadCallBack != null) {
            downloadCallBack.onError(e);
        }
    }

    @Override
    public void onComplete() {
        if (downloadCallBack != null) {
            downloadCallBack.onCompleted();
        }
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        DownLoadManager.getInstance(downloadCallBack).writeResponseBodyToDisk(mContextReference.get(), ((BodyResponse) responseBody).body, filePath);
    }
}