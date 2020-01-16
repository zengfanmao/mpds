package com.aimissu.ptt.presenter;

import android.content.Context;
import android.content.Intent;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataLogin;
import com.aimissu.ptt.model.ILoginModel;
import com.aimissu.ptt.model.LoginModel;
import com.aimissu.ptt.service.CoreService;
import com.aimissu.ptt.utils.LoginSharedPreferences;
import com.aimissu.ptt.view.ILoginView;
import com.grgbanking.video.VideoCore;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 */
public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter {

    private String tag = "LoginPresenter";
    ILoginModel model;

    public  LoginPresenter(ILoginView baseView) {
        super(baseView);
        model = new LoginModel();
    }

    private Disposable disposableLinPhone;
    //最长等待1分钟
    private long INIT_LIN_PHONE_MAX_WAIT_TIME = 60 * 1000;//ms
    private long INIT_LIN_PHONE_MAX_INTERVAL_TIME = 100;//ms

    @Override
    public void initLinPhone(Context context) {
        if (getView() != null) {
            getView().showProgressDialog();
            if (VideoCore.getInstance() != null) {
                getView().onLinPhoneServiceReady();
            } else {
                context.startService(new Intent(context, CoreService.class));
                disposableLinPhone = Flowable.intervalRange(0, INIT_LIN_PHONE_MAX_WAIT_TIME / INIT_LIN_PHONE_MAX_INTERVAL_TIME + 1, 0, INIT_LIN_PHONE_MAX_INTERVAL_TIME, TimeUnit.MILLISECONDS)
                        .map(new Function<Long, Long>() {
                            @Override
                            public Long apply(Long aLong) throws Exception {
                                return INIT_LIN_PHONE_MAX_WAIT_TIME - aLong * 100;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                if (aLong <= 0) {
                                    getView().hideProgressDialog();
                                    stopInitLinPhone();
                                }
                                if (VideoCore.getInstance() != null) {
                                    getView().hideProgressDialog();
                                    getView().onLinPhoneServiceReady();
                                    stopInitLinPhone();
                                }
                            }
                        });
            }
        }

    }

    @Override
    public void stopInitLinPhone() {
        if (disposableLinPhone != null && !disposableLinPhone.isDisposed())
            disposableLinPhone.dispose();
    }

    @Override
    public void login(String loginName, String pwd, String server) {
        //todo 登陆逻辑
        model.login(loginName,pwd, server, new RxCallBack<DataLogin>() {
            @Override
            public void onSucessed(final DataLogin dataLoginEntitiy) {
                if (getView() != null) {
                    if (dataLoginEntitiy.isIsSuccess()) {
                        if (dataLoginEntitiy.getEntity() != null) {
                            RxUtils.asyncRun(new Runnable() {
                                @Override
                                public void run() {
                                    AppManager.clearUser();
                                    AppManager.setLocalUserData(dataLoginEntitiy.getEntity().toEntity());
                                    LogUtil.e("MqttService", "loginName:" + AppManager.getLoginName());
                                    LogUtil.i(tag, "dataLoginEntitiy.getEntity() :" + dataLoginEntitiy.getEntity().toString());
                                }
                            });
                        }
                        getView().loginSuccessed(dataLoginEntitiy);

                    } else {
                        getView().loginFailed(loginName+"  server:"+server+"    "+dataLoginEntitiy.getMessage());
                        LogUtil.i(tag,"onFailed: "+dataLoginEntitiy.getMessage());
                    }
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().loginFailed(loginName+" "+"  server:"+server+"    "+e.message);

                }
                LogUtil.i(tag,"onFailed......... ");

            }
        });


    }

    @Override
    public void unBind() {
        super.unBind();
        stopInitLinPhone();
    }
}
