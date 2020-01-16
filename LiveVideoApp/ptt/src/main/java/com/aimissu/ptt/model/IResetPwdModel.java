package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataPdtGroup;

public interface IResetPwdModel {
    void resetPwd(String oldPwd,String newPwd,RxCallBack<DataPdtGroup> rxCallBack);
}
