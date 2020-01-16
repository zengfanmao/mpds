package com.aimissu.ptt.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.basemvp.view.GridSpacingItemDecoration;
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.ChatAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.call.CallConnectedEvent;
import com.aimissu.ptt.entity.call.CallDisconnectedEvent;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.event.CallSpeakerEvent;
import com.aimissu.ptt.entity.event.CutCallEvent;
import com.aimissu.ptt.entity.event.ReceiverVideoEvent;
import com.aimissu.ptt.entity.event.SendMapEvent;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ReceieveMsgEvent;
import com.aimissu.ptt.entity.im.TextModel;
import com.aimissu.ptt.entity.sipMessage.CallMessage;
import com.aimissu.ptt.entity.sipMessage.SipConferenceClose;
import com.aimissu.ptt.entity.sipMessage.SipMessageToPdt;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.presenter.ChatPresenter;
import com.aimissu.ptt.presenter.IChatPresenter;
import com.aimissu.ptt.service.CoreService;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.activity.MainActivity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.JsonUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.IChatView;
import com.aimissu.ptt.view.widget.ChatMsgInputView;
import com.aimissu.ptt.view.widget.audio.MediaManager;
import com.grgbanking.video.Message;
import com.grgbanking.video.VideoCore;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 群组聊天
 */
public class GroupChatFragment extends BaseMvpFragment<IChatPresenter> implements IChatView, OnRefreshListener, ChatMsgInputView.ChatMsgInputClickEvent, Message.StateListener {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.tv_title_info)
    TextView tvTitleInfo;
    @BindView(R.id.tv_speakingName)
    TextView tvSeakingName;

    @BindView(R.id.chat_msg_input_view)
    ChatMsgInputView chatMsgInputView;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.iv_speak)
    ImageView ivSpeak;

    private String msgToCode;
    private String sendUserName;
    private ChatAdapter mChatAdapter;
    @BindView(R.id.smartrefreshlaout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_speak_icon)
    LinearLayout mRlSpeakIcon;
    @BindView(R.id.iv_volume)
    ImageView mVolume;

    public static boolean reLoadMsgDetail = false;
    private final static int DEFAULT_PAGESIZE = 20;
    private final static int DEFAULT_PAGEINDEX = 0;
    private String tag = GroupChatFragment.class.getSimpleName();
    private AnimationDrawable mVolumeAnimationDrawable;
    private String msgGroupTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try{
            super.onActivityCreated(savedInstanceState);
            checkPresenter();
            if (getArguments() != null) {
                msgToCode = getArguments().getString(IMType.Params.MSG_TO_CODE);
                msgGroupTitle=getArguments().getString(IMType.Params.MSG_GROUP_TITLE);
                tvTile.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_GROUP_TITLE)));
            }
