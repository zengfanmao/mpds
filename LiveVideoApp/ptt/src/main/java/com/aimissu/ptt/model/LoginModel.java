package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.entity.data.DataLogin;

/**

 */
public class LoginModel implements ILoginModel {

    @Override
    public void login(String loginName, String pwd, String server, RxCallBack<DataLogin> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataLogin.class,
                RxConfig.getMethodApiUrl("api/do/userLogin"),
                RxMapBuild.created()
                        .put("LoginName", loginName)
                        .put("UserPwd", pwd)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}


