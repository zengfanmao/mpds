package com.aimissu.ptt.entity.im;

/**
 */
public class BaseMsg {
    /**基类消息通知
     * MsgType : Text
     * MsgCode : 1
     * MsgContent :
     * MsgErr :
     */

    private String MsgType;
    private String MsgCode;
    private String MsgContent;
    private String MsgErr;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    public String getMsgCode() {
        return MsgCode;
    }

    public void setMsgCode(String MsgCode) {
        this.MsgCode = MsgCode;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }

    public String getMsgErr() {
        return MsgErr;
    }

    public void setMsgErr(String MsgErr) {
        this.MsgErr = MsgErr;
    }
}
