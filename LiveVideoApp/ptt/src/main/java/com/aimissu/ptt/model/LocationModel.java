package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataGps;

/**

 */
public class LocationModel implements  ILocationModel {
    @Override
    public void getUserGps(String userCode, String dCode, String discussionCode, int pageNum, int pageSize, RxCallBack<DataGps> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataGps.class,
                RxConfig.getMethodApiUrl("api/do/getUserGps"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("dCode", dCode)
                        .put("DisscusionCode", discussionCode)
                        .put("PageNum", String.valueOf(pageNum))
                        .put("PageSize", String.valueOf(pageSize))
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}
