package com.aimissu.ptt.entity.im;

import com.aimissu.ptt.entity.ui.ChatMsgList;

/**
 * 聊天消息
 */
public class ChatMsg {


    /**
     * SendUserId : 4c2b4e0e-34a0-e811-96fd-509a4c2006e1
     * SendUserName : 李sir
     * SendUserCode : 1003 
     * SendUserLatitude : 0
     * SendUserLongitude : 0
     * SendPositionName : 
     * SendUserDuty : null
     * SendUserHeadPortrait : 
     * SendUserDepartment : 
     * SendUserDepartmentCode : null
     * MsgId : 0f4d16ca-c4bb-4e84-91bc-2e97a32b1a4e
     * CaseId : 
     * CaseCode : 
     * CaseName : 
     * MsgTime : 2018-08-16 20:50:42
     * MsgType : Text
     * MsgContent : {"Text":"测试消息"}
     * MsgFile : null
     * CreatedTime : null
     * _MsgTime : null
     * MsgFromType : Group
     * MsgToCode : 1
     * PersonGroup : null
     * PersonGroupName : null
     * GroupName : 交流大会
     */

    private String SendUserId;
    private String SendUserName;
    private String SendUserCode;
    private String SendUserLatitude;
    private String SendUserLongitude;
    private String SendPositionName;
    private String SendUserDuty;
    private String SendUserHeadPortrait;
    private String SendUserDepartment;
    private String SendUserDepartmentCode;
    private String MsgId;
    private String CaseId;
    private String CaseCode;
    private String CaseName;
    private String MsgTime;
    private String MsgType;
    private String MsgContent;
    private String MsgFile;
    private String CreatedTime;
    private String _MsgTime;
    private String MsgFromType;
    private String MsgToCode;
    private String PersonGroup;
    private String PersonGroupName;
    private String GroupName;

    public String getSendUserId() {
        return SendUserId;
    }

    public void setSendUserId(String SendUserId) {
        this.SendUserId = SendUserId;
    }

    public String getSendUserName() {
        return SendUserName;
    }

    public void setSendUserName(String SendUserName) {
        this.SendUserName = SendUserName;
    }

    public String getSendUserCode() {
        return SendUserCode;
    }

    public void setSendUserCode(String SendUserCode) {
        this.SendUserCode = SendUserCode;
    }

    public String getSendUserLatitude() {
        return SendUserLatitude;
    }

    public void setSendUserLatitude(String SendUserLatitude) {
        this.SendUserLatitude = SendUserLatitude;
    }

    public String getSendUserLongitude() {
        return SendUserLongitude;
    }

    public void setSendUserLongitude(String SendUserLongitude) {
        this.SendUserLongitude = SendUserLongitude;
    }

    public String getSendPositionName() {
        return SendPositionName;
    }

    public void setSendPositionName(String SendPositionName) {
        this.SendPositionName = SendPositionName;
    }

    public String getSendUserDuty() {
        return SendUserDuty;
    }

    public void setSendUserDuty(String SendUserDuty) {
        this.SendUserDuty = SendUserDuty;
    }

    public String getSendUserHeadPortrait() {
        return SendUserHeadPortrait;
    }

    public void setSendUserHeadPortrait(String SendUserHeadPortrait) {
        this.SendUserHeadPortrait = SendUserHeadPortrait;
    }

    public String getSendUserDepartment() {
        return SendUserDepartment;
    }

    public void setSendUserDepartment(String SendUserDepartment) {
        this.SendUserDepartment = SendUserDepartment;
    }

    public String getSendUserDepartmentCode() {
        return SendUserDepartmentCode;
    }

    public void setSendUserDepartmentCode(String SendUserDepartmentCode) {
        this.SendUserDepartmentCode = SendUserDepartmentCode;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }

    public String getCaseId() {
        return CaseId;
    }

    public void setCaseId(String CaseId) {
        this.CaseId = CaseId;
    }

    public String getCaseCode() {
        return CaseCode;
    }

