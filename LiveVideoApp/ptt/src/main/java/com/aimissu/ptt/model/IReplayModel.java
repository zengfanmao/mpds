package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataReplay;

/**

 */
public interface IReplayModel {

     void getAudioMsg(String userCode, String msgFromType, String rName, String startTime, String endTime, int pageNum, int pageSize, RxCallBack<DataReplay> rxCallBack);
}
