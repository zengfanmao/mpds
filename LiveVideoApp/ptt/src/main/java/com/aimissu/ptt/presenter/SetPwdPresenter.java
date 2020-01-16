package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.model.IResetPwdModel;
import com.aimissu.ptt.model.ResetPwdModel;
import com.aimissu.ptt.view.IResetPwdView;

public class SetPwdPresenter extends BasePresenter<IResetPwdView> implements ISetPwdPresenter{

    IResetPwdModel model;
    public SetPwdPresenter(IResetPwdView baseView) {
        super(baseView);
        model = new ResetPwdModel();
    }
    @Override
    public void resetPwd(String oldPwd, String newPwd) {
        model.resetPwd(oldPwd, newPwd, new RxCallBack<DataPdtGroup>() {
            @Override
            public void onSucessed(DataPdtGroup dataPdtGroup) {
                if (getView()!=null){
                    if (dataPdtGroup.isIsSuccess()){
                        getView().getResetPwdSuccessed(dataPdtGroup);
                    }else {
                        getView().getUserGroupFailed(dataPdtGroup.getMessage());
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
}
