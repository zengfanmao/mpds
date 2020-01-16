package com.aimissu.ptt.entity.sipMessage;

public class BaseSipMessage {

    //主叫者设备id
    public String CallerSSI;

    public String getCallerSSI() {
        return CallerSSI;
    }

    public void setCallerSSI(String callerSSI) {
        CallerSSI = callerSSI;
    }

    @Override
    public String toString() {
        return "BaseSipMessage{" +
                "CallerSSI='" + CallerSSI + '\'' +
                '}';
    }
}
