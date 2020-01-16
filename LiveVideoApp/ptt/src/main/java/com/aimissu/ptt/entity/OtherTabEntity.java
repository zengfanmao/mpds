package com.aimissu.ptt.entity;

import com.aimissu.ptt.R;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class OtherTabEntity {
    public static final int TYPE_SETTING = 1;
    public static final int TYPE_ABOUT = 2;
    public static final int TYPE_USERINFO = 3;
    public String title;
    public int icon;
    public int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OtherTabEntity(String title, int icon, int id) {
        this.title = title;
        this.icon = icon;
        this.id = id;
    }

    public static List<OtherTabEntity> buidData() {
        List<OtherTabEntity> otherTabEntities = new ArrayList<>();
        otherTabEntities.add(new OtherTabEntity("个人信息", R.mipmap.other_userinfo, TYPE_USERINFO));
        otherTabEntities.add(new OtherTabEntity("关于", R.mipmap.other_about, TYPE_ABOUT));
        otherTabEntities.add(new OtherTabEntity("设置", R.mipmap.other_setting, TYPE_SETTING));
        return otherTabEntities;
    }
}
