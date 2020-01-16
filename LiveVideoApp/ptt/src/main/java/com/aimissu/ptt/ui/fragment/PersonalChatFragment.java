package com.aimissu.ptt.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.basemvp.view.GridSpacingItemDecoration;
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.ChatAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.call.CallConnectedEvent;
import com.aimissu.ptt.entity.call.CallDisconnectedEvent;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.event.PersonalCallEvent;
import com.aimissu.ptt.entity.event.ReceiverVideoEvent;
import com.aimissu.ptt.entity.event.SendMapEvent;
import com.aimissu.ptt.entity.event.WaitingClickEvent;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ReceieveMsgEvent;
import com.aimissu.ptt.entity.sipMessage.CallMessage;
import com.aimissu.ptt.entity.sipMessage.SipConferenceClose;
import com.aimissu.ptt.entity.sipMessage.SipMessageToPdt;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.presenter.ChatPresenter;
import com.aimissu.ptt.presenter.IChatPresenter;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.activity.LoginActivity;
import com.aimissu.ptt.ui.activity.MainActivity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.LoginSharedPreferences;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.IChatView;
import com.aimissu.ptt.view.widget.ChatMsgInputView;
import com.grgbanking.video.Message;
import com.grgbanking.video.VideoCallFragment;
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

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * 个人聊天
 */
