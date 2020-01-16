package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.model.ILocationModel;
import com.aimissu.ptt.model.LocationModel;
import com.aimissu.ptt.view.ILocationView;

/**

 */
public class LocationPresenter extends BasePresenter<ILocationView> implements ILocationPresenter {

    ILocationModel model;
    private String tag="LocationPresenter";
    public LocationPresenter(ILocationView baseView) {
        super(baseView);
        model=new LocationModel();
    }


    @Override
    public void getUserGps(String userCode, String dCode, String discussionCode, int pageNum, int pageSize) {
        LogUtil.i(tag,"dCode:   "+dCode +"  discussionCode: "+discussionCode);
            model.getUserGps(userCode,dCode,discussionCode,pageNum,pageSize, new RxCallBack<DataGps>() {
                @Override
                public void onSucessed(DataGps dataGps) {
                        if(dataGps.isIsSuccess()){
                            if(getView()!=null)
                                getView().getUserGpsSuccessed(dataGps);
                        }else{
                            if(getView()!=null)
                                getView().getUserGpsFailed(dataGps.getMessage());
                        }
                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                    if(getView()!=null)
                        getView().getUserGpsFailed(e.message);
                }
            });
    }
}
