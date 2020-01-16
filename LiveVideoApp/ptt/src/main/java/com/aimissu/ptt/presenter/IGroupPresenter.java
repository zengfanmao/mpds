package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.ptt.entity.ui.UserGroupRank;

/**

 */
public interface IGroupPresenter extends IBasePresenter {

    void getUserGroup();

    void getUserGroupRank();

    void getUserGroupByRank(long rank);

    /**
     * 根据APP组获取融合PDT组
     */
    void getPdtGroup(String disscusionCode);

    void switchGroup(String disscusionCode,String disscusionName);

    void getPermission();

    void setDefaultGroup(String disscusionCode, String isDefaultGroup);
}
