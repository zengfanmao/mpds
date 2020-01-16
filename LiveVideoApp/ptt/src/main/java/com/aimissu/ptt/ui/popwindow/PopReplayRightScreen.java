package com.aimissu.ptt.ui.popwindow;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.event.ReplayScreenEvent;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.ReplayScreenCondition;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.utils.CommonUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */

public class PopReplayRightScreen extends PopupWindow {
    private final static String TAG = PopReplayRightScreen.class.getSimpleName();
    private boolean _CanMiss = true;
    private View mMenuView;
    WeakReference<View> viewWeakReference;
    private static ReplayScreenCondition mDataBillClassify;

    private static TimePickerView pvTime;

    public static int choise = 0;
    public static final int CHOISE_START = 1;
    public static final int CHOISE_END = 2;


    public void setCanMiss(boolean canMiss) {
        _CanMiss = canMiss;
    }

    private WeakReference<Context> mContext;
    private static ViewHolder mViewHolder;

    public PopReplayRightScreen(final Context context, ReplayScreenCondition dataBillClassify) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        initBaserView(context, dataBillClassify);
    }


    private void initBaserView(Context context, ReplayScreenCondition dataBillClassify) {
        mContext = new WeakReference<Context>(context);
        mDataBillClassify = dataBillClassify;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.replay_pop_screen_right, null);
        this.setContentView(mMenuView);

        this.setWidth(ScreenUtils.getScreenWidth(context) * 2 / 3);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimList);
        mViewHolder = new ViewHolder(mMenuView);
        mViewHolder.etRname.setText(dataBillClassify.getrName());
        mMenuView.setOnTouchListener(onTouchListener);
        if (mViewHolder != null && dataBillClassify != null) {
//            mViewHolder.etRname.setText(dataBillClassify.getrName());
            mViewHolder.etStartime.setText(formatShowDate(dataBillClassify.getStartTime()));
            mViewHolder.etEndtime.setText(formatShowDate(dataBillClassify.getEndTime()));
        }
        //时间选择器
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
        endDate.set(2040, 11, 31);

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (choise == CHOISE_START) {

                    mDataBillClassify.setStartTime(CommonUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
                    mViewHolder.etStartime.setText(mDataBillClassify.getStartTime());

                } else if (choise == CHOISE_END) {
                    mDataBillClassify.setEndTime(CommonUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
                    mViewHolder.etEndtime.setText(mDataBillClassify.getEndTime());

                }

            }
        }).setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("时间选择")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(ContextCompat.getColor(context, R.color.white))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(context, R.color.white))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(context, R.color.white))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(context, R.color.common_focus))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(context, R.color.window_background))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
