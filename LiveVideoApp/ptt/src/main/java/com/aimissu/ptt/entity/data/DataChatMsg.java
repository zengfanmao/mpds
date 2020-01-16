package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.im.ChatMsg;

import java.util.List;

/**

 */
public class DataChatMsg extends BaseEntity {
    List<ChatMsg> Entity;


    public List<ChatMsg> getEntity() {
        return Entity;
    }

    @Override
    public String toString() {
        return "DataChatMsg{" +
                "Entity=" + Entity +
                '}';
    }
}
