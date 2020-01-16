package com.aimissu.ptt.ui.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.event.RefreshMsgCountOnlineEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LogConfig;
import com.aimissu.ptt.entity.local.LogConfigDao;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.bumptech.glide.Glide;
import com.xdandroid.hellodaemon.IntentWrapper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingFragment extends BaseMvpFragment<IBasePresenter> {

    @BindView(R.id.tv_title)
    TextView tvTile;


    @BindView(R.id.tv_log_action)
    TextView tvLogAction;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected IBasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTile.setText("设置");
        initUi();

    }

    private void initUi() {

        try{

            if (AppManager.getLogIsUpload()) {
                tvLogAction.setText("已开启");
                tvLogAction.setTextColor(Color.GREEN);
            } else {
                tvLogAction.setText("已关闭");
                tvLogAction.setTextColor(Color.RED);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onBack() {
        PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_OTHER, true);
    }

    @OnClick({R.id.li_back, R.id.rl_set_pwd, R.id.rl_clear_cache, R.id.rl_log, R.id.rl_setting_ruler})
    void OnClick(View view) {

        try{

            switch (view.getId()) {
                case R.id.li_back:
                    onBack();
                    break;
                case R.id.rl_set_pwd:
                    PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_SET_PASSWORD);
                    break;
                case R.id.rl_clear_cache:
                    showProgressDialog();
                    RxUtils.asyncRun(new Runnable() {
                        @Override
                        public void run() {
                            //删除缓存文件，留1及文件夹
                            File fileRoot = new File(Configs.Root);
                            if (fileRoot.isDirectory()) {
                                for (File file : fileRoot.listFiles()) {
                                    if (file != null) {
                                        if (file.isDirectory()) {
                                            deleteFile(file);
                                            deleteSubFileDirectory(file);
                                        } else {
                                            file.delete();
                                        }
                                    }
                                }
                            }

                            File logRoot = new File(Configs.linPhoneLog);
                            if (logRoot.isDirectory()) {
                                for (File file : logRoot.listFiles()) {
                                    if (file != null) {
                                        if (file.isDirectory()) {
                                            deleteFile(file);
                                            deleteSubFileDirectory(file);
                                        } else {
                                            file.delete();
                                        }
                                    }
                                }
                            }
                            Glide.get(getContext()).clearDiskCache();
                            AppManager.getDaoSession().getLocalMsgFileDao().deleteAll();
//                        AppManager.getDaoSession().getLocalPersonUserEntityDao().deleteAll();
//                        AppManager.getDaoSession().getLocalUserGroupDao().deleteAll();
//                        AppManager.getDaoSession().getLocalMsgCountDao().deleteAll();
                            AppManager.setHadRedAllMsgCount(IMType.MsgFromType.Group.toString(), false);
                            AppManager.setHadRedAllMsgCount(IMType.MsgFromType.Person.toString(), false);
                            EventBus.getDefault().post(new RefreshMsgCountOnlineEvent());
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showLocalToast("清理成功");
                            hideProgressDialog();
                        }
                    });
                    break;
                case R.id.rl_log:
                    //点击日志是否开启关闭

                    //已开启
                    boolean isUplaod = AppManager.getLogIsUpload();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(isUplaod ? "关闭日志上传" : "开启日志上传");
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View v = inflater.inflate(R.layout.setting_log_api_layout, null);
                    EditText etLogApi = (EditText) v.findViewById(R.id.et_log_api);
                    dialog.setView(v);
                    etLogApi.setText(AppManager.getLogApi());
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    if (AppManager.getLogIsUpload()) {
                        etLogApi.setEnabled(false);
                        tvLogAction.setText("已开启");
                        tvLogAction.setTextColor(Color.GREEN);
                        //已经开启了服务器日志
                        dialog.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //关闭服务器日志
                                updateLogData(etLogApi.getText().toString(), String.valueOf(false));
                                initUi();

                            }
                        });
                    } else {
                        etLogApi.setEnabled(true);
                        tvLogAction.setText("已关闭");
                        tvLogAction.setTextColor(Color.RED);
                        dialog.setNegativeButton("开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(etLogApi.getText().toString())) {
                                    ToastUtils.showLocalToast("清先设置日志服务器Api地址");
                                    return;
                                }
                                if (!etLogApi.getText().toString().startsWith("http")) {
                                    ToastUtils.showLocalToast("Api地址格式不正确http://");
                                    return;
                                }


                                updateLogData(etLogApi.getText().toString(), String.valueOf(true));
                                initUi();
                            }
                        });
                    }


                    dialog.show();

                    break;
                case R.id.rl_setting_ruler:
                    IntentWrapper.whiteListMatters(getActivity(), "消息和电话监听的持续运行");
                    break;
            }


        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private void updateLogData(String logApi, String isUpload) {

        try{

            //开启服务器日志
            LogConfigDao logConfigDao = AppManager.getDaoSession().getLogConfigDao();
            List<LogConfig> logConfigDaos = logConfigDao.loadAll();
            LogConfig logConfig;
            if (logConfigDaos != null && logConfigDaos.size() > 0) {
                logConfig = logConfigDaos.get(0);
            } else {
                logConfig = new LogConfig();
            }
            logConfig.setServerApi(logApi);
            logConfig.setIsUpload(isUpload);
            //更新到本地
            AppManager.getDaoSession().insertOrReplace(logConfig);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private void deleteFile(File file) {

        try{

            if (file != null) {
                if (file.isDirectory()) {
                    for (File fileSub : file.listFiles()) {

                        deleteFile(fileSub);
                    }
                } else {
                    file.delete();
                }
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private void deleteSubFileDirectory(File directory) {

        try{

            for (File file1 : directory.listFiles()) {
                for (File file : file1.listFiles()) {
                    deleteSubFileDirectory(file);
                    file.delete();
                }
                file1.delete();
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

}
