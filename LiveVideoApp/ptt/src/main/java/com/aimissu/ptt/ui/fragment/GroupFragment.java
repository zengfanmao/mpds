package com.aimissu.ptt.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.GroupAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ReadMsgEvent;
import com.aimissu.ptt.entity.call.CallConnectedEvent;
import com.aimissu.ptt.entity.call.CallDisconnectedEvent;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataUserAction;
import com.aimissu.ptt.entity.data.DataUserGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.event.CallSpeakerEvent;
import com.aimissu.ptt.entity.event.CutCallEvent;
import com.aimissu.ptt.entity.event.GetGroupByRankEvent;
import com.aimissu.ptt.entity.event.HeadSetPttEvent;
import com.aimissu.ptt.entity.event.RefreshMsgCountOnlineEvent;
import com.aimissu.ptt.entity.event.RefreshRedPointEvent;
import com.aimissu.ptt.entity.event.SetDefaultGroupEvent;
import com.aimissu.ptt.entity.event.WaitingClickEvent;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ReceieveMsgEvent;
import com.aimissu.ptt.entity.im.TextModel;
import com.aimissu.ptt.entity.sipMessage.CallMessage;
import com.aimissu.ptt.entity.sipMessage.SipConferenceClose;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.PopScreenCondition;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.entity.ui.UserGroupRank;
import com.aimissu.ptt.presenter.GroupPresenter;
import com.aimissu.ptt.presenter.IGroupPresenter;
import com.aimissu.ptt.presenter.PersonalPresenter;
import com.aimissu.ptt.ui.Slide.PlusItemSlideCallback;
import com.aimissu.ptt.ui.Slide.WItemTouchHelperPlus;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.JsonUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.IGroupView;
import com.aimissu.ptt.view.IPersonalView;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.grgbanking.video.Message;
import com.grgbanking.video.VideoCore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.aimissu.ptt.config.AppManager.getUserGroupList;

/**
 */
public class GroupFragment extends BaseMvpFragment<IGroupPresenter> implements IGroupView, Message.StateListener, OnRefreshListener, IPersonalView {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    //    @BindView(R.id.btn_speaker)
//    Button speaker;
    @BindView(R.id.btn_speaker1)
    Button speaker1;
    @BindView(R.id.rl_speak_icon)
    LinearLayout mRlSpeakIcon;
    @BindView(R.id.iv_volume)
    ImageView mVolume;
    @BindView(R.id.smartrefreshlaout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_speakingName)
    TextView tvSeakingName;
    @BindView(R.id.btn_call)
    Button mCall;
    @BindView(R.id.tab_left)
    TextView tabLeft;
    @BindView(R.id.tab_right)
    TextView tabRight;
    @BindView(R.id.group_call_press)
    LinearLayout group_call_press;

    @BindView(R.id.icon_group_search_cancel)
    TextView icon_group_search_cancel;
    @BindView(R.id.icon_group_search)
    LinearLayout icon_group_search;
    @BindView(R.id.et_group_search)
    EditText et_group_search;
    @BindView(R.id.search_progressbar)
    ProgressBar search_Progressbar;


    private String tag = "GroupFragment";
    private AudioManager mAudioManager;
    private AnimationDrawable mVolumeAnimationDrawable;
    private GroupAdapter mAdapter;

    private Gson gson;
    private TextModel textModel;
    public PersonalPresenter mPersonalPresenter;
    private PopScreenCondition popScreenCondition = new PopScreenCondition(AppManager.getdCode(), "", PersonalFragment.class.getSimpleName());
    private Timer mTimer;
    private String mHangUp = "   挂断";
    private String mCallGroup = "   呼叫";

    private Vibrator vibrator;
    private boolean isDefaultGroupView = true;

    private int lastPosition = 0;
    private int lastOffset = 0;
    private LinearLayoutManager mLayoutManager;


    /**
     * 所有组集合
     */
    private List<UserGroup> userGroups;
    /**
     * 常用组集合
     */
    private List<UserGroup> defaultUserGroups;

    /**
     * 非常用组集合
     */
    private List<UserGroup> notDefaultUserGroups;

    /**
     * 混合列表
     */
//    private List<MultiItemEntity> multiItemGroupList;
    private List<MultiItemEntity> multiItemDefaultGroupList;
    private List<MultiItemEntity> multiItemRankList;
    private List<UserGroupRank> userGroupRanks;
//    private List<MultiItemEntity> multiItemEntities;

    /**
     * 当前显示页面是否常用页面
     */
    private boolean isDefaultUserGroups = true;

