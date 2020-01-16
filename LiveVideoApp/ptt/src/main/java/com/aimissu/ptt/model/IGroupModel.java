package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.data.DataLogin;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataPermission;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.entity.ui.UserGroupRank;

/**

 */
public interface IGroupModel {

    void getUserGroupRank(RxCallBack<DataUserGroupRank> rxCallBack);

    void getUserGroup(RxCallBack<DataUserGroup> rxCallBack);

    void getUserGroupByRank(long rank, RxCallBack<DataUserGroupRank> rxCallBack);
    /**
     * 根据APP组获取融合PDT组
     */
    void getPdtGroup(String disscusionCode,RxCallBack<DataPdtGroup> rxCallBack);

    void switchGroup(String disscusionCode,String disscusionName,RxCallBack<DataLogin> rxCallBack );

    void getPermission(RxCallBack<DataPermission> rxCallBack);

    void setDefaultGroup(String disscusionCode,String isDefaultGroup,RxCallBack<DataLogin> rxCallBack);
}
