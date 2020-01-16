package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataUserAction;

/**

 */
public interface IPersonalModel {
    void getMyChats(RxCallBack<DataMyChats> rxCallBack);

    void searchUser(String disscusionCode,String dCode,String userName,int pageNum,int pageSize,boolean isOnline, RxCallBack<DataPersonUserEntity> rxCallBack);

    void manageUser(String msgToCode,String userAction,RxCallBack<DataUserAction> rxCallBack);

    void getUserGps(String userCode,String userName,String discussionCode,int pageNum,int pageSize, RxCallBack<DataGps> rxCallBack);

}
