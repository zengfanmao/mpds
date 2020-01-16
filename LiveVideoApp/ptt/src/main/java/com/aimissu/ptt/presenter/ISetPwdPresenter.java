package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.IBasePresenter;

public interface ISetPwdPresenter extends IBasePresenter {
    void resetPwd(String oldPwd,String newPwd);
}
