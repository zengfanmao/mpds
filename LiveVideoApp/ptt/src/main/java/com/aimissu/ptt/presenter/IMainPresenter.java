package com.aimissu.ptt.presenter;

import android.content.Context;

import com.aimissu.basemvp.mvp.IBasePresenter;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

/**

 */
public interface IMainPresenter extends IBasePresenter {
     ArrayList<NavigationTabBar.Model> buildTab(Context context);
}
