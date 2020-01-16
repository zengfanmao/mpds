package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataReplay;
import com.aimissu.ptt.model.IReplayModel;
import com.aimissu.ptt.model.ReplayModel;
import com.aimissu.ptt.view.IReplayView;

/**

 */
public class ReplayPresenter extends BasePresenter<IReplayView> implements IReplayPresenter {

    IReplayModel model;

    public ReplayPresenter(IReplayView baseView) {
        super(baseView);
        model = new ReplayModel();
    }


    @Override
    public void getAudioMsg(String userCode, String msgFromType, String rName, String startTime, String endTime, int pageNum, int pageSize,boolean isLoadMore) {
        model.getAudioMsg(userCode, msgFromType, rName, startTime, endTime, pageNum, pageSize, new RxCallBack<DataReplay>() {
            @Override
            public void onSucessed(DataReplay dataReplay) {
                if(dataReplay.isIsSuccess()){
                    if(getView()!=null)
                        getView().getAudioMsgSuccessed(dataReplay,isLoadMore);
                }else{
                    if(getView()!=null)
                        getView().getAudioMsgFailed(dataReplay.getMsg(),isLoadMore);
                }

            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if(getView()!=null)
                    getView().getAudioMsgFailed(e.message,isLoadMore);
            }
        });

    }
}
