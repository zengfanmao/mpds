package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.im.IMPamras;

/**

 */
public interface IChatView extends IBaseView {


    void pushMsgSuccessed(DataPushMsg dataPushMsg, IMPamras imPamras);

    void pushMsgFailed(String msg, IMPamras imPamras);

    void getMyChatDetailsSuccessed(boolean isLoadMore, DataChatMsg model);

    void getMyChatDetailsFailed(boolean isLoadMore, String msg);

    void uploadFileSuccessed(DataPostFile dataPostFile, IMPamras imPamras);

    void uploadFileFailed(String msg, IMPamras imPamras);


    void updateFilePropertiySuccessed(DataPostFile dataPostFile, IMPamras imPamras);

    void updateFilePropertiyFailed(String msg, IMPamras imPamras);

}