//        tvTile.setText("群组聊天");
            mChatAdapter = new ChatAdapter(null);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(mChatAdapter);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(getActivity(), 2), false));
            chatMsgInputView.setHideTouchView(recyclerView);
            smartRefreshLayout.setEnableLoadmore(false);
            smartRefreshLayout.setOnRefreshListener(this);
            WaterDropHeader classicsHeader = new WaterDropHeader(getActivity());
            classicsHeader.setPrimaryColors(ContextCompat.getColor(getActivity(), R.color.confirm_top_bg));
            smartRefreshLayout.setRefreshHeader(classicsHeader);
            chatMsgInputView.setChatMsgInputClickEvent(this);
            getData(0, DEFAULT_PAGESIZE, null, false);
            handleSpeaker();

            //判断当前是否处于通话中
            if (VideoCore.getLc() != null && VideoCore.getLc().getCurrentCall() != null) {
                ivCall.setSelected(true);
                ivSpeak.setVisibility(View.VISIBLE);
                tvTitleInfo.setText("语音对讲中...");
                if (!TextUtils.isEmpty(AppManager.callName)) {
                    tvTitleInfo.setText(AppManager.callName + " 发起语音对讲");
                }
                tvTitleInfo.setVisibility(View.VISIBLE);
            }
            chatMsgInputView.viewHolder.ivPickerVideo.setVisibility(View.GONE);
            LogUtil.i(tag, "onActivityCreated......");
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    protected IChatPresenter createPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    public void onBack() {
        PageUtils.turnPage(PageConfig.PAGE_ONE, PageConfig.PAGE_ID_GROUP, true);
    }

    @OnClick({R.id.li_back, R.id.iv_call, R.id.iv_speak})
    void OnClick(View view) {
        try{
            switch (view.getId()) {
                case R.id.li_back:
                    onBack();
                    break;
                case R.id.iv_call:
//                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                String mimeType = mimeTypeMap.getMimeTypeFromExtension("doc");
//                String mimeType2 = mimeTypeMap.getMimeTypeFromExtension("docx");
//                String mimeType3 = mimeTypeMap.getMimeTypeFromExtension("rar  ");
//                String mimeType4 = mimeTypeMap.getMimeTypeFromExtension("zip  ");
//                String mimeType5 = mimeTypeMap.getMimeTypeFromExtension("rar  ");
//                if(true)
//                    return;
                    if (!PermissionUtils.hasGroupCallPermisson()) {
                        ToastUtils.showToast("您没有组呼权限");
                        return;
                    }

                    String pdtNumber = AppManager.getPdtNumberWithDisscusionCode(msgToCode);
                    if (!TextUtils.isEmpty(pdtNumber)) {
                        if (VideoCore.getLc().getCurrentCall() != null && !view.isSelected()) {
                            Global.showToast("您已经处于通话");
                            return;
                        }
                        if (view.isSelected()) {
                            VideoCore.getInstance().terminateCall();
                            view.setSelected(false);

                            if (PermissionUtils.hasGroupCutPermisson()&&!AppManager.callName.equals("我")) {
                                EventBus.getDefault().post(new CutCallEvent(pdtNumber));
                            }
                        } else {
                            if (Double.valueOf(pdtNumber) <= 0) {
                                ToastUtils.showToast("组号不正确");
                                return;
                            }
                            AppManager.callName = "我";
                            VideoCore.getInstance().newOutgoingCall("group_" + pdtNumber + "-" + AppManager.getUserDeviceId(), false);
                            view.setSelected(true);
                        }
                    } else {
                        ToastUtils.showToast("组号为空");
                    }
                    break;

                case R.id.iv_speak:
                    break;
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        try{
            super.onHiddenChanged(hidden);
            chatMsgInputView.onHiddenChanged(hidden);
            if (!hidden) {
                if (getArguments() != null) {
                    String code = getArguments().getString(IMType.Params.MSG_TO_CODE);
                    tvTile.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_GROUP_TITLE)));
//                tvTitleInfo.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_GROUP_TITLE)));

                    if (!TextUtils.isEmpty(code) && !msgToCode.equals(code)) {
                        msgToCode = code;
                        mChatAdapter.setNewData(null);
                        getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, null, false);
                    }


                } else {
                    Global.hideInputMethod(chatMsgInputView);
                }

                //切换守候组后重新加载服务器ChatMsgDetail  by cuizh,0321
                if (reLoadMsgDetail) {
                    getData(DEFAULT_PAGEINDEX, mChatAdapter.getItemCount(), null, false);
                    reLoadMsgDetail = false;
                }
            }

            LogUtil.i(tag, "getPersonUserEntity(msgToCode) : " + AppManager.getPersonUserEntity(msgToCode) + "msgToCode:" + msgToCode);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    @Override
    public void getMyChatDetailsSuccessed(boolean isLoadMore, DataChatMsg model) {
        smartRefreshLayout.finishRefresh();
        try{
            if (model != null) {
                if (!isLoadMore) {
                    //刷新

                    mChatAdapter.setNewData(mChatAdapter.buildItemEntity(model.getEntity()));

                } else {
                    if (model.getEntity() != null) {
                        mChatAdapter.addData(mChatAdapter.buildItemEntity(model.getEntity()));
                    }
                }



            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void getMyChatDetailsFailed(boolean isLoadMore, String msg) {
        smartRefreshLayout.finishRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceieveMsgEvent event) {
        try{
            if (event != null && mChatAdapter != null && event.getChatMsg() != null) {
                if (IMType.MsgFromType.Group.toString().equals(event.getChatMsg().getMsgFromType())) {
                    LogUtil.i("RxLog", "GroupChatFragment group ReceieveMsgEvent");
                    LogUtil.i(tag, "GroupChatFragment group ReceieveMsgEvent");
                    LogUtil.i(tag, "是群组消息。。。。");
                    //群消息
                    if (msgToCode.equals(event.getChatMsg().getMsgToCode())) {
                        try {
                            mChatAdapter.addNewMsg(event.getChatMsg());
                            recyclerView.smoothScrollToPosition(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LogUtil.i(tag, "消息添加成功。。。。" + event.getChatMsg());
                    }
                } else if (IMType.MsgFromType.Person.toString().equals(event.getChatMsg().getMsgFromType())) {
                    //个人消息
                    LogUtil.i("RxLog", "GroupChatFragment person ReceieveMsgEvent");
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


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        try{
            if (mChatAdapter != null && mChatAdapter.getItemCount() > 0) {
                //加载更多
                getData(mChatAdapter.getItemCount(), DEFAULT_PAGESIZE, null, true);
//            ToastUtils.showLocalToast(mChatAdapter.getItemCount()+"");
//            getData(0, DEFAULT_PAGESIZE, null, false);
            } else {
                getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, mChatAdapter.getCurrentLastMsgId(), false);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    public void getData(Integer skip, Integer take, String firstMsgId, boolean isLoadMore) {
        try{
            mPresenter.getMyChatDetails(isLoadMore, String.valueOf(skip), String.valueOf(take), firstMsgId, IMType.MsgFromType.Group.toString(), msgToCode, null);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    public void pushMsg(final IMPamras imPamras) {
        try{
            imPamras.setUserCode(AppManager.getLoginName());
            imPamras.setMsgFromType(IMType.MsgFromType.Group.toString());
            imPamras.setMsgToCode(msgToCode);
            mPresenter.pushMsg(imPamras);

            if (imPamras.getMsgType().equals(IMType.MsgType.Text)) {
                SipMessageToPdt mpdt = new SipMessageToPdt();
                mpdt.setCallerSSI(AppManager.getUserDeviceId());
                mpdt.setHead("PDTMSG");
                mpdt.setMsgtype("Conference_SendMsg");
                mpdt.setCalledSSI(AppManager.getPdtNumberWithDisscusionCode(msgToCode));
                mpdt.setMulticast("1");
                mpdt.setMsg(imPamras.getMsgContent());
                if (!VideoCore.getInstance().haveCall()){
                    VideoCore.getInstance().prepareMessageEnvironmentWithOutCall(AppManager.getPdtNumberWithDisscusionCode(msgToCode));
                }
                Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(mpdt).toString(), GroupChatFragment.this);
//            LogUtil.i(tag,"imPamras.getMsgContent:"+imPamras.getMsgContent()+ " message1:"+message1.toString());
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void pushMsgSuccessed(DataPushMsg model, final IMPamras imPamras) {
        chatMsgInputView.clearMsg();
        hideProgressDialog();
        if (!TextUtils.isEmpty(imPamras.localChatId)) {
            mChatAdapter.setPushMsgSuccessed(imPamras.localChatId, model.getEntity());
            recyclerView.smoothScrollToPosition(0);
        } else {
            mChatAdapter.addNewMsg(model.getEntity());
            recyclerView.smoothScrollToPosition(0);
        }


    }

    @Override
    public void pushMsgFailed(String msg, final IMPamras imPamras) {
        hideProgressDialog();
        if (!TextUtils.isEmpty(imPamras.localChatId)) {
            mChatAdapter.setPushMsgFailed(imPamras.localChatId);
        }
        LogUtil.i(tag, "pushMsgFailed msg:" + msg);
    }


    @Override
    public void uploadFileSuccessed(final DataPostFile file, final IMPamras imPamras) {
        //上传成功开始发送消息
        try{
            LogUtil.i(tag, "IMPamras:" + imPamras.toString());
            if (file != null) {
                String fileId = file.getEntity() == null ? "" : file.getEntity().fCode;
                switch (imPamras.msgType) {
                    case Image:
                        if (!TextUtils.isEmpty(fileId)) {
                            pushMsg(new IMPamras.Builder().msgType(imPamras.msgType)
                                    .msgFile(fileId)
                                    .localChatId(imPamras.localChatId)
                                    .build());
                            return;
                        }
                        break;
                    case Map:
                        pushMsg(new IMPamras.Builder().msgType(imPamras.msgType)
                                .msgFile(fileId)
                                .longitude(imPamras.longitude)
                                .latitude(imPamras.latitude)
                                .positionName(imPamras.positionName)
                                .localChatId(imPamras.localChatId)
                                .build());
                        break;
                    case Video:
                        //上传视频成功

                        switch (imPamras.uploadType) {
                            case Image:
                                //上传首帧图片成功后更新视频首帧图
                                LogUtil.i("hexiang", "上传首帧图片 ,filePaht:" + imPamras.filePath);
                                mChatAdapter.setSendVideoFirstFramePicUrl(imPamras.localChatId, imPamras.filePath);
                                mPresenter.updateFilePropertiy(new IMPamras.Builder().msgType(imPamras.msgType)
                                        .msgFile(imPamras.msgFile)
                                        .picCode(fileId)
                                        .uploadType(IMType.UploadType.Image)
                                        .msgType(imPamras.msgType)
                                        .uploadFile(imPamras.uploadFile)
                                        .localChatId(imPamras.localChatId)
                                        .build());

                                break;
                            case Video:
                                //发送视频
                                pushMsg(new IMPamras.Builder()
                                        .msgType(imPamras.msgType)
                                        .msgFile(fileId)
                                        .duration(imPamras.duration)
                                        .localChatId(imPamras.localChatId)
                                        .build());
                                //更新文件时间   更新视频参数
                                RxUtils.asyncRun(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            MediaPlayer mediaPlayer = new MediaPlayer();
                                            mediaPlayer.setDataSource(imPamras.filePath);
                                            mediaPlayer.prepare();
                                            long duration = mediaPlayer.getDuration();
                                            mediaPlayer.release();
                                            mPresenter.updateFilePropertiy(new IMPamras.Builder().msgType(imPamras.msgType)
                                                    .msgFile(fileId)
                                                    .uploadType(IMType.UploadType.Video)
                                                    .msgType(imPamras.msgType)
                                                    .duration(String.valueOf(duration))
                                                    .build());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                //上传首帧图片
                                Uri uri = Uri.fromFile(new File(imPamras.picUrl));
                                new Compressor(AppManager.getApp())
                                        .compressToFileAsFlowable(new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri)))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<File>() {
                                            @Override
                                            public void accept(File file) {
                                                mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Video)
                                                        .msgFile(fileId)
                                                        .filePath(file.getPath())
                                                        .localChatId(imPamras.localChatId)
                                                        .uploadType(IMType.UploadType.Image)
                                                        .build());
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) {
                                                throwable.printStackTrace();
                                                LogUtil.i("hexiang", "uploadFile file err:" + throwable.getMessage());
                                            }
                                        });


                                break;
                        }


                        break;
                    case Audio:
                        if (!TextUtils.isEmpty(fileId)) {
                            RxUtils.asyncRun(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        MediaPlayer mediaPlayer = new MediaPlayer();
                                        mediaPlayer.setDataSource(imPamras.filePath);
                                        mediaPlayer.prepare();
                                        long duration = mediaPlayer.getDuration();
                                        mediaPlayer.release();

                                        mPresenter.updateFilePropertiy(new IMPamras.Builder().msgType(imPamras.msgType)
                                                .msgFile(fileId)
                                                .uploadType(IMType.UploadType.Audio)
                                                .msgType(imPamras.msgType)
                                                .uploadFile(imPamras.uploadFile)
                                                .localChatId(imPamras.localChatId)
                                                .duration(String.valueOf(duration))
                                                .build());

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            return;
                        }
                        break;
                    case File:
                        //上传文件成功
                        if (!TextUtils.isEmpty(fileId)) {
                            pushMsg(new IMPamras.Builder().msgType(imPamras.msgType)
                                    .msgFile(fileId)
                                    .msgContent(imPamras.msgContent)
                                    .localChatId(imPamras.localChatId)
                                    .build());
                            return;
                        }

                        break;
                }

            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void uploadFileFailed(final String msg, final IMPamras imPamras) {
        try{
            LogUtil.i(tag, "上传文件失败  msg：" + msg);
            if (!TextUtils.isEmpty(imPamras.localChatId)) {
                if (imPamras.msgType != null) {
                    switch (imPamras.msgType) {
                        case Video:
                            if (imPamras.uploadType != null && imPamras.uploadType.equals(IMType.UploadType.Video)) {
                                //视频上传失败
                                mChatAdapter.setPushMsgFailed(imPamras.localChatId);
                            }
                            break;
                        case Audio:
                            mChatAdapter.setPushMsgFailed(imPamras.localChatId);
                            break;
                        case Map:
                            pushMsg(new IMPamras.Builder().msgType(imPamras.msgType)
                                    .fileCode(null)
                                    .localChatId(imPamras.localChatId)
                                    .build());
                            break;
                        case File:
                            mChatAdapter.setPushMsgFailed(imPamras.localChatId);
                            break;
                    }
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void updateFilePropertiySuccessed(final DataPostFile dataPostFile, IMPamras imPamras) {
        try{
            if (imPamras.msgType != null) {
                switch (imPamras.msgType) {
                    case Video:
                        if (imPamras.uploadType != null && imPamras.uploadType.equals(IMType.UploadType.Video)) {
                            //更新时间
                            mChatAdapter.setSendVideoDuration(imPamras.localChatId, imPamras.duration);
                        } else if (imPamras.uploadType != null && imPamras.uploadType.equals(IMType.UploadType.Video)) {
                            //更新图片

                        }
                        break;
                    case Audio:
                        pushMsg(new IMPamras.Builder().msgType(imPamras.msgType)
                                .msgFile(imPamras.msgFile)
                                .localChatId(imPamras.localChatId)
                                .build());
                        break;
                    case Map:

                        break;
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void updateFilePropertiyFailed(String msg, IMPamras imPamras) {
        if (imPamras.msgType != null) {
            switch (imPamras.msgType) {
                case Audio:
                    if (!TextUtils.isEmpty(imPamras.localChatId)) {
                        mChatAdapter.setPushMsgFailed(imPamras.localChatId);
                    }
                    break;
            }
        }
    }

    @Override
    public void sendMsg(String msg) {

        try{
            //showProgressDialog();
            String localChatId = String.valueOf(System.currentTimeMillis());
            mChatAdapter.addNewLocalMsg(localChatId, msg, IMType.MsgType.Text);
            recyclerView.smoothScrollToPosition(0);
            pushMsg(new IMPamras.Builder().msgType(IMType.MsgType.Text)
                    .msgContent(msg)
                    .localChatId(localChatId)
                    .build());
            chatMsgInputView.clearMsg();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void sendPhoto(View view) {
        try{
            //选择照片
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void sendTakePhoto(View view) {
        try{

            //拍照
//        PictureSelector.create(this)
//                .openCamera(PictureMimeType.ofImage())
//                .forResult(PictureConfig.CHOOSE_REQUEST);
            MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                    .fullScreen(true)
                    .smallVideoWidth(360)
                    .smallVideoHeight(480)
                    .recordTimeMax(10000)
                    .recordTimeMin(1500)
                    .maxFrameRate(20)
                    .videoBitrate(600000)
                    .captureThumbnailsTime(1)
                    .build();
            MediaRecorderActivity.goSmallVideoRecorder(getActivity(), MainActivity.class.getName(), config);
//        MediaRecorderActivity.goSmallVideoRecorder(getActivity(), null, config);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void sendVideo(View view) {
        //拍视频
        // 录制

    }

    @Override
    public void sendAudio(View view, String filePath, String duration) {
        try{

            //发送语音
            String localChatId = String.valueOf(System.currentTimeMillis());
            mChatAdapter.addNewLocalMsg(localChatId, "", IMType.MsgType.Audio);
            recyclerView.smoothScrollToPosition(0);
            mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Audio)
                    .filePath(filePath)
                    .msgToCode(msgToCode)
                    .localChatId(localChatId)
                    .uploadType(IMType.UploadType.Audio)
                    .picUrl(null)
                    .build());
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void sendMap(View view) {
        try{

            new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            ActivityLuanch.viewLocationActivity(getActivity());
                        } else {
                            ToastUtils.showLocalToast("定位需要用户授权");
                        }
                    });

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    //发送文件
    public static final int REQUESTCODE_FROM_ACTIVITY = 1000;

    @Override
    public void sendFile(View view) {
        try{

            //发送文件

            new LFilePicker()
                    .withSupportFragment(this)
                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                    .withStartPath(Configs.Root)
                    .withBackgroundColor("#2D2D2D")
                    .start();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            chatMsgInputView.hideMore();
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case PictureConfig.CHOOSE_REQUEST:
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                        LogUtil.i("hexiang", "selectList:" + selectList.toString());

                        for (LocalMedia imageItem : selectList) {
                            if (!TextUtils.isEmpty(imageItem.getPath())) {
                                LogUtil.i("hexiang", "selectList imageItem.getPath():" + imageItem.getPath());
                                String localChatId = String.valueOf(System.currentTimeMillis());
                                mChatAdapter.addNewLocalMsg(localChatId, null, IMType.MsgType.Image);
                                recyclerView.smoothScrollToPosition(0);

                                Uri uri = Uri.fromFile(new File(imageItem.getPath()));
                                new Compressor(AppManager.getApp())
                                        .compressToFileAsFlowable(new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri)))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(file -> mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Image)
                                                .filePath(file.getPath())
                                                .msgToCode(msgToCode)
                                                .localChatId(localChatId)
                                                .uploadType(IMType.UploadType.Image)
                                                .picUrl(null)
                                                .build()), throwable -> {
                                            throwable.printStackTrace();
                                            LogUtil.i("hexiang", "uploadFile file err:" + throwable.getMessage());
                                        });


                            }

                        }

                        break;
                    case REQUESTCODE_FROM_ACTIVITY:
                        //如果是文件选择模式，需要获取选择的所有文件的路径集合
                        //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                        List<String> list = data.getStringArrayListExtra("paths");
                        for (String filePath : list) {
                            if (!TextUtils.isEmpty(filePath)) {
                                LogUtil.i("hexiang", "selectList filePath:" + filePath);
                                String localChatId = String.valueOf(System.currentTimeMillis());
                                mChatAdapter.addNewLocalMsg(localChatId, null, IMType.MsgType.File);
                                recyclerView.smoothScrollToPosition(0);

                                mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.File)
                                        .filePath(filePath)
                                        .msgToCode(msgToCode)
                                        .msgContent(CommonUtils.getFileNameFromUrl(filePath))
                                        .localChatId(localChatId)
                                        .uploadType(IMType.UploadType.File)
                                        .picUrl(null)
                                        .build());


                            }

                        }
                        break;
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMapEvent event) {
        try{

            if (event != null && isVisible() && !isHidden()) {
                String localChatId = String.valueOf(System.currentTimeMillis());
                mChatAdapter.addNewLocalMsg(localChatId, null, IMType.MsgType.Map);
                recyclerView.smoothScrollToPosition(0);
                mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Map)
                        .msgToCode(msgToCode)
                        .localChatId(localChatId)
                        .uploadType(IMType.UploadType.Image)
                        .longitude(event.getLongitude())
                        .latitude(event.getLatitude())
                        .positionName(event.getPositionName())
                        .filePath(event.getImgUrl())
                        .build());
                LogUtil.i("RxLog", "sendmap:" + event.toString());

            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceiverVideoEvent event) {
        try{

            if (event != null && isVisible() && !isHidden()) {
                //收到视频
                chatMsgInputView.hideMore();
                String localChatId = String.valueOf(System.currentTimeMillis());
                mChatAdapter.addNewLocalMsg(localChatId, null, IMType.MsgType.Video);
                recyclerView.smoothScrollToPosition(0);
                mPresenter.uploadFile(new IMPamras.Builder().msgType(IMType.MsgType.Video)
                        .filePath(event.getVideoUri())
                        .msgToCode(msgToCode)
                        .localChatId(localChatId)
                        .uploadType(IMType.UploadType.Video)
                        .picUrl(event.getVideoScreenshot())
                        .build());
            }

            LogUtil.i(tag,"群聊收到小视频·····");
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatAdapter != null) {
            mChatAdapter.onDestory();
        }
        GSYVideoManager.releaseAllVideos();
    }


    @Override
    public void onResume() {
        super.onResume();
        chatMsgInputView.hideMore();
        GSYVideoManager.onResume();
    }

    @Override
    public void stateChanged(Message message, String s) {
//        LogUtil.i(tag,"message.getMessageText() : "+message.getMessageText());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallConnected(CallConnectedEvent event) {
        try{

            if (event != null && !event.isPersonCall()) {
                LogUtil.i("GroupChatFragment", "onCallConnected");

                //手机接上耳机时关闭扬声器  by cuizh,0410
                if (AppManager.HEADSET_PLUG_STATE == 1) {
                    VideoCore.getInstance().enableSpeaker(false);
                }

                //准备环境
                VideoCore.getInstance().prepareMessageEnvironment();
                ivCall.setSelected(true);
                ivSpeak.setVisibility(View.VISIBLE);
                tvTitleInfo.setText("语音对讲中");
                tvTitleInfo.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(AppManager.callName)) {
                    tvTitleInfo.setText(AppManager.callName + " 发起语音对讲");
                }
                if (!mVolumeAnimationDrawable.isRunning()) {
                    mVolumeAnimationDrawable.start();
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallDisConnected(CallDisconnectedEvent event) {
        try{

            if (event != null) {

                ToastUtils.showLocalToast("通话结束");

                LogUtil.i("GroupChatFragment", "onCallDisConnected");
                //通话断开后，遍历设置通话按钮为正常显示
                ivCall.setSelected(false);
                ivSpeak.setVisibility(View.GONE);
                tvTitleInfo.setText("");
                tvTitleInfo.setVisibility(View.GONE);
                tvSeakingName.setVisibility(View.GONE);
                mRlSpeakIcon.setVisibility(View.GONE);
                tvSeakingName.setText("");
                AppManager.callName = "";
                AppManager.speakingName = "";
                if (mVolumeAnimationDrawable.isRunning()) {
                    mVolumeAnimationDrawable.stop();
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    /**
     * 处理说话按钮的效果
     */
    private void handleSpeaker() {
        try{

            mVolumeAnimationDrawable = (AnimationDrawable) mVolume.getDrawable();

            ivSpeak.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    CallMessage callMessage = new CallMessage();
                    callMessage.setCallerSSI(AppManager.getUserDeviceId());
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            LogUtil.i(tag,"有抢话的权限。。。。"+PermissionUtils.UserPermissons.toString());
                            if (PermissionUtils.hasGroupChargePermisson()){
                                LogUtil.i(tag,"有抢话的权限。。。。");
                                RetrofitClient.getInstance().postAsync(BaseEntity.class,
                                        RxConfig.getMethodApiUrl("/api/do/chargeEnable"),
                                        RxMapBuild.created()
                                                .put("Take", LocalCache.getInstance().getConferenceCreator())
                                                .put("ApiToken", AppManager.getApiToken())
                                                .put("UserCode", AppManager.getUserCode())
                                                .build()
                                ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<BaseEntity>() {
                                    @Override
                                    public void onSucessed(BaseEntity baseEntity) {
                                        if (baseEntity.isIsSuccess()){
                                            sendMsgSpeakingEnable(callMessage);
                                            LogUtil.i(tag,"有抢话的权限。。。。可以抢话");
                                        }else {
                                            if (!TextUtils.isEmpty(AppManager.speakingName)){
                                                ToastUtils.showToast("请稍后再讲话...");
                                                return ;
                                            }
                                            LogUtil.i(tag,"有抢话的权限。。。。不能抢话");
                                            sendMsgSpeakingEnable(callMessage);
                                        }

                                    }

                                    @Override
                                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                                        ToastUtils.showToast("网络异常");
                                    }
                                }));

                                return false;
                            }


                            if (!TextUtils.isEmpty(AppManager.speakingName)){
                                ToastUtils.showToast("请稍后再讲话...");
                                return false;
                            }
                            sendMsgSpeakingEnable(callMessage);                        break;
                        case MotionEvent.ACTION_UP:
                            mRlSpeakIcon.setVisibility(View.GONE);
                            callMessage.setHead("PDTMSG");
                            callMessage.setMsgtype("Conference_PttOff");
                            Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(callMessage).toString(), GroupChatFragment.this);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    public boolean onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(getActivity())) {
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    //显示说话人信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallSpeakerEvent(CallSpeakerEvent event) {
        try{

            if (event != null) {
                if (event.getStatu() == 1) {
                    if (!event.getUserCode().equals(AppManager.getUserCode())) {
                        tvSeakingName.setVisibility(View.VISIBLE);
                        tvSeakingName.setText(event.getSpeakerName());
                    } else {
                        tvSeakingName.setVisibility(View.GONE);
                        tvSeakingName.setText("");
                    }

                } else {
                    tvSeakingName.setVisibility(View.GONE);
                    tvSeakingName.setText("");
                }

            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    public String getMsgToCode() {
        return msgToCode;
    }

    /**
     * 通知sip服务器开始讲话了
     * @param callMessage
     */
    private void sendMsgSpeakingEnable(CallMessage callMessage) {
        try{

//        MediaManager.playSound(CoreService.anxiaShound);
            mRlSpeakIcon.setVisibility(View.VISIBLE);
            callMessage.setHead("PDTMSG");
            callMessage.setMsgtype("Conference_PttOn");
            callMessage.setCallerSSI(AppManager.getUserDeviceId());
//                        callMessage.setCalledSSI();
            Message message = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(callMessage).toString(), GroupChatFragment.this);
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

}
