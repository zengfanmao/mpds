package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.ui.UserGroupRank;

/**

 */
public interface IGroupView extends IBaseView {

    void getUserGroupRankSuccessed(DataUserGroupRank model);

    void getUserGroupRankFailed(String msg);

    void getUserGroupByRankSuccessed(long rank, DataUserGroupRank model);

    void getUserGroupByRankFailed(String msg);

    void getUserGroupSuccessed(DataUserGroup model);

    void getUserGroupFailed(String msg);

    void getPdtGroupSuccessed(DataPdtGroup model);

    void getPdtGroupFailed(String msg);

    void switchGroupSuccessed();

    void switchGroupFailed(String msg);

    void getPermissionSuccessed();

    void getPermissionFailed(String msg);

    void setDefaultGroupSuccessed();

    void setDefaultGroupFailed(String msg);
}
