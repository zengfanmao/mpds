package com.aimissu.ptt.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.ptt.utils.PageUtils;

import java.util.HashMap;

/**

 */
public class MainAdapter extends FragmentStatePagerAdapter {

    volatile HashMap<Integer, BaseMvpFragment> fragmentHashMap = new HashMap<>();
    volatile HashMap<Integer, Integer> tabIsTop = new HashMap<>();

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseMvpFragment baseMvpFragment = getFragmentByID(PageUtils.getIDByPosition(position));
        return baseMvpFragment;
    }

    @Override
    public int getCount() {
        return 5;
    }


    public BaseMvpFragment getFragmentByID(@NonNull int id) {
        synchronized (this) {
            if (!fragmentHashMap.containsKey(id)) {
                fragmentHashMap.put(id, PageUtils.getFragmentByID(id));
            }
        }
        return fragmentHashMap.get(id);
    }

    public void setTabTopID(int position, int pageID) {
        synchronized (this) {
            this.tabIsTop.put(position, pageID);
        }

    }

    public int getTabTopID(int position) {
        return tabIsTop != null && tabIsTop.containsKey(position) ? tabIsTop.get(position) : -1;
    }
}

