package com.aimissu.ptt.entity.im;

public class SpeakerControlMessage {
    public String IsPdt;
    public String CallerSSI;
    public String CalledSSI;
    public String EventName;
    public String ptt_type;

    public String getIsPdt() {
        return IsPdt;
    }

    public void setIsPdt(String isPdt) {
        IsPdt = isPdt;
    }

    public String getCallerSSI() {
        return CallerSSI;
    }

    public void setCallerSSI(String callerSSI) {
        CallerSSI = callerSSI;
    }

    public String getCalledSSI() {
        return CalledSSI;
    }

    public void setCalledSSI(String calledSSI) {
        CalledSSI = calledSSI;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getPtt_type() {
        return ptt_type;
    }

    public void setPtt_type(String ptt_type) {
        this.ptt_type = ptt_type;
    }

    @Override
    public String toString() {
        return "SpeakerControlMessage{" +
                "IsPdt='" + IsPdt + '\'' +
                ", CallerSSI='" + CallerSSI + '\'' +
                ", CalledSSI='" + CalledSSI + '\'' +
                ", EventName='" + EventName + '\'' +
                ", ptt_type='" + ptt_type + '\'' +
                '}';
    }
}
