package com.aimissu.ptt.entity.ui;

public class AudioDetailContent {
    public String IsPdt;
    public String ptt_type;
    public String CalledSSI;
    public String CallerSSI;
    public String conference_Creator;
    public String conference_CreatorIsPdt;
    public String conference_name;
    public String conference_uuid;
    public String EventName;

    public String getIsPdt() {
        return IsPdt;
    }

    public void setIsPdt(String isPdt) {
        IsPdt = isPdt;
    }

    public String getPtt_type() {
        return ptt_type;
    }

    public void setPtt_type(String ptt_type) {
        this.ptt_type = ptt_type;
    }

    public String getCalledSSI() {
        return CalledSSI;
    }

    public void setCalledSSI(String calledSSI) {
        CalledSSI = calledSSI;
    }

    public String getCallerSSI() {
        return CallerSSI;
    }

    public void setCallerSSI(String callerSSI) {
        CallerSSI = callerSSI;
    }

    public String getConference_Creator() {
        return conference_Creator;
    }

    public void setConference_Creator(String conference_Creator) {
        this.conference_Creator = conference_Creator;
    }

    public String getConference_CreatorIsPdt() {
        return conference_CreatorIsPdt;
    }

    public void setConference_CreatorIsPdt(String conference_CreatorIsPdt) {
        this.conference_CreatorIsPdt = conference_CreatorIsPdt;
    }

    public String getConference_name() {
        return conference_name;
    }

    public void setConference_name(String conference_name) {
        this.conference_name = conference_name;
    }

    public String getConference_uuid() {
        return conference_uuid;
    }

    public void setConference_uuid(String conference_uuid) {
        this.conference_uuid = conference_uuid;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    @Override
    public String toString() {
        return "AudioDetailContent{" +
                "IsPdt='" + IsPdt + '\'' +
                ", ptt_type='" + ptt_type + '\'' +
                ", CalledSSI='" + CalledSSI + '\'' +
                ", CallerSSI='" + CallerSSI + '\'' +
                ", conference_Creator='" + conference_Creator + '\'' +
                ", conference_CreatorIsPdt='" + conference_CreatorIsPdt + '\'' +
                ", conference_name='" + conference_name + '\'' +
                ", conference_uuid='" + conference_uuid + '\'' +
                ", EventName='" + EventName + '\'' +
                '}';
    }
}
