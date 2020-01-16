package com.aimissu.ptt.entity.event;

public class ReplayAudioDetailsEvent {
    public String uuid;

    public ReplayAudioDetailsEvent(String uuid){
        this.uuid=uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
