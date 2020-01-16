package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataLogin;

/**

 */
public interface ILoginModel {
    void login(String loginName, String pwd, String server, RxCallBack<DataLogin> rxCallBack);

}
