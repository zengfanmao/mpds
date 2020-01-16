package com.aimissu.ptt.view.widget.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.aimissu.basemvp.net.rx.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 */
public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private static String tag = "MediaManager";
    private static AudioTrack audioTrack;
    private static int minBufferSize;


    public static void playSound(String filePath){
        playSound(filePath,null);

    }
    /**
     * 播放音乐
     *
     * @param filePath
     * @param onCompletionListener
     */
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if(TextUtils.isEmpty(filePath))
            return;
        if(!new File(filePath).exists()){
            return;
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
            LogUtil.i(tag, "mMediaPlayer.pause()");
        }
    }

    public static int getDuration(String filePath) {

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();

            return mMediaPlayer.getDuration();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getCurrentPosition(){
        if (mMediaPlayer!=null){
            return mMediaPlayer.getCurrentPosition();
        }
       return 0;
    }

    /**
     * 当前是isPause状态
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            LogUtil.i(tag, " mMediaPlayer.release()");
        }
    }

}
