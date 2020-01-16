package com.aimissu.ptt.entity.event;

public class HeadSetPttEvent {

    private boolean isPttOn;

    public HeadSetPttEvent(boolean pttOn){
        isPttOn = pttOn;
    }

    public boolean isPttOn() {
        return isPttOn;
    }
}
