package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataUserAction;
import com.aimissu.ptt.entity.event.LocationManageUserEvent;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.model.IPersonalModel;
import com.aimissu.ptt.model.PersonalModel;
import com.aimissu.ptt.view.IPersonalView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class PersonalPresenter extends BasePresenter<IPersonalView> implements IPersonalPresenter {

    IPersonalModel model;
    private String tag = "PersonalPresenter";

    public PersonalPresenter(IPersonalView baseView) {
        super(baseView);
        model = new PersonalModel();
    }

    @Override
    public void getMyChats() {
        model.getMyChats(new RxCallBack<DataMyChats>() {
            @Override
            public void onSucessed(final DataMyChats dataMyChats) {
                if (getView() != null) {
                    if (dataMyChats.isIsSuccess()) {
                        RxUtils.asyncRun(new Runnable() {
                            @Override
                            public void run() {
                                AppManager.setChatMsgList(dataMyChats.getEntity());
                            }
                        });
                        AppManager.setChatMsgList(dataMyChats.getEntity());
                        getView().getMyChatSuccessed(dataMyChats);
                    } else {
                        getView().getMyChatsFailed(dataMyChats.getMessage());
                    }

                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getMyChatsFailed(e.message);
                }
            }
        });
    }

    @Override
    public void searchUser(String disscusionCode, String dCode,String userName,int pageNum,int pageSize, boolean isLoadMore, boolean isOnline) {
        model.searchUser(disscusionCode, dCode, userName,pageNum, pageSize, isOnline, new RxCallBack<DataPersonUserEntity>() {
            @Override
            public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {

                if (dataPersonUserEntity.isIsSuccess()) {
                    RxUtils.asyncRun(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                try {
                                    if (isLoadMore) {
                                        AppManager.addPersonUserEntityList(dataPersonUserEntity.getEntity());
                                    }else {
                                        AppManager.setPersonUserList(dataPersonUserEntity.getEntity());
                                    }
                                    LogUtil.i(tag, "dataPersonUserEntity.getEntity(): " + dataPersonUserEntity.getEntity() != null ? dataPersonUserEntity.getEntity().toString() : "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                    if (getView() != null) {
                        getView().getPersonUserSuccessed(dataPersonUserEntity.getEntity(),isLoadMore);
                    }
                } else {
                    if (getView() != null) {
                        getView().getPersonUserFailed(dataPersonUserEntity.getMessage());
                    }
                }

            }


            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getPersonUserFailed(e.message);
                }
            }
        });
    }


    @Override
    public void manageUser(String msgToCode, String userAction) {
        model.manageUser(msgToCode, userAction, new RxCallBack<DataUserAction>() {
            @Override
            public void onSucessed(DataUserAction dataUserAction) {
                if (getView() != null) {
                    if (dataUserAction.isIsSuccess()) {
                        getView().getmanageUserSuccessed(dataUserAction);
                        EventBus.getDefault().post(new LocationManageUserEvent(1));

                    } else {
                        getView().getmanageUserFailed(dataUserAction.getMessage());
                        EventBus.getDefault().post(new LocationManageUserEvent(0));
                    }
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getmanageUserFailed(e.getMessage());
                }
            }
        });
    }

    @Override
    public void getUserGps(String userCode, String userName, String discussionCode, int pageNum, int pageSize) {
        model.getUserGps(userCode, userName, discussionCode, pageNum, pageSize, new RxCallBack<DataGps>() {
            @Override
            public void onSucessed(DataGps dataGps) {
                if (dataGps.isIsSuccess()) {
                    if (getView() != null)
                        getView().getUserGpsSuccessed(dataGps);
                } else {
                    if (getView() != null)
                        getView().getUserGpsFailed(dataGps.getMessage());
                }
                getView().hideProgressDialog();
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null)
                    getView().getUserGpsFailed(e.message);

                getView().hideProgressDialog();
            }
        });
    }
}
