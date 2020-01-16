package com.aimissu.ptt.entity.sipMessage;

public class SipConferenceClose extends BaseSipMessage{

    public String head;
    public String msgtype;
    public String callid;

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

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }
}
