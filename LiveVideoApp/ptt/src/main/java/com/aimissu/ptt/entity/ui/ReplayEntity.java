package com.aimissu.ptt.entity.ui;

/**

 */
public class ReplayEntity {

    /**
     * RecordingUrl : /UploadFiles/Adagio.mp3
     * RecordingTime : 2018-09-03T19:02:38
     * AudioEndTime : null
     * AudioDescription : null
     * FileCode : 11111111111111
     * FileName : Adagio.mp3
     * sender : 陆警官
     * receiver : null
     * fromType : Person
     * dName : null
     * discussionName : null
     * second : 00:01:59
     * FileSize : null
     */

    private String RecordingUrl;
    private String RecordingTime;
    private String AudioEndTime;
    private String AudioDescription;
    private String FileCode;
    private String FileName;
    private String sender;
    private String receiver;
    private String fromType;
    private String dName;
    private String discussionName;
    private String second;
    private String FileSize;
    private String uuid;

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRecordingUrl() {
        return RecordingUrl;
    }

    public void setRecordingUrl(String RecordingUrl) {
        this.RecordingUrl = RecordingUrl;
    }

    public String getRecordingTime() {
        return RecordingTime;
    }

    public void setRecordingTime(String RecordingTime) {
        this.RecordingTime = RecordingTime;
    }

    public String getAudioEndTime() {
        return AudioEndTime;
    }

    public void setAudioEndTime(String AudioEndTime) {
        this.AudioEndTime = AudioEndTime;
    }

    public String getAudioDescription() {
        return AudioDescription;
    }

    public void setAudioDescription(String AudioDescription) {
        this.AudioDescription = AudioDescription;
    }

    public String getFileCode() {
        return FileCode;
    }

    public void setFileCode(String FileCode) {
        this.FileCode = FileCode;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    public String getDiscussionName() {
        return discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String FileSize) {
        this.FileSize = FileSize;
    }

    @Override
    public String toString() {
        return "ReplayEntity{" +
                "RecordingUrl='" + RecordingUrl + '\'' +
                ", RecordingTime='" + RecordingTime + '\'' +
                ", AudioEndTime='" + AudioEndTime + '\'' +
                ", AudioDescription='" + AudioDescription + '\'' +
                ", FileCode='" + FileCode + '\'' +
                ", FileName='" + FileName + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", fromType='" + fromType + '\'' +
                ", dName='" + dName + '\'' +
                ", discussionName='" + discussionName + '\'' +
                ", second='" + second + '\'' +
                ", FileSize='" + FileSize + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
