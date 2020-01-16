package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataUserGroup;

public class ResetPwdModel implements IResetPwdModel{
    @Override
    public void resetPwd(String oldPwd, String newPwd, RxCallBack<DataPdtGroup> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPdtGroup.class,
                RxConfig.getMethodApiUrl("api/do/resetPwd"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("UserOldPwd", oldPwd)
                        .put("UserPwd", newPwd)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}
