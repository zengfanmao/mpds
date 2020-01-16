package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;

/**

 */
public interface IReplayPresenter extends IBasePresenter {
    void getAudioMsg(String userCode, String msgFromType, String rName, String startTime, String endTime, int pageNum, int pageSize,boolean isLoadMore);
}
