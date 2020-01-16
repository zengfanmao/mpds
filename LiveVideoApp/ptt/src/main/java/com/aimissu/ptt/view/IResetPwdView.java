package com.aimissu.ptt.view;

import com.aimissu.basemvp.mvp.IBaseView;
import com.aimissu.ptt.entity.data.DataPdtGroup;


public interface IResetPwdView extends IBaseView {
    void getResetPwdSuccessed(DataPdtGroup mode);
    void getUserGroupFailed(String msg);
}
