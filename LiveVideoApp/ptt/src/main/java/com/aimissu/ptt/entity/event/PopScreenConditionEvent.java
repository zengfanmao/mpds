package com.aimissu.ptt.entity.event;

import com.aimissu.ptt.entity.ui.PopScreenCondition;

/**
 * @author：dz-hexiang on 2018/7/22.
 * @email：472482006@qq.com
 */
public class PopScreenConditionEvent {
    PopScreenCondition popScreenCondition;

    public PopScreenConditionEvent(PopScreenCondition popScreenCondition) {
        this.popScreenCondition = popScreenCondition;
    }

    public PopScreenCondition getPopScreenCondition() {
        return popScreenCondition;
    }

    public void setPopScreenCondition(PopScreenCondition popScreenCondition) {
        this.popScreenCondition = popScreenCondition;
    }
}
