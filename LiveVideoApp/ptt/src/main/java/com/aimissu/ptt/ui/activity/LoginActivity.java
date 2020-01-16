package com.aimissu.ptt.ui.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aimissu.basemvp.base.BaseJsonData;
import com.aimissu.basemvp.mvp.BaseMvpActivity;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.model.ChatModel;
import com.aimissu.ptt.presenter.ILoginPresenter;
import com.aimissu.ptt.presenter.LoginPresenter;
import com.aimissu.ptt.ui.popwindow.PopLocationBaiDuDiTu;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.LoginSharedPreferences;
import com.aimissu.ptt.utils.SharePreferenceUtil;
import com.aimissu.ptt.view.ILoginView;
import com.grgbanking.video.VideoCore;
import com.grgbanking.video.VideoStateListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */
public class LoginActivity extends BaseMvpActivity<ILoginPresenter> implements ILoginView, VideoStateListener {
    private String tag = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_username)
    EditText mUsername;
    @BindView(R.id.et_userpwd)
    EditText mUserpwd;
    //    @BindView(R.id.et_server)
//    EditText mServer;
    @BindView(R.id.cb_remember)
    CheckBox mRemember;
    @BindView(R.id.btn_login)
    Button mLogin;
    @BindView(R.id.iv_setServerIp)
    ImageView mIvsSetServerIp;
    @BindView(R.id.ll_login_container)
    LinearLayout mLoginContainer;

    private static final int SAY_HOLLE = 0;
    private List<String> mBaidituItems = new ArrayList<>();
    private PopLocationBaiDuDiTu mPopLocationBaiDuDiTu;
//    private Messenger mService;

    //    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            mService = new Messenger(iBinder);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };
    @Override
    protected ILoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void configToolBar(Toolbar toolbar, TextView title) {
        hideToolBarLayout(true);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent intent) {
//        ActivityLuanch.startMqttService(this);

        BaseMvpActivity.flag = 0;

        handIntent(intent);
        mPresenter.initLinPhone(this);
//        initAccountData();
//        Intent intentMqttService = new Intent(this, MqttService.class);
//        bindService(intentMqttService, mConnection, Context.BIND_AUTO_CREATE);
//        ActivityLuanch.startMqttService(this);
        mRemember.setOnCheckedChangeListener((buttonView, isChecked) -> LoginSharedPreferences.setLoginCheck(LoginActivity.this, isChecked));
        mRemember.setChecked(LoginSharedPreferences.getLoginCheck(this));

//        upLoadLog();
//        login();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handIntent(intent);
    }

    private void handIntent(Intent intent) {
        //处理第三方启动activity 穿过来的参数
        if (intent != null) {

            String loginId = intent.getStringExtra("loginId");
            LogUtil.i("RxLog", "loginId:" + loginId + ",old loginId:" + AppManager.getThirdAppLoginId());
            if (!TextUtils.isEmpty(loginId)) {
                //当activity 存在并且 曾经登陆的id和当前的一样,否则重新登陆
                mIvsSetServerIp.setVisibility(View.INVISIBLE);
                mLoginContainer.setVisibility(View.INVISIBLE);
                if (AppManager.getApp().isMainActvityRunning() && loginId.equals(AppManager.getThirdAppLoginId())) {
                    //直接进主界面
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                }

//                else{
//                    //重新登陆
//                    mIvsSetServerIp.setVisibility(View.VISIBLE);
//                    mLoginContainer.setVisibility(View.VISIBLE);
//                }
                mUsername.setText(loginId);
            } else {
                if (AppManager.getApp().isMainActvityRunning()) {
                    //直接进主界面
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                }
                mUsername.setText(LoginSharedPreferences.getAccount(this));
            }
        }else {
            if (AppManager.getApp().isMainActvityRunning()) {
                //直接进主界面
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
        }
    }


    /**
     * 初始化用户数据
     */
    private void initAccountData() {
        mUsername.setText(LoginSharedPreferences.getAccount(this));
//        mUserpwd.setText(LoginSharedPreferences.getPassword(this));
//        mServer.setText(AppManager.getServerIpInOtherProcess());
    }


    @OnClick({R.id.btn_login, R.id.iv_setServerIp})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();

                break;
            case R.id.iv_setServerIp:
                showSettingDialog();
                break;
        }
    }

    /**
     * 登陆
     */
    private void login() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        if (VideoCore.getInstance() != null) {

                            LogUtil.i(tag, "login中········");
                            showProgressDialog();
                            mLogin.setEnabled(false);
//                            String mServerUrl = AppManager.getServerIpInOtherProcess();
                            String mServerUrl = Configs.BG_SERVER_IP;
                            RxConfig.setBaseApiUrl("http://" + mServerUrl + "/");
                            String loginName = mUsername.getText().toString().trim();
                            AppManager.setThirdAppLoginId(loginName);
                            String pwd = mUserpwd.getText().toString().trim();
                            LogUtil.i(tag, "登陆地址mServerUrl:" + mServerUrl);
                            mPresenter.login(loginName, pwd, mServerUrl);
                            AppManager.setNeedSedPwd("admin888".equals(pwd));
                            if (mRemember.isChecked()) {
                                LoginSharedPreferences.setAccount(RxConfig.getContext(), loginName);
                                LoginSharedPreferences.setPassword(RxConfig.getContext(), pwd);
                            } else {
                                LoginSharedPreferences.setAccount(RxConfig.getContext(), "");
                                LoginSharedPreferences.setPassword(RxConfig.getContext(), "");
                            }
                            ActivityLuanch.stopMqttService(LoginActivity.this);
                            ActivityLuanch.startMqttService(LoginActivity.this);
                            mLogin.setEnabled(true);
                        }
                    } else {
                        ToastUtils.showLocalToast(this, "需要定位、sd卡读写、相机、录音、等权限才能使用", ToastUtils.Duration.LONG);
                    }
                });
    }


    /**
     *
     */
    private void showSettingDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("设置服务器地址");
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.set_serverip_layout, null);
        EditText serverPort = (EditText) v.findViewById(R.id.et_video_port);
        EditText serverIp = (EditText) v.findViewById(R.id.et_server_ip);
        dialog.setView(v);
        serverPort.setText(AppManager.getVideoServerPort());
        serverIp.setText(AppManager.getServerIpInOtherProcess());
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String serverPortStr = serverPort.getText().toString().trim();
                String serverIpStr = serverIp.getText().toString().trim();
                AppManager.setServer(serverIpStr, serverPortStr);
                serverPort.setText(serverPortStr);
                serverIp.setText(serverIpStr);
            }
        });

        dialog.show();
    }

