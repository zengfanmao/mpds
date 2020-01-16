package com.aimissu.ptt.entity;

/**
 * @author：dz-hexiang on 2018/7/22.
 * @email：472482006@qq.com
 */
public class ReadMsgEvent {
    public String msgId;
    public String type;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ReadMsgEvent(String msgId, String type) {
        this.msgId = msgId;
        this.type = type;
    }
}