public class PersonalChatFragment extends BaseMvpFragment<IChatPresenter> implements IChatView, OnRefreshListener, ChatMsgInputView.ChatMsgInputClickEvent, Message.StateListener {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.tv_person_title)
    TextView tvPersonTitle;

    @BindView(R.id.chat_msg_input_view)
    ChatMsgInputView chatMsgInputView;

    private String msgToCode;
    private String userType;
    private ChatAdapter mChatAdapter;
    @BindView(R.id.smartrefreshlaout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_speak_icon)
    LinearLayout mRlSpeakIcon;
    @BindView(R.id.iv_volume)
    ImageView mVolume;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.li_picker_video)
    ImageView li_picker_video;
    @BindView(R.id.li_picker_video_text)
    TextView li_picker_video_text;
    @BindView(R.id.iv_speak)
    ImageView ivSpeak;
    @BindView(R.id.fl_suface)
    FrameLayout mFl_suface;

    private String tag = "PersonalChatFragment";

    private final static int DEFAULT_PAGESIZE = 20;
    private final static int DEFAULT_PAGEINDEX = 0;
    private AnimationDrawable mVolumeAnimationDrawable;
    private VideoCallFragment mVideoCallFragment;
    private boolean videoEnable = false;
    private String deviceId;
    private String mConferenceCreator;
    private String mConferencename;

    private AlertDialog.Builder dialogBuilder;
    private Dialog dialog;

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
                msgToCode = getArguments().getString(IMType.Params.MSG_TO_CODE, "");
                userType = getArguments().getString(IMType.Params.USER_TYPE, "");
                deviceId = getArguments().getString(IMType.Params.DEVICE_ID, "");
                mConferenceCreator = getArguments().getString(IMType.Params.CONFERENCE_CREATOR, "");
                if (!TextUtils.isEmpty(mConferenceCreator)){
                    AppManager.conferenceCreator = mConferenceCreator;
                }
                mConferencename = getArguments().getString(IMType.Params.CONFERENCE_NAME, "");

                //个人聊天页面上标题显示部门+名称 by cuizh,0402
                tvPersonTitle.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.USER_DEPARTMENT, ""))
                        +" "
                        + CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_PERSONAL_TITLE, "")));

                isAppUserCallConnected();

                LogUtil.i(tag, "msgToCode : " + msgToCode +"  deviceId : " + deviceId +"  userType : " + userType + "   mConferenceCreator:" + mConferenceCreator);
            }
            tvTile.setText("");
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

            isPDTuser();
            setFl_videoSuface();
            handleSpeaker();

            dialogBuilder = new AlertDialog.Builder(getActivity());
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * app用户通话中
     */
    private void isAppUserCallConnected() {
        try{
            ivCall.setSelected(false);

            //手机接上耳机时关闭扬声器  by cuizh,0410
            if (AppManager.HEADSET_PLUG_STATE == 1) {
                VideoCore.getInstance().enableSpeaker(false);
            }

            if (VideoCore.getLc().getCurrentCall() != null && userType.equals(IMType.Params.TYPE_APP)) {
                String remoteUserName = VideoCore.getLc().getCurrentCall().getRemoteAddress().getUserName();
                videoEnable = getArguments().getBoolean(IMType.Params.VIDEO_CALL);
                if (msgToCode.equals(remoteUserName)) {
                    LogUtil.i(tag, "建立连接了remoteUserName:" + remoteUserName + "    msgToCode:" + msgToCode);
                    ivCall.setSelected(true);
                    if (videoEnable) {
                        showVideoCall();
                        tvPersonTitle.setText("与 " + msgToCode + " 视频对讲中");
                    } else {
                        tvPersonTitle.setText("与 " + msgToCode + " 语音对讲中");
                    }
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private void isPDTuser() {

        try{

            if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_PDT)) {
                chatMsgInputView.viewHolder.changeToText();
                chatMsgInputView.viewHolder.showSendView();
                chatMsgInputView.viewHolder.btnEdit.setVisibility(View.VISIBLE);
                chatMsgInputView.viewHolder.btnVoice.setVisibility(View.INVISIBLE);
                chatMsgInputView.viewHolder.etMsg.setVisibility(View.VISIBLE);
                chatMsgInputView.viewHolder.btnPressToSpeak.setVisibility(View.GONE);

                chatMsgInputView.viewHolder.etMsg.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                   if (!TextUtils.isEmpty(charSequence)){
//                       chatMsgInputView.viewHolder.showSendView();
//                   }
                        chatMsgInputView.viewHolder.showSendView();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if (VideoCore.getInstance().haveCall()){
                    String remoteUserName = VideoCore.getLc().getCurrentCall().getRemoteAddress().getUserName();
                }


            } else {
//            chatMsgInputView.viewHolder.changeToVoice();
//            chatMsgInputView.viewHolder.s();
                chatMsgInputView.setHideTouchView(recyclerView);
                if (TextUtils.isEmpty(chatMsgInputView.viewHolder.etMsg.getText())) {
                    chatMsgInputView.viewHolder.hideSendView();
                }
                chatMsgInputView.viewHolder.btnEdit.setVisibility(View.INVISIBLE);
                chatMsgInputView.viewHolder.btnVoice.setVisibility(View.VISIBLE);
                chatMsgInputView.viewHolder.etMsg.setVisibility(View.VISIBLE);
                chatMsgInputView.viewHolder.btnPressToSpeak.setVisibility(View.INVISIBLE);
                chatMsgInputView.viewHolder.etMsg.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        chatMsgInputView.viewHolder.showSendView();
                        if (TextUtils.isEmpty(charSequence)) {
                            //隐藏发送按钮
                            chatMsgInputView.viewHolder.hideSendView();
                        } else {
                            //显示发送按钮
                            chatMsgInputView.viewHolder.showSendView();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    protected IChatPresenter createPresenter() {
        return new ChatPresenter(this);
    }

    private int pageFromId = PageConfig.PAGE_TWO;
    private int pageFromPosition = PageConfig.PAGE_ID_PERSONAL;

    @Override
    public void onHiddenChanged(boolean hidden) {
        try{

            super.onHiddenChanged(hidden);
            chatMsgInputView.onHiddenChanged(hidden);
            LogUtil.i(tag, "hidden:" + hidden);
            if (!hidden) {
                if (getArguments() != null) {
                    userType = getArguments().getString(IMType.Params.USER_TYPE);
                    String code = getArguments().getString(IMType.Params.MSG_TO_CODE);
                    deviceId = getArguments().getString(IMType.Params.DEVICE_ID);
                    pageFromId = getArguments().getInt(IMType.Params.PAGE_FROM_ID, PageConfig.PAGE_TWO);
                    mConferenceCreator = getArguments().getString(IMType.Params.CONFERENCE_CREATOR, "");
                    if (!TextUtils.isEmpty(mConferenceCreator)){
                        AppManager.conferenceCreator = mConferenceCreator;
                    }

                    pageFromPosition = getArguments().getInt(IMType.Params.PAGE_FROM_POSITION, PageConfig.PAGE_ID_PERSONAL);
                    tvPersonTitle.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.USER_DEPARTMENT, ""))
                            +" "
                            + CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_PERSONAL_TITLE, "")));
                    if (!TextUtils.isEmpty(code) && !msgToCode.equals(code)) {
                        msgToCode = code;
                        mChatAdapter.setNewData(null);
                        getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, null, false);
                    }

                    isAppUserCallConnected();
                    isPDTuser();
                    LogUtil.i(tag, "userType : " + userType + "  msgToCode:" + msgToCode);
                    String remoteUserName = "";
                    String ysRemoteUserName="";
                    if (VideoCore.getInstance().haveCall()) {
                        remoteUserName = VideoCore.getLc().getCurrentCall().getRemoteAddress().getUserName();
                        ysRemoteUserName = remoteUserName;
                        if (remoteUserName.contains("_") && remoteUserName.contains("-")) {
                            remoteUserName = remoteUserName.substring(remoteUserName.indexOf("_") + 1, remoteUserName.indexOf("-"));
                        }
                        LogUtil.i(tag, "VideoCore.getInstance().haveCall() ...... remoteUserName:   " + remoteUserName);
                    }
                    LogUtil.i(tag, "AppManager.conferenceCreator:   " + AppManager.conferenceCreator + "    msgToCode:  " + msgToCode);
                    if (VideoCore.getInstance().haveCall() && !ysRemoteUserName.contains("group_")&&
                            (CommonUtils.emptyIfNull(AppManager.conferenceCreator).equals(msgToCode) || CommonUtils.emptyIfNull(remoteUserName).equals(msgToCode))) {
                        LogUtil.i(tag, "ivCall.setSelected(true)......");
                        ivCall.setSelected(true);
                        if (userType.equals(IMType.Params.TYPE_PDT)) {
                            ivSpeak.setVisibility(View.VISIBLE);

                            if (!mVolumeAnimationDrawable.isRunning()) {
                                mVolumeAnimationDrawable.start();
                            }
                            tvPersonTitle.setText("与 " + msgToCode + " 语音对讲中");
                        } else {
                            if (videoEnable) {
                                showVideoCall();
                                tvPersonTitle.setText("与 " + msgToCode + " 视频对讲中");

                            } else {
                                tvPersonTitle.setText("与 " + msgToCode + " 语音对讲中");
                            }
                        }
                    } else {
                        LogUtil.i(tag, "没有通话中····");
                        ivSpeak.setVisibility(View.GONE);
                        ivCall.setSelected(false);
                    }
                }
            } else {
                Global.hideInputMethod(chatMsgInputView);
            }
//        chatMsgInputView.toogleMore();
            chatMsgInputView.viewHolder.liChatInputMoreContainer.setVisibility(View.GONE);
            LogUtil.i(tag, "getPersonUserEntity(msgToCode) : " + AppManager.getPersonUserEntity(msgToCode));
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    public void getData(Integer skip, Integer take, String firstMsgId, boolean isLoadMore) {

        try{

            mPresenter.getMyChatDetails(isLoadMore, String.valueOf(skip), String.valueOf(take), firstMsgId, IMType.MsgFromType.Person.toString(), AppManager.getLoginName(), msgToCode);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }


    @Override
    public void getMyChatDetailsSuccessed(boolean isLoadMore, DataChatMsg model) {

        try{
            smartRefreshLayout.finishRefresh();
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
                if (IMType.MsgFromType.Person.toString().equals(event.getChatMsg().getMsgFromType())) {
                    //个人消息
                    LogUtil.i(tag, "PersonalChatFragment person ReceieveMsgEvent :msgToCode :   " + msgToCode);
                    LogUtil.i(tag, "PersonalChatFragment person ReceieveMsgEvent :event.getChatMsg() :   " + event.getChatMsg());
                    if (msgToCode.equals(event.getChatMsg().getSendUserCode())) {
                        mChatAdapter.addNewMsg(event.getChatMsg());
                        recyclerView.smoothScrollToPosition(0);
                    }
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
            } else {
                getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, mChatAdapter.getCurrentLastMsgId(), false);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void onBack() {
        PageUtils.turnPage(pageFromPosition, pageFromId, true);
        Global.hideInputMethod(ivCall);
    }

    @OnClick({R.id.li_back, R.id.iv_call, R.id.iv_speak})
    void OnClick(View view) {

        try{

            switch (view.getId()) {
                case R.id.li_back:
                    onBack();
                    break;
                case R.id.iv_call:

                    if (VideoCore.getLc().getCurrentCall() != null && !view.isSelected()) {
                        ToastUtils.showToast("您已经处于通话");
                        return;
                    }
                    if (view.isSelected()) {
//                    if (!PermissionUtils.hasPersonCutPermisson()) {
//                        ToastUtils.showToast("您没有中断个呼权限");
//                        return;
//                    }
                        SipConferenceClose sipConferenceClose = new SipConferenceClose();
                        sipConferenceClose.setCallerSSI(AppManager.getUserDeviceId());
                        //发送消息给视频服务器关闭整个组呼
                        sipConferenceClose.setHead("PDTMSG");
                        sipConferenceClose.setMsgtype("Conference_Close");
                        sipConferenceClose.setCallid(deviceId);
                        if (!TextUtils.isEmpty(mConferencename)) {
                            sipConferenceClose.setCallid(mConferencename);
                        }
                        Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(sipConferenceClose).toString(), PersonalChatFragment.this);

                        ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                        VideoCore.getInstance().terminateCall();

                        view.setSelected(false);
                    } else {

                        if (!PermissionUtils.hasPersonCallPermisson()) {
                            ToastUtils.showToast("您没有个呼权限");
                            return;
                        }

                        if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_APP)) {
                            VideoCore.getInstance().newOutgoingCall("call_" + msgToCode, false);

                            ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                            ToastUtils.showLocalToast("正在发起呼叫");
                            showDialog(false);

                            LogUtil.i(tag,"");
//                        VideoCore.getInstance().newOutgoingCall(msgToCode, false);
                        } else if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_PDT)) {
                            AppManager.personPdtCall = true;
                            LogUtil.i(tag, "deviceId: " + deviceId);
                            VideoCore.getInstance().newOutgoingCall("person_" + deviceId + "-" + AppManager.getUserDeviceId(), false);
                        }
                        videoEnable = false;
                        view.setSelected(true);
                    }
                    break;
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    public void pushMsg(final IMPamras imPamras) {

        try{

            imPamras.setUserCode(AppManager.getLoginName());
            imPamras.setMsgFromType(IMType.MsgFromType.Person.toString());
            imPamras.setMsgToCode(msgToCode);
            mPresenter.pushMsg(imPamras);

            if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_PDT)) {
                SipMessageToPdt mpdt = new SipMessageToPdt();
                mpdt.setCallerSSI(AppManager.getUserDeviceId());
                mpdt.setHead("PDTMSG");
                mpdt.setMsgtype("Conference_SendMsg");
                mpdt.setCalledSSI(deviceId);
                mpdt.setMulticast("0");
                mpdt.setMsg(imPamras.getMsgContent());
                if (!VideoCore.getInstance().haveCall()) {
                    VideoCore.getInstance().prepareMessageEnvironmentWithOutCall(deviceId);
                }
                Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(mpdt).toString(), PersonalChatFragment.this);
                LogUtil.i(tag, "deviceId:"+deviceId+"   发送的sip消息message1:" + message1);
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void pushMsgSuccessed(DataPushMsg model, final IMPamras imPamras) {

        try{

            chatMsgInputView.clearMsg();
            hideProgressDialog();
            if (!TextUtils.isEmpty(imPamras.localChatId)) {
                mChatAdapter.setPushMsgSuccessed(imPamras.localChatId, model.getEntity());
                recyclerView.smoothScrollToPosition(0);
            } else {
                mChatAdapter.addNewMsg(model.getEntity());
                recyclerView.smoothScrollToPosition(0);
            }

//        isPDTuser();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void pushMsgFailed(String msg, final IMPamras imPamras) {
        try{
            hideProgressDialog();
            if (!TextUtils.isEmpty(imPamras.localChatId)) {
                mChatAdapter.setPushMsgFailed(imPamras.localChatId);
            }

//        isPDTuser();
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void uploadFileSuccessed(final DataPostFile file, final IMPamras imPamras) {
        try{
            //上传成功开始发送消息
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

        try{

            if (imPamras.msgType != null) {
                switch (imPamras.msgType) {
                    case Audio:
                        if (!TextUtils.isEmpty(imPamras.localChatId)) {
                            mChatAdapter.setPushMsgFailed(imPamras.localChatId);
                        }
                        break;
                }
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void sendMsg(String msg) {

        try{

            //showProgressDialog();
            if (!TextUtils.isEmpty(msg)) {
                String localChatId = String.valueOf(System.currentTimeMillis());
                mChatAdapter.addNewLocalMsg(localChatId, msg, IMType.MsgType.Text);
                recyclerView.smoothScrollToPosition(0);
                pushMsg(new IMPamras.Builder().msgType(IMType.MsgType.Text)
                        .msgContent(msg)
                        .localChatId(localChatId)
                        .build());
                chatMsgInputView.clearMsg();
//        isPDTuser();
            }
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
                    .maxSelectNum(1)
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
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void sendVideo(View view) {

        try{
            //拍视频
            // 录制

            if (VideoCore.getLc().getCurrentCall() != null && !view.isSelected()) {
                ToastUtils.showToast("您已经处于通话");
                return;
            }

            if (!PermissionUtils.hasPersonCallPermisson()) {
                ToastUtils.showToast("您没有个呼权限");
                return;
            }

            li_picker_video.setSelected(true);
            li_picker_video_text.setText("挂断通话");

            new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {

                            if (!VideoCore.getInstance().haveCall()) {
                                videoEnable = true;
                                VideoCore.getInstance().newOutgoingCall("call_"+msgToCode+"-"+AppManager.getUserDeviceId(), true);
                                ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                                ToastUtils.showLocalToast("正在发起视频通话...");
                                showDialog(true);

                            } else {
                                VideoCore.getInstance().terminateCall();
                                ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                            }
                        } else {
                            ToastUtils.showLocalToast(getActivity(), "需要录音、相机权限才能使用", ToastUtils.Duration.LONG);
                        }
                    });

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

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
//                            mChatAdapter.addNewLocalMsg(localChatId, imageItem.getPath(), IMType.MsgType.Image);
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

            LogUtil.i(tag,"个人聊天收到小视频·····");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        chatMsgInputView.hideMore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallConnected(CallConnectedEvent event) {

        try{

//        if (event != null && event.isPersonCall()) {
            if (event != null) {
                LogUtil.i(tag, "onCallConnected");

                //手机接上耳机时关闭扬声器  by cuizh,0410
                if (AppManager.HEADSET_PLUG_STATE == 1) {
                    VideoCore.getInstance().enableSpeaker(false);
                }

                //准备环境
                ivCall.setSelected(true);
//            EventBus.getDefault().post(new PersonalCallEvent(msgToCode));
                VideoCore.getInstance().prepareMessageEnvironment();
                if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_PDT)) {
                    ivSpeak.setVisibility(View.VISIBLE);

                    if (!mVolumeAnimationDrawable.isRunning()) {
                        mVolumeAnimationDrawable.start();
                    }
                    tvPersonTitle.setText("与 " + msgToCode + " 语音对讲中");
                } else {
                    if (videoEnable) {
                        showVideoCall();

                        tvPersonTitle.setText("与 " + msgToCode + " 视频对讲中");

                    } else {
                        tvPersonTitle.setText("与 " + msgToCode + " 语音对讲中");
                    }
                }

                dismissDialog();

                if (!TextUtils.isEmpty(mConferenceCreator)){
                    AppManager.conferenceCreator = mConferenceCreator;
                }
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    /**
     * 显示视频
     */
    private void showVideoCall() {

        try{

            li_picker_video.setSelected(true);
            li_picker_video_text.setText("挂断通话");

            mFl_suface.setVisibility(View.VISIBLE);

            if (getActivity().findViewById(R.id.rl_videoSuface) != null) {

                //检查相机权限  by cuizh,0418
                new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {

                                mVideoCallFragment = new VideoCallFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.rl_videoSuface, mVideoCallFragment);
                                try {
                                    transaction.commitAllowingStateLoss();
//                                    transaction.commit();
                                } catch (Exception e) {
                                    ToastUtils.showLocalToast("transaction.commitAllowingStateLoss() failed");
                                    LogHelper.sendErrorLog(e);
                                }

                            } else {

                                ToastUtils.showLocalToast("需要重新获取相机权限");
                            }
                        });
            }else {
                ToastUtils.showLocalToast("rl_videoSuface is null");
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 移除通话的fragment
     */
    private void removeFragment() {

        try{

            mFl_suface.setVisibility(View.GONE);
            if (mVideoCallFragment != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(mVideoCallFragment).commitAllowingStateLoss();
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallDisConnected(CallDisconnectedEvent event) {

        try{

            if (event != null) {

                dismissDialog();

                ToastUtils.showLocalToast("通话结束");

                LogUtil.i(tag, "onCallDisConnected...");
                //通话断开后，遍历设置通话按钮为正常显示
                ivCall.setSelected(false);

                li_picker_video.setSelected(false);
                li_picker_video_text.setText("视频通话");

                ivSpeak.setVisibility(View.GONE);
                mRlSpeakIcon.setVisibility(View.GONE);
                EventBus.getDefault().post(new PersonalCallEvent(""));
                if (!TextUtils.isEmpty(userType) && userType.equals(IMType.Params.TYPE_APP)) {
                    removeFragment();
                }
                if (mVolumeAnimationDrawable.isRunning()) {
                    mVolumeAnimationDrawable.stop();
                }
                tvPersonTitle.setText(CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.USER_DEPARTMENT, ""))
                        +" "
                        + CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_PERSONAL_TITLE, "")));
            }

            videoEnable = false;
            AppManager.personPdtCall = false;
            AppManager.conferenceCreator="";
//        mConferenceCreator=null;
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
                            mRlSpeakIcon.setVisibility(View.VISIBLE);
                            callMessage.setHead("PDTMSG");
                            callMessage.setMsgtype("Conference_PttOn");
                            Message message = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(callMessage).toString(), PersonalChatFragment.this);
                            break;
                        case MotionEvent.ACTION_UP:
                            mRlSpeakIcon.setVisibility(View.GONE);
                            callMessage.setHead("PDTMSG");
                            callMessage.setMsgtype("Conference_PttOff");
                            Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(callMessage).toString(), PersonalChatFragment.this);
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


    @Override
    public void stateChanged(Message message, String s) {

    }

    private View content;
    private int screenWidth;
    private int screenHeight;
    private boolean hasMeasured;
    private int downX;
    private int downY;
    private boolean clickormove;
    private long mCurTime;
    private long mLastTime;

    /**
     * 设置suface 拖拽事件的处理
     */
    public void setFl_videoSuface() {

        try{

            LogUtil.i(tag, "设置suface 拖拽事件的处理·····");
            content = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View

            DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;


            ViewTreeObserver vto = content.getViewTreeObserver();//获取ViewTree的监听器
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {

                    // TODO Auto-generated method stub
                    if (!hasMeasured) {

                        screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
                        hasMeasured = true;//设置为true，使其不再被测量。

                    }
                    return true;//如果返回false，界面将为空。

                }

            });
            screenHeight = screenHeight - Global.dp2px(100);

            mFl_suface.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间

                int lastX, lastY; // 记录移动的最后的位置

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // TODO Auto-generated method stub
                    int ea = event.getAction();//获取事件类型
                    switch (ea) {
                        case MotionEvent.ACTION_DOWN: // 按下事件


                            if (setDoubleClickListener()) return true;

                            LogUtil.i(tag, "按下suface·····");
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                            downX = lastX;
                            downY = lastY;
                            break;

                        case MotionEvent.ACTION_MOVE: // 拖动事件

                            LogUtil.i(tag, "拖动suface·····");
                            // 移动中动态设置位置
                            int dx = (int) event.getRawX() - lastX;//位移量X
                            int dy = (int) event.getRawY() - lastY;//位移量Y
                            int left = v.getLeft() + dx;
                            int top = v.getTop() + dy;
                            int right = v.getRight() + dx;
                            int bottom = v.getBottom() + dy;

                            //++限定按钮被拖动的范围
                            if (left < 0) {

                                left = 0;
                                right = left + v.getWidth();

                            }
                            if (right > screenWidth) {

                                right = screenWidth;
                                left = right - v.getWidth();

                            }
                            if (top < 0) {
                                top = 0;
                                bottom = top + v.getHeight();

                            }
                            if (bottom > screenHeight - Global.dp2px(100)) {

                                bottom = screenHeight - Global.dp2px(100);
                                top = bottom - v.getHeight();

                            }
                            //--限定按钮被拖动的范围
                            v.layout(left, top, right, bottom);//按钮重画

                            // 记录当前的位置
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                            break;

                        case MotionEvent.ACTION_UP: // 弹起事件

                            LogUtil.i(tag, "弹起suface·····");
                            //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件

                            if (Math.abs((int) (event.getRawX() - downX)) > 5
                                    || Math.abs((int) (event.getRawY() - downY)) > 5)
                                clickormove = false;
                            else
                                clickormove = true;

                            break;
                    }
                    return true;
                }

            });

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 双击切换大小
     *
     * @return
     */
    private boolean setDoubleClickListener() {

        try{

            mLastTime = mCurTime;
            mCurTime = System.currentTimeMillis();

            if (mCurTime - mLastTime < 500) {
                //双击切换大小
                setmFl_videoSufaceLayoutLarger();
                return true;
            }
            return false;
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
        return false;
    }

    /**
     * 设置全屏合适最小
     */
    private void setmFl_videoSufaceLayoutLarger() {

        try{

            if (mVideoCallFragment != null) {
                SurfaceView captureView = mVideoCallFragment.getCaptureView();
                RelativeLayout.LayoutParams captureViewlp = (RelativeLayout.LayoutParams) captureView.getLayoutParams();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFl_suface.getLayoutParams();
                if (lp.width == RelativeLayout.LayoutParams.MATCH_PARENT) {
                    lp.width = Global.dp2px(100);
                    lp.height = Global.dp2px(100);
                    captureViewlp.width = Global.dp2px(1);
                    captureViewlp.height = Global.dp2px(1);

                } else {
//            mVideoCallFragment.setVideoFullscreenShow();
                    lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//            lp.height = Global.dp2px(250);

                    captureViewlp.width = Global.dp2px(120);
                    captureViewlp.height = Global.dp2px(145);

                }
                captureView.requestLayout();
                mFl_suface.requestLayout();
            }
        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    public String getMsgToCode() {
        return msgToCode;
    }


    /**
     * 呼叫时显示dialog  by cuizh,0422
     * @param isVideoCall
     */
    private void showDialog(Boolean isVideoCall){
        if (isVideoCall) {
            dialogBuilder.setTitle("正在向 "
                    + CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_PERSONAL_TITLE, ""))
                    + " 发起视频呼叫...\n");
        }else {
            dialogBuilder.setTitle("正在向 "
                    + CommonUtils.emptyIfNull(getArguments().getString(IMType.Params.MSG_PERSONAL_TITLE, ""))
                    + " 发起语音呼叫...\n");
        }
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VideoCore.getInstance().terminateCall();
                dialogInterface.dismiss();
            }
        });
        dialog = dialogBuilder.show();
    }

    private void dismissDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
