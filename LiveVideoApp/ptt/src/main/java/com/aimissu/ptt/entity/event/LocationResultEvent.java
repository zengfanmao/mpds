package com.aimissu.ptt.entity.event;

import com.aimissu.ptt.db.LocalCache;

/**

 */
public class LocationResultEvent {
    LocalCache localCache;

    public LocationResultEvent(LocalCache localCache) {
        this.localCache = localCache;
    }

    public LocalCache getLocalCache() {
        return localCache;
    }

    public void setLocalCache(LocalCache localCache) {
        this.localCache = localCache;
    }
}
