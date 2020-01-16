package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.im.ChatMsg;

/**
 * 聊天推送
 */
public class DataPushMsg extends BaseEntity {
    ChatMsg Entity;

    public ChatMsg getEntity() {
        return Entity;
    }
}
