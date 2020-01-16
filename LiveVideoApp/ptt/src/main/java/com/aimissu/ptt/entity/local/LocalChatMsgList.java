package com.aimissu.ptt.entity.local;

import com.aimissu.ptt.entity.ui.ChatMsgList;
import com.aimissu.ptt.entity.ui.UserEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.List;

/**
 */
@Entity(indexes = {@Index(value = "id DESC")})
public class LocalChatMsgList {
    @Id
    public Long id;
    public String MsgId;
    public String MsgFromType;
    public String MsgToCode;
    public String CaseCode;
    public String CaseName;
    public String MsgTime;
    public String MsgType;
    public String SendUserCode;
    public String SendUserName;
    public String SendUserHeadPortrait;
    public String MsgContent;
    public String MsgFile;
    public String MsgCount;
    public String CreatedTime;
    public String PersonGroup;
    public String OtherUserName;
    public String OtherUserCode;
    public String OtherUserHeadPortrait;
    public String MsgGroupName;
    public String MsgGroupCode;
    public String CreatedMsgGroupUserName;
    public String CreatedMsgGroupUserCode;
    public String _OrderTime;


    @Generated(hash = 1623792816)
    public LocalChatMsgList(Long id, String MsgId, String MsgFromType,
            String MsgToCode, String CaseCode, String CaseName, String MsgTime,
            String MsgType, String SendUserCode, String SendUserName,
            String SendUserHeadPortrait, String MsgContent, String MsgFile,
            String MsgCount, String CreatedTime, String PersonGroup,
            String OtherUserName, String OtherUserCode,
            String OtherUserHeadPortrait, String MsgGroupName, String MsgGroupCode,
            String CreatedMsgGroupUserName, String CreatedMsgGroupUserCode,
            String _OrderTime) {
        this.id = id;
        this.MsgId = MsgId;
        this.MsgFromType = MsgFromType;
        this.MsgToCode = MsgToCode;
        this.CaseCode = CaseCode;
        this.CaseName = CaseName;
        this.MsgTime = MsgTime;
        this.MsgType = MsgType;
        this.SendUserCode = SendUserCode;
        this.SendUserName = SendUserName;
        this.SendUserHeadPortrait = SendUserHeadPortrait;
        this.MsgContent = MsgContent;
        this.MsgFile = MsgFile;
        this.MsgCount = MsgCount;
        this.CreatedTime = CreatedTime;
        this.PersonGroup = PersonGroup;
        this.OtherUserName = OtherUserName;
        this.OtherUserCode = OtherUserCode;
        this.OtherUserHeadPortrait = OtherUserHeadPortrait;
        this.MsgGroupName = MsgGroupName;
        this.MsgGroupCode = MsgGroupCode;
        this.CreatedMsgGroupUserName = CreatedMsgGroupUserName;
        this.CreatedMsgGroupUserCode = CreatedMsgGroupUserCode;
        this._OrderTime = _OrderTime;
    }


