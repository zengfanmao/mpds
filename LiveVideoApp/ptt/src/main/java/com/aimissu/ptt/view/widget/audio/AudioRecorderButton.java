package com.aimissu.ptt.view.widget.audio;

import android.Manifest;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.utils.Configs;
import com.czt.mp3recorder.MP3Recorder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;


/**
 * 自定义录音的button
 */
public class AudioRecorderButton extends android.support.v7.widget.AppCompatButton {
    private static final int DISTANCE_Y_CANCEL = 50; // 正常开发情况下,应该使用dp,然后将dp转换为px
    private static final int STATE_NORMAL = 1; // 默认状态
    private static final int STATE_RECORDING = 2; // 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3; // 希望取消

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;
    private static final int TIME_OUT = 0X113;
    private static final int REMAININGTIME = 0X114;

    private int mCurState = STATE_NORMAL; // 当前的状态
    // 已经开始录音
    private boolean isRecording = false; // 是否已经开始录音
    private DialogManager mDialogManger;
    //    private AudioUtils mAudioManger;
    private String tag = AudioRecorderButton.class.getSimpleName();
    //     private AudioRecordFunc mRecord_1;
    private MP3Recorder mRecorder;
    private float mTime;
    // 是否触发longClick方法
    private boolean mReady;


    public String generateFileName() {
        return System.currentTimeMillis() + ".mp3";
    }

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDialogManger = new DialogManager(getContext());
        // TODO 判断SD卡是否存在
        mRecorder = new MP3Recorder(new File(Configs.AudioRoot, generateFileName()));
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new RxPermissions(((FragmentActivity) context)).request(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe((Boolean granted) -> {
                            if (granted) {
                                mReady = true;
                                startRecord();
                            } else {
                                ToastUtils.showLocalToast("需要录音和sd卡权限");
                            }
                        });
                return false;
            }
        });
    }

    private AudioFinishRecorderListener mListener;

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath, long mStartTime,
                      long mEndTime);
    }

    public void setAudioFinishRecorderListener(
            AudioFinishRecorderListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取语音大小的Runnable
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    handler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mTime >= 50) {
                    handler.sendEmptyMessage(REMAININGTIME);
                }

                if (mTime >= 60) {
                    handler.sendEmptyMessage(TIME_OUT);
                }
            }
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示应该在audio end prepared以后
                    mDialogManger.showRecordingDialog();
                    isRecording = true;

                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManger.updateVoiceLevel(getVoiceLevel(7));


                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManger.dimissDialog();
                    break;
                case TIME_OUT:
                    mEndTime = System.currentTimeMillis();
                    // 正常录制的时候结束
                    mDialogManger.dimissDialog();
                    mRecorder.stop();
                    if (mListener != null) {
                        mListener.onFinish(mTime, getFilePath(), mStartTime, mEndTime);
                        LogUtil.i(tag, "语音发送了");
                    }
                    reset();

                    break;

                case REMAININGTIME:
                    mDialogManger.setRemainingTimeVisible();
                    if (mTime <= 50) {
                        mDialogManger.setRemainingTimeText(10);
                    } else if (mTime <= 51) {
                        mDialogManger.setRemainingTimeText(9);
                    } else if (mTime <= 52) {
                        mDialogManger.setRemainingTimeText(8);
                    } else if (mTime <= 53) {
                        mDialogManger.setRemainingTimeText(7);
                    } else if (mTime <= 54) {
                        mDialogManger.setRemainingTimeText(6);
                    } else if (mTime <= 55) {
                        mDialogManger.setRemainingTimeText(5);
                    } else if (mTime <= 56) {
                        mDialogManger.setRemainingTimeText(4);
                    } else if (mTime <= 57) {
                        mDialogManger.setRemainingTimeText(3);
                    } else if (mTime <= 58) {
                        mDialogManger.setRemainingTimeText(2);
                    } else if (mTime <= 59) {
                        mDialogManger.setRemainingTimeText(1);
                    } else if (mTime <= 60) {
                        mDialogManger.setRemainingTimeText(0);
                    }
                    break;
            }
        }
    };
    private long mStartTime;
    private long mEndTime;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartTime = System.currentTimeMillis();
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    // 根据x,y的坐标,判断是否取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                mEndTime = System.currentTimeMillis();
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || mTime < 0.6f) {
                    mDialogManger.tooShort();
                    mRecorder.stop();

                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                } else if (mCurState == STATE_RECORDING) { // 正常录制的时候结束
                    mDialogManger.dimissDialog();
                    mRecorder.stop();
                    if (mListener != null) {
                        mListener.onFinish(mTime, getFilePath(), mStartTime, mEndTime);
                    }
                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    mDialogManger.dimissDialog();
                    mRecorder.stop();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
        // 超过按钮的宽度
        if (x < 0 || x > getWidth()) {
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    /**
     * 改变Button的文本以及背景色
     *
     * @param state
     */
    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setText(R.string.str_recorder_recorder);
                    if (isRecording) {
                        mDialogManger.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManger.wantToCancel();
                    break;
            }
        }
    }

    public void startRecord() {
        try {
            mRecorder.setRecordFile(new File(Configs.AudioRoot, generateFileName()));
            mRecorder.start();
            handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecrod() {
        try {
            mRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        if (mRecorder == null || mRecorder.getRecordFile() == null) {
            return "";
        }
        return mRecorder.getRecordFile().getPath();
    }

    private int getVoiceLevel(int maxLevel) {

        try {
            //mMediaRecorder.getMaxAmplitude() 范围:1-32767之间
            float level = 2000 / maxLevel;
            int voice = (int) (mRecorder.getRealVolume() / level);

            if (voice <= 0) {
                voice = 1;
            } else if (voice > 7) {
                voice = 7;
            }
            LogUtil.i(tag, "mAudioRecord. getRealVolume()音量值 " + mRecorder.getRealVolume() + ".getVolume()音量值:" + mRecorder.getVolume() + ".level:" + level + ",voice:" + voice);
            LogUtil.i(tag, "音量值:" + voice);
            return voice;
        } catch (Exception e) {

        }

        return 1;
    }
}
