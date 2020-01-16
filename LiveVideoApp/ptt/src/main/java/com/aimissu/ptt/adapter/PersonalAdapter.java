package com.aimissu.ptt.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.event.ManageUserEvent;
import com.aimissu.ptt.entity.event.PersonalLocationEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LocalMsgCount;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.utils.PermissionUtils;
import com.aimissu.ptt.view.widget.RedImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

/**
 */
public class PersonalAdapter extends BaseQuickAdapter<PersonUserEntity, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    public PersonalAdapter(int layoutResId, @Nullable List<PersonUserEntity> data) {
        super(layoutResId, data);
        setOnItemClickListener(this);
        setOnItemChildClickListener(this);
    }

    HashMap<String, LocalMsgCount> localMsgCountHashMap = new HashMap<>();

    public void updateLocalMsgCountHashMap() {
        localMsgCountHashMap = AppManager.getMsgCountList(IMType.MsgFromType.Person.toString());
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonUserEntity item) {
        
        helper.setText(R.id.tv_title, item.getuName());
        helper.setText(R.id.tv_status, "(" + item.getStatus() + ")");
        helper.addOnClickListener(R.id.rl_personal_item);
//        helper.addOnClickListener(R.id.li_chat);
        helper.addOnClickListener(R.id.li_location);
        helper.addOnClickListener(R.id.ll_dizzy);
        helper.addOnClickListener(R.id.ll_die);
        helper.getView(R.id.ll_die).setVisibility(View.VISIBLE);
        helper.getView(R.id.ll_dizzy).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_msg, CommonUtils.emptyIfNull(item.getGroupName()));

        if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE)) {
            helper.getView(R.id.li_location).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.li_location).setVisibility(View.INVISIBLE);
        }

        if (item.getDevicetype().equals("APP")){
            helper.setText(R.id.tv_type, "APP");
        }else {
            helper.setText(R.id.tv_type, "PDT");
        }

            //设置消息数
            RedImageView redImageView = helper.getView(R.id.tab_icon);
            LocalMsgCount localMsgCount = null;
            if (localMsgCountHashMap != null && localMsgCountHashMap.containsKey(item.getuCode().trim())) {
                localMsgCount = localMsgCountHashMap.get(item.getuCode().trim());
                int count = CommonUtils.toInt(localMsgCount.getMsgCount()) - CommonUtils.toInt(localMsgCount.getReadCount());
                if (count <= 0) {
                    count = 0;
                }

//                LogUtil.i("PersonalAdapter","notNull　　　　　　"+item.getuCode().trim()+"   "+localMsgCount.getMsgCount() +"##########    "+localMsgCount.getReadCount());

                redImageView.setText(String.valueOf(count));
            } else {

//                LogUtil.i("PersonalAdapter","Null　　　　　　"+item.getuCode().trim()+"   "+localMsgCount.getMsgCount() +"##########    "+localMsgCount.getReadCount());

                redImageView.setText(String.valueOf(0));
            }


            if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE)) {
                LogUtil.i(TAG, "item.getStatus():    " + item.getStatus());
                helper.setTextColor(R.id.tv_title, Global.mContext.getResources().getColor(R.color.greeny));
                helper.setTextColor(R.id.tv_status, Global.mContext.getResources().getColor(R.color.greeny));
                helper.setTextColor(R.id.tv_msg,  Global.mContext.getResources().getColor(R.color.white));
            } else {
//                helper.setTextColor(R.id.tv_title, Global.mContext.getResources().getColor(R.color.white));
//                helper.setTextColor(R.id.tv_status, Global.mContext.getResources().getColor(R.color.white));
                helper.setTextColor(R.id.tv_title, Color.GRAY);
                helper.setTextColor(R.id.tv_status,Color.GRAY);
                helper.setTextColor(R.id.tv_msg,Color.GRAY);
            }

            if (CommonUtils.emptyIfNull(item.getDevicetype()).equals(IMType.Params.TYPE_PDT)) {
                if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.REMOTE_DIZZY);
                    helper.setText(R.id.tv_die, IMType.Params.REMOTE_KILL);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);

                    if (!PermissionUtils.hasPersonKillPermisson()) {
//                        ToastUtils.showToast("您没有遥毙权限");
                        helper.getView(R.id.ll_die).setVisibility(View.GONE);
                    }
                    if (!PermissionUtils.hasPersonStopPermisson()) {
//                        ToastUtils.showToast("您没有遥晕权限");
                        helper.getView(R.id.ll_dizzy).setVisibility(View.GONE);
                    }
                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.ACTIVATE);
                    helper.setText(R.id.tv_die, IMType.Params.REMOTE_KILL);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);

                    if (!PermissionUtils.hasPersonEnablePermisson()) {
//                        ToastUtils.showToast("您没有激活权限");
                        helper.getView(R.id.ll_dizzy).setVisibility(View.GONE);
                    }
                    if (!PermissionUtils.hasPersonKillPermisson()) {
//                        ToastUtils.showToast("您没有遥晕权限");
                        helper.getView(R.id.ll_die).setVisibility(View.GONE);
                    }
                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.REMOTE_KILL)) {
                    helper.getView(R.id.tv_dizzy).setVisibility(View.GONE);
                    helper.getView(R.id.tv_die).setVisibility(View.GONE);
                }
            } else if (item.getDevicetype().equals(IMType.Params.TYPE_APP)) {
                if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.OFF_LINE)) {
                    helper.setText(R.id.tv_die, IMType.Params.DISABLE);
                    helper.getView(R.id.tv_die).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.GONE);

                    if (!PermissionUtils.hasPersonDisablePermisson()) {
//                        ToastUtils.showToast("您没有禁用权限");
                        helper.getView(R.id.ll_die).setVisibility(View.GONE);
                    }

                } else if (CommonUtils.emptyIfNull(item.getStatus()).equals(IMType.Params.DISABLE)) {
                    helper.setText(R.id.tv_dizzy, IMType.Params.ACTIVATE);
                    helper.getView(R.id.tv_dizzy).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_die).setVisibility(View.GONE);

                    if (!PermissionUtils.hasPersonEnablePermisson()) {
//                        ToastUtils.showToast("您没有激活权限");
                        helper.getView(R.id.ll_dizzy).setVisibility(View.GONE);
                    }
                }
            }


        }

        @Override
        public void onItemClick (BaseQuickAdapter adapter, View view,int position){
//        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT);
        }

        @Override
        public void onItemChildClick (BaseQuickAdapter adapter, View view,int position){
            PersonUserEntity personUserEntity = getItem(position);
            String dizzyString = "";
            String dieString = "";
            switch (view.getId()) {
                case R.id.rl_personal_item:
//                case R.id.li_chat:
                    if (personUserEntity != null && !TextUtils.isEmpty(personUserEntity.getuCode())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(IMType.Params.MSG_TO_CODE, CommonUtils.emptyIfNull(personUserEntity.getuCode()).trim());
                        bundle.putString(IMType.Params.USER_TYPE, CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).trim());
                        bundle.putString(IMType.Params.MSG_PERSONAL_TITLE, CommonUtils.emptyIfNull(personUserEntity.getuName()).trim());
                        bundle.putString(IMType.Params.DEVICE_ID, CommonUtils.emptyIfNull(personUserEntity.getDeviceid()).trim());
                        bundle.putString(IMType.Params.USER_DEPARTMENT, CommonUtils.emptyIfNull(personUserEntity.getdName()).trim());
                        PageUtils.turnPage(PageConfig.PAGE_TWO, PageConfig.PAGE_ID_CHAT, bundle);
                    }
                    break;

                case R.id.ll_dizzy:
                    if (CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dizzyString = IMType.Params.REMOTE_DIZZY;
                        } else if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    } else if (personUserEntity.getDevicetype().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.DISABLE)) {
                            dizzyString = IMType.Params.ACTIVATE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dizzyString, CommonUtils.emptyIfNull(personUserEntity.getuCode()), CommonUtils.emptyIfNull(personUserEntity.getuName())));

                    break;
                case R.id.ll_die:
                    if (CommonUtils.emptyIfNull(personUserEntity.getDevicetype()).equals(IMType.Params.TYPE_PDT)) {
                        if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        } else if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.REMOTE_DIZZY)) {
                            dieString = IMType.Params.REMOTE_KILL;
                        }
                    } else if (personUserEntity.getDevicetype().equals(IMType.Params.TYPE_APP)) {
                        if (CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.ON_LINE) || CommonUtils.emptyIfNull(personUserEntity.getStatus()).equals(IMType.Params.OFF_LINE)) {
                            dieString = IMType.Params.DISABLE;
                        }
                    }
                    EventBus.getDefault().post(new ManageUserEvent(dieString, CommonUtils.emptyIfNull(personUserEntity.getuCode()), CommonUtils.emptyIfNull(personUserEntity.getuName())));
                    break;

                case R.id.li_location:
                    EventBus.getDefault().post(new PersonalLocationEvent(personUserEntity.getuCode()));
                    break;

            }
        }

        public int getPositionByLocalUserCode (String userCode){
            if (getData() != null && !TextUtils.isEmpty(userCode)) {
                for (int i = 0; i < getData().size(); i++) {
                    if (getData().get(i) != null && userCode.equals(getData().get(i).getuCode())) {
                        return i;
                    }
                }
            }
            return -1;
        }

        /**
         * 更新已读消息界面
         *
         * @param userCode
         */
        public void updateHadReadMsg (String userCode){
            int position = getPositionByLocalUserCode(userCode);
            if (position >= 0) {
                updateLocalMsgCountHashMap();
                notifyItemChanged(position + getHeaderLayoutCount());
            }
        }

    }

