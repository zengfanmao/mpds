package com.aimissu.ptt.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpActivity;
import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.net.rx.JsonResponse;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.FragmentUtil;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.MainAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.ReadMsgEvent;
import com.aimissu.ptt.entity.call.CallDisconnectedEvent;
import com.aimissu.ptt.entity.call.CallIncomingReceivedEvent;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.event.CallSpeakerEvent;
import com.aimissu.ptt.entity.event.ChangePageEvent;
import com.aimissu.ptt.entity.event.ConnectTimeOutEvent;
import com.aimissu.ptt.entity.event.HeadSetPttEvent;
import com.aimissu.ptt.entity.event.LocationResultEvent;
import com.aimissu.ptt.entity.event.PersonalCallEvent;
import com.aimissu.ptt.entity.event.ReConnectEvent;
import com.aimissu.ptt.entity.event.ReceiverVideoEvent;
import com.aimissu.ptt.entity.event.RefreshRedPointEvent;
import com.aimissu.ptt.entity.event.initReadyEvent;
import com.aimissu.ptt.entity.im.BaseMsg;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.CommandMessage;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ReceieveMsgEvent;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.model.ChatModel;
import com.aimissu.ptt.presenter.IMainPresenter;
import com.aimissu.ptt.presenter.MainPresenter;
import com.aimissu.ptt.receiver.HeadsetPlugReceiver;
import com.aimissu.ptt.service.ILocationListener;
import com.aimissu.ptt.service.LocationManger;
import com.aimissu.ptt.service.MqttService;
import com.aimissu.ptt.ui.fragment.GroupChatFragment;
import com.aimissu.ptt.ui.fragment.PersonalChatFragment;
import com.aimissu.ptt.utils.BadgeUtil;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.JsonUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.view.IMainView;
import com.aimissu.ptt.view.widget.NoScrollViewPager;
import com.google.gson.Gson;
import com.grgbanking.video.VideoCore;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.IntentWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linphone.core.LinphoneNatPolicy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import devlight.io.library.ntb.NavigationTabBar;

/**
 */
public class MainActivity extends BaseMvpActivity<IMainPresenter> implements IMainView, NavigationTabBar.OnTabBarSelectedIndexListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;
    MainAdapter mainAdapter;

    @BindView(R.id.navigationtabar)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.tv_network_state)
    TextView tvNetworkState;

    @BindView(R.id.fl_child_fragment_container)
    FrameLayout flChildFragmentContainer;
    ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
    private AlertDialog.Builder dialog;
    private AlertDialog mTimeOutAlertDialog;
    private AlertDialog mAlertDialog;
    private Timer mTimer;
    private ConnectTimeOutReceiver mConnectTimeOutReceiver;
    private FragmentTransaction transaction;
    private LoginFragment mLoginFragment;

    private HeadsetPlugReceiver headsetPlugReceiver;


    /**
     * IM连接超时弹出框
     */
    private AlertDialog.Builder timeOutDialog;
    private String mBaiducfgPath;
    private List<PersonUserEntity> pUserEntitiesCache = new ArrayList<>();


    @Override
    protected IMainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void configToolBar(Toolbar toolbar, TextView title) {
        hideToolBarLayout(true);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent intent) {

        initMainData(intent);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (BaseMvpActivity.flag == -1) {
            restartApp();
        }

    }

    //被手机后台kill掉后重启APP  by cuizh,0326
    public void restartApp() {
        try{

            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("RestartFlag", "-1");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里LoginActivity之上的所有activty
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    //Activity被回收时，回收fragment  by cuizh,0326
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化主页主页面数据
     *
     * @param intent
     */
    private void initMainData(Intent intent) {
        try{
            mainAdapter = new MainAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mainAdapter);
            models = mPresenter.buildTab(this);
            models.get(PageConfig.PAGE_ONE).hideBadge();
            models.get(PageConfig.PAGE_TWO).hideBadge();
            models.get(PageConfig.PAGE_THREE).hideBadge();
            models.get(PageConfig.PAGE_FIVE).hideBadge();
            navigationTabBar.setModels(models);
            navigationTabBar.setOnTabBarSelectedIndexListener(this);
            viewPager.setOffscreenPageLimit(mainAdapter.getCount());
            navigationTabBar.setModelIndex(PageConfig.PAGE_ONE);
            viewPager.setCurrentItem(PageConfig.PAGE_ONE, false);
            ActivityLuanch.startMqttService(this);
            intCurrentPage(intent);
            checkPermission();
            registImReceiver();
            receiverVideo(intent);
            startLocation();
            updateBadger();
            heartBeat();
            registConnectTimeOutReceiver();

//            checkNotification();

            //检测耳机插入和拔出 by cuizh,0410
            registerHeadsetPlugReceiver();

//        setStunServer();

            if (AppManager.getSharePreferenceUtil().getBooleanValue("white_list", true)) {
                IntentWrapper.whiteListMatters(this, "消息和电话监听的持续运行");
                AppManager.getSharePreferenceUtil().writeBooleanValue("white_list", false);
            }

            LogUtil.i(TAG, "百度地图是否存在Global.isBaiDuExists():" + Global.isBaiDuExists());
//        int a = 1/0;
            upLoadLog();
//        LogUtil.i(TAG, "RxConfig.getBaseApiUrl():" + RxConfig.getBaseApiUrl().substring(0, RxConfig.getBaseApiUrl().lastIndexOf("/")));

            if (!Global.isBaiDuExists()) {
                ToastUtils.showLocalToast(MainActivity.this, "没有地图数据包，请下载！", ToastUtils.Duration.LONG);
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_USER_INFO);
                    }
                }, 100);

            }

            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Global.clearCache();
                }
            }, 500);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


