package com.aimissu.ptt.view;

import com.aimissu.basemvp.base.BaseJsonData;
import com.aimissu.basemvp.mvp.IBaseView;

/**

 */
public interface ILoginView extends IBaseView {

    void onLinPhoneServiceReady();

    void loginSuccessed(BaseJsonData model);

    void loginFailed(String msg);
}
