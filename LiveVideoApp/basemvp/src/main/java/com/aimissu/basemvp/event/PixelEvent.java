package com.aimissu.basemvp.event;

/**

 */
public class PixelEvent {
    public boolean destory = false;

    public PixelEvent(boolean destory) {
        this.destory = destory;
    }

    public boolean isDestory() {
        return destory;
    }

    public void setDestory(boolean destory) {
        this.destory = destory;
    }
}

