package com.aimissu.ptt.entity.sipMessage;

/**
 * 通话时候，发送的消息实体
 */
public class CallMessage extends BaseSipMessage{
    public String head;
    public  String msgtype;

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

    @Override
    public String toString() {
        return "CallMessage{" +
                "head='" + head + '\'' +
                ", msgtype='" + msgtype + '\'' +
                '}';
    }
}