    /**
     * 当前是否正在搜索用户组
     */
    private boolean isSearchingByNet;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected IGroupPresenter createPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    protected void lazyInitData() {

        try {

            checkPresenter();
            mAdapter = new GroupAdapter(null);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.bindToRecyclerView(recyclerView);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(getActivity(), 2), false));

            //防止刷新时屏幕闪烁 by cuizh,0318
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            //recyclerview左滑菜单 by cuizh,0318
            PlusItemSlideCallback callback = new PlusItemSlideCallback();
            callback.setType(WItemTouchHelperPlus.SLIDE_ITEM_TYPE_ITEMVIEW);
            WItemTouchHelperPlus extension = new WItemTouchHelperPlus(callback);
            extension.attachToRecyclerView(recyclerView);

            vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

            mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            handleSpeaker();
//        mPresenter.getUserGroup();
            mPresenter.getPermission();
            smartRefreshLayout.setEnableLoadmore(false);
            smartRefreshLayout.setOnRefreshListener(this);
            gson = new Gson();

            mPersonalPresenter = new PersonalPresenter(this);
            mPersonalPresenter.searchUser(popScreenCondition.discussionCode, popScreenCondition.dCode, null, 0, 300, false, true);

            //获取组群信息 by cuizh,0415
            mPresenter.getUserGroupRank();

            //定期更新数据
            mTimer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!isDefaultGroupView && !TextUtils.isEmpty(et_group_search.getText().toString()) && isSearchingByNet) {

                        searchGroupFromNetwork();
                    } else {

                        mPresenter.getUserGroup();
                    }
                }
            };
            mTimer.schedule(task, 0, 1000 * 30);

