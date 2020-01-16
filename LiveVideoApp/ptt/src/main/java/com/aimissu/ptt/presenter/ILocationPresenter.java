package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;

/**

 */
public interface ILocationPresenter extends IBasePresenter {
    void getUserGps(String userCode, String dCode, String discussionCode, int pageNum, int pageSize);
}
