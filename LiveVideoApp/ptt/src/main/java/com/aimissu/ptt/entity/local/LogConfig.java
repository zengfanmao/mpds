package com.aimissu.ptt.entity.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * @author：dz-hexiang on 2018/10/15.
 * @email：472482006@qq.com
 */
@Entity(indexes = {@Index(value = "ID DESC")})
public class LogConfig {
    @Id
    public Long ID;

    public String serverApi;

    public String isUpload;

    public String isStop;

    @Generated(hash = 316817836)
    public LogConfig(Long ID, String serverApi, String isUpload, String isStop) {
        this.ID = ID;
        this.serverApi = serverApi;
        this.isUpload = isUpload;
        this.isStop = isStop;
    }

    @Generated(hash = 503206013)
    public LogConfig() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getServerApi() {
        return this.serverApi;
    }

    public void setServerApi(String serverApi) {
        this.serverApi = serverApi;
    }

    public String getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public String getIsStop() {
        return this.isStop;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }

   


}
