package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.data.DataLogin;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataPermission;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.entity.ui.UserGroupRank;

/**
 */
public class GroupModel implements  IGroupModel {


    @Override
    public void getUserGroupRank( RxCallBack<DataUserGroupRank> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataUserGroupRank.class,
                RxConfig.getMethodApiUrl("api/do/getUserGroup"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
//                        .put("MsgFromType", IMType.MsgFromType.Group.toString())
                        .put("IsDefault","0")
                        .put("Isrank","1")
                        .put("PageNum","0")
                        .put("PageSize","100")
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }


    @Override
    public void getUserGroup( RxCallBack<DataUserGroup> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataUserGroup.class,
                RxConfig.getMethodApiUrl("api/do/getUserGroup"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
//                        .put("MsgFromType", IMType.MsgFromType.Group.toString())
                        .put("IsDefault","1")
                        .put("PageNum","0")
                        .put("PageSize","100")
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void getUserGroupByRank(long rank, RxCallBack<DataUserGroupRank> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataUserGroupRank.class,
                RxConfig.getMethodApiUrl("api/do/getUserGroup"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
//                        .put("MsgFromType", IMType.MsgFromType.Group.toString())
                        .put("IsDefault","0")
                        .put("Isrank","0")
                        .put("RankNo",String.valueOf(rank))
                        .put("PageNum","0")
                        .put("PageSize","100")
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void getPdtGroup(String disscusionCode,RxCallBack<DataPdtGroup> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPdtGroup.class,
                RxConfig.getMethodApiUrl("api/do/getPdtGroup"),
                RxMapBuild.created()
                        .put("DisscusionCode", disscusionCode)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void switchGroup(String disscusionCode, String disscusionName, RxCallBack<DataLogin> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataLogin.class,
                RxConfig.getMethodApiUrl("api/do/switchGroup"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("DisscusionCode", disscusionCode)
                        .put("DisscusionName",disscusionName)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void getPermission(RxCallBack<DataPermission> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPermission.class,
                RxConfig.getMethodApiUrl("api/do/getPermission"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .put("RName", AppManager.getUserData().getRoleName())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void setDefaultGroup(String disscusionCode, String isDefaultGroup, RxCallBack<DataLogin> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataLogin.class,
                RxConfig.getMethodApiUrl("api/do/setDefaultGroup"),
                RxMapBuild.created()
                        .put("UserCode", AppManager.getLoginName())
                        .put("DisscusionCode", disscusionCode)
                        .put("Isdefault",isDefaultGroup)
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }


}
