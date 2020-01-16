package com.aimissu.ptt.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.aimissu.ptt.adapter.PersonalAdapter;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.ReadMsgEvent;
import com.aimissu.ptt.entity.data.DataDepartment;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.data.DataMyChats;
import com.aimissu.ptt.entity.data.DataPersonUserEntity;
import com.aimissu.ptt.entity.data.DataUserAction;
import com.aimissu.ptt.entity.event.GetPersonalUserDataEvent;
import com.aimissu.ptt.entity.event.ManageUserEvent;
import com.aimissu.ptt.entity.event.PersonalCallEvent;
import com.aimissu.ptt.entity.event.PersonalLocationEvent;
import com.aimissu.ptt.entity.event.PopScreenConditionEvent;
import com.aimissu.ptt.entity.event.RefreshMsgCountOnlineEvent;
import com.aimissu.ptt.entity.event.RefreshRedPointEvent;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.im.ReceieveMsgEvent;
import com.aimissu.ptt.entity.ui.GpsEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.PopScreenCondition;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.presenter.IPersonalPresenter;
import com.aimissu.ptt.presenter.PersonalPresenter;
import com.aimissu.ptt.ui.activity.LocationActivity;
import com.aimissu.ptt.ui.popwindow.PopLocationRightScreen;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.ILocationView;
import com.aimissu.ptt.view.IPersonalView;
import com.grgbanking.video.VideoCore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linphone.core.LinphoneCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人聊天列表
 */
public class PersonalFragment extends BaseMvpFragment<IPersonalPresenter> implements IPersonalView, OnRefreshListener, OnLoadmoreListener,ILocationView {
    @BindView(R.id.rl_search_container)
    RelativeLayout rlSearchContainer;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_chat_title_bar)
    RelativeLayout rlScreenContainer;
    @BindView(R.id.li_user_screen)
    LinearLayout liUserScreen;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.li_condition_screen)
    LinearLayout liConditionScreen;
    @BindView(R.id.li_search)
    LinearLayout liSearch;
//    @BindView(R.id.fl_call_message)
//    LinearLayout flCallMessage;
    @BindView(R.id.tv_call_message)
    TextView tvCallMessage;
//    @BindView(R.id.tv_time)
//    Chronometer tv_time;

    Chronometer callTime;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    PersonalAdapter mAdapter;
    @BindView(R.id.smartrefreshlaout)
    SmartRefreshLayout smartRefreshLayout;
    private String tag = "PersonalFragment";
    private String mCurrenUserCode;
    private AlertDialog.Builder dialog;
    private TextView headView;

    private Switch switch_online;

    private Timer mTimer;
    private PopScreenCondition popScreenCondition = new PopScreenCondition(AppManager.getdCode(), "", PersonalFragment.class.getSimpleName());

    private final static int DEFAULT_PAGESIZE = 300;
    private final static int DEFAULT_PAGEINDEX = 0;
    private static boolean isLoadMore = false;

    private List<PersonUserEntity> personList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }


    @Override
    protected IPersonalPresenter createPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        try{

            super.onActivityCreated(savedInstanceState);
            checkPresenter();
            mAdapter = new PersonalAdapter(R.layout.personal_item, null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(getActivity(), 2), false));

            smartRefreshLayout.setEnableLoadmore(true);
            smartRefreshLayout.setOnRefreshListener(this);
            smartRefreshLayout.setOnLoadmoreListener(this);

            View headerView = getLayoutInflater().inflate(R.layout.personal_item_header, null);
            headView = headerView.findViewById(R.id.tv_info);
            switch_online = headerView.findViewById(R.id.switch_online);

            headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mAdapter.addHeaderView(headerView);

            setDefaultTitle();
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                doSearch();
                }
            });
            getDepartments();

            //定期更新数据
            mTimer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
//                refreshData(false);
                    getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());
                }
            };
            mTimer.schedule(task, 0, 1000 * 60);
