package com.aimissu.basemvp.net.rx;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * 下载工具类
 */
public class DownLoadManager {

    private DownloadCallBack callBack;

    private static final String TAG = "DownLoadManager";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";

    private static String PNG_CONTENTTYPE = "image/png";

    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String fileSuffix = "";

    private Handler handler;

    public DownLoadManager(DownloadCallBack callBack) {
        this.callBack = callBack;
    }

    private static DownLoadManager sInstance;

    /**
     * DownLoadManager getInstance
     */
    public static synchronized DownLoadManager getInstance(DownloadCallBack callBack) {
        if (sInstance == null) {
            sInstance = new DownLoadManager(callBack);
        } else {
            sInstance.setCallBack(callBack);
        }

        return sInstance;
    }

    public DownloadCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(DownloadCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 保存到sd卡中
     *
     * @param context
     * @param body
     * @return
     */
    public boolean writeResponseBodyToDisk(Context context, ResponseBody body, String filePath) {

        Log.d(TAG, "contentType:>>>>" + body.contentType().toString());

        String type = body.contentType().toString();

        if (type.equals(APK_CONTENTTYPE)) {

            fileSuffix = ".apk";
        } else if (type.equals(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.equals(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        }

        // 其他同上 自己判断加入

        if (TextUtils.isEmpty(filePath))
            filePath = context.getExternalFilesDir(null) + File.separator + System.currentTimeMillis() + fileSuffix;

        Log.d(TAG, "path:>>>>" + filePath + ",thread:" + Thread.currentThread().getName());

        try {

            Uri uri = Uri.fromFile(new File(filePath));
            File futureStudioIconFile = new File(GetPathFromUri4kitkat.getPath(RxConfig.getContext(), uri));

            if (futureStudioIconFile.exists()) {
                futureStudioIconFile.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                Log.d(TAG, "file length: " + fileSize);
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    if (callBack != null) {
                        handler = new Handler(Looper.getMainLooper());
                        final long finalFileSizeDownloaded = fileSizeDownloaded;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onProgress(finalFileSizeDownloaded);
                            }
                        });

                    }
                }

                outputStream.flush();
                Log.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                final String receivedUrl = filePath;
                if (callBack != null) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSucess(receivedUrl, "", fileSize);

                        }
                    });
                    Log.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                }

                return true;
            } catch (IOException e) {
                if (callBack != null) {
                    callBack.onError(e);
                }
                return false;
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                callBack.onError(e);
            }
            return false;
        }

    }
}
