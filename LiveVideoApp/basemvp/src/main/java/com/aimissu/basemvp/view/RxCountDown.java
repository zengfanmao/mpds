package com.aimissu.basemvp.view;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**

 * Rxjava2写的倒计时
 */

public class RxCountDown {
    private long maxTime;
    private Flowable mFlowable;
    private Disposable mDisposable;
    private Consumer subscriber;

    private RxCountDown(Builder builder) {
        this.maxTime = builder.MaxTime;
        this.subscriber = builder.subscriber;
        mFlowable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(maxTime + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return maxTime - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 开始
     */
    public void start() {
        if (subscriber != null)
            mDisposable = mFlowable.subscribe(subscriber);
    }

    /**
     * 结束
     */
    public void stop() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
    }


    public static class Builder {
        private long MaxTime;
        private Consumer subscriber;

        public Builder setMaxTime(long maxTime) {
            this.MaxTime = maxTime;
            return this;
        }

        public Builder setSubscriber(Consumer subscriber) {
            this.subscriber = subscriber;
            return this;
        }

        public RxCountDown build() {
            return new RxCountDown(this);
        }
    }

}