//                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
    }


    private String formatShowDate(String date) {
        String result = "";
        if (TextUtils.isEmpty(date) || date.length() < 19)
            return result;
        try {
            return date.substring(0, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    class ViewHolder {
        @BindView(R.id.li_container)
        LinearLayout liContainer;
        @BindView(R.id.et_rname)
        EditText etRname;
        @BindView(R.id.et_startime)
        TextView etStartime;
        @BindView(R.id.et_endtime)
        TextView etEndtime;
        @BindView(R.id.name_title)
        TextView etNameTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            if (mDataBillClassify.getType() == ReplayScreenCondition.TYPE_GROUP) {
                etNameTitle.setText("请选择常用组或直接输入组号:");
            } else {
                etNameTitle.setText("主叫人");

            }
        }

        @OnClick({R.id.btn_action, R.id.et_startime, R.id.et_endtime, R.id.btn_name})
        void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            switch (view.getId()) {
                case R.id.btn_action:
                    dismiss();
                    EventBus.getDefault().post(new ReplayScreenEvent(new ReplayScreenCondition(mViewHolder.etRname.getText().toString(), mViewHolder.etStartime.getText().toString(), mViewHolder.etEndtime.getText().toString(), mDataBillClassify.getType())));

                    break;
                case R.id.et_startime:
                    choise = CHOISE_START;
                    if (pvTime != null) {
                        pvTime.setTitleText("开始时间");
                        LogUtil.i(TAG, "。。。。。mDataBillClassify:" + mDataBillClassify.getStartTime());
                        if (mDataBillClassify != null && !TextUtils.isEmpty(mDataBillClassify.getStartTime())) {
                            Date date = CommonUtils.strToDate(mDataBillClassify.getStartTime());
                            if (date != null) {
                                calendar.setTime(date);
                            }
                            pvTime.setDate(calendar);
                        }

                        pvTime.show();
                    }
                    break;
                case R.id.et_endtime:
                    choise = CHOISE_END;
                    if (pvTime != null) {
                        pvTime.setTitleText("结束时间");
                        if (mDataBillClassify != null && !TextUtils.isEmpty(mDataBillClassify.getEndTime())) {
                            Date date = CommonUtils.strToDate(mDataBillClassify.getEndTime());
                            if (date != null) {
                                calendar.setTime(date);
                            }
                            pvTime.setDate(calendar);
                        }
                        pvTime.show();
                    }
                    break;
                case R.id.btn_name:

                    if (mDataBillClassify.getType() == ReplayScreenCondition.TYPE_GROUP) {
                        List<UserGroup> userGroups = AppManager.getUserGroupList();
                        if (userGroups == null) {
                            userGroups = new ArrayList<>();
                        }
                        List<String> groupNames = new ArrayList<>();
                        int selectIndex = -1;
                        for (int i = 0, userGroupsSize = userGroups.size(); i < userGroupsSize; i++) {
                            UserGroup userGroup = (UserGroup)userGroups.get(i);
//                            if (userGroup != null && !TextUtils.isEmpty(userGroup.getGroupName())) {
                            //只显示常用组  by cuizh,0418
                            if (userGroup != null && userGroup.getIsdefault()!=null && userGroup.getIsdefault().equals("1") && !TextUtils.isEmpty(userGroup.getGroupName())) {
                                groupNames.add(userGroup.getGroupName());
                                if (userGroup.getGroupName().equals(mDataBillClassify.getrName())) {
                                    selectIndex = i;
                                }
                            }
                        }
                        new MaterialDialog.Builder(mContext.get())
                                .title("选择群组")
                                .items(groupNames)
                                .itemsCallbackSingleChoice(selectIndex, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        /**
                                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                         * returning false here won't allow the newly selected radio button to actually be selected.
                                         **/
                                        if (!TextUtils.isEmpty(text)) {
                                            Log.e(TAG, "onSelection: " + text);
                                            mDataBillClassify.rName = String.valueOf(text);
                                            mViewHolder.etRname.setText(text);
                                        }
                                        return true;
                                    }
                                })
                                .positiveText("确定")
                                .show();

                    } else {

                        List<PersonUserEntity> userEntities = AppManager.getPersonUserEntityList();
                        if (userEntities == null) {
                            userEntities = new ArrayList<>();
                        }
                        int selectIndex = -1;
                        List<String> userNames = new ArrayList<>();
                        for (int i = 0, userGroupsSize = userEntities.size(); i < userGroupsSize; i++) {
                            PersonUserEntity userEntity = userEntities.get(i);
                            if (userEntity != null && !TextUtils.isEmpty(userEntity.getuName())) {
                                userNames.add(userEntity.getuName());
                                if (userEntity.getuName().equals(mDataBillClassify.getrName())) {
                                    selectIndex = i;
                                }
                            }
                        }
                        new MaterialDialog.Builder(mContext.get())
                                .title("选择用户")
                                .items(userNames)
                                .itemsCallbackSingleChoice(selectIndex, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        /**
                                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                         * returning false here won't allow the newly selected radio button to actually be selected.
                                         **/

                                        if (!TextUtils.isEmpty(text)) {
                                            Log.e(TAG, "onSelection: " + text);
                                            mDataBillClassify.rName = String.valueOf(text);
                                            mViewHolder.etRname.setText(text);
                                        }
                                        return true;
                                    }
                                })
                                .positiveText("确定")
                                .show();
                    }


                    break;
            }

        }
    }


    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View backbroundView = mMenuView.findViewById(R.id.li_container);
            int height = backbroundView.getTop();
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    if (_CanMiss) {
                        dismiss();
                    }
                }
                if (y > backbroundView.getBottom()) {
                    if (_CanMiss) {
                        dismiss();
                    }
                }

                if (x < backbroundView.getLeft()) {
                    if (_CanMiss) {
                        dismiss();
                    }
                }
                if (x > backbroundView.getRight()) {
                    if (_CanMiss) {
                        dismiss();
                    }
                }
            }
            return true;
        }
    };

}
