package com.aimissu.ptt.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimissu.basemvp.net.rx.DownloadCallBack;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.im.AudioModel;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.FileModel;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ImageModel;
import com.aimissu.ptt.entity.im.MapModel;
import com.aimissu.ptt.entity.im.TextModel;
import com.aimissu.ptt.entity.im.VideoModel;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.activity.LocationActivity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.ImageUtils;
import com.aimissu.ptt.utils.JsonUtils;
import com.aimissu.basemvp.utils.FileUtils;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.view.widget.audio.MediaManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天适配器
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatAdapter.ChatMultiItemEntity, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {


    private int mMaxItemWidth;
    private int mMinItemWidth;

    public ChatAdapter(List<ChatMultiItemEntity> data) {
        super(data);
        this.addItemType(ChatMultiItemEntity.TEXT_RECEIVE, R.layout.item_receive_txt);
        this.addItemType(ChatMultiItemEntity.IMAGE_RECEIVE, R.layout.item_receive_photo);
        this.addItemType(ChatMultiItemEntity.VIDEO_RECEIVE, R.layout.item_receive_video);
        this.addItemType(ChatMultiItemEntity.AUDIO_RECEIVE, R.layout.item_receive_voice);
//        this.addItemType(ChatMultiItemEntity.AUDIO_RECEIVE, R.layout.item_receive_voice);
        this.addItemType(ChatMultiItemEntity.MAP_RECEIVE, R.layout.item_receive_map);
        this.addItemType(ChatMultiItemEntity.TEXT_SEND, R.layout.item_send_text);
        this.addItemType(ChatMultiItemEntity.IMAGE_SEND, R.layout.item_send_photo);
        this.addItemType(ChatMultiItemEntity.VIDEO_SEND, R.layout.item_send_video);
        this.addItemType(ChatMultiItemEntity.AUDIO_SEND, R.layout.item_send_voice);
        this.addItemType(ChatMultiItemEntity.MAP_SEND, R.layout.item_send_map);
        this.addItemType(ChatMultiItemEntity.FILE_RECEIVE, R.layout.item_receive_file);
        this.addItemType(ChatMultiItemEntity.FILE_SEND, R.layout.item_send_file);
        setOnItemChildClickListener(this);

        getWindowWith();
    }

    private final static Gson mGson = new Gson();

    @Override
    protected void convert(BaseViewHolder helper, ChatMultiItemEntity item) {
        ChatMsg chatMsg = item.getChatEntiy();
        TextModel textModel;
        ImageModel imageModel;
        VideoModel videoModel;
        AudioModel audioModel;
        FileModel fileModel;
        MapModel mapModel;

        if (item.isLocalData) {
            //造假数据
            switch (item.getItemType()) {
                case ChatMultiItemEntity.IMAGE_SEND:
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_photo), helper.getView(R.id.aurora_pb_msgitem_sending));

//                    ImageUtils.loadImage(mContext, chatMsg.getMsgContent(), helper.getView(R.id.aurora_iv_msgitem_photo), helper.getView(R.id.aurora_pb_msgitem_sending));

                   break;
                case ChatMultiItemEntity.TEXT_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setText(R.id.aurora_tv_msgitem_message, CommonUtils.emptyIfNull(chatMsg.getMsgContent()));
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.MAP_SEND:
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.VIDEO_SEND:
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.AUDIO_SEND:
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
                    helper.setText(R.id.aurora_tv_voice_length, item.isPushErr() ? "" : "...");
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.FILE_SEND:
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, AppManager.getUserName());
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, item.isPushErr() ? true : false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, item.isPushErr() ? false : true);
                    helper.setText(R.id.aurora_tv_file_filename, item.isPushErr() ? "" : "...");
