package com.aimissu.ptt.ui.popwindow;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aimissu.basemvp.view.RecycleViewDivider;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.event.ManageUserEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.ui.GpsEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.PageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */

public class PopLocationLeftScreen extends PopupWindow {
    private final static String TAG = PopLocationLeftScreen.class.getSimpleName();
    private boolean _CanMiss = true;
    private View mMenuView;
    WeakReference<View> viewWeakReference;
    private DataGps mDataBillClassify;
    public ScreenAdapter screenAdapter;
    private String mIncomeCode;
    private TextView headView;

    public void setCanMiss(boolean canMiss) {
        _CanMiss = canMiss;
    }

    private WeakReference<Context> mContext;
    private ViewHolder mViewHolder;

    public PopLocationLeftScreen(final Context context, DataGps dataBillClassify, String incomeCode) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mIncomeCode = incomeCode;
        initBaserView(context, dataBillClassify);
    }


    private void initBaserView(Context context, DataGps dataBillClassify) {
        mContext = new WeakReference<Context>(context);
        mDataBillClassify = dataBillClassify;

        List<GpsEntity> gpsList = mDataBillClassify.getEntity();
        if (gpsList == null || gpsList.size() == 0) {
            return;
        }
        for (int i = 0; i < gpsList.size(); i++) {
            if (gpsList.get(i).getUserCode().equals(AppManager.getUserCode())) {
                gpsList.remove(i);
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.location_pop_user_left, null);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimList);
        mViewHolder = new ViewHolder(mMenuView);
        mMenuView.setOnTouchListener(onTouchListener);

        screenAdapter = new ScreenAdapter(R.layout.location_pop_user_item, mDataBillClassify == null ? null : gpsList);
        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext.get(), LinearLayoutManager.VERTICAL, false));
        mViewHolder.recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, 2, ContextCompat.getColor(context, R.color.c_d7f0eff5)));
        mViewHolder.recyclerView.setAdapter(screenAdapter);


        View headerView = View.inflate(Global.mContext, R.layout.personal_item_header, null);
        headView = headerView.findViewById(R.id.tv_info);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        screenAdapter.addHeaderView(headerView);

        int olUser = 0;
        for (GpsEntity gpsEntity : gpsList) {
            if (gpsEntity.getStatus().equals(IMType.Params.ON_LINE)) {
                olUser++;
            }
        }
        headView.setText("我的好友 " + olUser + "/" + gpsList.size());
    }


    static class ViewHolder {
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        @BindView(R.id.li_container)
        LinearLayout liContainer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private View.OnClickListener onActionClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };


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

    public class ScreenAdapter extends BaseQuickAdapter<GpsEntity, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {


        public ScreenAdapter(int layoutResId, @Nullable List<GpsEntity> data) {
            super(layoutResId, data);
            this.setOnItemChildClickListener(this);
        }

        @Override
        protected void convert(BaseViewHolder helper, GpsEntity item) {
            helper.setText(R.id.tv_depart_name, String.valueOf(item.getDName()));
            helper.setText(R.id.tv_username, String.valueOf(item.getUserName()));

            helper.addOnClickListener(R.id.iv_msg);
            helper.addOnClickListener(R.id.ll_dizzy);
            helper.addOnClickListener(R.id.ll_die);

            if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE)) {
                helper.setTextColor(R.id.tv_username, Global.mContext.getResources().getColor(R.color.locaton_personal_online));
            } else {
                helper.setTextColor(R.id.tv_username, Global.mContext.getResources().getColor(R.color.white));
            }

            if (CommonUtils.emptyIfNull(item.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.REMOTE_DIZZY);
                    helper.setText(R.id.tv_die, IMType.Params.REMOTE_KILL);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);
                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.ACTIVATE);
                    helper.setText(R.id.tv_die, IMType.Params.REMOTE_KILL);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);
                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_KILL)) {
                    helper.getView(R.id.tv_dizzy).setVisibility(View.GONE);
                    helper.getView(R.id.tv_die).setVisibility(View.GONE);
                }
            } else if (item.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                    helper.setText(R.id.tv_die, IMType.Params.DISABLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.GONE);
                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.DISABLE)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.ACTIVATE);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.GONE);
                }
            }
        }


        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            GpsEntity gpsEntity = getItem(position);
            String dizzyString = "";
            String dieString = "";
            switch (view.getId()) {
                case R.id.iv_msg:
                    GpsEntity item = getItem(position);
                    PersonUserEntity userEntity = AppManager.getUserEntityCache(item.getUserCode().trim());
                    if (item != null && !TextUtils.isEmpty(item.getUserCode())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(item.getUserCode()).trim());
                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(item.getUserName()).trim());
                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(item.getGpsTargetType()).trim());
                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(userEntity.getDeviceid()).trim());

//                        bundle.putInt(IMType.Params.PAGE_FROM_ID,PageConfig.PAGE_ID_LOCATION );
//                        bundle.putInt(IMType.Params.PAGE_FROM_POSITION,PageConfig.PAGE_THREE );
                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                        dismiss();
                    }
                    break;
                case R.id.ll_dizzy:
                    if (CommonUtils.emptyIfNull(gpsEntity.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dizzyString = IMType.Params.REMOTE_DIZZY;
                        } else if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    } else if (gpsEntity.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.DISABLE)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dizzyString, CommonUtils.emptyIfNull(gpsEntity.getUserCode()), CommonUtils.emptyIfNull(gpsEntity.getUserName())));

                    break;
                case R.id.ll_die:
                    if (CommonUtils.emptyIfNull(gpsEntity.getGpsTargetType()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        } else if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        }
                    } else if (gpsEntity.getGpsTargetType().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(gpsEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.DISABLE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dieString, CommonUtils.emptyIfNull(gpsEntity.getUserCode()), CommonUtils.emptyIfNull(gpsEntity.getUserName())));
                    break;
            }
        }
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
