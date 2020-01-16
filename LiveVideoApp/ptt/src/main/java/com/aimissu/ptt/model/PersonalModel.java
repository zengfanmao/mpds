package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataUserAction;
import com.aimissu.ptt.entity.im.IMType;

/**

 */
public class PersonalModel implements  IPersonalModel {
    private String tag="PersonalModel";
    @Override
    public void getMyChats( RxCallBack<DataMyChats> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataMyChats.class,
                RxConfig.getMethodApiUrl("api/do/getMyChatLists"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("MsgFromType", IMType.MsgFromType.Person.toString())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void searchUser(String dsscusionCode, String dCode,String userName,int pageNum,int pageSize, boolean isOnline, RxCallBack<DataPersonUserEntity> rxCallBack) {
        LogUtil.i(tag,"DisscusionCode:"+dsscusionCode+" dCode："+dCode+"userName:"+userName+"  AppManager.getApiToken():"+AppManager.getApiToken()+"  UserCode:"+AppManager.getLoginName());
        if (isOnline) {
            RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                    RxConfig.getMethodApiUrl("api/do/searchUser"),
                    RxMapBuild.created()
                            .put("UserCode", AppManager.getLoginName())
                            .put("ApiToken", AppManager.getApiToken())
                            .put("DisscusionCode", dsscusionCode)
                            .put("dCode", dCode)
                            .put("UserName",userName)
                            .put("UserStatus","在线")
                            .put("PageNum",String.valueOf(pageNum))
                            .put("PageSize",String.valueOf(pageSize))
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
        }else {
            RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                    RxConfig.getMethodApiUrl("api/do/searchUser"),
                    RxMapBuild.created()
                            .put("UserCode", AppManager.getLoginName())
                            .put("ApiToken", AppManager.getApiToken())
                            .put("DisscusionCode", dsscusionCode)
                            .put("dCode", dCode)
                            .put("UserName",userName)
                            .put("PageNum",String.valueOf(pageNum))
                            .put("PageSize",String.valueOf(pageSize))
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
        }

    }

    @Override
    public void manageUser(String msgToCode,String userAction,RxCallBack<DataUserAction> rxCallBack) {
        LogUtil.i(tag,"msgToCode:"+msgToCode+"userAction:"+userAction);
        RetrofitClient.getInstance().postAsync(DataUserAction.class,
                RxConfig.getMethodApiUrl("api/do/mgntUser"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("MsgToCode", msgToCode)
                        .put("userAction", userAction)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }


//    @Override
//    public void getUserGps(String userCode, String dCode, String discussionCode, int pageNum, int pageSize, RxCallBack<DataGps> rxCallBack) {
//        RetrofitClient.getInstance().postAsync(DataGps.class,
//                RxConfig.getMethodApiUrl("api/do/getUserGps"),
//                RxMapBuild.created()
//                        .put("UserCode", AppManager.getLoginName())
//                        .put("ApiToken", AppManager.getApiToken())
//                        .put("dCode", dCode)
//                        .put("DisscusionCode", discussionCode)
//                        .put("PageNum", String.valueOf(pageNum))
//                        .put("PageSize", String.valueOf(pageSize))
//                        .build()
//        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
//    }


    @Override
    public void getUserGps(String userCode, String userName, String discussionCode, int pageNum, int pageSize, RxCallBack<DataGps> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataGps.class,
                RxConfig.getMethodApiUrl("api/do/getUserGps"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("UserName", userName)
                        .put("DisscusionCode", discussionCode)
                        .put("PageNum", String.valueOf(pageNum))
                        .put("PageSize", String.valueOf(pageSize))
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }
}
