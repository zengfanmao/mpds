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
public class LocalServer {
    @Id
    public Long ID;

    public String SERVER_IP;

    public String VIDEO_PORT;

    @Generated(hash = 150484024)
    public LocalServer(Long ID, String SERVER_IP, String VIDEO_PORT) {
        this.ID = ID;
        this.SERVER_IP = SERVER_IP;
        this.VIDEO_PORT = VIDEO_PORT;
    }

    @Generated(hash = 523612907)
    public LocalServer() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSERVER_IP() {
        return this.SERVER_IP;
    }

    public void setSERVER_IP(String SERVER_IP) {
        this.SERVER_IP = SERVER_IP;
    }

    public String getVIDEO_PORT() {
        return this.VIDEO_PORT;
    }

    public void setVIDEO_PORT(String VIDEO_PORT) {
        this.VIDEO_PORT = VIDEO_PORT;
    }




}
