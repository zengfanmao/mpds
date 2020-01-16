package com.aimissu.ptt.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.entity.data.DataUserGroupRank;
import com.aimissu.ptt.entity.event.GetGroupByRankEvent;
import com.aimissu.ptt.entity.event.SetDefaultGroupEvent;
import com.aimissu.ptt.entity.event.WaitingClickEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LocalMsgCount;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.entity.ui.UserGroupRank;
import com.aimissu.ptt.ui.Slide.SlideSwapAction;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.webRequestUtil;
import com.aimissu.ptt.view.widget.RedImageView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.grgbanking.video.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.aimissu.ptt.ui.fragment.GroupFragment.expandPos;
import static com.aimissu.ptt.ui.fragment.GroupFragment.expandRank;

/**
 */
public class GroupAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, GroupAdapter.RecViewholder> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,Message.StateListener {


    @BindView(R.id.expand_or_collapse)
    ImageView expand_or_collapse;

    @BindView(R.id.search_progressbar)
    ProgressBar search_Progressbar;

    private String TAG = GroupAdapter.class.getSimpleName();
//    private LinearLayout call;
    private Gson gson;
    private List<UserGroup> defaultUserGroups;

    public GroupAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);

        addItemType(1,R.layout.group_item);
        addItemType(0,R.layout.group_rank_item);

        setOnItemClickListener(this);
        setOnItemChildClickListener(this);
        gson = new Gson();
