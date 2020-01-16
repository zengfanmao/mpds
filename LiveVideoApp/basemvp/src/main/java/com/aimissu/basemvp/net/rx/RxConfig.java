package com.aimissu.basemvp.net.rx;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RxConfig {
    private static volatile ExecutorService executorService;
    public static boolean isDebug = true;
    public static volatile String BASE_API_URL = "";
    public static volatile Context context;

    public static Context getContext() {
        return context;

    }

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (RxConfig.class) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                }
            }
        }
        return executorService;
    }

    public static void setContext(@NonNull Context context) {
        if (RxConfig.context == null) {
            synchronized (RxConfig.class) {
                if (RxConfig.context == null) {
                    RxConfig.context = context;
                }
            }
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static String getBaseApiUrl() {
        return BASE_API_URL;
    }

    public static String getMethodApiUrl(String method) {
        return getBaseApiUrl() + method;
    }

    public static void setBaseApiUrl(String baseApiUrl) {
        BASE_API_URL = baseApiUrl;
    }
}
