package com.aimissu.ptt.entity.im;

/**
 * 接收到消息
 */
public class ReceieveMsgEvent {
    private  ChatMsg chatMsg;

    public ReceieveMsgEvent(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }

    public ChatMsg getChatMsg() {
        return chatMsg;
    }
}