//        VideoCore.getInstance().prepareMessageEnvironment();
    }

    HashMap<String, LocalMsgCount> localMsgCountHashMap = new HashMap<>();

    public void updateLocalMsgCountHashMap() {
        localMsgCountHashMap = AppManager.getMsgCountList(IMType.MsgFromType.Group.toString());
    }


    @Override
    protected void convert(RecViewholder helper, MultiItemEntity multiItem) {

        try{
            switch (helper.getItemViewType()){
                case 0:

                    UserGroupRank userGroupRank = (UserGroupRank) multiItem;

                    if (userGroupRank.isExpanded()){
                        helper.setImageResource(R.id.expand_or_collapse,R.drawable.collapse);

                    }else {
                        helper.setImageResource(R.id.expand_or_collapse, R.drawable.expand);
                    }

                    helper.setText(R.id.rank_title,userGroupRank.getRankName());
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = helper.getAdapterPosition();

                            if (userGroupRank.isExpanded()){

                                collapse(pos);

                                expandRank = -1;
//                            exUserGroupRank = null;

                                if (userGroupRank.hasSubItem()) {
                                    userGroupRank.getSubItems().clear();
                                }

//                            expandRankId.remove(userGroupRank.getRank());

                                helper.setImageResource(R.id.expand_or_collapse,R.drawable.collapse);
                            }else {

//                            search_Progressbar.setVisibility(View.VISIBLE);

                                collapse(expandPos);

                                if (userGroupRank.hasSubItem()) {
                                    userGroupRank.getSubItems().clear();
                                }

                                RetrofitClient.getInstance().postAsync(DataUserGroupRank.class,
                                        RxConfig.getMethodApiUrl("api/do/getUserGroup"),
                                        RxMapBuild.created()
                                                .put("UserCode", AppManager.getLoginName())
                                                .put("ApiToken", AppManager.getApiToken())
                                                .put("IsDefault","0")
                                                .put("Isrank","0")
                                                .put("RankNo",String.valueOf(userGroupRank.getRank()))
                                                .put("PageNum","0")
                                                .put("PageSize","100")
                                                .build()
                                ).subscribe(RxUtils.getDefaultSubscriber(new RxCallBack<DataUserGroupRank>() {
                                    @Override
                                    public void onSucessed(DataUserGroupRank dataUserGroupRank) {

                                        List<UserGroup> userGroups = new ArrayList<>();

                                        if(dataUserGroupRank.getEntity()!=null && dataUserGroupRank.getEntity().size()>0){
                                            for(UserGroup userGroup:dataUserGroupRank.getEntity().get(0).getGroupList()){

                                                userGroupRank.addSubItem(userGroup);
                                                userGroups.add(userGroup);

                                            }
                                        }


                                        //获取PDT组号
                                        EventBus.getDefault().post(new GetGroupByRankEvent(userGroups));

//                                    expand(pos);

                                        expand(getPositionByLocalRankId(userGroupRank.getRank()));

//                                    search_Progressbar.setVisibility(View.GONE);
                                    }


                                    @Override
                                    public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

//                                    getView.search_Progressbar.setVisibility(View.GONE);
                                    }
                                }));

                                expandPos = getPositionByLocalRankId(userGroupRank.getRank());

                                expandRank = userGroupRank.getRank();
//                            exUserGroupRank = userGroupRank;
//                            expandRankId.add(userGroupRank.getRank());

                                helper.setImageResource(R.id.expand_or_collapse,R.drawable.expand);


                            }
                        }
                    });
                    break;

                case 1:

                    UserGroup item = (UserGroup)multiItem;

                    if (item.getRelated().equals("0")){
                        helper.setText(R.id.tv_touxiang, "APP");
                    }else {
                        helper.setText(R.id.tv_touxiang, "PDT");
                    }

                    if (item.getIsdefault()!=null && item.getIsdefault().equals("1")) {
                        helper.setTextColor(R.id.tv_title,Color.WHITE);
                        helper.setText(R.id.default_group_select,"删除常用组");
                    }else {
                        helper.setTextColor(R.id.tv_title,Color.GRAY);
                        helper.setText(R.id.default_group_select,"加入常用组");
                    }

                    helper.setText(R.id.tv_online_num, "在线: " + item.getUserCount());
                    helper.addOnClickListener(R.id.li_chat);
                    helper.addOnClickListener(R.id.iv_chat);
                    helper.addOnClickListener(R.id.tab_icon);
                    helper.addOnClickListener(R.id.li_call);
                    helper.addOnClickListener(R.id.iv_type_icon);
                    helper.addOnClickListener(R.id.rl_group_item);
                    helper.setText(R.id.tv_title,item.getGroupName());

                    helper.addOnClickListener(R.id.default_group_select);

                    helper.setImageResource(R.id.item_type, 0);
                    if (item.getGroupid().equals(AppManager.getUserData().getDiscussionCode())) {
                        helper.getView(R.id.iv_type_icon).setSelected(true);
                        helper.getView(R.id.rl_group_item).setSelected(true);

//                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)helper.getView(R.id.rl_group_item).getLayoutParams();
//                    layoutParams.height = (int)(1.5*layoutParams.height);
//                    helper.getView(R.id.rl_group_item).setLayoutParams(layoutParams);

                        helper.setTextColor(R.id.tv_title,Color.GREEN);

                        helper.setText(R.id.tv_shouhouzhu, " (当前守候组)");
                        helper.getView(R.id.tv_shouhouzhu).setVisibility(View.VISIBLE);
                        String currentPdtNumber = AppManager.getPdtNumberWithDisscusionCode(item.getGroupid());
                        AppManager.setCurrentGroupNumber(currentPdtNumber);

                    } else {
                        helper.getView(R.id.iv_type_icon).setSelected(false);
                        helper.getView(R.id.rl_group_item).setSelected(false);
                        helper.setText(R.id.tv_shouhouzhu, "");
                        helper.getView(R.id.tv_shouhouzhu).setVisibility(View.GONE);
                    }

                    if (CommonUtils.emptyIfNull(item.getCalling()).equals("true")){
                        helper.setImageResource(R.id.li_call,  R.mipmap.u214);
                    }else {
                        helper.setImageResource(R.id.li_call,  R.mipmap.u473);
                    }

                    //设置消息数
                    RedImageView redImageView = helper.getView(R.id.tab_icon);
                    LocalMsgCount localMsgCount = null;
                    if (localMsgCountHashMap != null && localMsgCountHashMap.containsKey(item.getGroupid())) {
                        localMsgCount = localMsgCountHashMap.get(item.getGroupid());
                        int count = CommonUtils.toInt(localMsgCount.getMsgCount()) - CommonUtils.toInt(localMsgCount.getReadCount());
                        if (count <= 0) {
                            count = 0;
                        }
                        redImageView.setText(String.valueOf(count));
                    } else {
                        redImageView.setText(String.valueOf(0));
                    }

                    LogUtil.i(TAG,"UserGroup:"+item.toString());
                    LogUtil.i(TAG, "item.getGroupid():" + item.getGroupid() + " getDiscussionCode():" + AppManager.getUserData().getDiscussionCode());
