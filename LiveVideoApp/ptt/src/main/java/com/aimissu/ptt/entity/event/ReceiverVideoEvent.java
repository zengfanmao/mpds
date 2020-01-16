package com.aimissu.ptt.entity.event;


/**
 */
public class ReceiverVideoEvent extends BaseEvent {
    public String videoUri;
    public String videoScreenshot;

    public ReceiverVideoEvent(String videoUri, String videoScreenshot) {
        this.videoUri = videoUri;
        this.videoScreenshot = videoScreenshot;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getVideoScreenshot() {
        return videoScreenshot;
    }
}
