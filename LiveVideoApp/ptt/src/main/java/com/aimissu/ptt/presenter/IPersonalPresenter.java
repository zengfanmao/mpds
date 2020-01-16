package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;

import java.util.List;

/**

 */
public interface IPersonalPresenter extends IBasePresenter {

    void getMyChats();

    void searchUser(String disscusionCode, String dCode,String userName,int pageNum,int pageSize, boolean isLoadMore, boolean isOnline);

    void manageUser(String msgToCode,String userAction);

    void getUserGps(String userCode, String dCode, String discussionCode, int pageNum, int pageSize);

}
