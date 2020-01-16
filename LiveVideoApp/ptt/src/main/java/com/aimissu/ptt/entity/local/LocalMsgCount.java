package com.aimissu.ptt.entity.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 */
@Entity(indexes = {@Index(value = "id DESC")})
public class LocalMsgCount {
    @Id
    public Long id;
    public String msgId;
    public String msgCount;
    public String userCode;
    public String isDefault;

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String readCount;
    public String type;

    @Generated(hash = 2145897269)
    public LocalMsgCount(Long id, String msgId, String msgCount, String userCode,
            String isDefault, String readCount, String type) {
        this.id = id;
        this.msgId = msgId;
        this.msgCount = msgCount;
        this.userCode = userCode;
        this.isDefault = isDefault;
        this.readCount = readCount;
        this.type = type;
    }

    @Generated(hash = 421339738)
    public LocalMsgCount() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgCount() {
        return this.msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
