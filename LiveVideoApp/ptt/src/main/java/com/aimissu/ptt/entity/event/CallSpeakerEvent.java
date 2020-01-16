package com.aimissu.ptt.entity.event;

public class CallSpeakerEvent {
    /**
     *  1 ; 通话中
     *  0 : 空闲中
     */

    public int statu  = 0;
    public String speakerName;
    public String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public CallSpeakerEvent(int statu, String speakerName, String userCode){
        this.statu=statu;
        this.speakerName=speakerName;
        this.userCode=userCode;
    }

    public CallSpeakerEvent(int statu){
        this.statu=statu;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }
}
