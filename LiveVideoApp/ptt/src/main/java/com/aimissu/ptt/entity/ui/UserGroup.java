package com.aimissu.ptt.entity.ui;

import com.aimissu.ptt.entity.local.LocalUserGroup;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class UserGroup implements MultiItemEntity {
    /**
     "id": 1,
     "groupid": "10009",
     "type": "APP",
     "account": "admin",
     "name": "admin",
     "dept": "123",
     "deptid": 123,
     "createtime": "2018-09-01T16:51:30",
     "updatetime": "2018-09-01T16:51:33",
     "groupName": "10009"
     */
    public Long id;
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

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public String getSendUserCode() {
        return sendUserCode;
    }

    public void setSendUserCode(String sendUserCode) {
        this.sendUserCode = sendUserCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalUserGroup toLocalUserGroup(){
        LocalUserGroup localUserGroup = new LocalUserGroup();
        localUserGroup.id=id;
        localUserGroup.groupid=groupid;
        localUserGroup.type=type;
        localUserGroup.account=account;
        localUserGroup.name=name;
        localUserGroup.dept=dept;
        localUserGroup.deptid=deptid;
        localUserGroup.createtime=createtime;
        localUserGroup.updatetime=updatetime;
        localUserGroup.groupName=groupName;
        localUserGroup.sendUserName=sendUserName;
        localUserGroup.msgContent=msgContent;
        localUserGroup.msgType=msgType;
        localUserGroup.sendUserCode=sendUserCode;
        localUserGroup.userCount=userCount;
        localUserGroup.uIsUnilt=uIsUnilt;
        localUserGroup.calling=calling;
        localUserGroup.related=related;
        localUserGroup.isdefault=isdefault;
        return localUserGroup;
    }

    public String getuIsUnilt() {
        return uIsUnilt;
    }

    public void setuIsUnilt(String uIsUnilt) {
        this.uIsUnilt = uIsUnilt;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
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

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    @Override
    public int getItemType() {
        return 1;
    }

}
