package com.aimissu.basemvp.mvp;

import java.lang.ref.WeakReference;

/**

 */

public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter {

    private WeakReference<T> mWeakReference;

    public BasePresenter(T baseView) {
        setView(baseView);
        bind();
    }

    public T getView() {
        return mWeakReference != null ? mWeakReference.get() : null;
    }

    public void setView(T view) {
        if (view != null) {
            this.mWeakReference = new WeakReference<>(view);
        }
    }

    @Override
    public void unBind() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    @Override
    public void bind() {

    }
}