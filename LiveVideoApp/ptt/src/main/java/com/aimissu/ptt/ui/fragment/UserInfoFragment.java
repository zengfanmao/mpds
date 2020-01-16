package com.aimissu.ptt.ui.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.mvp.IBasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPostHeadImage;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.local.LocalUserEntity;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.model.ChatModel;
import com.aimissu.ptt.service.LocationManger;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.popwindow.PopLocationBaiDuDiTu;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.ImageUtils;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.webRequestUtil;
import com.aimissu.ptt.view.widget.audio.FileUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 个人信息
 */
public class UserInfoFragment extends BaseMvpFragment<IBasePresenter> {

    private static final String TAG = UserInfoFragment.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_depart_name)
    TextView tvDepartName;
    @BindView(R.id.tv_dcode)
    TextView tvDcode;
    @BindView(R.id.tv_relateNum)
    TextView tvRelateNum;
    @BindView(R.id.tv_userCode)
    TextView tvUserCode;
    @BindView(R.id.iv_head)
    ImageView ivhead;
    @BindView(R.id.tv_downloadbaidu)
    TextView tvDownloadbaidu;
    private List<String> mBaidituItems = new ArrayList<>();
    private PopLocationBaiDuDiTu mPopLocationBaiDuDiTu;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_userinfo;
    }

    @Override
    protected IBasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTile.setText("个人信息");
        tvUserName.setText(AppManager.getUserName());
        tvDepartName.setText(AppManager.getUserData().getdName());
        tvDcode.setText(AppManager.getUserData().getdCode());
        tvRelateNum.setText(AppManager.getUserData().getDeviceId());
        tvUserCode.setText(AppManager.getUserData().getUserCode());

        ImageUtils.loadImage(getContext(), AppManager.getUserData().getHeadPortrait(), ivhead, null);
        LogUtil.i(TAG, "头像路径：" + AppManager.getUserData().getHeadPortrait() + "  用户实体：" + AppManager.getUserData());

        if (!Global.isBaiDuExists()) {
            tvTile.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showDownLoadBaidu(tvTile);
                }
            }, 50);

        }


        //初始化百度地图下载窗口 by cuizh.0320
        mPopLocationBaiDuDiTu = new PopLocationBaiDuDiTu(getActivity(), mBaidituItems, null);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBack() {
        PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_OTHER, true);
    }

    @OnClick({R.id.li_back, R.id.btn_quit, R.id.iv_head, R.id.tv_downloadbaidu})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.li_back:
                onBack();
                break;
            case R.id.btn_quit:

                LogUtil.i("getApitoken", "getApitoken test:    " + AppManager.getApiToken());

                webRequestUtil.logOut(new RxCallBack<DataPdtGroup>() {
                    @Override
                    public void onSucessed(DataPdtGroup dataPdtGroup) {
                        if (dataPdtGroup.isIsSuccess()) {

                        }
                    }

                    @Override
                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                    }
                });

                ActivityLuanch.stopMqttService(getActivity());
                ((Activity)getContext()).finish();
                LocationManger.getInstance().stopLocation();

                ((NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE)).cancel(1);

                Global.getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        System.exit(0);
                    }
                }, 100);

                break;
            case R.id.iv_head:
                //头像修改，1.上传头像，2.把上传头像地址更新到用户表
                //todo 1
                //选择照片
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                break;
            case R.id.tv_downloadbaidu:
                showDownLoadBaidu(tvTile);
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
//            //得到选择上传的照片结果
//            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//            LocalMedia imageItem = null;
//            if (selectList != null && selectList.size() >= 0 && !TextUtils.isEmpty((imageItem = selectList.get(0)).getPath())) {
//                LogUtil.i("hexiang", "selectList imageItem.getPath():" + imageItem.getPath());
//                Uri uri = Uri.fromFile(new File(imageItem.getPath()));
//                new Compressor(AppManager.getApp())
//                        .compressToFileAsFlowable(new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri)))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(file ->
//                                        //上传图片
//                                        new ChatModel().uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Image)
//                                                        .filePath(file.getPath())
//                                                        .uploadType(IMType.UploadType.Image)
//                                                        .build()
//                                                , new RxCallBack<DataPostFile>() {
//                                                    @Override
//                                                    public void onSucessed(DataPostFile dataPostFile) {
//                                                        if (dataPostFile.isIsSuccess() && dataPostFile.getEntity() != null) {
//                                                            //上传成功保存到本地
//                                                            LocalMsgFile localMsgFile = FileUtils.getLocalMsgFileByMsgId(AppManager.getUserCode());
//                                                            if (localMsgFile != null) {
//                                                                try {
//                                                                    localMsgFile.setFileDownloadUrll(RxConfig.getBaseApiUrl() + dataPostFile.getEntity().fRelativePath);
//                                                                    localMsgFile.setLocalFileUrl(file.getPath());
//                                                                    AppManager.getDaoSession().getLocalMsgFileDao().insertOrReplace(localMsgFile);
//                                                                } catch (Exception e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                            } else {
//                                                                FileUtils.addFile(new LocalMsgFile.Builder()
//                                                                        .localFileUrl(file.getPath())
//                                                                        .fileDownloadUrll(RxConfig.getBaseApiUrl() + dataPostFile.getEntity().fRelativePath)
//                                                                        .fCode(dataPostFile.getEntity().fCode)
//                                                                        .msgId(AppManager.getUserCode())//msgId=自己id，表示自己的头像记录
//                                                                        .build());
//                                                            }
//                                                            LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,uploadFile onSucessed addFile filecode:" + dataPostFile.getEntity().fCode + " ,localPath:" + file.getPath() + ",downfile:" + RxConfig.getBaseApiUrl() + dataPostFile.getEntity().fRelativePath);
//                                                            //调用api，更新到用户信息表中
//                                                            String headPortrait = localMsgFile.getLocalFileUrl();
//                                                            try {
//                                                                File file = new File(headPortrait);
//                                                                if (!file.exists()) {
//                                                                    headPortrait = localMsgFile.getFileDownloadUrll();
//                                                                }
//                                                            } catch (Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                            try {
//                                                                List<LocalUserEntity> userEntities = AppManager.getDaoSession().getLocalUserEntityDao().loadAll();
//                                                                LocalUserEntity localUserEntity=null;
//                                                                if (userEntities != null && userEntities.size() > 0) {
//                                                                    localUserEntity=userEntities.get(0);
//                                                                }
//                                                                localUserEntity.setHeadPortrait(localMsgFile.getFileDownloadUrll());
//                                                                AppManager.getDaoSession().getLocalUserEntityDao().insertOrReplace(localUserEntity);
//                                                            } catch (Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                            ImageUtils.loadImage(getContext(), headPortrait, ivhead, null);
//                                                            //调用api更新到用户表
//                                                            updateUserHeadPortrait(dataPostFile.getEntity().fCode);
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
//
//                                                    }
//                                                })
//
//                                , throwable -> {
//                                    throwable.printStackTrace();
//                                    LogUtil.i("hexiang", "uploadFile file err:" + throwable.getMessage());
//                                });
//
//
//            }
//
//
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtil.i("test", "Picture_Result_OK");
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            //得到选择上传的照片结果
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            LocalMedia imageItem = null;
            if (selectList != null && selectList.size() >= 0 && !TextUtils.isEmpty((imageItem = selectList.get(0)).getPath())) {
                LogUtil.i("hexiang", "selectList imageItem.getPath():" + imageItem.getPath());
                Uri uri = Uri.fromFile(new File(imageItem.getPath()));
                new Compressor(AppManager.getApp())
                        .compressToFileAsFlowable(new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(file ->
                                        //上传图片
                                        new ChatModel().uploadHeadImage(new IMPamras.Builder().msgType(IMType.MsgType.Image)
                                                        .filePath(file.getPath())
                                                        .uploadType(IMType.UploadType.Image)
                                                        .build()
                                                , new RxCallBack<DataPostHeadImage>() {
                                                    @Override
                                                    public void onSucessed(DataPostHeadImage dataPostFile) {
                                                        if (dataPostFile.isIsSuccess() && dataPostFile.getEntity() != null) {
                                                            LogUtil.i(TAG, "上传成功····");
                                                            //上传成功保存到本地
                                                            LocalMsgFile localMsgFile = null;
                                                            try {
                                                                localMsgFile = FileUtils.getLocalMsgFileByMsgId(AppManager.getUserCode());
                                                                LogUtil.i(TAG, "localMsgFile:" + localMsgFile);
                                                            } catch (Exception e) {

                                                            }

                                                            if (localMsgFile != null) {

                                                                try {
                                                                    localMsgFile.setFileDownloadUrll(dataPostFile.getEntity());
                                                                    localMsgFile.setLocalFileUrl(file.getPath());
                                                                    AppManager.getDaoSession().getLocalMsgFileDao().insertOrReplace(localMsgFile);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {

                                                                FileUtils.addFile(new LocalMsgFile.Builder()
                                                                        .localFileUrl(file.getPath())
                                                                        .fileDownloadUrll(dataPostFile.getEntity())
//                                                                        .fCode(dataPostFile.getEntity().fCode)
                                                                        .msgId(AppManager.getUserCode())//msgId=自己id，表示自己的头像记录
                                                                        .build());

                                                                //修复新装APP上传头像显示不正常的问题  by cuizh,0417
                                                                try {
                                                                    localMsgFile = FileUtils.getLocalMsgFileByMsgId(AppManager.getUserCode());
                                                                    localMsgFile.setFileDownloadUrll(dataPostFile.getEntity());
                                                                    localMsgFile.setLocalFileUrl(file.getPath());
                                                                    AppManager.getDaoSession().getLocalMsgFileDao().insertOrReplace(localMsgFile);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                            LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,localPath:" + file.getPath() + ",downfile:" + dataPostFile.getEntity());
                                                            //调用api，更新到用户信息表中
                                                            String headPortrait = null;
                                                            try {
                                                                headPortrait = localMsgFile.getLocalFileUrl();
                                                                File file = new File(headPortrait);
                                                                if (!file.exists()) {
                                                                    headPortrait = localMsgFile.getFileDownloadUrll();
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                List<LocalUserEntity> userEntities = AppManager.getDaoSession().getLocalUserEntityDao().loadAll();
                                                                LocalUserEntity localUserEntity = null;
                                                                if (userEntities != null && userEntities.size() > 0) {
                                                                    localUserEntity = userEntities.get(0);
                                                                }
                                                                localUserEntity.setHeadPortrait(localMsgFile.getFileDownloadUrll());
                                                                Long result = AppManager.getDaoSession().getLocalUserEntityDao().insertOrReplace(localUserEntity);
                                                                LogUtil.i(TAG, " localUserEntity:" + localUserEntity + "  result:" + result);
                                                                ImageUtils.loadImage(getContext(), headPortrait, ivhead, null);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            //调用api更新到用户表
//                                                            updateUserHeadPortrait(dataPostFile.getEntity().fCode);

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                                        LogUtil.i(TAG, "上传失败····");
                                                    }
                                                })

                                , throwable -> {
                                    throwable.printStackTrace();
                                    LogUtil.i("hexiang", "uploadFile file err:" + throwable.getMessage());
                                });


            }


        }
    }

    /**
     * 更新用户头像api调用
     */
    private void updateUserHeadPortrait(String fileCode) {
        //todo 2 调用api，更新到用户信息表中，

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
        if (mPopLocationBaiDuDiTu != null) {
            mPopLocationBaiDuDiTu.dismiss();
        }

        //取消每次click都初始化下载窗口 by cuizh,0320
//        mPopLocationBaiDuDiTu = new PopLocationBaiDuDiTu(getActivity(), mBaidituItems, null);

        mPopLocationBaiDuDiTu.showAsDropDown(view);

    }

}
