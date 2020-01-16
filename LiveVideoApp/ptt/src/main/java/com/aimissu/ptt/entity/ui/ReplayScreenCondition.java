package com.aimissu.ptt.entity.ui;

import com.aimissu.basemvp.net.rx.LogUtil;

/**

 */
public class ReplayScreenCondition {
    public String rName;
    public String startTime;
    public String endTime;

    public int type = ReplayScreenCondition.TYPE_GROUP;
    public final static int TYPE_PERSONAL = 1;
    public final static int TYPE_GROUP = 2;


    public ReplayScreenCondition(String rName, String startTime, String endTime) {
        this.rName = rName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ReplayScreenCondition(String rName, String startTime, String endTime, int type) {
        this.rName = rName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReplayScreenCondition{" +
                "rName='" + rName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type=" + type +
                '}';
    }
}
