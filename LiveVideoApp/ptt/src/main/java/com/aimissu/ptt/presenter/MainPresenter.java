package com.aimissu.ptt.presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.ptt.R;
import com.aimissu.ptt.view.IMainView;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

/**

 */
public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {

    public MainPresenter(IMainView baseView) {
        super(baseView);
    }


    @Override
    public ArrayList<NavigationTabBar.Model> buildTab(Context context) {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#055757");
        models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(context,R.mipmap.tab_group), bgColor)
                        .title("群组")
                        .badgeTitle("")
                        .build()
        );
        models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(context,R.mipmap.tab_personal), bgColor)
                        .title("个人")
                        .badgeTitle("")
                        .build()
        );
        models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(context,R.mipmap.tab_location), bgColor)
                        .title("定位")
                        .badgeTitle("")
                        .build()
        );
        models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(context,R.mipmap.tab_replay), bgColor)
                        .title("回放")
                        .badgeTitle("")
                        .build()
        );
        models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(context,R.mipmap.tab_other), bgColor)
                        .title("其他")
                        .badgeTitle("")
                        .build()
        );

        return models;
    }
}