//        call = helper.getView(R.id.li_call);

                    //不再显示消息预览  by cuizh,0508
//                String msg = "";
//                if (!TextUtils.isEmpty(item.getSendUserCode())) {
//                    if (!item.getSendUserCode().equals(AppManager.getLoginName())) {
//                        msg = item.getSendUserName() + ":";
//                    }
//                    if (IMType.MsgType.Text.toString().equals(item.getMsgType())) {
//                        msg += item.getMsgContent();
//                        helper.setImageResource(R.id.item_type, R.mipmap.group_item_type_text);
//                    } else if (IMType.MsgType.Image.toString().equals(item.getMsgType())) {
//                        msg += "[图片]";
//                        helper.setImageResource(R.id.item_type, R.mipmap.group_item_type_pic);
//                    } else if (IMType.MsgType.Map.toString().equals(item.getMsgType())) {
//                        msg += "[地图]";
//                        helper.setImageResource(R.id.item_type, R.mipmap.tab_location);
//                    } else if (IMType.MsgType.Video.toString().equals(item.getMsgType())) {
//                        msg += "[视频]";
//                        helper.setImageResource(R.id.item_type, R.mipmap.group_item_type_video);
//                    } else if (IMType.MsgType.Audio.toString().equals(item.getMsgType())) {
//                        msg += "[语音]";
//                        helper.setImageResource(R.id.item_type, R.mipmap.group_item_type_audio);
//                    } else if (IMType.MsgType.File.toString().equals(item.getMsgType())) {
//                        msg += "[文件]";
//                        helper.setImageResource(R.id.item_type, R.mipmap.group_item_type_file);
//                    }else {
//                        helper.setImageResource(R.id.item_type, 0);
//                    }
//                }
//                helper.setText(R.id.tv_msg, msg);
            }

        }catch (Exception e){
            LogHelper.sendErrorLog(e);
        }

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        PageUtils.turnPage(PageConfig.PAGE_ONE, PageConfig.PAGE_ID_GROUP_CHAT);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        UserGroup userGroup = (UserGroup)getItem(position);

        switch (view.getId()) {
            case R.id.li_call:
//                if (!PermissionUtils.hasGroupCallPermisson()) {
//                    ToastUtils.showToast("您没有组呼权限");
//                    return;
//                }
//
//                if (isCurrentWaitingGroup(userGroup,position)) return;
//                String pdtNumber = AppManager.getPdtNumberWithDisscusionCode(userGroup.getGroupid());
//                UserGroup currentUserGroup= AppManager.getUserGroup(userGroup.getGroupid());
//                if (!TextUtils.isEmpty(pdtNumber)) {
//                    if (VideoCore.getInstance().haveCall() && !CommonUtils.emptyIfNull(userGroup.getCalling()).equals("true")) {
//                        ToastUtils.showToast("您已经处于通话");
//                        return;
//                    }
//                    if (CommonUtils.emptyIfNull(userGroup.getCalling()).equals("true")) {
//                        if (PermissionUtils.hasGroupCutPermisson()&&!AppManager.callName.equals("我")) {
//                            EventBus.getDefault().post(new CutCallEvent(pdtNumber));
//                        }
//                        VideoCore.getInstance().terminateCall();
//                        currentUserGroup.setCalling("false");
//
//                    } else {
////                        if (Double.valueOf(pdtNumber) <= 0) {
////                            ToastUtils.showToast("组号不正确");
////                            return;
////                        }
//
//                        AppManager.callName = "我";
//                        VideoCore.getInstance().newOutgoingCall("group_" + pdtNumber + "-" + AppManager.getUserDeviceId(), false);
//                        currentUserGroup.setCalling("true");
//                    }
//
//                    AppManager.updateLocalUserGroup(currentUserGroup.toLocalUserGroup());
//                    List<UserGroup> userGroupList = AppManager.getUserGroupList();
//                    setNewData(userGroupList);
//                } else {
//                    ToastUtils.showToast("组号为空");
//                }
//
//                LogUtil.i(TAG,"点击了通话按钮。。。。");


                break;
            case R.id.li_chat:
            case R.id.tab_icon:
            case R.id.rl_group_item:
            case R.id.iv_chat:
                if (isCurrentWaitingGroup(userGroup,position)) return;
                if (userGroup != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(userGroup.getGroupid()).trim());
                    bundle.putString(IMType.Params.MSG_GROUP_TITLE, CommonUtils.emptyIfNull(userGroup.getGroupName()).trim());
                    PageUtils.turnPage(PageConfig.PAGE_ONE, PageConfig.PAGE_ID_GROUP_CHAT, bundle);
                }

                break;
            case R.id.iv_type_icon:
                EventBus.getDefault().post(new WaitingClickEvent(position));
                break;
            case R.id.default_group_select:
                setDefaultGroup(userGroup,position);
                break;

        }
    }


    //设置常用组  by cuizh,0319
    private boolean setDefaultGroup(UserGroup userGroup,int position){

        AlertDialog.Builder  dialog = new AlertDialog.Builder(mContext);
        //获取AlertDialog对象
        if (userGroup.getIsdefault()!=null && userGroup.getIsdefault().equals("1")) {
            dialog.setTitle("是否将组 \""+userGroup.getGroupName()+"\" 移出常用组列表?");
        }else {
            dialog.setTitle("是否将 \""+userGroup.getGroupName()+"\" 加入常用组列表?");
        }

        dialog.setCancelable(false);//设置是否可取消
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override//设置ok的事件
            public void onClick(DialogInterface dialogInterface, int i) {
                //在此处写入ok的逻辑

                if (userGroup.getIsdefault()!=null && userGroup.getIsdefault().equals("1")) {
                    EventBus.getDefault().post(new SetDefaultGroupEvent(position,"0"));
                }else {
                    EventBus.getDefault().post(new SetDefaultGroupEvent(position,"1"));
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

        return true;
    }

    private boolean isCurrentWaitingGroup(UserGroup userGroup,int position) {
        if (!userGroup.getGroupid().equals(AppManager.getUserData().getDiscussionCode())) {

            //设置当前守候组时弹出窗口确认  by cuizh,0506
            AlertDialog.Builder  dialog = new AlertDialog.Builder(mContext);
            //获取AlertDialog对象
            dialog.setTitle("是否将组 \""+userGroup.getGroupName()+"\" 设为当前守候组?");

            dialog.setCancelable(false);//设置是否可取消
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override//设置ok的事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    //在此处写入ok的逻辑
                    EventBus.getDefault().post(new WaitingClickEvent(position));

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

//            EventBus.getDefault().post(new WaitingClickEvent(position));
//            ToastUtils.showLocalToast("切换守候组");
            return true;
        }
        return false;
    }

    /**
     * 邀请其他人加入会议
     *
     * @param sendUserCode
     * @param msgToCode
     * @param pdtNumber
     */
    private void joinOthersInConference(String sendUserCode, String msgToCode, String pdtNumber) {
        String msgContent = IMType.Params.JOIN_CALL_CONFERENCE + ",group_" + pdtNumber;

        webRequestUtil.joinOthersInConference(sendUserCode, msgContent, msgToCode, new RxCallBack<DataPdtGroup>() {
            @Override
            public void onSucessed(DataPdtGroup dataPdtGroup) {

            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

            }
        });
    }

    @Override
    public void stateChanged(Message message, String s) {

    }

    public int getPositionByLocalGroupId(String groupId) {
        if (getData() != null && !TextUtils.isEmpty(groupId)) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i) != null && groupId.equals(((UserGroup)getData().get(i)).getGroupid())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getPositionByLocalRankId(Long rank) {
        if (getData() != null) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i) != null && rank == ((UserGroupRank)getData().get(i)).getRank()) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 更新已读消息界面
     *
     * @param groupId
     */
    public void updateHadReadMsg(String groupId) {
        int position = getPositionByLocalGroupId(groupId);
        if (position >= 0) {
            updateLocalMsgCountHashMap();
            notifyItemChanged(position + getHeaderLayoutCount());
        }
    }

    public class RecViewholder extends BaseViewHolder implements SlideSwapAction {

        public LinearLayout group_item_slide;
        public TextView default_select;

        public RecViewholder(View itemView) {
            super(itemView);
            group_item_slide = itemView.findViewById(R.id.group_item_slide);
            default_select = itemView.findViewById(R.id.default_group_select);
        }

        @Override
        public float getActionWidth() {
            return dip2px(mContext, 100);
        }

        @Override
        public View ItemView() {
            return group_item_slide;
        }

    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