//                    ImageUtils.loadImage(mContext, R.drawable.msg_default, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;

            }
        } else {
            helper.addOnClickListener(R.id.aurora_iv_msgitem_avatar);
            if (!TextUtils.isEmpty(chatMsg.getSendUserHeadPortrait())) {
                Glide.with(mContext).load(chatMsg.getSendUserHeadPortrait())
                        .into((ImageView) helper.getView(R.id.aurora_iv_msgitem_avatar));
            } else {
//                ImageUtils.loadImage(mContext, R.drawable.contact, helper.getView(R.id.aurora_iv_msgitem_avatar), null);
            }

//            helper.setText(R.id.aurora_tv_msgitem_date, CommonUtils.formatDateMinute(CommonUtils.strToDate(chatMsg.getMsgTime())));
            helper.setText(R.id.aurora_tv_msgitem_date, CommonUtils.convertData(chatMsg.getMsgTime()));

            LogUtil.i(TAG,"消息时间CommonUtils.convertData(chatMsg.getMsgTime()):"+CommonUtils.convertData(chatMsg.getMsgTime()));
            //造假数据
            switch (item.getItemType()) {
                case ChatMultiItemEntity.AUDIO_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    audioModel = JsonUtils.toModel(chatMsg.getMsgContent(), AudioModel.class);
                    if (audioModel != null) {
                        helper.setText(R.id.aurora_tv_voice_length, formatAudioDuration(audioModel.Duration));
                        setViewWidth(helper.getView(R.id.aurora_tv_msgitem_message), CommonUtils.toInt(audioModel.Duration));
                    }
                    helper.addOnClickListener(R.id.aurora_tv_msgitem_message);
                    break;
                case ChatMultiItemEntity.IMAGE_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    imageModel = JsonUtils.toModel(chatMsg.getMsgContent(), ImageModel.class);
                    if (imageModel != null) {
                        ImageUtils.loadImage(mContext, imageModel.ImageUrl, helper.getView(R.id.aurora_iv_msgitem_photo), null);
                    }
                    helper.addOnClickListener(R.id.aurora_iv_msgitem_photo);
                    break;
                case ChatMultiItemEntity.TEXT_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());
                    textModel = JsonUtils.toModel(chatMsg.getMsgContent(), TextModel.class);
                    helper.setText(R.id.aurora_tv_msgitem_message, textModel == null ? "" : textModel.Text);
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.VIDEO_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    videoModel = JsonUtils.toModel(chatMsg.getMsgContent(), VideoModel.class);
                    if (videoModel != null) {
//                        ImageUtils.loadImage(mContext, videoModel.ShowPicutre, helper.getView(R.id.aurora_iv_msgitem_cover), null);
                        helper.setText(R.id.aurora_tv_duration, formatVideoDuration(videoModel.Duration));
                    }
                    bindVideo(helper.getView(R.id.video_player), helper.getLayoutPosition(), videoModel);
                    break;
                case ChatMultiItemEntity.MAP_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());

                    //修正头像获取不对的问题 by cuizh,0316
//                    ImageUtils.loadImage(mContext,  AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);

                    helper.setText(R.id.aurora_tv_map_location_name, chatMsg.getSendPositionName());
                    helper.setText(R.id.aurora_tv_map_location_district, "");
                    mapModel = JsonUtils.toModel(chatMsg.getMsgContent(), MapModel.class);
                    if (mapModel != null) {
                        ImageUtils.loadImage(mContext, mapModel.ShowPicutre, helper.getView(R.id.aurora_iv_msgitem_map_corver), helper.getView(R.id.aurora_pb_msgitem_sending));
                    }
                    helper.addOnClickListener(R.id.aurora_iv_msgitem_map_corver);
                    break;
                case ChatMultiItemEntity.FILE_RECEIVE:
                    helper.setText(R.id.aurora_tv_msgitem_receiver_display_name, chatMsg.getSendUserName());

                    //修正头像获取不对的问题 by cuizh,0316
//                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    ImageUtils.loadImage(mContext, chatMsg.getSendUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    fileModel = JsonUtils.toModel(chatMsg.getMsgContent(), FileModel.class);
                    if (fileModel != null) {
                        helper.setText(R.id.aurora_tv_file_filename, fileModel.OriginalFileName);
                    }
                    helper.addOnClickListener(R.id.aurora_fl_msgitem_file_container);
                    break;
                case ChatMultiItemEntity.IMAGE_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    imageModel = JsonUtils.toModel(chatMsg.getMsgContent(), ImageModel.class);
                    LogUtil.i(TAG,"imageModel:"+imageModel.toString());
                    if (imageModel != null) {
                        ImageUtils.loadImage(mContext, imageModel.ImageUrl, helper.getView(R.id.aurora_iv_msgitem_photo), helper.getView(R.id.aurora_pb_msgitem_sending));
                    }
                    helper.addOnClickListener(R.id.aurora_iv_msgitem_photo);
                    break;
                case ChatMultiItemEntity.TEXT_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
