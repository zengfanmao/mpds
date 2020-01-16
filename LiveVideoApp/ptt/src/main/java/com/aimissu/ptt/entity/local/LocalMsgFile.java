package com.aimissu.ptt.entity.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 */
@Entity(indexes = {@Index(value = "id DESC")})
public class LocalMsgFile {
    @Id
    public Long id;

    public String msgId;

    public String localFileUrl;

    public String fileDownloadUrll;

    public String duration;

    public String msgType;

    public String fileType;

    public String fCode;

    @Generated(hash = 111010231)
    public LocalMsgFile(Long id, String msgId, String localFileUrl,
                        String fileDownloadUrll, String duration, String msgType,
                        String fileType, String fCode) {
        this.id = id;
        this.msgId = msgId;
        this.localFileUrl = localFileUrl;
        this.fileDownloadUrll = fileDownloadUrll;
        this.duration = duration;
        this.msgType = msgType;
        this.fileType = fileType;
        this.fCode = fCode;
    }

    @Generated(hash = 1454982914)
    public LocalMsgFile() {
    }

    private LocalMsgFile(Builder builder) {
        setMsgId(builder.msgId);
        setLocalFileUrl(builder.localFileUrl);
        setFileDownloadUrll(builder.fileDownloadUrll);
        setDuration(builder.duration);
        setMsgType(builder.msgType);
        setFileType(builder.fileType);
        fCode = builder.fCode;
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

    public String getLocalFileUrl() {
        return this.localFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }

    public String getFileDownloadUrll() {
        return this.fileDownloadUrll;
    }

    public void setFileDownloadUrll(String fileDownloadUrll) {
        this.fileDownloadUrll = fileDownloadUrll;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFCode() {
        return this.fCode;
    }

    public void setFCode(String fCode) {
        this.fCode = fCode;
    }


    public static final class Builder {
        private String msgId;
        private String localFileUrl;
        private String fileDownloadUrll;
        private String duration;
        private String msgType;
        private String fileType;
        private String fCode;

        public Builder() {
        }

        public Builder msgId(String val) {
            msgId = val;
            return this;
        }

        public Builder localFileUrl(String val) {
            localFileUrl = val;
            return this;
        }

        public Builder fileDownloadUrll(String val) {
            fileDownloadUrll = val;
            return this;
        }

        public Builder duration(String val) {
            duration = val;
            return this;
        }

        public Builder msgType(String val) {
            msgType = val;
            return this;
        }

        public Builder fileType(String val) {
            fileType = val;
            return this;
        }

        public Builder fCode(String val) {
            fCode = val;
            return this;
        }

        public LocalMsgFile build() {
            return new LocalMsgFile(this);
        }
    }

    @Override
    public String toString() {
        return "LocalMsgFile{" +
                "id=" + id +
                ", msgId='" + msgId + '\'' +
                ", localFileUrl='" + localFileUrl + '\'' +
                ", fileDownloadUrll='" + fileDownloadUrll + '\'' +
                ", duration='" + duration + '\'' +
                ", msgType='" + msgType + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fCode='" + fCode + '\'' +
                '}';
    }
}
