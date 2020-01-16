package com.aimissu.ptt.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.ptt.R;
import com.aimissu.ptt.ui.activity.MainActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.text.NumberFormat;

import io.reactivex.annotations.Nullable;

import static com.baidu.location.LocationClientOption.LOC_SENSITIVITY_HIGHT;

/**

 */
public class LocationService extends Service {


    private GeoCoder mSearch;
    private double latitude;
    private double longitude;
    private String cityName;
    private String positionName;
    private String tag = "LocationService";

    private Notification notification;

    private final int MIN_INTERVAL = 10000;
    private final int MIN_DISTANCE = 10;
//    private final int MIN_DISTANCE = 5;

    private ILocation.Stub mLocation = new ILocation.Stub() {

        @Override
        public void startLocation() throws RemoteException {
            if (client == null) {

                initLocationClient();
            }
            client.start();
            Log.i("hexiang", "location startLocation");
        }

        @Override
        public void stopLocation() throws RemoteException {
            if (client != null) {

                client.disableLocInForeground(true);
                client.stop();
            }
            Log.i("hexiang", "location stopLocation");
        }

        public void stopforeground() {
            client.disableLocInForeground(true);
        }

        @Override
        public double getLongitude() throws RemoteException {
            return getLongitude();
        }

        @Override
        public double getLatitude() throws RemoteException {
            return LocationService.this.getLatitude();
        }

        @Override
        public String getPositionName() throws RemoteException {
            return getPositionName();
        }

        @Override
        public void registListener(ILocationListener loctionListener) throws RemoteException {
            synchronized (this) {
                mILocationListener = loctionListener;
            }
        }

        @Override
        public void unregistListener(ILocationListener loctionListener) throws RemoteException {
            mILocationListener = null;
        }


    };

    private ILocationListener mILocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocation;
    }


    private LocationClient client;   //客户端定位

    @Override
    public void onCreate() {
        super.onCreate();
        initLocationClient();
    }

    //得到用户的具体位置
    private void initLocationClient() {

        client = new LocationClient(getApplicationContext());
        client.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

//        option.setScanSpan(MIN_INTERVAL);
        option.setOpenAutoNotifyMode(MIN_INTERVAL, MIN_DISTANCE, LOC_SENSITIVITY_HIGHT);
//        option.setOpenAutoNotifyMode();
//        option.setScanSpan(2000);
//        option.setOpenAutoNotifyMode(3000, 1, LOC_SENSITIVITY_HIGHT);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

//        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
//        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        client.setLocOption(option);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        Log.i("hexiang", "location initLocationClient");


        //开启后台定位  by cuizh,0419
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel2 = new NotificationChannel("ptt.location",
                    "定位服务", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("后台定位通知渠道");
            channel2.enableLights(true);
            channel2.setLightColor(Color.GREEN);
            channel2.setShowBadge(false);
            notificationManager.createNotificationChannel(channel2);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"ptt.location");
            builder.setContentTitle("PDT云集群定位服务")
                    .setContentText("正在后台定位")
                    .setSmallIcon(R.mipmap.ic_launcher_pdt_cloud_trunking)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSound(null);

            notification = builder.build();

        } else {

            notification = new Notification.Builder(this)
                    .setContentIntent(PendingIntent.
                            getActivity(this, 0,
                                    new Intent(this, MainActivity.class), 0)) // 设置PendingIntent
                    .setContentTitle("PDT云集群定位服务") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher_pdt_cloud_trunking) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                    .build(); // 获取构建好的Notification
        }

        client.enableLocInForeground(2, notification);
    }

    //此方法是为了监听用户操作
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
            //使用StringBuffer来接收中国内的城市
            StringBuffer buffer = new StringBuffer(256);
            //判断GPS/NETWORK 来接收得到的城市名称
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                buffer.append(bdLocation.getCity());
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                buffer.append(bdLocation.getCity());
            }
            //判断得到的城市是否为空
            if (!TextUtils.isEmpty(buffer.toString())) {
                //如果handler不为空的话，就用city来接收buffer得到的城市
                setCityName(buffer.toString());
            }
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            String addr = bdLocation.getAddrStr();    //获取详细地址信息
            String country = bdLocation.getCountry();    //获取国家
            String province = bdLocation.getProvince();    //获取省份
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息
            Log.i("hexiang", "location onReceiveLocation:addr" + addr + ",country" + country + ",province:" + city + ",city:" + province + ",district:" + district + ",street:" + street);
        }
    }


    private LatLng mLastLatLng;
    private int mLatLngDistance = -2;
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

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
                try {
                    if (mILocationListener != null) {
                        mILocationListener.onLocationResultFailed(result.error.name());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


//            if (mLastLatLng != null && result.getLocation() != null) {
//                mLatLngDistance = Integer.parseInt(getLatLngDistance(mLastLatLng, result.getLocation()));
//            }
//            LogUtil.i(tag,"mLatLngDistance  : "+mLatLngDistance);

            //获取反向地理编码结果
            if (result != null && result.getLocation() != null) {

                setLatitude(result.getLocation().latitude);
                setLongitude(result.getLocation().longitude);
                setPositionName(result.getAddress());
                Log.i(tag, "location onGetReverseGeoCodeResult:longitude" + longitude + ",latitude" + latitude + ",positionName:" + positionName);
                if (mILocationListener != null) {
                    Log.i(tag, "location onGetReverseGeoCodeResult:mILocationListener");
                    try {
                        mILocationListener.onLocationResultSuccessed(getLongitude(), getLatitude(), getPositionName(), getCityName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                mLastLatLng = result.getLocation();
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

}
