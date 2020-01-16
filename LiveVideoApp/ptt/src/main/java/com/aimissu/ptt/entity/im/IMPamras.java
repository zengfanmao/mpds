package com.aimissu.ptt.entity.im;

/**
 */
public class IMPamras {

    public String localChatId;
    public String fileId;
    public IMType.UploadType uploadType;
    public String uploadFile;
    public IMType.MsgType msgType;
    public String fileCode;
    public String picCode;
    public String startTime;
    public String endTime;
    public String duration;
    public String msg;
    public String picUrl;
    public String latitude;
    public String longitude;
    public String positionName;
    public String msgToCode;
    public String loginName;

    public String msgFromType;
    public String userCode;
    public String msgContent;
    public String msgFile;

    public String filePath;
    public String virtualId;

    public IMType.MsgType FileType;

    private IMPamras(Builder builder) {
        localChatId = builder.localChatId;
        fileId = builder.fileId;
        uploadType = builder.uploadType;
        uploadFile = builder.uploadFile;
        msgType = builder.msgType;
        fileCode = builder.fileCode;
        picCode = builder.picCode;
        startTime = builder.startTime;
        endTime = builder.endTime;
        duration = builder.duration;
        msg = builder.msg;
        picUrl = builder.picUrl;
        latitude = builder.latitude;
        longitude = builder.longitude;
        positionName = builder.positionName;
        msgToCode = builder.msgToCode;
        loginName = builder.loginName;
        msgFromType = builder.msgFromType;
        userCode = builder.userCode;
        msgContent = builder.msgContent;
        msgFile = builder.msgFile;
        filePath = builder.filePath;
        virtualId = builder.virtualId;
        FileType = builder.FileType;
    }


    public static final class Builder {
        private String localChatId;
        private String fileId;
        private IMType.UploadType uploadType;
        private String uploadFile;
        private IMType.MsgType msgType;
        private String fileCode;
        private String picCode;
        private String startTime;
        private String endTime;
        private String duration;
        private String msg;
        private String picUrl;
        private String latitude;
        private String longitude;
        private String positionName;
        private String msgToCode;
        private String loginName;
        private String msgFromType;
        private String userCode;
        private String msgContent;
        private String msgFile;
        private String filePath;
        private String virtualId;
        public IMType.MsgType FileType;

        public Builder() {
        }

        public Builder FileType(IMType.MsgType val) {
            FileType = val;
            return this;
        }
        public Builder localChatId(String val) {
            localChatId = val;
            return this;
        }

        public Builder fileId(String val) {
            fileId = val;
            return this;
        }

        public Builder uploadType(IMType.UploadType val) {
            uploadType = val;
            return this;
        }

        public Builder uploadFile(String val) {
            uploadFile = val;
            return this;
        }

        public Builder msgType(IMType.MsgType val) {
            msgType = val;
            return this;
        }

        public Builder fileCode(String val) {
            fileCode = val;
            return this;
        }

        public Builder picCode(String val) {
            picCode = val;
            return this;
        }

        public Builder startTime(String val) {
            startTime = val;
            return this;
        }

        public Builder endTime(String val) {
            endTime = val;
            return this;
        }

        public Builder duration(String val) {
            duration = val;
            return this;
        }

        public Builder msg(String val) {
            msg = val;
            return this;
        }

        public Builder picUrl(String val) {
            picUrl = val;
            return this;
        }

        public Builder latitude(String val) {
            latitude = val;
            return this;
        }

        public Builder longitude(String val) {
            longitude = val;
            return this;
        }

        public Builder positionName(String val) {
            positionName = val;
            return this;
        }

        public Builder msgToCode(String val) {
            msgToCode = val;
            return this;
        }

        public Builder loginName(String val) {
            loginName = val;
            return this;
        }

        public Builder msgFromType(String val) {
            msgFromType = val;
            return this;
        }

        public Builder userCode(String val) {
            userCode = val;
            return this;
        }

        public Builder msgContent(String val) {
            msgContent = val;
            return this;
        }

        public Builder msgFile(String val) {
            msgFile = val;
            return this;
        }

        public Builder filePath(String val) {
            filePath = val;
            return this;
        }

        public Builder virtualId(String val) {
            virtualId = val;
            return this;
        }

        public IMPamras build() {
            return new IMPamras(this);
        }
    }

    public IMType.MsgType getFileType() {
        return FileType;
    }

    public void setFileType(IMType.MsgType fileType) {
        FileType = fileType;
    }

    public String getLocalChatId() {
        return localChatId;
    }

    public String getFileId() {
        return fileId;
    }

    public IMType.UploadType getUploadType() {
        return uploadType;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public IMType.MsgType getMsgType() {
        return msgType;
    }

    public String getFileCode() {
        return fileCode;
    }

    public String getPicCode() {
        return picCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getMsg() {
        return msg;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getMsgToCode() {
        return msgToCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMsgFromType() {
        return msgFromType;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public String getMsgFile() {
        return msgFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getVirtualId() {
        return virtualId;
    }

    public void setLocalChatId(String localChatId) {
        this.localChatId = localChatId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setUploadType(IMType.UploadType uploadType) {
        this.uploadType = uploadType;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public void setMsgType(IMType.MsgType msgType) {
        this.msgType = msgType;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setMsgToCode(String msgToCode) {
        this.msgToCode = msgToCode;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setMsgFromType(String msgFromType) {
        this.msgFromType = msgFromType;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public void setMsgFile(String msgFile) {
        this.msgFile = msgFile;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setVirtualId(String virtualId) {
        this.virtualId = virtualId;
    }

    @Override
    public String toString() {
        return "IMPamras{" +
                "localChatId='" + localChatId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", uploadType=" + uploadType +
                ", uploadFile='" + uploadFile + '\'' +
                ", msgType=" + msgType +
                ", fileCode='" + fileCode + '\'' +
                ", picCode='" + picCode + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", msg='" + msg + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", positionName='" + positionName + '\'' +
                ", msgToCode='" + msgToCode + '\'' +
                ", loginName='" + loginName + '\'' +
                ", msgFromType='" + msgFromType + '\'' +
                ", userCode='" + userCode + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", msgFile='" + msgFile + '\'' +
                ", filePath='" + filePath + '\'' +
                ", virtualId='" + virtualId + '\'' +
                '}';
    }
}
