package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.ptt.entity.im.IMPamras;

/**

 */
public interface IChatPresenter extends IBasePresenter {

    void getMyChatDetails(boolean isLoadMore,String skip, String take, String firstMsgId, String msgFromType, String msgToCode, String msgFromUserCode);
    void pushMsg(IMPamras imPamras);
    void uploadFile(IMPamras imPamras);
    void updateFilePropertiy(IMPamras imPamras);
}
