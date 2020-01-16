package com.aimissu.ptt.entity.ui;

import android.support.annotation.NonNull;

import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LocalPersonUserEntity;
import com.aimissu.ptt.utils.CommonUtils;

public class PersonUserEntity implements Comparable<PersonUserEntity> {
    /**
     * "ID": 8,
     * "isDel": 0,
     * "recSN": null,
     * "uCode": "1001",
     * "uPassword": "87FA6AD6CBFDF3108E4DD6F47F5D04A4",
     * "uSalt": "24V0XZ",
     * "uBelong": "",
     * "uIsActive": 0,
     * "rName": "前台用户",
     * "pcNum": null,
     * "uName": "李小王",
     * "uSex": null,
     * "uDuty": "警员",
     * "dCode": "S0006",
     * "uTel": "13809782354",
     * "uShortNum": "334674",
     * "uHeadPortrait": "",
     * "dName": "海珠区公安局",
     * "LYCID": null,
     * "loginFailTimes": null,
     * "lastLoginTime": null,
     * "uRemarks": null,
     * "Createtime": "2018-09-03T15:01:10",
     * "uDepartment": "海珠区公安局",
     * "accountType": "特种警察",
     * "uEmployeenum": null,
     * "uIshistory": null,
     * "uIsUnilt": null,
     * "uIsAccontion": null,
     * "uUnitCode": "",
     * "roleid": 3,
     * "roleType": 3,
     * "groupid": "F009",
     * "groupName": "专案组",
     * "deviceid": null,
     * "deviceESN": null,
     * "devicetype": "APP",
     * "status": ""
     */

    public Long ID;
    public String isDel;
    public String recSN;
    public String uCode;
    public String uPassword;
    public String uSalt;
    public String uBelong;
    public String uIsActive;
    public String rName;
    public String pcNum;
    public String uName;
    public String uSex;
    public String uDuty;
    public String dCode;
    public String uTel;
    public String uShortNum;
    public String uHeadPortrait;
    public String dName;
    public String LYCID;
    public String loginFailTimes;
    public String lastLoginTime;
    public String uRemarks;
    public String Createtime;
    public String uDepartment;
    public String accountType;
    public String uEmployeenum;
    public String uIshistory;
    public String uIsUnilt;
    public String uIsAccontion;
    public String uUnitCode;
    public String roleid;
    public String roleType;
    public String groupid;
    public String groupName;
    public String deviceid;
    public String deviceESN;
    public String devicetype;
    public String status;
    public String sendUserName;
    public String sendUserCode;
    public String msgContent;
    public String msgType;

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserCode() {
        return sendUserCode;
    }

