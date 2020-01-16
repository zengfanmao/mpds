package com.aimissu.ptt.utils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.im.IMType;

/**
 * 网络请求工具
 */
public class webRequestUtil {

    /**
     * 邀请其他人加入会议
     * @param disscusionCode
     * @param rxCallBack
     */
    public static void joinOthersInConference(String UserCode,String MsgContent,String disscusionCode,RxCallBack<DataPdtGroup> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPdtGroup.class,
                RxConfig.getMethodApiUrl("api/do/pushCommand"),
                RxMapBuild.created()
                        .put("UserCode", UserCode)
                        .put("MsgType", IMType.MsgType.Command.toString())
                        .put("MsgContent", MsgContent)
                        .put("MsgFromType", "Group")
                        .put("DisscusionCode", disscusionCode)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    /**
     * 退出
     * @param rxCallBack
     */
    public static void logOut(RxCallBack<DataPdtGroup> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPdtGroup.class,
                RxConfig.getMethodApiUrl("api/do/userLogout"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getUserCode())
                        .put("ApiToken", AppManager.getApiToken())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}

