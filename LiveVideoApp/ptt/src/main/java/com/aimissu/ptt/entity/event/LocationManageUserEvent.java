package com.aimissu.ptt.entity.event;

public class LocationManageUserEvent {
    /**
     * 1 成功
     * 0 失败
     */
    public int mStatus = 0;
    public LocationManageUserEvent(int status){
        mStatus=status;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }
}
