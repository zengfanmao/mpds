package com.aimissu.ptt.entity.event;

public class WaitingClickEvent {
    private int mPosition = -1;
    public WaitingClickEvent(int position){
        mPosition=position;
    }

    public int getPosition(){
        return mPosition;
    }
}
