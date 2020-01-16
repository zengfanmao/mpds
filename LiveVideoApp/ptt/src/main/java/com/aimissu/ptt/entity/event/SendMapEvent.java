package com.aimissu.ptt.entity.event;


/**
 */
public class SendMapEvent extends BaseEvent {

    public String latitude;
    public String longitude;
    public String positionName;
    public String imgUrl;

    public SendMapEvent(String latitude, String longitude, String positionName, String imgUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.positionName = positionName;
        this.imgUrl = imgUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "SendMapEvent{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", positionName='" + positionName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