//        setFl_videoSuface();

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

        try{

            getData(personList.size()/(DEFAULT_PAGESIZE+1)+1,DEFAULT_PAGESIZE,true, switch_online.isChecked());
//        refreshData(true);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    protected void lazyInitData() {

    }

    @Override
    public void onBack() {

    }

    private void setDefaultTitle() {
        UserEntity userEntity = AppManager.getUserData();
        if (userEntity != null) {
            tvTitle.setText(userEntity.dName);
        }
    }

    private void getDepartments() {

        try{

            UserEntity userEntity = AppManager.getUserData();
            if (userEntity == null)
                return;
            RetrofitClient.getInstance().postAsync(DataDepartment.class,
                    RxConfig.getMethodApiUrl("/api/do/getDeptTree"),
                    RxMapBuild.created()
                            .put("dCode", userEntity.getdCode())
                            .put("ApiToken", AppManager.getApiToken())
                            .put("UserCode", AppManager.getLoginName())
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataDepartment>() {
                @Override
                public void onSucessed(DataDepartment dataDepartment) {
                    if (dataDepartment.isIsSuccess()) {
                        LocalCache.getInstance().setDataDepartment(dataDepartment);
                    }
                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                }
            }));

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getMyChatSuccessed(DataMyChats model) {

    }

    @Override
    public void getMyChatsFailed(String msg) {
        smartRefreshLayout.finishRefresh();
    }

//    private DataPersonUserEntity dataPersonUserEntity;

    @Override
    public void getPersonUserSuccessed(List<PersonUserEntity> personUserEntityList,boolean isLoadMore) {

        try{

            final int[] olUser = {0};
//        dataPersonUserEntity = model;
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadmore();

            if (!isLoadMore) {
                personList.clear();
            }
            personList.addAll(personUserEntityList);

            headView.setVisibility(View.VISIBLE);

//        List<PersonUserEntity> personUserEntityList = model.getEntity();
            if (personUserEntityList != null && personUserEntityList.size() > 0) {
                Collections.sort(personUserEntityList);
            }
//        personUserEntityList.get(0).setuIsUnilt("1");
//        personUserEntityList.get(1).setuIsUnilt("2");
            //设置所有消息数
            AppManager.setAllMsgCountPerson(personUserEntityList, new Runnable() {
                @Override
                public void run() {
                    mAdapter.updateLocalMsgCountHashMap();
                    EventBus.getDefault().post(new RefreshRedPointEvent());

                    for (int i = 0; i < personUserEntityList.size(); i++) {
                        if (personUserEntityList.get(i).getuCode().equals(AppManager.getUserCode())) {
                            personUserEntityList.remove(i);
                        }
                    }

                    if (mAdapter != null) {
                        if (isLoadMore) {
                            mAdapter.addData(personUserEntityList == null ? null : personUserEntityList);
                        }else {
                            mAdapter.setNewData(personUserEntityList == null ? null : personUserEntityList);
                        }
                    }
                    mAdapter.notifyDataSetChanged();

//                for (PersonUserEntity personUserEntity : personUserEntityList) {
//                    if (personUserEntity.getStatus().equals(IMType.Params.ON_LINE)) {
//                        olUser[0]++;
//                    }
//                }

                    if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                        tvTitle.setText(popScreenCondition.getdName() + "-" + popScreenCondition.getDiscussionName());
                    } else if (!TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && TextUtils.isEmpty(popScreenCondition.getdName())) {
                        tvTitle.setText(popScreenCondition.getDiscussionName());
                    } else if (TextUtils.isEmpty(popScreenCondition.getDiscussionName()) && !TextUtils.isEmpty(popScreenCondition.getdName())) {
                        tvTitle.setText(popScreenCondition.getdName());
                    } else {
                        tvTitle.setText(AppManager.getUserData().getdName());
                    }

                    LogUtil.i(tag, "popScreenCondition:" + popScreenCondition.toString());
//                headView.setText("我的好友 " + olUser[0] + "/" + personUserEntityList.size());
                    headView.setText("联系人列表：");

                    LogUtil.i(tag, "getPersonUserSuccessed  dataPersonUserEntity.getEntity():  " + personUserEntityList.toString());

                }
            });

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void getPersonUserFailed(String msg) {

        try{


            smartRefreshLayout.finishRefresh();
            mAdapter.setNewData(null);
            headView.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void getmanageUserFailed(String mode) {
        LogUtil.i(tag, "getmanageUserFailed : " + mode);
    }

    @Override
    public void getmanageUserSuccessed(DataUserAction msg) {

        try{

            LogUtil.i(tag, "getmanageUserSuccessed:" + msg.toString());
//        refreshData(false);
            getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    private void getData(int pageNum, int pageSize ,boolean isLoadMore, boolean isOnline) {

        try{

            if (TextUtils.isEmpty(popScreenCondition.dCode)) {
                popScreenCondition.dCode = AppManager.getdCode();
            }
            mPresenter.searchUser(popScreenCondition.discussionCode, popScreenCondition.dCode,etSearch.getText().toString(),pageNum,pageSize,isLoadMore, isOnline);
            LogUtil.i(tag, "getData....");

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void getUserGpsSuccessed(DataGps dataGps) {

        try{

            if (dataGps.getEntity().size()>0){
                for (GpsEntity gpsEntity : dataGps.getEntity()) {
                    if (gpsEntity.getUserCode().equals(mCurrenUserCode)) {

                        new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_FINE_LOCATION)
                                .subscribe(granted -> {
                                    if (granted) {
                                        Intent intent = new Intent(getActivity(), LocationActivity.class);
                                        intent.putExtra(Configs.LATITDUE, gpsEntity.getLatitdue());
                                        intent.putExtra(Configs.LONGITUDE, gpsEntity.getLongitude());
                                        intent.putExtra(Configs.USERNAME, gpsEntity.getUserName());
                                        intent.putExtra(Configs.GPSTARGET_TYPE, gpsEntity.getGpsTargetType());
                                        getActivity().startActivity(intent);
                                    } else {
                                        ToastUtils.showLocalToast("定位需要用户授权");
                                    }
                                });
                    }else {
//                    ToastUtils.showToast("获取定位信息失败");
                    }
                }
            }else {
                ToastUtils.showToast("获取定位信息失败");
            }


        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void getUserGpsFailed(String msg) {
        LogHelper.sendLog("getUserGps", "获取用户定位信息失败getUserGpsFailed···");
        ToastUtils.showToast("获取定位信息失败");
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        try{

//        refreshData(false);
            getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(ReceieveMsgEvent event) {

        try{

            if (event != null && event.getChatMsg() != null) {
                if (IMType.MsgFromType.Group.toString().equals(event.getChatMsg().getMsgFromType())) {
                    LogUtil.i("RxLog", "PersonalFragment group ReceieveMsgEvent");
                    //群消息
                } else if (IMType.MsgFromType.Person.toString().equals(event.getChatMsg().getMsgFromType())) {
                    //个人消息
                    LogUtil.i("RxLog", "PersonalFragment person ReceieveMsgEvent");
                    if (!isHidden()) {
                        ChatMsg chatMsg = event.getChatMsg();
                        String userCode = chatMsg.getMsgToCode();
                        AppManager.updateLocalPersonUser(AppManager.getPersonUserEntity(userCode).toLocalPersonUserEntity());
//                    mAdapter.setNewData( AppManager.getPersonUserEntityList());

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onManageUserEvent(ManageUserEvent event) {
        String userAction = event.getUserAction();
        String msgCode = event.getMsgToCode();
        String uName = event.getuName();
        LogUtil.i(tag, "userAction: " + userAction + "   msgCode: " + msgCode);
        if (event != null && !TextUtils.isEmpty(userAction)) {
            showDialog(userAction, msgCode, uName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPersonalCallEvent(PersonalCallEvent event) {

        try{

            if (event != null) {
                String msgToCode = event.getMsgToCode();
                LogUtil.i(tag, "收到通话消息·····msgToCode："+msgToCode);
                if (msgToCode.equals("")) {
//                ToastUtils.showToast("电话挂断了");
//                tv_time.stop();
//                tv_time = null;
//                flCallMessage.setVisibility(View.GONE);
                    LogUtil.i(tag, "隐藏时间框·····");
                } else {
                    LogUtil.i(tag, "显示时间框·····");
//                flCallMessage.setVisibility(View.VISIBLE);
                    PersonUserEntity personUserEntity = AppManager.getPersonUserEntity(msgToCode);
                    if (personUserEntity != null) {
                        tvCallMessage.setText("与 " + personUserEntity.getuName() + " 通话中");
                    } else {
                        tvCallMessage.setText("与 " + msgToCode + " 通话中");
                    }

                    LinphoneCall call = VideoCore.getLc().getCurrentCall();
                    if (call != null) {
                        registerCallDurationTimer(call);
                    }

//                flCallMessage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
//                            bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
//                            bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
//                            bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
//                            PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
//                        }
//                    }
//                });
                }
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    /**
     * 显示用户管理确认对话框
     *
     * @param userAction
     * @param msgCode
     */
    private void showDialog(String userAction, String msgCode, String uName) {
        if (userAction.equals(IMType.Params.REMOTE_KILL)) {
            if (!PermissionUtils.hasPersonKillPermisson()) {
                ToastUtils.showToast("您没有遥毙权限");
                return;
            }

        } else if (userAction.equals(IMType.Params.REMOTE_DIZZY)) {
            if (!PermissionUtils.hasPersonStopPermisson()) {
                ToastUtils.showToast("您没有遥晕权限");
                return;
            }

        } else if (userAction.equals(IMType.Params.ACTIVATE)) {
            if (!PermissionUtils.hasPersonEnablePermisson()) {
                ToastUtils.showToast("您没有激活权限");
                return;
            }

        } else if (userAction.equals(IMType.Params.DISABLE)) {
            if (!PermissionUtils.hasPersonDisablePermisson()) {
                ToastUtils.showToast("您没有禁用权限");
                return;
            }

        }

        dialog = new AlertDialog.Builder(getActivity());
        //获取AlertDialog对象

        //修改标题，号码->名字  by cuizh,0509
//        dialog.setTitle(userAction + " " + msgCode + " ?");//设置标题
        dialog.setTitle(userAction + " " + uName + " ?");//设置标题

        dialog.setCancelable(false);//设置是否可取消
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override//设置ok的事件
            public void onClick(DialogInterface dialogInterface, int i) {
                //在此处写入ok的逻辑
                if (userAction.equals(IMType.Params.REMOTE_KILL)) {
                    mPresenter.manageUser(msgCode, IMType.UserAction.kill.toString());
                } else if (userAction.equals(IMType.Params.REMOTE_DIZZY)) {
                    mPresenter.manageUser(msgCode, IMType.UserAction.stop.toString());
                } else if (userAction.equals(IMType.Params.ACTIVATE)) {
                    mPresenter.manageUser(msgCode, IMType.UserAction.enable.toString());
                } else if (userAction.equals(IMType.Params.DISABLE)) {
                    mPresenter.manageUser(msgCode, IMType.UserAction.disable.toString());
                }
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(PersonalLocationEvent event) {

        try{

            mCurrenUserCode = event.getUserCode();
            LogUtil.i(tag, "mCurrenUserCode:" + mCurrenUserCode);
            if (event != null && !TextUtils.isEmpty(mCurrenUserCode)) {
                UserEntity userEntity = AppManager.getUserData();
                if (userEntity != null) {

                    //修改获取用户GPS方法 by cuizh,0328
                    mPresenter.getUserGps(userEntity.getLoginName(), mCurrenUserCode, "", 0, 100);
//                mPresenter.getUserGps(userEntity.getLoginName(), userEntity.getdCode(), "", 0, 100);
                    showProgressDialog();
                }
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @OnClick({R.id.tv_cancel, R.id.li_search_toggle, R.id.li_search, R.id.li_user_screen, R.id.li_condition_screen})
    void bindClick(View view) {

        try{

            switch (view.getId()) {
                case R.id.tv_cancel:
                    etSearch.setText("");
                    toggleScreenContainer();
                    getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());
                    //隐藏输入法 by cuizh,0316
                    Global.hideInputMethod(view);

                    break;
                case R.id.li_search_toggle:
                    if (!PermissionUtils.hasPersonSearchPermisson()) {
                        ToastUtils.showToast("您没有个人搜索权限");
                        return;
                    }
                    toggleScreenContainer();
                    break;
                case R.id.li_search:
                    //搜素
                    if (!PermissionUtils.hasPersonSearchPermisson()) {
                        ToastUtils.showToast("您没有个人搜索权限");
                        return;
                    }
                    doSearch();
                    break;
                case R.id.li_user_screen:
                    //左边用户筛选

                    break;
                case R.id.li_condition_screen:
                    //右边条件搜素
                    if (!PermissionUtils.hasPersonSearchPermisson()) {
                        ToastUtils.showToast("您没有个人搜索权限");
                        return;
                    }
                    new PopLocationRightScreen(getActivity(), LocalCache.getInstance().getDataDepartment(), popScreenCondition)
                            .showAsDropDown(view);
                    break;

            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

//    private void doSearch() {
//        String etText = etSearch.getText().toString();
//        List<PersonUserEntity> personUserEntities = new ArrayList<>();
//        if (!TextUtils.isEmpty(etText)) {
//            if (dataPersonUserEntity != null && dataPersonUserEntity.getEntity() != null) {
//                for (PersonUserEntity personUserEntity : dataPersonUserEntity.getEntity()) {
//                    if (personUserEntity != null) {
//                        LogUtil.i(tag, "personUserEntity     :    " + personUserEntity.toString());
//                        if ((personUserEntity.getGroupName() != null && personUserEntity.getGroupName().contains(etText)) || (personUserEntity.getdName() != null && personUserEntity.getdName().contains(etText)) || (personUserEntity.getuName() != null && personUserEntity.getuName().contains(etText)) || (personUserEntity.getuCode() != null && personUserEntity.getuCode().contains(etText))) {
//                            personUserEntities.add(personUserEntity);
//                        }
//                    }
//                }
//            }
//            tvTitle.setText("搜索-" + etSearch.getText().toString());
//        } else {
//            setDefaultTitle();
//            getData();
//            return;
//        }
//
//        if (mAdapter != null) {
//            mAdapter.setNewData(personUserEntities);
//        }
//    }

    private void doSearch() {

        try{

            UserEntity userEntity = AppManager.getUserData();
            if (userEntity == null)
                return;
            RetrofitClient.getInstance().postAsync(DataPersonUserEntity.class,
                    RxConfig.getMethodApiUrl("/api/do/searchUser"),
                    RxMapBuild.created()
//                        .put("dCode", userEntity.getdCode())
                            .put("ApiToken", AppManager.getApiToken())
                            .put("UserCode", AppManager.getLoginName())

                            //搜索用户不传入dCode by cuizh,0402
//                        .put("dCode", popScreenCondition.dCode)
                            .put("DisscusionCode",popScreenCondition.discussionCode)
                            .put("UserName",etSearch.getText().toString())
                            .put("PageNum",String.valueOf(DEFAULT_PAGEINDEX))
                            .put("PageSize",String.valueOf(DEFAULT_PAGESIZE))
                            .build()
            ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataPersonUserEntity>() {
                @Override
                public void onSucessed(DataPersonUserEntity dataPersonUserEntity) {
                    if (dataPersonUserEntity.isIsSuccess()) {
                        getPersonUserSuccessed(dataPersonUserEntity.getEntity(),false);
                    }
                }

                @Override
                public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                    ToastUtils.showLocalToast("搜索用户失败");
                }
            }));


        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void toggleScreenContainer() {
        try{

            if (rlScreenContainer.getVisibility() == View.VISIBLE) {
                rlScreenContainer.setVisibility(View.GONE);
                rlSearchContainer.setVisibility(View.VISIBLE);
            } else {
                rlScreenContainer.setVisibility(View.VISIBLE);
                rlSearchContainer.setVisibility(View.GONE);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PopScreenConditionEvent event) {

        try{

            if (event != null && event.getPopScreenCondition() != null && PersonalFragment.class.getSimpleName().equals(event.getPopScreenCondition().getRequestCode())) {
                this.popScreenCondition = event.getPopScreenCondition();

                getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());


//            refreshData(false);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetPersonalUserDataEvent(GetPersonalUserDataEvent event) {

        try{

            LogUtil.i(tag, "onGetPersonalUserDataEvent·····");
            if (event != null) {
//            refreshData(false);

                getData(DEFAULT_PAGEINDEX,DEFAULT_PAGESIZE,false, switch_online.isChecked());
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(ReadMsgEvent event) {

        try{

            if (event != null) {
                if (IMType.MsgFromType.Person.toString().equals(event.getType()) && !TextUtils.isEmpty(event.getMsgId())) {
                    if ("READ_ALL".equals(event.getMsgId())) {
                        mAdapter.updateLocalMsgCountHashMap();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.updateHadReadMsg(event.getMsgId());
                    }
                }
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(RefreshMsgCountOnlineEvent event) {
        if (event != null) {
            onRefresh(null);
        }
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
     * @param
     */
//    public void setFl_videoSuface(PersonUserEntity personUserEntity) {
//
//        LogUtil.i(tag, "设置suface 拖拽事件的处理·····");
//        content = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View
//
//        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
//        screenWidth = dm.widthPixels;
//        screenHeight = dm.heightPixels;
//
//
//        ViewTreeObserver vto = content.getViewTreeObserver();//获取ViewTree的监听器
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//
//            @Override
//            public boolean onPreDraw() {
//
//                // TODO Auto-generated method stub
//                if (!hasMeasured) {
//
//                    screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
//                    hasMeasured = true;//设置为true，使其不再被测量。
//
//                }
//                return true;//如果返回false，界面将为空。
//
//            }
//
//        });
//        screenHeight = screenHeight - Global.dp2px(100);
//
//        flCallMessage.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间
//
//            int lastX, lastY; // 记录移动的最后的位置
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                // TODO Auto-generated method stub
//                int ea = event.getAction();//获取事件类型
//                switch (ea) {
//                    case MotionEvent.ACTION_DOWN: // 按下事件
//
//
//                        if (setDoubleClickListener(personUserEntity)) return true;
//
//                        LogUtil.i(tag, "按下suface·····");
//                        lastX = (int) event.getRawX();
//                        lastY = (int) event.getRawY();
//                        downX = lastX;
//                        downY = lastY;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE: // 拖动事件
//
//                        LogUtil.i(tag, "拖动suface·····");
//                        // 移动中动态设置位置
//                        int dx = (int) event.getRawX() - lastX;//位移量X
//                        int dy = (int) event.getRawY() - lastY;//位移量Y
//                        int left = v.getLeft() + dx;
//                        int top = v.getTop() + dy;
//                        int right = v.getRight() + dx;
//                        int bottom = v.getBottom() + dy;
//
//                        //++限定按钮被拖动的范围
//                        if (left < 0) {
//
//                            left = 0;
//                            right = left + v.getWidth();
//
//                        }
//                        if (right > screenWidth) {
//
//                            right = screenWidth;
//                            left = right - v.getWidth();
//
//                        }
//                        if (top < 0) {
//                            top = 0;
//                            bottom = top + v.getHeight();
//
//                        }
//                        if (bottom > screenHeight - Global.dp2px(100)) {
//
//                            bottom = screenHeight - Global.dp2px(100);
//                            top = bottom - v.getHeight();
//
//                        }
//                        //--限定按钮被拖动的范围
//                        v.layout(left, top, right, bottom);//按钮重画
//
//                        // 记录当前的位置
//                        lastX = (int) event.getRawX();
//                        lastY = (int) event.getRawY();
//                        break;
//
//                    case MotionEvent.ACTION_UP: // 弹起事件
//
//                        LogUtil.i(tag, "弹起suface·····");
//                        //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件
//
//                        if (Math.abs((int) (event.getRawX() - downX)) > 5
//                                || Math.abs((int) (event.getRawY() - downY)) > 5)
//                            clickormove = false;
//                        else
//                            clickormove = true;
//
//                        break;
//                }
//                return false;
//            }
//
//        });
//
//    }


    public void registerCallDurationTimer(LinphoneCall call) {
        int callDuration = call.getDuration();
        if (callDuration == 0 && call.getState() != LinphoneCall.State.StreamsRunning) {
            return;
        }

//        if (tv_time == null) {
//            tv_time = (Chronometer) getmRootView().findViewById(R.id.tv_time);
//        }
//
//        if (tv_time != null) {
//            tv_time.setBase(SystemClock.elapsedRealtime() - 1000 * callDuration);
//            tv_time.start();
//            LogUtil.i(tag,"开始计时·····");
//        }
    }

    /**
     * 双击切换大小
     *
     * @return
     * @param personUserEntity
     */
    private boolean setDoubleClickListener(PersonUserEntity personUserEntity) {
        mLastTime = mCurTime;
        mCurTime = System.currentTimeMillis();

        if (mCurTime - mLastTime < 500) {
            if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                Bundle bundle = new Bundle();
                bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
            }
            return true;
        }
        return false;
    }

}