//                Glide.with(mContext).load("http://img5.imgtn.bdimg.com/it/u=1474146476,1690443177&fm=27&gp=0.jpg")
//                        .into((ImageView) helper.getView(R.id.aurora_iv_msgitem_avatar));
                    textModel = JsonUtils.toModel(chatMsg.getMsgContent(), TextModel.class);
                    helper.setText(R.id.aurora_tv_msgitem_message, textModel == null ? "" : textModel.Text);
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    break;
                case ChatMultiItemEntity.MAP_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);

                    helper.setText(R.id.aurora_tv_map_location_name, chatMsg.getSendPositionName());
                    helper.setText(R.id.aurora_tv_map_location_district, "");
                    mapModel = JsonUtils.toModel(chatMsg.getMsgContent(), MapModel.class);
                    if (mapModel != null) {
                        ImageUtils.loadImage(mContext, mapModel.ShowPicutre, helper.getView(R.id.aurora_iv_msgitem_map_corver), helper.getView(R.id.aurora_pb_msgitem_sending));
                    }
                    helper.addOnClickListener(R.id.aurora_iv_msgitem_map_corver);
                    break;
                case ChatMultiItemEntity.FILE_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);

                    fileModel = JsonUtils.toModel(chatMsg.getMsgContent(), FileModel.class);
                    if (fileModel != null) {
                        helper.setText(R.id.aurora_tv_file_filename, fileModel.OriginalFileName);
                    }
                    helper.addOnClickListener(R.id.aurora_fl_msgitem_file_container);
                    break;
                case ChatMultiItemEntity.VIDEO_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
                    videoModel = JsonUtils.toModel(chatMsg.getMsgContent(), VideoModel.class);
                    if (!TextUtils.isEmpty(item.getLocalVideoDuration())) {
                        helper.setText(R.id.aurora_tv_duration, formatVideoDuration(item.getLocalVideoDuration()));
                    } else if (videoModel != null) {
                        helper.setText(R.id.aurora_tv_duration, formatVideoDuration(videoModel.Duration));
                    }
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);

                    bindVideo(helper.getView(R.id.video_player), helper.getLayoutPosition(), videoModel);


                    break;
                case ChatMultiItemEntity.AUDIO_SEND:
                    helper.setVisible(R.id.aurora_ib_msgitem_resend, false);
                    helper.setVisible(R.id.aurora_pb_msgitem_sending, false);
                    helper.setText(R.id.aurora_tv_msgitem_sender_display_name, chatMsg.getSendUserName());
                    ImageUtils.loadImage(mContext, AppManager.getUserHeadPortrait(), helper.getView(R.id.aurora_iv_msgitem_avatar), null);
                    audioModel = JsonUtils.toModel(chatMsg.getMsgContent(), AudioModel.class);
                    if (audioModel != null) {
                        helper.setText(R.id.aurora_tv_voice_length, formatAudioDuration(audioModel.Duration));
                        setViewWidth(helper.getView(R.id.aurora_tv_msgitem_message), CommonUtils.toInt(audioModel.Duration));
                    }
                    helper.addOnClickListener(R.id.aurora_tv_msgitem_message);

                    break;

            }
        }


        try {
            Date date = CommonUtils.strToDate(chatMsg.getMsgTime());
            ChatMultiItemEntity preItem = getItem(helper.getAdapterPosition() + 1);
            Date datePre = preItem == null ? null : CommonUtils.strToDate(preItem.getChatEntiy().getMsgTime());
            if (preItem != null) {
                long dateMillSecconds = date == null ? item.getLocalCreateTime() : date.getTime();
                long datePreMillSecconds = datePre == null ? preItem.getLocalCreateTime() : datePre.getTime();
                LogUtil.i(TAG,"dateMillSecconds : "+dateMillSecconds+"dateMillSecconds : "+dateMillSecconds+"(dateMillSecconds - datePreMillSecconds) / 1000 : "+(dateMillSecconds - datePreMillSecconds) / 1000 );
                if ((dateMillSecconds - datePreMillSecconds) / 1000 > 60 * 10) {
                    helper.getView(R.id.aurora_tv_msgitem_date).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.aurora_tv_msgitem_date).setVisibility(View.GONE);
                }
            } else {
                helper.getView(R.id.aurora_tv_msgitem_date).setVisibility(View.VISIBLE);
            }
            TextView textView = helper.getView(R.id.aurora_tv_msgitem_date);
            if (TextUtils.isEmpty(textView.getText().toString())) {
                textView.setVisibility(View.GONE);
            }


        } catch (Exception ex) {
        }
    }

    private void setViewWidth(View view, int duration) {
        // 根据时间动态显示语音背景长度
        ViewGroup.LayoutParams textParams = (ViewGroup.LayoutParams) view.getLayoutParams();
        textParams.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * (duration / 1000 * 2));

        if (textParams.width > mMaxItemWidth) {
            textParams.width = mMaxItemWidth;
        }
        view.requestLayout();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        ChatMsg chatMsg = null;
        if (chatMultiItemEntity != null) {
            chatMsg = chatMultiItemEntity.getChatEntiy();
        }
        switch (view.getId()) {
            case R.id.aurora_fl_msgitem_file_container:
                //文件浏览
                NumberProgressBar downloadProgressBar = view.findViewById(R.id.download_progress_bar);
                if (chatMultiItemEntity != null) {
                    final String msgid = chatMsg.getMsgId();
                    LogUtil.i("hexiang", "thread name " + Thread.currentThread().getName() + " ,chatMsg.getMsgFile():" + chatMsg.getMsgFile());
                    if (chatMsg != null) {
                        FileModel fileModel = JsonUtils.toModel(chatMsg.getMsgContent(), FileModel.class);
                        if (fileModel != null) {

                            LocalMsgFile localMsgFile = com.aimissu.ptt.view.widget.audio.FileUtils.getLocalMsgFileByFileCode(fileModel.FileCode);

                            File localFile = (localMsgFile == null || TextUtils.isEmpty(localMsgFile.localFileUrl)) ? null : new File(localMsgFile.localFileUrl);
                            if (localMsgFile == null || localFile == null || !localFile.exists()) {
                                //开始下载
                                if (downloadProgressBar != null) {
                                    downloadProgressBar.setProgress(0);
                                    downloadProgressBar.setVisibility(View.VISIBLE);
                                }
                                String localPath = Configs.FileRoot + CommonUtils.getFileNameFromUrl(fileModel.FielDownLoadUrl);
                                ChatMsg finalChatMsg = chatMsg;
                                FileDownloader.getImpl().create(fileModel.FielDownLoadUrl)
                                        .setPath(localPath)
                                        .setListener(new FileDownloadListener() {
                                            @Override
                                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            }

                                            @Override
                                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                            }

                                            @Override
                                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                                if (downloadProgressBar != null) {
                                                    int percent = ((Float) ((float) soFarBytes * 100 / (float) totalBytes)).intValue();
                                                    downloadProgressBar.setProgress(percent);
                                                }
                                            }

                                            @Override
                                            protected void blockComplete(BaseDownloadTask task) {
                                            }

                                            @Override
                                            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                                            }

                                            @Override
                                            protected void completed(BaseDownloadTask task) {
                                                if (downloadProgressBar != null) {
                                                    downloadProgressBar.setProgress(100);
                                                }
                                                LogUtil.i("hexiang", "thread name " + Thread.currentThread().getName() + " ,下载成功localPath:" + localPath);
                                                com.aimissu.ptt.view.widget.audio.FileUtils.addFile(new LocalMsgFile.Builder()
                                                        .msgId(CommonUtils.emptyIfNull(msgid).trim())
                                                        .localFileUrl(localPath)
                                                        .fileType(finalChatMsg.getMsgType())
                                                        .fileDownloadUrll(fileModel == null ? "" : fileModel.FielDownLoadUrl)
                                                        .fileType(finalChatMsg.getMsgType())
                                                        .fCode(fileModel.FileCode)
                                                        .build());
                                                FileUtils.openFile(mContext, new File(localPath));
                                            }

                                            @Override
                                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            }

                                            @Override
                                            protected void error(BaseDownloadTask task, Throwable e) {
                                            }

                                            @Override
                                            protected void warn(BaseDownloadTask task) {
                                            }
                                        }).start();
                            } else {
                                //打开本地文件
                                LogUtil.i("hexiang", "thread name " + Thread.currentThread().getName() + " ,本地存在 localMsgFile.getLocalFileUrl():" + localMsgFile.getLocalFileUrl());
                                FileUtils.openFile(mContext, new File(localMsgFile.getLocalFileUrl()));


//                                ToastUtils.showLocalToast("打开本地文件:"+localMsgFile.getLocalFileUrl());
                            }
                        } else {
                            //发送本地文件

                        }
                    }
                }


                break;
            case R.id.aurora_iv_msgitem_cover:
                //视频播放
                if (chatMsg != null) {
                    VideoModel videoModel = JsonUtils.toModel(chatMsg.getMsgContent(), VideoModel.class);
                    if (videoModel != null) {
                        ActivityLuanch.viewVideoPlay(view.getContext(), videoModel);
                    }
                }

                break;
            case R.id.aurora_iv_msgitem_photo:
                new ImageViewer.Builder<ImageModel>(view.getContext(), getChatImages(chatMsg == null ? "" : chatMsg.getMsgId()))
                        .setFormatter(new ImageViewer.Formatter<ImageModel>() {
                            @Override
                            public String format(ImageModel imageModel) {
                                return imageModel.ImageUrl;
                            }
                        })
                        .show();
                break;
            case R.id.aurora_tv_msgitem_message:
                //语音播放
                View viewParent = (View) view.getParent();
                View viewAnimation = null;
                if (viewParent != null) {
                    viewAnimation = viewParent.findViewById(R.id.aurora_iv_msgitem_voice_anim);
                }

                if (chatMultiItemEntity != null) {
                    final String msgid = chatMsg.getMsgId();
                    LogUtil.i("hexiang", "thread name " + Thread.currentThread().getName() + " ,msgid:" + msgid + ",new MSgid:" + new String(chatMsg.getMsgId()) + ",getMsgid:" + chatMsg.getMsgId());
                    if (chatMsg != null) {
                        AudioModel audioModel = JsonUtils.toModel(chatMsg.getMsgContent(), AudioModel.class);
                        if (audioModel != null) {

                            LocalMsgFile localMsgFile = com.aimissu.ptt.view.widget.audio.FileUtils.getLocalMsgFileByFileCode(audioModel.FileCode);

                            File audioLocalFile = (localMsgFile == null || TextUtils.isEmpty(localMsgFile.localFileUrl)) ? null : new File(localMsgFile.localFileUrl);
                            if (localMsgFile == null || audioLocalFile == null || !audioLocalFile.exists()) {


                                //开始下载
                                View finalViewAnimation = viewAnimation;
                                RetrofitClient.getInstance()
                                        .download(null, view.getContext(), audioModel.AudioUrl, Configs.AudioRoot + CommonUtils.getFileNameFromUrl(audioModel.AudioUrl), null, null, new DownloadCallBack<ChatMsg>(chatMsg) {
                                            @Override
                                            public void onError(Throwable e) {
                                                LogUtil.i("hexiang", "下载失败 msg:" + e.getMessage());
                                            }

                                            @Override
                                            public void onSucess(String path, String name, long fileSize) {
                                                ChatMsg chatMsg = getData();
                                                if (chatMsg != null) {
                                                    AudioModel audioModel = JsonUtils.toModel(chatMsg.getMsgContent(), AudioModel.class);
                                                    LogUtil.i("hexiang", "下载成功 local path:" + path);
                                                    LogUtil.i("hexiang", "下载成功 thread name " + Thread.currentThread().getName() + " ,msgid:" + msgid + ",new MSgid:" + new String(chatMsg.getMsgId()) + ",getMsgid:" + chatMsg.getMsgId());
                                                    com.aimissu.ptt.view.widget.audio.FileUtils.addFile(new LocalMsgFile.Builder()
                                                            .duration(String.valueOf(CommonUtils.toInt(audioModel == null ? "" : audioModel.Duration)))
                                                            .msgId(CommonUtils.emptyIfNull(getData().getMsgId()).trim())
                                                            .localFileUrl(path)
                                                            .fileType(getData().getMsgType())
                                                            .fileDownloadUrll(audioModel == null ? "" : audioModel.AudioUrl)
                                                            .fileType(chatMsg.getMsgType())
                                                            .fCode(audioModel.FileCode)
                                                            .build());
                                                    playSound(path, finalViewAnimation);

                                                }

                                            }
                                        });

                            } else {
                                //播放历史记录
                                LogUtil.i("hexiang", "已下载播放历史记录 local path:" + localMsgFile.getLocalFileUrl());
                                playSound(localMsgFile.getLocalFileUrl(), viewAnimation);
                            }


                        } else {
                            playSound(chatMultiItemEntity.getLocalAudioUrl(), viewAnimation);
                        }
                    } else {
                        //自己本地发送的
                        playSound(chatMultiItemEntity.getLocalAudioUrl(), viewAnimation);
                    }


                }


                break;
            case R.id.aurora_iv_msgitem_map_corver:

                Intent intent = new Intent(Global.mContext, LocationActivity.class);
                intent.putExtra(Configs.LATITDUE, chatMsg.getSendUserLatitude());
                intent.putExtra(Configs.LONGITUDE, chatMsg.getSendUserLongitude());
                intent.putExtra(Configs.USERNAME, chatMsg.getSendUserName());
                intent.putExtra(Configs.USERCODE, chatMsg.getSendUserCode());
                mContext.startActivity(intent);

                break;
            case R.id.aurora_iv_msgitem_avatar:
                PersonUserEntity personUserEntity = AppManager.getPersonUserEntity(chatMsg.getSendUserCode());
                if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode()) && !personUserEntity.getuCode().equals(AppManager.getUserCode())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                    bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                    bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                    bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                    PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                }
                break;
        }
    }

    public static class ChatMultiItemEntity implements MultiItemEntity {

        public static final int IMAGE_RECEIVE = 0;
        public static final int TEXT_RECEIVE = 1;
        public static final int AUDIO_RECEIVE = 3;
        public static final int VIDEO_RECEIVE = 4;
        public static final int MAP_RECEIVE = 5;
        public static final int FILE_RECEIVE = 6;

        public static final int IMAGE_SEND = 11;
        public static final int TEXT_SEND = 12;
        public static final int AUDIO_SEND = 13;
        public static final int VIDEO_SEND = 14;
        public static final int MAP_SEND = 15;
        public static final int FILE_SEND = 16;

        /**
         * 本地id
         */
        private String localChatId;
        /**
         * 是否是本地数据
         */
        private boolean isLocalData = false;

        /**
         * true 表示推送失败 ,false 表示正在推送
         */
        private boolean isPushErr = false;

        /**
         * 本地发送视频的首帧图片
         */
        public String localVideoFirstFramePicUrl;
        /**
         * 本地发送视频时常
         */
        public String localVideoDuration;

        /**
         * 本地语音播放地址
         */
        public String localAudioUrl;

        public Long localCreateTime;

        public Long getLocalCreateTime() {
            return localCreateTime == null ? 0 : localCreateTime;
        }

        public void setLocalCreateTime(Long localCreateTime) {
            this.localCreateTime = localCreateTime;
        }

        public String getLocalVideoFirstFramePicUrl() {
            return localVideoFirstFramePicUrl;
        }

        public void setLocalVideoFirstFramePicUrl(String localVideoFirstFramePicUrl) {
            this.localVideoFirstFramePicUrl = localVideoFirstFramePicUrl;
        }

        public String getLocalVideoDuration() {
            return localVideoDuration;
        }

        public void setLocalVideoDuration(String localVideoDuration) {
            this.localVideoDuration = localVideoDuration;
        }

        public String getLocalAudioUrl() {
            return localAudioUrl;
        }

        public void setLocalAudioUrl(String localAudioUrl) {
            this.localAudioUrl = localAudioUrl;
        }

        public ChatMultiItemEntity(ChatMsg chatEntiy) {
            this.chatEntiy = chatEntiy;
        }

        public ChatMultiItemEntity(int msgType, ChatMsg chatEntiy) {
            this.msgType = msgType;
            this.chatEntiy = chatEntiy;
        }

        private int msgType = -1;


        private ChatMsg chatEntiy;

        public ChatMultiItemEntity(int msgType) {
            this.msgType = msgType;
        }

        @Override
        public int getItemType() {
            return this.msgType;
        }

        public ChatMsg getChatEntiy() {
            return chatEntiy;
        }

        public String getLocalChatId() {
            return localChatId;
        }

        public void setLocalChatId(String localChatId) {
            this.localChatId = localChatId;
        }

        public boolean isLocalData() {
            return isLocalData;
        }

        public void setLocalData(boolean localData) {
            isLocalData = localData;
        }

        public boolean isPushErr() {
            return isPushErr;
        }

        public void setPushErr(boolean pushErr) {
            isPushErr = pushErr;
        }

        public void setChatEntiy(ChatMsg chatEntiy) {
            this.chatEntiy = chatEntiy;
        }
    }

    public List<ChatMultiItemEntity> buildItemEntity(List<ChatMsg> chatMsgs) {
        if (chatMsgs == null) {
            return null;
        }
        List<ChatAdapter.ChatMultiItemEntity> chatMultiItemEntities = new ArrayList<>();
        for (ChatMsg chatMsg : chatMsgs) {
            ChatMultiItemEntity chatMultiItemEntity = buildItemEntity(chatMsg);
            if (chatMultiItemEntity != null) {
                chatMultiItemEntities.add(buildItemEntity(chatMsg));
            }

        }
        return chatMultiItemEntities;
    }

    public ChatMultiItemEntity buildItemEntity(ChatMsg chatMsg) {
        if (chatMsg == null) {
            return null;
        }
        boolean isSelf = (TextUtils.isEmpty(chatMsg.getSendUserCode())) ? false : chatMsg.getSendUserCode().trim().equals(AppManager.getLoginName());
        return buildItemEntity(chatMsg, isSelf);
    }

    /**
     * 构建个本地发送的实体
     *
     * @param
     * @return
     */
    public ChatMultiItemEntity buildLocalItemEntity(ChatMsg chatMsg, String localChatId) {

        ChatMultiItemEntity chatMultiItemEntity = buildItemEntity(chatMsg, true);
        chatMultiItemEntity.setLocalChatId(localChatId);
        chatMultiItemEntity.setLocalData(true);
        chatMultiItemEntity.setLocalCreateTime(System.currentTimeMillis());
        return chatMultiItemEntity;
    }

    public ChatMultiItemEntity buildItemEntity(ChatMsg chatMsg, boolean isSelf) {

        ChatAdapter.ChatMultiItemEntity chatMultiItemEntity = null;

        if (IMType.MsgType.Text.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatAdapter.ChatMultiItemEntity.TEXT_SEND : ChatAdapter.ChatMultiItemEntity.TEXT_RECEIVE, chatMsg);
        } else if (IMType.MsgType.Image.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatAdapter.ChatMultiItemEntity.IMAGE_SEND : ChatAdapter.ChatMultiItemEntity.IMAGE_RECEIVE, chatMsg);
        } else if (IMType.MsgType.Map.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatAdapter.ChatMultiItemEntity.MAP_SEND : ChatAdapter.ChatMultiItemEntity.MAP_RECEIVE, chatMsg);
        } else if (IMType.MsgType.Video.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatAdapter.ChatMultiItemEntity.VIDEO_SEND : ChatAdapter.ChatMultiItemEntity.VIDEO_RECEIVE, chatMsg);
        } else if (IMType.MsgType.Audio.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatAdapter.ChatMultiItemEntity.AUDIO_SEND : ChatAdapter.ChatMultiItemEntity.AUDIO_RECEIVE, chatMsg);
        } else if (IMType.MsgType.File.toString().equals(chatMsg.getMsgType())) {
            chatMultiItemEntity = new ChatAdapter.ChatMultiItemEntity(isSelf ? ChatMultiItemEntity.FILE_SEND : ChatMultiItemEntity.FILE_RECEIVE, chatMsg);
        }
        return chatMultiItemEntity;
    }


    public String getCurrentLastMsgId() {
        if (getData() == null || getData().size() <= 0) {
            return null;
        }
        ChatMsg chatMsg = getItem(getItemCount()).getChatEntiy();
        LogUtil.i("RxLog", "chat smg:" + chatMsg.getMsgContent());
        return chatMsg == null ? null : chatMsg.getMsgId();
    }

    public void addNewMsg(ChatMsg chatMsg) {
        if (!isChatMsgExist(chatMsg)) {
            ChatMultiItemEntity chatMultiItemEntity = buildItemEntity(chatMsg);
            if (chatMultiItemEntity != null) {
                addData(0, chatMultiItemEntity);
            }
        }
    }

    public void addNewLocalMsg(String localChatId, String msg, @NotNull IMType.MsgType msgType) {
        if (!isChatLocalMsgExist(localChatId)) {
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setMsgType(msgType.toString());
            chatMsg.setMsgContent(msg);
            ChatMultiItemEntity chatMultiItemEntity = buildLocalItemEntity(chatMsg, localChatId);
            if (chatMultiItemEntity != null) {
                addData(0, chatMultiItemEntity);
            }
        }
    }

    public boolean isChatMsgExist(ChatMsg chatMsg) {
        if (getData() != null) {
            for (ChatMultiItemEntity chatMultiItemEntity : getData()) {
                if (chatMultiItemEntity.getChatEntiy() != null) {
                    if (chatMsg.getMsgId().equals(chatMultiItemEntity.getChatEntiy().getMsgId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isChatLocalMsgExist(String lcoalChatMsgId) {
        if (TextUtils.isEmpty(lcoalChatMsgId)) {
            return false;
        }
        if (getData() != null) {
            for (ChatMultiItemEntity chatMultiItemEntity : getData()) {
                if (chatMultiItemEntity.getChatEntiy() != null) {
                    if (lcoalChatMsgId.equals(chatMultiItemEntity.getLocalChatId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public int getPositionByLocalChatId(String localChatId) {
        if (getData() != null && !TextUtils.isEmpty(localChatId)) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i) != null && localChatId.equals(getData().get(i).getLocalChatId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ChatMultiItemEntity getItemByLocalChatId(String localChatId) {
        int position = getPositionByLocalChatId(localChatId);
        return getItem(position);
    }


    /**
     * 设置推送消息成功,失败
     *
     * @param localChatId
     */
    public void setPushMsgFailed(String localChatId) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalData(true);
            chatMultiItemEntity.setPushErr(true);
            notifyItemChanged(position);
        }
    }

    /**
     * 设置正在推送
     *
     * @param localChatId
     */
    public void setPushMsgLoading(String localChatId) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalData(true);
            chatMultiItemEntity.setPushErr(false);
            notifyItemChanged(position);
        }
    }

    public void setPushMsgSuccessed(String localChatId, ChatMsg chatMsg) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalData(false);
            chatMultiItemEntity.setChatEntiy(chatMsg);
            notifyItemChanged(position);
        }
    }

    public void setSendVideoFirstFramePicUrl(String localChatId, String picUrl) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalVideoFirstFramePicUrl(picUrl);
            notifyItemChanged(position);
        }
    }

    public void setSendVideoDuration(String localChatId, String duration) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalVideoDuration(duration);
            notifyItemChanged(position);
        }
    }

    public void setSendAudioUrl(String localChatId, String audioUrl) {
        int position = getPositionByLocalChatId(localChatId);
        ChatMultiItemEntity chatMultiItemEntity = getItem(position);
        if (chatMultiItemEntity != null) {
            chatMultiItemEntity.setLocalAudioUrl(audioUrl);
            notifyItemChanged(position);
        }
    }

    public String formatVideoDuration(String durationMillSeconds) {
        String result = "0:0";
        if (TextUtils.isEmpty(durationMillSeconds)) {
            return result;
        }
        int duration = CommonUtils.toInt(durationMillSeconds);
        if (duration > 0) {
            int second = duration / 1000 % 60;
            int minute = duration / 1000 / 60;
            return (minute < 10 ? "0" + minute : String.valueOf(minute)) + ":" + (second < 10 ? "0" + second : String.valueOf(second));
        } else {
            return result;
        }
    }

    public String formatAudioDuration(String durationMillSeconds) {
        return String.valueOf((int) CommonUtils.toInt(durationMillSeconds) / 1000) + "'";
    }

    public void onDestory() {
        MediaManager.release();
    }

    private AnimationDrawable mLastAnimationDrawable;
    private String mLastFilePath;
    public void playSound(String filePath, View anim) {

        if (mLastAnimationDrawable!=null && mLastAnimationDrawable.isRunning()){
            mLastAnimationDrawable.stop();
//            MediaManager.pause();
        }

        if (!TextUtils.isEmpty(mLastFilePath) && mLastFilePath.equals(filePath)){
            mLastAnimationDrawable.stop();
            MediaManager.pause();
            mLastFilePath="";
            return;
        }



        if (TextUtils.isEmpty(filePath))
            return;
        if (!new File(filePath).exists()) {
            return;
        }
        if (anim != null) {
            AnimationDrawable animationDrawable = (AnimationDrawable) anim.getBackground();
            if (animationDrawable != null) {
                animationDrawable.start();
            }
            MediaManager.playSound(filePath, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (animationDrawable != null) {
                        animationDrawable.stop();
                    }
                }
            });

            mLastAnimationDrawable = animationDrawable;
            mLastFilePath = filePath;
        }
    }

    public List<ImageModel> getChatImages(String msgId) {
        List<ImageModel> imageModels = new ArrayList<>();
        if (getData() != null) {
            for (ChatMultiItemEntity chatMultiItemEntity : getData()) {
                if (chatMultiItemEntity.getChatEntiy() != null) {
                    ImageModel imageModel = JsonUtils.toModel(chatMultiItemEntity.getChatEntiy().getMsgContent(), ImageModel.class);
                    if (imageModel != null && !TextUtils.isEmpty(imageModel.ImageUrl)) {
                        if (!TextUtils.isEmpty(msgId) && msgId.trim().equals(CommonUtils.emptyIfNull(chatMultiItemEntity.getChatEntiy().getMsgId()))) {
                            imageModels.add(0, imageModel);
                        } else {
                            imageModels.add(imageModel);
                        }
                    }
                }
            }
        }
        return imageModels;
    }

    private void bindVideo(StandardGSYVideoPlayer gsyVideoPlayer, int position, VideoModel videoModel) {
        if (videoModel == null)
            return;
        int width = mContext.getResources().getDimensionPixelSize(R.dimen.im_image_width);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1)
                                .placeholder(R.drawable.msg_default)
                                .override(width, width)
                                .centerCrop())
                .load(videoModel.VideoUrl)
                .into(imageView);
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder
                .setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(videoModel.VideoUrl)
                .setCacheWithPlay(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);

        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(mContext, false, true);
            }
        });
    }

    /**
     * 获取屏幕的宽度
     */
    private void getWindowWith() {
        // 获取屏幕的宽度
        WindowManager wm = (WindowManager) Global.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.65f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
    }
}
