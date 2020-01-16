package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.data.DataLogin;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataPermission;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.entity.ui.UserGroupRank;
import com.aimissu.ptt.model.GroupModel;
import com.aimissu.ptt.model.IGroupModel;
import com.aimissu.ptt.view.IGroupView;

/**
 */
public class GroupPresenter extends BasePresenter<IGroupView> implements IGroupPresenter {
    private String tag = GroupPresenter.class.getSimpleName();

    IGroupModel model;

    public GroupPresenter(IGroupView baseView) {
        super(baseView);
        model = new GroupModel();
    }


    @Override
    public void getUserGroupRank() {
        model.getUserGroupRank(new RxCallBack<DataUserGroupRank>() {
            @Override
            public void onSucessed(final DataUserGroupRank dataUserGroupRank) {
                if (dataUserGroupRank.isIsSuccess()) {
                    if (getView() != null) {
                        getView().getUserGroupRankSuccessed(dataUserGroupRank);

                    } else {

                    }
                } else {
                    if (getView() != null) {
                        getView().getUserGroupRankFailed(dataUserGroupRank.getMessage());
                    }
                }
            }


            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getUserGroupRankFailed(e.message);
                }
            }
        });
    }

    @Override
    public void getUserGroupByRank(long rank) {

        model.getUserGroupByRank(rank,new RxCallBack<DataUserGroupRank>() {
            @Override
            public void onSucessed(final DataUserGroupRank dataUserGroupRank) {
                if (dataUserGroupRank.isIsSuccess()) {
                    if (getView() != null) {
                        getView().getUserGroupByRankSuccessed(rank,dataUserGroupRank);
                    }
                } else {
                    if (getView() != null) {
                        getView().getUserGroupByRankFailed(dataUserGroupRank.getMessage());
                    }
                }
            }


            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getUserGroupByRankFailed(e.message);
                }
            }
        });
    }

    @Override
    public void getUserGroup() {
        model.getUserGroup(new RxCallBack<DataUserGroup>() {
            @Override
            public void onSucessed(final DataUserGroup dataUserGroup) {
                if (dataUserGroup.isIsSuccess()) {
//                        RxUtils.asyncRun(new Runnable() {
//                            @Override
//                            public void run() {
//                                AppManager.setUserGroupList(dataUserGroup.getEntity());
//                                LogUtil.i(tag, "dataMyChats.getEntity():" + dataUserGroup.getEntity());
//                            }
//                        });
//                    AppManager.setUserGroupList(dataUserGroup.getEntity());
                    if (getView() != null) {
                        getView().getUserGroupSuccessed(dataUserGroup);
                        LogUtil.i(tag,"dataUserGroup.getEntity(): "+dataUserGroup.getEntity().toString());
                    } else {
                    }
                } else {
                    if (getView() != null) {
                        getView().getUserGroupFailed(dataUserGroup.getMessage());
                    }
                }
            }


            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getUserGroupFailed(e.message);
                }
            }
        });
    }

    @Override
    public void getPdtGroup(String disscusionCode) {
        model.getPdtGroup(disscusionCode, new RxCallBack<DataPdtGroup>() {
            @Override
            public void onSucessed(DataPdtGroup dataPdtGroup) {
                if (getView() != null) {
                    if (dataPdtGroup.isIsSuccess()) {
                        RxUtils.asyncRun(new Runnable() {
                            @Override
                            public void run() {
                                AppManager.setPdtNumberWithDisscusionCode(disscusionCode, dataPdtGroup.getEntity());
                                LogUtil.i(tag, "获取到的组号       ：  group_" + dataPdtGroup.getEntity());
                            }
                        });
                        getView().getPdtGroupSuccessed(dataPdtGroup);
                    } else {
                        getView().getPdtGroupFailed(dataPdtGroup.getMessage());
                    }
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getPdtGroupFailed(e.message);
                }
            }
        });
    }


    @Override
    public void switchGroup(String disscusionCode, String disscusionName) {
        model.switchGroup(disscusionCode, disscusionName, new RxCallBack<DataLogin>() {
            @Override
            public void onSucessed(DataLogin dataLogin) {
                if (getView() != null) {
                    if (dataLogin.isIsSuccess()) {
                        RxUtils.asyncRun(new Runnable() {
                            @Override
                            public void run() {
                                UserEntity userEntity = AppManager.getUserData();
                                userEntity.setDiscussionCode(disscusionCode);
                                userEntity.setDiscussionName(disscusionName);
                                AppManager.updateLocalUserData(userEntity.toEntity());
                                LogUtil.i(tag, "getDiscussionCode :" + userEntity.getDiscussionCode());
                            }
                        });
                        getView().switchGroupSuccessed();
                    }


                } else {
                    getView().switchGroupFailed(dataLogin.getMessage());
                    LogUtil.i(tag, "onFailed: " + dataLogin.getMessage());
                }
            }


            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().switchGroupFailed(e.message);
                }
            }
        });
    }

    @Override
    public void getPermission() {
        model.getPermission(new RxCallBack<DataPermission>() {
            @Override
            public void onSucessed(DataPermission dataPermission) {
                if (getView() != null) {
                    if (dataPermission.isIsSuccess()) {
                        LocalCache.getInstance().setUserPermisson(dataPermission.getEntity());
                        getView().getPermissionSuccessed();
                    }


                } else {
                    getView().getPermissionFailed(dataPermission.getMessage());
                    LogUtil.i(tag, "onFailed: " + dataPermission.getMessage());
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getPermissionFailed(e.message);
                }
            }
        });
    }

    @Override
    public void setDefaultGroup(String disscusionCode, String isDefaultGroup) {
        model.setDefaultGroup(disscusionCode, isDefaultGroup, new RxCallBack<DataLogin>() {
            @Override
            public void onSucessed(DataLogin baseEntity) {
                if (getView() != null) {
                    if (baseEntity.isIsSuccess()) {

                        getView().setDefaultGroupSuccessed();
                        LogUtil.i(tag, "setDefaultGroup :" + disscusionCode + "\t" + isDefaultGroup);
                    }

                } else {
                    getView().switchGroupFailed(baseEntity.getMsg());

                }

            }
            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().setDefaultGroupFailed(e.message);
                }
            }
        });
    }
}
