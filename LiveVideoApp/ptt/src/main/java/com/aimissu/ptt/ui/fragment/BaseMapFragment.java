package com.aimissu.ptt.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.TextureSupportMapFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;

/**
 * 百度地图基类封装
 */
public abstract class BaseMapFragment<T extends IBasePresenter> extends BaseMvpFragment<T> {
    @BindView(R.id.map_view)

            //地图改用MapView  by cuizh,0411
//    TextureMapView mapView;
            MapView mapView;
    private BaiduMap baiduMap;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        checkPermission();
        LogUtil.i("hexiang","baseMap.mapView.onResume()");
    }
    private void checkPermission(){
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        mapView.onResume();
                        LogUtil.i("hexiang","baseMap.mapView.onResume()");
                    } else {
                        checkPermission();
                        ToastUtils.showLocalToast("需要设置权限");
                    }
                });
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        LogUtil.i("hexiang","baseMap.mapView.onPause()");
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        LogUtil.i("hexiang","baseMap.mapView.onSaveInstanceState()");
    }



    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("hexiang","baseMap.mapView.onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i("hexiang","baseMap.mapView.onDestroyView()");
        mapView.onDestroy();
        LogUtil.i("hexiang","baseMap.mapView.onDestroyView() done");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.i("hexiang","baseMap.mapView.onDetach()");
    }

    public BaiduMap getBaiduMap() {
        return baiduMap = mapView != null ? mapView.getMap() : null;
    }

    /**
     * 初始化
     *
     * @return
     */
    public abstract MapStatus getInitMapStatus();


    public void setMapStatus(MapStatus mapStatus) {
        if (getBaiduMap() != null) {
            if (mapStatus != null) {
                getBaiduMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            }
        }
    }

//    public TextureMapView getMapView() {
//        return mapView;
//    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    protected void lazyInitData() {
        super.lazyInitData();
        if (getBaiduMap() != null) {
            if (getInitMapStatus() != null) {
                setMapStatus(getInitMapStatus());
            }
        }
    }
}
