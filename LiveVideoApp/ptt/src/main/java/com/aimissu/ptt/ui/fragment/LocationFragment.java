package com.aimissu.ptt.ui.fragment;


import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataDepartment;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.event.LocationManageUserEvent;
import com.aimissu.ptt.entity.event.ManageUserEvent;
import com.aimissu.ptt.entity.event.PopScreenConditionEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.ui.GpsEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.PopScreenCondition;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.presenter.ILocationPresenter;
import com.aimissu.ptt.presenter.LocationPresenter;
import com.aimissu.ptt.ui.popwindow.PopLocationBaiDuDiTu;
import com.aimissu.ptt.ui.popwindow.PopLocationRightScreen;
import com.aimissu.ptt.utils.BdMapUtils;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.ImageUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.ILocationView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 定位
 */
public class LocationFragment extends BaseMapFragment<ILocationPresenter> implements ILocationView, OnMapLoadedCallback, SensorEventListener, ClusterManager.OnClusterClickListener<GpsEntity>, ClusterManager.OnClusterItemClickListener<GpsEntity>, MKOfflineMapListener {

    private String tag = "LocationFragment";
    @BindView(R.id.rl_search_container)
    RelativeLayout rlSearchContainer;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_chat_title_bar)
    RelativeLayout rlScreenContainer;
    @BindView(R.id.li_user_screen)
    LinearLayout liUserScreen;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.li_condition_screen)
    LinearLayout liConditionScreen;
    @BindView(R.id.li_search)
    LinearLayout liSearch;

    private ClusterManager<GpsEntity> mClusterManager;
    // 定位相关
    LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    boolean isFirstMove = true; // 是否首次移动
    private MyLocationData locData;

    private GpsEntity locDataGps;

    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private Double lastX = 0.0;
    private float mCurrentAccracy;
    private SensorManager mSensorManager;
    //    private PopLocationLeftScreen mPopLocationLeftScreen;
    private PopScreenCondition popScreenCondition = new PopScreenCondition(AppManager.getdCode(), "", LocationFragment.class.getSimpleName());
    private Timer mTimer;
    private TimerTask mTask;
    private AlertDialog.Builder dialog;
    private BaiDuDiTuAdapter mBaiDuDiTuAdapter;
    private List<String> mBaidituItems = new ArrayList<>();
    private PopLocationBaiDuDiTu mPopLocationBaiDuDiTu;
    private MKOfflineMap mkOfflineMap;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_location;
    }

    @Override
    protected ILocationPresenter createPresenter() {
        return new LocationPresenter(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //获取传感器管理服务
        mSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);

    }

    @Override
    protected void lazyInitData() {
        try{

            initMap();
            setDefaultTitle();
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    doSearch();
                }
            });
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onBack() {
    }


    private void setDefaultTitle() {
        try{

            if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getdName() + "-" + popScreenCondition.getDiscussionName());
            } else if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getDiscussionName());
            } else if (TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getdName());
            } else {
                tvTitle.setText(AppManager.getUserData().getdName());
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    public void initMap() {
        try{

            mClusterManager = new ClusterManager<GpsEntity>(getActivity(), getBaiduMap());
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            // 设置地图监听，当地图状态发生改变时，进行点聚合运算
            getBaiduMap().setOnMapStatusChangeListener(mClusterManager);
            getBaiduMap().setOnMarkerClickListener(mClusterManager);
            getBaiduMap().setMyLocationConfiguration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                    .fromResource(R.mipmap.as9))
            );
            getBaiduMap().setMyLocationEnabled(true);

            //打开指南针 by cuizh,0416
//        getBaiduMap().getUiSettings().setCompassEnabled(false);
            getBaiduMap().getUiSettings().setCompassEnabled(true);

            //不启用地图俯视  by cuizh,0509
//            getBaiduMap().getUiSettings().setOverlookingGesturesEnabled(false);

//            getMapView().setLogoPosition(LogoPosition.logoPostionRightTop);

            // 定位初始化
//        mLocClient = new LocationClient(getActivity());

            mLocClient = new LocationClient(Objects.requireNonNull(getActivity()).getApplicationContext());

            mLocClient.registerLocationListener(new MyLocationListenner());

            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
            mLocClient.start();
            if (LocalCache.getInstance().getPosition() != null) {
                getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder()
                        .target(LocalCache.getInstance().getPosition())
                        .zoom(15.0f)
                        .build()));
            }
            resetLocation();
            getDepartments();
            getBaiduMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    getBaiduMap().hideInfoWindow();
                }
            });

            //定期更新数据
            mTimer = new Timer();
            mTask = new TimerTask() {
                @Override
                public void run() {
                    getGpsData();
                }
            };
            mTimer.schedule(mTask, 0, 1000 * 25);

            mkOfflineMap = new MKOfflineMap();
            mkOfflineMap.init(this);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    @Override
    public MapStatus getInitMapStatus() {
        try{
            LatLng latLng = LocalCache.getInstance().getPosition();
            return latLng == null ? null : new MapStatus.Builder()
                    .target(latLng)
                    .zoom(15.0f)
                    .build();

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
        return null;
    }

    @OnClick({R.id.tv_cancel, R.id.li_search_toggle, R.id.li_search, R.id.li_user_screen, R.id.li_condition_screen, R.id.btn_location
    })
    void bindClick(View view) {
        try{

            switch (view.getId()) {
                case R.id.tv_cancel:
                    etSearch.getText().clear();
                    toggleScreenContainer();
                    doSearch();
                    setDefaultTitle();
                    break;
                case R.id.li_search_toggle:
                    if (!PermissionUtils.hasGpsSearchPermisson()) {
                        ToastUtils.showToast("您没有定位搜索权限");
                        return;
                    }
                    toggleScreenContainer();
                    break;
                case R.id.li_search:
                    //搜素
                    if (!PermissionUtils.hasGpsSearchPermisson()) {
                        ToastUtils.showToast("您没有定位搜索权限");
                        return;
                    }
                    doSearch();
                    break;
                case R.id.li_user_screen:
//                LogUtil.i(tag, "定位消息mDataGps.getEntity():" + mDataGps.getEntity().toString());
                    //左边用户筛选
//                mPopLocationLeftScreen = new PopLocationLeftScreen(getActivity(), mDataGps, null);
//                mPopLocationLeftScreen.showAsDropDown(view);
                    showDownLoadBaidu(view);
                    break;
                case R.id.li_condition_screen:
                    //右边条件搜素
                    if (!PermissionUtils.hasGpsSearchPermisson()) {
                        ToastUtils.showToast("您没有定位搜索权限");
                        return;
                    }
                    new PopLocationRightScreen(getActivity(), LocalCache.getInstance().getDataDepartment(), popScreenCondition)
                            .showAsDropDown(view);
//                toggleShowRightScreen();
                    break;
                case R.id.btn_location:
                    //定位
                    resetLocation();

                    break;
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 下载百度离线地图
     *
     * @param view
     */
    private void showDownLoadBaidu(View view) {

        if (mBaidituItems.size() == 0) {
            mBaidituItems.add("全国基础");
            mBaidituItems.add("广州市");
            mBaidituItems.add("佛山市");
            mBaidituItems.add("深圳市");
            mBaidituItems.add("东莞市");
            mBaidituItems.add("中山市");
            mBaidituItems.add("珠海市");
            mBaidituItems.add("韶关市");
            mBaidituItems.add("潮州市");
            mBaidituItems.add("河源市");
            mBaidituItems.add("惠州市");
            mBaidituItems.add("江门市");
            mBaidituItems.add("揭阳市");
            mBaidituItems.add("茂名市");
            mBaidituItems.add("梅州市");
            mBaidituItems.add("清远市");
            mBaidituItems.add("汕头市");
            mBaidituItems.add("汕尾市");
            mBaidituItems.add("阳江市");
            mBaidituItems.add("云浮市");
            mBaidituItems.add("湛江市");
            mBaidituItems.add("肇庆市");

        }
        mPopLocationBaiDuDiTu = new PopLocationBaiDuDiTu(getActivity(), mBaidituItems, null);
        mPopLocationBaiDuDiTu.showAsDropDown(view);


//
//        dialog = new AlertDialog.Builder(getContext());
//        //获取AlertDialog对象
//        dialog.setTitle("下载离线地图");//设置标题
//        dialog.setCancelable(false);//设置是否可取消
//        dialog.setView(R.layout.downditu);
//        RecyclerView recyclerView = (RecyclerView) getmRootView().findViewById(R.id.recycleView);
//        mBaiDuDiTuAdapter = new BaiDuDiTuAdapter(R.layout.baiduditu_item, null);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(mBaiDuDiTuAdapter);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(getActivity(), 2), false));
////        smartRefreshLayout.setEnableLoadmore(false);
////        smartRefreshLayout.setOnRefreshListener(this);
//        mBaiDuDiTuAdapter.setNewData(mBaidituItems);
//        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override//设置ok的事件
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //在此处写入ok的逻辑
//
//                dialogInterface.dismiss();
//            }
//        });
//        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override//设置取消事件
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //在此写入取消的事件
//                dialogInterface.dismiss();
//            }
//        });
//
//        dialog.show();
    }


    private void doSearch() {
        try{

            String etText = etSearch.getText().toString();
            List<GpsEntity> gpsEntities = new ArrayList<>();
            List<GpsEntity> gpsEntityList = new ArrayList<>();
//        if (!TextUtils.isEmpty(etText)) {
            if (mDataGps != null && mDataGps.getEntity() != null) {
                for (GpsEntity gpsEntity : mDataGps.getEntity()) {
                    if (gpsEntity != null) {

                        if ((gpsEntity.getDiscussionName() != null && gpsEntity.getDiscussionName().contains(etText)) || (gpsEntity.getUserName() != null && gpsEntity.getUserName().contains(etText)) || (gpsEntity.getDName() != null && gpsEntity.getDName().contains(etText)) || (gpsEntity.getUserCode() != null && gpsEntity.getUserCode().contains(etText))) {
                            gpsEntities.add(gpsEntity);
                        }
                    }
                }


                for (GpsEntity gpsEntity : gpsEntities) {
                    if (gpsEntity.getStatus().equals(IMType.Params.ON_LINE) && !gpsEntity.getUserCode().equals(AppManager.getUserCode())) {

                        gpsEntityList.add(gpsEntity);
                    }
                }
                mClusterManager.clearItems();
                mClusterManager.addItems(gpsEntityList);
                mClusterManager.cluster();
            } else {
                mClusterManager.clearItems();
            }
//            tvTitle.setText("搜索-" + etSearch.getText().toString());

            moveToSearch(gpsEntityList);
//
//        } else {
//            setDefaultTitle();
//            getGpsData();
//        }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 获取数据成功，根据条件过滤用户
     */
    private void doSearchGetGps() {
        try{

            String etText = etSearch.getText().toString();
            List<GpsEntity> gpsEntities = new ArrayList<>();
            List<GpsEntity> gpsEntityList = new ArrayList<>();
            if (!TextUtils.isEmpty(etText)) {
                if (mDataGps != null && mDataGps.getEntity() != null) {
                    for (GpsEntity gpsEntity : mDataGps.getEntity()) {
                        if (gpsEntity != null) {

                            if ((gpsEntity.getDiscussionName() != null && gpsEntity.getDiscussionName().contains(etText)) || (gpsEntity.getUserName() != null && gpsEntity.getUserName().contains(etText)) || (gpsEntity.getDName() != null && gpsEntity.getDName().contains(etText)) || (gpsEntity.getUserCode() != null && gpsEntity.getUserCode().contains(etText))) {
                                gpsEntities.add(gpsEntity);
                            }
                        }
                    }


                    for (GpsEntity gpsEntity : gpsEntities) {
                        if (gpsEntity.getStatus().equals(IMType.Params.ON_LINE) && !gpsEntity.getUserCode().equals(AppManager.getUserCode())) {
                            gpsEntityList.add(gpsEntity);
                        }

                    }
                    mClusterManager.clearItems();
                    mClusterManager.addItems(gpsEntityList);
                    mClusterManager.cluster();
                } else {
                    mClusterManager.clearItems();
                }
                tvTitle.setText("搜索-" + etSearch.getText().toString());

                //by cuizh,0527
//                moveToSearch(gpsEntityList);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 移动搜索个人区域
     *
     * @param gpsEntities
     */
    private void moveToSearch(List<GpsEntity> gpsEntities) {
        try{

            LogUtil.i(tag, "定位集群的大小:" + gpsEntities.size());
            LogUtil.i(tag, "gpsEntities.toString() :     " + gpsEntities.toString());

            //by cuizh,0527
//            if (gpsEntities.size() == 1) {
//                GpsEntity gpsEntity = gpsEntities.get(0);
//                if (!TextUtils.isEmpty(gpsEntity.getLatitdue()) && !TextUtils.isEmpty(gpsEntity.getLongitude())) {
//                    LatLng latLng = new LatLng(Double.valueOf(gpsEntity.getLatitdue()), Double.valueOf(gpsEntity.getLongitude()));
//                    if (!gpsEntity.getGpsTargetType().equals("APP")) {
//                        latLng = BdMapUtils.converGpsToBd(CommonUtils.toDouble(gpsEntity.getLatitdue()), CommonUtils.toDouble(gpsEntity.getLongitude()));
//                    }
//                    getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder()
//                            .target(latLng)
//                            .zoom(15.0f)
//                            .build()));
//
//                    LogUtil.i(tag, "移动到中心。。。。");
//                }
//
//            } else if (gpsEntities.size() > 1) {
                List<LatLng> points = new ArrayList<>();
                for (int i = 0; i < gpsEntities.size(); i++) {
                    if (!TextUtils.isEmpty(gpsEntities.get(i).getLatitdue()) && !TextUtils.isEmpty(gpsEntities.get(i).getLongitude())) {
                        LatLng latLng = new LatLng(Double.valueOf(gpsEntities.get(i).getLatitdue()), Double.valueOf(gpsEntities.get(i).getLongitude()));
                        if (!gpsEntities.get(i).getGpsTargetType().equals("APP")) {
                            latLng = BdMapUtils.converGpsToBd(CommonUtils.toDouble(gpsEntities.get(i).getLatitdue()), CommonUtils.toDouble(gpsEntities.get(i).getLongitude()));
                        }
                        points.add(latLng);
                    }
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng p : points) {
                    builder = builder.include(p);
                }

                //APP自身位置加入到LatLngBounds中  by cuizh,0527
                if (locDataGps != null) {
                    LatLng loclatLng = new LatLng(Double.valueOf(locDataGps.getLatitdue()), Double.valueOf(locDataGps.getLongitude()));
                    builder=builder.include(loclatLng);
                }

                LatLngBounds latlngBounds = builder.build();
                LogUtil.i(tag, "移动群体中心。。。。mapView.getWidth():" + mapView.getWidth() + "  mapView.getHeight():" + mapView.getHeight());

                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mapView.getWidth()/2, mapView.getHeight()/2);
                getBaiduMap().animateMapStatus(u);
                LogUtil.i(tag, "移动群体中心。。。。");
//            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    private void getDepartments() {
        try{

            UserEntity userEntity = AppManager.getUserData();
            if (userEntity == null)
                return;
            RetrofitClient.getInstance().postAsync(DataDepartment.class,
                    RxConfig.getMethodApiUrl("/api/do/getDeptTree"),
                    RxMapBuild.created()
                            .put("dCode", userEntity.getdCode())
                            .put("ApiToken", AppManager.getApiToken())
                            .put("UserCode", AppManager.getLoginName())
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataDepartment>() {
                @Override
                public void onSucessed(DataDepartment dataDepartment) {
                    if (dataDepartment.isIsSuccess()) {
                        LocalCache.getInstance().setDataDepartment(dataDepartment);
                    }
                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                }
            }));
        }catch (Exception e){
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


    @Override
    public void toggleScreenContainer() {
        try{
            if (rlScreenContainer.getVisibility() == View.VISIBLE) {
                rlScreenContainer.setVisibility(View.GONE);
                rlSearchContainer.setVisibility(View.VISIBLE);
            } else {
                rlScreenContainer.setVisibility(View.VISIBLE);
                rlSearchContainer.setVisibility(View.GONE);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private DataGps mDataGps;
    private static final long DIFF_DEFAULT_LAST_UPDATE_TIME = 25000;
    /**
     * 最后一次更新时间
     */
    private long mLastUpdateTime = -1;

    @Override
    public void getUserGpsSuccessed(DataGps dataGps) {
        try{

            long nowTime = SystemClock.elapsedRealtime();
            long diff = nowTime - mLastUpdateTime;
            if (diff >= DIFF_DEFAULT_LAST_UPDATE_TIME) {
                mLastUpdateTime = nowTime;
            } else {
                return;
            }

            if (!getUserVisibleHint() || isPause) {
                return;
            }
            LogUtil.i(tag, "获取用户定位信息成功dataGps.getEntity() :     " + dataGps.getEntity().toString());
            this.mDataGps = dataGps;
            List<GpsEntity> gpsLists = dataGps.getEntity();
            String etText = etSearch.getText().toString();
            if (dataGps != null && dataGps.getEntity() != null && dataGps.getEntity().size() > 0) {
                mClusterManager.clearItems();
                List<GpsEntity> gpsEntityList = new ArrayList<>();
                List<GpsEntity> gpsEntities = new ArrayList<>();


//            if (!TextUtils.isEmpty(etText)) {
//                for (GpsEntity gpsEntity : mDataGps.getEntity()) {
//                    if (gpsEntity != null) {
//
//                        if ((gpsEntity.getDiscussionName() != null && gpsEntity.getDiscussionName().contains(etText)) || (gpsEntity.getUserName() != null && gpsEntity.getUserName().contains(etText)) || (gpsEntity.getDName() != null && gpsEntity.getDName().contains(etText)) || (gpsEntity.getUserCode() != null && gpsEntity.getUserCode().contains(etText))) {
//                            gpsEntities.add(gpsEntity);
//                        }
//                    }
//                }
//            }
//
//            if (gpsEntities!=null&&gpsEntities.size()>0){
//                gpsLists.clear();
//                gpsLists=gpsEntities;
//            }

                for (GpsEntity gpsEntity : gpsLists) {

                    if (gpsEntity.getStatus().equals(IMType.Params.ON_LINE)){

                        if (gpsEntity.getUserCode().equals(AppManager.getUserCode())) {
                            //APP自身位置信息 by cuizh,0527
                            locDataGps = gpsEntity;
                        }else {
                            gpsEntityList.add(gpsEntity);
                        }
                    }



                }
//                for (GpsEntity gpsEntity : gpsLists) {
//                    if (gpsEntity.getStatus().equals(IMType.Params.ON_LINE) && !gpsEntity.getUserCode().equals(AppManager.getUserCode())) {
//                        gpsEntityList.add(gpsEntity);
//                    }
//
//                }
                LogUtil.i(tag, "在线用户数·····gpsEntityList.size():" + gpsEntityList.size());
                if (gpsEntityList.size() > 0) {
                    mClusterManager.addItems(gpsEntityList);
                    mClusterManager.cluster();
                    if (isFirstMove) {
                        isFirstMove = false;
                        moveToSearch(gpsEntityList);
                        LogUtil.i(tag, "开始移动到定位的地方·····");
                    }

                    doSearchGetGps();

                } else {
                    mClusterManager.clearItems();
                    mClusterManager.cluster();
                    ToastUtils.showLongToast("没有在线用户");
                    LogUtil.i(tag, "没有在线用户");
                }

            } else {
                mClusterManager.clearItems();
                mClusterManager.cluster();
                LogHelper.sendLog("getUserGps", "获取定位信息为空···");
                ToastUtils.showLongToast("获取定位信息为空");
                LogUtil.i(tag, "获取定位信息为空");
            }


            //更新侧边栏的数据
//        if (mPopLocationLeftScreen != null && mPopLocationLeftScreen.screenAdapter != null) {
//            mPopLocationLeftScreen.screenAdapter.setNewData(dataGps.getEntity());
//        }

            if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getdName() + "-" + popScreenCondition.getDiscussionName());
            } else if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getDiscussionName());
            } else if (TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                tvTitle.setText(popScreenCondition.getdName());
            } else {
                tvTitle.setText(AppManager.getUserData().getdName());
            }

            LogUtil.i(tag, "popScreenCondition:" + popScreenCondition.toString());

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getUserGpsFailed(String msg) {
        try{

            mClusterManager.clearItems();
            mClusterManager.cluster();
            LogHelper.sendLog("getUserGps", "获取用户定位信息失败getUserGpsFailed···");
            ToastUtils.showLongToast("获取定位信息失败");
            LogUtil.i(tag, "获取定位信息失败");
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    private void getGpsData() {
        try{

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getBaiduMap().hideInfoWindow();
                }
            });


            //默认为用户本部门 by cuizh,0402
            if (TextUtils.isEmpty(popScreenCondition.getdCode())) {
                this.popScreenCondition.setdCode(AppManager.getdCode());
                this.popScreenCondition.setdName(AppManager.getUserData().getdName());
            }

            UserEntity userEntity = AppManager.getUserData();
            if (userEntity != null) {
                if (getUserVisibleHint() && !isPause) {

//                LogUtil.i(tag,"MapTest: getGpsData");
                    mPresenter.getUserGps(userEntity.getLoginName(), this.popScreenCondition.dCode, popScreenCondition.discussionCode, 0, 1000);
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onMapLoaded() {
        getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15.0f).build()));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        try{

            double x = sensorEvent.values[SensorManager.DATA_X];
            if (Math.abs(x - lastX) > 1.0) {
                mCurrentDirection = (int) x;
                locData = new MyLocationData.Builder()
//                    .accuracy(mCurrentAccracy)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mCurrentDirection).latitude(mCurrentLat)
                        .longitude(mCurrentLon).build();
                getBaiduMap().setMyLocationData(locData);
            }
            lastX = x;

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onClusterClick(Cluster<GpsEntity> cluster) {
        return false;
    }

    private View mMarkInfoView;
    private ViewHolder viewHolder;

    @Override
    public boolean onClusterItemClick(GpsEntity item) {
        showMarker(item, null);
        return false;
    }

    private String showUserCode = "";

    private void showMarker(GpsEntity item, GpsEntity showItem) {
        try{

            LogUtil.i(tag, "地图个人消息item:" + item.toString());

            getBaiduMap().hideInfoWindow();
            if (mMarkInfoView == null) {
                mMarkInfoView = LayoutInflater.from(getActivity()).inflate(R.layout.bts_map_user_layout, null);
            }
//        if (TextUtils.isEmpty(showUserCode)||showUserCode.equals(item.getUserCode())) {
//            GpsEntity next = getNextClusterItem(item);
//            if (next != null) {
//                item = next;
//            }
//        }
            viewHolder = new ViewHolder(mMarkInfoView);
            if (viewHolder != null && item != null) {
                viewHolder.ll_die.setVisibility(View.VISIBLE);
                viewHolder.ll_dizzy.setVisibility(View.VISIBLE);

                viewHolder.txtUserDuty.setText(CommonUtils.emptyIfNull(item.getDName()));
                viewHolder.txtStatus.setText(CommonUtils.emptyIfNull(item.getStatus()));
                viewHolder.txtUserName.setText(CommonUtils.emptyIfNull(item.getUserName()));
                viewHolder.txtUserDescription.setText(CommonUtils.emptyIfNull(item.getCaseName()));
                viewHolder.txtTackOrder.setText(CommonUtils.emptyIfNull(item.getGpsTargetType()));
                ImageUtils.loadImage(getContext(), item.getUserHeadPortrait(), viewHolder.imgUserPhoto, null);

                if (CommonUtils.emptyIfNull(item.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                    if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                        viewHolder.tv_dizzy.setText(IMType.Params.REMOTE_DIZZY);
                        viewHolder.tv_die.setText(IMType.Params.REMOTE_KILL);
                        viewHolder.tv_dizzy.setVisibility(View.VISIBLE);
                        viewHolder.tv_die.setVisibility(View.VISIBLE);

                        if (!PermissionUtils.hasPersonKillPermisson()) {
//                        ToastUtils.showToast("您没有遥毙权限");
                            viewHolder.ll_die.setVisibility(View.GONE);
                        }
                        if (!PermissionUtils.hasPersonStopPermisson()) {
//                        ToastUtils.showToast("您没有遥晕权限");
                            viewHolder.ll_dizzy.setVisibility(View.GONE);
                        }
                    } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                        viewHolder.tv_dizzy.setText(IMType.Params.ACTIVATE);
                        viewHolder.tv_die.setText(IMType.Params.REMOTE_KILL);
                        viewHolder.tv_dizzy.setVisibility(View.VISIBLE);
                        viewHolder.tv_die.setVisibility(View.VISIBLE);

                        if (!PermissionUtils.hasPersonEnablePermisson()) {
//                        ToastUtils.showToast("您没有激活权限");
                            viewHolder.ll_dizzy.setVisibility(View.GONE);
                        }
                        if (!PermissionUtils.hasPersonKillPermisson()) {
//                        ToastUtils.showToast("您没有遥晕权限");
                            viewHolder.ll_die.setVisibility(View.GONE);
                        }
                    } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_KILL)) {
                        viewHolder.tv_dizzy.setVisibility(View.GONE);
                        viewHolder.tv_die.setVisibility(View.GONE);
                    }
                } else if (item.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                    if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                        viewHolder.tv_die.setText(IMType.Params.DISABLE);
                        viewHolder.tv_die.setVisibility(View.VISIBLE);
                        viewHolder.tv_dizzy.setVisibility(View.GONE);

                        if (!PermissionUtils.hasPersonDisablePermisson()) {
//                        ToastUtils.showToast("您没有禁用权限");
                            viewHolder.ll_die.setVisibility(View.GONE);
                        }
                    } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.DISABLE)) {
                        viewHolder.tv_dizzy.setText(IMType.Params.ACTIVATE);
                        viewHolder.tv_dizzy.setVisibility(View.VISIBLE);
                        viewHolder.tv_die.setVisibility(View.GONE);

                        if (!PermissionUtils.hasPersonEnablePermisson()) {
//                        ToastUtils.showToast("您没有激活权限");
                            viewHolder.ll_dizzy.setVisibility(View.GONE);
                        }
                    }
                }

            }
            viewHolder.li_container.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            viewHolder.li_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击开始聊天
                    PersonUserEntity personUserEntity = AppManager.getPersonUserEntity(item.getUserCode());
                    if (personUserEntity != null) {
                        //本地有用户数据
                        Bundle bundle = new Bundle();
                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                    } else {
                        //本地没有用户数据，从网络上请求
                        showProgressDialog();
                        RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                                RxConfig.getMethodApiUrl("api/do/searchUser"),
                                RxMapBuild.created()
                                        .put("UserCode", AppManager.getLoginName())
                                        .put("ApiToken", AppManager.getApiToken())
                                        .put("UserName", item.getUserName())
                                        .put("PageNum", "0")
                                        .put("PageSize", "100")
                                        .build()
                        ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                            @Override
                            public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                                if (dataPersonUserEntity.isIsSuccess() && dataPersonUserEntity.getEntity() != null && dataPersonUserEntity.getEntity().size() > 0) {
                                    PersonUserEntity personUserEntity = dataPersonUserEntity.getEntity().get(0);
                                    if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                                    } else {
                                        ToastUtils.showToast("获取用户信息失败");
                                    }
                                } else {
                                    ToastUtils.showToast("获取用户信息失败");
                                }

                                hideProgressDialog();
                            }

                            @Override
                            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                LogUtil.i(tag, "获取用户的错误信息" + e.getMessage());
                                ToastUtils.showToast("获取用户信息失败");
                                hideProgressDialog();
                            }
                        }));
                    }


                }
            });
            viewHolder.li_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击开始打电话
                }
            });

            viewHolder.ll_dizzy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dizzyString = "";
                    if (CommonUtils.emptyIfNull(item.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dizzyString = IMType.Params.REMOTE_DIZZY;
                        } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    } else if (item.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.DISABLE)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dizzyString, CommonUtils.emptyIfNull(item.getUserCode()), CommonUtils.emptyIfNull(item.getUserName())));
                }
            });

            viewHolder.ll_die.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dieString = "";
                    if (CommonUtils.emptyIfNull(item.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        }
                    } else if (item.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.DISABLE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dieString, CommonUtils.emptyIfNull(item.getUserCode()), CommonUtils.emptyIfNull(item.getUserName())));
                }
            });

            InfoWindow mInfoWindow = new InfoWindow(mMarkInfoView, item.getPosition(), -80);
            getBaiduMap().showInfoWindow(mInfoWindow);
            showUserCode = item.getUserCode();
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(item.getPosition());
            getBaiduMap().setMapStatus(update);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private GpsEntity getNextClusterItem(GpsEntity currentGpsEntity) {
        try{

            if (mClusterManager.getmAlgorithm() != null) {
                Collection<GpsEntity> collection = mClusterManager.getmAlgorithm().getItems();
                if (collection != null && collection.size() > 1) {
                    Random random = new Random();
                    int position = random.nextInt(collection.size());
                    GpsEntity gpsEntity = null;
                    try {
                        gpsEntity = (GpsEntity) collection.toArray()[position];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (gpsEntity != null && gpsEntity.getUserCode().equals(currentGpsEntity.getUserCode())) {
                        return getNextClusterItem(currentGpsEntity);
                    } else if (gpsEntity != null && !gpsEntity.getUserCode().equals(currentGpsEntity.getUserCode())) {
                        return gpsEntity;
                    }

                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
        return null;
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            try{

                // map view 销毁后不在处理新接收的位置
                if (location == null || getBaiduMap() == null) {
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

//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                if (isFirstLoc) {
//                    isFirstLoc = false;
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    builder.target(ll).zoom(15.0f);
//                    getBaiduMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                }

            }catch (Exception e){
                LogHelper.sendErrorLog(e);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    boolean isPause = false;

    @Override
    public void onResume() {
        super.onResume();
//        getBaiduMap().getmGLMapView().onResume();

        try{
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_UI);

            isPause = false;

//        if (getUserVisibleHint()&&!isPause) {
//            getGpsData();
//        }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //界面可见
            LogUtil.i(tag, "map.life setUserVisibleHint: 界面可见");

        } else {
            //界面不可见 相当于onpause
            LogUtil.i(tag, "map.life setUserVisibleHint: 界面不可见");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        LogUtil.i(tag,"MapTest: onPause");

//        mSensorManager.unregisterListener(this);

        isPause = true;
    }

    @Override
    public void onStop() {
        super.onStop();

//        LogUtil.i(tag,"MapTest: onStop");

        mSensorManager.unregisterListener(this);
    }

    static class ViewHolder {
        @BindView(R.id.bts_user_info_layout)
        RelativeLayout btsUserInfoLayout;
        @BindView(R.id.img_user_photo)
        ImageView imgUserPhoto;
        @BindView(R.id.txt_user_name)
        TextView txtUserName;
        @BindView(R.id.txt_user_duty)
        TextView txtUserDuty;

        @BindView(R.id.bts_user_change_layout)
        RelativeLayout btsUserChangeLayout;
        @BindView(R.id.txt_tack_order)
        TextView txtTackOrder;
        @BindView(R.id.txt_status)
        TextView txtStatus;
        @BindView(R.id.bts_user_line)
        ImageView btsUserLine;
        @BindView(R.id.bts_user_unmanage_layout)
        RelativeLayout btsUserUnmanageLayout;
        @BindView(R.id.txt_user_description)
        TextView txtUserDescription;
        @BindView(R.id.li_container)
        LinearLayout li_container;

        @BindView(R.id.li_call)
        ImageView li_call;

        @BindView(R.id.li_chat)
        RelativeLayout li_chat;
        @BindView(R.id.ll_dizzy)
        LinearLayout ll_dizzy;
        @BindView(R.id.tv_dizzy)
        TextView tv_dizzy;
        @BindView(R.id.ll_die)
        LinearLayout ll_die;
        @BindView(R.id.tv_die)
        TextView tv_die;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PopScreenConditionEvent event) {
        if (event != null && event.getPopScreenCondition() != null && LocationFragment.class.getSimpleName().equals(event.getPopScreenCondition().getRequestCode())) {
            mLastUpdateTime = -1;
            this.popScreenCondition = event.getPopScreenCondition();

            getGpsData();
        }
    }

    @Override
    public boolean isSupportEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onManageUserEvent(ManageUserEvent event) {
        String userAction = event.getUserAction();
        String msgCode = event.getMsgToCode();
        LogUtil.i(tag, "userAction: " + userAction + "   msgCode: " + msgCode);
        if (event != null && !TextUtils.isEmpty(userAction)) {
//            showDialog(userAction, msgCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationManageUserEvent(LocationManageUserEvent event) {

        if (event != null) {
            int status = event.getmStatus();
            if (status == 1) {
                LogUtil.i(tag, "onLocationManageUserEvent: 成功····");
                if (mClusterManager != null) {
                    getGpsData();
                }

            } else {
                LogUtil.i(tag, "onLocationManageUserEvent: 失败····");
            }
        }
    }

    class BaiDuDiTuAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {

        public BaiDuDiTuAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_chengshi, item);
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }

}
