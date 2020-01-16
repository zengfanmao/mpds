package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataGps;

/**

 */
public interface ILocationModel {
    void getUserGps(String userCode,String dCode,String discussionCode,int pageNum,int pageSize, RxCallBack<DataGps> rxCallBack);
}
