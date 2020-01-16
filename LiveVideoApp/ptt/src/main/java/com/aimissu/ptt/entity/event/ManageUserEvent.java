package com.aimissu.ptt.entity.event;

public class ManageUserEvent {
    private String userAction;
    private String msgToCode;
    private String uName;

    public void setUserAction(String userAction) {
        this.userAction = userAction;

    }

    public String getMsgToCode() {
        return msgToCode;
    }

    public void setMsgToCode(String msgToCode) {
        this.msgToCode = msgToCode;
    }

    public ManageUserEvent(String action,String msgToCode,String uName){
        this.userAction = action;
        this.msgToCode = msgToCode;
        this.uName = uName;
    }

    public String getUserAction(){
        return userAction;
    }

    public String getuName() {
        return uName;
    }
}
