package com.aimissu.ptt.entity.event;


import android.os.Bundle;

/**
 */
public class ChangePageEvent extends BaseEvent {
    private int tabPosition;
    private int tabID;
    private boolean hideTargetChild = true;
    private Bundle bundle;

    public ChangePageEvent(int tabPosition, int tabID) {
        this.tabPosition = tabPosition;
        this.tabID = tabID;
    }

    public ChangePageEvent(int tabPosition, int tabID, boolean hideTargetChild) {
        this.tabPosition = tabPosition;
        this.tabID = tabID;
        this.hideTargetChild = hideTargetChild;
    }

    public ChangePageEvent(int tabPosition, int tabID, boolean hideTargetChild, Bundle bundle) {
        this.tabPosition = tabPosition;
        this.tabID = tabID;
        this.hideTargetChild = hideTargetChild;
        this.bundle = bundle;
    }

    public ChangePageEvent(int tabID) {
        this.tabID = tabID;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public int getTabID() {
        return tabID;
    }

    public boolean isHideTargetChild() {
        return hideTargetChild;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
