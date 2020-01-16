package com.aimissu.ptt.entity.event;

import com.aimissu.ptt.entity.ui.ReplayScreenCondition;

/**

 */
public class ReplayScreenEvent {
    ReplayScreenCondition condition;

    public ReplayScreenCondition getCondition() {
        return condition;
    }

    public void setCondition(ReplayScreenCondition condition) {
        this.condition = condition;
    }

    public ReplayScreenEvent(ReplayScreenCondition condition) {
        this.condition = condition;
    }
}
