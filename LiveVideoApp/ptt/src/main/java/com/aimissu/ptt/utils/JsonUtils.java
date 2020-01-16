package com.aimissu.ptt.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * json工具类
 */
public class JsonUtils {
    public final static Gson mGson = new Gson();

    public static <T> T toModel(String json, Class<T> mClass) {
        if (TextUtils.isEmpty(json) || mClass == null) {
            return null;
        }
        try {
            T model= mGson.fromJson(json,mClass);
            return  model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
