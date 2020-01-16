package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataUserAction;
import com.aimissu.ptt.entity.ui.PersonUserEntity;

import java.util.List;

/**

 */
public interface IPersonalView extends IBaseView {

    void getMyChatSuccessed(DataMyChats model);

    void getMyChatsFailed(String msg);

    void getPersonUserSuccessed(List<PersonUserEntity> personUserEntityList, boolean isLoadMore);

    void getPersonUserFailed(String msg);

    void getmanageUserFailed(String mode);

    void getmanageUserSuccessed(DataUserAction msg);

    void  getUserGpsSuccessed(DataGps dataGps);
    void  getUserGpsFailed(String msg);

}
