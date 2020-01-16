package com.aimissu.ptt.entity.local;

import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.grgbanking.video.VideoCore;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

    //将表格主键改为uCode  by cuizh,0404
@Entity(indexes = {@Index(value = "uCode DESC")})
public class LocalPersonUserEntity {
    public Long ID;
    public String isDel;
    public String recSN;
    @Id
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
    @Generated(hash = 915642545)
    public LocalPersonUserEntity(Long ID, String isDel, String recSN, String uCode,
            String uPassword, String uSalt, String uBelong, String uIsActive,
            String rName, String pcNum, String uName, String uSex, String uDuty,
            String dCode, String uTel, String uShortNum, String uHeadPortrait,
            String dName, String LYCID, String loginFailTimes, String lastLoginTime,
            String uRemarks, String Createtime, String uDepartment,
            String accountType, String uEmployeenum, String uIshistory,
            String uIsUnilt, String uIsAccontion, String uUnitCode, String roleid,
            String roleType, String groupid, String groupName, String deviceid,
            String deviceESN, String devicetype, String status, String sendUserName,
            String sendUserCode, String msgContent, String msgType) {
        this.ID = ID;
        this.isDel = isDel;
        this.recSN = recSN;
        this.uCode = uCode;
        this.uPassword = uPassword;
        this.uSalt = uSalt;
        this.uBelong = uBelong;
        this.uIsActive = uIsActive;
        this.rName = rName;
        this.pcNum = pcNum;
        this.uName = uName;
        this.uSex = uSex;
        this.uDuty = uDuty;
        this.dCode = dCode;
        this.uTel = uTel;
        this.uShortNum = uShortNum;
        this.uHeadPortrait = uHeadPortrait;
        this.dName = dName;
        this.LYCID = LYCID;
        this.loginFailTimes = loginFailTimes;
        this.lastLoginTime = lastLoginTime;
        this.uRemarks = uRemarks;
        this.Createtime = Createtime;
        this.uDepartment = uDepartment;
        this.accountType = accountType;
        this.uEmployeenum = uEmployeenum;
        this.uIshistory = uIshistory;
        this.uIsUnilt = uIsUnilt;
        this.uIsAccontion = uIsAccontion;
        this.uUnitCode = uUnitCode;
        this.roleid = roleid;
        this.roleType = roleType;
        this.groupid = groupid;
        this.groupName = groupName;
        this.deviceid = deviceid;
        this.deviceESN = deviceESN;
        this.devicetype = devicetype;
        this.status = status;
        this.sendUserName = sendUserName;
        this.sendUserCode = sendUserCode;
        this.msgContent = msgContent;
        this.msgType = msgType;
    }
    @Generated(hash = 2005374202)
    public LocalPersonUserEntity() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getIsDel() {
        return this.isDel;
    }
    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
    public String getRecSN() {
        return this.recSN;
    }
    public void setRecSN(String recSN) {
        this.recSN = recSN;
    }
    public String getUCode() {
        return this.uCode;
    }
    public void setUCode(String uCode) {
        this.uCode = uCode;
    }
    public String getUPassword() {
        return this.uPassword;
    }
    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }
    public String getUSalt() {
        return this.uSalt;
    }
    public void setUSalt(String uSalt) {
        this.uSalt = uSalt;
    }
    public String getUBelong() {
        return this.uBelong;
    }
    public void setUBelong(String uBelong) {
        this.uBelong = uBelong;
    }
    public String getUIsActive() {
        return this.uIsActive;
    }
    public void setUIsActive(String uIsActive) {
        this.uIsActive = uIsActive;
    }
    public String getRName() {
        return this.rName;
    }
    public void setRName(String rName) {
        this.rName = rName;
    }
    public String getPcNum() {
        return this.pcNum;
    }
    public void setPcNum(String pcNum) {
        this.pcNum = pcNum;
    }
    public String getUName() {
        return this.uName;
    }
    public void setUName(String uName) {
        this.uName = uName;
    }
    public String getUSex() {
        return this.uSex;
    }
    public void setUSex(String uSex) {
        this.uSex = uSex;
    }
    public String getUDuty() {
        return this.uDuty;
    }
    public void setUDuty(String uDuty) {
        this.uDuty = uDuty;
    }
    public String getDCode() {
        return this.dCode;
    }
    public void setDCode(String dCode) {
        this.dCode = dCode;
    }
    public String getUTel() {
        return this.uTel;
    }
    public void setUTel(String uTel) {
        this.uTel = uTel;
    }
    public String getUShortNum() {
        return this.uShortNum;
    }
    public void setUShortNum(String uShortNum) {
        this.uShortNum = uShortNum;
    }
    public String getUHeadPortrait() {
        return this.uHeadPortrait;
    }
    public void setUHeadPortrait(String uHeadPortrait) {
        this.uHeadPortrait = uHeadPortrait;
    }
    public String getDName() {
        return this.dName;
    }
    public void setDName(String dName) {
        this.dName = dName;
    }
    public String getLYCID() {
        return this.LYCID;
    }
    public void setLYCID(String LYCID) {
        this.LYCID = LYCID;
    }
    public String getLoginFailTimes() {
        return this.loginFailTimes;
    }
    public void setLoginFailTimes(String loginFailTimes) {
        this.loginFailTimes = loginFailTimes;
    }
    public String getLastLoginTime() {
        return this.lastLoginTime;
    }
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public String getURemarks() {
        return this.uRemarks;
    }
    public void setURemarks(String uRemarks) {
        this.uRemarks = uRemarks;
    }
    public String getCreatetime() {
        return this.Createtime;
    }
    public void setCreatetime(String Createtime) {
        this.Createtime = Createtime;
    }
    public String getUDepartment() {
        return this.uDepartment;
    }
    public void setUDepartment(String uDepartment) {
        this.uDepartment = uDepartment;
    }
    public String getAccountType() {
        return this.accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getUEmployeenum() {
        return this.uEmployeenum;
    }
    public void setUEmployeenum(String uEmployeenum) {
        this.uEmployeenum = uEmployeenum;
    }
    public String getUIshistory() {
        return this.uIshistory;
    }
    public void setUIshistory(String uIshistory) {
        this.uIshistory = uIshistory;
    }
    public String getUIsUnilt() {
        return this.uIsUnilt;
    }
    public void setUIsUnilt(String uIsUnilt) {
        this.uIsUnilt = uIsUnilt;
    }
    public String getUIsAccontion() {
        return this.uIsAccontion;
    }
    public void setUIsAccontion(String uIsAccontion) {
        this.uIsAccontion = uIsAccontion;
    }
    public String getUUnitCode() {
        return this.uUnitCode;
    }
    public void setUUnitCode(String uUnitCode) {
        this.uUnitCode = uUnitCode;
    }
    public String getRoleid() {
        return this.roleid;
    }
    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
    public String getRoleType() {
        return this.roleType;
    }
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    public String getGroupid() {
        return this.groupid;
    }
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getDeviceid() {
        return this.deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getDeviceESN() {
        return this.deviceESN;
    }
    public void setDeviceESN(String deviceESN) {
        this.deviceESN = deviceESN;
    }
    public String getDevicetype() {
        return this.devicetype;
    }
    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LocalPersonUserEntity{" +
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

    public PersonUserEntity toModel(){
        PersonUserEntity personUserEntity = new PersonUserEntity();
        personUserEntity.ID=ID;
        personUserEntity.isDel=isDel;
        personUserEntity.recSN=recSN;
        personUserEntity.uCode=uCode;
        personUserEntity.uPassword=uPassword;
        personUserEntity.uSalt=uSalt;
        personUserEntity.uBelong=uBelong;
        personUserEntity.uIsActive=uIsActive;
        personUserEntity.rName=rName;
        personUserEntity.pcNum=pcNum;
        personUserEntity.uName=uName;
        personUserEntity.uSex=uSex;
        personUserEntity.uDuty=uDuty;
        personUserEntity.dCode=dCode;
        personUserEntity.uTel=uTel;
        personUserEntity.uShortNum=uShortNum;
        personUserEntity.uHeadPortrait=uHeadPortrait;
        personUserEntity.dName=dName;
        personUserEntity.LYCID=LYCID;
        personUserEntity.loginFailTimes=loginFailTimes;
        personUserEntity.lastLoginTime=lastLoginTime;
        personUserEntity.uRemarks=uRemarks;
        personUserEntity.Createtime=Createtime;
        personUserEntity.uDepartment=uDepartment;
        personUserEntity.accountType=accountType;
        personUserEntity.uEmployeenum=uEmployeenum;
        personUserEntity.uIshistory=uIshistory;
        personUserEntity.uIsUnilt=uIsUnilt;
        personUserEntity.uIsAccontion=uIsAccontion;
        personUserEntity.uUnitCode=uUnitCode;
        personUserEntity.roleid=roleid;
        personUserEntity.roleType=roleType;
        personUserEntity.groupid=groupid;
        personUserEntity.groupName=groupName;
        personUserEntity.deviceid=deviceid;
        personUserEntity.deviceESN=deviceESN;
        personUserEntity.devicetype=devicetype;
        personUserEntity.status=status;
        personUserEntity.sendUserName=sendUserName;
        personUserEntity.sendUserCode=sendUserCode;
        personUserEntity.msgContent=msgContent;
        personUserEntity.msgType=msgType;

        return personUserEntity;
    }
    public String getSendUserName() {
        return this.sendUserName;
    }
    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
    public String getSendUserCode() {
        return this.sendUserCode;
    }
    public void setSendUserCode(String sendUserCode) {
        this.sendUserCode = sendUserCode;
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
}
