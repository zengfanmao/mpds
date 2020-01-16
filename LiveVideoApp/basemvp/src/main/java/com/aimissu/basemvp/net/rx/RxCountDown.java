package com.aimissu.basemvp.net.rx;

/**
 * rxJava写的倒计时
 */

public class RxCountDown {

    private  long MAX_TIME = 10;

    public static class Builder {

        long MAX_TIME = 10;

        public Builder setMaxTime(Long l) {
            this.MAX_TIME = l;
            return this;
        }

        public RxCountDown builder() {
            return  new RxCountDown(this);
        }



    }
    public RxCountDown(Builder builder)
    {
        this.MAX_TIME=builder.MAX_TIME;
    }
}
