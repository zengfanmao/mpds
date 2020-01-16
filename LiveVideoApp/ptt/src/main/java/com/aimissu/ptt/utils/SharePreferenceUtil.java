package com.aimissu.ptt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharePreferenceUtil {

    private SharedPreferences settings;

    public SharePreferenceUtil(Context context, String sharePreFileName) {
        settings = context.getSharedPreferences(sharePreFileName, Context.MODE_PRIVATE);
    }

    public boolean delete(String key) {
        return settings.edit().remove(key).commit();
    }

    /**
     * 清除SharedPreference中的所有内容
     *
     * @return
     */
    public boolean clear() {
        return settings.edit().clear().commit();
    }


    /**
     * 移除key
     *
     * @param key
     */
    public void remove(String key) {
        settings.edit().remove(key).commit();
    }

    /**
     * 是否包含key对应的value
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return settings.contains(key);
    }

    public String getStringValue(String key, String defValue) {
        return settings.getString(key, defValue);
    }

    public String getStringValue(String key) {
        return settings.getString(key, "");
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }

    public boolean getBooleanValue(String key) {
        return settings.getBoolean(key, false);
    }

    public float getFloatValue(String key, float defValue) {
        return settings.getFloat(key, defValue);
    }

    public float getFloatValue(String key) {
        return settings.getFloat(key, 0f);
    }

    public int getIntValue(String key, int defValue) {
        return settings.getInt(key, defValue);
    }

    public int getIntValue(String key) {
        return settings.getInt(key, -1);
    }

    public long getLongValue(String key, long defValue) {
        return settings.getLong(key, defValue);
    }

    public long getLongValue(String key) {
        return settings.getLong(key, -1L);
    }

    public boolean writeBooleanValue(String key, boolean value) {
        return settings.edit().putBoolean(key, value).commit();
    }

    public boolean writeStringValue(String key, String value) {
        return settings.edit().putString(key, value).commit();
    }

    public boolean writeFloatValue(String key, float value) {
        return settings.edit().putFloat(key, value).commit();
    }

    public boolean writeLongValue(String key, long value) {
        return settings.edit().putLong(key, value).commit();
    }

    public boolean writeIntValue(String key, int value) {
        return settings.edit().putInt(key, value).commit();
    }

    @SuppressWarnings("rawtypes")
    public Map getAll() {
        return settings.getAll();
    }
}
