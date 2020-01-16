package com.aimissu.basemvp.net.rx;


import io.reactivex.subscribers.DisposableSubscriber;


/**

 * 观察者的实现，自定义基类，相当于回调或消费者等
 */
public abstract class BaseSubscriber<T> extends DisposableSubscriber<T> {
    public static final String TAG = BaseSubscriber.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(">>>", "统一在每个请求前检测网络");
        if (!RxUtils.isNetworkAvailable(RxConfig.getContext())) {
//            Looper.prepare();
//            ToastUtils.showToast("没有网络");
//            Looper.loop();
            onComplete();
        }
    }

    /**
     * 因错误请求结束
     *
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        LogUtil.i(TAG, "BaseSubscriber onError:" + t.getMessage());
        if (RxConfig.isDebug()) {
            t.printStackTrace();
        }
        if (t instanceof ResultExceptionUtils.ResponseThrowable) {
            onError((ResultExceptionUtils.ResponseThrowable) t);
        } else {
            onError(new ResultExceptionUtils.ResponseThrowable(t));
        }
    }

    /**
     * 请求完成
     */
    @Override
    public void onComplete() {
        LogUtil.i(TAG, "BaseSubscriber onComplete:");

    }


    /**
     * 请求结果
     *
     * @param t
     */
    @Override
    public void onNext(T t) {
        onSucessed(t);

    }


    /**
     * 成功后返回的数据
     * 一定有数据，且不会空
     *
     * @param model
     */
    public abstract void onSucessed(T model);

    /**
     * 统一处理后返回的错误
     * e.message 是处理过的消息提示
     *
     * @param e
     */
    public abstract void onError(ResultExceptionUtils.ResponseThrowable e);


}
