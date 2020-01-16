package com.aimissu.ptt.entity.event;

public class PersonalCallEvent {
    public String msgToCode;

    public PersonalCallEvent(String msgToCode){
        this.msgToCode = msgToCode;
    }

    public String getMsgToCode(){
        return msgToCode;
    }
}
