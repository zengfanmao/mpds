package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.ChatMsgList;

import java.util.List;

/**
 * 聊天列表
 */
public class DataMyChats extends BaseEntity {
    List<ChatMsgList> Entity;

    public List<ChatMsgList> getEntity() {
        return Entity;
    }

    @Override
    public String toString() {
        return "DataMyChats{" +
                "Entity=" + Entity +
                '}';
    }
}
