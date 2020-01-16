package com.aimissu.ptt.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.event.ChangePageEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.ui.fragment.AboutFragment;
import com.aimissu.ptt.ui.fragment.GroupChatFragment;
import com.aimissu.ptt.ui.fragment.GroupFragment;
import com.aimissu.ptt.ui.fragment.LocationFragment;
import com.aimissu.ptt.ui.fragment.OtherFragment;
import com.aimissu.ptt.ui.fragment.PersonalChatFragment;
import com.aimissu.ptt.ui.fragment.PersonalFragment;
import com.aimissu.ptt.ui.fragment.ReplayFragment;
import com.aimissu.ptt.ui.fragment.SetPasswordFragment;
import com.aimissu.ptt.ui.fragment.SettingFragment;
import com.aimissu.ptt.ui.fragment.UserInfoFragment;

import org.greenrobot.eventbus.EventBus;

/**
 */
public class PageUtils {
    /**
     * 更换页面
     *
     * @param position 第几tab 第几个viewpager
     * @param tabID    tabid
     */
    public static void turnPage(int position, int tabID) {
        turnPage(position, tabID, false, null);
    }

    public static void turnPage(int position, int tabID, Bundle bundle) {
        turnPage(position, tabID, false, bundle);
    }

    /**
     * @param position
     * @param tabID
     * @param hideTargetChild 人为隐藏主界面，比如子界面按返回，hideTargetChild=true
     */
    public static void turnPage(int position, int tabID, boolean hideTargetChild, Bundle bundle) {
        EventBus.getDefault().post(new ChangePageEvent(position, tabID, hideTargetChild, bundle));
        if (position == PageConfig.PAGE_ONE && tabID == PageConfig.PAGE_ID_GROUP_CHAT) {
            //群组聊天页
            //设置阅读消息
            AppManager.setHadRedMsgCount(bundle.getString(IMType.Params.MSG_TO_CODE), IMType.MsgFromType.Group.toString());
        } else if (position == PageConfig.PAGE_TWO && tabID == PageConfig.PAGE_ID_CHAT) {
            //个人聊天页
            //设置阅读消息
            AppManager.setHadRedMsgCount(bundle.getString(IMType.Params.MSG_TO_CODE), IMType.MsgFromType.Person.toString());
        }
    }

    public static void turnPage(int position, int tabID, boolean hideTargetChild) {
        turnPage(position, tabID, hideTargetChild, null);
    }

    public static int getIDByPosition(@NonNull Integer position) {
        switch (position) {
            case 0:
                return PageConfig.PAGE_ONE;
            case 1:
                return PageConfig.PAGE_TWO;
            case 2:
                return PageConfig.PAGE_THREE;
            case 3:
                return PageConfig.PAGE_FOUR;
            case 4:
                return PageConfig.PAGE_FIVE;
        }
        return -1;
    }

    public static BaseMvpFragment getFragmentByID(@NonNull int id) {
        BaseMvpFragment baseMvpFragment = null;
        switch (id) {
            case PageConfig.PAGE_ID_GROUP:
                baseMvpFragment = new GroupFragment();
                break;
            case PageConfig.PAGE_ID_PERSONAL:
                baseMvpFragment = new PersonalFragment();
                break;
            case PageConfig.PAGE_ID_LOCATION:
                baseMvpFragment = new LocationFragment();
                break;
            case PageConfig.PAGE_ID_REPLAY:
                baseMvpFragment = new ReplayFragment();
                break;
            case PageConfig.PAGE_ID_OTHER:
                baseMvpFragment = new OtherFragment();
                break;
            case PageConfig.PAGE_ID_USER_INFO:
                baseMvpFragment = new UserInfoFragment();
                break;
            case PageConfig.PAGE_ID_SETTINGS:
                baseMvpFragment = new SettingFragment();
                break;
            case PageConfig.PAGE_ID_ABOUT:
                baseMvpFragment = new AboutFragment();
                break;
            case PageConfig.PAGE_ID_SET_PASSWORD:
                baseMvpFragment = new SetPasswordFragment();
                break;
            case PageConfig.PAGE_ID_CHAT:
                baseMvpFragment = new PersonalChatFragment();
                break;
            case PageConfig.PAGE_ID_GROUP_CHAT:
                baseMvpFragment = new GroupChatFragment();
                break;
        }
        return baseMvpFragment;
    }
}