//    //通过线控耳机按键PTT讲话  by cuizh,0506
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
//
//            if (event.getRepeatCount() == 0) {  //如果长按的话，getRepeatCount值会一直变大
//                //短按
//                EventBus.getDefault().post(new HeadSetPttEvent(true));
//                LogUtil.i("test","short click");
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
//
//            EventBus.getDefault().post(new HeadSetPttEvent(false));
//            LogUtil.i("test","click release");
//            return true;
//        }
//        return false;
//    }

    private void checkNotification(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).getNotificationChannel("ptt.notification");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                ToastUtils.showLocalToast("请手动将通知打开");
            }
        }

    }

    /**
     * 设置穿透服务器
     */
    private void setStunServer() {
//        String stun = "http://"+Configs.DEFAULT_SERVER_IP + ":55000";
        String stun = Configs.DEFAULT_SERVER_IP + ":55000";
        LinphoneNatPolicy nat = VideoCore.getInstance().getLc().getNatPolicy();
        if (nat == null) {
            nat = VideoCore.getInstance().getLc().createNatPolicy();
        }
        if (nat != null) {
            nat.setStunServer(stun);

            if (stun != null && !stun.isEmpty()) {
                nat.enableStun(true);
//                nat.enableIce(true);
//                nat.enableTurn(true);
            }
            VideoCore.getLc().setNatPolicy(nat);
        }
    }

    //后台服务定位并上传
    private void startLocation() {

        try {

            LocationManger.getInstance().setConLisntener(new LocationManger.ConLisntener() {
                @Override
                public void onServiceConnected() {
                    LocationManger.getInstance().startLocation();
                    LocationManger.getInstance().registListener(new ILocationListener() {
                        @Override
                        public void onLocationResultSuccessed(double longitude, double latitude, String positionName, String cityName) throws RemoteException {
                            //每10秒或者位置改变了就定位上传

//                            LogUtil.i("test","location>>>  longitude:"+longitude+"   latitude:"+latitude);

                            EventBus.getDefault().post(new LocationResultEvent(LocalCache.getInstance().setData(longitude, latitude, positionName, cityName)));
                            RetrofitClient.getInstance().postAsync(null,
                                    RxConfig.getMethodApiUrl("api/do/addGpsHistory"),
                                    RxMapBuild.created()
                                            .put("CaseCode", null)
                                            .put("DevCode", android.os.Build.SERIAL)
                                            .put("UserCode", AppManager.getLoginName())
                                            .put("GpsTargetType", "user")
                                            .put("UserLatitude", String.valueOf(latitude))
                                            .put("UserLongitude", String.valueOf(longitude))
                                            .put("Speed", null)
                                            .put("AccelerationX", null)
                                            .put("AccelerationY", null)
                                            .put("AccelerationZ", null)
                                            .put("Altitude", null)
                                            .put("ApiToken", AppManager.getApiToken())
                                            .build()
                            ).subscribe(RxUtils.getJsonResponseSubscriber(new RxCallBack<JsonResponse>() {
                                @Override
                                public void onSucessed(JsonResponse jsonResponse) {

//                                    ToastUtils.showLocalToast("location send success");
//                                    LogUtil.i("test","location send success");
                                }

                                @Override
                                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                                }
                            }));
                        }


                        @Override
                        public void onLocationResultFailed(String msg) throws RemoteException {

                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    });
                }
            });
        } catch (Exception e) {
            LogUtil.i(TAG, e.getMessage());
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void onStartTabSelected(NavigationTabBar.Model model, int index) {
//        if (AppManager.isNeedSedPwd()) {
//            ToastUtils.showLocalToast(this, "密码太简单，请先修改默认密码！", ToastUtils.Duration.LONG);
//            return;
//        }
        turnPage(index, PageUtils.getIDByPosition(index), false, null);
    }

    @Override
    public void onEndTabSelected(NavigationTabBar.Model model, int index) {
    }

    /**
     * 检查是否相机开启的有权限
     */
    private void checkPermission() {
        int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
        LogUtil.i(TAG, "[Permission] Camera permission is " + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (camera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 203);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangePageEvent event) {
        if (event != null && mainAdapter != null) {
            navigationTabBar.setModelIndex(event.getTabPosition());
            turnPage(event.getTabPosition(), event.getTabID(), event.isHideTargetChild(), event.getBundle());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitReadyEvent(initReadyEvent event) {
        if (event != null && mLoginFragment != null && transaction != null) {
            transaction.remove(mLoginFragment);
        }
    }

    Fragment currentChildFragment = null;

    private void turnPage(int position, int id, boolean hideTargetChild, Bundle bundle) {
//        if (id == PageConfig.PAGE_ID_GROUP_CHAT) {
//            AppManager.getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_GROUP_SUM, 0);
//            models.get(PageConfig.PAGE_ONE).setBadgeTitle("");
//            navigationTabBar.setModels(models);
//        }
//        if (id == PageConfig.PAGE_ID_CHAT) {
//            AppManager.getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_PERSON_SUM, 0);
//            models.get(PageConfig.PAGE_ONE).setBadgeTitle("");
//            navigationTabBar.setModels(models);
//        }
        try{
            synchronized (this) {
                switch (id) {
                    case PageConfig.PAGE_ONE:
                    case PageConfig.PAGE_TWO:
                    case PageConfig.PAGE_THREE:
                    case PageConfig.PAGE_FOUR:
                    case PageConfig.PAGE_FIVE:
                        //当前对应置顶的界面id
                        if (hideTargetChild) {
                            //隐藏要跳转界面的子界面，比如按了返回
                            mainAdapter.setTabTopID(position, -1);
                        }
                        int currentPositionTopID = mainAdapter.getTabTopID(position);
                        if (currentPositionTopID > 0 && currentPositionTopID != PageConfig.PAGE_ONE
                                && currentPositionTopID != PageConfig.PAGE_TWO
                                && currentPositionTopID != PageConfig.PAGE_THREE
                                && currentPositionTopID != PageConfig.PAGE_FOUR
                                && currentPositionTopID != PageConfig.PAGE_FIVE
                                ) {
                            //要跳转的主页面有子界面，所以应该转而跳到子界面
                            Fragment changeToFragment = mainAdapter.getFragmentByID(currentPositionTopID);
                            if (bundle != null) {
                                changeToFragment.setArguments(bundle);
                            }
                            if (changeToFragment != null) {
                                if (currentChildFragment != null) {
                                    FragmentUtil.hideFragment(getSupportFragmentManager(), currentChildFragment);
                                }
                                if (changeToFragment.isAdded()) {
                                    currentChildFragment = changeToFragment;
                                    FragmentUtil.showFragment(getSupportFragmentManager(), changeToFragment);
                                } else {
                                    currentChildFragment = changeToFragment;
                                    FragmentUtil.addFragment(getSupportFragmentManager(), changeToFragment, R.id.fl_child_fragment_container);
                                }
                            }
                            mainAdapter.setTabTopID(position, currentPositionTopID);
                            viewPager.setVisibility(View.GONE);
                            flChildFragmentContainer.setVisibility(View.VISIBLE);
                        } else {
                            //普通置顶界面切换
                            if (viewPager.getCurrentItem() != position) {
                                viewPager.setCurrentItem(position, false);
                            }
                            viewPager.setVisibility(View.VISIBLE);
                            if (currentChildFragment != null) {
                                FragmentUtil.hideFragment(getSupportFragmentManager(), currentChildFragment);
                            }
                            flChildFragmentContainer.setVisibility(View.GONE);
                        }


                        break;
                    default:
                        //跳到对应子界面
                        Fragment changeToFragment = mainAdapter.getFragmentByID(id);
                        if (bundle != null) {
                            changeToFragment.setArguments(bundle);
                        }
                        if (changeToFragment != null) {
                            if (currentChildFragment != null) {
                                FragmentUtil.hideFragment(getSupportFragmentManager(), currentChildFragment);
                            }
                            if (changeToFragment.isAdded()) {
                                currentChildFragment = changeToFragment;
                                FragmentUtil.showFragment(getSupportFragmentManager(), changeToFragment);
                            } else {
                                currentChildFragment = changeToFragment;
                                FragmentUtil.addFragment(getSupportFragmentManager(), changeToFragment, R.id.fl_child_fragment_container);
                            }
                        }
                        mainAdapter.setTabTopID(position, id);
                        viewPager.setVisibility(View.GONE);
                        flChildFragmentContainer.setVisibility(View.VISIBLE);
                        break;
                }

            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }


    }

    @Override
    public boolean isSupportEventBus() {
        return true;
    }

    /**
     * 两次返回退出应用间隔时间
     */

    //修改为1000ms
//    private static final long DIFF_DEFAULT_BACK_TIME = 1000;
    private static final long DIFF_DEFAULT_BACK_TIME = 2000;
    /**
     * 上次返回时间
     */
    private long mBackTime = -1;

    @Override
    public void onBackPressed() {
        try{

            int position = navigationTabBar.getModelIndex();
            int currentPositionTopID = mainAdapter.getTabTopID(position);
            if (currentPositionTopID > 0 && currentPositionTopID != PageConfig.PAGE_ONE
                    && currentPositionTopID != PageConfig.PAGE_TWO
                    && currentPositionTopID != PageConfig.PAGE_THREE
                    && currentPositionTopID != PageConfig.PAGE_FOUR
                    && currentPositionTopID != PageConfig.PAGE_FIVE
                    ) {
                try {
                    BaseMvpFragment fragment = mainAdapter.getFragmentByID(currentPositionTopID);
                    if (fragment != null) {
                        fragment.onBack();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } else {
//                IntentWrapper.onBackPressed(this);
            }


            // 连续点击返回退出
            long nowTime = SystemClock.elapsedRealtime();
            long diff = nowTime - mBackTime;
            if (diff >= DIFF_DEFAULT_BACK_TIME) {
                mBackTime = nowTime;
                ToastUtils.showLocalToast(this, "再按一下退出");
            } else {
                super.onBackPressed();
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    //拦截返回键增强兼容性  by cuizh,0507
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //拦截返回键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            //判断触摸UP事件才会进行返回事件处理
            if (event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
            }
            //只要是返回事件，直接返回true，表示消费掉
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private static Gson mGson = new Gson();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intCurrentPage(intent);
        receiverVideo(intent);
    }

    /**
     * 初始化页面在第几页
     *
     * @param intent
     */
    private void intCurrentPage(Intent intent) {

        try{
            if (intent != null && intent.getExtras() != null) {
                String msg = intent.getStringExtra(IMType.Params.MSG_DATA);
                BaseMsg baseMsg = JsonUtils.toModel(msg, BaseMsg.class);
                if (baseMsg == null)
                    return;
                if (!TextUtils.isEmpty(baseMsg.getMsgType())) {
                    if (IMType.MsgType.Text.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.Map.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.Image.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.Video.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.Map.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.Audio.toString().equals(baseMsg.getMsgType())
                            || IMType.MsgType.File.toString().equals(baseMsg.getMsgType())
                            ) {
                        LogUtil.i("RxLog", "MyReceiver receive a msg:" + baseMsg.getMsgContent());
                        //如果是消息通知
                        ChatMsg chatMsg = JsonUtils.toModel(baseMsg.getMsgContent(), ChatMsg.class);

                        if (chatMsg != null) {
                            if (AppManager.getLoginName().equals(chatMsg.getSendUserCode())) {
                                return;
                            }
                            if (IMType.MsgFromType.Person.toString().equals(chatMsg.getMsgFromType())) {
                                Bundle bundle = new Bundle();
                                String toUserCode = "";
                                for (String user : chatMsg.getPersonGroup().split(",")) {
                                    if (!TextUtils.isEmpty(user) && !AppManager.getLoginName().equals(user)) {
                                        toUserCode = user;
                                    }
                                }
                                String toUserName = "";
                                for (String user : chatMsg.getPersonGroupName().split(",")) {
                                    if (!TextUtils.isEmpty(user) && !AppManager.getLoginName().equals(user)) {
                                        toUserName = user;
                                    }
                                }
                                PersonUserEntity userEntity = AppManager.getUserEntityCache(toUserCode);
                                String userType = "";
                                String deviceId = "";
                                if (userEntity != null) {
                                    userType = userEntity.getDevicetype();
                                    deviceId = userEntity.getDeviceid();
                                }
                                bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(toUserCode).trim());
                                bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(userType).trim());
                                bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(toUserName).trim());
                                bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(deviceId).trim());
                                PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                            } else if (IMType.MsgFromType.Group.toString().equals(chatMsg.getMsgFromType())) {
                                Bundle bundle = new Bundle();
                                bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(chatMsg.getMsgToCode()).trim());
                                bundle.putString(IMType.Params.MSG_GROUP_TITLE, CommonUtils.emptyIfNull(chatMsg.getGroupName()).trim());
                                PageUtils.turnPage(PageConfig.PAGE_ONE, PageConfig.PAGE_ID_GROUP_CHAT, bundle);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            try{
                if (intent != null && intent.getExtras() != null) {
                    String msg = intent.getStringExtra(IMType.Params.MSG_DATA);
                    LogUtil.i(TAG, "onReceive msg:" + msg);
                    BaseMsg baseMsg = JsonUtils.toModel(msg, BaseMsg.class);
                    if (baseMsg == null)
                        return;
                    if (!TextUtils.isEmpty(baseMsg.getMsgType())) {

                        if (IMType.MsgType.Text.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.Map.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.Image.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.Video.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.Map.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.Audio.toString().equals(baseMsg.getMsgType())
                                || IMType.MsgType.File.toString().equals(baseMsg.getMsgType())
                                ) {
                            LogUtil.i("RxLog", "MyReceiver receive a msg:" + baseMsg.getMsgContent());
                            //如果是消息通知
                            ChatMsg chatMsg = JsonUtils.toModel(baseMsg.getMsgContent(), ChatMsg.class);
                            if (chatMsg != null) {
                                //消息数量增加
                                try {
                                    String toUserCode = chatMsg.getMsgToCode();
                                    if (IMType.MsgFromType.Person.toString().equals(chatMsg.getMsgFromType())) {
                                        for (String user : chatMsg.getPersonGroup().split(",")) {
                                            if (!TextUtils.isEmpty(user) && !AppManager.getLoginName().equals(user)) {
                                                toUserCode = user;
                                            }
                                        }
                                    } else if (IMType.MsgFromType.Group.toString().equals(chatMsg.getMsgFromType())) {
                                        toUserCode = chatMsg.getMsgToCode();
                                    }
                                    int position = navigationTabBar.getModelIndex();
                                    int currentPositionTopID = mainAdapter.getTabTopID(position);
                                    BaseMvpFragment fragment = mainAdapter.getFragmentByID(currentPositionTopID);

                                    GroupChatFragment groupChatFragment = null;
                                    PersonalChatFragment personalChatFragment = null;
                                    if (fragment instanceof GroupChatFragment
                                            && (groupChatFragment = (GroupChatFragment) fragment) != null
                                            && toUserCode.equals(groupChatFragment.getMsgToCode()) && IMType.MsgFromType.Group.toString().equals(chatMsg.getMsgFromType())
                                            ) {
                                        //如果收到的消息是当前打开的聊天组，不显示消息数
                                        LogUtil.i("RxLog", "MyReceiver receive a msg will not set setMsgCountIncrease because chat detail GroupChatFragment is top of window:");
                                        AppManager.setMsgCountIncrease(toUserCode, chatMsg.getMsgFromType(), false);
                                        AppManager.setHadRedMsgCount(toUserCode, chatMsg.getMsgFromType(), false);
                                    } else if (fragment instanceof PersonalChatFragment
                                            && (personalChatFragment = (PersonalChatFragment) fragment) != null
                                            && toUserCode.equals(personalChatFragment.getMsgToCode()) && IMType.MsgFromType.Person.toString().equals(chatMsg.getMsgFromType())
                                            ) {
                                        LogUtil.i("RxLog", "MyReceiver receive a msg will not set setMsgCountIncrease because chat detail PersonalChatFragment is top of window:");
                                        //如果收到的消息是当前打开的个人聊天页，不显示消息数
                                        AppManager.setMsgCountIncrease(toUserCode, chatMsg.getMsgFromType(), false);
                                        AppManager.setHadRedMsgCount(toUserCode, chatMsg.getMsgFromType(), false);
                                    } else {
                                        AppManager.setMsgCountIncrease(toUserCode, chatMsg.getMsgFromType());
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                LogUtil.i("RxLog", "MyReceiver receive a msg and send event:");
                                EventBus.getDefault().post(new ReceieveMsgEvent(chatMsg));

//                            if (IMType.MsgFromType.Group.toString().equals(chatMsg.getMsgFromType())) {
//                                int value = AppManager.getSharePreferenceUtil().getIntValue(IMType.Params.MSG_GROUP_SUM, 0) + 1;
//                                AppManager.getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_GROUP_SUM, value);
//                                models.get(PageConfig.PAGE_ONE).setBadgeTitle(value <= 0 ? "" : String.valueOf(value));
//                                navigationTabBar.setModels(models);
//                            } else if (IMType.MsgFromType.Person.toString().equals(chatMsg.getMsgFromType())) {
//                                //个人消息
//                                int value = AppManager.getSharePreferenceUtil().getIntValue(IMType.Params.MSG_PERSON_SUM, 0) + 1;
//                                AppManager.getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_PERSON_SUM, value);
//                                models.get(PageConfig.PAGE_TWO).setBadgeTitle(value <= 0 ? "" : String.valueOf(value));
//                                navigationTabBar.setModels(models);
//                            }
                            }

                        } else if (IMType.MsgType.Command.toString().equals(baseMsg.getMsgType())) {
                            LogHelper.sendLog("收到Command", baseMsg.getMsgContent());
                            String command = baseMsg.getMsgContent();
                            exitAPP(command);
                            LogUtil.i(TAG, "getMsgContent : " + command);
                            CommandMessage commandMessage = JsonUtils.toModel(command, CommandMessage.class);
                            if (commandMessage == null) {
                                LogUtil.i(TAG, "commandMessage 为空 ");
                                return;
                            }
//                        LogUtil.i(TAG, "commandMessage.getPtt_type(): " + commandMessage.getPtt_type());

                            if (commandMessage.getPtt_type() != null && commandMessage.getPtt_type().equals("1") && !TextUtils.isEmpty(commandMessage.getCallerSSI())) {
                                LogUtil.i(TAG, "commandMessage.getPtt_type(): " + commandMessage.getPtt_type());
                                PersonUserEntity personUserEntity = getPersonUserEntityFromLocal(commandMessage.getCallerSSI());
                                if (personUserEntity != null) {
                                    AppManager.speakingName = personUserEntity.getuName() + " 正在讲话中";
                                    AppManager.speakingDeviceId = commandMessage.getCallerSSI();
                                    EventBus.getDefault().post(new CallSpeakerEvent(1, AppManager.speakingName, personUserEntity.getuCode()));

                                } else {
                                    //本地没有用户数据，从网络上请求
                                    RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                                            RxConfig.getMethodApiUrl("api/do/searchUser"),
                                            RxMapBuild.created()
                                                    .put("UserCode", AppManager.getLoginName())
                                                    .put("ApiToken", AppManager.getApiToken())
                                                    .put("UserName", commandMessage.getCallerSSI())
                                                    .put("PageNum", "0")
                                                    .put("PageSize", "100")
                                                    .build()
                                    ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                                        @Override
                                        public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                                            if (dataPersonUserEntity.isIsSuccess() && dataPersonUserEntity.getEntity() != null && dataPersonUserEntity.getEntity().size() > 0) {
                                                PersonUserEntity personUserEntity = dataPersonUserEntity.getEntity().get(0);
                                                if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                                                    AppManager.speakingName = personUserEntity.getuName() + " 正在讲话中";
                                                    AppManager.speakingDeviceId = commandMessage.getCallerSSI();
                                                    EventBus.getDefault().post(new CallSpeakerEvent(1, AppManager.speakingName, personUserEntity.getuCode()));
                                                    pUserEntitiesCache.add(personUserEntity);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                                        }
                                    }));
                                }
                            } else if (commandMessage.getPtt_type() != null && AppManager.speakingDeviceId.equals(commandMessage.getCallerSSI()) && commandMessage.getPtt_type().equals("0")) {
                                AppManager.speakingName = "";
                                AppManager.speakingDeviceId = "";
                                EventBus.getDefault().post(new CallSpeakerEvent(0));
                            }

                            LogUtil.i(TAG, "AppManager.speakingName: " + AppManager.speakingName + "     AppManager.speakingDeviceId: " + AppManager.speakingDeviceId);
//                        AppManager.setCallSatusWithGroupNumber(commandMessage.getCalledSSI(), commandMessage.getPtt_type());
//                        AppManager.callName = "";
                            if (commandMessage.getEventName().equals("conference_create") && !VideoCore.getInstance().haveCall()) {
                                String conference_Creator = commandMessage.getConference_Creator();
                                LocalCache.getInstance().setConferenceCreator(conference_Creator);
                                if (!TextUtils.isEmpty(conference_Creator)) {
//                                PersonUserEntity personUserEntity = AppManager.getPersonUserEntityBydeviceId(conference_Creator);
                                    PersonUserEntity personUserEntity = getPersonUserEntityFromLocal(conference_Creator);
                                    if (personUserEntity != null) {
                                        AppManager.callName = personUserEntity.getdName() + "-" + personUserEntity.getuName();
                                        LogUtil.i(TAG, "AppManager.callName: " + AppManager.callName + "  personUserEntity    :   " + personUserEntity.toString());
                                    } else {

                                        //本地没有用户数据，从网络上请求
                                        RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                                                RxConfig.getMethodApiUrl("api/do/searchUser"),
                                                RxMapBuild.created()
                                                        .put("UserCode", AppManager.getLoginName())
                                                        .put("ApiToken", AppManager.getApiToken())
                                                        .put("UserName", conference_Creator)
                                                        .put("PageNum", "0")
                                                        .put("PageSize", "100")
                                                        .build()
                                        ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                                            @Override
                                            public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                                                if (dataPersonUserEntity.isIsSuccess() && dataPersonUserEntity.getEntity() != null && dataPersonUserEntity.getEntity().size() > 0) {
                                                    PersonUserEntity personUserEntity = dataPersonUserEntity.getEntity().get(0);
                                                    if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                                                        AppManager.callName = personUserEntity.getdName() + "-" + personUserEntity.getuName();
                                                        pUserEntitiesCache.add(personUserEntity);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                                            }
                                        }));

                                    }
                                }
                                if (commandMessage.getConference_name().equals(AppManager.getUserDeviceId())) {

                                    VideoCore.getInstance().newOutgoingCall("person_" + commandMessage.getConference_name() + "-" + AppManager.getUserDeviceId(), false);
                                    //对讲机单呼app
//                                PersonUserEntity personUserEntity = AppManager.getPersonUserEntityBydeviceId(commandMessage.getConference_Creator());
                                    PersonUserEntity personUserEntity = getPersonUserEntityFromLocal(commandMessage.getConference_Creator());
                                    if (personUserEntity != null) {
                                        //本地有用户数据
                                        Bundle bundle = new Bundle();
                                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                                        bundle.putBoolean(IMType.Params.VIDEO_CALL, false);
                                        bundle.putString(IMType.Params.CONFERENCE_CREATOR, CommonUtils.emptyIfNull(commandMessage.getConference_Creator()).trim());
                                        bundle.putString(IMType.Params.CONFERENCE_NAME, CommonUtils.emptyIfNull(commandMessage.getConference_name()).trim());
                                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                                        EventBus.getDefault().post(new PersonalCallEvent(personUserEntity.getuCode()));
                                        AppManager.personPdtCall = true;
                                    } else {
                                        //本地没有用户数据，从网络上请求
                                        showProgressDialog();
                                        RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                                                RxConfig.getMethodApiUrl("api/do/searchUser"),
                                                RxMapBuild.created()
                                                        .put("UserCode", AppManager.getLoginName())
                                                        .put("ApiToken", AppManager.getApiToken())
                                                        .put("UserName", commandMessage.getConference_Creator())
                                                        .put("PageNum", "0")
                                                        .put("PageSize", "100")
                                                        .build()
                                        ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                                            @Override
                                            public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                                                if (dataPersonUserEntity.isIsSuccess() && dataPersonUserEntity.getEntity() != null && dataPersonUserEntity.getEntity().size() > 0) {
                                                    PersonUserEntity personUserEntity = dataPersonUserEntity.getEntity().get(0);
                                                    if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                                                        pUserEntitiesCache.add(personUserEntity);
                                                        new RxPermissions(MainActivity.this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                                                .subscribe((Boolean granted) -> {
                                                                    if (granted) {
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                                                                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                                                                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                                                                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                                                                        bundle.putBoolean(IMType.Params.VIDEO_CALL, false);
                                                                        bundle.putString(IMType.Params.CONFERENCE_CREATOR, CommonUtils.emptyIfNull(commandMessage.getConference_Creator()).trim());
                                                                        bundle.putString(IMType.Params.CONFERENCE_NAME, CommonUtils.emptyIfNull(commandMessage.getConference_name()).trim());
                                                                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                                                                        EventBus.getDefault().post(new PersonalCallEvent(personUserEntity.getuCode()));
                                                                        AppManager.personPdtCall = true;
                                                                    } else {
                                                                        ToastUtils.showLocalToast(MainActivity.this, "需要录音、相机权限才能使用", ToastUtils.Duration.LONG);
                                                                    }
                                                                });

                                                    } else {
                                                        ToastUtils.showToast("获取用户信息失败-1");
                                                        VideoCore.getInstance().terminateCall();
                                                    }
                                                } else {
                                                    ToastUtils.showToast("获取用户信息失败-2");
                                                    VideoCore.getInstance().terminateCall();
                                                }

                                                hideProgressDialog();
                                            }

                                            @Override
                                            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                                LogUtil.i(TAG, "获取用户的错误信息" + e.getMessage());
                                                ToastUtils.showToast("获取用户信息失败-3");
                                                VideoCore.getInstance().terminateCall();
                                                hideProgressDialog();
                                            }
                                        }));


                                    }
                                } else {
                                    //对讲机组呼app
                                    VideoCore.getInstance().newOutgoingCall("group_" + commandMessage.getConference_name() + "-" + AppManager.getUserDeviceId(), false);
                                }

                            } else if (commandMessage.getEventName().equals("conference_ptt") && commandMessage.getPtt_type().equals("1") && !VideoCore.getInstance().haveCall()) {
                                VideoCore.getInstance().newOutgoingCall("group_" + commandMessage.getCalledSSI() + "-" + AppManager.getUserDeviceId(), false);

                            }
                        }
                    }
                }
            }catch (Exception e){
                LogHelper.sendErrorLog(e);
            }

        }
    }

    /**
     * 退出app
     *
     * @param command
     */
    private void exitAPP(String command) {

        try{
            if (!TextUtils.isEmpty(command) && command.equals("offline")) {
                ToastUtils.showLocalToast("APP已被禁用，退出登陆！");
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 2000);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 从本地获取PersonUserEntity
     *
     * @param deviceid
     * @return
     */
    @Nullable
    private PersonUserEntity getPersonUserEntityFromLocal(String deviceid) {

        try{
            PersonUserEntity personUserEntity = AppManager.getPersonUserEntityBydeviceId(deviceid);
            if (personUserEntity == null) {
                for (PersonUserEntity p : pUserEntitiesCache) {
                    if (p.getDeviceid().equals(deviceid)) {
                        personUserEntity = p;
                    }
                }
            }
            return personUserEntity;
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

        return null;
    }

    /**
     * 从本地获取PersonUserEntity
     *
     * @param uCode
     * @return
     */
    @Nullable
    private PersonUserEntity getPersonUserEntityFromLocalByUname(String uCode) {

        try{
            PersonUserEntity personUserEntity = AppManager.getPersonUserEntity(uCode);
            if (personUserEntity == null) {
                for (PersonUserEntity p : pUserEntitiesCache) {
                    if (p.getuCode().equals(uCode)) {
                        personUserEntity = p;
                    }
                }
            }
            return personUserEntity;
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

        return null;
    }

    private MyReceiver mReceiver;

    private void registImReceiver() {
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseMsg.class.getName());
        registerReceiver(mReceiver, filter);
    }

    private void unRegistImReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }


    /**
     * 检测耳机插入和拔出 by cuizh,0410
     */
    private void registerHeadsetPlugReceiver() {
        headsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);
    }

    private void unRegistHeadsetPlugReceiver() {
        if (headsetPlugReceiver != null) {
            unregisterReceiver(headsetPlugReceiver);
        }
    }


    private void registConnectTimeOutReceiver() {
        if (mConnectTimeOutReceiver == null) {
            mConnectTimeOutReceiver = new ConnectTimeOutReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectTimeOutEvent.class.getName());
        registerReceiver(mConnectTimeOutReceiver, filter);
    }

    private void unRegistConnectTimeOutReceiver() {
        if (mConnectTimeOutReceiver != null) {
            unregisterReceiver(mConnectTimeOutReceiver);
        }
    }

    class ConnectTimeOutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "ConnectTimeOutReceiver。。。。");

            if (intent != null) {
                int state = intent.getIntExtra(Configs.netWorkState, 0);
                if (state == -1) {
                    tvNetworkState.setVisibility(View.VISIBLE);
                    showConnectTimeOutDialog();
                    LogUtil.i(TAG, "");
                } else {
                    tvNetworkState.setVisibility(View.GONE);
                }

                LogUtil.i(TAG, "state:" + state);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            AppManager.getApp().setMainActivity(this);

            LocationManger.getInstance().bindService(this);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    protected void onDestroy() {

        try{
            AppManager.getApp().setMainActivity(null);
            try {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        webRequestUtil.logOut(new RxCallBack<DataPdtGroup>() {
//            @Override
//            public void onSucessed(DataPdtGroup dataPdtGroup) {
//
//            }
//
//            @Override
//            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
//
//            }
//        });

            //关闭APP时取消通知栏
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(1);

            unRegistHeadsetPlugReceiver();

            unRegistImReceiver();
            unRegistConnectTimeOutReceiver();
            LocationManger.getInstance().stopLocation();
            LocationManger.getInstance().unbindService(this);
            ActivityLuanch.stopMqttService(this);
//            System.exit(0);

            //延时exit  by cuizh,0408
            Global.getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    System.exit(0);
                }
            }, 100);

            if (mTimer != null) {
                mTimer.cancel();
            }

            super.onDestroy();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{

            if (BaseMvpActivity.flag == -1) {
                restartApp();
            }

            if (!AppManager.isServiceRunning(MqttService.class)) {
                ActivityLuanch.startMqttService(this);
            } else {
                LogUtil.i("RxLog", "onResume MqttService is Still Running");
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    /**
     * 处理小视频
     *
     * @param intent
     */
    private void receiverVideo(Intent intent) {


        try{
            if (intent != null) {
                String videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
                String videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
                LogUtil.i("hexiang", "ReceiverVideoEvent mainactivity videoUrl:" + videoUri + ",screenShot:" + videoScreenshot);
                if (!TextUtils.isEmpty(videoUri) && !TextUtils.isEmpty(videoScreenshot)) {
                    //得到拍摄小视屏的内容
                    EventBus.getDefault().post(new ReceiverVideoEvent(videoUri, videoScreenshot));
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallIncomingReceivedEvent(CallIncomingReceivedEvent event) {

        try{
            if (event != null) {

//            onResume();
                String remoteUserName = VideoCore.getLc().getCurrentCall().getRemoteAddress().getUserName();
                boolean videoEnabled = VideoCore.getLc().getCurrentCall().getRemoteParams().getVideoEnabled();
                LogUtil.i(TAG, "跳转到通话页面。。。。VideoEnabled : " + videoEnabled + "   remoteUserName : " + remoteUserName);
                showDialog(remoteUserName, videoEnabled);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallDisConnected(CallDisconnectedEvent event) {

        try{
            if (event != null && mAlertDialog != null) {
                mAlertDialog.dismiss();
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 显示来电接听弹出框
     */
    public void showDialog(String userName, boolean videoEnabled) {

        try{
            dialog = new AlertDialog.Builder(MainActivity.this);
            //获取AlertDialog对象

            //显示来电联系人姓名 by cuizh,0316
            PersonUserEntity personUserEntity = getPersonUserEntityFromLocalByUname(userName);

            String CallingName = userName;
            if (personUserEntity != null) {
                CallingName = personUserEntity.getuName();
            }
            dialog.setTitle(CallingName + " 语音来电");//设置标题
            if (videoEnabled) {
                dialog.setTitle(CallingName + " 视频来电");
            }

            dialog.setCancelable(false);//设置是否可取消
            dialog.setPositiveButton("接听", new DialogInterface.OnClickListener() {
                @Override//设置ok的事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此处写入ok的逻辑
//                PersonUserEntity personUserEntity = AppManager.getPersonUserEntity(userName);

                    if (personUserEntity != null) {
                        //本地有用户数据
                        new RxPermissions(MainActivity.this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                .subscribe((Boolean granted) -> {
                                    if (granted) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                                        bundle.putBoolean(IMType.Params.VIDEO_CALL, videoEnabled);
                                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                                        VideoCore.getInstance().acceptCall();
                                        EventBus.getDefault().post(new PersonalCallEvent(personUserEntity.getuCode()));
                                    } else {
                                        ToastUtils.showLocalToast(MainActivity.this, "需要录音、相机权限才能使用", ToastUtils.Duration.LONG);
                                    }
                                });
                    } else {
                        //本地没有用户数据，从网络上请求
                        showProgressDialog();
                        RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                                RxConfig.getMethodApiUrl("api/do/searchUser"),
                                RxMapBuild.created()
                                        .put("UserCode", AppManager.getLoginName())
                                        .put("ApiToken", AppManager.getApiToken())
                                        .put("UserName", userName)
                                        .put("PageNum", "0")
                                        .put("PageSize", "100")
                                        .build()
                        ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                            @Override
                            public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                                if (dataPersonUserEntity.isIsSuccess() && dataPersonUserEntity.getEntity() != null && dataPersonUserEntity.getEntity().size() > 0) {
                                    PersonUserEntity personUserEntity = dataPersonUserEntity.getEntity().get(0);
                                    pUserEntitiesCache.add(personUserEntity);
                                    if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                                        new RxPermissions(MainActivity.this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                                .subscribe((Boolean granted) -> {
                                                    if (granted) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                                                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                                                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                                                        bundle.putBoolean(IMType.Params.VIDEO_CALL, videoEnabled);
                                                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                                                        VideoCore.getInstance().acceptCall();
                                                        EventBus.getDefault().post(new PersonalCallEvent(personUserEntity.getuCode()));
                                                    } else {
                                                        ToastUtils.showLocalToast(MainActivity.this, "需要录音、相机权限才能使用", ToastUtils.Duration.LONG);
                                                    }
                                                });

                                    } else {
                                        ToastUtils.showToast("获取用户信息失败-4");
                                        VideoCore.getInstance().terminateCall();
                                    }
                                } else {
                                    ToastUtils.showToast("获取用户信息失败-5");
                                    VideoCore.getInstance().terminateCall();
                                }

                                hideProgressDialog();
                            }

                            @Override
                            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                LogUtil.i(TAG, "获取用户的错误信息" + e.getMessage());
                                ToastUtils.showToast("获取用户信息失败-6");
                                VideoCore.getInstance().terminateCall();
                                hideProgressDialog();
                            }
                        }));

                    }


//                if (personUserEntity != null) {
//
//                    new RxPermissions(MainActivity.this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
//                            .subscribe((Boolean granted) -> {
//                                if (granted) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
//                                    bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
//                                    bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
//                                    bundle.putBoolean(IMType.Params.VIDEO_CALL, videoEnabled);
//                                    PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
//                                    VideoCore.getInstance().acceptCall();
//                                    EventBus.getDefault().post(new PersonalCallEvent(personUserEntity.getuCode()));
//                                } else {
//                                    ToastUtils.showLocalToast(MainActivity.this, "需要录音、相机权限才能使用", ToastUtils.Duration.LONG);
//                                }
//                            });
//
//                } else {
//                    VideoCore.getInstance().terminateCall();
//                }

                    dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton("挂断", new DialogInterface.OnClickListener() {
                @Override//设置取消事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此写入取消的事件
                    VideoCore.getInstance().terminateCall();
                    dialogInterface.dismiss();
                }
            });

            mAlertDialog = dialog.show();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    public void showConnectTimeOutDialog() {

        try{
            if (mTimeOutAlertDialog != null) {
                mTimeOutAlertDialog.dismiss();
            }
            timeOutDialog = new AlertDialog.Builder(MainActivity.this);
            //获取AlertDialog对象
            timeOutDialog.setTitle("IM服务器连接超时，继续重连？");//设置标题
            timeOutDialog.setCancelable(false);//设置是否可取消
            timeOutDialog.setPositiveButton("重连", new DialogInterface.OnClickListener() {
                @Override//设置ok的事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此处写入ok的逻辑
                    ActivityLuanch.broadReConnect(MainActivity.this, ReConnectEvent.class.getName());
                    dialogInterface.dismiss();
                }
            });
            timeOutDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override//设置取消事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此写入取消的事件
                    dialogInterface.dismiss();
                }
            });

            mTimeOutAlertDialog = timeOutDialog.show();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(ReadMsgEvent event) {
        if (event != null) {
            updateBadger();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshRedPointEvent(RefreshRedPointEvent event) {
        if (event != null) {
            updateBadger();
        }
    }


    /**
     * 更新所有地方的小红点
     */
    public void updateBadger() {

        try{
            RxUtils.asyncRun(new Runnable() {
                @Override
                public void run() {
                    HashMap<String, Integer> allUnRead = AppManager.getAllUnRead();
                    if (allUnRead != null) {
                        int groupUnRead = allUnRead.containsKey(IMType.MsgFromType.Group.toString()) ? allUnRead.get(IMType.MsgFromType.Group.toString()) : 0;
                        int personUnRead = allUnRead.containsKey(IMType.MsgFromType.Person.toString()) ? allUnRead.get(IMType.MsgFromType.Person.toString()) : 0;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                            不显示个人的消息红点  by cuizh,0403
                                setTabReadPoint(groupUnRead, 0);
                                BadgeUtil.setBadgeCount(MainActivity.this, (groupUnRead + 0));

//                            setTabReadPoint(groupUnRead, personUnRead);
//                            BadgeUtil.setBadgeCount(MainActivity.this, (groupUnRead + personUnRead));
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTabReadPoint(0, 0);
                                BadgeUtil.setBadgeCount(MainActivity.this, 0);
                            }
                        });
                    }
                }

            });

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * tab 页的小红点
     *
     * @param groupUnRead
     * @param personUnRead
     */
    private void setTabReadPoint(int groupUnRead, int personUnRead) {

        try{
            models.get(PageConfig.PAGE_ONE).setBadgeTitle(String.valueOf(groupUnRead));
//        models.get(PageConfig.PAGE_ONE).updateBadgeTitle(String.valueOf(groupUnRead));
            if (groupUnRead > 0) {
                models.get(PageConfig.PAGE_ONE).showBadge();
            } else {
                models.get(PageConfig.PAGE_ONE).hideBadge();
            }
            models.get(PageConfig.PAGE_TWO).setBadgeTitle(String.valueOf(personUnRead));
//        models.get(PageConfig.PAGE_TWO).updateBadgeTitle(String.valueOf(personUnRead));
            if (personUnRead > 0) {
                models.get(PageConfig.PAGE_TWO).showBadge();
            } else {
                models.get(PageConfig.PAGE_TWO).hideBadge();
            }
            navigationTabBar.postInvalidate();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 心跳包
     */
    public void heartBeat() {

        try{
            if (TextUtils.isEmpty(RxConfig.getBaseApiUrl())) {
                return;
            }
            if (mTimer != null)
                return;
            mTimer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    RetrofitClient.getInstance().postAsync(null,
                            RxConfig.getMethodApiUrl("api/do/heartbeat"),
                            RxMapBuild.created()
                                    .put("UserCode", AppManager.getLoginName())
                                    .put("ApiToken", AppManager.getApiToken())
                                    .build()
                    ).subscribe(RxUtils.getJsonResponseSubscriber(new RxCallBack<JsonResponse>() {
                        @Override
                        public void onSucessed(JsonResponse jsonResponse) {
                            LogUtil.i(TAG, "心跳上传成功···· Thread ：" + Thread.currentThread().getName());
                        }

                        @Override
                        public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                        }
                    }));
                }
            };
            mTimer.schedule(task, 0, 1000 * 30);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 上传错误日志
     */
    private void upLoadLog() {
        String logDir = Environment.getExternalStorageDirectory() + "/crash/";

        File logFolder = new File(logDir);
        if (!logFolder.exists()) {
            return;
        }

        File[] subFile = logFolder.listFiles();
        if (subFile.length == 0) {
            return;
        }

        File logfile = subFile[0];

        if (logfile != null) {
            new ChatModel().uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.File)
                    .filePath(logfile.getAbsolutePath())
                    .FileType(IMType.MsgType.txt)
                    .build(), new RxCallBack<DataPostFile>() {
                @Override
                public void onSucessed(DataPostFile dataPostFile) {
                    LogUtil.i(TAG, "上传成功···" + dataPostFile.getMessage());

                    for (int i = 0; i < subFile.length; i++) {
                        File f = subFile[i];
                        f.delete();
                        LogUtil.i(TAG, "删除文件成功····");
                    }

                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                    LogUtil.i(TAG, "上传失败···");
                }
            });
        }

    }


    //点击EditText以外的地方时收起软键盘 by cuizh.0320
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        try{
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
            //必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
            return onTouchEvent(ev);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

        return false;
    }

    //isShoudHideInput(View v,MotionEvent e)方法
    public boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}

