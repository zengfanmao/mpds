package com.aimissu.ptt.view;

/**

 */
public interface IChatInputView {
    void hideSendView();

    void showSendView();

    void toogleMore();

    /**
     * 切换语音说话
     */
    void changeToVoice();

    void changeToText();
}
