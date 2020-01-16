package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataUserAction;

/**

 */
public interface ILocationView extends IBaseView {

    /**
     * 筛选容器切换
     */
    void toggleScreenContainer();

    void  getUserGpsSuccessed(DataGps dataGps);
    void  getUserGpsFailed(String msg);

}
