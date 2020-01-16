package com.aimissu.ptt.entity.event;

public class NetWorkSateEvent {
    /**
     * 0:网络链接成功
     * -1:网络链接失败
     */
    public int netWorkSate;

    public NetWorkSateEvent(int netWorkSate){
        this.netWorkSate = netWorkSate;
    }
}
