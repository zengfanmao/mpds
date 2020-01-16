package com.aimissu.ptt.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.event.RefreshMsgCountOnlineEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.grgbanking.video.VideoCore;
import com.grgbanking.video.utils.LogUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.LinphoneAddress;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 全局变量或操作
 *
 * @author JQ
 */
public class Global {

    /**
     * 全局上下文对象
     */
    public static Context mContext;

    /**
     * 屏幕密度
     */
    public static float mDensity;

    /**
     * 屏幕宽度
     */
    public static int mScreenWidth;

    /**
     * 屏幕高度
     */
    public static int mScreenHeight;

    /**
     * Handler对象
     */


    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
    };
    public static Gson mGson;
    private static String tag = "Global";

//	public static RequestQueue mQueue;


    public static void initialize(Context context) {
        mContext = context;
        initScreenSize();
//		mQueue = Volley.newRequestQueue(context);
    }

    private static void initScreenSize() {
        DisplayMetrics displayMetrics = mContext.getResources()
                .getDisplayMetrics();
        mDensity = displayMetrics.density;
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mGson = new Gson();
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * 判断当前是否在ui主线程中运行
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static int dp2px(int dp) {
        return (int) (mDensity * dp);
    }

    private static Toast mToast;

    /***
     * 优化显示吐司
     * @param message
     */
    public static void showToast(String message) {
//		Looper.prepare();
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();

    }

    public static View inflate(int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    /**
     * 隐藏输入法面板
     */
    public static void hideInputMethod(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = mFormat.format(currentTimeMillis);

        return time;
    }

    /**
     * 显示输入法面板
     */
    public static void showInputMethod(final EditText editText) {
        Global.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) editText
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        });
    }

    /**
     * px = dp * (dpi / 160)
     *
     * @param ctx
     * @param dip
     * @return
     */
    public static int dipToPX(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }


    /**
     * >=4.0 14
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }


    public static Bitmap getBitmap(int id, int dp) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), id, options);
        options.inSampleSize = calculateInSampleSize(options, Global.dp2px(dp), Global.dp2px(dp));
        options.inJustDecodeBounds = false;
        photo = BitmapFactory.decodeResource(mContext.getResources(), id, options);
        return photo;
    }


    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */

    public static int sp2px(int spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String getAddressDisplayName(LinphoneAddress address) {
        if (address.getDisplayName() != null) {
            return address.getDisplayName();
        } else {
            if (address.getUserName() != null) {
                return address.getUserName();
            } else {
                return address.asStringUriOnly();
            }
        }
    }

    public static String getUsernameFromAddress(String address) {
        if (address.contains("sip:"))
            address = address.replace("sip:", "");

        if (address.contains("@"))
            address = address.split("@")[0];

        return address;
    }

    /**
     * 获取保存图片的路径
     *
     * @return
     */
    public static String getPictureSavePath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = format.format(new Date(System.currentTimeMillis()));
        // Store image in dcim
        try {
            File rootDir = new File(Configs.Root);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            File audioDir = new File(Configs.ImgRoot);
            if (!audioDir.exists()) {
                audioDir.mkdirs();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        File file = new File(Configs.ImgRoot, Configs.PNG_FILE_PREFIX + filename + Configs.PNG_FILE_SUFFIX);
        String audioPath = file.getAbsolutePath();

        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
//        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return audioPath;
    }

    /**
     * 获取相机支持的分辨率
     *
     * @param mCamera
     * @return
     */
    public static String getCameraSupportedVideoSizes(int mCamera) {
        Camera camera = Camera.open(mCamera);
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> previewSIzes = params.getSupportedVideoSizes();
        if (previewSIzes == null) {
            previewSIzes = params.getSupportedPreviewSizes();
        }
        camera.release();
        StringBuilder s = new StringBuilder();
        if (previewSIzes != null) {
            for (Camera.Size size : previewSIzes) {
                if (s.length() != 0)
                    s.append(",");
                s.append(size.width).append('x').append(size.height);
            }
        }
        LogUtil.i(tag, "Video supported sizes: " + s.toString());
        return s.toString();
    }

    /**
     * 获取分辨率
     *
     * @param size
     * @return
     */
    public static VideoCore.VideoSize getVideoSize(String size) {
        if (size.equals("1920x1080")) {
            return VideoCore.VideoSize.VideoSize_1080P;
        } else if (size.equals("1280x720")) {
            return VideoCore.VideoSize.VideoSize_720P;
        } else if (size.equals("1024x768")) {
            return VideoCore.VideoSize.VideoSize_XGA;
        } else if (size.equals("800x600")) {
            return VideoCore.VideoSize.VideoSize_VGA;
        } else if (size.equals("640x480")) {
            return VideoCore.VideoSize.VideoSize_SVGA;
        } else if (size.equals("352x288")) {
            return VideoCore.VideoSize.VideoSize_CIF;
        } else if (size.equals("320x240")) {
            return VideoCore.VideoSize.VideoSize_QVGA;
        } else if (size.equals("176x144")) {
            return VideoCore.VideoSize.VideoSize_QCIF;
        }
        return null;
    }


    /**
     * 获取相机后置的分辨率
     */
    public static String[] getcameraBackSupportedVideoSizes() {
        String cameraBackSupportedVideoSizes = getCameraSupportedVideoSizes(0);
        String[] Sizes = cameraBackSupportedVideoSizes.split(",");
        String[] videoSizes = VideoCore.VideoSize.getSupportedVideoSizes();
        List<String> SizesList = Arrays.asList(Sizes);
        List<String> videoSizesList = Arrays.asList(videoSizes);
        ArrayList<String> supportSize = new ArrayList();

        for (String size : videoSizesList) {
            LogUtil.i(tag, "videoSizesList:" + size);
            if (SizesList.contains(size)) {
                supportSize.add(size);
            }
        }

        String[] array = new String[supportSize.size()];
        return supportSize.toArray(array);
    }

    /**
     * 获取相机前置的分辨率
     */
    public static String[] getcameraFrontSupportedVideoSizes() {
        String cameraFrontSupportedVideoSizes = getCameraSupportedVideoSizes(1);
        String[] Sizes = cameraFrontSupportedVideoSizes.split(",");
        String[] videoSizes = VideoCore.VideoSize.getSupportedVideoSizes();
        List<String> SizesList = Arrays.asList(Sizes);
        List<String> videoSizesList = Arrays.asList(videoSizes);
        ArrayList<String> supportSize = new ArrayList();

        for (String size : videoSizesList) {
            LogUtil.i(tag, "videoSizesList:" + size);
            if (SizesList.contains(size)) {
                supportSize.add(size);
            }
        }

        String[] array = new String[supportSize.size()];
        return supportSize.toArray(array);
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

            if (f.length() <= 4 * 1024) {
                f.delete();
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }


    //判断文件是否存在
    public static boolean fileIsLoading(String fileName) {
        File folder = new File(mContext.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp");
        File[] subFile = folder.listFiles();
        for (File file: subFile){
            LogUtil.i(tag,"file.getName():"+file.getName()+"  fileName:"+fileName);
            if (file.getName().contains(fileName)){
                return true;
            }
        }

        return false;

    }

    public static boolean isBaiDuExists() {
        File folder = new File(mContext.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp");
        File[] subFile = folder.listFiles();
        if (subFile != null && subFile.length > 2) {
            return true;
        }

        return false;
    }

    public static void isFirstRun() {

    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        boolean isFirstRun = AppManager.getSharePreferenceUtil().getBooleanValue("isFirstRun", true);
        LogUtil.i(tag,"第一次运行isFirstRun:"+isFirstRun);
        if (isFirstRun) {
            RxUtils.asyncRun(new Runnable() {
                @Override
                public void run() {
                    //删除缓存文件，留1及文件夹
                    File fileRoot = new File(Configs.Root);
                    if (fileRoot.isDirectory()) {
                        for (File file : fileRoot.listFiles()) {
                            if (file != null) {
                                if (file.isDirectory()) {
                                    deleteFile(file);
                                    deleteSubFileDirectory(file);
                                } else {
                                    file.delete();
                                }
                            }
                        }
                    }

                    File logRoot = new File(Configs.linPhoneLog);
                    if (logRoot.isDirectory()) {
                        for (File file : logRoot.listFiles()) {
                            if (file != null) {
                                if (file.isDirectory()) {
                                    deleteFile(file);
                                    deleteSubFileDirectory(file);
                                } else {
                                    file.delete();
                                }
                            }
                        }
                    }
                    Glide.get(mContext).clearDiskCache();
                    AppManager.getDaoSession().getLocalMsgFileDao().deleteAll();
//                        AppManager.getDaoSession().getLocalPersonUserEntityDao().deleteAll();
//                        AppManager.getDaoSession().getLocalUserGroupDao().deleteAll();
//                        AppManager.getDaoSession().getLocalMsgCountDao().deleteAll();
                    AppManager.setHadRedAllMsgCount(IMType.MsgFromType.Group.toString(), false);
                    AppManager.setHadRedAllMsgCount(IMType.MsgFromType.Person.toString(), false);
                    EventBus.getDefault().post(new RefreshMsgCountOnlineEvent());
                }
            }, new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showLocalToast("清理成功");
                }
            });

            AppManager.getSharePreferenceUtil().writeBooleanValue("isFirstRun", false);
        }

    }

    public static void deleteFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                for (File fileSub : file.listFiles()) {

                    deleteFile(fileSub);
                }
            } else {
                file.delete();
            }
        }
    }

    public static void deleteSubFileDirectory(File directory) {
        for (File file1 : directory.listFiles()) {
            for (File file : file1.listFiles()) {
                deleteSubFileDirectory(file);
                file.delete();
            }
            file1.delete();
        }
    }

}