    @Generated(hash = 336649452)
    public LocalChatMsgList() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getMsgId() {
        return this.MsgId;
    }


    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }


    public String getMsgFromType() {
        return this.MsgFromType;
    }


    public void setMsgFromType(String MsgFromType) {
        this.MsgFromType = MsgFromType;
    }


    public String getMsgToCode() {
        return this.MsgToCode;
    }


    public void setMsgToCode(String MsgToCode) {
        this.MsgToCode = MsgToCode;
    }


    public String getCaseCode() {
        return this.CaseCode;
    }


    public void setCaseCode(String CaseCode) {
        this.CaseCode = CaseCode;
    }


    public String getCaseName() {
        return this.CaseName;
    }


    public void setCaseName(String CaseName) {
        this.CaseName = CaseName;
    }


    public String getMsgTime() {
        return this.MsgTime;
    }


    public void setMsgTime(String MsgTime) {
        this.MsgTime = MsgTime;
    }


    public String getMsgType() {
        return this.MsgType;
    }


    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }


    public String getSendUserCode() {
        return this.SendUserCode;
    }


    public void setSendUserCode(String SendUserCode) {
        this.SendUserCode = SendUserCode;
    }


    public String getSendUserName() {
        return this.SendUserName;
    }


    public void setSendUserName(String SendUserName) {
        this.SendUserName = SendUserName;
    }


    public String getSendUserHeadPortrait() {
        return this.SendUserHeadPortrait;
    }


    public void setSendUserHeadPortrait(String SendUserHeadPortrait) {
        this.SendUserHeadPortrait = SendUserHeadPortrait;
    }


    public String getMsgContent() {
        return this.MsgContent;
    }


    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }


    public String getMsgFile() {
        return this.MsgFile;
    }


    public void setMsgFile(String MsgFile) {
        this.MsgFile = MsgFile;
    }


    public String getMsgCount() {
        return this.MsgCount;
    }


    public void setMsgCount(String MsgCount) {
        this.MsgCount = MsgCount;
    }


    public String getCreatedTime() {
        return this.CreatedTime;
    }


    public void setCreatedTime(String CreatedTime) {
        this.CreatedTime = CreatedTime;
    }


    public String getPersonGroup() {
        return this.PersonGroup;
    }


    public void setPersonGroup(String PersonGroup) {
        this.PersonGroup = PersonGroup;
    }


    public String getOtherUserName() {
        return this.OtherUserName;
    }


    public void setOtherUserName(String OtherUserName) {
        this.OtherUserName = OtherUserName;
    }


    public String getOtherUserCode() {
        return this.OtherUserCode;
    }


    public void setOtherUserCode(String OtherUserCode) {
        this.OtherUserCode = OtherUserCode;
    }


    public String getOtherUserHeadPortrait() {
        return this.OtherUserHeadPortrait;
    }


    public void setOtherUserHeadPortrait(String OtherUserHeadPortrait) {
        this.OtherUserHeadPortrait = OtherUserHeadPortrait;
    }


    public String getMsgGroupName() {
        return this.MsgGroupName;
    }


    public void setMsgGroupName(String MsgGroupName) {
        this.MsgGroupName = MsgGroupName;
    }


    public String getMsgGroupCode() {
        return this.MsgGroupCode;
    }


    public void setMsgGroupCode(String MsgGroupCode) {
        this.MsgGroupCode = MsgGroupCode;
    }


    public String getCreatedMsgGroupUserName() {
        return this.CreatedMsgGroupUserName;
    }


    public void setCreatedMsgGroupUserName(String CreatedMsgGroupUserName) {
        this.CreatedMsgGroupUserName = CreatedMsgGroupUserName;
    }


    public String getCreatedMsgGroupUserCode() {
        return this.CreatedMsgGroupUserCode;
    }


    public void setCreatedMsgGroupUserCode(String CreatedMsgGroupUserCode) {
        this.CreatedMsgGroupUserCode = CreatedMsgGroupUserCode;
    }


    public String get_OrderTime() {
        return this._OrderTime;
    }


    public void set_OrderTime(String _OrderTime) {
        this._OrderTime = _OrderTime;
    }

    public ChatMsgList toChatMsgList(){
        ChatMsgList chatMsgList=new ChatMsgList();
        chatMsgList.MsgId = MsgId;
        chatMsgList.MsgFromType = MsgFromType;
        chatMsgList.MsgToCode = MsgToCode;
        chatMsgList.CaseCode = CaseCode;
        chatMsgList.CaseName = CaseName;
        chatMsgList.MsgTime = MsgTime;
        chatMsgList.MsgType = MsgType;
        chatMsgList.SendUserCode = SendUserCode;
        chatMsgList.SendUserName = SendUserName;
        chatMsgList.SendUserHeadPortrait = SendUserHeadPortrait;
        chatMsgList.MsgContent = MsgContent;
        chatMsgList.MsgFile = MsgFile;
        chatMsgList.MsgCount = MsgCount;
        chatMsgList.CreatedTime = CreatedTime;
        chatMsgList.PersonGroup = PersonGroup;
        chatMsgList.OtherUserName = OtherUserName;
        chatMsgList.OtherUserCode = OtherUserCode;
        chatMsgList.OtherUserHeadPortrait = OtherUserHeadPortrait;
        chatMsgList.MsgGroupName = MsgGroupName;
        chatMsgList.MsgGroupCode = MsgGroupCode;
        chatMsgList.CreatedMsgGroupUserName = CreatedMsgGroupUserName;
        chatMsgList.CreatedMsgGroupUserCode = CreatedMsgGroupUserCode;
        chatMsgList._OrderTime = _OrderTime;
        return chatMsgList;
    }

}
