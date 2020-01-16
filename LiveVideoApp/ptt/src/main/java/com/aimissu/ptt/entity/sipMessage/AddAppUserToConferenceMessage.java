package com.aimissu.ptt.entity.sipMessage;

/**
 * 添加app用户进入会议的消息
 */
public class AddAppUserToConferenceMessage extends BaseSipMessage{
//    private sring"head": "PDTMSG",
//            "msgtype": "Conference_Add",
//            "callid": "1001"

    private String head;
    private String msgtype;
    private String callid;

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
