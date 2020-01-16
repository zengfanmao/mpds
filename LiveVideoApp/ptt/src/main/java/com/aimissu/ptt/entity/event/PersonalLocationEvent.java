package com.aimissu.ptt.entity.event;

public class PersonalLocationEvent {
    String userCode;

    public PersonalLocationEvent (String ucode){
        this.userCode=ucode;
    }

    public String getUserCode(){
        return userCode;
    }
}
