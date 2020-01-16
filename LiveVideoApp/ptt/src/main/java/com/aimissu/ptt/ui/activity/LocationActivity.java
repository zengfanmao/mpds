package com.aimissu.ptt.ui.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpActivity;
import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.event.SendMapEvent;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.utils.BdMapUtils;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.LogHelper;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 */
public class LocationActivity extends BaseMvpActivity implements SensorEventListener, MKOfflineMapListener {

    @BindView(R.id.texturemapview)
    TextureMapView mMapView;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private String mCurrentPositionName;
    private String mCurentScreenShotUrl;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private GeoCoder mSearch;
    private String mLatitdue;
    private String mLongitude;
    private String tag = "LocationActivity";
    private LatLng latLng;
    private String mUserName;
    private String mGpsTargetType;
    private String mUserCode;
    private MKOfflineMap mkOfflineMap;

    @Override
    protected IBasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BaseMvpActivity.flag == -1) {
            restartApp();
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        LogUtil.i(tag, "LocationActivity···onCreate···");
        mkOfflineMap = new MKOfflineMap();
        mkOfflineMap.init(this);
    }

    public void restartApp() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    @Override
    protected void configToolBar(Toolbar toolbar, TextView title) {
        title.setText("位置");
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
            mLatitdue = intent.getStringExtra(Configs.LATITDUE);
            mLongitude = intent.getStringExtra(Configs.LONGITUDE);
            mUserName = intent.getStringExtra(Configs.USERNAME);
            mUserCode = intent.getStringExtra(Configs.USERCODE);
            mGpsTargetType = intent.getStringExtra(Configs.GPSTARGET_TYPE);
            LogUtil.i(tag, "mLatitdue:   " + mLatitdue + "   mLongitude:   " + mLongitude);
            if (!TextUtils.isEmpty(mLatitdue) && !TextUtils.isEmpty(mLongitude) && !AppManager.getUserCode().equals(CommonUtils.emptyIfNull(mUserCode))) {
                if (IMType.Params.TYPE_PDT.equals(CommonUtils.emptyIfNull(mGpsTargetType))) {
                    latLng = BdMapUtils.converGpsToBd(Double.valueOf(mLatitdue), Double.valueOf(mLongitude));
                    LogUtil.i(tag, "转换地址。。。。");
                } else {
                    latLng = new LatLng(Double.valueOf(mLatitdue), Double.valueOf(mLongitude));
                    LogUtil.i(tag, "没有转换地址。。。。");
                }
            }
        }
        getBaiduMap().setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                .fromResource(R.mipmap.as9), ContextCompat.getColor(this, R.color.transparent), ContextCompat.getColor(this, R.color.transparent))
        );

//        MapStatus.Builder builder1 = new MapStatus.Builder();
//        builder1.overlook(0);
//        getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));

        // 开启定位图层
        getBaiduMap().setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        if (latLng != null) {

            //下面语句有时候会导致地图缩放比例不正常  by cuizh,0524
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(18.0f);
            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

//            getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            View view = LayoutInflater.from(this).inflate(R.layout.location_user_view, null);
            TextView tvTitle = view.findViewById(R.id.tv_custom_title);
            tvTitle.setText(CommonUtils.emptyIfNull(mUserName));
            InfoWindow mInfoWindow = new InfoWindow(view, latLng, -80);
            //显示InfoWindow
            getBaiduMap().showInfoWindow(mInfoWindow);
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        getBaiduMap().setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        LogUtil.i(tag, "LocationActivity···onDestroy···");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            getBaiduMap().setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            getBaiduMap().setMyLocationData(locData);

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));

            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus.Builder builder = new MapStatus.Builder();
                if (latLng != null) {
                    List<LatLng> points = new ArrayList<LatLng>();
                    points.add(latLng);
                    points.add(ll);
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (LatLng p : points) {
                        builder1 = builder1.include(p);
                    }
                    LatLngBounds latlngBounds = builder1.build();

                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMapView.getWidth() / 2, mMapView.getHeight() / 2);

                    getBaiduMap().animateMapStatus(u);

                }
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public BaiduMap getBaiduMap() {
        return mMapView != null ? mMapView.getMap() : null;
    }


    public void setMapStatus(MapStatus mapStatus) {
        if (getBaiduMap() != null) {
            if (mapStatus != null) {
                getBaiduMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.location, menu);
        if (latLng == null) {
            getMenuInflater().inflate(R.menu.location, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.navigation_send:
                showProgressDialog();
                Rect rect = new Rect(20, mMapView.getHeight() / 2 - 140, mMapView.getWidth() - 20, mMapView.getHeight() / 2 + 140);// 左xy 右xy
                getBaiduMap().snapshotScope(rect, snapshot -> RxUtils.asyncRun(() -> {
                    mCurentScreenShotUrl = Configs.MapRoot + System.currentTimeMillis() + ".jpg";
                    File file = new File(mCurentScreenShotUrl);
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(file);
                        if (snapshot.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                            out.flush();
                            out.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, () -> runOnUiThread(() -> {
                    hideProgressDialog();
                    Uri uri = Uri.fromFile(new File(mCurentScreenShotUrl));
                    new Compressor(AppManager.getApp())
                            .compressToFileAsFlowable(new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(file -> {
                                EventBus.getDefault().post(new SendMapEvent(String.valueOf(mCurrentLat), String.valueOf(mCurrentLon), mCurrentPositionName, file.getPath()));
                                finish();
                            }, throwable -> {
                                throwable.printStackTrace();
                                ToastUtils.showLocalToast("发送失败");
                            });

                })));


                break;
        }
        return true;

    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }

            //获取地理编码结果
        }

        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }
            mCurrentPositionName = result.getAddress();
            //获取反向地理编码结果
        }
    };

    @OnClick({R.id.btn_location_activity})
    void bindClick(View view) {
        try {

            switch (view.getId()) {

                case R.id.btn_location_activity:
                    resetLocation();
                    break;
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    private void resetLocation() {
        try{

            mLocClient.requestLocation();
            LatLng latLng = LocalCache.getInstance().getPosition();

            if (mCurrentLat > 0 && mCurrentLon > 0) {
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                getBaiduMap().animateMapStatus(u);
            } else if (latLng != null) {
                getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            } else {
                isFirstLoc = true;
                mLocClient.requestLocation();
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }
}
