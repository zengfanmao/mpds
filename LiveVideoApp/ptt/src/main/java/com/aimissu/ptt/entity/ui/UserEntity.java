package com.aimissu.ptt.entity.ui;

import com.aimissu.ptt.entity.local.LocalUserEntity;

/**

 */
public class UserEntity {

    /**
     * LoginName : 1001
     * UserPwd : null
     * ApiToken : mwbmi7xLCF13hpMM
     * UserName : 陆警官
     * UserPhone : null
     * Department :
     * HeadPortrait :
     * UserCode : 1001
     * UserId : 4a2b4e0e-34a0-e811-96fd-509a4c2006e1
     * UserSex : 0
     * UserDuty : null
     * DepartmentCode : null
     * UserShortNum : null
     * UserPCNum : null
     * RoleName : null
     * _CreatedTime : null
     * LYCID : null
     */

    public Long id;
    public String LoginName;
    public String UserPwd;
    public String ApiToken;
    public String UserName;
    public String UserPhone;
    public String Department;
    public String HeadPortrait;
    public String UserCode;
    public String UserId;
    public String UserSex;
    public String UserDuty;
    public String DepartmentCode;
    public String UserShortNum;
    public String UserPCNum;
    public String RoleName;
    public String roleId;
    public String _CreatedTime;
    public String LYCID;
    public String videoUrl;
    public String videoPort;
    public String discussionCode;
    public String discussionName;
    public String dCode;
    public String dName;
    public String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }

    public String getUserPwd() {
        return UserPwd;
    }

    public void setUserPwd(String UserPwd) {
        this.UserPwd = UserPwd;
    }

    public String getApiToken() {
        return ApiToken;
    }

    public void setApiToken(String ApiToken) {
        this.ApiToken = ApiToken;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String UserPhone) {
        this.UserPhone = UserPhone;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String UserSex) {
        this.UserSex = UserSex;
    }

    public String getUserDuty() {
        return UserDuty;
    }

    public void setUserDuty(String UserDuty) {
        this.UserDuty = UserDuty;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
    }

    public String getUserShortNum() {
        return UserShortNum;
    }

    public void setUserShortNum(String UserShortNum) {
        this.UserShortNum = UserShortNum;
    }

    public String getUserPCNum() {
        return UserPCNum;
    }

    public void setUserPCNum(String UserPCNum) {
        this.UserPCNum = UserPCNum;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }

    public String get_CreatedTime() {
        return _CreatedTime;
    }

    public void set_CreatedTime(String _CreatedTime) {
        this._CreatedTime = _CreatedTime;
    }

    public String getLYCID() {
        return LYCID;
    }

    public void setLYCID(String LYCID) {
        this.LYCID = LYCID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPort() {
        return videoPort;
    }

    public void setVideoPort(String videoPort) {
        this.videoPort = videoPort;
    }

    public String getDiscussionCode() {
        return discussionCode;
    }

    public void setDiscussionCode(String discussionCode) {
        this.discussionCode = discussionCode;
    }

    public String getDiscussionName() {
        return discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public LocalUserEntity toEntity() {
        LocalUserEntity localUserEntity = new LocalUserEntity();
        localUserEntity.id = id;
        localUserEntity.LoginName = LoginName;
        localUserEntity.UserPwd = UserPwd;
        localUserEntity.ApiToken = ApiToken;
        localUserEntity.UserName = UserName;
        localUserEntity.UserPhone = UserPhone;
        localUserEntity.Department = Department;
        localUserEntity.HeadPortrait = HeadPortrait;
        localUserEntity.UserCode = UserCode;
        localUserEntity.UserId = UserId;
        localUserEntity.UserSex = UserSex;
        localUserEntity.UserDuty = UserDuty;
        localUserEntity.DepartmentCode = DepartmentCode;
        localUserEntity.UserShortNum = UserShortNum;
        localUserEntity.UserPCNum = UserPCNum;
        localUserEntity.RoleName = RoleName;
        localUserEntity._CreatedTime = _CreatedTime;
        localUserEntity.LYCID = LYCID;
        localUserEntity.roleId=roleId;
        localUserEntity.videoUrl=videoUrl;
        localUserEntity.videoPort=videoPort;
        localUserEntity.discussionCode=discussionCode;
        localUserEntity.discussionName=discussionName;
        localUserEntity.dCode=dCode;
        localUserEntity.dName=dName;
        localUserEntity.deviceId=deviceId;
        return localUserEntity;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", LoginName='" + LoginName + '\'' +
                ", UserPwd='" + UserPwd + '\'' +
                ", ApiToken='" + ApiToken + '\'' +
                ", UserName='" + UserName + '\'' +
                ", UserPhone='" + UserPhone + '\'' +
                ", Department='" + Department + '\'' +
                ", HeadPortrait='" + HeadPortrait + '\'' +
                ", UserCode='" + UserCode + '\'' +
                ", UserId='" + UserId + '\'' +
                ", UserSex='" + UserSex + '\'' +
                ", UserDuty='" + UserDuty + '\'' +
                ", DepartmentCode='" + DepartmentCode + '\'' +
                ", UserShortNum='" + UserShortNum + '\'' +
                ", UserPCNum='" + UserPCNum + '\'' +
                ", RoleName='" + RoleName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", _CreatedTime='" + _CreatedTime + '\'' +
                ", LYCID='" + LYCID + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoPort='" + videoPort + '\'' +
                ", discussionCode='" + discussionCode + '\'' +
                ", discussionName='" + discussionName + '\'' +
                ", dCode='" + dCode + '\'' +
                ", dName='" + dName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
