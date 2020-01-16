package com.aimissu.ptt.entity.sipMessage;

public class SipMessageToPdt extends BaseSipMessage{
    /**
     * Multicast：1为组呼，0为单呼
     * CalledSSI为需要把消息发送到PDT组的组呼号/单呼号
     */
    private String head;
    private String msgtype;
    private String msg;
    private String CalledSSI;
    private String Multicast;

    public String getMulticast() {
        return Multicast;
    }

    public void setMulticast(String multicast) {
        Multicast = multicast;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCalledSSI() {
        return CalledSSI;
    }

    public void setCalledSSI(String calledSSI) {
        CalledSSI = calledSSI;
    }
}
