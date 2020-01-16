package com.aimissu.ptt.entity.ui;

/**

 */
public class PopScreenCondition {
    public String dCode;
    public String discussionCode;
    public String requestCode;
    public String dName;
    public String discussionName;

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getDiscussionName() {
        return discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public PopScreenCondition(String dCode, String discussionCode) {
        this.dCode = dCode;
        this.discussionCode = discussionCode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getDiscussionCode() {
        return discussionCode;
    }

    public void setDiscussionCode(String discussionCode) {
        this.discussionCode = discussionCode;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public PopScreenCondition(String dCode, String discussionCode, String requestCode) {
        this.dCode = dCode;
        this.discussionCode = discussionCode;
        this.requestCode = requestCode;
    }

    @Override
    public String toString() {
        return "PopScreenCondition{" +
                "dCode='" + dCode + '\'' +
                ", discussionCode='" + discussionCode + '\'' +
                ", requestCode='" + requestCode + '\'' +
                ", dName='" + dName + '\'' +
                ", discussionName='" + discussionName + '\'' +
                '}';
    }
}
