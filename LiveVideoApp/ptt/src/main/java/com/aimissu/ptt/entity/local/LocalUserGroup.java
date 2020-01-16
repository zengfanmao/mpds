package com.aimissu.ptt.entity.local;

import com.aimissu.ptt.entity.ui.UserGroup;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {@Index(value = "groupid DESC")})
public class LocalUserGroup {
    public Long id;
    @Id
    public String groupid;
    public String type;
    public String account;
    public String name;
    public String dept;
    public String deptid;
    public String createtime;
    public String updatetime;
    public String groupName;
    public String sendUserName;
    public String sendUserCode;
    public String msgContent;
    public String msgType;
    public String userCount;
    public String uIsUnilt;
    public String calling;
    public String related;
    public String isdefault;
    @Generated(hash = 1516183382)
    public LocalUserGroup(Long id, String groupid, String type, String account,
            String name, String dept, String deptid, String createtime,
            String updatetime, String groupName, String sendUserName,
            String sendUserCode, String msgContent, String msgType,
            String userCount, String uIsUnilt, String calling, String related,
            String isdefault) {
        this.id = id;
        this.groupid = groupid;
        this.type = type;
        this.account = account;
        this.name = name;
        this.dept = dept;
        this.deptid = deptid;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.groupName = groupName;
        this.sendUserName = sendUserName;
        this.sendUserCode = sendUserCode;
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.userCount = userCount;
        this.uIsUnilt = uIsUnilt;
        this.calling = calling;
        this.related = related;
        this.isdefault = isdefault;
    }
    @Generated(hash = 1246966713)
    public LocalUserGroup() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroupid() {
        return this.groupid;
    }
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDept() {
        return this.dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getDeptid() {
        return this.deptid;
    }
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }
    public String getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public String getUpdatetime() {
        return this.updatetime;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UserGroup toUserGroup(){
        UserGroup userGroup = new UserGroup();
        userGroup.id=id;
        userGroup.groupid=groupid;
        userGroup.type=type;
        userGroup.account=account;
        userGroup.name=name;
        userGroup.dept=dept;
        userGroup.deptid=deptid;
        userGroup.createtime=createtime;
        userGroup.updatetime=updatetime;
        userGroup.groupName=groupName;
        userGroup.sendUserName=sendUserName;
        userGroup.msgContent=msgContent;
        userGroup.msgType=msgType;
        userGroup.sendUserCode=sendUserCode;
        userGroup.userCount=userCount;
        userGroup.uIsUnilt=uIsUnilt;
        userGroup.calling=calling;
        userGroup.related=related;
        userGroup.isdefault=isdefault;
        return userGroup;
    }
    public String getSendUserName() {
        return this.sendUserName;
    }
    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
    public String getMsgContent() {
        return this.msgContent;
    }
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    public String getMsgType() {
        return this.msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


    @Override
    public String toString() {
        return "LocalUserGroup{" +
                "id=" + id +
                ", groupid='" + groupid + '\'' +
                ", type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", deptid='" + deptid + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", groupName='" + groupName + '\'' +
                ", sendUserName='" + sendUserName + '\'' +
                ", sendUserCode='" + sendUserCode + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", msgType='" + msgType + '\'' +
                ", userCount='" + userCount + '\'' +
                ", uIsUnilt='" + uIsUnilt + '\'' +
                ", calling='" + calling + '\'' +
                ", related='" + related + '\'' +
                ", isdefault='" + isdefault + '\'' +
                '}';
    }

    public String getSendUserCode() {
        return this.sendUserCode;
    }
    public void setSendUserCode(String sendUserCode) {
        this.sendUserCode = sendUserCode;
    }
    public String getUserCount() {
        return this.userCount;
    }
    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
    public String getUIsUnilt() {
        return this.uIsUnilt;
    }
    public void setUIsUnilt(String uIsUnilt) {
        this.uIsUnilt = uIsUnilt;
    }
    public String getCalling() {
        return this.calling;
    }
    public void setCalling(String calling) {
        this.calling = calling;
    }
    public String getRelated() {
        return this.related;
    }
    public void setRelated(String related) {
        this.related = related;
    }
    public String getIsdefault() {
        return this.isdefault;
    }
    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }
}
