package com.aimissu.basemvp.mvp;


import com.aimissu.basemvp.base.BaseActivity;
import com.aimissu.basemvp.utils.ToastUtils;

/**

 */

public abstract class BaseMvpActivity<T extends IBasePresenter> extends BaseActivity {

    protected T mPresenter;

    public static int flag = -1;

    @Override
    protected void beforeInitData() {
        super.beforeInitData();
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.bind();
        }
    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.unBind();
        }
        super.onDestroy();
    }

    public void showToastMsg(String msg) {
        ToastUtils.showToast(msg);
    }
}
