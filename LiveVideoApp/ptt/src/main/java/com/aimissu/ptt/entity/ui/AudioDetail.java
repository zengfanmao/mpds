package com.aimissu.ptt.entity.ui;

public class AudioDetail {

    public String conference_uuid;
    public String conference_name;
    public String startor;
    public String listener;
    public String event_name;
    public String event_time;
    public String content;

    public String getConference_uuid() {
        return conference_uuid;
    }

    public void setConference_uuid(String conference_uuid) {
        this.conference_uuid = conference_uuid;
    }

    public String getConference_name() {
        return conference_name;
    }

    public void setConference_name(String conference_name) {
        this.conference_name = conference_name;
    }

    public String getStartor() {
        return startor;
    }

    public void setStartor(String startor) {
        this.startor = startor;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AudioDetail{" +
                "conference_uuid='" + conference_uuid + '\'' +
                ", conference_name='" + conference_name + '\'' +
                ", startor='" + startor + '\'' +
                ", listener='" + listener + '\'' +
                ", event_name='" + event_name + '\'' +
                ", event_time='" + event_time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
