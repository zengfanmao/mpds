package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataReplay;

/**

 */
public class ReplayModel implements  IReplayModel {
    private String tag="ReplayModel";
    @Override
    public void getAudioMsg(String userCode, String msgFromType, String rName, String startTime, String endTime, int pageNum, int pageSize, RxCallBack<DataReplay> rxCallBack) {
        LogUtil.i(tag,"UserCode:"+userCode+"    msgFromType:"+msgFromType+"    RName:"+rName+"    ApiToken:"+AppManager.getApiToken()
                +"    startTime:"+startTime+"    endTime:"+endTime+"    PageNum:"+pageNum+"    pageSize:"+pageSize);
        RetrofitClient.getInstance().postAsync(DataReplay.class,
                RxConfig.getMethodApiUrl("api/do/getAudioMsg"),
                RxMapBuild.created()
                        .put("UserCode", userCode)
                        .put("MsgFromType", msgFromType)
                        .put("RName", rName)
                        .put("ApiToken", AppManager.getApiToken())
                        .put("StartTime", startTime)
                        .put("EndTime", endTime)
                        .put("PageNum", String.valueOf(pageNum))
                        .put("PageSize", String.valueOf(pageSize))
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}
