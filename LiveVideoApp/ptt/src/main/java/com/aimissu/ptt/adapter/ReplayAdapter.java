package com.aimissu.ptt.adapter;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.utils.FileUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.entity.event.PersonalLocationEvent;
import com.aimissu.ptt.entity.event.ReplayAudioDetailsEvent;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.ui.ReplayEntity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.view.widget.audio.MediaManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 */
public class ReplayAdapter extends BaseQuickAdapter<ReplayEntity, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private String TAG = "ReplayAdapter";
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    private TimerTask task;
    private Timer timer = new Timer();


    public ReplayAdapter(int layoutResId, @Nullable List<ReplayEntity> data) {
        super(layoutResId, data);
        setOnItemChildClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplayEntity item) {
        helper.addOnClickListener(R.id.iv_download);
        helper.addOnClickListener(R.id.iv_play);
        helper.addOnClickListener(R.id.iv_audioDetail);
        helper.setText(R.id.tv_name, CommonUtils.emptyIfNull(item.getReceiver()));
        helper.setText(R.id.tv_time, CommonUtils.emptyIfNull(item.getSecond()));
        String recodeTime = CommonUtils.emptyIfNull(item.getRecordingTime());
        recodeTime = recodeTime.replace("T","  ");
        if (recodeTime.contains("+08:00")){
            recodeTime = recodeTime.replace("+08:00","");
        }
        helper.setText(R.id.tv_recordTime, "主叫人:"+CommonUtils.emptyIfNull(item.getSender())+"  "+CommonUtils.emptyIfNull(recodeTime));

        LocalMsgFile localMsgFile = com.aimissu.ptt.view.widget.audio.FileUtils.getLocalMsgFileByFileCode(item.getFileCode());
        File localFile = (localMsgFile == null || TextUtils.isEmpty(localMsgFile.localFileUrl)) ? null : new File(localMsgFile.localFileUrl);
        NumberProgressBar downloadProgressBar =  helper.getView(R.id.download_progress_bar);
        //文件存在就隐藏下载图标
        if ( localFile != null && localFile.exists()) {
            helper.getView(R.id.iv_download).setVisibility(View.INVISIBLE);
            downloadProgressBar.setProgress(100);
        }else{
            helper.getView(R.id.iv_download).setVisibility(View.VISIBLE);
            downloadProgressBar.setProgress(0);
        }
//        if(item.getFromType().equals("Group")){
//            helper.getView(R.id.iv_audioDetail).setVisibility(View.VISIBLE);
//        }else {
//            helper.getView(R.id.iv_audioDetail).setVisibility(View.GONE);
//        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ReplayEntity item = getItem(position);
        NumberProgressBar downloadProgressBar = null;
        View viewParent = (View) view.getParent();
        if (viewParent != null && viewParent.getParent() != null) {
            viewParent = ((View) viewParent.getParent());
            downloadProgressBar = viewParent.findViewById(R.id.download_progress_bar);
        }

        if (item == null || downloadProgressBar == null)
            return;
        switch (view.getId()) {
            case R.id.iv_download:
            case R.id.iv_play:
                NumberProgressBar finalDownloadProgressBar = downloadProgressBar;
                new RxPermissions(((FragmentActivity) mContext)).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                playEvent(item, finalDownloadProgressBar,view,position);
                            } else {
                                ToastUtils.showLocalToast("需要sd卡读写权限");
                            }
                        });

                break;

            case R.id.iv_audioDetail:
                EventBus.getDefault().post(new ReplayAudioDetailsEvent(item.getUuid()));
                break;
        }
    }

    private void playEvent(ReplayEntity item, NumberProgressBar downloadProgressBar, View view, int position) {
        LocalMsgFile localMsgFile = com.aimissu.ptt.view.widget.audio.FileUtils.getLocalMsgFileByFileCode(item.getFileCode());
        File localFile = (localMsgFile == null || TextUtils.isEmpty(localMsgFile.localFileUrl)) ? null : new File(localMsgFile.localFileUrl);

        if (localMsgFile == null || localFile == null || !localFile.exists()) {
            String fileDownloadUrl = RxConfig.getBaseApiUrl() + CommonUtils.emptyIfNull(item.getRecordingUrl());
            //开始下载
            if (downloadProgressBar != null) {
                downloadProgressBar.setProgress(0);
                downloadProgressBar.setVisibility(View.VISIBLE);
            }
            String localPath = Configs.FileRoot + CommonUtils.getFileNameFromUrl(fileDownloadUrl);
            com.aimissu.basemvp.net.rx.LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,开始下载fileDownloadUrl:" + fileDownloadUrl);
            NumberProgressBar finalDownloadProgressBar = downloadProgressBar;
            FileDownloader.getImpl().create(fileDownloadUrl)
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
                            if (finalDownloadProgressBar != null) {
                                int percent = ((Float) ((float) soFarBytes * 100 / (float) totalBytes)).intValue();
                                finalDownloadProgressBar.setProgress(percent);
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
                            if (finalDownloadProgressBar != null) {
                                finalDownloadProgressBar.setProgress(100);
                            }
                            com.aimissu.basemvp.net.rx.LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,下载成功localPath:" + localPath);
                            com.aimissu.ptt.view.widget.audio.FileUtils.addFile(new LocalMsgFile.Builder()
//                                            .msgId(CommonUtils.emptyIfNull(msgid).trim())
                                    .localFileUrl(localPath)
//                                            .fileTypee()
                                    .fileDownloadUrll(fileDownloadUrl)
                                    .fileType(FileUtils.getFileExtensionFromUrl(fileDownloadUrl))
                                    .fCode(item.getFileCode())
                                    .build());

                            View playView = null;
                            if(view.getId() == R.id.iv_download){
                                view.setVisibility(View.INVISIBLE);
                                View viewParent = ((View) view.getParent());
                                playView=viewParent.findViewById(R.id.iv_play);
                                playView.setSelected(true);
                            }else if(view.getId() == R.id.iv_play){
                                View viewParent = ((View) view.getParent());
                                viewParent.findViewById(R.id.iv_download).setVisibility(View.INVISIBLE);
                                view.setSelected(true);
                                playView=view;
                            }
                            switchPlayIcon(position);
                            playSound(localPath,playView,downloadProgressBar);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            ToastUtils.showLocalToast("下载失败");
                            com.aimissu.basemvp.net.rx.LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,error:" + e.getMessage());
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                        }
                    }).start();
        } else {
            com.aimissu.basemvp.net.rx.LogUtil.i(TAG, "thread name " + Thread.currentThread().getName() + " ,本地存在 localMsgFile.getLocalFileUrl():" + localMsgFile.getLocalFileUrl());

            if (!view.isSelected()){
                playSound(localMsgFile.getLocalFileUrl(), view, downloadProgressBar);
                view.setSelected(true);
            }else {
                MediaManager.pause();
                view.setSelected(false);
            }
            switchPlayIcon(position);
        }
    }

    /**
     * 同步播放按钮正常显示
     * @param position
     */
    private void switchPlayIcon(int position) {
        for (int i=0;i<getItemCount();i++){
             if (i!=position){
                 if (getViewByPosition(i, R.id.iv_play)!=null){
                     getViewByPosition(i,R.id.iv_play).setSelected(false);
                 }
             }
        }
    }

    public void playSound(String filePath, View playView, NumberProgressBar downloadProgressBar) {
        //如果已经有任务就取消之前的任务
        if (task != null){
            task.cancel();
        }
        downloadProgressBar.setProgress(0);
        if (TextUtils.isEmpty(filePath))
            return;
        if (!new File(filePath).exists()) {
            return;
        }
       int totalDuration = MediaManager.getDuration(filePath);

        task = new TimerTask() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                int CurrentPosition = MediaManager.getCurrentPosition();
                int percent = ((Float) ((float) CurrentPosition * 100 / (float) totalDuration)).intValue();
                Global.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadProgressBar.setProgress(percent);
                    }
                });
            }
        };
        timer.schedule(task,0,100);

        MediaManager.playSound(filePath,new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (playView!=null){
                    playView.setSelected(false);
                   downloadProgressBar.setProgress(100);
                }
            }
        } );
    }
}