//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initCallBtnShow();
//            }
//        }, 1500);

            et_group_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (isDefaultGroupView) {
                        doGroupSearch();
                    }
                }
            });


            /**
             * 记录recyclerview当前位置
             */
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (mAdapter.getItemCount() > 0) {
//
//                    View topView = mLayoutManager.getChildAt(0);          //获取可视的第一个view
//                    lastOffset = topView.getTop();                           //获取与该view的顶部的偏移量
//                    lastPosition = mLayoutManager.getPosition(topView);  //得到该View的数组位置
//                }
//            }
//        });
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * *记录当前所有展开位置
     */
//    public static Set<Long> expandRankId = new HashSet<>();
    public static long expandRank = -1;
    public static int expandPos;


    @Override
    public void onBack() {

    }

    private Timer mSpeakerTimer;

    /**
     * 处理说话按钮的效果
     */
    private void handleSpeaker() {

        try {

            mVolumeAnimationDrawable = (AnimationDrawable) mVolume.getDrawable();

            speaker1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mSpeakerTimer = new Timer();
                    CallMessage callMessage = new CallMessage();
                    callMessage.setCallerSSI(AppManager.getUserDeviceId());


                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            LogUtil.i(tag, "有抢话的权限。。。。" + PermissionUtils.UserPermissons.toString());
                            if (!VideoCore.getInstance().haveCall()) {
                                ToastUtils.showLocalToast("暂无通话可用，请先呼叫通话");
                                return false;
                            }


                            if (PermissionUtils.hasGroupChargePermisson()) {
                                LogUtil.i(tag, "有抢话的权限。。。。");
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
                                        if (baseEntity.isIsSuccess()) {
                                            sendMsgSpeakingEnable(callMessage);
                                            LogUtil.i(tag, "有抢话的权限。。。。可以抢话");
                                        } else {
                                            if (!TextUtils.isEmpty(AppManager.speakingName)) {
                                                ToastUtils.showToast("请稍后再讲话...");
                                                return;
                                            }
                                            LogUtil.i(tag, "有抢话的权限。。。。不能抢话");
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


                            if (!TextUtils.isEmpty(AppManager.speakingName)) {
                                ToastUtils.showToast("请稍后再讲话...");
                                return false;
                            }
                            sendMsgSpeakingEnable(callMessage);

                            break;
                        case MotionEvent.ACTION_UP:
                            actionUp(callMessage);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 松开说话按钮
     *
     * @param callMessage
     */
    private void actionUp(CallMessage callMessage) {

        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSpeakerTimer.cancel();
                    mRlSpeakIcon.setVisibility(View.GONE);
                    callMessage.setHead("PDTMSG");
                    callMessage.setMsgtype("Conference_PttOff");
                    callMessage.setCallerSSI(AppManager.getUserDeviceId());
                    Message message1 = VideoCore.getInstance().sendMessage(null, gson.toJson(callMessage).toString(), GroupFragment.this);

                }
            });
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

//    //接收线控耳机PTT消息
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onHeadSetPttEvent(HeadSetPttEvent event) {
//
//        CallMessage callMessage = new CallMessage();
//        callMessage.setCallerSSI(AppManager.getUserDeviceId());
//
//        if (event.isPttOn()) {
//
//            LogUtil.i(tag, "有抢话的权限。。。。" + PermissionUtils.UserPermissons.toString());
//            if (!VideoCore.getInstance().haveCall()) {
//                ToastUtils.showLocalToast("暂无通话可用，请先呼叫通话");
//                return;
//            }
//            if (PermissionUtils.hasGroupChargePermisson()) {
//                LogUtil.i(tag, "有抢话的权限。。。。");
//                RetrofitClient.getInstance().postAsync(BaseEntity.class,
//                        RxConfig.getMethodApiUrl("/api/do/chargeEnable"),
//                        RxMapBuild.created()
//                                .put("Take", LocalCache.getInstance().getConferenceCreator())
//                                .put("ApiToken", AppManager.getApiToken())
//                                .put("UserCode", AppManager.getUserCode())
//                                .build()
//                ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<BaseEntity>() {
//                    @Override
//                    public void onSucessed(BaseEntity baseEntity) {
//                        if (baseEntity.isIsSuccess()) {
//                            sendMsgSpeakingEnable(callMessage);
//                            LogUtil.i(tag, "有抢话的权限。。。。可以抢话");
//                        } else {
//                            if (!TextUtils.isEmpty(AppManager.speakingName)) {
//                                ToastUtils.showToast("请稍后再讲话...");
//                                return;
//                            }
//                            LogUtil.i(tag, "有抢话的权限。。。。不能抢话");
//                            sendMsgSpeakingEnable(callMessage);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
//                        ToastUtils.showToast("网络异常");
//                    }
//                }));
//                return;
//            }
//            if (!TextUtils.isEmpty(AppManager.speakingName)) {
//                ToastUtils.showToast("请稍后再讲话...");
//                return;
//            }
//            sendMsgSpeakingEnable(callMessage);
//        }else {
//            actionUp(callMessage);
//            return;
//        }
//    }

    /**
     * 通知sip服务器开始讲话了
     *
     * @param callMessage
     */
    private void sendMsgSpeakingEnable(CallMessage callMessage) {
//        MediaManager.playSound(CoreService.anxiaShound);
        try {
            mRlSpeakIcon.setVisibility(View.VISIBLE);
            callMessage.setHead("PDTMSG");
            callMessage.setMsgtype("Conference_PttOn");
            callMessage.setCallerSSI(AppManager.getUserDeviceId());
//                        callMessage.setCalledSSI();
            Message message = VideoCore.getInstance().sendMessage(null, gson.toJson(callMessage).toString(), GroupFragment.this);
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void stateChanged(Message message, String s) {
        LogUtil.i(tag, "message  : " + message + " s   :" + s);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetGroupByRankEvent(GetGroupByRankEvent event) {

        try {
            if (event != null && event.getUserGroupList() != null) {

                AppManager.setUserGroupList(event.getUserGroupList());
                // 获取pdt组号
                for (UserGroup userGroup : event.getUserGroupList()) {

                    if (TextUtils.isEmpty(AppManager.getPdtNumberWithDisscusionCode(userGroup.getGroupid()))) {
                        mPresenter.getPdtGroup(userGroup.getGroupid());
                    }

                }

            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getUserGroupRankSuccessed(DataUserGroupRank model) {

        try {

//        smartRefreshLayout.finishRefresh();

            multiItemRankList = new ArrayList<>();

            userGroupRanks = model.getEntity();

            for (UserGroupRank groupRank : userGroupRanks) {
//        for (int i = 0; i < userGroupRanks.size(); i++) {
//
//            UserGroupRank groupRank = model.getEntity().get(i);
//
//            for (int j = 0; j < groupRank.getGroupList().size(); j++) {
//
//                UserGroup group = groupRank.getGroupList().get(j);
//
//                userGroups.add(group);
//
//                groupRank.addSubItem(group);
//
//            }

                multiItemRankList.add(groupRank);

            }


//        ((LinearLayoutManager)mLayoutManager).scrollToPositionWithOffset(lastPosition, lastOffset);
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getUserGroupRankFailed(String msg) {
        smartRefreshLayout.finishRefresh();
        ToastUtils.showLocalToast("获取组群列表数据失败");
    }

    @Override
    public void getUserGroupByRankSuccessed(long rank, DataUserGroupRank model) {
        try {
            if (model.getEntity() == null) {
                return;
            }

            multiItemRankList = new ArrayList<>();

            for (UserGroupRank groupRank : userGroupRanks) {

                if (groupRank.hasSubItem()) {

                    groupRank.getSubItems().clear();
//                LogUtil.i("test",groupRank.getSubItems().toString());
                    LogUtil.i("test", groupRank.getSubItems().size() + "");
                }


                if (groupRank.getRank() == rank) {

                    groupRank.setExpanded(false);

                    if(model.getEntity()!=null && model.getEntity().size()>0){

                        for (UserGroup userGroup : model.getEntity().get(0).getGroupList()) {

                            if (userGroup != null) {
                                groupRank.addSubItem(userGroup);
                                userGroups.add(userGroup);
                            }
                        }
                    }

                }

                multiItemRankList.add(groupRank);
            }

            //设置所有消息数
            AppManager.setAllMsgCountGroup(userGroups, new Runnable() {
                @Override
                public void run() {
                    mAdapter.updateLocalMsgCountHashMap();
                    EventBus.getDefault().post(new RefreshRedPointEvent());

                    AppManager.setUserGroupList(userGroups);

//                获取pdt组号
                    for (UserGroup userGroup : userGroups) {

                        if (TextUtils.isEmpty(AppManager.getPdtNumberWithDisscusionCode(userGroup.getGroupid()))) {
                            mPresenter.getPdtGroup(userGroup.getGroupid());
                        }
                    }

                    if (isDefaultGroupView) {
                        mAdapter.setNewData(multiItemDefaultGroupList);
                    } else {

                        mAdapter.setNewData(multiItemRankList);

                        expandGroupRank();
                    }

                    if (!TextUtils.isEmpty(et_group_search.getText().toString())) {

                        if (isDefaultGroupView) {
                            doGroupSearch();
                        }
                    }
                }

            });
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }


    }

    @Override
    public void getUserGroupByRankFailed(String msg) {
        ToastUtils.showLocalToast("获取组数据失败");
    }


    private void expandGroupRank() {
        try {
            int count = mAdapter.getItemCount();
            for (int i = count - 1; i >= 0; i--) {

//            if (mAdapter.getItem(i).getItemType() == 0 && expandRankId.contains(((UserGroupRank) mAdapter.getItem(i)).getRank())) {

                if (mAdapter.getItem(i).getItemType() == 0 && expandRank == (((UserGroupRank) mAdapter.getItem(i)).getRank())) {

                    mAdapter.expand(i, false);
                }
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getUserGroupSuccessed(DataUserGroup model) {
        try {
            smartRefreshLayout.finishRefresh();

//        LocalCache.getInstance().setUserGroups(model.getEntity());
            LogUtil.i(tag, "model.getEntity() : " + model.getEntity());
//        AppManager.getUserGroupList()
//        mAdapter.setNewData(model.getEntity());

            userGroups = model.getEntity();

            multiItemDefaultGroupList = new ArrayList<>();
            multiItemDefaultGroupList.addAll(model.getEntity());

            //获取选中组群的组数据  by cuizh,0415
            if (expandRank != -1) {
                mPresenter.getUserGroupByRank(expandRank);

            } else {

                //设置所有消息数
                AppManager.setAllMsgCountGroup(userGroups, new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateLocalMsgCountHashMap();
                        EventBus.getDefault().post(new RefreshRedPointEvent());

                        AppManager.setUserGroupList(userGroups);

//                获取pdt组号
                        for (UserGroup userGroup : userGroups) {
                            if (TextUtils.isEmpty(AppManager.getPdtNumberWithDisscusionCode(userGroup.getGroupid()))) {
                                mPresenter.getPdtGroup(userGroup.getGroupid());
                            }
                        }

                        if (isDefaultGroupView) {
                            mAdapter.setNewData(multiItemDefaultGroupList);
                        } else {
                            mAdapter.setNewData(multiItemRankList);
//                    expandGroupRank();
//                    mAdapter.expandAll();
                        }

                        if (!TextUtils.isEmpty(et_group_search.getText().toString())) {
                            if (isDefaultGroupView) {
                                doGroupSearch();
                            }
                        }
                    }

                });
            }

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 过滤常用的组
     */
    private List<UserGroup> getDefaultGroup() {
        try {
            List<UserGroup> defaultUserGroups = new ArrayList<>();
            for (UserGroup userGroup : userGroups) {
                if (CommonUtils.emptyIfNull(userGroup.getIsdefault()).equals("1")) {
                    defaultUserGroups.add(userGroup);
                    Log.i(tag, "添加的userGroup:" + userGroup.getGroupName());
                }
            }

            return defaultUserGroups;
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
        return null;
    }


    /**
     * 过滤非常用的组
     */
//    private List<UserGroup> getNotDefaultGroup() {
//
//        List<UserGroup> notDefaultUserGroups = new ArrayList<>();
//        for (UserGroup userGroup : userGroups) {
//            if (CommonUtils.emptyIfNull(userGroup.getIsdefault()).equals("0")) {
//                notDefaultUserGroups.add(userGroup);
//                Log.i(tag, "添加的userGroup:" + userGroup.getGroupName());
//            }
//        }
//
//        return notDefaultUserGroups;
//    }
    @Override
    public void getUserGroupFailed(String msg) {
        smartRefreshLayout.finishRefresh();
        ToastUtils.showLocalToast("获取常用组数据失败");
    }

    @Override
    public void getPdtGroupSuccessed(DataPdtGroup model) {

    }

    @Override
    public void getPdtGroupFailed(String msg) {

    }

    @Override
    public void switchGroupSuccessed() {

        try {
            if (VideoCore.getLc().getCurrentCall() != null) {
                VideoCore.getInstance().terminateCall();
            }

            if (!TextUtils.isEmpty(et_group_search.getText().toString())) {
                if (!isDefaultGroupView) {
                    searchGroupFromNetwork();
                }
            } else {
                mPresenter.getUserGroup();
                mAdapter.notifyDataSetChanged();
            }


            vibrator.vibrate(100);

            //重新加载服务器ChatMsgDetail  by cuizh,0321
            GroupChatFragment.reLoadMsgDetail = true;

            LogUtil.i(tag, "切换售后组·····switchGroupSuccessed");
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void switchGroupFailed(String msg) {
        LogUtil.i(tag, "切换售后组·····switchGroupFailed ：msg " + msg);
    }

    @Override
    public void setDefaultGroupSuccessed() {
        try {
            ToastUtils.showLocalToast("设置常用组成功");
            LogUtil.i(tag, "设置常用组·····setDefaultGroupSuccessed");

//        mAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(et_group_search.getText().toString())) {
                if (!isDefaultGroupView) {
                    searchGroupFromNetwork();
                }
            } else {
                mPresenter.getUserGroup();
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount() - 1);

            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void setDefaultGroupFailed(String msg) {

        ToastUtils.showLocalToast("设置常用组失败");
        LogUtil.i(tag, "设置常用组·····setDefaultGroupFailed ：msg " + msg);

    }

    @Override
    public void getPermissionSuccessed() {
        LogUtil.i(tag, "LocalCache.getInstance().getUserPermisson()：msg " + LocalCache.getInstance().getUserPermisson().size() + " " + LocalCache.getInstance().getUserPermisson().toString());
    }

    @Override
    public void getPermissionFailed(String msg) {
        try {
            LogUtil.i(tag, "getPermissionFailed：msg " + msg);

            //获取权限失败隔2秒后重新从后台获取  by cuizh,0422
            Global.getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getPermission();
                }
            }, 2000);
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    public void onRefresh(RefreshLayout refreshlayout) {
        try {
            if (!isDefaultGroupView && !TextUtils.isEmpty(et_group_search.getText())) {

                searchGroupFromNetwork();
            } else {

                mPresenter.getUserGroup();
            }
            mPresenter.getPermission();
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceieveMsgEvent event) {
        //不再显示消息预览  by cuizh,0508
//        if (event != null && event.getChatMsg() != null) {
//            if (IMType.MsgFromType.Group.toString().equals(event.getChatMsg().getMsgFromType())) {
//                if (!isHidden()) {
//                    ChatMsg chatMsg = event.getChatMsg();
//                    String groupId = chatMsg.getMsgToCode();
//                    UserGroup userGroup = AppManager.getUserGroup(groupId);
//                    userGroup.setSendUserCode(chatMsg.getSendUserCode());
//                    userGroup.setSendUserName(chatMsg.getSendUserName());
//                    userGroup.setMsgType(chatMsg.getMsgType());
//                    textModel = JsonUtils.toModel(chatMsg.getMsgContent(), TextModel.class);
//                    userGroup.setMsgContent(textModel == null ? "" : textModel.Text);
//                    AppManager.updateLocalUserGroup(userGroup.toLocalUserGroup());
//                    userGroups = getUserGroupList();
//                    defaultUserGroups = getDefaultGroup();
//                    if (isDefaultUserGroups) {
////                        mAdapter.setNewData(defaultUserGroups);
//
//                    } else {
////                        mAdapter.setNewData(userGroups);
//                    }
//
//                }
//
//                //群消息
//            } else if (IMType.MsgFromType.Person.toString().equals(event.getChatMsg().getMsgFromType())) {
//                //个人消息
//                LogUtil.i("RxLog", "PersonalFragment person ReceieveMsgEvent");
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallConnected(CallConnectedEvent event) {
        try {
            if (event != null && !event.isPersonCall()) {
                LogUtil.i("GroupFragment", "onCallConnected");
//            speaker.setVisibility(View.VISIBLE);
                mCall.setText(mHangUp);
                mCall.setSelected(true);

                //手机接上耳机时关闭扬声器  by cuizh,0410
                if (AppManager.HEADSET_PLUG_STATE == 1) {
                    VideoCore.getInstance().enableSpeaker(false);
                }

                //准备环境
                VideoCore.getInstance().prepareMessageEnvironment();
                if (!mVolumeAnimationDrawable.isRunning()) {
                    mVolumeAnimationDrawable.start();
                }

//            for (int i = 0; i < mAdapter.getItemCount(); i++) {
//                if (mAdapter.getItem(i).getGroupid().equals(AppManager.getUserData().getDiscussionCode())) {
//                    UserGroup userGroup = AppManager.getUserGroup(mAdapter.getItem(i).getGroupid());
//                    if (!CommonUtils.emptyIfNull(userGroup.getCalling()).equals("true")) {
//                        userGroup.setCalling("true");
//                        AppManager.updateLocalUserGroup(userGroup.toLocalUserGroup());
//                        List<UserGroup> userGroupList = getUserGroupList();
//                        mAdapter.setNewData(userGroupList);
//                    }
//                }
//            }
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallDisConnected(CallDisconnectedEvent event) {
        try {
            if (event != null) {

                ToastUtils.showLocalToast("通话结束");

                LogUtil.i("GroupFragment", "onCallDisConnected");
                if (VideoCore.getLc().getCurrentCall() != null) {
                    VideoCore.getInstance().terminateCall();
                }

//            speaker.setVisibility(View.GONE);
                mCall.setText(mCallGroup);
                mCall.setSelected(false);

                mRlSpeakIcon.setVisibility(View.GONE);
                tvSeakingName.setVisibility(View.GONE);
                AppManager.callName = "";
                AppManager.speakingName = "";
                tvSeakingName.setVisibility(View.GONE);
                tvSeakingName.setText("");
                if (mVolumeAnimationDrawable.isRunning()) {
                    mVolumeAnimationDrawable.stop();
                }
//            initCallBtnShow();

            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 初始化电话按钮的显示
     */
    private void initCallBtnShow() {
        //通话断开后，遍历设置通话按钮为正常显示
//        for (int i = 0; i < mAdapter.getItemCount(); i++) {
//            UserGroup userGroup = AppManager.getUserGroup(mAdapter.getItem(i).getGroupid());
//            if (!CommonUtils.emptyIfNull(userGroup.getCalling()).equals("false")) {
//                userGroup.setCalling("false");
//                AppManager.updateLocalUserGroup(userGroup.toLocalUserGroup());
//            }
//        }
//        List<UserGroup> userGroupList = getUserGroupList();
//        mAdapter.setNewData(userGroupList);
    }

    //切换守候组
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWaitingClick(WaitingClickEvent event) {
        try {
            if (event != null && event.getPosition() != -1) {
                if (!((UserGroup) mAdapter.getItem(event.getPosition())).getGroupid().equals(AppManager.getUserData().getDiscussionCode())) {
                    mPresenter.switchGroup(((UserGroup) mAdapter.getItem(event.getPosition())).getGroupid(), ((UserGroup) mAdapter.getItem(event.getPosition())).getGroupName());
                }
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }

    //设置常用组
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetDefaultGroup(SetDefaultGroupEvent event) {
        try {
            if (event != null && event.getPosition() != -1 && event.getIsDefaultGroup() != null) {
                mPresenter.setDefaultGroup(((UserGroup) mAdapter.getItem(event.getPosition())).getGroupid(), event.getIsDefaultGroup());
            }

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    //显示说话人信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallSpeakerEvent(CallSpeakerEvent event) {
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
    }

    @Override
    public boolean isSupportEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(tag, "GroupFragment.....onDestroy.....");
    }

    @Override
    public void getMyChatSuccessed(DataMyChats model) {

    }

    @Override
    public void getMyChatsFailed(String msg) {

    }

    @Override
    public void getPersonUserSuccessed(List<PersonUserEntity> personUserEntityList, boolean isLoadMore) {

    }

    @Override
    public void getPersonUserFailed(String msg) {

    }

    @Override
    public void getmanageUserFailed(String mode) {

    }

    @Override
    public void getmanageUserSuccessed(DataUserAction msg) {

    }

    @Override
    public void getUserGpsSuccessed(DataGps dataGps) {

    }

    @Override
    public void getUserGpsFailed(String msg) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(ReadMsgEvent event) {
        try {
            if (event != null) {
                if (IMType.MsgFromType.Group.toString().equals(event.getType())) {
                    if ("READ_ALL".equals(event.getMsgId())) {
                        mAdapter.updateLocalMsgCountHashMap();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.updateHadReadMsg(event.getMsgId());
                    }
                }

            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCutCallEvent(CutCallEvent event) {
        if (event != null && !TextUtils.isEmpty(event.getPdtNumber())) {
            showDialog(event.getPdtNumber());
        }
    }

    /**
     * 中断电话
     */
    public void showDialog(String pdtNumber) {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            //获取AlertDialog对象
            dialog.setTitle("已退出组呼，是否强拆？");//设置标题
            dialog.setCancelable(false);//设置是否可取消
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override//设置ok的事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此处写入ok的逻辑
                    SipConferenceClose sipConferenceClose = new SipConferenceClose();
                    sipConferenceClose.setCallerSSI(AppManager.getUserDeviceId());
                    //发送消息给视频服务器关闭整个组呼
                    sipConferenceClose.setHead("PDTMSG");
                    sipConferenceClose.setMsgtype("Conference_Close");
                    sipConferenceClose.setCallid(pdtNumber);
                    Message message1 = VideoCore.getInstance().sendMessage(null, Global.mGson.toJson(sipConferenceClose).toString(), GroupFragment.this);

                    dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override//设置取消事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此写入取消的事件
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(RefreshMsgCountOnlineEvent event) {
        if (event != null) {
            onRefresh(null);
        }
    }

    @OnClick({R.id.btn_call, R.id.tab_left, R.id.tab_right, R.id.icon_group_search_cancel, R.id.icon_group_search})
    void OnClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_call:
                    grounpCall();
                    break;
                case R.id.tab_left:

                    tabLeft.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom_selector));
                    tabRight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom));

//                mAdapter.setNewData(defaultUserGroups);
//                recyclerView.setAdapter(mAdapter);
//                mPresenter.getUserGroup();
                    mAdapter.setNewData(multiItemDefaultGroupList);
                    isDefaultGroupView = true;

                    et_group_search.getText().clear();

//                et_group_search.setHint("请输入用户组名/号码");
                    icon_group_search.setVisibility(View.INVISIBLE);

                    LogUtil.i(tag, "常用组defaultUserGroups:" + defaultUserGroups);
                    isDefaultUserGroups = true;
//                onRefresh(null);

                    break;
                case R.id.tab_right:

                    tabRight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom_selector));
                    tabLeft.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_bg_bottom));


//                mAdapter.setNewData(notDefaultUserGroups);
//                mPresenter.getUserGroupRank();
                    mAdapter.setNewData(multiItemRankList);

                    expandGroupRank();

                    isDefaultGroupView = false;

                    et_group_search.getText().clear();
                    LogUtil.i(tag, "全部组userGroups:" + notDefaultUserGroups);
                    isDefaultUserGroups = false;

//                et_group_search.setHint("请输入用户组号码");
                    icon_group_search.setVisibility(View.VISIBLE);

//                onRefresh(null);
                    break;
                case R.id.icon_group_search_cancel:
                    et_group_search.getText().clear();
//                if (isDefaultUserGroups) {
////                    mAdapter.setNewData(defaultUserGroups);
//                } else {
////                    mAdapter.setNewData(userGroups);
//                }
                    if (!isDefaultGroupView) {
                        isSearchingByNet = false;
                        mAdapter.setNewData(multiItemRankList);
                    }
                    break;
                case R.id.icon_group_search:
                    if (!isDefaultGroupView && !TextUtils.isEmpty(et_group_search.getText())) {
                        isSearchingByNet = true;
                        searchGroupFromNetwork();
                    }
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }


    /**
     * 从后台服务器中搜索组数据  by cuizh,0425
     */
    private void searchGroupFromNetwork() {
        try {

//        String disscusionCode = "0" + et_group_search.getText();
            String disscusionCode = et_group_search.getText().toString();

            boolean isNumeber = Pattern.compile("^[0-9]*").matcher(disscusionCode).matches();
            RetrofitClient.getInstance().postAsync(DataUserGroupRank.class,
                    RxConfig.getMethodApiUrl("api/do/getUserGroup"),
                    RxMapBuild.created()
                            .put("UserCode", AppManager.getLoginName())
                            .put("ApiToken", AppManager.getApiToken())
                            .put(isNumeber ? "DisscusionCode" : "DisscusionName", disscusionCode)
                            .put("IsDefault", "0")
                            .put("PageNum", "0")
                            .put("PageSize", "100")
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataUserGroupRank>() {
                @Override
                public void onSucessed(DataUserGroupRank dataUserGroupRank) {

                    List<MultiItemEntity> searchItemEntity = new ArrayList<>();

//                    if (dataUserGroupRank.getEntity() != null && dataUserGroupRank.getEntity().size() > 0
//                            && dataUserGroupRank.getEntity().get(0).getGroupList().size() > 0) {
//
//                        for (UserGroup userGroup : dataUserGroupRank.getEntity().get(0).getGroupList()) {
//
//                            searchItemEntity.add(userGroup);
//
//                        }
//                    }

                    if (dataUserGroupRank.getEntity() != null && dataUserGroupRank.getEntity().size() > 0) {

                        for (UserGroupRank userGroupRank : dataUserGroupRank.getEntity()) {

                            if (userGroupRank.getGroupList().size() > 0) {

                                for (UserGroup userGroup : userGroupRank.getGroupList()) {

                                    searchItemEntity.add(userGroup);

                                }
                            }

                        }

                    }

                    mAdapter.setNewData(searchItemEntity);

                    smartRefreshLayout.finishRefresh();
                }


                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                }
            }));

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    /**
     * 组呼
     */
    private void grounpCall() {
        try {
            if (!PermissionUtils.hasGroupCallPermisson()) {
                ToastUtils.showToast("您没有组呼权限");
                return;
            }

            String currentDiscussionCode = AppManager.getUserData().getDiscussionCode();
            UserGroup userGroup = AppManager.getUserGroup(currentDiscussionCode);
            if (userGroup == null) {
                ToastUtils.showLocalToast("用户组数据为空");
                return;
            }
            String pdtNumber = AppManager.getPdtNumberWithDisscusionCode(userGroup.getGroupid());
            UserGroup currentUserGroup = AppManager.getUserGroup(userGroup.getGroupid());
            if (!TextUtils.isEmpty(pdtNumber)) {
//            if (VideoCore.getInstance().haveCall() && !CommonUtils.emptyIfNull(userGroup.getCalling()).equals("true")) {
//                ToastUtils.showToast("您已经处于通话");
//                return;
//            }
//            if (VideoCore.getInstance().haveCall() && !mCall.getText().equals(mHangUp)) {
//                ToastUtils.showToast("您已经处于通话");
//                return;
//            }
                if (mCall.getText().equals(mHangUp)) {
                    if (PermissionUtils.hasGroupCutPermisson() && !AppManager.callName.equals("我")) {
                        EventBus.getDefault().post(new CutCallEvent(pdtNumber));
                    }
                    VideoCore.getInstance().terminateCall();
                    currentUserGroup.setCalling("false");

                } else {
//                        if (Double.valueOf(pdtNumber) <= 0) {
//                            ToastUtils.showToast("组号不正确");
//                            return;
//                        }

                    AppManager.callName = "我";

//                LogUtil.i(tag,"stun: "+ VideoCore.getInstance().getLc().getNatPolicy().getStunServer());
                    VideoCore.getInstance().newOutgoingCall("group_" + pdtNumber + "-" + AppManager.getUserDeviceId(), false);
                    currentUserGroup.setCalling("true");
                }

                AppManager.updateLocalUserGroup(currentUserGroup.toLocalUserGroup());
//            List<UserGroup> userGroupList = AppManager.getUserGroupList();
//            setNewData(userGroupList);
            } else {
                ToastUtils.showToast("组号为空");
            }

            LogUtil.i(tag, "点击了通话按钮。。。。");

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    //搜索用户组 by cuizh,0320
    private void doGroupSearch() {

        try {
            String etText = et_group_search.getText().toString();
            List<MultiItemEntity> searchUserGroups = new ArrayList<>();
            if (!TextUtils.isEmpty(etText)) {

//            icon_group_search.setVisibility(View.GONE);
//            icon_group_search_cancel.setVisibility(View.VISIBLE);
//            icon_group_search.setVisibility(View.VISIBLE);

                if (isDefaultGroupView) {
                    for (UserGroup userGroup : userGroups) {
                        if (userGroup != null) {
                            LogUtil.i(tag, "userGroup     :    " + userGroup.getGroupName());
                            if ((userGroup.getGroupName() != null && userGroup.getIsdefault()!=null && userGroup.getIsdefault().equals("1") && userGroup.getGroupName().contains(etText)) || (userGroup.getGroupid() != null && userGroup.getGroupid().contains(etText))) {

                                searchUserGroups.add(userGroup);

                            }
                        }
                    }
                }

            } else {

//            icon_group_search.setVisibility(View.VISIBLE);
//            icon_group_search_cancel.setVisibility(View.GONE);

                if (isDefaultGroupView) {
                    mAdapter.setNewData(multiItemDefaultGroupList);
                } else {
                    mAdapter.setNewData(multiItemRankList);
                }

                return;
            }

            if (mAdapter != null) {
                mAdapter.setNewData(searchUserGroups);
            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


}
