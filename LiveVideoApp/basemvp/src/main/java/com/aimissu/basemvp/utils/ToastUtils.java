package com.aimissu.basemvp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.aimissu.basemvp.net.rx.RxConfig;

/**

 */
public class ToastUtils {
    public enum Duration {SHORT, LONG}

    static Toast mToast;

    private ToastUtils() {
        throw new AssertionError();
    }

    public static Toast showToast(Context context, int stringResId) {
        return showToast(context, stringResId, Duration.SHORT);
    }

    public static Toast showToast(Context context, int stringResId, Duration duration) {
        if (context == null) {
            return null;
        }
        return showToast(context, context.getString(stringResId), duration);
    }

    public static Toast showToast(Context context, String text) {
        return showToast(context, text, Duration.SHORT);
    }

    public static Toast showToast(String text) {
        return showToast(RxConfig.getContext(), text, Duration.SHORT);
    }

    public static Toast showLongToast(String text) {
        return showToast(RxConfig.getContext(), text, Duration.LONG);
    }

    public static Toast showToast(int id) {
        return showToast(RxConfig.getContext(), RxConfig.getContext().getString(id), Duration.SHORT);
    }

    public static Toast showLocalToast(Context context, int stringResId) {
        return showLocalToast(context, stringResId, Duration.SHORT);
    }

    public static Toast showLocalToast(Context context, int stringResId, Duration duration) {
        if (context == null) {
            return null;
        }
        return showLocalToast(context, context.getString(stringResId), duration);
    }

    public static Toast showLocalToast(int id) {
        return showLocalToast(RxConfig.getContext(), RxConfig.getContext().getString(id), Duration.SHORT);
    }

    public static Toast showLocalToast(String text) {
        return showLocalToast(RxConfig.getContext(), text);
    }

    public static Toast showLocalToast(Context context, String text) {
        return showLocalToast(context, text, Duration.SHORT);
    }

    /**
     * 本地Toast统一加入“【@String】”来与后端区分
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast showLocalToast(Context context, String text, Duration duration) {
        if (!TextUtils.isEmpty(text))
            text = "【" + text + "】";
        return showToast(context, text, duration);
    }

    public static Toast showToast(Context context, String text, Duration duration) {
        if (context == null) {
            return null;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text,
                    (duration == Duration.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG));
        }
        try {
            mToast.setText(text);
            mToast.setDuration(duration == Duration.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
//        mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mToast;
    }
}
