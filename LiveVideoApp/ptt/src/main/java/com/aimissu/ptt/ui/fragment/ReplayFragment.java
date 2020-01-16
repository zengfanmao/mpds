package com.aimissu.ptt.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.PatternMatcher;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.basemvp.view.GridSpacingItemDecoration;
import com.aimissu.basemvp.view.ProgressWheel;
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.ReplayAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.data.AudioDetailEntity;
import com.aimissu.ptt.entity.data.DataReplay;
import com.aimissu.ptt.entity.event.ReplayAudioDetailsEvent;
import com.aimissu.ptt.entity.event.ReplayScreenEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.sipMessage.SipConferenceClose;
import com.aimissu.ptt.entity.ui.AudioDetail;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.ReplayScreenCondition;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.presenter.GroupPresenter;
import com.aimissu.ptt.presenter.IReplayPresenter;
import com.aimissu.ptt.presenter.PersonalPresenter;
import com.aimissu.ptt.presenter.ReplayPresenter;
import com.aimissu.ptt.ui.popwindow.PopReplayRightScreen;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.IReplayView;
import com.grgbanking.video.Message;
import com.grgbanking.video.VideoCore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 回放
 */
public class ReplayFragment extends BaseMvpFragment<IReplayPresenter> implements IReplayView, OnRefreshListener, OnLoadmoreListener {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.smartrefreshlaout)
    SmartRefreshLayout smartRefreshLayout;

    private final static int DEFAULT_PAGESIZE = 30;
    private final static int DEFAULT_PAGEINDEX = 0;
    private String tag = "ReplayFragment";
    private String currentMsgFromType = IMType.MsgFromType.Group.toString();
    private Calendar calendar;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    @BindView(R.id.tab_left)
    TextView tabLeft;
    @BindView(R.id.tab_right)
    TextView tabRight;
    private ReplayScreenCondition replayScreenCondition;

    @BindView(R.id.replay_progressbar)
    ProgressBar replay_progressbar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_replay;
    }

    @Override
    protected IReplayPresenter createPresenter() {
        return new ReplayPresenter(this);
    }

    ReplayAdapter replayAdapter;


    @Override
    protected void lazyInitData() {

        try {

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            replayAdapter = new ReplayAdapter(R.layout.replay_item, null);
            recyclerView.setAdapter(replayAdapter);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(getActivity(), 2), false));
            replayAdapter.bindToRecyclerView(recyclerView);
            smartRefreshLayout.setEnableLoadmore(true);
            smartRefreshLayout.setOnRefreshListener(this);
            smartRefreshLayout.setOnLoadmoreListener(this);

            replayScreenCondition = new ReplayScreenCondition(AppManager.getUserData().getDiscussionName(), "", "");

            setConditonTime();
            onRefresh(null);

            new PersonalPresenter(null).searchUser("", "", null, 0, 300, false,true);
            new GroupPresenter(null).getUserGroup();

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    private void setConditonTime() {

        try {

            calendar = Calendar.getInstance();
            replayScreenCondition.setEndTime(CommonUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
            if (TextUtils.isEmpty(replayScreenCondition.getStartTime())) {
                calendar.add(Calendar.DATE, -1);
                replayScreenCondition.setStartTime(CommonUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            initConditionView();

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void onBack() {

    }

//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            replayScreenCondition = new ReplayScreenCondition(AppManager.getUserData().getDiscussionName(), "", "");
//            onRefresh(null);
//        }
//    }
//

    private void initConditionView() {

        try {

            if (replayScreenCondition != null) {

                tvStartTime.setText(formatShowDate(replayScreenCondition.getStartTime()));
                tvEndTime.setText(formatShowDate(replayScreenCondition.getEndTime()));
            }

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    private String formatShowDate(String date) {

        try {

            String result = "";
            if (TextUtils.isEmpty(date) || date.length() < 19)
                return result;
            try {
                return date.substring(0, 19);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

        return null;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        try {

            setConditonTime();
            getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, false);

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    public void getData(Integer skip, Integer take, boolean isLoadMore) {

        try {

            //搜索时显示progressbar  by cuizh,0321
//        showProgressDialog();
            replay_progressbar.setVisibility(View.VISIBLE);


            //直接输入组号查询录音记录  by cuizh,0418
            boolean isNumber = false;
//        for(UserGroup userGroup : AppManager.getUserGroupList()){
//            if (replayScreenCondition.getrName().equals(userGroup.getGroupName())) {
//                isNumber = false;
//                break;
//            }
//        }
            if (Pattern.compile("^[0-9]*").matcher(replayScreenCondition.getrName()).matches()) {
                isNumber = true;
            }

            mPresenter.getAudioMsg(AppManager.getLoginName(), currentMsgFromType,
                    isNumber ? "0" + replayScreenCondition.getrName() : replayScreenCondition.getrName(),
                    replayScreenCondition.getStartTime(), replayScreenCondition.getEndTime(), skip, take, isLoadMore);

            LogUtil.i(tag, "replayScreenCondition.getStartTime():" + replayScreenCondition.getStartTime() + "    replayScreenCondition.getEndTime():" + replayScreenCondition.getEndTime());


        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void getAudioMsgSuccessed(DataReplay model, boolean isLoadMore) {

        try{

            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadmore();

            //搜索结束时隐藏progressbar  by cuizh,0321
//        hideProgressDialog();
            replay_progressbar.setVisibility(View.GONE);

            if (model != null && model.getEntity() != null && model.getEntity().size() > 0) {

                if (isLoadMore) {
                    replayAdapter.addData(model.getEntity());
                } else {
                    replayAdapter.setNewData(model.getEntity());
                }

                LogUtil.i(tag, "model.getEntity():" + model.getEntity());
            } else {

                if (!isLoadMore) {
                    replayAdapter.setNewData(null);
                    ToastUtils.showToast("没有获取到语音信息");
                }
            }

            replayAdapter.notifyDataSetChanged();

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void getAudioMsgFailed(String msg, boolean isLoadMore) {

        try{

            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadmore();

            //搜索结束时隐藏progressbar  by cuizh,0321
            hideProgressDialog();
//        replayAdapter.setNewData(null);

            ToastUtils.showToast("获取语音信息失败");

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

        try{


//        getData(replayAdapter.getItemCount(), DEFAULT_PAGESIZE, true);
            //修复录音记录loadmore不成功的问题  by cuizh,0418
            getData(replayAdapter.getItemCount() / (DEFAULT_PAGESIZE + 1) + 1, DEFAULT_PAGESIZE, true);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.li_condition_screen, R.id.tab_left, R.id.tab_right})
    void OnClick(View view) {

        try{

            switch (view.getId()) {
                case R.id.tab_left:
                    replayScreenCondition.setType(ReplayScreenCondition.TYPE_GROUP);
                    tabLeft.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom_selector));
                    tabRight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom));
                    if (TextUtils.isEmpty(replayScreenCondition.rName)) {
                        replayScreenCondition.rName = AppManager.getUserData().getDiscussionName();
                    }
                    currentMsgFromType = IMType.MsgFromType.Group.toString();
                    onRefresh(null);

                    break;
                case R.id.tab_right:
                    replayScreenCondition.setType(ReplayScreenCondition.TYPE_PERSONAL);
                    tabRight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom_selector));
                    tabLeft.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom));
                    if (TextUtils.isEmpty(replayScreenCondition.rName)) {
                        replayScreenCondition.rName = AppManager.getLoginName();
                    }
                    currentMsgFromType = IMType.MsgFromType.Person.toString();
                    onRefresh(null);
                    break;
                case R.id.tv_start_time:

                    break;
                case R.id.tv_end_time:

                    break;
                case R.id.li_condition_screen:
                    if (!PermissionUtils.hasRecordingSearchPermisson()) {
                        ToastUtils.showToast("您没有录音搜索权限");
                        return;
                    }
                    new PopReplayRightScreen(getActivity(), replayScreenCondition).showAsDropDown(view);
                    break;
            }


        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public boolean isSupportEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReplayScreenEvent event) {

        try{

            if (event != null && event.getCondition() != null) {
                replayScreenCondition = event.getCondition();
                LogUtil.i(tag, "选择的条件replayScreenCondition:" + event.getCondition());
                initConditionView();
//            onRefresh(null);

                getData(DEFAULT_PAGEINDEX, DEFAULT_PAGESIZE, false);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReplayAudioDetailsEvent(ReplayAudioDetailsEvent event) {

        try{

            if (event != null && event.getUuid() != null) {
//            showDialog(event.getUuid());
                RetrofitClient.getInstance().postAsync(AudioDetailEntity.class,
                        RxConfig.getMethodApiUrl("/api/do/getAudioDetail"),
                        RxMapBuild.created()
                                .put("virtualId", event.getUuid())
                                .put("ApiToken", AppManager.getApiToken())
                                .put("UserCode", AppManager.getUserCode())
                                .build()
                ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<AudioDetailEntity>() {
                    @Override
                    public void onSucessed(AudioDetailEntity audioDetailEntity) {
                        if (audioDetailEntity.isIsSuccess()) {
                            if (audioDetailEntity.getEntity() != null && audioDetailEntity.getEntity().size() > 0) {
                                showDialog(audioDetailEntity.getEntity());

                            }
                        }

                    }

                    @Override
                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                        ToastUtils.showToast("网络异常");
                    }
                }));
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 显示语音详情
     */
    public void showDialog(List<AudioDetail> Entity) {

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.audio_detail, null);
            LinearLayout v = (LinearLayout) parent.findViewById(R.id.ll_container);
            TextView title = (TextView) v.findViewById(R.id.tv_title);
            ImageView close = (ImageView) v.findViewById(R.id.iv_close);
            LinearLayout llContainerText = (LinearLayout) v.findViewById(R.id.ll_container_text);
            //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分

            final Dialog dialog = builder.create();
            dialog.show();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            LogUtil.i(tag, "处理之前的列表：" + Entity.toString());
            dialog.getWindow().setContentView(parent);//自定义布局应该在这里添加，要在dialog.show()的后面
            title.setText(Entity.get(0).getConference_name() + "-对话列表");
            AudioDetail audioDetailLast = null;
            AudioDetail audioDetailFirst = null;
            for (int i = 0; i < Entity.size(); i++) {

                if (Entity.get(i).getEvent_name().equals("conference_destroy")) {
                    audioDetailLast = Entity.get(i);
                    Entity.remove(i);
                } else if (Entity.get(i).getEvent_name().equals("conference_create")) {
                    audioDetailFirst = Entity.get(i);
                    Entity.remove(i);
                }
            }

//        if (audioDetailLast == null ){
//            audioDetailLast=Entity.get(Entity.size()-1);
//            audioDetailLast.setEvent_name("conference_destroy");
//        }

            Entity.add(0, audioDetailFirst);
            Entity.add(audioDetailLast);
            LogUtil.i(tag, "处理之后的列表：" + Entity.toString());
            LogUtil.i(tag, "audioDetailEntity.getEntity():" + Entity.toString());
            for (AudioDetail audioDetail : Entity) {
                if (audioDetail == null) {
                    return;
                }
                TextView textView = new TextView(getActivity());
//            LogUtil.i(tag,"添加的条目开始:"+audioDetail);
                if ("conference_create".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name()))) {
                    textView.setText(CommonUtils.emptyIfNull(audioDetail.getEvent_time().replace("T", "  ")) + "  :  " + audioDetail.getStartor() + " | 对话创建");
                } else if ("conference_destroy".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name()))) {
                    textView.setText(CommonUtils.emptyIfNull(audioDetail.getEvent_time().replace("T", "  ")) + "  :  " + audioDetail.getStartor() + " | 结束对话");
                } else if ("conference_ptt_APP_1".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name())) || "conference_ptt_PDT_1".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name()))) {
                    textView.setText(CommonUtils.emptyIfNull(audioDetail.getEvent_time().replace("T", "  ")) + "  :  " + audioDetail.getStartor() + " | 按下讲话");
                } else if ("conference_ptt_APP_0".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name())) || "conference_ptt_PDT_0".equals(CommonUtils.emptyIfNull(audioDetail.getEvent_name()))) {
                    textView.setText(CommonUtils.emptyIfNull(audioDetail.getEvent_time().replace("T", "  ")) + "  :  " + audioDetail.getStartor() + " | 松开");
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setTextColor(getActivity().getResources().getColor(R.color.white));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                lp.bottomMargin = 10;
                lp.topMargin = 10;
                lp.leftMargin = 5;
                llContainerText.addView(textView, lp);
                LogUtil.i(tag, "添加一个条目结束:" + audioDetail + " 子控件的数目  : " + llContainerText.getChildCount());
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }
}
