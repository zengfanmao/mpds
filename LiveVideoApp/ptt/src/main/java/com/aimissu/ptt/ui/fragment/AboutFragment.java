package com.aimissu.ptt.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.mvp.IBasePresenter;
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
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataVersion;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.PageUtils;
import com.ansen.http.net.HTTPCaller;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.internal.Utils;
import io.github.lizhangqu.coreprogress.ProgressUIListener;

/**
 * 关于
 */
public class AboutFragment extends BaseMvpFragment<IBasePresenter> {

    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.tv_vision)
    TextView tvVision;
    @BindView(R.id.tv_newVision)
    TextView tvNewVision;
    @BindView(R.id.tv_newVision_info)
    TextView tv_newVision_info;
    private ProgressDialog progressDialog;
    private String tag="AboutFragment";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected IBasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTile.setText("关于");
        tvVision.setText("MPDS "+packageName(getActivity())+"");

    }

    /**
     * 获取最新的版本
     */
    private void getLatestVison() {
        RetrofitClient.getInstance().postAsync(DataVersion.class,
                RxConfig.getMethodApiUrl("api/do/getNewestVersion"),
                RxMapBuild.created()
                        .put("AppType", "android")
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataVersion>() {
            @Override
            public void onSucessed(DataVersion dataVersion) {
                if (dataVersion!=null&&dataVersion.isIsSuccess()){
                    if (dataVersion.getEntity().getAppVersionNo()>packageCode(getActivity())){
                        showUpdaloadDialog(dataVersion.getEntity().getAppDownloadUrl());
                    }else {
                        tv_newVision_info.setText("没有新版本");
                    }
//                    tvNewVision.setText(dataVersion.getEntity().appVersionName);
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

            }
        }));
    }

    @Override
    public void onBack() {
        PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_OTHER, true);
    }
    @OnClick({R.id.li_back,R.id.tv_newVision})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.li_back:
                onBack();
                break;
            case R.id.tv_newVision:
                getLatestVison();
                break;
        }

    }

    public int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    private void showUpdaloadDialog(final String downloadUrl){
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置提示框的标题
        builder.setTitle("版本升级").
                setIcon(R.mipmap.ic_launcher). // 设置提示框的图标
                setMessage("发现新版本！请及时更新").// 设置要显示的信息
                setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                startUpload(downloadUrl);//下载最新的版本程序
            }
        }).setNegativeButton("取消", null);//设置取消按钮,null是什么都不做，并关闭对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();
    }

    private void startUpload(String downloadUrl){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载新版本");
        progressDialog.setCancelable(false);//不能手动取消下载进度对话框

        String fileDownloadUrl = RxConfig.getBaseApiUrl() + CommonUtils.emptyIfNull(downloadUrl.substring(0));
        String fileSavePath = Configs.FileRoot + CommonUtils.getFileNameFromUrl(downloadUrl);
        LogUtil.i(tag,"fileDownloadUrl:"+fileDownloadUrl+"  fileSavePath:"+fileSavePath);
        HTTPCaller.getInstance().downloadFile(fileDownloadUrl,fileSavePath,null,new ProgressUIListener(){

            @Override
            public void onUIProgressStart(long totalBytes) {//下载开始
                progressDialog.setMax((int)totalBytes);
                progressDialog.show();
            }

            //更新进度
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                progressDialog.setProgress((int)numBytes);
            }

            @Override
            public void onUIProgressFinish() {//下载完成
//                Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_LONG).show();
                ToastUtils.showToast("下载完成");
                progressDialog.dismiss();
                openAPK(fileSavePath);
            }
        });
    }

    private void openAPK(String fileSavePath){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+fileSavePath),"application/vnd.android.package-archive");
        startActivity(intent);
    }

}
