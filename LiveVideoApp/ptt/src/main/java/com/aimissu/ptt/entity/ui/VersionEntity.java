package com.aimissu.ptt.entity.ui;

public class VersionEntity {
// "ID": 1,
//         "isDel": 0,
//         "recSN": null,
//         "appType": "android",
//         "appVersionNo": 1,
//         "appVersionName": "MPDS 1.0.0",
//         "appPublishTime": "2018-09-08T14:33:12+08:00",
//         "appFeatures": null,
//         "appTitle": "MPDS",
//         "appDownloadUrl": "/uploadfile/APP/MPDS 1.0.0.apk",
//         "createuser": null

    public String ID;
    public String isDel;
    public String recSN;
    public String appType;
    public int appVersionNo;
    public String appVersionName;
    public String appPublishTime;
    public String appFeatures;
    public String appTitle;
    public String appDownloadUrl;
    public String createuser;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getRecSN() {
        return recSN;
    }

    public void setRecSN(String recSN) {
        this.recSN = recSN;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public int getAppVersionNo() {
        return appVersionNo;
    }

    public void setAppVersionNo(int appVersionNo) {
        this.appVersionNo = appVersionNo;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppPublishTime() {
        return appPublishTime;
    }

    public void setAppPublishTime(String appPublishTime) {
        this.appPublishTime = appPublishTime;
    }

    public String getAppFeatures() {
        return appFeatures;
    }

    public void setAppFeatures(String appFeatures) {
        this.appFeatures = appFeatures;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    @Override
    public String toString() {
        return "VersionEntity{" +
                "ID='" + ID + '\'' +
                ", isDel='" + isDel + '\'' +
                ", recSN='" + recSN + '\'' +
                ", appType='" + appType + '\'' +
                ", appVersionNo='" + appVersionNo + '\'' +
                ", appVersionName='" + appVersionName + '\'' +
                ", appPublishTime='" + appPublishTime + '\'' +
                ", appFeatures='" + appFeatures + '\'' +
                ", appTitle='" + appTitle + '\'' +
                ", appDownloadUrl='" + appDownloadUrl + '\'' +
                ", createuser='" + createuser + '\'' +
                '}';
    }
}
