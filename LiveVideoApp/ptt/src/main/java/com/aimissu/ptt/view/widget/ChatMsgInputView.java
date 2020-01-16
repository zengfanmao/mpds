package com.aimissu.ptt.view.widget;

import android.Manifest;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.view.IChatInputView;
import com.aimissu.ptt.view.widget.audio.AudioRecorderButton;
import com.grgbanking.video.utils.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 */
public class ChatMsgInputView extends FrameLayout {

    private InputMethodManager inputMethodManager;
    public ViewHolder viewHolder;

    public ChatMsgInputView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ChatMsgInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ChatMsgInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    public void initView(Context context, AttributeSet attrs, int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.chat_input_layout, this);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(this);
        }
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatMsgInputView);
        if (typedArray != null) {
            typedArray.recycle();
        }

        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (viewHolder.liChatInputMoreContainer.getVisibility() == View.VISIBLE) {
                    Rect r = new Rect();
                    getRootView().getWindowVisibleDisplayFrame(r);
                    int screenHeight = getRootView().getHeight();
                    int heightDiff = screenHeight - (r.bottom - r.top);
                    if (heightDiff > 100) {
                        int realKeyboardHeight = heightDiff - getStatueBarHeight();
                        if (realKeyboardHeight > 150) {
                            viewHolder.liChatInputMoreContainer.setVisibility(View.GONE);
                            LogUtil.i("softkeybord", "heiight:" + realKeyboardHeight);
                        }
                    }
                }

            }
        });

        viewHolder.btnPressToSpeak.setAudioFinishRecorderListener((seconds, filePath, mStartTime, mEndTime) -> {
            chatMsgInputClickEvent.sendAudio(null,filePath,String.valueOf(seconds));
        });

    }

    public class ViewHolder implements IChatInputView {
        @BindView(R.id.li_chat_input_container)
        LinearLayout liChatInputContainer;
        @BindView(R.id.btn_press_to_speak)
        public AudioRecorderButton btnPressToSpeak;
        @BindView(R.id.et_msg)
        public EditText etMsg;
        @BindView(R.id.btn_voice)
        public Button btnVoice;
        @BindView(R.id.btn_send)
        Button btnSend;
        @BindView(R.id.btn_more)
        Button btnMore;
        @BindView(R.id.btn_edit)
        public Button btnEdit;
        @BindView(R.id.li_chat_input_More_container)
        public LinearLayout liChatInputMoreContainer;
        @BindView(R.id.ll_video)
        public LinearLayout ivPickerVideo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnTextChanged(R.id.et_msg)
        void onTextChanged(CharSequence text) {
            if (TextUtils.isEmpty(etMsg.getText())) {
                //隐藏发送按钮
                hideSendView();
            } else {
                //显示发送按钮
                showSendView();
            }
        }


        @OnClick({R.id.btn_voice, R.id.btn_more, R.id.btn_send, R.id.btn_edit, R.id.li_picker_photo, R.id.li_picker_takephoto, R.id.li_picker_video, R.id.li_picker_location,R.id.li_picker_file})
        void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_voice:
                    //切换语音
                    //隐藏软键盘
                    changeToVoice();
                    break;
                case R.id.btn_edit:
                    //切换文字输入
                    //弹出软键盘
                    changeToText();
                    break;
                case R.id.btn_more:
                    //更多面板打开
                    toogleMore();
                    break;
                case R.id.btn_send:
                    //消息发送
//                    ToastUtils.showLocalToast("消息发送");
                    if (chatMsgInputClickEvent != null) {
                        //限制短消息长度  by cuizh,0507
                        if (etMsg.getText().length() > 23) {
                            ToastUtils.showLocalToast("发送短消息请不要超过23个字");
                            return;
                        }
                        chatMsgInputClickEvent.sendMsg(etMsg.getText().toString());
                    }
                    break;
                case R.id.li_picker_photo:
                    //选择图片

                    if (chatMsgInputClickEvent != null) {
                        chatMsgInputClickEvent.sendPhoto(view);
                    }
                    break;
                case R.id.li_picker_takephoto:
                    //拍照
                    if (chatMsgInputClickEvent != null) {
                        chatMsgInputClickEvent.sendTakePhoto(view);
                    }


                    break;
                case R.id.li_picker_video:
                    //拍视频
                    if (chatMsgInputClickEvent != null) {
                        chatMsgInputClickEvent.sendVideo(view);
                    }
                    break;
                case R.id.li_picker_location:
                    //定位
                    if (chatMsgInputClickEvent != null) {
                        chatMsgInputClickEvent.sendMap(view);
                    }
                    break;
                case R.id.li_picker_file:
                    if (chatMsgInputClickEvent != null) {
                        chatMsgInputClickEvent.sendFile(view);
                    }
                    break;

            }
        }

        @Override
        public void hideSendView() {
            btnMore.setEnabled(true);
            btnSend.setEnabled(false);
            btnSend.setVisibility(View.INVISIBLE);
            btnMore.setVisibility(View.VISIBLE);
        }

        @Override
        public void showSendView() {
            btnMore.setEnabled(false);
            btnSend.setEnabled(true);
            btnSend.setVisibility(View.VISIBLE);
            btnMore.setVisibility(View.INVISIBLE);

        }

        @Override
        public void toogleMore() {
            if (View.VISIBLE == liChatInputMoreContainer.getVisibility()) {
                //隐藏更多内容
                liChatInputMoreContainer.setVisibility(View.GONE);
            } else {
                //显示跟多内容
                Global.hideInputMethod(etMsg);
                liChatInputMoreContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liChatInputMoreContainer.setVisibility(View.VISIBLE);
                        btnEdit.setEnabled(true);
                        btnVoice.setEnabled(false);
                        btnVoice.setVisibility(View.INVISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                    }
                }, 100);

            }

        }

        @Override
        public void changeToVoice() {
            liChatInputMoreContainer.setVisibility(View.GONE);
            Global.hideInputMethod(etMsg);
            etMsg.setVisibility(View.GONE);
            btnPressToSpeak.setVisibility(View.VISIBLE);
            btnEdit.setEnabled(true);
            btnVoice.setEnabled(false);
            btnVoice.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

        }

        @Override
        public void changeToText() {
            etMsg.setVisibility(View.VISIBLE);
            btnPressToSpeak.setVisibility(View.GONE);
//            etMsg.setFocusable(true);
//            Global.showInputMethod(etMsg);
            btnEdit.setEnabled(false);
            btnVoice.setEnabled(true);
            btnVoice.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);

        }
    }


    int statusBarHeight = -1;
    private int getStatueBarHeight() {
        if (statusBarHeight < 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public void setHideTouchView(@NonNull View view) {
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (inputMethodManager.isActive()) {
                    Global.hideInputMethod(viewHolder.etMsg);
                }
                if (viewHolder.liChatInputMoreContainer.getVisibility() == View.VISIBLE) {
                    viewHolder.liChatInputMoreContainer.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (inputMethodManager.isActive()) {
                Global.hideInputMethod(viewHolder.etMsg);
            }
        }
    }

    public interface ChatMsgInputClickEvent {
        void sendMsg(String msg);

        void sendPhoto(View view);

        void sendTakePhoto(View view);

        void sendVideo(View view);

        void sendAudio(View view,String filePath,String duration);

        void sendMap(View view);

        void sendFile(View view);
    }

    private ChatMsgInputClickEvent chatMsgInputClickEvent;

    public ChatMsgInputClickEvent getChatMsgInputClickEvent() {
        return chatMsgInputClickEvent;
    }

    public void setChatMsgInputClickEvent(ChatMsgInputClickEvent chatMsgInputClickEvent) {
        this.chatMsgInputClickEvent = chatMsgInputClickEvent;
    }

    public void clearMsg() {
        viewHolder.etMsg.setText("");
    }


    public void toogleMore() {
        if (viewHolder != null) {
            viewHolder.toogleMore();
        }
    }

    public void hideMore(){
        if(viewHolder!=null){
            if (View.VISIBLE == viewHolder.liChatInputMoreContainer.getVisibility()) {
                //隐藏更多内容
                viewHolder.liChatInputMoreContainer.setVisibility(View.GONE);
            }
        }
    }
}
