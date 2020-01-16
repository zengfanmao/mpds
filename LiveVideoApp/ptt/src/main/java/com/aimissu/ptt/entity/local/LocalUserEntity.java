package com.aimissu.ptt.entity.local;

import com.aimissu.ptt.entity.ui.UserEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 */
@Entity(indexes = {@Index(value = "id DESC")})
public class LocalUserEntity {
    @Id
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


    @Generated(hash = 357911501)
    public LocalUserEntity(Long id, String LoginName, String UserPwd, String ApiToken, String UserName,
            String UserPhone, String Department, String HeadPortrait, String UserCode, String UserId,
            String UserSex, String UserDuty, String DepartmentCode, String UserShortNum,
            String UserPCNum, String RoleName, String roleId, String _CreatedTime, String LYCID,
            String videoUrl, String videoPort, String discussionCode, String discussionName,
            String dCode, String dName, String deviceId) {
        this.id = id;
        this.LoginName = LoginName;
        this.UserPwd = UserPwd;
        this.ApiToken = ApiToken;
        this.UserName = UserName;
        this.UserPhone = UserPhone;
        this.Department = Department;
        this.HeadPortrait = HeadPortrait;
        this.UserCode = UserCode;
        this.UserId = UserId;
        this.UserSex = UserSex;
        this.UserDuty = UserDuty;
        this.DepartmentCode = DepartmentCode;
        this.UserShortNum = UserShortNum;
        this.UserPCNum = UserPCNum;
        this.RoleName = RoleName;
        this.roleId = roleId;
        this._CreatedTime = _CreatedTime;
        this.LYCID = LYCID;
        this.videoUrl = videoUrl;
        this.videoPort = videoPort;
        this.discussionCode = discussionCode;
        this.discussionName = discussionName;
        this.dCode = dCode;
        this.dName = dName;
        this.deviceId = deviceId;
    }

    @Generated(hash = 666352881)
    public LocalUserEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.LoginName;
    }

    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }

    public String getUserPwd() {
        return this.UserPwd;
    }

    public void setUserPwd(String UserPwd) {
        this.UserPwd = UserPwd;
    }

    public String getApiToken() {
        return this.ApiToken;
    }

    public void setApiToken(String ApiToken) {
        this.ApiToken = ApiToken;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhone() {
        return this.UserPhone;
    }

    public void setUserPhone(String UserPhone) {
        this.UserPhone = UserPhone;
    }

    public String getDepartment() {
        return this.Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getHeadPortrait() {
        return this.HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getUserCode() {
        return this.UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserSex() {
        return this.UserSex;
    }

    public void setUserSex(String UserSex) {
        this.UserSex = UserSex;
    }

    public String getUserDuty() {
        return this.UserDuty;
    }

    public void setUserDuty(String UserDuty) {
        this.UserDuty = UserDuty;
    }

    public String getDepartmentCode() {
        return this.DepartmentCode;
    }

    public void setDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
    }

    public String getUserShortNum() {
        return this.UserShortNum;
    }

    public void setUserShortNum(String UserShortNum) {
        this.UserShortNum = UserShortNum;
    }

    public String getUserPCNum() {
        return this.UserPCNum;
    }

    public void setUserPCNum(String UserPCNum) {
        this.UserPCNum = UserPCNum;
    }

    public String getRoleName() {
        return this.RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }

    public String get_CreatedTime() {
        return this._CreatedTime;
    }

    public void set_CreatedTime(String _CreatedTime) {
        this._CreatedTime = _CreatedTime;
    }

    public String getLYCID() {
        return this.LYCID;
    }

    public void setLYCID(String LYCID) {
        this.LYCID = LYCID;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPort() {
        return this.videoPort;
    }

    public void setVideoPort(String videoPort) {
        this.videoPort = videoPort;
    }

    public String getDiscussionCode() {
        return this.discussionCode;
    }

    public void setDiscussionCode(String discussionCode) {
        this.discussionCode = discussionCode;
    }

    public String getDiscussionName() {
        return this.discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public String getDCode() {
        return this.dCode;
    }

    public void setDCode(String dCode) {
        this.dCode = dCode;
    }

    public String getDName() {
        return this.dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    public UserEntity toModel() {
        UserEntity userEntity = new UserEntity();
        userEntity.id = id;
        userEntity.LoginName = LoginName;
        userEntity.UserPwd = UserPwd;
        userEntity.ApiToken = ApiToken;
        userEntity.UserName = UserName;
        userEntity.UserPhone = UserPhone;
        userEntity.Department = Department;
        userEntity.HeadPortrait = HeadPortrait;
        userEntity.UserCode = UserCode;
        userEntity.UserId = UserId;
        userEntity.UserSex = UserSex;
        userEntity.UserDuty = UserDuty;
        userEntity.DepartmentCode = DepartmentCode;
        userEntity.UserShortNum = UserShortNum;
        userEntity.UserPCNum = UserPCNum;
        userEntity.RoleName = RoleName;
        userEntity._CreatedTime = _CreatedTime;
        userEntity.LYCID = LYCID;
        userEntity.roleId=roleId;
        userEntity.videoUrl=videoUrl;
        userEntity.videoPort=videoPort;
        userEntity.discussionCode=discussionCode;
        userEntity.discussionName=discussionName;
        userEntity.dCode=dCode;
        userEntity.dName=dName;
        userEntity.deviceId=deviceId;
        return userEntity;
    }

    @Override
    public String toString() {
        return "LocalUserEntity{" +
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

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
