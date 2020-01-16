package com.aimissu.basemvp.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aimissu.basemvp.R;
import com.aimissu.basemvp.dialog.WaitingProgressDialog;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.FixLeakUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    protected FrameLayout mContentLayout;
    private WaitingProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG, geSubClassName() + ":onCreate");
        setContentView(R.layout.activity_layout_base);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            LayoutInflater.from(this).inflate(layoutId, mContentLayout, true);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.tv_center_title);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // 默认居左title不可用
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        configToolBar(mToolbar, mToolbarTitle);
        beforeInitData();
        initData(savedInstanceState, getIntent());
        if (isSupportEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LogUtil.i(TAG, geSubClassName() + ":onDestroy");
        hideProgressDialog();
        if (isSupportEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        mProgressDialog = null;
        FixLeakUtils.fixFocusedViewLeak(getApplication());
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void configToolBar(Toolbar toolbar, TextView title);

    protected abstract void initData(@Nullable Bundle savedInstanceState, Intent intent);

    public void hideToolBarLayout(boolean hide) {
        int visibility = hide ? View.GONE : View.VISIBLE;
        if (mToolbar != null) {
            mToolbar.setVisibility(visibility);
        }
    }

    protected void beforeInitData() {

    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingProgressDialog.create(this, msg, false, null);
        }
        try {
            mProgressDialog.setContentText(TextUtils.isEmpty(msg) ? "" : msg);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public boolean isSupportEventBus() {
        return false;
    }

    public String geSubClassName() {
        Class<?> mClass = this.getClass();
        return mClass == null ? "getClass is null" : mClass.getSimpleName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, geSubClassName() + ":onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, geSubClassName() + ":onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i(TAG, geSubClassName() + ":onRestart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        LogUtil.i(TAG, geSubClassName() + ":onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(TAG, geSubClassName() + ":onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        printLifeLog("onStop");
    }

    private void printLifeLog(String liveMethod) {
        LogUtil.i(TAG, geSubClassName() + ":" + liveMethod);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        printLifeLog("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        printLifeLog("onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        printLifeLog("onConfigurationChanged");
    }
}
