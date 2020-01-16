package com.aimissu.basemvp.mvp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aimissu.basemvp.base.LazyLoadFragment;
import com.aimissu.basemvp.dialog.WaitingProgressDialog;
import com.aimissu.basemvp.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**

 */

public abstract class BaseMvpFragment<T extends IBasePresenter> extends LazyLoadFragment {

    protected T mPresenter;
    private WaitingProgressDialog mProgressDialog;
    private ViewGroup mRootView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        checkPresenter();
        if (mPresenter != null) {
            mPresenter.bind();
        }
        if (isSupportEventBus()) {
            EventBus.getDefault().register(this);
        }
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        mRootView = rootView;
        return rootView;
    }

    protected abstract int getLayoutId();

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.unBind();
        }
        if (isSupportEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        hideProgressDialog();
        super.onDestroy();
    }


    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingProgressDialog.create(getActivity(), msg, false, null);
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

    public boolean isSupportEventBus() {
        return false;
    }


    @Override
    protected void lazyInitData() {

    }

    public void showToastMsg(String msg) {
        ToastUtils.showToast(msg);
    }

    public void checkPresenter() {
        if (this.mPresenter == null) {
            this.mPresenter = createPresenter();
        }
    }

    public abstract void onBack();

    public ViewGroup getmRootView() {
        return mRootView;
    }
}