    public void setCaseCode(String CaseCode) {
        this.CaseCode = CaseCode;
    }

    public String getCaseName() {
        return CaseName;
    }

    public void setCaseName(String CaseName) {
        this.CaseName = CaseName;
    }

    public String getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(String MsgTime) {
        this.MsgTime = MsgTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }

    public String getMsgFile() {
        return MsgFile;
    }

    public void setMsgFile(String MsgFile) {
        this.MsgFile = MsgFile;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String CreatedTime) {
        this.CreatedTime = CreatedTime;
    }

    public String get_MsgTime() {
        return _MsgTime;
    }

    public void set_MsgTime(String _MsgTime) {
        this._MsgTime = _MsgTime;
    }

    public String getMsgFromType() {
        return MsgFromType;
    }

    public void setMsgFromType(String MsgFromType) {
        this.MsgFromType = MsgFromType;
    }

    public String getMsgToCode() {
        return MsgToCode;
    }

    public void setMsgToCode(String MsgToCode) {
        this.MsgToCode = MsgToCode;
    }

    public String getPersonGroup() {
        return PersonGroup;
    }

    public void setPersonGroup(String PersonGroup) {
        this.PersonGroup = PersonGroup;
    }

    public String getPersonGroupName() {
        return PersonGroupName;
    }

    public void setPersonGroupName(String PersonGroupName) {
        this.PersonGroupName = PersonGroupName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }


    public ChatMsgList toChatMsgList(){
        ChatMsgList chatMsgList=new ChatMsgList();
        chatMsgList.setCaseCode(this.getCaseCode());
        chatMsgList.setMsgTime(this.getMsgTime());
        chatMsgList.setCreatedTime(this.getMsgTime());
        chatMsgList.setSendUserCode(this.getSendUserCode());
        chatMsgList.setSendUserName(this.getSendUserName());
        chatMsgList.setMsgId(this.getMsgId());
        chatMsgList.setMsgContent(this.getMsgContent());
        chatMsgList.setMsgType(this.getMsgType());
        chatMsgList.setMsgFile(this.getMsgFile());
        chatMsgList.setSendUserHeadPortrait(this.getSendUserHeadPortrait());
        chatMsgList.setMsgFromType(this.getMsgFromType());
        chatMsgList.setMsgToCode(this.getMsgToCode());
        return chatMsgList;
    }

    @Override
    public String toString() {
        return "ChatMsg{" +
                "SendUserId='" + SendUserId + '\'' +
                ", SendUserName='" + SendUserName + '\'' +
                ", SendUserCode='" + SendUserCode + '\'' +
                ", SendUserLatitude='" + SendUserLatitude + '\'' +
                ", SendUserLongitude='" + SendUserLongitude + '\'' +
                ", SendPositionName='" + SendPositionName + '\'' +
                ", SendUserDuty='" + SendUserDuty + '\'' +
                ", SendUserHeadPortrait='" + SendUserHeadPortrait + '\'' +
                ", SendUserDepartment='" + SendUserDepartment + '\'' +
                ", SendUserDepartmentCode='" + SendUserDepartmentCode + '\'' +
                ", MsgId='" + MsgId + '\'' +
                ", CaseId='" + CaseId + '\'' +
                ", CaseCode='" + CaseCode + '\'' +
                ", CaseName='" + CaseName + '\'' +
                ", MsgTime='" + MsgTime + '\'' +
                ", MsgType='" + MsgType + '\'' +
                ", MsgContent='" + MsgContent + '\'' +
                ", MsgFile='" + MsgFile + '\'' +
                ", CreatedTime='" + CreatedTime + '\'' +
                ", _MsgTime='" + _MsgTime + '\'' +
                ", MsgFromType='" + MsgFromType + '\'' +
                ", MsgToCode='" + MsgToCode + '\'' +
                ", PersonGroup='" + PersonGroup + '\'' +
                ", PersonGroupName='" + PersonGroupName + '\'' +
                ", GroupName='" + GroupName + '\'' +
                '}';
    }
}
