package com.aimissu.ptt.model;

import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.im.IMPamras;

/**

 */
public interface IChatModel {

    /// <summary>
    /// 获取个人聊天消息具体内容  可以获取某个讨论组消息列表、个人消息列表、
    /// 如查询与某个人的私人消息  MsgFromType='Person'  MsgToCode='接收人编号，接收消息人' msgFromUserCode ='谁跟你聊天，就带谁的编号,别人，发消息的人'
    /// 如查询‘小伙伴讨论组’消息  MsgFromType='Group'  MsgToCode='讨论组编号'
    /// </summary>
    /// <param name="CaseCode">案件编号</param>
    /// <param name="Skip">跳过多少条开始取</param>
    /// <param name="Take">取多少条</param>
    /// <param name="FirstMsgId">限制第一条消息id,获取的消息将都是这条消息之前的</param>
    /// <param name="MsgFromType">消息来源类型、person group case</param>
    /// <param name="MsgToCode">编号：用户编号、讨论组编号、案件编号</param>
    /// <param name="MsgFromUserCode">发消息的人</param>
    /// <returns></returns>
    void getMyChatDetails(String skip, String take, String firstMsgId, String msgFromType, String msgToCode, String msgFromUserCode, RxCallBack<DataChatMsg> rxCallBack);

    void pushMsg(IMPamras imPamras, RxCallBack<DataPushMsg> rxCallBack);

    void uploadFile(IMPamras imPamras,RxCallBack<DataPostFile> rxCallBack);

    void updateFilePropertiy(IMPamras imPamras, RxCallBack<DataPostFile> rxCallBack);

}
