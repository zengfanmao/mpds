package com.aimissu.ptt.presenter;

import android.content.Context;

import com.aimissu.basemvp.mvp.IBasePresenter;

/**

 */
public interface ILoginPresenter extends IBasePresenter {

    void stopInitLinPhone();

    /**
     * 初始化linphone
     * @param context
     */
    void initLinPhone(Context context);

    void login(String loginName,String pwd,String server);

}
