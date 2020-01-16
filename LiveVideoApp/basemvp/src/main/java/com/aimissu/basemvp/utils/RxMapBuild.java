package com.aimissu.basemvp.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class RxMapBuild {
    private Map<String, String> mMap = null;

    public RxMapBuild(Map<String, String> mMap) {
        this.mMap = mMap;
    }

    public static RxMapBuild created() {
        return new RxMapBuild(new HashMap<String, String>());
    }

    public RxMapBuild put(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            this.mMap.put(key, value);
        }
        return this;
    }

    public Map<String, String> build() {
        return this.mMap;
    }
}
