package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataReplay;

/**

 */
public interface IReplayView extends IBaseView {

    void getAudioMsgSuccessed(DataReplay dataReplay,boolean isLoadMore);
    void getAudioMsgFailed(String msg,boolean isLoadMore);
}