    public void setSendUserCode(String sendUserCode) {
        this.sendUserCode = sendUserCode;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getRecSN() {
        return recSN;
    }

    public void setRecSN(String recSN) {
        this.recSN = recSN;
    }

    public String getuCode() {
        return uCode;
    }

    public void setuCode(String uCode) {
        this.uCode = uCode;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuSalt() {
        return uSalt;
    }

    public void setuSalt(String uSalt) {
        this.uSalt = uSalt;
    }

    public String getuBelong() {
        return uBelong;
    }

    public void setuBelong(String uBelong) {
        this.uBelong = uBelong;
    }

    public String getuIsActive() {
        return uIsActive;
    }

    public void setuIsActive(String uIsActive) {
        this.uIsActive = uIsActive;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getPcNum() {
        return pcNum;
    }

    public void setPcNum(String pcNum) {
        this.pcNum = pcNum;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuDuty() {
        return uDuty;
    }

    public void setuDuty(String uDuty) {
        this.uDuty = uDuty;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public String getuShortNum() {
        return uShortNum;
    }

    public void setuShortNum(String uShortNum) {
        this.uShortNum = uShortNum;
    }

    public String getuHeadPortrait() {
        return uHeadPortrait;
    }

    public void setuHeadPortrait(String uHeadPortrait) {
        this.uHeadPortrait = uHeadPortrait;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getLYCID() {
        return LYCID;
    }

    public void setLYCID(String LYCID) {
        this.LYCID = LYCID;
    }

    public String getLoginFailTimes() {
        return loginFailTimes;
    }

    public void setLoginFailTimes(String loginFailTimes) {
        this.loginFailTimes = loginFailTimes;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getuRemarks() {
        return uRemarks;
    }

    public void setuRemarks(String uRemarks) {
        this.uRemarks = uRemarks;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String createtime) {
        Createtime = createtime;
    }

    public String getuDepartment() {
        return uDepartment;
    }

    public void setuDepartment(String uDepartment) {
        this.uDepartment = uDepartment;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getuEmployeenum() {
        return uEmployeenum;
    }

    public void setuEmployeenum(String uEmployeenum) {
        this.uEmployeenum = uEmployeenum;
    }

    public String getuIshistory() {
        return uIshistory;
    }

    public void setuIshistory(String uIshistory) {
        this.uIshistory = uIshistory;
    }

    public String getuIsUnilt() {
        return uIsUnilt;
    }

    public void setuIsUnilt(String uIsUnilt) {
        this.uIsUnilt = uIsUnilt;
    }

    public String getuIsAccontion() {
        return uIsAccontion;
    }

    public void setuIsAccontion(String uIsAccontion) {
        this.uIsAccontion = uIsAccontion;
    }

    public String getuUnitCode() {
        return uUnitCode;
    }

    public void setuUnitCode(String uUnitCode) {
        this.uUnitCode = uUnitCode;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceESN() {
        return deviceESN;
    }

    public void setDeviceESN(String deviceESN) {
        this.deviceESN = deviceESN;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PersonUserEntity{" +
                "ID=" + ID +
                ", isDel='" + isDel + '\'' +
                ", recSN='" + recSN + '\'' +
                ", uCode='" + uCode + '\'' +
                ", uPassword='" + uPassword + '\'' +
                ", uSalt='" + uSalt + '\'' +
                ", uBelong='" + uBelong + '\'' +
                ", uIsActive='" + uIsActive + '\'' +
                ", rName='" + rName + '\'' +
                ", pcNum='" + pcNum + '\'' +
                ", uName='" + uName + '\'' +
                ", uSex='" + uSex + '\'' +
                ", uDuty='" + uDuty + '\'' +
                ", dCode='" + dCode + '\'' +
                ", uTel='" + uTel + '\'' +
                ", uShortNum='" + uShortNum + '\'' +
                ", uHeadPortrait='" + uHeadPortrait + '\'' +
                ", dName='" + dName + '\'' +
                ", LYCID='" + LYCID + '\'' +
                ", loginFailTimes='" + loginFailTimes + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", uRemarks='" + uRemarks + '\'' +
                ", Createtime='" + Createtime + '\'' +
                ", uDepartment='" + uDepartment + '\'' +
                ", accountType='" + accountType + '\'' +
                ", uEmployeenum='" + uEmployeenum + '\'' +
                ", uIshistory='" + uIshistory + '\'' +
                ", uIsUnilt='" + uIsUnilt + '\'' +
                ", uIsAccontion='" + uIsAccontion + '\'' +
                ", uUnitCode='" + uUnitCode + '\'' +
                ", roleid='" + roleid + '\'' +
                ", roleType='" + roleType + '\'' +
                ", groupid='" + groupid + '\'' +
                ", groupName='" + groupName + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", deviceESN='" + deviceESN + '\'' +
                ", devicetype='" + devicetype + '\'' +
                ", status='" + status + '\'' +
                ", sendUserName='" + sendUserName + '\'' +
                ", sendUserCode='" + sendUserCode + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", msgType='" + msgType + '\'' +
                '}';
    }

    public LocalPersonUserEntity toLocalPersonUserEntity() {
        LocalPersonUserEntity localPersonUserEntity = new LocalPersonUserEntity();
        localPersonUserEntity.ID = ID;
        localPersonUserEntity.isDel = isDel;
        localPersonUserEntity.recSN = recSN;
        localPersonUserEntity.uCode = uCode;
        localPersonUserEntity.uPassword = uPassword;
        localPersonUserEntity.uSalt = uSalt;
        localPersonUserEntity.uBelong = uBelong;
        localPersonUserEntity.uIsActive = uIsActive;
        localPersonUserEntity.rName = rName;
        localPersonUserEntity.pcNum = pcNum;
        localPersonUserEntity.uName = uName;
        localPersonUserEntity.uSex = uSex;
        localPersonUserEntity.uDuty = uDuty;
        localPersonUserEntity.dCode = dCode;
        localPersonUserEntity.uTel = uTel;
        localPersonUserEntity.uShortNum = uShortNum;
        localPersonUserEntity.uHeadPortrait = uHeadPortrait;
        localPersonUserEntity.dName = dName;
        localPersonUserEntity.LYCID = LYCID;
        localPersonUserEntity.loginFailTimes = loginFailTimes;
        localPersonUserEntity.lastLoginTime = lastLoginTime;
        localPersonUserEntity.uRemarks = uRemarks;
        localPersonUserEntity.Createtime = Createtime;
        localPersonUserEntity.uDepartment = uDepartment;
        localPersonUserEntity.accountType = accountType;
        localPersonUserEntity.uEmployeenum = uEmployeenum;
        localPersonUserEntity.uIshistory = uIshistory;
        localPersonUserEntity.uIsUnilt = uIsUnilt;
        localPersonUserEntity.uIsAccontion = uIsAccontion;
        localPersonUserEntity.uUnitCode = uUnitCode;
        localPersonUserEntity.roleid = roleid;
        localPersonUserEntity.roleType = roleType;
        localPersonUserEntity.groupid = groupid;
        localPersonUserEntity.groupName = groupName;
        localPersonUserEntity.deviceid = deviceid;
        localPersonUserEntity.deviceESN = deviceESN;
        localPersonUserEntity.devicetype = devicetype;
        localPersonUserEntity.status = status;
        localPersonUserEntity.sendUserName = sendUserName;
        localPersonUserEntity.sendUserCode = sendUserCode;
        localPersonUserEntity.msgContent = msgContent;
        localPersonUserEntity.msgType = msgType;

        return localPersonUserEntity;
    }

    @Override
    public int compareTo(@NonNull PersonUserEntity o) {
        if (o == null)
            return 1;
        if (CommonUtils.emptyIfNull(o.getStatus()).equals(IMType.Params.ON_LINE)) {
            if (!CommonUtils.emptyIfNull(this.getStatus()).equals(IMType.Params.ON_LINE)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