//    /**
//     * 发送服务器地址给MqttServer
//     */
//    private void sendServerUrl() {
//        Message msg = Message.obtain(null, SAY_HOLLE,0,0);
//        Bundle data = new Bundle();
//        data.putString(Configs.SERVERURL, LoginSharedPreferences.getImserVerIp(LoginActivity.this));
//        msg.setData(data);
//
//        try {
//            mService.send(msg);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onStateChanged(int state, String message) {
        LogUtil.i(tag, "onStateChanged state:" + state + ", message: " + message);
        if (state == VideoStateListener.ERROR || state == VideoStateListener.REGISTER_FAILED) {
            Global.showToast(message);
            hideProgressDialog();
        } else if (state == VideoStateListener.GLOBAL_ON) { // 僵尸初始化成功，切换到登录界面
//            switchToLogin();

            hideProgressDialog();
        } else if (state == VideoStateListener.REGISTER_OK) {
            LogHelper.userEntity = null;
            hideProgressDialog();
//            ActivityLuanch.startMqttService(this);
            mLogin.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mRemember.isChecked()) {
                        mUsername.setText("");
                        mUserpwd.setText("");
                    }
                    ActivityLuanch.viewMainActivity(LoginActivity.this);
                    finish();
                }
            }, 200);
        } else {
//            ToastUtils.showLocalToast(this,"请确认服务器ip和端口是否正确!",ToastUtils.Duration.LONG);
        }
    }






    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(tag, "onResume : ");
    }

    @Override
    public void onLinPhoneServiceReady() {
        hideProgressDialog();
        LogUtil.i(tag, "onLinPhoneServiceReady : ");
        if (VideoCore.getInstance() != null) {
            VideoCore.getInstance().addStateListener(this);
        }

//        login();
        LogUtil.i(tag, "login········");
    }

    @Override
    public void loginSuccessed(BaseJsonData model) {
        //登陆成功后注册对讲的sdk
        RxConfig.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                try {
//                    ActivityLuanch.startMqttService(LoginActivity.this);
                    VideoCore.getInstance().register(mUsername.getText().toString().trim(), mUsername.getText().toString().trim(), "pdt1234", AppManager.getVideoServerInOtherProcess(), 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void loginFailed(String msg) {
        hideProgressDialog();
        showToastMsg(msg);

        mUsername.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000 * 2);
    }

    /**
     * 上传错误日志
     */
    private void upLoadLog() {
        LogUtil.i(tag, "开始上传错误日志···");
        String logDir = Environment.getExternalStorageDirectory() + "/crash/";

        File logFolder = new File(logDir);
        File[] subFile = logFolder.listFiles();
        File logfile = subFile[0];

        if (logfile != null) {
            new ChatModel().uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.File)
                    .filePath(logfile.getAbsolutePath())
                    .FileType(IMType.MsgType.txt)
                    .build(), new RxCallBack<DataPostFile>() {
                @Override
                public void onSucessed(DataPostFile dataPostFile) {
                    LogUtil.i(tag, "上传成功···" + dataPostFile.getMessage());

                    for (int i = 0; i < subFile.length; i++) {
                        File f = subFile[i];
                        f.delete();
                        LogUtil.i(tag, "删除文件···");
                    }

                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                    LogUtil.i(tag, "上传失败···");
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        mPresenter.unBind();
        //释放VideoCore.mStateListeners
        if (VideoCore.getInstance() != null) {
            VideoCore.getInstance().removeStateListener(this);
        }
//        unbindService(mConnection);
    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //修复每次进入APP时跳转到登陆页面的问题 by cuizh,0321
//        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
//            finish();
//            return;
//        }
//    }

}

