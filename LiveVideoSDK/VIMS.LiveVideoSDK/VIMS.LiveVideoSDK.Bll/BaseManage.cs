using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using VIMS.LiveVideoSDK.Bll.Entity;
using VIMS.LiveVideoSDK.Model;
using VIMS.LiveVideoSDK.Core;
using VIMS.LiveVideoSDK.Bll.Common;
using System.Net.Http;
using System.Configuration;
using System.Linq.Expressions;
using System.Data.Entity.SqlServer;
using System.Data.Entity.Validation;
using System.Collections;

namespace VIMS.LiveVideoSDK.Bll
{
    public partial class BaseManage : IBaseManage
    {



        #region 登陆
        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="userName">登录账号</param>
        /// <param name="userPwd">登陆密码</param>
        /// <returns></returns>
        public ResultEx Login(string LoginName, string UserPwd)
        {
            var result = new ResultEx() { Message = "登录失败" };
            if (string.IsNullOrEmpty(LoginName))
            {
                result.Message = "账号不能为空";
                return result;
            }
            /**/
            if (string.IsNullOrEmpty(UserPwd))
            {
                result.Message = "密码不能为空";
                return result;
            }
            
            var db = new DBLiveVideoEntities();
            var model = db.tbUsers.Where(m => m.uCode.Equals(LoginName) && m.devicetype.Equals("APP") && "0".Equals(m.isDel.ToString()) && !m.status.Equals("禁用")).FirstOrDefault();
            if (model == null || string.IsNullOrEmpty(LoginName))
            {
                result.Message = "账号不存在";
                return result;
            }
            if (model.status.Equals("禁用"))
            {
                result.Message = "账号被禁用";
                return result;
            }
            string encryptPwd = DESEncrypt.Encrypt(UserPwd.TrimEnd(), model.uSalt);
            if (model.uPassword.TrimEnd().Equals(encryptPwd))
            //if (true)
            {
                var modelDepartment = db.tbDepartments.Where(m => m.dCode.Equals(model.dCode)).FirstOrDefault();
                var departName = string.Empty;
                if (modelDepartment != null)
                    departName = modelDepartment.dName;
                var tokenModel = SetToken(model.uCode);

                if (tokenModel != null)
                {
                    if (tokenModel.IsSuccess)
                    {
                        var token = (tbApiToken)tokenModel.Entity;
                        var userModle = new UserModel()
                        {
                            UserId = model.ID.ToString(),
                            UserCode = model.uCode,
                            UserName = model.uName,
                            LoginName = model.uCode,
                            UserPhone = model.uTel,
                            UserPCNum = model.pcNum,
                            UserSex = Bll.Common.Utils.toString(model.uSex),
                            UserShortNum = model.uShortNum,
                            RoleName = Convert.ToString(model.roleid),
                            //HeadPortrait = Bll.Common.Utils.toSiteUrl(model.uHeadPortrait),
                            //UserPwd=model.UserPwd,
                            HeadPortrait = model.uHeadPortrait,
                            ApiToken = token.apiToken,
                            UserDuty = model.uDuty,
                            Department = model.uDepartment,
                            dCode = model.dCode,
                            dName = model.dName,
                            discussionCode = model.groupid,
                            discussionName = model.groupName,
                            deviceId = model.deviceid.ToString(),
                            LYCID = model.LYCID,
                            videoUrl = ConfigurationManager.AppSettings["LiveVideoSDK_Call_Ip"].ToString(),
                            videoPort = ConfigurationManager.AppSettings["LiveVideoSDK_Call_Port"].ToString()
                        };
                        result.Message = "登录成功";
                        result.ResultCode = "1";
                        result.IsSuccess = true;
                        result.Entity = userModle;
                        logEvent(model.uCode, model.uName, "Login", "登录成功", "APP");

                        //发送通知更新app和web消息
                        var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");
                        var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = "", MsgType = MsgTypes.OUserLogin.ToString() };
                        msgModel.MsgContent = userModle;
                        /*
                        var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
                        string clientId = Guid.NewGuid().ToString();
                        try { client.Connect(clientId); }
                        catch (Exception ex)
                        {
                            result.Message = "即时通讯服务未开起";
                            result.IsSuccess = false;
                            result.ResultCode = "-1";
                            return result;
                        }
                        */
                        MqttClient client;
                        try
                        {
                            client = this.mqttConnect();
                        }
                        catch (Exception ex)
                        {
                            result.Message = ex.Message.ToString();
                            result.IsSuccess = false;
                            result.ResultCode = "-1";
                            return result;
                        }
                        client.Publish(userListenerTag + LoginName.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
                        this.release(client);
                        model.LYCID = GetTimestamp(DateTime.Now).ToString();
                        model.lastLoginTime = DateTime.Now;
                        model.status = "在线";
                        db.SaveChanges();
                    }
                    else
                    {
                        result = tokenModel;
                        //result.Message = "登录失败";
                    }



                }
                else
                {
                    result.Message = "获取秘钥失败";
                    return result;
                }



                return result;
            }
            else
            {
                logEvent(model.uCode, model.uName, "Login", "账号/密码错误", "APP");
                result.Message = "账号/密码错误";
                return result;

            }

        }

        /// <summary>
        /// 发送案件群消息
        /// </summary>
        /// <param name="FromUserId"></param>
        /// <param name="ToUserId"></param>
        /// <param name="MsgType"></param>
        /// <param name="MsgConent"></param>
        /// <param name="MsgFromType"></param>
        /// <param name="MsgToCode"></param>
        /// <returns></returns>
        public ResultEx PushMsg(string UserCode, string CaseCode, string MsgType, string MsgConent, string msgFile, string UserLatitude, string UserLongitude, string UserPositionName, string MsgFromType, string MsgToCode)
        {

            var result = new ResultEx() { Message = "发送失败" };
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = MsgConent, MsgType = MsgType };
            var db = new DBLiveVideoEntities();
            var _groupName = string.Empty;
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try { client.Connect(clientId); }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }

            var users = new List<tbUser>();
            if (MsgFromType.Trim().Equals(MsgFromTypes.Group.ToString()))
            {
                UserModel p = new UserModel();
                p.discussionCode = MsgToCode;
                users = MyBatisHelper.QueryForList<tbUser>("select_app_user_by_msgtocode", p).ToList();
                //users = db.tbUsers.Where(m => m.groupid.Equals(MsgToCode) && m.devicetype.Equals("APP")).ToList();
                /*
                users = (from c in db.tbDiscussionGroupMenbers
                         join d in db.tbUsers on c.uCode equals d.uCode into d_join
                         from d in d_join.DefaultIfEmpty()
                         where c.discussionCode.Trim().Equals(MsgToCode.Trim())
                         select d
                           ).ToList();
                */
                //发送给讨论组的所有成员
                if (users.Count > 0)
                {
                    _groupName = this.getGroupById(p.discussionCode);
                }
                /*
                var groupModel = db.tbDiscussionGroups.Where(m => m.discussionCode.Trim().Equals(MsgToCode.Trim())).FirstOrDefault();
                if (groupModel != null)
                    _groupName = groupModel.discussionName;
                */
            }
            else if (MsgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
            {
                //发送给当前对话的人
                UserModel p = new UserModel();
                p.UserCode = MsgToCode;
                var userModel = MyBatisHelper.QueryForObject<tbUser>("select_app_user_by_ucode", p);
                users.Add(userModel);
                /*
                var userModel = db.tbUsers.Where(m => m.uCode.Trim().Equals(MsgToCode.Trim())).FirstOrDefault();
                if (userModel != null)
                    users.Add(userModel);
                */
            }
            else if (MsgFromType.Trim().Equals(MsgFromTypes.Case.ToString()))
            {
                //发送给
                users.Clear();
                users = getCaseDeployUsers(CaseCode);
            }
            UserModel param = new UserModel();
            param.UserCode = UserCode;
            var sendUser = MyBatisHelper.QueryForObject<tbUser>("select_app_user_by_ucode", param);
            //var sendUser = db.tbUsers.Where(m => m.uCode.Equals(UserCode)).SingleOrDefault();
            //var sendUserDepartment = db.tbDepartments.Where(m => m.dCode.TrimEnd().Equals(sendUser.dCode.TrimEnd())).SingleOrDefault();
            var addMsgModel = new tbCaseMessage()
            {
                msgFile = String.IsNullOrEmpty(msgFile) ? null : msgFile,
                msgType = MsgType,
                msgAbstract = MsgConent,
                csCode = CaseCode,
                uCode = UserCode,
                uLatitude = UserLatitude,
                uLongitude = UserLongitude,
                uPositionName = UserPositionName,
                msgFromType = MsgFromType,
                msgToCode = MsgToCode,
            };

            var msgResult = AddCaseMessage(addMsgModel);
            if (!msgResult.IsSuccess || msgResult.Entity == null)
            {
                this.release(client);
                result.Message = "发送失败数据新增失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            
            var caseMsgModel = (tbCaseMessage)msgResult.Entity;

            //var caseModel = string.IsNullOrEmpty(CaseCode) ? null : db.tbCases.Where(m => m.csCode.Equals(CaseCode)).SingleOrDefault();

            tbFileDetail tbFile =  null;
            tbFileDetail picFile = null;
            if (caseMsgModel.msgFile != null)
            {
                tbFile = db.tbFileDetails.Where(m => m.fCode.Equals(caseMsgModel.msgFile)).FirstOrDefault();
                if (tbFile != null && !string.IsNullOrEmpty(tbFile.fFirstFrame))
                    picFile = db.tbFileDetails.Where(m => m.fCode.Equals(tbFile.fFirstFrame)).FirstOrDefault();
            }

            ChatModel chatmodel = new ChatModel();
            try
            {
                chatmodel = new ChatModel
                {
                    SendUserLatitude = caseMsgModel.uLatitude,
                    SendPositionName = caseMsgModel.uPositionName,
                    SendUserLongitude = caseMsgModel.uLongitude,
                    SendUserId = sendUser.ID.ToString(),
                    SendUserCode = sendUser.uCode,
                    SendUserName = sendUser.uName,
                    SendUserDepartment = sendUser.uDepartment,
                    SendUserHeadPortrait = sendUser.uHeadPortrait,
                    SendUserDuty = sendUser.uDuty,

                    MsgTime = ((DateTime)caseMsgModel.msgTime).ToString("yyyy-MM-dd HH:mm:ss"),
                    CaseCode = caseMsgModel.csCode,
                    MsgId = caseMsgModel.ID.ToString(),
                    //CaseName = caseModel.csName,
                    //CaseId = caseModel.ID.ToString(),
                    MsgType = caseMsgModel.msgType,
                    MsgContent = GetMsgJson(caseMsgModel.msgType, caseMsgModel.msgAbstract, tbFile, picFile),
                    MsgFromType = caseMsgModel.msgFromType,
                    MsgToCode = caseMsgModel.msgToCode,
                    GroupName = _groupName,
                };
            }
            catch (Exception ex)
            {
                this.release(client);
                result.Entity = JsonConvert.SerializeObject(chatmodel);
                result.Message = "casemodel:" + JsonConvert.SerializeObject(caseMsgModel) + ";send:" + JsonConvert.SerializeObject(sendUser);
                result.IsSuccess = true;
                result.ResultCode = ex.ToString() + ",strunk:" + ex.StackTrace;
                return result;
            }
            if (!string.IsNullOrEmpty(caseMsgModel.msgFromType) && caseMsgModel.msgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
            {
                //var userTalks = db.tbUsers.Where(m => m.uCode.Equals(UserCode) || m.uCode.Equals(MsgToCode)).ToList();
                var personGroup = string.Empty;
                var personGroupName = string.Empty;
                foreach (var talk in users)
                {
                    if (!string.IsNullOrEmpty(talk.uCode))
                        personGroup += talk.uCode.Trim() + ",";

                    if (!string.IsNullOrEmpty(talk.uName))
                        personGroupName += talk.uName.Trim() + ",";
                }
                if (sendUser != null)
                {
                    personGroup += sendUser.uCode.Trim() + ",";
                    personGroupName += sendUser.uName.Trim() + ",";
                }

                chatmodel.PersonGroup = personGroup;
                chatmodel.PersonGroupName = personGroupName;
            }

            msgModel.MsgContent = JsonConvert.SerializeObject(chatmodel);
            var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");
            var groupListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Group");

            var listUsers = users.Select(m => m.uCode.Trim()).ToList();
            //var userAdmins = db.tbCaseDeploys.Where(m => m.csCode.Trim().Equals(CaseCode) && m.rName.Trim().Equals("指挥官")).ToList();
            //if (userAdmins != null && userAdmins.Count() > 0)
            //{
            //    foreach (var userTemp in userAdmins)
            //    {
            //        if (!string.IsNullOrEmpty(userTemp.uCode) && !listUsers.Contains(userTemp.uCode.Trim()))
            //        {
            //            listUsers.Add(userTemp.uCode.Trim());
            //        }
            //    }
            //}
            
            foreach (var user in listUsers)
            {

                if (user == null || string.IsNullOrEmpty(user) || user.TrimEnd().Equals(UserCode.TrimEnd()))
                    continue;
                client.Publish(userListenerTag + user.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
            }
            this.release(client);
            //client.Disconnect();
            result.Entity = chatmodel;
            result.Message = "发送成功";
            result.IsSuccess = true;
            result.ResultCode = "1";
            return result;
        }


        public ResultEx PushCommand(string UserCode, string MsgType, string MsgConent, string MsgFromType, string MsgToCode)
        {
            var result = new ResultEx() { Message = "发送失败" };
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = MsgConent, MsgType = MsgType };
            var db = new DBLiveVideoEntities();
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try
            {
                client.Connect(clientId);
            }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client = null;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                if (client != null)
                {
                    this.release(client);
                }
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }

            if (!String.IsNullOrEmpty(MsgConent) && !MsgConent.Contains("conference_uuid"))
            {
                var model = new tbCaseMessage()
                {
                    ID = DateTime.Now.Ticks,
                    csCode = Guid.NewGuid().ToString(),
                    uCode = UserCode,
                    msgAbstract = MsgConent,
                    msgTime = DateTime.Now,
                    msgFromType = MsgFromType,
                    msgToCode = MsgToCode,
                    msgType = MsgType
                };
                db.tbCaseMessages.Add(model);
            }
            
            //Save conference msg
            try
            {
                if (!String.IsNullOrEmpty(MsgConent) && MsgConent.Contains("conference_uuid"))
                {
                    ConferenceModel conference = JsonHelper.JSONToObject<ConferenceModel>(MsgConent);
                    string caller = String.IsNullOrEmpty(conference.CallerSSI) ? conference.conference_Creator : conference.CallerSSI;
                    string listener = String.IsNullOrEmpty(conference.CalledSSI) ? conference.conference_name : conference.CalledSSI;
                    string eventName = conference.EventName;
                    if (!String.IsNullOrEmpty(conference.ptt_type))
                    {
                        string ptttype = "_" + conference.ptt_type;
                        string pdtOrApp = String.Equals("0", conference.IsPdt) ? "_APP" : "_PDT";
                        eventName += pdtOrApp + ptttype;
                    }

                    var audioModel = new mpds_audio_detail()
                    {
                        id = DateTime.Now.Ticks,
                        conference_uuid = conference.conference_uuid,
                        conference_name = conference.conference_name,
                        event_name = eventName,
                        event_time = DateTime.Now,
                        startor = caller,
                        listener = listener,
                        content = JsonHelper.ObjectToJSON(conference)
                    };
                    db.mpds_audio_detail.Add(audioModel);
                }
                db.SaveChanges();
            } catch (Exception ex)
            {
                LogHelper.WriteError(typeof(BaseManage), "Save Audio Detail Error:", ex);
            }
            var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");
            if (MsgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
            {
                client.Publish(userListenerTag + MsgToCode, Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
            }
            if (MsgFromType.Trim().Equals(MsgFromTypes.Group.ToString()))
            {
                var groupListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Group");
                var users = db.tbUsers.Where(m => m.groupid.Equals(MsgToCode) && m.devicetype.Equals("APP")).ToList();
                //UserModel param = new UserModel();
                //param.discussionCode = MsgToCode;
                //IList<tbUser> users = MyBatisHelper.QueryForList<tbUser>("select_app_user_by_msgtocode", param);
                foreach (var user in users)
                {
                    //LogHelper.WriteInfo(typeof(BaseManage), "Sending Command : " + JsonConvert.SerializeObject(msgModel));
                    ushort msgId = client.Publish(userListenerTag + user.uCode, Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
                    //LogHelper.WriteInfo(typeof(BaseManage), "Sending Command ID : " + msgId);
                }
            }
            this.release(client);
            //client.Disconnect();
            result.Message = "发送成功";
            result.IsSuccess = true;
            result.ResultCode = "1";
            return result;
        }

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
        public List<ChatModel> GetMyChatDetails(string csCode, string skip, string take, string firstMsgId, string msgFromType, string msgToCode, string msgFromUserCode)
        {
            bool isEmptyCaseCode = string.IsNullOrWhiteSpace(csCode);
            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            var _maxTime = DateTime.Now;

            if (string.IsNullOrEmpty(msgFromType))
                return null;
            var containTyps = new List<string>();
            containTyps.Add(MsgFromTypes.Case.ToString());
            containTyps.Add(MsgFromTypes.Person.ToString());
            containTyps.Add(MsgFromTypes.Group.ToString());

            if (!containTyps.Contains(msgFromType.Trim()))
                return null;

            if (!string.IsNullOrEmpty(firstMsgId))
            {
                var firstModel = db.tbCaseMessages.Where(m => m.ID.ToString().Equals(firstMsgId)).SingleOrDefault();
                if (firstModel != null && firstModel.msgTime != null)
                    _maxTime = (DateTime)firstModel.msgTime;
            }

            var _msgFromTypePerson = MsgFromTypes.Person.ToString();
            var _msgFromTypeGroup = MsgFromTypes.Group.ToString();
            var _msgFromTypeCase = MsgFromTypes.Case.ToString();
            //var _msgFromType = (MsgFromTypes)Enum.Parse(typeof(MsgFromTypes), msgFromType);
            //过滤消息类型
            var msgTypeFilters = new List<String>();
            msgTypeFilters.Add(MsgTypes.Image.ToString());
            msgTypeFilters.Add(MsgTypes.Text.ToString());
            msgTypeFilters.Add(MsgTypes.File.ToString());
            msgTypeFilters.Add(MsgTypes.Video.ToString());
            msgTypeFilters.Add(MsgTypes.Audio.ToString());
            msgTypeFilters.Add(MsgTypes.Map.ToString());

            _take = _take <= 0 ? 10 : _take;
            var models = (
                 from c in db.tbCaseMessages
                     //join a in db.tbCases on c.csCode equals a.csCode
                 join d in db.tbUsers on c.uCode equals d.uCode
                 join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                 from g in e_join.DefaultIfEmpty()
                 where
                 //(isEmptyCaseCode||a.csCode.Equals(csCode)) &&
                 msgTypeFilters.Contains(c.msgType)&&
                 c.msgTime < _maxTime && c.msgFromType.Equals(msgFromType)
                 &&
                 (
                 (c.msgFromType.Equals(_msgFromTypePerson) &&
                 //包括别人发，自己收和自己发别人收 列如A和B B和A 
                 (c.uCode.Equals(msgFromUserCode) && c.msgToCode.Equals(msgToCode)) || (c.uCode.Equals(msgToCode) && c.msgToCode.Equals(msgFromUserCode)))
                 ||
                  //讨论组的
                  (c.msgFromType.Equals(_msgFromTypeGroup) && c.msgToCode.Equals(msgToCode))

                ||
                  //案件的
                  (c.msgFromType.Equals(_msgFromTypeCase) && c.msgToCode.Equals(msgToCode))
                  )


                 select new ChatModel()
                 {
                     //CaseName = a==null?"": a.csName,
                     MsgTime = c == null ? "" : c.msgTime.ToString(),
                     _MsgTime = c == null ? null : c.msgTime,
                     CreatedTime = c == null ? null : c.msgTime,
                     SendUserId = d == null ? "" : d.ID.ToString(),
                     SendUserCode = d == null ? "" : d.uCode,
                     SendUserDuty = d == null ? "" : d.uDuty,
                     SendUserName = d == null ? "" : d.uName,
                     MsgContent = c == null ? "" : c.msgAbstract,
                     MsgType = c == null ? "" : c.msgType,
                     MsgId = c == null ? "" : c.ID.ToString(),
                     MsgFile = c == null ? "" : c.msgFile,//
                     //CaseId = a == null ? "" : a.ID.ToString(),
                     //CaseCode = a == null ? "" : a.csCode,
                     SendUserDepartment = g == null ? "" : g.dName,
                     //SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(d.uHeadPortrait),
                     SendUserHeadPortrait = d == null ? "" : d.uHeadPortrait,
                     SendUserDepartmentCode = g == null ? "" : g.dCode,

                     SendUserLatitude = c == null ? "" : c.uLatitude.ToString(),
                     SendPositionName = c == null ? "" : c.uPositionName,
                     SendUserLongitude = c == null ? "" : c.uLongitude.ToString(),
                     MsgFromType = c == null ? "" : c.msgFromType,
                     MsgToCode = c == null ? "" : c.msgToCode
                 }
                )
                .OrderByDescending(m => m.CreatedTime).Skip(_skip).Take(_take).ToList();
            var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
            var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
            var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
            var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();

            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();
                var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                tbFileDetail picFile = null;
                if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                    picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                if (!string.IsNullOrEmpty(model.MsgTime))
                    model.MsgTime = ((DateTime)model._MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

            }

            //if (!string.IsNullOrEmpty(maxTime) && _maxTime != null)
            //    models = models.Where(m => Common.Utils.toDatetime(m.MsgTime) <= _maxTime).ToList();

            //models.Reverse();
            return models;
        }
        #endregion

        #region 发送消息

        /// <summary>
        /// 发送案件群消息
        /// </summary>
        /// <param name="FromUserId"></param>
        /// <param name="ToUserId"></param>
        /// <param name="MsgType"></param>
        /// <param name="MsgConent"></param>
        /// <param name="MsgConent"></param>
        /// <param name="MsgToCode"></param>
        /// <returns></returns>
        public ResultEx PushMsgCase(string UserCode, string CaseCode, string MsgType, string MsgConent, string msgFile, string UserLatitude, string UserLongitude, string UserPositionName)
        {

            var result = new ResultEx() { Message = "发送失败" };
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = MsgConent, MsgType = MsgType };
            var db = new DBLiveVideoEntities();
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try { client.Connect(clientId); }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            //发送给案件警力所有成员user
            var users = GetUsers(CaseCode);
            var sendUser = db.tbUsers.Where(m => m.uCode.Equals(UserCode)).SingleOrDefault();
            var sendUserDepartment = db.tbDepartments.Where(m => m.dCode.TrimEnd().Equals(sendUser.dCode.TrimEnd())).SingleOrDefault();
            var addMsgModel = new tbCaseMessage()
            {
                msgFile = String.IsNullOrEmpty(msgFile) ? null : msgFile,
                msgType = MsgType,
                msgAbstract = MsgConent,
                csCode = CaseCode,
                uCode = UserCode,
                uLatitude = UserLatitude,
                uLongitude = UserLongitude,
                uPositionName = UserPositionName
            };



            var msgResult = AddCaseMessage(addMsgModel);
            if (!msgResult.IsSuccess || msgResult.Entity == null)
            {
                result.Message = "发送失败数据新增失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }


            var caseMsgModel = (tbCaseMessage)msgResult.Entity;
            var caseModel = db.tbCases.Where(m => m.csCode.Equals(CaseCode)).SingleOrDefault();

            var tbFile = db.tbFileDetails.Where(m => m.fCode.Equals(caseMsgModel.msgFile)).FirstOrDefault();
            tbFileDetail picFile = null;
            if (tbFile != null && !string.IsNullOrEmpty(tbFile.fFirstFrame))
                picFile = db.tbFileDetails.Where(m => m.fCode.Equals(tbFile.fFirstFrame)).FirstOrDefault();

            var chatmodel = new ChatModel
            {
                SendUserLatitude = caseMsgModel.uLatitude.ToString(),
                SendPositionName = caseMsgModel.uPositionName,
                SendUserLongitude = caseMsgModel.uLongitude.ToString(),

                SendUserId = sendUser.ID.ToString(),
                SendUserCode = sendUser.uCode,
                SendUserName = sendUser.uName,
                SendUserDepartment = sendUserDepartment.dName,
                SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(sendUser.uHeadPortrait),
                SendUserDuty = sendUser.uDuty,
                MsgTime = caseMsgModel.msgTime == null ? "" : ((DateTime)caseMsgModel.msgTime).ToString("yyyy-MM-dd HH:mm:ss"),
                CaseCode = caseMsgModel.csCode,
                MsgId = caseMsgModel.ID.ToString(),
                CaseName = caseModel.csName,
                CaseId = caseModel.ID.ToString(),
                MsgType = caseMsgModel.msgType,
                //MsgContent = GetMsgJson(caseMsgModel.msgType,caseMsgModel.msgAbstract,caseMsgModel.msgFile)
                MsgContent = GetMsgJson(caseMsgModel.msgType, caseMsgModel.msgAbstract, tbFile, picFile)
            };









            msgModel.MsgContent = JsonConvert.SerializeObject(chatmodel);
            var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");
            var groupListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Group");
            foreach (var user in users)
            {
                if (user.uCode.TrimEnd().Equals(UserCode.TrimEnd()))
                    continue;
                client.Publish(userListenerTag + user.uCode.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
            }
            this.release(client);
            //client.Disconnect();
            result.Entity = JsonConvert.SerializeObject(chatmodel);
            result.Message = "发送成功";
            result.IsSuccess = true;
            result.ResultCode = "1";
            return result;
        }

        #endregion




        public ResultEx SetToken(string uCode)
        {
            var model = new tbApiToken()
            {
                ID = Guid.NewGuid().ToString(),
                isDel = false,
                recSN = Common.Utils.GetTimeStampBits(),
                createdTime = DateTime.Now,
                uCode = uCode
            };

            var result = new ResultEx();
            try
            {

                var bll = new DBLiveVideoEntities();
                var newModel = new tbApiToken();
                if (!string.IsNullOrWhiteSpace(model.uCode))
                    newModel = bll.tbApiTokens.FirstOrDefault(m => m.uCode.TrimEnd().Equals(model.uCode.TrimEnd()));
                if (newModel != null)
                {

                    newModel.apiToken = Bll.Common.RandomIdGenerator.GetBase62(16);

                    if (bll.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Entity = newModel;
                        result.Message = "更新秘钥成功";
                        return result;
                    }
                    else
                    {

                        result.IsSuccess = false;
                        result.Message = "更新秘钥失败";
                        return result;
                    }

                }
                else
                {
                    model.uCode = uCode;
                    model.apiToken = Bll.Common.RandomIdGenerator.GetBase62(16);
                    bll.tbApiTokens.Add(model);
                    if (bll.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Message = "新增秘钥成功";
                        result.Entity = model;
                        return result;
                    }
                    else
                    {
                        result.IsSuccess = false;
                        result.Message = "新增秘钥失败";
                        return result;
                    }
                }


            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "发生不可预见错误";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = string.Format("代号：{0}", "001");

            }


            return result;
        }

        /// <summary>
        /// 新增用户
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddUser(tbUser model)
        {
            var result = new ResultEx();
            try
            {
                //model.ID = Guid.NewGuid().ToString();
                model.isDel = 0;
                model.uIsActive = 1;
                model.recSN = Common.Utils.GetTimeStampBits();
                if (string.IsNullOrEmpty(model.uPassword))
                    model.uPassword = "E10ADC3949BA59ABBE56E057F20F883E";//123456
                if (string.IsNullOrEmpty(model.uCode))
                {
                    result.IsSuccess = false;
                    result.Message = "用户编号不能为空";
                    result.Exception = "";
                    result.ResultCode = "-1";
                    return result;
                }
                if (string.IsNullOrEmpty(model.dCode))
                {
                    result.IsSuccess = false;
                    result.Message = "用户部门不能为空";
                    result.Exception = "";
                    result.ResultCode = "-1";
                    return result;
                }
                var db = new DBLiveVideoEntities();
                var existModel = new tbUser();
                if (!string.IsNullOrWhiteSpace(model.uCode))
                    existModel = db.tbUsers.FirstOrDefault(m => m.uCode.TrimEnd().Equals(model.uCode.TrimEnd()));
                if (existModel != null)
                {
                    result.IsSuccess = false;
                    result.Message = "改用户已经存在";
                    result.Exception = "改用户已经存在";
                    result.ResultCode = "-1";

                }
                else
                {
                    db.tbUsers.Add(model);
                    if (db.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Message = "添加成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                    }
                    else
                    {
                        result.IsSuccess = false;
                        result.Message = "添加失败";
                        result.ResultCode = "-1";
                    }

                    result.Entity = model;
                }


            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }

        /// <summary>
        /// 添加部门
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddDepartment(tbDepartment model)
        {
            var result = new ResultEx();
            try
            {

                //model.ID = Guid.NewGuid().ToString();

                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = 0;
                if (string.IsNullOrEmpty(model.dCode))
                    model.dCode = Common.RandomIdGenerator.GetBase62(16);

                var db = new DBLiveVideoEntities();

                db.tbDepartments.Add(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        /// <summary>
        /// 添加新消息
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddCaseMessage(tbCaseMessage model)
        {
            var result = new ResultEx();
            try
            {
                //var db = new DBLiveVideoEntities();
                model.ID = DateTime.Now.Ticks;
                model.csCode = Guid.NewGuid().ToString();
                //model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;
                model.msgTime = DateTime.Now;
                if (string.IsNullOrEmpty(model.msgFromType))
                    model.msgFromType = MsgFromTypes.Case.ToString();

                //model.msgID = (short)(db.tbCaseMessages.Count() + 1);
                //if (string.IsNullOrEmpty(model.msgID))
                //    model.msgID = Common.RandomIdGenerator.GetBase62(16);

                //if (string.IsNullOrEmpty(model.msgID))
                //    model.msgID = Common.RandomIdGenerator.GetBase62(16);
                MyBatisHelper.InsertObject("add_case_message", model);
                result.IsSuccess = true;
                result.Message = "添加成功";
                result.Exception = "";
                result.ResultCode = "1";
                /*
                db.tbCaseMessages.Add(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }
                */
                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                LogHelper.WriteError(typeof(BaseManage), "Add Case Messsage Error", ex);
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";
            }
            return result;
        }
        /// <summary>
        /// 添加部门
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddRole(tbRole model)
        {
            var result = new ResultEx();
            try
            {

                model.ID = Guid.NewGuid().ToString();

                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;


                var db = new DBLiveVideoEntities();

                db.tbRoles.Add(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }
        /// <summary>
        /// 创建案件
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddCase(tbCas model)
        {
            var result = new ResultEx();
            try
            {

                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;

                var db = new DBLiveVideoEntities();

                db.tbCases.Add(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                    result.Entity = model;
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        /// <summary>
        /// 为案件部署警员
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddCaseDeploy(tbCaseDeploy model)
        {
            var result = new ResultEx();
            try
            {


                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;
                var db = new DBLiveVideoEntities();

                db.tbCaseDeploys.Add(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        /// <summary>
        /// 获取当前的用户
        /// </summary>
        /// <returns></returns>
        public List<UserModel> GetCaseDeployUsers(string caseCode)
        {
            //        join a in db.tbCases on c.csCode equals a.csCode
            //join d in db.tbUsers on c.uCode equals d.uCode
            //join e in db.tbDepartments on d.dCode equals e.dCode into e_join


            var db = new DBLiveVideoEntities();
            var models = (
           from b in db.tbCaseDeploys
           join a in db.tbUsers on b.uCode equals a.uCode
           join c in db.tbDepartments on a.dCode equals c.dCode into c_join
           from c in c_join.DefaultIfEmpty()
           where b.csCode.Equals(caseCode)
           select new UserModel()
           {
               UserId = a.ID.ToString(),
               UserCode = a.uCode,
               UserName = a.uName,
               LoginName = a.uCode,
               UserPhone = a.uTel,
               UserPCNum = a.pcNum,

               HeadPortrait = a.uHeadPortrait,
               DepartmentCode = a.dCode,
               UserShortNum = a.uShortNum,
               RoleName = a.rName,
               //UserPwd=model.UserPwd,
               //ApiToken = token.apiToken,
               UserDuty = a.uDuty,
               Department = c.dName,
           }
          ).OrderByDescending(m => m.DepartmentCode).ToList();
            foreach (var user in models)
            {
                user.HeadPortrait = Bll.Common.Utils.toSiteUrl(user.HeadPortrait);
            }
            return models;

        }


        /// <summary>
        /// 获取当前案件部署的警员
        /// csCode案件编号
        /// </summary>
        /// <returns></returns>
        public List<tbUser> GetUsers(string csCode)
        {
            var db = new DBLiveVideoEntities();

            var model = (
                from b in db.tbCaseDeploys
                join a in db.tbUsers on b.uCode equals a.uCode into a_join
                from a in a_join.DefaultIfEmpty()
                where b.csCode.Equals(csCode)
                select a
               ).ToList();

            foreach (var user in model)
            {
                if (user != null)
                    user.uHeadPortrait = Utils.toSiteUrl(user.uHeadPortrait);
            }
            return model;

        }
        /// <summary>
        /// 获取我的秘钥
        /// </summary>
        /// <param name="UserId"></param>
        /// <returns></returns>Code
        public string GetMyToke(string uCode)
        {
            var db = new DBLiveVideoEntities();
            var model = db.tbApiTokens.Where(m => m.uCode.Equals(uCode)).SingleOrDefault();
            if (model != null)
                return model.apiToken;
            else
                return "";
        }

        /// <summary>
        /// 获取个人案件信息
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        public List<CaseModel> GetMyCases(string uCode, string skip, string take)
        {
            return GetMyCases(uCode, skip, take, string.Empty, null, null, null, null);
        }
        /// <summary>
        /// 获取所有案件的
        /// </summary>
        /// <param name="uCode">用户编号</param>
        /// <param name="skip">跳过几条</param>
        /// <param name="take">取多少条数据</param>
        /// <param name="status">案件状态，如已立案，已结案， 空的话忽略查询所有</param>
        /// <param name="caseName">案件名称，  空的话忽略查询所有</param>
        /// <param name="caseType">案件类别，  空的话忽略查询所有</param>
        /// <param name="startTime">立案时间 大于starttime   空的话忽略查询所有</param>
        /// <param name="endTime">立案时间小于endtime  空的话忽略查询所有</param>
        /// <returns></returns>
        public List<CaseModel> GetMyCases(string uCode, string skip, string take, string status,
            string caseName, string caseType, string startTime, string endTime
            )
        {
            var _startTime = Common.Utils.toDatetime(startTime);
            var _endTime = Common.Utils.toDatetime(endTime);
            _endTime = _endTime.AddDays(1);
            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            _take = _take <= 0 ? 10 : _take;
            var models = (
                 from c in db.tbCases
                 join a in db.tbCaseDeploys on c.csCode equals a.csCode into a_join
                 from a in a_join.DefaultIfEmpty()
                 where a.uCode.Equals(uCode) && (string.IsNullOrEmpty(status) || c.csStatus.Trim().Equals(status.Trim()))

                     && (string.IsNullOrEmpty(caseName) || c.csName.Contains(caseName))
                    && (string.IsNullOrEmpty(caseType) || c.csType.Contains(caseType))
                    && (string.IsNullOrEmpty(startTime) || c.csTime >= _startTime)
                    && (string.IsNullOrEmpty(endTime) || c.csTime < _endTime)
                 select new CaseModel()
                 {
                     CaseName = c.csName,
                     CaseCreatedTime = c.csCreateTime.ToString(),
                     _CaseCreatedTime = c.csCreateTime,
                     ImageUrl = ""
                    ,
                     Department = "",
                     CaseId = c.ID.ToString(),
                     CaseCode = c.csCode,
                     CaseType = c.csType,
                     csTime_h = c.csTime_h,
                     csTime = c.csTime,
                     CaseTime = c.csTime.ToString(),
                     CaseMaxTime = c.csTime_h.ToString(),
                     CaseSummary = c.csDesc,
                     CaseState = c.csStatus,
                     CaseAddr = c.csAddress,
                     _csEndTime = c.csEndTime,
                     csEndTime = c.csEndTime.ToString(),
                     CaseAddres = c.csAddress,
                     CaseAddresLatitude = c.csLatitude.ToString(),
                     CaseAddresLongitude = c.csLongitude.ToString()

                 }
                ).OrderByDescending(m => m.csTime).Skip(_skip).Take(_take).ToList();

            var caseCodes = models.Select(m => m.CaseCode).ToList();

            var modelCommanders = db.tbCaseDeploys.Where(m => caseCodes.Contains(m.csCode) && m.rName.Trim().Equals("指挥官")).ToList();

            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.CaseTime))
                    model.CaseTime = ((DateTime)model.csTime).ToString("yyyy-MM-dd HH:mm:ss");
                if (!string.IsNullOrEmpty(model.CaseMaxTime))
                    model.CaseMaxTime = ((DateTime)model.csTime_h).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.CaseCreatedTime))
                    model.CaseCreatedTime = ((DateTime)model._CaseCreatedTime).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.csEndTime))
                    model.csEndTime = ((DateTime)model._csEndTime).ToString("yyyy-MM-dd HH:mm:ss");

                var commanders = modelCommanders.Where(m => m.csCode.Equals(model.CaseCode)).Select(n => n.uCode).ToList();

                if (commanders != null)
                {
                    string names = "";
                    foreach (var user in commanders)
                    {
                        if (!string.IsNullOrEmpty(user))
                        {
                            if (string.IsNullOrEmpty(names))
                                names = user.Trim();
                            else
                                names = names + "," + user.Trim();
                        }
                    }

                    model.CaseCommanderUserCode = names;
                }
            }
            return models;
        }

        public List<CaseModel> GetAllCases(string uCode, string status, string caseName, string caseType)
        {
            return GetAllCases(uCode, status, caseName, caseType, null);
        }
        /// <summary>
        /// 获取所有案件的
        /// </summary>
        /// <param name="status">案件状态，如已立案，已结案， 空的话忽略查询所有</param>
        /// <param name="caseName">案件名称，  空的话忽略查询所有</param>
        /// <param name="caseType">案件类别，  空的话忽略查询所有</param>
        /// <param name="rName">用户角色，  空的话忽略查询所有</param>
        /// <returns></returns>
        public List<CaseModel> GetAllCases(string uCode, string status, string caseName, string caseType, string rName)
        {

            var db = new DBLiveVideoEntities();

            var isAdmin = false;
            var userModel = db.tbUsers.Where(m => m.rName.Equals(rName) && m.uCode.Equals(uCode)).FirstOrDefault();
            if (userModel != null)
            {
                //管理员
                isAdmin = true;
            }
            else
            {
                isAdmin = false;
                //不是管理员
            }
            if (string.IsNullOrEmpty(rName))
                isAdmin = false;
            //var caseDeploy = db.tbCaseDeploys.Where(m => m.uCode.Trim().Equals(uCode.Trim())).FirstOrDefault();
            var models = new List<CaseModel>();
            if (isAdmin)
            {
                models = (
                 from c in db.tbCases
                 where
                      (string.IsNullOrEmpty(status) || c.csStatus.Trim().Equals(status.Trim()))
                     && (string.IsNullOrEmpty(caseName) || c.csName.Contains(caseName))
                    && (string.IsNullOrEmpty(caseType) || c.csType.Contains(caseType))
                 select new CaseModel()
                 {
                     CaseName = c.csName,
                     CaseCreatedTime = c.csCreateTime.ToString(),
                     _CaseCreatedTime = c.csCreateTime,
                     ImageUrl = ""
                     ,
                     Department = "",
                     CaseId = c.ID.ToString(),
                     CaseCode = c.csCode,
                     CaseType = c.csType,
                     csTime_h = c.csTime_h,
                     csTime = c.csTime,
                     CaseTime = c.csTime.ToString(),
                     CaseMaxTime = c.csTime_h.ToString(),
                     CaseSummary = c.csDesc,
                     CaseState = c.csStatus,
                     CaseAddr = c.csAddress

                 }
                ).OrderByDescending(m => m.csTime).ToList();

            }
            else
            {
                models = (
                     from c in db.tbCases
                     join a in db.tbCaseDeploys on c.csCode equals a.csCode into a_join
                     from a in a_join.DefaultIfEmpty()
                     where a.uCode.Equals(uCode)
                         && (string.IsNullOrEmpty(status) || c.csStatus.Trim().Equals(status.Trim()))
                         && (string.IsNullOrEmpty(caseName) || c.csName.Contains(caseName))
                        && (string.IsNullOrEmpty(caseType) || c.csType.Contains(caseType))
                     select new CaseModel()
                     {
                         CaseName = c.csName,
                         CaseCreatedTime = c.csCreateTime.ToString(),
                         _CaseCreatedTime = c.csCreateTime,
                         ImageUrl = ""
                         ,
                         Department = "",
                         CaseId = c.ID.ToString(),
                         CaseCode = c.csCode,
                         CaseType = c.csType,
                         csTime_h = c.csTime_h,
                         csTime = c.csTime,
                         CaseTime = c.csTime.ToString(),
                         CaseMaxTime = c.csTime_h.ToString(),
                         CaseSummary = c.csDesc,
                         CaseState = c.csStatus,
                         CaseAddr = c.csAddress

                     }
                    ).OrderByDescending(m => m.csTime).ToList();

            }



            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.CaseTime))
                    model.CaseTime = ((DateTime)model.csTime).ToString("yyyy-MM-dd HH:mm:ss");
                if (!string.IsNullOrEmpty(model.CaseMaxTime))
                    model.CaseMaxTime = ((DateTime)model.csTime_h).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.CaseCreatedTime))
                    model.CaseCreatedTime = ((DateTime)model._CaseCreatedTime).ToString("yyyy-MM-dd HH:mm:ss");
            }
            return models;
        }


        /// <summary>
        /// CCS案件列表查询已立案按照时间倒序排列，添加滚动条。
        /// </summary>
        /// <param name="uCode"></param>
        /// <returns></returns>
        public List<CaseModel> GetCases(string uCode)
        {
            var db = new DBLiveVideoEntities();
            var models = (
                 from c in db.tbCases
                 join a in db.tbCaseDeploys on c.csCode equals a.csCode into a_join
                 from a in a_join.DefaultIfEmpty()
                 where a.uCode.Equals(uCode)
                 select new CaseModel()
                 {
                     CaseName = c.csName,
                     CaseCreatedTime = c.csCreateTime.ToString(),
                     _CaseCreatedTime = c.csCreateTime,
                     ImageUrl = ""
                     ,
                     Department = "",
                     CaseId = c.ID.ToString(),
                     CaseCode = c.csCode,
                     CaseType = c.csType,
                     csTime_h = c.csTime_h,
                     csTime = c.csTime,
                     CaseTime = c.csTime.ToString(),
                     CaseMaxTime = c.csTime_h.ToString(),
                     CaseSummary = c.csDesc,
                     CaseState = c.csStatus,
                     CaseAddr = c.csAddress,
                     CsRegDate = c.csRegDate.ToString(),
                     _csRegDate = c.csRegDate,


                 }
                ).OrderByDescending(m => m.csTime).ToList();


            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.CaseTime))
                    model.CaseTime = ((DateTime)model.csTime).ToString("yyyy-MM-dd HH:mm:ss");
                if (!string.IsNullOrEmpty(model.CaseMaxTime))
                    model.CaseMaxTime = ((DateTime)model.csTime_h).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.CaseCreatedTime))
                    model.CaseCreatedTime = ((DateTime)model._CaseCreatedTime).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.CsRegDate))
                    model.CsRegDate = ((DateTime)model._csRegDate).ToString("yyyy-MM-dd HH:mm:ss");
            }
            return models;
        }
        /// <summary>
        /// 获取个人聊天信息
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        public List<ChatModel> GetMyChats(string csCode, string skip, string take, string firstMsgId)
        {
            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            var _maxTime = DateTime.Now;
            if (!string.IsNullOrEmpty(firstMsgId))
            {
                var firstModel = db.tbCaseMessages.Where(m => m.ID.ToString().Equals(firstMsgId)).SingleOrDefault();
                if (firstModel != null && firstModel.msgTime != null)
                    _maxTime = (DateTime)firstModel.msgTime;
            }


            _take = _take <= 0 ? 10 : _take;
            var models = (
                 from c in db.tbCaseMessages
                 join a in db.tbCases on c.csCode equals a.csCode
                 join d in db.tbUsers on c.uCode equals d.uCode
                 join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                 from g in e_join.DefaultIfEmpty()
                 where a.csCode.Equals(csCode) && c.msgTime < _maxTime
                 select new ChatModel()
                 {
                     CaseName = a.csName,
                     MsgTime = c.msgTime.ToString(),
                     CreatedTime = c.msgTime,
                     SendUserId = d.ID.ToString(),
                     SendUserCode = d.uCode,
                     SendUserDuty = d.uDuty,
                     SendUserName = d.uName,
                     MsgContent = c.msgAbstract,
                     MsgType = c.msgType,
                     MsgId = c.ID.ToString(),
                     MsgFile = c.msgFile,//
                     CaseId = a.ID.ToString(),
                     CaseCode = a.csCode,
                     SendUserDepartment = g.dName,
                     //SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(d.uHeadPortrait),
                     SendUserHeadPortrait = d.uHeadPortrait,
                     SendUserDepartmentCode = g.dCode,

                     SendUserLatitude = c.uLatitude.ToString(),
                     SendPositionName = c.uPositionName,
                     SendUserLongitude = c.uLongitude.ToString(),
                     MsgFromType = c.msgFromType,
                     MsgToCode = c.msgToCode
                 }
                )
                .OrderByDescending(m => m.CreatedTime).Skip(_skip).Take(_take).ToList();


            var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
            var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
            var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
            var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();

            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();
                var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                tbFileDetail picFile = null;
                if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                    picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);

                model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                if (!string.IsNullOrEmpty(model.MsgTime))
                    model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

            }

            //if (!string.IsNullOrEmpty(maxTime) && _maxTime != null)
            //    models = models.Where(m => Common.Utils.toDatetime(m.MsgTime) <= _maxTime).ToList();

            models.Reverse();
            return models;
        }


        /// <summary>
        /// 转换为消息内容
        /// </summary>
        /// <param name="msgType">消息类型</param>
        /// <param name="msgAbstract">小文字内容</param>
        /// <param name="tbFile">文件型的内容</param>
        /// <returns>返回json</returns>
        //public string GetMsgJson(string msgType, string msgAbstract, string fCode)
        //{
        //    var result = string.Empty;

        //    if (!Utils.isConatinMsgType(msgType))
        //        msgType = Utils.getDefaultMsgTYpe().ToString();


        //    var _msgType = (MsgTypes)Enum.Parse(typeof(MsgTypes), msgType);
        //    if (_msgType != null)
        //    {

        //        var tbFile = GetFile(fCode);
        //        switch (_msgType)
        //        {
        //            case MsgTypes.Image:
        //                if (tbFile == null) return result;
        //                result = JsonConvert.SerializeObject(new ImageModel()
        //                {
        //                    ImageUrl = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath),
        //                    FileName = tbFile.fName,
        //                    FileCode = tbFile.fCode,
        //                    FileSize = Bll.Common.Utils.toString(tbFile.fSize)
        //                });
        //                break;
        //            case MsgTypes.Text:
        //                result = JsonConvert.SerializeObject(new TextModel()
        //                {
        //                    Text = msgAbstract
        //                });
        //                break;
        //            case MsgTypes.Audio:
        //                if (tbFile == null) return result;
        //                result = JsonConvert.SerializeObject(new AudioModel()
        //                {
        //                    AudioUrl = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath),
        //                    AudioDescription = tbFile.fAbstract,
        //                    AudioStartTime = tbFile.fStartTime == null ? "" : ((DateTime)tbFile.fStartTime).ToString("yyyy-MM-dd HH:mm:ss"),
        //                    AudioEndTime = tbFile.fEndTime == null ? "" : ((DateTime)tbFile.fEndTime).ToString("yyyy-MM-dd HH:mm:ss"),
        //                    FileName = tbFile.fName,
        //                    FileCode = tbFile.fCode,
        //                    FileSize = Bll.Common.Utils.toString(tbFile.fSize)
        //                });
        //                break;
        //            case MsgTypes.Video:
        //                if (tbFile == null) return result;
        //                var picFile = GetFile(tbFile.fFirstFrame);

        //                var picUrl = string.Empty;
        //                if (picFile != null)
        //                    picUrl = Bll.Common.Utils.toSiteUrl((string.IsNullOrEmpty(picFile.fRelativePath) ? "" : picFile.fRelativePath));

        //                var videoFile = string.Empty;
        //                if (!string.IsNullOrEmpty(tbFile.fRelativePath) && !tbFile.fRelativePath.Contains("rtmp:"))
        //                    videoFile = Common.Utils.toSiteUrl(tbFile.fRelativePath);
        //                else
        //                    videoFile = tbFile.fRelativePath;
        //                result = JsonConvert.SerializeObject(new VideoModel()
        //                {

        //                    VideoUrl = videoFile,
        //                    ShowPicutre = picUrl,
        //                    VideoDescription = tbFile.fAbstract,
        //                    VideoStartTime = tbFile.fStartTime == null ? "" : ((DateTime)tbFile.fStartTime).ToString("yyyy-MM-dd HH:mm:ss"),
        //                    VideoEndTime = tbFile.fEndTime == null ? "" : ((DateTime)tbFile.fEndTime).ToString("yyyy-MM-dd HH:mm:ss"),
        //                    FileName = tbFile.fName,
        //                    FileCode = tbFile.fCode,
        //                    FileSize = Bll.Common.Utils.toString(tbFile.fSize)
        //                });
        //                break;
        //            case MsgTypes.Living:
        //                break;
        //            case MsgTypes.Map:
        //                if (tbFile == null) return result;
        //                result = JsonConvert.SerializeObject(new MapModel()
        //                {
        //                    ShowPicutre = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath)
        //                });
        //                break;

        //        }
        //    }

        //    return result;
        //}
        public string GetMsgJson(string msgType, string msgAbstract, tbFileDetail tbFile, tbFileDetail picFile)
        {
            var result = string.Empty;

            if (!Utils.isConatinMsgType(msgType))
                msgType = Utils.getDefaultMsgTYpe().ToString();


            var _msgType = (MsgTypes)Enum.Parse(typeof(MsgTypes), msgType);
            if (_msgType != null)
            {

                //var tbFile = GetFile(fCode);
                switch (_msgType)
                {
                    case MsgTypes.Image:
                        if (tbFile == null) return result;
                        result = JsonConvert.SerializeObject(new ImageModel()
                        {
                            FileId = tbFile.ID.ToString(),
                            ImageUrl = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath),
                            FileName = tbFile.fName,
                            FileCode = tbFile.fCode,
                            FileSize = Bll.Common.Utils.toString(tbFile.fSize)
                        });
                        break;
                    case MsgTypes.Text:
                        result = JsonConvert.SerializeObject(new TextModel()
                        {
                            Text = msgAbstract
                        });
                        break;
                    case MsgTypes.Audio:
                        if (tbFile == null) return result;
                        result = JsonConvert.SerializeObject(new AudioModel()
                        {
                            FileId = tbFile.ID.ToString(),
                            AudioUrl = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath),
                            AudioDescription = tbFile.fAbstract,
                            AudioStartTime = tbFile.fStartTime == null ? "" : ((DateTime)tbFile.fStartTime).ToString("yyyy-MM-dd HH:mm:ss"),
                            AudioEndTime = tbFile.fEndTime == null ? "" : ((DateTime)tbFile.fEndTime).ToString("yyyy-MM-dd HH:mm:ss"),
                            FileName = tbFile.fName,
                            FileCode = tbFile.fCode,
                            Duration = tbFile.fduration,
                            FileSize = Bll.Common.Utils.toString(tbFile.fSize)
                        });
                        break;
                    case MsgTypes.Video:
                        if (tbFile == null) return result;
                        //var picFile = GetFile(tbFile.fFirstFrame);

                        var picUrl = string.Empty;
                        if (picFile != null)
                            picUrl = Bll.Common.Utils.toSiteUrl((string.IsNullOrEmpty(picFile.fRelativePath) ? "" : picFile.fRelativePath));

                        var videoFile = string.Empty;
                        if (!string.IsNullOrEmpty(tbFile.fRelativePath) && !tbFile.fRelativePath.Contains("rtmp:"))
                            videoFile = Common.Utils.toSiteUrl(tbFile.fRelativePath);
                        else
                            videoFile = tbFile.fRelativePath;
                        result = JsonConvert.SerializeObject(new VideoModel()
                        {
                            FileId = tbFile.ID.ToString(),
                            VideoUrl = videoFile,
                            ShowPicutre = picUrl,
                            VideoDescription = tbFile.fAbstract,
                            VideoStartTime = tbFile.fStartTime == null ? "" : ((DateTime)tbFile.fStartTime).ToString("yyyy-MM-dd HH:mm:ss"),
                            VideoEndTime = tbFile.fEndTime == null ? "" : ((DateTime)tbFile.fEndTime).ToString("yyyy-MM-dd HH:mm:ss"),
                            FileName = tbFile.fName,
                            FileCode = tbFile.fCode,
                            Duration=tbFile.fduration,
                            FileSize = Bll.Common.Utils.toString(tbFile.fSize)
                        });
                        break;
                    case MsgTypes.Living:
                        break;
                    case MsgTypes.Map:
                        if (tbFile == null) return result;
                        result = JsonConvert.SerializeObject(new MapModel()
                        {
                            FileId = tbFile.ID.ToString(),
                            FileCode = tbFile.fCode,
                            ShowPicutre = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath)
                        });
                        break;
                    case MsgTypes.File:
                        if (tbFile == null) return result;
                        result = JsonConvert.SerializeObject(new FileModel()
                        {
                            FileId = tbFile.ID.ToString(),
                            FileCode = tbFile.fCode,
                            OriginalFileName=msgAbstract,
                            FielDownLoadUrl = Bll.Common.Utils.toSiteUrl(tbFile.fRelativePath)
                        });
                        break;

                }
            }

            return result;
        }


        public ResultEx AddFile(tbFileDetail model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;
                if (string.IsNullOrEmpty(model.fCode))
                    model.fCode = Bll.Common.RandomIdGenerator.GetBase62(14);

                var db = new DBLiveVideoEntities();
                db.tbFileDetails.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                //result.Exception = "添加失败";

            }


            return result;
        }

        /// <summary>
        /// 更新视频文件
        /// </summary>
        /// <param name="fCode"></param>
        /// <param name="fFirstFrame">第一帧图片编码</param>
        /// <param name="fStartTime">视频开始时间</param>
        /// <param name="fEndTime">视频结束时间</param>
        /// <returns></returns>
        public ResultEx updateVideoFile(string fCode, string fFirstFrame, string fStartTime, string fEndTime,string fDuration)
        {
            var result = new ResultEx();
            try
            {

                if (string.IsNullOrEmpty(fCode))
                    return result;

                var db = new DBLiveVideoEntities();
                var model = db.tbFileDetails.Where(m => m.fCode.Trim().Equals(fCode.Trim())).SingleOrDefault();
                if (!string.IsNullOrEmpty(fFirstFrame))
                    model.fFirstFrame = fFirstFrame;
                if (!string.IsNullOrEmpty(fStartTime))
                    model.fStartTime = Common.Utils.toDatetime(fStartTime);
                if (!string.IsNullOrEmpty(fEndTime))
                    model.fEndTime = Common.Utils.toDatetime(fEndTime);
                if (!string.IsNullOrEmpty(fDuration))
                    model.fduration = fDuration;
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "更新成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "更新失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "更新失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "更新失败";

            }


            return result;
        }

        public tbFileDetail GetFile(string fCode)
        {
            var result = new ResultEx();
            try
            {
                if (string.IsNullOrEmpty(fCode))
                    return null;

                var db = new DBLiveVideoEntities();
                var model = db.tbFileDetails.Where(m => m.fCode.TrimEnd().Equals(fCode.TrimEnd())).SingleOrDefault();
                return model;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        ///// <summary>
        ///// 
        ///// </summary>
        ///// <param name="model"></param>
        ///// <returns></returns>
        //public ResultEx AddLiving(t_living model)
        //{
        //    var result = new ResultEx();
        //    try
        //    {

        //        model.LiveId = Common.Utils.GetUniqueId().ToString();
        //        if(string.IsNullOrEmpty(model.Living))
        //        model.Living = "0";
        //        model.CreatedTime = DateTime.Now;
        //        var bll = new livevideoDBEntities();

        //        bll.t_living.Add(model);
        //        if (bll.SaveChanges() > 0)
        //        {
        //            result.IsSuccess = true;
        //            result.Message = "添加成功";
        //            result.Exception = "";
        //            result.ResultCode = "1";
        //        }
        //        else
        //        {
        //            result.IsSuccess = false;
        //            result.Message = "添加失败";
        //            result.ResultCode = "-1";
        //        }

        //        result.Entity = model;

        //    }
        //    catch (Exception ex)
        //    {
        //        result.IsSuccess = false;
        //        result.Message = "添加失败";
        //        //result.Exception = ex.GetBaseException().ToString();
        //        //记录错误日志
        //        result.Exception = "添加失败";

        //    }


        //    return result;
        //}


        public ResultEx updateLiving(string livingId, bool livingStatus)
        {
            var result = new ResultEx();
            try
            {
                var db = new DBLiveVideoEntities();
                var model = db.tbCaseLivingShows.Where(m => m.ID.ToString().Equals(livingId)).SingleOrDefault();
                var listKeys = Common.Utils.getIncomingStreamNames();
                var streamName = Common.Utils.getStreamNameFromRtmpUrl(model.rtmpUrl.Trim());

                if (livingStatus)
                {

                    if (!listKeys.Contains(streamName))
                    {
                        result.Message = "设置开启失败,设备并未直播。";
                        return result;
                    }
                }
                else
                {

                    if (listKeys.Contains(streamName))
                    {
                        result.Message = "设置关闭失败,设备还在直播。";
                        return result;
                    }
                }



                model.livingState = livingStatus;
                model.endTime = DateTime.Now;

                if (db.SaveChanges() > 0)
                {
                    //if (!livingStatus)
                    //{ 
                    //    //关闭直播 do  把录像发给部署警员-发聊天消息
                    //    PushMsgCase(model.userCode, model.caseCode, MsgTypes.Video.ToString(), "", "");



                    //}
                    result.IsSuccess = true;
                    result.Message = livingStatus ? "开启直播" : "关闭直播";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "更新失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "更新失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "更新失败";

            }
            return result;
        }


        /// <summary>
        /// 推送直播
        /// </summary>
        /// <param name="FromUserId"></param>
        /// <param name="UserName"></param>
        /// <param name="CaseId"></param>
        /// <param name="MsgType"></param>
        /// <param name="rtmpUrl"></param>
        /// <returns></returns>
        public ResultEx pushLivingCase(string UserCode, string CaseCode, string rtmpUrl, string sourceType, string deviceCode, string fFirstFrame)
        {

            var result = new ResultEx() { Message = "发送失败" };



            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = "", MsgType = MsgTypes.Living.ToString() };
            var db = new DBLiveVideoEntities();
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try { client.Connect(clientId); }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var _sourceType = string.Empty;
            if (string.IsNullOrEmpty(sourceType))
            {
                _sourceType = SourceType.Phone.ToString();
            }
            else _sourceType = sourceType;

            if (_sourceType.Equals(SourceType.Camera.ToString().Trim()))
            {
                var deviceModel = db.tbDevices.Where(m => m.devCode.Equals(deviceCode)).FirstOrDefault();
                var deviceName = deviceModel != null ? (string.IsNullOrEmpty(deviceModel.devName) ? "" : deviceModel.devName.Trim()) : "";
                var listKeys = Common.Utils.getIncomingStreamNames();
                var streamName = Common.Utils.getStreamNameFromRtmpUrl(rtmpUrl);
                if (!listKeys.Contains(streamName))
                {
                    result.Message = deviceName + "设备未开启直播。";
                    return result;
                }
            }



            var sendUser = db.tbUsers.Where(m => m.uCode.Equals(UserCode)).SingleOrDefault();
            var caseModel = db.tbCases.Where(m => m.csCode.Equals(CaseCode)).SingleOrDefault();

            tbCaseLivingShow livingResultModel = null;
            livingResultModel = db.tbCaseLivingShows.Where(m => m.rtmpUrl.Equals(rtmpUrl.Trim())).SingleOrDefault();
            if (livingResultModel != null)
            {
                livingResultModel.livingState = true;
                livingResultModel.caseCode = CaseCode;
                livingResultModel.userCode = UserCode;
                livingResultModel.sourceType = sourceType;
                livingResultModel.deviceCode = deviceCode;
                livingResultModel.startTime = DateTime.Now;
                livingResultModel.fFirstFrame = fFirstFrame;
                livingResultModel.Cumulative = livingResultModel.Cumulative + 1;
                db.SaveChanges();
            }
            else
            {
                var livingResult = AddCaseLivingShow(new tbCaseLivingShow()
                {
                    caseCode = CaseCode,
                    userCode = UserCode,
                    livingState = true,
                    endTime = null,
                    startTime = DateTime.Now,
                    rtmpUrl = rtmpUrl,
                    sourceType = _sourceType,
                    deviceCode = deviceCode,
                    fFirstFrame = fFirstFrame,
                    Cumulative = 0

                });

                if (livingResult.IsSuccess)
                {
                    livingResultModel = (tbCaseLivingShow)livingResult.Entity;
                }


            }


            if (livingResultModel != null)
            {

                //发送给案件警力所有成员user
                var users = GetUsers(CaseCode);
                var modelDepartment = db.tbDepartments.Where(m => m.dCode.Equals(sendUser.dCode)).SingleOrDefault();
                var departName = modelDepartment.dName;
                var msgLivingModel = new LivingModel()
                {
                    LivingState = livingResultModel.livingState.ToString(),
                    LiveId = livingResultModel.ID.ToString(),
                    RtmpUrl = livingResultModel.rtmpUrl,
                    PublishCaseCode = CaseCode,
                    PublishUserCode = sendUser.uCode,
                    PublishCaseName = caseModel.csName,
                    PublishTime = Utils.timeToString(livingResultModel.startTime),
                    PublishUserDepartment = modelDepartment.dName,
                    PublishUserDuty = sendUser.uDuty,
                    PublishUserHeadPortrait = Utils.toSiteUrl(sendUser.uHeadPortrait),
                    PublishUserName = sendUser.uName,
                    FirstFramePicUrl = getFirstFramePicUrlByCode(livingResultModel.fFirstFrame),
                    Cumulative = livingResultModel.Cumulative.ToString()
                };
                msgModel.MsgContent = JsonConvert.SerializeObject(msgLivingModel);
                var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");


                var listUsers = users.Select(m => m.uCode.Trim()).ToList();
                var userAdmins = db.tbCaseDeploys.Where(m => m.csCode.Trim().Equals(CaseCode) && m.rName.Trim().Equals("指挥官")).ToList();
                if (userAdmins != null && userAdmins.Count() > 0)
                {
                    foreach (var userTemp in userAdmins)
                    {
                        if (!string.IsNullOrEmpty(userTemp.uCode) && !listUsers.Contains(userTemp.uCode.Trim()))
                        {
                            listUsers.Add(userTemp.uCode.Trim());
                        }
                    }
                }
                foreach (var user in listUsers)
                {
                    if (user == null || string.IsNullOrEmpty(user) || user.TrimEnd().Equals(UserCode.TrimEnd()))
                        continue;
                    client.Publish(userListenerTag + user.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
                }
                this.release(client);
                //client.Disconnect();
                result.Entity = msgModel.MsgContent;
                result.Message = "布控成功";
                result.IsSuccess = true;
                result.ResultCode = "1";
                return result;
            }
            else
            {
                result.Entity = string.Empty;
                result.Message = "布控失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;

            }

        }
        /// <summary>
        /// 获取直播首帧的图片
        /// </summary>
        /// <param name="fCode"></param>
        /// <returns></returns>
        public string getFirstFramePicUrlByCode(string fCode)
        {

            var fistFramePicUrl = string.Empty;
            if (!string.IsNullOrEmpty(fCode))
            {
                var db = new DBLiveVideoEntities();
                var fistFrameModel = db.tbFileDetails.Where(m => m.fCode.Trim().Equals(fCode)).FirstOrDefault();
                if (fistFrameModel != null)
                {
                    fistFramePicUrl = Bll.Common.Utils.toSiteUrl((string.IsNullOrEmpty(fistFrameModel.fRelativePath) ? "" : fistFrameModel.fRelativePath));

                }
            }
            return fistFramePicUrl;
        }


        /// <summary>
        /// 获取直播
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        /// <summary>
        /// 获取直播案件
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        public List<LivingModel> GetMyLivings(string userCode, string skip, string take, string caseCode)
        {
            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            _take = _take <= 0 ? 10 : _take;
            var livingOn = Common.Utils.toString(VideoLivingStatus.On.GetHashCode());
            var models = (
                 from c in db.tbCaseLivingShows
                 join a in db.tbCases on c.caseCode equals a.csCode
                 join e in db.tbCaseDeploys on c.caseCode equals e.csCode
                 join d in db.tbUsers on c.userCode equals d.uCode
                 join k in db.tbUserIMDatas on c.userCode equals k.uCode
                 join f in db.tbDepartments on d.dCode equals f.dCode into d_join
                 from g in d_join.DefaultIfEmpty()
                 where e.uCode.Equals(userCode)
                 && (string.IsNullOrEmpty(caseCode) || c.caseCode.Equals(caseCode))

                 select new LivingModel()
                 {
                     PublishUserName = d.uName,
                     PublishUserDuty = d.uDuty,
                     PublishUserDepartment = g.dName,
                     PublishUserCode = d.uCode,
                     PublishTime = string.Empty,
                     PublishCaseName = a.csName,
                     LiveId = c.ID.ToString(),
                     LivingState = c.livingState.ToString(),
                     PublishCaseCode = c.caseCode,
                     RtmpUrl = c.rtmpUrl,
                     createdTime = c.startTime,
                     Cumulative = c.Cumulative.ToString(),
                     PublishUserLatitude = k.latitude.ToString(),
                     PublishUserLongitude = k.longitude.ToString(),
                     PublishUserPositionName = k.positionName,
                     PublishUserHeadPortrait = d.uHeadPortrait,
                     FirstFramePicUrl = c.fFirstFrame,

                 }
                ).OrderByDescending(m => m.createdTime).Skip(_skip).Take(_take).ToList();

            var newModels = new List<LivingModel>();
            //var livingModels = Common.Utils.getIncomingStream();
            //if (livingModels == null)
            //    return null;
            //var listKeys=new List<string>();
            //foreach (var model in livingModels)
            //{
            //    if(!string.IsNullOrEmpty(model.Name))
            //    listKeys.Add(model.Name);
            //}
            var listKeys = Common.Utils.getIncomingStreamNames();
            //var tbDevices = db.tbDevices.Where(m => listKeys.Contains(m.devSN)).ToList();//开了直播的第三方设备



            var listAppStreams = new List<string>();
            var listDeviceStreams = new List<string>();
            //App 直播的加上 第三方设备直播的
            var camera = DeloyType.Camera.ToString();
            //开了直播的第三方设备
            var modelDevices = (
                     from d in db.tbCaseDeployDevs
                     join c in db.tbDevices on d.devCode equals c.devCode into c_join
                     from c in c_join.DefaultIfEmpty()
                     join e in db.tbUsers on d.uCode equals e.uCode into e_join
                     from e in e_join.DefaultIfEmpty()
                     join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
                     from a in a_join.DefaultIfEmpty()
                     where d.csCode.Equals(caseCode) && listKeys.Contains(c.devSN.Trim())

                     select new DeployModel()
                     {
                         DevSN = c.devSN,
                         recSN = d.recSN,
                         DeployType = camera,
                         DeployDeviceCode = d.devCode,
                         DeployDeviceName = c.devName,
                         DeployDeviceUserCode = e.uCode,
                         DeployDeviceUserName = e.uName,
                         DeployDevicePhoto = a.fRelativePath,
                         Level = 2,

                     }).OrderByDescending(m => m.recSN).ToList();
            /// 设备位置信息
            var deviceIMDatas = db.tbDeviceIMDatas.Where(m => listKeys.Contains(m.devSn)).ToList();
            foreach (var streamName in listKeys)
            {
                //tbCaseLivingShows有记录的和没记录的加起来， app发起的直播一定有记录，相机有可能没记录，这个记录来自收app领用，现在废弃这功能
                var streamModel = models.Where(m => streamName.Equals(Common.Utils.getStreamNameFromRtmpUrl(m.RtmpUrl.Trim()))).FirstOrDefault();
                if (streamModel != null)
                {
                    streamModel.FirstFramePicUrl = getFirstFramePicUrlByCode(streamModel.FirstFramePicUrl);
                    newModels.Add(streamModel);
                }
                else
                {
                    var deviceModel = modelDevices.Where(m => m.DevSN.Trim().Equals(streamName.Trim())).FirstOrDefault();
                    tbUserIMData userDevice = null;

                    tbDeviceIMData deviceIMData = deviceIMDatas.Where(m => m.devSn.Trim().Equals(streamName.Trim())).FirstOrDefault();

                    if (deviceModel != null)
                    {
                        if (!string.IsNullOrEmpty(deviceModel.DeployDeviceUserCode))
                        {
                            userDevice = db.tbUserIMDatas.Where(m => m.uCode.Equals(deviceModel.DeployDeviceUserCode)).OrderByDescending(n => n.recSN).FirstOrDefault();
                        }
                        newModels.Add(new LivingModel()
                        {
                            PublishUserName = string.IsNullOrEmpty(deviceModel.DeployDeviceUserName) ? (deviceModel == null ? "" : deviceModel.DeployDeviceName) : deviceModel.DeployDeviceUserName,
                            PublishUserDuty = "",
                            PublishUserDepartment = "",
                            PublishUserCode = string.IsNullOrEmpty(deviceModel.DeployDeviceUserCode) ? (deviceModel == null ? "" : deviceModel.DevSN) : deviceModel.DeployDeviceUserCode,

                            PublishTime = "",
                            PublishCaseName = "",
                            LiveId = "",
                            LivingState = "",
                            PublishCaseCode = "",
                            RtmpUrl = Utils.getRrtmUrlBySreamName(streamName),
                            createdTime = null,
                            Cumulative = "0",
                            PublishUserLatitude = userDevice == null ? (deviceIMData == null ? "" : deviceIMData.latitude.ToString()) : userDevice.latitude.ToString(),//优先取个人信息
                            PublishUserLongitude = userDevice == null ? (deviceIMData == null ? "" : deviceIMData.longitude.ToString()) : userDevice.longitude.ToString(),
                            PublishUserPositionName = userDevice == null ? (deviceIMData == null ? "" : deviceIMData.positionName.ToString()) : userDevice.positionName,
                            FirstFramePicUrl = "",

                        });
                    }


                }
            }
            //所有直播过的记录
            //foreach (var model in models)
            //{
            //    if (model.createdTime != null)
            //        model.PublishTime = Common.Utils.toDatetime(model.createdTime.ToString()).ToString("yyyy-MM-dd HH:mm:ss");
            //    if (listKeys.Contains(Common.Utils.getStreamNameFromRtmpUrl(model.RtmpUrl.Trim())))
            //    {
            //        model.FirstFramePicUrl = getFirstFramePicUrlByCode(model.FirstFramePicUrl);
            //        newModels.Add(model);
            //    }
            //    //model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), model.MsgFile);

            //}

            foreach (var model in newModels)
            {
                if (model.createdTime != null)
                    model.PublishTime = Common.Utils.toDatetime(model.createdTime.ToString()).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.PublishUserHeadPortrait))
                    model.PublishUserHeadPortrait = Utils.toSiteUrl(model.PublishUserHeadPortrait);

            }
            return newModels;
        }






        public ResultEx AddCaseLivingShow(tbCaseLivingShow model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;


                var db = new DBLiveVideoEntities();

                db.tbCaseLivingShows.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        public int getMyLivingCounts(string uCode)
        {
            //var db = new DBLiveVideoEntities();
            //var livingCounts = (
            //     from c in db.tbCaseLivingShows
            //     join a in db.tbCases on c.caseCode equals a.csCode
            //     join e in db.tbCaseDeploys on c.caseCode equals e.csCode
            //     join d in db.tbUsers on e.uCode equals d.uCode  into d_join
            //     from g in d_join.DefaultIfEmpty()
            //     where g.uCode.Equals(uCode)&&c.livingState==true
            //     select 1
            //     ).Count();


            var livingCounts = 0;
            var livingModels = Common.Utils.getIncomingStream();
            if (livingModels != null)
                livingCounts = livingModels.Count();
            return livingCounts;
        }

        /// <summary>
        /// 增加直播历史视频文件
        ///  piccode不可以重复
        ///  当有的时候更新没有的时候添加
        /// </summary>
        /// <param name="rtmpUrl"></param>
        /// <param name="picCode"></param>
        /// <param name="startTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public ResultEx addLivingHistoryVideoFile(string rtmpUrl, string picCode, string startTime, string endTime, string caseCode)
        {
            var result = new ResultEx();
            try
            {


                var db = new DBLiveVideoEntities();
                //var models = db.tbCaseLivingShows.Where(m=>m.rtmpUrl.Trim().Equals(rtmpUrl.Trim())).SingleOrDefault();

                var streamName = Utils.getStreamNameFromRtmpUrl(rtmpUrl);
                //var mp4Url = Bll.Common.Utils.getNextMp4UrlFromRtmp(rtmpUrl, models.Count());
                var nowVideoCount = Utils.getVideoCountByStreamName(streamName);

                var mp4Url = Bll.Common.Utils.getMp4NameUrlFromRtmp(rtmpUrl, Common.Utils.ToInt(nowVideoCount));


                var addModel = new tbFileDetail()
                {
                    //fStartTime = Common.Utils.toDatetime(startTime),
                    //fEndTime =Common.Utils.toDatetime(endTime),
                    fName = "",
                    fSize = 0,
                    fRelativePath = mp4Url,
                    fFirstFrame = picCode,
                    fCode = RandomIdGenerator.GetBase62(14),
                };



                tbFileDetail model = null;
                if (!string.IsNullOrEmpty(picCode))
                {
                    model = db.tbFileDetails
                       .Where(m => m.fFirstFrame.Trim().Equals(picCode.Trim())).FirstOrDefault();
                }
                if (model != null)
                {
                    //更新图片
                    if (!string.IsNullOrEmpty(rtmpUrl))
                        model.fRelativePath = mp4Url;

                    if (!string.IsNullOrEmpty(endTime))
                        model.fEndTime = Common.Utils.toDatetime(endTime);

                    if (!string.IsNullOrEmpty(startTime))
                        model.fStartTime = Common.Utils.toDatetime(startTime);

                    if (db.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Message = "更新成功";
                        result.ResultCode = "1";
                        result.Entity = model;
                        return result;
                    }
                    else
                    {

                        result.IsSuccess = false;
                        result.Message = "更新失败";
                        return result;
                    }
                }

                else
                {

                    //新增图片
                    //开始和结束时间可以为空 比如说直播的时候就上传第一帧图片
                    if (!string.IsNullOrEmpty(endTime))
                        addModel.fEndTime = Common.Utils.toDatetime(endTime);

                    if (!string.IsNullOrEmpty(startTime))
                        addModel.fStartTime = Common.Utils.toDatetime(startTime);

                    if (string.IsNullOrEmpty(endTime))
                        addModel.fEndTime = null;
                    if (string.IsNullOrEmpty(startTime))
                        addModel.fStartTime = null;

                    result = AddFile(addModel);
                }










            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        public ResultEx pushNoticeCase(ApiParams mParams)
        {
            //.UserCode, mParams.CaseCode, mParams.Notice, mParams.NoticeType, userCodes
            var result = new ResultEx() { Message = "发送失败" };
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = "", MsgType = MsgTypes.Notice.ToString() };
            var db = new DBLiveVideoEntities();
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try { client.Connect(clientId); }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var sendUser = db.tbUsers.Where(m => m.uCode.Equals(mParams.UserCode)).SingleOrDefault();
            var caseModel = db.tbCases.Where(m => m.csCode.Equals(mParams.CaseCode)).SingleOrDefault();

            var livingResult = AddCaseGoing(new tbCaseGoing()
            {
                ID = new Guid(mParams.NoticeId).ToString(),
                csCode = mParams.CaseCode,
                uCode = mParams.UserCode,
                cgType = mParams.NoticeType,
                cgAbstract = mParams.Notice,
                PositionRemark = mParams.PositionRemark,
                PositionLatitude = Utils.ToDecimal(mParams.PositionLatitude),
                PositionLongitude = Utils.ToDecimal(mParams.PositionLongitude)

            });

            if (livingResult.IsSuccess)
            {


                //发送给案件警力所有成员user
                //var users = GetUsers(CaseCode);
                var modelDepartment = db.tbDepartments.Where(m => m.dCode.Equals(sendUser.dCode)).SingleOrDefault();
                var departName = modelDepartment.dName;
                var noticeModel = (tbCaseGoing)livingResult.Entity;
                string imgs = string.Empty;
                if (!string.IsNullOrEmpty(mParams.virtualId))
                {
                    var pics = db.tbFileDetails.Where(m => m.virtualId.Equals(mParams.virtualId)).OrderByDescending(c => c.recSN).ToList();

                    var pic = pics.Select(m => m.fRelativePath).ToList();
                    foreach (var url in pic)
                    {
                        var img = Utils.toSiteUrl((string.IsNullOrEmpty(url) ? "" : url));
                        imgs = imgs + img + ",";
                    }

                }



                var msgNoticeModel = new NoticeModel()
                {
                    UserPics = imgs,
                    NoticeId = noticeModel.ID.ToString(),
                    NoticeType = noticeModel.cgType,
                    Notice = noticeModel.cgAbstract,
                    CaseCode = mParams.CaseCode,
                    UserCode = sendUser.uCode,
                    CaseName = caseModel.csName,
                    PositionLatitude = noticeModel.PositionLatitude.ToString(),
                    PositionLongitude = noticeModel.PositionLongitude.ToString(),
                    PositionRemark = noticeModel.PositionRemark,
                    NoticeTime = ((DateTime)noticeModel.cgCreateTime).ToString("yyyy-MM-dd HH:mm:ss"),
                    UserName = sendUser.uName,
                };
                msgModel.MsgContent = JsonConvert.SerializeObject(msgNoticeModel);
                var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");


                List<string> userCodes = new List<string>();

                if (string.IsNullOrEmpty(mParams.UserCodes))
                {
                    var userModel = db.tbCaseDeploys.Where(m => m.csCode.Equals(mParams.CaseCode)).ToList();
                    if (userModel != null)
                        userCodes = userModel.Select(m => m.uCode).ToList();
                }

                if (!string.IsNullOrEmpty(mParams.UserCodes))
                {
                    var users = mParams.UserCodes.Split(',');
                    foreach (var user in users)
                    {
                        if (!string.IsNullOrEmpty(user))
                            userCodes.Add(user);
                    }

                }


                var userAdmins = db.tbCaseDeploys.Where(m => m.csCode.Trim().Equals(mParams.CaseCode) && m.rName.Trim().Equals("指挥官")).ToList();
                if (userAdmins != null && userAdmins.Count() > 0)
                {
                    foreach (var userTemp in userAdmins)
                    {
                        if (!string.IsNullOrEmpty(userTemp.uCode) && !userCodes.Contains(userTemp.uCode.Trim()))
                        {
                            userCodes.Add(userTemp.uCode.Trim());
                        }
                    }
                }
                foreach (var user in userCodes)
                {
                    client.Publish(userListenerTag + user.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
                }
                this.release(client);
                //client.Disconnect();
                result.Entity = msgModel.MsgContent;
                result.Message = "发布公告成功";
                result.IsSuccess = true;
                result.ResultCode = "1";
                return result;
            }
            else
            {
                result.Entity = string.Empty;
                result.Message = "发布公告失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;

            }

        }


        public ResultEx AddCaseGoing(tbCaseGoing model)
        {
            var result = new ResultEx();
            try
            {
                //model.ID = Guid.NewGuid();
                if (model.ID == null || string.IsNullOrEmpty(model.ID.ToString()))
                    model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;

                model.cgCreateTime = DateTime.Now;
                var db = new DBLiveVideoEntities();

                db.tbCaseGoings.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }


        public ResultEx AddGps(tbCaseGp model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;

                model.gpsTime = DateTime.Now;
                var db = new DBLiveVideoEntities();

                db.tbCaseGps.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(BaseManage), "Save Gps with Error", ex);
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }

        /// <summary>
        /// 历史视频列表
        /// </summary>
        /// <param name="userCode"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        public List<ChatModel> GetMyVideoHistory(string userCode, string skip, string take)
        {

            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            _take = _take <= 0 ? 10 : _take;
            var videoType = MsgTypes.Video.ToString();
            var models = (
                 from c in db.tbCaseMessages
                 join a in db.tbCases on c.csCode equals a.csCode
                 join d in db.tbUsers on c.uCode equals d.uCode
                 join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                 from g in e_join.DefaultIfEmpty()
                 where c.msgType.Equals(videoType)
                 select new ChatModel()
                 {
                     CaseName = a.csName,
                     MsgTime = c.msgTime.ToString(),
                     CreatedTime = c.msgTime,
                     SendUserId = d.ID.ToString(),
                     SendUserCode = d.uCode,
                     SendUserDuty = d.uDuty,
                     SendUserName = d.uName,
                     MsgContent = c.msgAbstract,
                     MsgType = c.msgType,
                     MsgId = c.ID.ToString(),
                     MsgFile = c.msgFile,//
                     CaseId = a.ID.ToString(),
                     CaseCode = a.csCode,
                     SendUserDepartment = g.dName,
                     //SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(d.uHeadPortrait),
                     SendUserHeadPortrait = d.uHeadPortrait,
                     SendUserDepartmentCode = g.dCode,

                     SendUserLatitude = c.uLatitude.ToString(),
                     SendPositionName = c.uPositionName,
                     SendUserLongitude = c.uLongitude.ToString(),
                     MsgFromType = c.msgFromType,
                     MsgToCode = c.msgToCode
                 }
                )
                .OrderByDescending(m => m.CreatedTime).Skip(_skip).Take(_take).ToList();
            var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
            var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
            var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
            var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();

            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();
                var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                tbFileDetail picFile = null;
                if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                    picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                if (!string.IsNullOrEmpty(model.MsgTime))
                    model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

            }
            return models;
        }

        /// <summary>
        /// 获取我的设备
        /// </summary>
        /// <param name="uCode"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <returns></returns>
        public List<DeviceModel> getMyDevice(string uCode, string skip, string take)
        {

            if (string.IsNullOrEmpty(uCode))
                return null;
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            var db = new DBLiveVideoEntities();
            var models = (
                from d in db.tbCaseDeployDevs
                join c in db.tbDevices on d.devCode equals c.devCode into c_join
                from c in c_join.DefaultIfEmpty()

                join g in db.tbCases on d.csCode equals g.csCode into g_join
                from g in g_join.DefaultIfEmpty()

                join b in db.tbCaseLivingShows on d.devCode equals b.deviceCode into b_join
                from b in b_join.DefaultIfEmpty()
                join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
                from a in a_join.DefaultIfEmpty()
                where d.uCode.Equals(uCode) && !string.IsNullOrEmpty(c.devSN)
                select new DeviceModel()
                {
                    isDel = c.isDel,
                    recSN = d.recSN,
                    devCode = c.devCode,
                    devName = c.devName,
                    devType = c.devType,
                    devBrand = c.devBrand,
                    devModel = c.devModel,
                    devPDate = c.devPDate,
                    devGPeriod = c.devGPeriod,
                    devSTime = c.devSTime,
                    uCode = c.uCode,
                    devStatus = c.devStatus,
                    devPhoto = a.fRelativePath,
                    devRemark = c.devRemark,
                    ID = c.ID,
                    devSN = c.devSN,
                    LivingState = b.livingState.ToString(),
                    LivingID = b.ID.ToString(),
                    CaseCode = d.csCode,
                    CaseName = g.csName
                }).OrderByDescending(m => m.recSN).Skip(_skip).Take(_take).ToList();




            //var livingModels = Common.Utils.getIncomingStream();
            //if (livingModels == null)
            //    return null;
            //var listKeys = new List<string>();
            //foreach (var model in livingModels)
            //{
            //    if (!string.IsNullOrEmpty(model.Name))
            //        listKeys.Add(model.Name);
            //}
            var listKeys = Common.Utils.getIncomingStreamNames();
            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.devPhoto))
                    model.devPhoto = Bll.Common.Utils.toSiteUrl(model.devPhoto);

                if (string.IsNullOrEmpty(model.LivingState))
                    model.LivingState = "false";

                if (!string.IsNullOrEmpty(model.devSN) && listKeys.Contains(model.devSN.Trim()))
                {
                    model.LivingState = true.ToString();
                }
                else
                {
                    model.LivingState = false.ToString();
                }

            }



            return models;

        }


        /// <summary>
        /// 添加设备
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddDevice(tbDevice model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;


                var db = new DBLiveVideoEntities();
                var exitModel = db.tbDevices.Where(m => m.devSN.Equals(model.devSN)).SingleOrDefault();
                if (exitModel != null && !string.IsNullOrEmpty(exitModel.devCode))
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败,该设备已经添加了";
                    //result.Exception = ex.GetBaseException().ToString();
                    //记录错误日志
                    result.ResultCode = "-1";
                    result.Exception = "";
                    return result;
                }
                var cameraCount = db.tbDevices.Count();
                model.devName = model.devName + cameraCount.ToString();
                db.tbDevices.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }
        /// <summary>
        /// 领用设备
        /// </summary>
        /// <param name="uCode"></param>
        /// <param name="devCode"></param>
        /// <returns></returns>
        public ResultEx useDevice(string uCode, string devSN, string caseCode)
        {
            var result = new ResultEx();
            try
            {

                var db = new DBLiveVideoEntities();

                var deviceModel = db.tbDevices.Where(m => m.devSN.Equals(devSN.Trim())).SingleOrDefault();
                if (deviceModel == null)
                {
                    result.IsSuccess = false;
                    result.Message = "领用失败,设备未入库";
                    result.ResultCode = "-1";
                    return result;

                }
                var model = db.tbCaseDeployDevs.Where(m => m.devCode.Equals(deviceModel.devCode) && m.csCode.Equals(caseCode.Trim())).SingleOrDefault();
                if (model == null)
                {
                    result.IsSuccess = false;
                    result.Message = "领用失败,设备未分配到案件";
                    result.ResultCode = "-1";
                    return result;

                }
                model.uCode = uCode;

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "领用成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "领用失败";
                    result.ResultCode = "-1";
                }
                var rtmpUrl = Common.Utils.GetAppSetting("LiveVideoSDK_RtmpUrl") + "live/" + devSN;
                var livingResultModel = db.tbCaseLivingShows.Where(m => m.rtmpUrl.Equals(rtmpUrl.Trim())).SingleOrDefault();
                if (livingResultModel != null)
                {
                    livingResultModel.livingState = true;
                    livingResultModel.caseCode = caseCode;
                    livingResultModel.userCode = uCode;
                    livingResultModel.sourceType = DeloyType.Camera.ToString();
                    livingResultModel.deviceCode = deviceModel.devCode;
                    livingResultModel.startTime = DateTime.Now;
                    livingResultModel.fFirstFrame = "";
                    livingResultModel.Cumulative = livingResultModel.Cumulative + 1;
                    db.SaveChanges();
                }
                else
                {
                    var livingResult = AddCaseLivingShow(new tbCaseLivingShow()
                    {
                        caseCode = caseCode,
                        userCode = uCode,
                        livingState = true,
                        endTime = null,
                        startTime = DateTime.Now,
                        rtmpUrl = rtmpUrl,
                        sourceType = DeloyType.Camera.ToString(),
                        deviceCode = deviceModel.devCode,
                        fFirstFrame = "",
                        Cumulative = 0

                    });

                    if (livingResult.IsSuccess)
                    {
                        livingResultModel = (tbCaseLivingShow)livingResult.Entity;
                    }


                }


                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "领用失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "领用失败";

            }


            return result;
        }
        /// <summary>
        /// 取消领用设备
        /// </summary>
        /// <param name="uCode"></param>
        /// <param name="devCode"></param>
        /// <returns></returns>
        public ResultEx unUseDevice(string uCode, string devCode, string caseCode)
        {
            var result = new ResultEx();
            try
            {

                var db = new DBLiveVideoEntities();
                var model = db.tbCaseDeployDevs.Where(m => m.devCode.Equals(devCode) && uCode.Equals(uCode.Trim()) && m.csCode.Equals(caseCode.Trim())).SingleOrDefault();
                if (model != null)
                    model.uCode = "";

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "领用成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "领用失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "领用失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "领用失败";

            }


            return result;
        }


        public ResultEx removeDevice(string deviceCode)
        {
            var result = new ResultEx();
            try
            {

                var db = new DBLiveVideoEntities();

                var model = db.tbDevices.Where(m => m.devCode.Equals(deviceCode)).SingleOrDefault();
                if (model != null)
                {
                    db.tbDevices.Remove(model);

                }

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "删除成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "删除失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "删除失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }



        public List<DeployModel> getCaseDeploy(string caseCode)
        {

            var person = DeloyType.Person.ToString();
            var camera = DeloyType.Camera.ToString();
            var db = new DBLiveVideoEntities();
            var modelDevices = (
                from d in db.tbCaseDeployDevs
                join c in db.tbDevices on d.devCode equals c.devCode into c_join
                from c in c_join.DefaultIfEmpty()
                join e in db.tbUsers on d.uCode equals e.uCode into e_join
                from e in e_join.DefaultIfEmpty()

                join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
                from a in a_join.DefaultIfEmpty()
                where d.csCode.Equals(caseCode)

                select new DeployModel()
                {
                    recSN = d.recSN,
                    DeployType = camera,
                    DeployDeviceCode = d.devCode,
                    DeployDeviceName = c.devName,
                    DeployDeviceUserCode = e.uCode,
                    DeployDeviceUserName = e.uName,
                    DeployDevicePhoto = a.fRelativePath,
                    Level = 2,

                }).OrderByDescending(m => m.recSN).ToList();


            var modelUsers = (
            from d in db.tbCaseDeploys
            join e in db.tbUsers on d.uCode equals e.uCode into e_join
            from e in e_join.DefaultIfEmpty()
            where d.csCode.Equals(caseCode)

            select new DeployModel()
            {
                recSN = d.recSN,
                DeployType = person,
                DeployUserPhoto = e.uHeadPortrait,
                DeployUserCode = e.uCode,
                DeployUserDuty = e.uDuty,
                DeployUserName = e.uName,
                DeployUserRName = d.rName,
                DeployDeviceUserCode = e.uCode,
                Level = 1,


            }).OrderByDescending(m => m.recSN).ToList();

            var unionModels = modelDevices.Union(modelUsers).ToList();

            var groupModels = unionModels.GroupBy(m => m.DeployDeviceUserCode).ToList();
            var models = new List<DeployModel>();
            foreach (var group in groupModels)
            {
                var groupSorts = group.OrderBy(m => m.Level).ToList();
                if (groupModels != null && groupModels.Any())
                    foreach (var model in groupSorts)
                    {
                        models.Add(model);
                    }
            }
            //models.OrderBy(m => m.recSN);

            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.DeployDevicePhoto))
                    model.DeployDevicePhoto = Common.Utils.toSiteUrl(model.DeployDevicePhoto);
                if (!string.IsNullOrEmpty(model.DeployUserPhoto))
                    model.DeployUserPhoto = Common.Utils.toSiteUrl(model.DeployUserPhoto);
            }
            return models;

        }

        /// <summary>
        /// 获取案件预案信息
        /// </summary>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        public string getCasePlan(string caseCode)
        {
            var plan = string.Empty;
            if (string.IsNullOrEmpty(caseCode))
                return plan;
            var db = new DBLiveVideoEntities();
            var model = db.tbCases.Where(m => m.csCode.Trim().Equals(caseCode.Trim())).SingleOrDefault();
            if (model != null)
                plan = model.csPlan;

            return plan;
        }


        /// <summary>
        /// 获取最新版本
        /// </summary>
        /// <param name="appType">app版本类型，是android还是ios</param>
        /// <returns></returns>
        public tbAppVersion getNewestVersion(string appType)
        {
            var result = new ResultEx();
            try
            {
                if (string.IsNullOrEmpty(appType))
                    return null;

                var db = new DBLiveVideoEntities();
                var model = db.tbAppVersions.Where(m => m.appType.TrimEnd().Equals(appType.TrimEnd())).OrderByDescending(m => m.appPublishTime).FirstOrDefault();
                return model;
            }
            catch (Exception ex)
            {
                return null;
            }
        }




        /// <summary>
        /// 错误日志
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx AddAppLog(tbAppLog model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;
                model.createdTime = DateTime.Now;

                var db = new DBLiveVideoEntities();

                db.tbAppLogs.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }

        public ResultEx updateUserGps(string caseCode, string userCode, string latitude, string longitude, string positionName)
        {
            return updateUserGps(caseCode, userCode, latitude, longitude, positionName,
                "", "", "", "", ""
                );
        }

        /// <summary>
        /// 更新个人gps记录
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx updateUserGps(string caseCode, string userCode, string latitude, string longitude, string positionName,
            string altitude, string speed, string accelerationX, string accelerationY, string accelerationZ)
        {
            var result = new ResultEx();
            try
            {
                if (string.IsNullOrEmpty(userCode))
                {
                    result.Message = "用户编号不能为空";
                    return result;
                }

                var db = new DBLiveVideoEntities();
                var model = new tbUserIMData() { uCode = userCode};
                //var model = db.tbUserIMDatas.Where(m => m.uCode.Trim().Equals(userCode)).SingleOrDefault();
                model = MyBatisHelper.QueryForObject<tbUserIMData>("select_user_gps", model);
                if (model != null)
                {
                    //MyBatisHelper.UpdateObject("update_user_gps", model);
                    
                    //更新
                    model.LastModifyTime = DateTime.Now;
                    model.latitude = Common.Utils.ToDouble(latitude.Trim());
                    model.longitude = Common.Utils.ToDouble(longitude.Trim());
                    model.csCode = caseCode;
                    model.positionName = positionName;


                    //海拔速度，加速度
                    model.altitude = Utils.ToDecimal(altitude);
                    model.speed = Utils.ToDecimal(speed);
                    model.accelerationX = Utils.ToDecimal(accelerationX);
                    model.accelerationY = Utils.ToDecimal(accelerationY);
                    //model.accelerationZ = Utils.ToDecimal(accelerationZ);

                    MyBatisHelper.UpdateObject("update_user_gps", model);
                }
                else
                {
                    model = new tbUserIMData()
                    {
                        //ID = Guid.NewGuid().ToString(),
                        recSN = Common.Utils.GetTimeStampBits(),
                        isDel = false,
                        LastModifyTime = DateTime.Now,
                        uCode = userCode,
                        csCode = caseCode,
                        latitude = Common.Utils.ToDouble(latitude.Trim()),
                        longitude = Common.Utils.ToDouble(longitude.Trim()),
                        positionName = positionName,


                        //海拔速度，加速度
                        altitude = Utils.ToDecimal(altitude),
                        speed = Utils.ToDecimal(speed),
                        accelerationX = Utils.ToDecimal(accelerationX),
                        accelerationY = Utils.ToDecimal(accelerationY)
                    };

                    MyBatisHelper.InsertObject("add_user_gps", model);

                    //插入
                    /*
                    db.tbUserIMDatas.Add(model);
                    db.SaveChanges();
                    */
                }

                var tbUser = db.tbUsers.Where(m => m.uCode.Trim().Equals(userCode) && m.status.Equals("离线")).FirstOrDefault();
                if (tbUser != null)
                {
                    tbUser.status = "在线";
                    MyBatisHelper.UpdateObject("update_user_status", tbUser);
                }
                /*
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "更新成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "更新失败";
                    result.ResultCode = "-1";
                }
                */
                result.Entity = model;

            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(BaseManage), "Update user Gps Error : ", ex);
                result.IsSuccess = false;
                result.Message = "更新失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "更新失败";

            }


            return result;
        }

        public ResultEx updateDeviceGps(string caseCode, string devSN, string latitude, string longitude, string positionName)
        {
            return updateDeviceGps(caseCode, devSN, latitude, longitude, positionName,
                "", "", "", "", ""
                );
        }
        /// <summary>
        /// 更新设备GPS
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx updateDeviceGps(string caseCode, string devSN, string latitude, string longitude, string positionName,
            string altitude, string speed, string accelerationX, string accelerationY, string accelerationZ)

        {
            var result = new ResultEx();
            try
            {
                if (string.IsNullOrEmpty(devSN))
                {
                    result.Message = "设备编号不能为空";
                    return result;
                }

                var db = new DBLiveVideoEntities();
                var model = db.tbDeviceIMDatas.Where(m => m.devSn.Trim().Equals(devSN)).SingleOrDefault();

                if (model != null)
                {
                    //更新
                    model.LastModifyTime = DateTime.Now;
                    model.latitude = Common.Utils.ToDecimal(latitude.Trim());
                    model.longitude = Common.Utils.ToDecimal(longitude.Trim());
                    model.csCode = caseCode;
                    model.positionName = positionName;

                    //海拔速度，加速度
                    model.altitude = Utils.ToDecimal(altitude);
                    model.speed = Utils.ToDecimal(speed);
                    model.accelerationX = Utils.ToDecimal(accelerationX);
                    model.accelerationY = Utils.ToDecimal(accelerationY);
                    model.accelerationZ = Utils.ToDecimal(accelerationZ);
                }
                else
                {
                    model = new tbDeviceIMData()
                    {
                        ID = Guid.NewGuid().ToString(),
                        recSN = Common.Utils.GetTimeStampBits(),
                        isDel = false,
                        LastModifyTime = DateTime.Now,
                        devSn = devSN,
                        csCode = caseCode,
                        latitude = Common.Utils.ToDecimal(latitude.Trim()),
                        longitude = Common.Utils.ToDecimal(longitude.Trim()),
                        positionName = positionName,

                        //海拔速度，加速度
                        altitude = Utils.ToDecimal(altitude),
                        speed = Utils.ToDecimal(speed),
                        accelerationX = Utils.ToDecimal(accelerationX),
                        accelerationY = Utils.ToDecimal(accelerationY),
                        accelerationZ = Utils.ToDecimal(accelerationZ)


                    };

                    //插入

                    db.tbDeviceIMDatas.Add(model);

                }






                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "更新成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "更新失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "更新失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "更新失败";

            }


            return result;
        }



        /// <summary>
        /// v1.3获取案件部署的人及设备信息、
        /// </summary>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        public List<DeployModel> getCaseUsers(string caseCode)
        {

            var person = DeloyType.Person.ToString();
            var camera = DeloyType.Camera.ToString();
            var db = new DBLiveVideoEntities();


            var modelUsers = (
            from d in db.tbCaseDeploys
            join e in db.tbUsers on d.uCode equals e.uCode into e_join
            from e in e_join.DefaultIfEmpty()
            join b in db.tbUserIMDatas on d.uCode equals b.uCode into b_join
            from b in b_join.DefaultIfEmpty()
            where d.csCode.Equals(caseCode) && !string.IsNullOrEmpty(e.uCode)


            //优先取人的定位信息
            select new DeployModel()
            {
                recSN = d.recSN,
                DeployType = "Person",
                DeployUserPhoto = e.uHeadPortrait,
                DeployUserCode = e.uCode,
                DeployUserDuty = e.uDuty,
                DeployUserName = e.uName,
                DeployUserRName = d.rName,
                DeployDeviceUserCode = e.uCode,
                DeployUserLatitude = b.latitude.ToString(),
                DeployUserLongitude = b.longitude.ToString(),
                DeployUserPositionName = b.positionName,
                Level = 1,


            }).OrderByDescending(m => m.recSN).ToList();




            //-------------------------------------------
            //设备优先取设备的定位信息、没有就取个人定位信息
            var modelDevices = (
                from d in db.tbCaseDeployDevs
                join c in db.tbDevices on d.devCode equals c.devCode
                join e in db.tbUsers on d.uCode equals e.uCode into e_join
                from e in e_join.DefaultIfEmpty()
                join b in db.tbDeviceIMDatas on c.devSN equals b.devSn into b_join
                from b in b_join.DefaultIfEmpty()
                join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
                from a in a_join.DefaultIfEmpty()
                join g in db.tbUserIMDatas on d.uCode equals g.uCode into g_join
                from g in g_join.DefaultIfEmpty()
                where d.csCode.Equals(caseCode)

                select new DeployModel()
                {
                    recSN = d.recSN,

                    DeployType = c.devType,
                    DeployDeviceCode = c.devCode,
                    DeployDeviceName = c.devName,
                    DeployDeviceUserCode = e.uCode,
                    DevSN = c.devSN,
                    DeployDeviceUserName = e.uName,
                    DeployDevicePhoto = a.fRelativePath,
                    DeployUserLatitude = string.IsNullOrEmpty(b.latitude.ToString()) ? g.latitude.ToString() : b.latitude.ToString(),
                    DeployUserLongitude = string.IsNullOrEmpty(b.longitude.ToString()) ? g.longitude.ToString() : b.longitude.ToString(),
                    DeployUserPositionName = string.IsNullOrEmpty(b.positionName) ? g.positionName : b.positionName,
                    Level = 2,

                }).OrderByDescending(m => m.recSN).ToList();



            var unionModels = modelDevices.Union(modelUsers).ToList();

            var groupModels = unionModels.GroupBy(m => m.DeployDeviceUserCode).ToList();
            var models = new List<DeployModel>();
            foreach (var group in groupModels)
            {
                var groupSorts = group.OrderBy(m => m.Level).ToList();
                if (groupModels != null && groupModels.Any())
                    foreach (var model in groupSorts)
                    {
                        models.Add(model);
                    }
            }
            //models.OrderBy(m => m.recSN);

            foreach (var model in models)
            {
                if (!string.IsNullOrEmpty(model.DeployDevicePhoto))
                    model.DeployDevicePhoto = Common.Utils.toSiteUrl(model.DeployDevicePhoto);
                if (!string.IsNullOrEmpty(model.DeployUserPhoto))
                    model.DeployUserPhoto = Common.Utils.toSiteUrl(model.DeployUserPhoto);
            }
            return models;

        }


        /// <summary>
        /// 获取案件所有部署成员
        /// </summary>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        public List<tbUser> getCaseDeployUsers(string caseCode)
        {

            var person = DeloyType.Person.ToString();
            var camera = DeloyType.Camera.ToString();
            var db = new DBLiveVideoEntities();
            var modelUsers = (
            from d in db.tbCaseDeploys
            join e in db.tbUsers on d.uCode equals e.uCode
            //from e in e_join.DefaultIfEmpty()
            where d.csCode.Equals(caseCode)
            select e).OrderByDescending(m => m.recSN).ToList();
            return modelUsers;

        }


        /// <summary>
        /// 获取个人消息，包括每个人发个自己的最新一条消息，和每个讨论组内最新的一条消息 ，及消息计数
        /// </summary>
        /// <param name="uCode">/param>
        /// <param name="csCode"></param>
        /// <returns></returns>
        public List<MsgList> GetMyChatLists(string uCode, string csCode, string msgFromType)
        {
            bool isEmptyCaseCode = string.IsNullOrWhiteSpace(csCode);
            var db = new DBLiveVideoEntities();
            var _msgFromTypePerson = MsgFromTypes.Person.ToString();
            var _msgFromTypeGroup = MsgFromTypes.Group.ToString();
            var _msgFromTypeCase = MsgFromTypes.Case.ToString();
            var models = new List<MsgList>();
            var groupModels = new List<tbDiscussionGroup>();
            var listGroups = new List<string>();
            var listUsers = new List<string>();
            if (string.IsNullOrEmpty(msgFromType) || msgFromType.Equals(_msgFromTypePerson))
            {
                //下面取个人聊天信息，分组排序取每个发给自己的最新一条消息
                var queryPerson = (from c in db.tbCaseMessages
                                   join d in db.tbUsers on c.uCode equals d.uCode into d_join
                                   from d in d_join.DefaultIfEmpty()
                                   where
                                     (isEmptyCaseCode || c.csCode.Equals(csCode)) && c.msgFromType.Equals(_msgFromTypePerson)
                                       &&

                                     //A跟B或者B跟A一组
                                     //A跟C或者C跟A
                                     //.......
                                     (c.uCode.Equals(uCode) || c.msgToCode.Equals(uCode))
                                   && !string.IsNullOrEmpty(c.uCode)
                                   && !string.IsNullOrEmpty(c.msgToCode)
                                   select new ChatModel()
                                   {
                                       CaseCode = c.csCode,
                                       MsgTime = c.msgTime.ToString(),
                                       CreatedTime = c.msgTime,
                                       SendUserId = d.ID.ToString(),
                                       SendUserCode = d.uCode,
                                       SendUserDuty = d.uDuty,
                                       SendUserName = d.uName,
                                       MsgContent = c.msgAbstract,
                                       MsgType = c.msgType,
                                       MsgId = c.ID.ToString(),
                                       MsgFile = c.msgFile,
                                       SendUserHeadPortrait = d.uHeadPortrait,
                                       MsgFromType = c.msgFromType,
                                       MsgToCode = c.msgToCode,
                                       PersonGroup = c.uCode.Trim().Equals(uCode.Trim()) ? uCode.Trim() + "," + c.msgToCode.Trim() : c.msgToCode.Trim() + "," + c.uCode.Trim()

                                   })
                    .OrderByDescending(x => x.CreatedTime)
                    .GroupBy(m => m.PersonGroup)
                    .ToList()
                    .Select(t => new
                    {
                        Model = t.OrderByDescending(x => x.CreatedTime).Take(1).FirstOrDefault(),
                        Count = t.Count()
                    }).ToList();


                foreach (var model in queryPerson)
                {

                    var users = model.Model.PersonGroup.Split(',');
                    foreach (var user in users)
                    {
                        if (!string.IsNullOrEmpty(user) && !user.Trim().Equals(uCode) && !listUsers.Contains(user.Trim()))
                            listUsers.Add(user.Trim());
                    }


                    models.Add(new MsgList()
                    {
                        CaseCode = model.Model.CaseCode,
                        CreatedTime = model.Model.CreatedTime,
                        _OrderTime = model.Model.CreatedTime,
                        MsgId = model.Model.MsgId,
                        MsgTime = model.Model.CreatedTime == null ? "" : ((DateTime)model.Model.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss"),
                        SendUserCode = model.Model.SendUserId,
                        SendUserName = model.Model.SendUserName,
                        MsgContent = model.Model.MsgContent,
                        MsgType = model.Model.MsgType,
                        MsgFile = model.Model.MsgFile,
                        SendUserHeadPortrait = model.Model.SendUserHeadPortrait,
                        MsgFromType = model.Model.MsgFromType,
                        MsgToCode = model.Model.MsgToCode,
                        MsgCount = model.Count.ToString(),
                        PersonGroup = model.Model.PersonGroup,

                    });
                }


            }

            if (string.IsNullOrEmpty(msgFromType) || msgFromType.Equals(_msgFromTypeGroup))
            {
                //下面去讨论组消息

                var myDiscussions = db.tbDiscussionGroupMenbers.Where(m => (isEmptyCaseCode || m.csCode.Equals(csCode)) && m.uCode.Equals(uCode)).ToList();
                var myDiscussionKeys = new List<string>();
                foreach (var model in myDiscussions)
                {
                    myDiscussionKeys.Add(model.discussionCode);
                }

                //下面取个人聊天信息，分组排序取每个发给自己的最新一条消息
                var queryGroup = (from c in db.tbCaseMessages
                                  join d in db.tbUsers on c.uCode equals d.uCode into d_join
                                  from d in d_join.DefaultIfEmpty()
                                  where (isEmptyCaseCode || c.csCode.Equals(csCode)) && c.msgFromType.Equals(_msgFromTypeGroup)
                                   &&
                                   //讨论组的
                                   (c.msgFromType.Equals(_msgFromTypeGroup) && myDiscussionKeys.Contains(c.msgToCode))

                                  select new ChatModel()
                                  {

                                      CaseCode = c.csCode,
                                      MsgTime = c.msgTime.ToString(),
                                      CreatedTime = c.msgTime,
                                      SendUserId = d.ID.ToString(),
                                      SendUserCode = d.uCode,
                                      SendUserDuty = d.uDuty,
                                      SendUserName = d.uName,
                                      MsgContent = c.msgAbstract,
                                      MsgType = c.msgType,
                                      MsgId = c.ID.ToString(),
                                      MsgFile = c.msgFile,
                                      SendUserHeadPortrait = d.uHeadPortrait,
                                      MsgFromType = c.msgFromType,
                                      MsgToCode = c.msgToCode,
                                      _MsgTime = c.msgTime


                                  })
                    .OrderByDescending(x => x._MsgTime)
                    .GroupBy(m => m.MsgToCode)
                    .ToList()
                    .Select(t => new
                    {
                        Model = t.OrderByDescending(x => x._MsgTime).Take(1).FirstOrDefault(),
                        Count = t.Count()
                    }).ToList();


                foreach (var model in queryGroup)
                {
                    listGroups.Add(model.Model.MsgToCode.Trim());
                    models.Add(new MsgList()
                    {
                        CaseCode = model.Model.CaseCode,
                        CreatedTime = model.Model.CreatedTime,
                        _OrderTime = model.Model.CreatedTime,
                        MsgId = model.Model.MsgId,
                        MsgTime = model.Model.CreatedTime == null ? "" : ((DateTime)model.Model.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss"),
                        SendUserCode = model.Model.SendUserId,
                        SendUserName = model.Model.SendUserName,
                        MsgContent = model.Model.MsgContent,
                        MsgType = model.Model.MsgType,
                        MsgFile = model.Model.MsgFile,
                        SendUserHeadPortrait = model.Model.SendUserHeadPortrait,
                        MsgFromType = model.Model.MsgFromType,
                        MsgToCode = model.Model.MsgToCode,
                        MsgCount = model.Count.ToString(),


                    });
                }

                //var groupModels = db.tbDiscussionGroups.Where(m => listGroups.Contains(m.discussionCode.Trim()));
                groupModels = (
                from b in db.tbDiscussionGroups
                join a in db.tbDiscussionGroupMenbers on b.discussionCode equals a.discussionCode into a_join
                from a in a_join.DefaultIfEmpty()
                where a.uCode.Trim().Equals(uCode.Trim()) && (isEmptyCaseCode || b.csCode.Equals(csCode))
                select b
               ).ToList();

            }



            //增加尚未开始聊天的讨论组
            var noTalkGroupModels = groupModels == null ? null : groupModels.Where(m => !listGroups.Contains(m.discussionCode.Trim()));
            if (noTalkGroupModels != null)
            {
                foreach (var noTalkModel in noTalkGroupModels)
                {
                    models.Add(new MsgList()
                    {
                        CaseCode = noTalkModel.csCode,
                        CreatedTime = noTalkModel.createdTime,
                        _OrderTime = noTalkModel.createdTime,
                        MsgId = "",
                        MsgTime = "",
                        SendUserCode = "",
                        SendUserName = "",
                        MsgContent = "",
                        MsgType = "",
                        MsgFile = "",
                        SendUserHeadPortrait = "",
                        MsgFromType = _msgFromTypeGroup,
                        MsgToCode = noTalkModel.discussionCode,
                        MsgCount = "0",


                    });
                }
            }


            ///赋值跟你聊天人的信息
            var toUsers = db.tbUsers.Where(m => listUsers.Contains(m.uCode.Trim()));
            if (models != null)
            {
                foreach (var msgModel in models)
                {
                    if (msgModel.MsgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
                    {
                        var toUserCode = string.Empty;
                        var users = msgModel.PersonGroup.Split(',');
                        foreach (var user in users)
                        {
                            if (!string.IsNullOrEmpty(user) && !user.Trim().Equals(uCode))
                                toUserCode = user.Trim();

                        }
                        var toUserModel = toUsers.Where(m => m.uCode.Equals(toUserCode)).FirstOrDefault();
                        if (toUserModel != null)
                        {
                            msgModel.OtherUserCode = toUserModel.uCode;
                            msgModel.OtherUserName = toUserModel.uName;
                            msgModel.OtherUserHeadPortrait = Utils.toSiteUrl(toUserModel.uHeadPortrait);

                        }

                    }
                    else if (msgModel.MsgFromType.Trim().Equals(MsgFromTypes.Group.ToString()))
                    {
                        var groupModel = groupModels.Where(m => m.discussionCode.Trim().Equals(msgModel.MsgToCode.Trim())).FirstOrDefault();
                        if (groupModel != null)
                        {
                            msgModel.MsgGroupCode = groupModel.discussionCode;
                            msgModel.MsgGroupName = groupModel.discussionName;
                            msgModel.CreatedMsgGroupUserCode = groupModel.createdUserCode;
                            msgModel.CreatedMsgGroupUserName = groupModel.createdUserName;
                        }
                    }

                    if (string.IsNullOrEmpty(msgModel.SendUserHeadPortrait))
                        msgModel.SendUserHeadPortrait = Utils.toSiteUrl(msgModel.SendUserHeadPortrait);
                }
            }



            models = models.OrderByDescending(m => m._OrderTime).ToList();

            ////下面是讨论组案件消息
            //var caseModel = db.tbCases.Where(m => m.csCode.Trim().Equals(csCode.Trim())).FirstOrDefault();
            //var caseMsgCount = db.tbCaseMessages.Where(m => m.csCode.Equals(csCode) && m.msgFromType.Equals(_msgFromTypeCase)).Count();


            //var queryCase = (from c in db.tbCaseMessages
            //                 join d in db.tbUsers on c.uCode equals d.uCode into d_join
            //                 from d in d_join.DefaultIfEmpty()
            //                 where c.csCode.Equals(csCode) && c.msgFromType.Equals(_msgFromTypeCase)
            //                 select new ChatModel()
            //                 {
            //                     CaseCode = c.csCode,
            //                     MsgTime = c.msgTime.ToString(),
            //                     CreatedTime = c.msgTime,
            //                     SendUserId = d.ID.ToString(),
            //                     SendUserCode = d.uCode,
            //                     SendUserDuty = d.uDuty,
            //                     SendUserName = d.uName,
            //                     MsgContent = c.msgAbstract,
            //                     MsgType = c.msgType,
            //                     MsgId = c.ID.ToString(),
            //                     MsgFile = c.msgFile,
            //                     SendUserHeadPortrait = d.uHeadPortrait,
            //                     MsgFromType = c.msgFromType,
            //                     MsgToCode = c.msgToCode,
            //                     PersonGroup = c.uCode.Trim().Equals(uCode.Trim()) ? uCode.Trim() + "," + c.msgToCode.Trim() : c.msgToCode.Trim() + "," + c.uCode.Trim()

            //                 })
            //    .OrderByDescending(x => x.CreatedTime).FirstOrDefault();

            //var caseMsgModel =new MsgList()
            //{


            //    MsgTime = queryCase != null ? (queryCase.CreatedTime == null ? "" : ((DateTime)queryCase.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss")) : "",
            //    SendUserCode = queryCase!=null?queryCase.SendUserCode:"",
            //    SendUserName = queryCase!=null?queryCase.SendUserName:"",
            //    MsgContent = queryCase!=null?queryCase.MsgContent:"",
            //    MsgType = queryCase!=null?queryCase.MsgType:"",
            //    MsgFile = queryCase!=null?queryCase.MsgFile:"",
            //    SendUserHeadPortrait = queryCase != null ? queryCase.SendUserHeadPortrait : "",
            //    CaseCode = caseModel.csCode,
            //    CaseName = caseModel.csName,
            //    CreatedTime = caseModel.csCreateTime,
            //    MsgFromType = _msgFromTypeCase,
            //    MsgToCode = csCode,
            //    MsgCount = caseMsgCount.ToString(),

            //};
            //models.Insert(0,caseMsgModel);

            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();

                if (_msgFromTypeGroup.Equals(model.MsgFromType))
                {
                    model.MsgId = Utils.emptyIfNulll(model.MsgFromType) + "-" + Utils.emptyIfNulll(model.MsgToCode);

                }
                else if (_msgFromTypePerson.Equals(model.MsgFromType))
                {
                    model.MsgId = Utils.emptyIfNulll(model.MsgFromType) + "-" + Utils.emptyIfNulll(model.OtherUserCode);

                }

            }
            return models;
        }



        /// <summary>
        /// 发送案件群消息
        /// </summary>
        /// <param name="FromUserId"></param>
        /// <param name="ToUserId"></param>
        /// <param name="MsgType"></param>
        /// <param name="MsgConent"></param>
        /// <param name="MsgFromType"></param>
        /// <param name="MsgToCode"></param>
        /// <returns></returns>
        public ResultEx PushMsgCaseNew(string UserCode, string CaseCode, string MsgType, string MsgConent, string msgFile, string UserLatitude, string UserLongitude, string UserPositionName, string MsgFromType, string MsgToCode)
        {

            var result = new ResultEx() { Message = "发送失败" };
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = MsgConent, MsgType = MsgType };
            var db = new DBLiveVideoEntities();
            var _groupName = string.Empty;
            /*
            var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
            string clientId = Guid.NewGuid().ToString();
            try { client.Connect(clientId); }
            catch (Exception ex)
            {
                result.Message = "即时通讯服务未开起";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            */
            MqttClient client;
            try
            {
                client = this.mqttConnect();
            }
            catch (Exception ex)
            {
                result.Message = ex.Message.ToString();
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var users = new List<tbUser>();
            if (MsgFromType.Trim().Equals(MsgFromTypes.Group.ToString()))
            {
                users = (from c in db.tbDiscussionGroupMenbers
                         join d in db.tbUsers on c.uCode equals d.uCode into d_join
                         from d in d_join.DefaultIfEmpty()
                         where c.discussionCode.Trim().Equals(MsgToCode.Trim())
                         select d
                           ).ToList();
                //发送给讨论组的所有成员
                var groupModel = db.tbDiscussionGroups.Where(m => m.discussionCode.Trim().Equals(MsgToCode.Trim())).FirstOrDefault();
                if (groupModel != null)
                    _groupName = groupModel.discussionName;
            }
            else if (MsgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
            {
                //发送给当前对话的人
                var userModel = db.tbUsers.Where(m => m.uCode.Trim().Equals(MsgToCode.Trim())).FirstOrDefault();
                if (userModel != null)
                    users.Add(userModel);


            }
            else if (MsgFromType.Trim().Equals(MsgFromTypes.Case.ToString()))
            {
                //发送给
                users.Clear();
                users = getCaseDeployUsers(CaseCode);
            }
            var sendUser = db.tbUsers.Where(m => m.uCode.Equals(UserCode)).SingleOrDefault();
            //var sendUserDepartment = db.tbDepartments.Where(m => m.dCode.TrimEnd().Equals(sendUser.dCode.TrimEnd())).SingleOrDefault();
            var addMsgModel = new tbCaseMessage()
            {
                msgFile = String.IsNullOrEmpty(msgFile) ? null : msgFile,
                msgType = MsgType,
                msgAbstract = MsgConent,
                csCode = CaseCode,
                uCode = UserCode,
                uLatitude = UserLatitude,
                uLongitude = UserLongitude,
                uPositionName = UserPositionName,
                msgFromType = MsgFromType,
                msgToCode = MsgToCode,


            };



            var msgResult = AddCaseMessage(addMsgModel);
            if (!msgResult.IsSuccess || msgResult.Entity == null)
            {
                result.Message = "发送失败数据新增失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }


            var caseMsgModel = (tbCaseMessage)msgResult.Entity;
            var caseModel = db.tbCases.Where(m => m.csCode.Equals(CaseCode)).SingleOrDefault();


            var tbFile = db.tbFileDetails.Where(m => m.fCode.Equals(caseMsgModel.msgFile)).FirstOrDefault();
            tbFileDetail picFile = null;
            if (tbFile != null && !string.IsNullOrEmpty(tbFile.fFirstFrame))
                picFile = db.tbFileDetails.Where(m => m.fCode.Equals(tbFile.fFirstFrame)).FirstOrDefault();

            ChatModel chatmodel = new ChatModel();
            try
            {
                chatmodel = new ChatModel
                {
                    SendUserLatitude = caseMsgModel.uLatitude.ToString(),
                    SendPositionName = caseMsgModel.uPositionName,
                    SendUserLongitude = caseMsgModel.uLongitude.ToString(),
                    SendUserId = sendUser.ID.ToString(),
                    SendUserCode = sendUser.uCode,
                    SendUserName = sendUser.uName,
                    SendUserDepartment = sendUser.uDepartment,
                    SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(sendUser.uHeadPortrait),
                    SendUserDuty = sendUser.uDuty,

                    MsgTime = ((DateTime)caseMsgModel.msgTime).ToString("yyyy-MM-dd HH:mm:ss"),
                    CaseCode = caseMsgModel.csCode,
                    MsgId = caseMsgModel.ID.ToString(),
                    CaseName = caseModel.csName,
                    CaseId = caseModel.ID.ToString(),
                    MsgType = caseMsgModel.msgType,
                    //MsgContent = GetMsgJson(caseMsgModel.msgType, caseMsgModel.msgAbstract, caseMsgModel.msgFile),
                    MsgContent = GetMsgJson(caseMsgModel.msgType, caseMsgModel.msgAbstract, tbFile, picFile),
                    MsgFromType = caseMsgModel.msgFromType,
                    MsgToCode = caseMsgModel.msgToCode,
                    GroupName = _groupName,
                };


            }
            catch (Exception ex)
            {
                result.Entity = JsonConvert.SerializeObject(chatmodel);
                result.Message = "casemodel:" + JsonConvert.SerializeObject(caseMsgModel) + ";send:" + JsonConvert.SerializeObject(sendUser);
                result.IsSuccess = true;
                result.ResultCode = ex.ToString() + ",strunk:" + ex.StackTrace;
                return result;
            }
            if (!string.IsNullOrEmpty(caseMsgModel.msgFromType) && caseMsgModel.msgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
            {
                var userTalks = db.tbUsers.Where(m => m.uCode.Equals(UserCode) || m.uCode.Equals(MsgToCode)).ToList();
                var personGroup = string.Empty;
                var personGroupName = string.Empty;
                foreach (var talk in userTalks)
                {
                    if (!string.IsNullOrEmpty(talk.uCode))
                        personGroup += talk.uCode.Trim() + ",";

                    if (!string.IsNullOrEmpty(talk.uName))
                        personGroupName += talk.uName.Trim() + ",";
                }

                chatmodel.PersonGroup = personGroup;
                chatmodel.PersonGroupName = personGroupName;
            }

            msgModel.MsgContent = JsonConvert.SerializeObject(chatmodel);
            var userListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Persion");
            var groupListenerTag = Common.Utils.GetAppSetting("LiveVideoSDK_Group");

            var listUsers = users.Select(m => m.uCode.Trim()).ToList();
            var userAdmins = db.tbCaseDeploys.Where(m => m.csCode.Trim().Equals(CaseCode) && m.rName.Trim().Equals("指挥官")).ToList();
            if (userAdmins != null && userAdmins.Count() > 0)
            {
                foreach (var userTemp in userAdmins)
                {
                    if (!string.IsNullOrEmpty(userTemp.uCode) && !listUsers.Contains(userTemp.uCode.Trim()))
                    {
                        listUsers.Add(userTemp.uCode.Trim());
                    }
                }
            }

            foreach (var user in listUsers)
            {

                if (user == null || string.IsNullOrEmpty(user) || user.TrimEnd().Equals(UserCode.TrimEnd()))
                    continue;
                client.Publish(userListenerTag + user.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
            }
            this.release(client);
            //client.Disconnect();
            result.Entity = JsonConvert.SerializeObject(chatmodel);
            result.Message = "发送成功";
            result.IsSuccess = true;
            result.ResultCode = "1";
            return result;
        }







        /// <summary>
        ///  新增讨论组
        /// </summary>
        /// <param name="uCode"></param>
        /// <param name="disscustionName"></param>
        /// <param name="csCode"></param>
        /// <returns></returns>
        public ResultEx AddDiscussionGroup(string uCode, string disscusionName, string csCode, string userMenbers)
        {
            var result = new ResultEx();
            try
            {

                var db = new DBLiveVideoEntities();
                var model = new tbDiscussionGroup();
                //model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = 0;
                model.createdTime = DateTime.Now;
                model.discussionCode = Common.RandomIdGenerator.GetBase62(16);
                model.discussionName = disscusionName;
                model.csCode = csCode;

                var createUserModel = db.tbUsers.Where(m => m.uCode.Equals(uCode)).FirstOrDefault();
                if (createUserModel != null)
                {
                    model.createdUserCode = createUserModel.uCode;
                    model.createdUserName = createUserModel.uName;
                }
                db.tbDiscussionGroups.Add(model);

                if (db.SaveChanges() > 0)
                {

                    //添加讨论组成功下面添加讨论组成员
                    var users = userMenbers.Split(',');
                    var listUsers = new List<string>();
                    foreach (var user in users)
                    {
                        if (!string.IsNullOrEmpty(user))
                            listUsers.Add(user);
                    }

                    //默认包含创始人
                    if (!listUsers.Contains(uCode))
                        listUsers.Add(uCode);
                    var userModels = db.tbUsers.Where(m => listUsers.Contains(m.uCode)).ToList();
                    var tbDiscussionGroupMenbers = new List<tbDiscussionGroupMenber>();

                    foreach (var user in userModels)
                    {

                        tbDiscussionGroupMenbers.Add(new tbDiscussionGroupMenber()
                        {
                            //ID = Guid.NewGuid().ToString(),
                            recSN = Common.Utils.GetTimeStampBits(),
                            isDel = false,
                            createdUserCode = createUserModel.uCode,
                            createdUserName = createUserModel.uName,
                            createdTime = DateTime.Now,
                            uCode = user.uCode,
                            csCode = csCode,
                            discussionCode = model.discussionCode,

                        });

                    }

                    db.tbDiscussionGroupMenbers.AddRange(tbDiscussionGroupMenbers);

                    if (db.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Message = "添加成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                    }
                    else
                    {

                        result.IsSuccess = false;
                        result.Message = "添加失败";
                        result.ResultCode = "-1";

                    }
                }
                result.Entity = model;
            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }
            return result;
        }



        public List<DiscussionGroupModel> GetMyDiscussionGroup(string uCode, string caseCode)
        {
            var db = new DBLiveVideoEntities();
            bool isEmptyCaseCode = string.IsNullOrWhiteSpace(caseCode);
            var models = (
             from b in db.tbDiscussionGroups
             join a in db.tbDiscussionGroupMenbers on b.discussionCode equals a.discussionCode into a_join
             from a in a_join.DefaultIfEmpty()
             where a.uCode.Trim().Equals(uCode.Trim()) && (isEmptyCaseCode || b.csCode.Equals(caseCode))
             select new
              DiscussionGroupModel()
             {
                 ID = b.ID.ToString(),
                 _createdTime = b.createdTime,
                 discussionCode = b.discussionCode,
                 discussionName = b.discussionName,
                 createdUserCode = b.discussionCode,
                 createdUserName = b.createdUserName,
                 csCode = b.csCode,

             }
            ).OrderByDescending(m => m._createdTime).ToList();

            foreach (var model in models)
            {
                if (model._createdTime != null)
                    model.createdTime = ((DateTime)model._createdTime).ToString("yyyy-MM-dd HH:mm:ss");
            }
            return models;
        }








        /// <summary>
        /// 获取个人消息，包括每个人发个自己的最新一条消息，和每个讨论组内最新的一条消息 ，及消息计数
        /// </summary>
        /// userName即使个人又是讨论组
        /// <param name="uCode">/param>
        /// <param name="csCode"></param>
        /// <returns></returns>
        public List<MsgList> SearchChatLists(string uCode, string csCode
            //搜索条件
            , string userName, string discussionName, string startTime, string endTime)
        {


            var db = new DBLiveVideoEntities();
            var caseDeploy = db.tbCaseDeploys.Where(m => m.uCode.Trim().Equals(uCode.Trim())).FirstOrDefault();
            var isAdmin = caseDeploy.rName.Equals("指挥官");



            var _msgFromTypePerson = MsgFromTypes.Person.ToString();
            var _msgFromTypeGroup = MsgFromTypes.Group.ToString();
            var models = new List<MsgList>();
            //下面取个人聊天信息，分组排序取每个发给自己的最新一条消息

            var queryPerson = (from c in db.tbCaseMessages
                               join d in db.tbUsers on c.uCode equals d.uCode into d_join
                               from d in d_join.DefaultIfEmpty()
                               where c.csCode.Equals(csCode) && c.msgFromType.Equals(_msgFromTypePerson)
                                 &&

                                 //A跟B或者B跟A一组
                                 //A跟C或者C跟A
                                 //.......
                                 //个人的聊天信息，或者管理员查看所有人的信息
                                 ((c.uCode.Equals(uCode) || c.msgToCode.Equals(uCode)) || isAdmin)
                                //搜索某个人的聊天信息
                                && (string.IsNullOrEmpty(userName) || (d.uCode.Trim().Contains(userName) || d.uName.Trim().Contains(userName)))
                                    && !string.IsNullOrEmpty(c.uCode)
                             && !string.IsNullOrEmpty(c.msgToCode)
                               select new ChatModel()
                               {
                                   CaseCode = c.csCode,
                                   MsgTime = c.msgTime.ToString(),
                                   CreatedTime = c.msgTime,
                                   SendUserId = d.ID.ToString(),
                                   SendUserCode = d.uCode,
                                   SendUserDuty = d.uDuty,
                                   SendUserName = d.uName,
                                   MsgContent = c.msgAbstract,
                                   MsgType = c.msgType,
                                   MsgId = c.ID.ToString(),
                                   MsgFile = c.msgFile,
                                   SendUserHeadPortrait = d.uHeadPortrait,
                                   MsgFromType = c.msgFromType,
                                   MsgToCode = c.msgToCode,
                                   PersonGroup = c.uCode.Trim().Equals(uCode.Trim()) ? uCode.Trim() + "," + c.msgToCode.Trim() : c.msgToCode.Trim() + "," + c.uCode.Trim()

                               })
                .OrderByDescending(x => x.CreatedTime)
                .GroupBy(m => m.PersonGroup)
                .Select(t => new
                {
                    Model = t.OrderByDescending(x => x.CreatedTime).Take(1).FirstOrDefault(),
                    Count = t.Count()
                }).ToList();

            var listUsers = new List<string>();
            foreach (var model in queryPerson)
            {

                var users = model.Model.PersonGroup.Split(',');
                foreach (var user in users)
                {
                    if (!string.IsNullOrEmpty(user) && !user.Trim().Equals(uCode) && !listUsers.Contains(user.Trim()))
                        listUsers.Add(user.Trim());
                }


                models.Add(new MsgList()
                {
                    CaseCode = model.Model.CaseCode,
                    CreatedTime = model.Model.CreatedTime,
                    _OrderTime = model.Model.CreatedTime,
                    MsgId = model.Model.MsgId,
                    MsgTime = model.Model.CreatedTime == null ? "" : ((DateTime)model.Model.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss"),
                    SendUserCode = model.Model.SendUserId,
                    SendUserName = model.Model.SendUserName,
                    MsgContent = model.Model.MsgContent,
                    MsgType = model.Model.MsgType,
                    MsgFile = model.Model.MsgFile,
                    SendUserHeadPortrait = model.Model.SendUserHeadPortrait,
                    MsgFromType = model.Model.MsgFromType,
                    MsgToCode = model.Model.MsgToCode,
                    MsgCount = model.Count.ToString(),
                    PersonGroup = model.Model.PersonGroup,

                });
            }
            //下面去讨论组消息


            var myDiscussionKeys = new List<string>();

            //默认个人参与的讨论组

            var myDiscussions1 =
                (from a in db.tbDiscussionGroupMenbers
                 join b in db.tbUsers on a.uCode equals b.uCode into b_join
                 from d in b_join.DefaultIfEmpty()
                 where a.csCode.Equals(csCode) && (string.IsNullOrEmpty(userName) || (d.uCode.Contains(userName) || d.uName.Contains(userName)))
                 select a
                   ).ToList();

            //db.tbDiscussionGroupMenbers.Where(m => m.csCode.Equals(csCode) && m.uCode.Equals(uCode)).ToList();
            foreach (var model in myDiscussions1)
            {
                if (!myDiscussionKeys.Contains(model.discussionCode.Trim()))
                    myDiscussionKeys.Add(model.discussionCode.Trim());
            }


            //搜索具体某个讨论组的
            var myDiscussions2 = db.tbDiscussionGroups.Where(m => m.discussionName.Contains(userName.Trim())).ToList();
            foreach (var model in myDiscussions2)
            {
                if (!myDiscussionKeys.Contains(model.discussionCode.Trim()))
                    myDiscussionKeys.Add(model.discussionCode.Trim());
            }





            //下面取个人聊天信息，分组排序取每个发给自己的最新一条消息
            var queryGroup = (from c in db.tbCaseMessages
                              join d in db.tbUsers on c.uCode equals d.uCode into d_join
                              from d in d_join.DefaultIfEmpty()
                              where c.csCode.Equals(csCode) && c.msgFromType.Equals(_msgFromTypeGroup)
                               &&
                               //讨论组的
                               c.msgFromType.Equals(_msgFromTypeGroup)


                                //&& myDiscussionKeys.Contains(c.msgToCode))
                                && ((myDiscussionKeys.Contains(c.msgToCode)))


                              select new ChatModel()
                              {

                                  CaseCode = c.csCode,
                                  MsgTime = c.msgTime.ToString(),
                                  CreatedTime = c.msgTime,
                                  SendUserId = d.ID.ToString(),
                                  SendUserCode = d.uCode,
                                  SendUserDuty = d.uDuty,
                                  SendUserName = d.uName,
                                  MsgContent = c.msgAbstract,
                                  MsgType = c.msgType,
                                  MsgId = c.ID.ToString(),
                                  MsgFile = c.msgFile,
                                  SendUserHeadPortrait = d.uHeadPortrait,
                                  MsgFromType = c.msgFromType,
                                  MsgToCode = c.msgToCode

                              })
                .OrderByDescending(x => x.CreatedTime)
                .GroupBy(m => m.MsgToCode)
                .Select(t => new
                {
                    Model = t.OrderByDescending(x => x.CreatedTime).Take(1).FirstOrDefault(),
                    Count = t.Count()
                }).ToList();

            var listGroups = new List<string>();
            foreach (var model in queryGroup)
            {
                listGroups.Add(model.Model.MsgToCode.Trim());
                models.Add(new MsgList()
                {
                    CaseCode = model.Model.CaseCode,
                    CreatedTime = model.Model.CreatedTime,
                    _OrderTime = model.Model.CreatedTime,
                    MsgId = model.Model.MsgId,
                    MsgTime = model.Model.CreatedTime == null ? "" : ((DateTime)model.Model.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss"),
                    SendUserCode = model.Model.SendUserId,
                    SendUserName = model.Model.SendUserName,
                    MsgContent = model.Model.MsgContent,
                    MsgType = model.Model.MsgType,
                    MsgFile = model.Model.MsgFile,
                    SendUserHeadPortrait = model.Model.SendUserHeadPortrait,
                    MsgFromType = model.Model.MsgFromType,
                    MsgToCode = model.Model.MsgToCode,
                    MsgCount = model.Count.ToString(),


                });
            }

            //var groupModels = db.tbDiscussionGroups.Where(m => listGroups.Contains(m.discussionCode.Trim()));
            var groupModels = (
             from b in db.tbDiscussionGroups
             join a in db.tbDiscussionGroupMenbers on b.discussionCode equals a.discussionCode into a_join
             from a in a_join.DefaultIfEmpty()
             where a.uCode.Trim().Equals(uCode.Trim()) && b.csCode.Equals(csCode)
                                     && ((myDiscussionKeys.Contains(a.discussionCode)))
             select b
            ).ToList();


            //增加尚未开始聊天的讨论组
            var noTalkGroupModels = groupModels.Where(m => !listGroups.Contains(m.discussionCode.Trim()));
            foreach (var noTalkModel in noTalkGroupModels)
            {
                models.Add(new MsgList()
                {
                    CaseCode = noTalkModel.csCode,
                    CreatedTime = noTalkModel.createdTime,
                    _OrderTime = noTalkModel.createdTime,
                    MsgId = "",
                    MsgTime = "",
                    SendUserCode = "",
                    SendUserName = "",
                    MsgContent = "",
                    MsgType = "",
                    MsgFile = "",
                    SendUserHeadPortrait = "",
                    MsgFromType = _msgFromTypeGroup,
                    MsgToCode = noTalkModel.discussionCode,
                    MsgCount = "0",


                });
            }

            ///赋值跟你聊天人的信息
            var toUsers = db.tbUsers.Where(m => listUsers.Contains(m.uCode.Trim()));

            foreach (var msgModel in models)
            {
                if (msgModel.MsgFromType.Trim().Equals(MsgFromTypes.Person.ToString()))
                {
                    var toUserCode = string.Empty;
                    var users = msgModel.PersonGroup.Split(',');
                    foreach (var user in users)
                    {
                        if (!string.IsNullOrEmpty(user) && !user.Trim().Equals(uCode))
                            toUserCode = user.Trim();

                    }
                    var toUserModel = toUsers.Where(m => m.uCode.Equals(toUserCode)).FirstOrDefault();
                    msgModel.OtherUserCode = toUserModel.uCode;
                    msgModel.OtherUserName = toUserModel.uName;
                    msgModel.OtherUserHeadPortrait = Utils.toSiteUrl(toUserModel.uHeadPortrait);

                }
                else if (msgModel.MsgFromType.Trim().Equals(MsgFromTypes.Group.ToString()))
                {
                    var groupModel = groupModels.Where(m => m.discussionCode.Trim().Equals(msgModel.MsgToCode.Trim())).FirstOrDefault();
                    if (groupModel != null)
                    {
                        msgModel.MsgGroupCode = groupModel.discussionCode;
                        msgModel.MsgGroupName = groupModel.discussionName;
                        msgModel.CreatedMsgGroupUserCode = groupModel.createdUserCode;
                        msgModel.CreatedMsgGroupUserName = groupModel.createdUserName;
                    }
                }

                if (string.IsNullOrEmpty(msgModel.SendUserHeadPortrait))
                    msgModel.SendUserHeadPortrait = Utils.toSiteUrl(msgModel.SendUserHeadPortrait);
            }
            if (string.IsNullOrEmpty(userName))
            {
                var _msgFromTypeCase = MsgFromTypes.Case.ToString();
                //下面是讨论组案件消息
                var caseModel = db.tbCases.Where(m => m.csCode.Trim().Equals(csCode.Trim())).FirstOrDefault();
                var caseMsgCount = db.tbCaseMessages.Where(m => m.csCode.Equals(csCode) && m.msgFromType.Equals(_msgFromTypeCase)).Count();



                var queryCase = (from c in db.tbCaseMessages
                                 join d in db.tbUsers on c.uCode equals d.uCode into d_join
                                 from d in d_join.DefaultIfEmpty()
                                 where c.csCode.Equals(csCode) && c.msgFromType.Equals(_msgFromTypeCase)
                                 select new ChatModel()
                                 {
                                     CaseCode = c.csCode,
                                     MsgTime = c.msgTime.ToString(),
                                     CreatedTime = c.msgTime,
                                     SendUserId = d.ID.ToString(),
                                     SendUserCode = d.uCode,
                                     SendUserDuty = d.uDuty,
                                     SendUserName = d.uName,
                                     MsgContent = c.msgAbstract,
                                     MsgType = c.msgType,
                                     MsgId = c.ID.ToString(),
                                     MsgFile = c.msgFile,
                                     SendUserHeadPortrait = d.uHeadPortrait,
                                     MsgFromType = c.msgFromType,
                                     MsgToCode = c.msgToCode,
                                     PersonGroup = c.uCode.Trim().Equals(uCode.Trim()) ? uCode.Trim() + "," + c.msgToCode.Trim() : c.msgToCode.Trim() + "," + c.uCode.Trim()

                                 })
                  .OrderByDescending(x => x.CreatedTime).FirstOrDefault();
                var caseMsgModel = new MsgList()
                {



                    SendUserCode = queryCase != null ? queryCase.SendUserCode : "",
                    SendUserName = queryCase != null ? queryCase.SendUserName : "",
                    MsgContent = queryCase != null ? queryCase.MsgContent : "",
                    MsgType = queryCase != null ? queryCase.MsgType : "",
                    MsgFile = queryCase != null ? queryCase.MsgFile : "",
                    SendUserHeadPortrait = queryCase != null ? queryCase.SendUserHeadPortrait : "",
                    CaseCode = caseModel.csCode,
                    CaseName = caseModel.csName,
                    CreatedTime = caseModel.csCreateTime,
                    MsgFromType = _msgFromTypeCase,
                    MsgToCode = csCode,
                    MsgCount = caseMsgCount.ToString(),

                };
                models.Insert(0, caseMsgModel);

            }
            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();

            }

            return models.OrderByDescending(m => m._OrderTime).ToList();
        }

        public List<GpsModel> SearchGpsList(string userName, string startTime)
        {
            return SearchGpsList(userName, startTime, null);
        }
        /// <summary>
        /// 历史轨迹，每隔几分钟去一条，且是分钟里面第一条，间隔自定，如分钟带3，或者5,10等 必带条件，用户编号，开始时间
        /// </summary>只能搜索一个人
        /// <param name="userCode"></param>
        /// <param name="userName"></param>
        /// <param name="discussionName"></param>
        /// <param name="startTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public List<GpsModel> SearchGpsList(string userName, string startTime, string caseCode)
        {
            var db = new DBLiveVideoEntities();


            //from c in db.tbCaseMessages
            //   join a in db.tbCases on c.csCode equals a.csCode
            //   join d in db.tbUsers on c.uCode equals d.uCode
            //   join e in db.tbDepartments on d.dCode equals e.dCode into e_join
            //   from g in e_join.DefaultIfEmpty()

            //案件里面所有人
            var listUsers = new List<string>();
            var listUserNames = new List<string>();
            var caseUsers = getCaseDeployUsers(caseCode);
            foreach (var user in caseUsers)
            {
                if (user != null && !string.IsNullOrEmpty(user.uCode) && !listUsers.Contains(user.uCode.Trim()))
                {
                    listUsers.Add(user.uCode.Trim());
                    listUserNames.Add(user.uName.Trim());
                }

            }

            //与userName相关的讨论组的所有成员去除了重复的
            //var discussionUsers =
            // (from a in db.tbUsers
            //  join b in db.tbDiscussionGroupMenbers on a.uCode equals b.uCode into b_join
            //  from d in b_join.DefaultIfEmpty()
            //  join c in db.tbDiscussionGroups on d.discussionCode equals c.discussionCode
            //  where (string.IsNullOrEmpty(userName) || (c.discussionName.Contains(userName) || c.discussionCode.Contains(userName)))
            //  &&c.csCode.Equals(caseCode.Trim())
            //  select a
            //    ).GroupBy(m=>m.uCode)
            //    .Select(t => new
            //    {
            //        user = t.FirstOrDefault()
            //    })
            //    .ToList();






            var vtime = Common.Utils.toDatetime(startTime);
            if (string.IsNullOrEmpty(startTime))
                vtime = DateTime.Now;
            var models = new List<GpsModel>();
            //var baseModels = (from a in db.tbCaseGps
            //                  join b in db.tbUsers on a.uCode equals b.uCode
            //                  join c in db.tbCases on a.csCode equals c.csCode into c_join
            //                  from c in c_join.DefaultIfEmpty()
            //                  where
            //                  //c.csCode.Equals(caseCode)&&
            //                  a.gpsTime.Year == vtime.Year && a.gpsTime.Day == vtime.Day && a.gpsTime.Month == vtime.Month &&
            //                  a.gpsTime.Minute % 5 == 0 &&
            //                  //搜索具体个人人的历史纪录
            //                  (string.IsNullOrEmpty(userName) || (b.uName.Trim().Contains(userName.Trim()) || b.uCode.Trim().Contains(userName.Trim())))
            //                   //并且是这个案件的
            //                  && (string.IsNullOrEmpty(caseCode) || listUsers.Count() <= 0 ||
            //                        listUsers.Contains(b.uCode.Trim())
            //                    )
            //                  select new GpsModel()
            //                  {
            //                      CaseCode = c.csCode,
            //                      CaseName = c.csName,
            //                      UserName = b.uName,
            //                      UserHeadPortrait = b.uHeadPortrait,
            //                      UserCode = a.uCode,
            //                      DevCode = a.devCode,
            //                      //每隔5分钟的gps定位时间的第一条加上最新一条，如3，6，9，12
            //                      DayTime = a.gpsTime.Year.ToString() + "," + a.gpsTime.Month.ToString() + "," + a.gpsTime.Day.ToString() + "," + a.gpsTime.Minute,

            //                      MinuteTime = a.gpsTime.Minute,
            //                      Interval = 5,
            //                      _CreatedTime = a.gpsTime,
            //                      GpsTime = a.gpsTime.ToString(),
            //                      GpsTargetType = a.gpsTargetType,
            //                      Latitdue = a.gpsLatitude.ToString(),
            //                      Longitude = a.gpsLongitude.ToString(),

            //                      ID = a.ID.ToString()
            //                  })

            //          .OrderByDescending(x => x._CreatedTime)
            //         .GroupBy(m => new { m.UserCode, m.MinuteTime })
            //         .Select(t => new
            //         {

            //             Model = t.OrderByDescending(n => n._CreatedTime).Take(1).FirstOrDefault()


            //         })
            //         .ToList();


            //foreach (var bmdoel in baseModels)
            //{


            //    bmdoel.Model.UserHeadPortrait = Utils.toSiteUrl(bmdoel.Model.UserHeadPortrait);
            //    bmdoel.Model.GpsTime = ((DateTime)bmdoel.Model._CreatedTime).ToString("yyyy-MM-dd HH:mm:ss");
            //    models.Add(bmdoel.Model);

            //}

            return models;
        }

        /// <summary>
        /// userName 即使用户也是讨论组
        /// </summary>
        /// <param name="userCode"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <param name="userName"></param>
        /// <param name="discussionName"></param>
        /// <param name="startTime"></param>
        /// <param name="endTime"></param>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        //public List<ChatModel> searchVideoHistory(string userCode, string skip, string take
        //      , string userName, string discussionName, string startTime, string endTime,string caseCode
        //    )
        //{

        //    var db = new DBLiveVideoEntities();
        //    var _skip = Common.Utils.ToInt(skip);
        //    var _take = Common.Utils.ToInt(take);
        //    _take = _take <= 0 ? 10 : _take;
        //    var videoType = MsgTypes.Video.ToString();
        //    var models = (
        //         from c in db.tbCaseMessages
        //         join a in db.tbCases on c.csCode equals a.csCode
        //         join d in db.tbUsers on c.uCode equals d.uCode
        //         join i in db.tbDiscussionGroups on c.msgToCode equals i.discussionCode into i_join
        //         from i in i_join.DefaultIfEmpty()
        //         join e in db.tbDepartments on d.dCode equals e.dCode into e_join
        //         from g in e_join.DefaultIfEmpty()
        //         where c.msgType.Equals(videoType)
        //         && (string.IsNullOrEmpty(caseCode)||c.csCode.Equals(caseCode))
        //         //搜索某个人的直播视频
        //         &&
        //         ((string.IsNullOrEmpty(userName) || (d.uCode.Trim().Contains(userName.Trim()) || d.uName.Trim().Contains(userName.Trim())))

        //         || (string.IsNullOrEmpty(userName) || i.discussionCode.Trim().Contains(userName.Trim()) || i.discussionName.Trim().Contains(userName.Trim()))
        //          )

        //         select new ChatModel()
        //         {
        //             CaseName = a.csName,
        //             MsgTime = c.msgTime.ToString(),
        //             CreatedTime = c.msgTime,
        //             SendUserId = d.ID.ToString(),
        //             SendUserCode = d.uCode,
        //             SendUserDuty = d.uDuty,
        //             SendUserName = d.uName,
        //             MsgContent = c.msgAbstract,
        //             MsgType = c.msgType,
        //             MsgId = c.ID.ToString(),
        //             MsgFile = c.msgFile,//
        //             CaseId = a.ID.ToString(),
        //             CaseCode = a.csCode,
        //             SendUserDepartment = g.dName,
        //             //SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(d.uHeadPortrait),
        //             SendUserHeadPortrait = d.uHeadPortrait,
        //             SendUserDepartmentCode = g.dCode,

        //             SendUserLatitude = c.uLatitude.ToString(),
        //             SendPositionName = c.uPositionName,
        //             SendUserLongitude = c.uLongitude.ToString(),
        //             MsgFromType = c.msgFromType,
        //             MsgToCode = c.msgToCode
        //         }
        //        )
        //        .OrderByDescending(m => m.CreatedTime).Skip(_skip).Take(_take).ToList();

        //    foreach (var model in models)
        //    {
        //        model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), model.MsgFile);
        //        model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
        //        model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
        //        if (!string.IsNullOrEmpty(model.MsgTime))
        //            model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

        //    }
        //    return models;
        //}


        /// <summary>
        /// userName 即使用户也是讨论组
        /// </summary>
        /// <param name="userCode"></param>
        /// <param name="skip"></param>
        /// <param name="take"></param>
        /// <param name="userName"></param>
        /// <param name="discussionName"></param>
        /// <param name="startTime"></param>
        /// <param name="endTime"></param>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        public List<ChatModel> searchVideoHistory(string userCode, string skip, string take
              , string userName, string discussionName, string startTime, string endTime, string caseCode
            )
        {

            var db = new DBLiveVideoEntities();
            var _skip = Common.Utils.ToInt(skip);
            var _take = Common.Utils.ToInt(take);
            _take = _take <= 0 ? 10 : _take;
            var videoType = MsgTypes.Video.ToString();

            var videoModels = Utils.getVideoNames();
            //var caseDeploy = getCaseDeploy(caseCode);

            var camera = DeloyType.Camera.ToString();
            ///案件里的所有设备

            var modelDevices = (
             from d in db.tbCaseDeployDevs
             join c in db.tbDevices on d.devCode equals c.devCode into c_join
             from c in c_join.DefaultIfEmpty()
             join e in db.tbUsers on d.uCode equals e.uCode into e_join
             from e in e_join.DefaultIfEmpty()
             join b in db.tbDeviceIMDatas on c.devSN equals b.devSn into b_join
             from b in b_join.DefaultIfEmpty()
             join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
             from a in a_join.DefaultIfEmpty()
             where d.csCode.Equals(caseCode)

             select new DeployModel()
             {
                 recSN = d.recSN,

                 DeployType = c.devType,
                 DeployDeviceCode = c.devCode,
                 DeployDeviceName = c.devName,
                 DeployDeviceUserCode = e.uCode,
                 DevSN = c.devSN,
                 DeployDeviceUserName = e.uName,
                 DeployDevicePhoto = a.fRelativePath,
                 DeployUserLatitude = b.latitude.ToString(),
                 DeployUserLongitude = b.longitude.ToString(),
                 DeployUserPositionName = b.positionName,
                 Level = 2,

             }).OrderByDescending(m => m.recSN).ToList();





            var deviceKeys = new List<string>();
            modelDevices.Where(d => (string.IsNullOrEmpty(userName) || ((d.DeployDeviceUserName != null) && d.DeployDeviceUserName.Contains(userName.Trim()))))
            .Select(m => m.DevSN).ToList().ForEach(s =>
            {
                deviceKeys.Add(s);

            });
            var models = (
               from c in db.tbCaseMessages
               join a in db.tbCases on c.csCode equals a.csCode
               join d in db.tbUsers on c.uCode equals d.uCode
               join i in db.tbDiscussionGroups on c.msgToCode equals i.discussionCode into i_join
               from i in i_join.DefaultIfEmpty()
               join e in db.tbDepartments on d.dCode equals e.dCode into e_join
               from g in e_join.DefaultIfEmpty()
               where c.msgType.Equals(videoType)
               && (string.IsNullOrEmpty(caseCode) || c.csCode.Equals(caseCode))
               //搜索某个人的直播视频
               &&
               ((string.IsNullOrEmpty(userName) || (d.uCode.Trim().Contains(userName.Trim()) || d.uName.Trim().Contains(userName.Trim())))

               || (string.IsNullOrEmpty(userName) || i.discussionCode.Trim().Contains(userName.Trim()) || i.discussionName.Trim().Contains(userName.Trim()))
                )

               select new ChatModel()
               {
                   CaseName = a.csName,
                   MsgTime = c.msgTime.ToString(),
                   CreatedTime = c.msgTime,
                   SendUserId = d.ID.ToString(),
                   SendUserCode = d.uCode,
                   SendUserDuty = d.uDuty,
                   SendUserName = d.uName,
                   MsgContent = c.msgAbstract,
                   MsgType = c.msgType,
                   MsgId = c.ID.ToString(),
                   MsgFile = c.msgFile,//
                     CaseId = a.ID.ToString(),
                   CaseCode = a.csCode,
                   SendUserDepartment = g.dName,
                     //SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(d.uHeadPortrait),
                     SendUserHeadPortrait = d.uHeadPortrait,
                   SendUserDepartmentCode = g.dCode,

                   SendUserLatitude = c.uLatitude.ToString(),
                   SendPositionName = c.uPositionName,
                   SendUserLongitude = c.uLongitude.ToString(),
                   MsgFromType = c.msgFromType,
                   MsgToCode = c.msgToCode
               }
              )
              .OrderByDescending(m => m.CreatedTime).Skip(_skip).Take(_take).ToList();

            var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
            var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
            var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
            var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();

            foreach (var model in models)
            {
                var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                tbFileDetail picFile = null;
                if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                    picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                if (!string.IsNullOrEmpty(model.MsgTime))
                    model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

            }

            videoModels.Where(m => deviceKeys.Contains(m.StreamName)).ToList().ForEach(s =>
            {

                var deployModel = modelDevices.Where(m => m.DevSN != null && m.DevSN.Equals(s.StreamName.Trim())).FirstOrDefault();
                models.Insert(0, new ChatModel()
                {
                    MsgTime = s.VideoEndTime,
                    CreatedTime = Utils.toDatetime(s.VideoEndTime),
                    SendUserCode = deployModel == null ? "" : deployModel.DeployDeviceUserCode,
                    SendUserDuty = deployModel == null ? "" : deployModel.DeployUserDuty,
                    SendUserName = deployModel == null ? "" : deployModel.DeployDeviceUserName,
                    MsgContent = JsonConvert.SerializeObject(s),
                    MsgType = MsgTypes.Video.ToString(),
                    CaseCode = deployModel == null ? "" : deployModel.CaseCode,

                    SendUserLatitude = deployModel == null ? "" : deployModel.DeployUserLatitude,
                    SendPositionName = deployModel == null ? "设备暂无定位" : (string.IsNullOrEmpty(deployModel.DeployUserPositionName) ? "设备暂无定位" : deployModel.DeployUserPositionName),
                    SendUserLongitude = deployModel == null ? "" : deployModel.DeployUserLongitude,

                });
            });
            return models.OrderByDescending(m => m.CreatedTime).ToList();
        }


        //获取讨论组成员
        public List<UserModel> getDiscussionMenbers(string DiscussionCode)
        {

            var db = new DBLiveVideoEntities();
            var videoType = MsgTypes.Video.ToString();
            var models = (
                from c in db.tbDiscussionGroupMenbers
                join d in db.tbUsers on c.uCode equals d.uCode
                join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                from g in e_join.DefaultIfEmpty()
                where c.discussionCode.Equals(DiscussionCode)
                select new UserModel()
                {
                    UserId = d.ID.ToString(),
                    UserCode = d.uCode,
                    UserName = d.uName,
                    LoginName = d.uCode,
                    UserPhone = d.uTel,
                    UserPCNum = d.pcNum,
                    HeadPortrait = d.uHeadPortrait,
                    DepartmentCode = g.dCode,
                    UserShortNum = d.uShortNum,
                    RoleName = d.rName,
                    //UserPwd=model.UserPwd,
                    //ApiToken = token.apiToken,
                    UserDuty = d.uDuty,
                    Department = g.dName,
                    _CreatedTime = c.createdTime
                }
               ).OrderByDescending(m => m._CreatedTime).ToList();

            foreach (var user in models)
            {
                user.HeadPortrait = Utils.toSiteUrl(user.HeadPortrait);

            }

            return models;
        }

        /// <summary>
        /// 添加一组讨论组成员，没有的添加，有的不动
        /// </summary>
        /// <param name="userCodes"></param>
        /// <param name="discussionCode"></param>
        /// <param name="createdUserCode"></param>
        /// <param name="caseCode"></param>
        /// <returns></returns>
        public ResultEx AddDiscussionMenbers(string userCodes, string discussionCode, string createdUserCode, string caseCode)
        {
            var result = new ResultEx();
            try
            {


                var userKeys = userCodes.Split(',');
                var listKeys = new List<string>();
                for (int i = 0; i < userKeys.Length; i++)
                {
                    if (!string.IsNullOrEmpty(userKeys[i]) && !listKeys.Contains(userKeys[i]))
                    {
                        listKeys.Add(userKeys[i].Trim());
                    }
                }


                var db = new DBLiveVideoEntities();

                var createdUserModel = db.tbUsers.Where(m => m.uCode.Equals(createdUserCode)).FirstOrDefault();
                var discussionMenrbs = db.tbDiscussionGroupMenbers.Where(m => m.discussionCode.Equals(discussionCode)).ToList();

                if (discussionMenrbs != null)
                {
                    foreach (var model in discussionMenrbs)
                    {
                        if (listKeys.Contains(model.uCode.Trim()))
                        {
                            listKeys.Remove(model.uCode.Trim());
                        }
                    }
                }

                var tbDiscussionGroupMenbers = new List<tbDiscussionGroupMenber>();

                foreach (var user in listKeys)
                {

                    tbDiscussionGroupMenbers.Add(new tbDiscussionGroupMenber()
                    {
                        //ID = Guid.NewGuid().ToString(),
                        recSN = Common.Utils.GetTimeStampBits(),
                        isDel = false,
                        createdUserCode = createdUserModel.uCode,
                        createdUserName = createdUserModel.uName,
                        createdTime = DateTime.Now,
                        uCode = user,
                        csCode = caseCode,
                        discussionCode = discussionCode,

                    });

                }

                db.tbDiscussionGroupMenbers.AddRange(tbDiscussionGroupMenbers);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                    result.Entity = null;
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = null;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }



        /// <summary>
        /// 删除成员
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ResultEx deleteDiscussionMenber(string userCode, string discussionCode)
        {
            var result = new ResultEx();
            try
            {
                var userKeys = userCode.Split(',');
                var listKeys = new List<string>();
                for (int i = 0; i < userKeys.Length; i++)
                {
                    if (!string.IsNullOrEmpty(userKeys[i]) && !listKeys.Contains(userKeys[i]))
                    {
                        listKeys.Add(userKeys[i].Trim());
                    }
                }

                var db = new DBLiveVideoEntities();


                var discussionMenrb = db.tbDiscussionGroupMenbers.Where(m => listKeys.Contains(m.uCode) && m.discussionCode.Trim().Equals(discussionCode.Trim())).ToList();

                if (discussionMenrb != null && discussionMenrb.Count() > 0)
                    db.tbDiscussionGroupMenbers.RemoveRange(discussionMenrb);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "删除成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                    result.Entity = null;
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "删除失败";
                    result.ResultCode = "-1";
                }

                result.Entity = null;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "删除成功";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "删除失败";

            }


            return result;
        }


        /// v.1.3版本新增方法
        #region v.1.3版本新增方法



        //获取个人任务
        public List<MissionModel> getCasePersonMissions(string caseCode, string userCode)
        {

            var db = new DBLiveVideoEntities();
            var videoType = MsgTypes.Video.ToString();
            var models = (
                from c in db.tbCaseMissions
                join d in db.tbCaseMissionDistributions on c.ID.ToString() equals d.missionId
                join e in db.tbUsers on d.userCode equals e.uCode into e_join
                from g in e_join
                where c.caseCode.Equals(caseCode) && d.userCode.Equals(userCode)
                select new MissionModel()
                {
                    messionId = c.ID.ToString(),
                    caseCode = c.caseCode,
                    missionStatus = c.missionStatus,
                    missionLimiTime = c.missionLimiTime,
                    toPositionName = c.toPositionName,
                    toPositionLatitude = c.toPositionLatitude,
                    toPositionLongitude = c.toPositionLongitude,
                    missionRemark = c.missionRemark,
                    routeDistance = c.routeDistance,
                    createdUserCode = c.createdUserCode,
                    createdPositionName = c.createdPositionName,
                    createdPositionLatitude = c.createdPositionLatitude,
                    createdPositionLongitude = c.createdPositionLongitude,
                    finishTime = c.finishTime,
                    missionType = c.missionType,

                    messionPersonId = d.ID.ToString(),
                    messioPersonCreatedTime = d.createdTime,
                    missionPersonStatus = d.missionPersonStatus,
                    persionFinishTime = d.persionFinishTime,
                    personFinishPositionName = d.finishPositionName,
                    personFinishPositionLatitude = d.finishPositionLatitude,
                    personFinishPositionLongitude = d.finishPositionLongitude,

                    userCode = g.uCode,
                    userDuty = g.uDuty,
                    headPortrait = g.uHeadPortrait,
                    roleName = g.rName,
                    userSex = g.uSex.ToString(),
                    userName = g.uName,
                    _createdTime = d.createdTime,
                }
               ).OrderByDescending(m => m.messioPersonCreatedTime).ToList();

            foreach (var user in models)
            {
                user.headPortrait = Utils.toSiteUrl(user.headPortrait);
                user.createdTime = ((DateTime)user._createdTime).ToString("yyyy-MM-dd HH:mm:ss");

            }

            return models;
        }
        //获取管理员任务
        public List<MissionModel> getCaseAdminMissions(string caseCode, string userCode, string taskStatus)
        {

            var db = new DBLiveVideoEntities();
            var videoType = MsgTypes.Video.ToString();
            taskStatus = string.IsNullOrEmpty(taskStatus) ? "0" : taskStatus;
            var _status = Utils.ToInt(taskStatus);

            var caseDeployModel = db.tbCaseDeploys.Where(m => m.csCode.Equals(caseCode) && m.uCode.Equals(userCode) && m.rName.Equals("指挥官")).FirstOrDefault();
            if (caseDeployModel == null)
                return getCasePersonMissions(caseCode, userCode);

            var models = (
                from c in db.tbCaseMissions
                join d in db.tbCaseMissionDistributions on c.ID.ToString() equals d.missionId
                join e in db.tbUsers on d.userCode equals e.uCode into e_join
                from g in e_join
                where c.caseCode.Equals(caseCode)
                && d.missionPersonStatus == _status
                select new MissionModel()
                {
                    messionId = c.ID.ToString(),
                    caseCode = c.caseCode,

                    missionStatus = c.missionStatus,
                    missionLimiTime = c.missionLimiTime,
                    toPositionName = c.toPositionName,
                    toPositionLatitude = c.toPositionLatitude,
                    toPositionLongitude = c.toPositionLongitude,
                    missionRemark = c.missionRemark,
                    routeDistance = c.routeDistance,
                    createdUserCode = c.createdUserCode,
                    createdPositionName = c.createdPositionName,
                    createdPositionLatitude = c.createdPositionLatitude,
                    createdPositionLongitude = c.createdPositionLongitude,
                    finishTime = c.finishTime,
                    missionType = c.missionType,

                    messionPersonId = d.ID.ToString(),
                    messioPersonCreatedTime = d.createdTime,
                    missionPersonStatus = d.missionPersonStatus,
                    persionFinishTime = d.persionFinishTime,
                    personFinishPositionName = d.finishPositionName,
                    personFinishPositionLatitude = d.finishPositionLatitude,
                    personFinishPositionLongitude = d.finishPositionLongitude,

                    userCode = g.uCode,
                    userDuty = g.uDuty,
                    headPortrait = g.uHeadPortrait,
                    roleName = g.rName,
                    userSex = g.uSex.ToString(),
                    userName = g.uName,
                    _createdTime = d.createdTime,
                }
               ).OrderByDescending(m => m.messioPersonCreatedTime).ToList();

            foreach (var user in models)
            {
                user.headPortrait = Utils.toSiteUrl(user.headPortrait);
                user.createdTime = ((DateTime)user._createdTime).ToString("yyyy-MM-dd HH:mm:ss");

            }

            return models;
        }



        /// <summary>
        ///  发布任务
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx addMission(tbCaseMission model)
        {
            var result = new ResultEx();
            try
            {
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;
                model.createdTime = DateTime.Now;

                var db = new DBLiveVideoEntities();

                db.tbCaseMissions.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }

        /// <summary>
        /// 发布任务
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ResultEx publishMission(ApiParams mParams)
        {
            var msgModel = new MsgModel() { MsgCode = "1", MsgErr = "", MsgContent = "", MsgType = "" };
            var mission = new MissionModel();
            var listKeys = new List<string>();
            var result = new ResultEx();

            var tbCaseMissionDistributions = new List<tbCaseMissionDistribution>();

            try
            {
                var db = new DBLiveVideoEntities();
                var userKeys = mParams.MissionUserCodes.Split(',');

                for (int i = 0; i < userKeys.Length; i++)
                {
                    if (!string.IsNullOrEmpty(userKeys[i]) && !listKeys.Contains(userKeys[i]))
                    {
                        listKeys.Add(userKeys[i].Trim());
                    }
                }


                var missionModel = new tbCaseMission();
                missionModel = new tbCaseMission()
                {
                    caseCode = mParams.CaseCode,
                    missionLimiTime = Utils.ToInt(mParams.MissionLimitTime),
                    missionRemark = mParams.MissionRemark,
                    toPositionLatitude = Utils.ToDecimal(mParams.ToLatitude),
                    toPositionLongitude = Utils.ToDecimal(mParams.ToLongitude),
                    toPositionName = mParams.ToPositionName,
                    missionType = mParams.MissionType,
                    createdUserCode = mParams.UserCode,
                    createdPositionLatitude = Utils.ToDecimal(mParams.UserLatitude),
                    createdPositionLongitude = Utils.ToDecimal(mParams.UserLongitude),
                    createdPositionName = mParams.UserPositionName,
                    missionStatus = 0,
                    routeDistance = Utils.ToDecimal(mParams.RouteDistance),


                };

                mission.missionLimiTime = Utils.ToInt(mParams.MissionLimitTime);
                mission.missionRemark = mParams.MissionRemark;
                mission.toPositionLatitude = Utils.ToDecimal(mParams.ToLatitude);
                mission.toPositionLongitude = Utils.ToDecimal(mParams.ToLongitude);
                mission.toPositionName = mParams.ToPositionName;
                mission.missionType = mParams.MissionType;
                mission.createdUserCode = mParams.UserCode;
                mission.createdUserName = mParams.UserName;
                mission.createdPositionLatitude = Utils.ToDecimal(mParams.UserLatitude);
                mission.createdPositionLongitude = Utils.ToDecimal(mParams.UserLongitude);
                mission.createdPositionName = mParams.UserPositionName;
                mission.routeDistance = Utils.ToDecimal(mParams.RouteDistance);



                var addMissionResult = addMission(missionModel);
                if (addMissionResult.IsSuccess)
                {
                    //创建任务成功，下面开始分配给具体人员
                    tbCaseMission addMissionModel = (tbCaseMission)addMissionResult.Entity;
                    //删除之前任务执行人
                    if (addMissionModel != null)
                    {

                        var missionMenrbs = db.tbCaseMissionDistributions.Where(m =>
                        m.missionId.Trim().Equals(addMissionModel.ID.ToString()) &&
                        m.caseCode.Equals(mParams.CaseCode) &&
                       listKeys.Contains(m.userCode)
                        ).ToList();

                        if (missionMenrbs != null)
                        {
                            foreach (var model in missionMenrbs)
                            {
                                if (listKeys.Contains(model.userCode.Trim()))
                                {
                                    listKeys.Remove(model.userCode.Trim());
                                }
                            }
                        }



                        foreach (var user in listKeys)
                        {

                            tbCaseMissionDistributions.Add(new tbCaseMissionDistribution()
                            {
                                ID = Guid.NewGuid().ToString(),
                                recSN = Common.Utils.GetTimeStampBits(),
                                isDel = false,
                                createdTime = DateTime.Now,
                                userCode = user,
                                caseCode = mParams.CaseCode,
                                missionId = addMissionModel.ID.ToString(),
                                missionPersonStatus = 0,


                            });

                        }
                        db.tbCaseMissionDistributions.AddRange(tbCaseMissionDistributions);

                    }



                }

                if (db.SaveChanges() > 0)
                {



                    string clientId = Guid.NewGuid().ToString();
                    try
                    {
                        /*
                        var client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
                        client.Connect(clientId);
                        */
                        MqttClient client;
                        try
                        {
                            client = this.mqttConnect();
                        }
                        catch (Exception ex)
                        {
                            result.Message = ex.Message.ToString();
                            result.IsSuccess = false;
                            result.ResultCode = "-1";
                            return result;
                        }
                        msgModel.MsgContent = JsonConvert.SerializeObject(mission);
                        msgModel.MsgType = MsgTypes.NewMission.ToString();
                        var userListenerTag = Utils.GetAppSetting("LiveVideoSDK_Persion");


                        var userAdmins = db.tbCaseDeploys.Where(m => m.csCode.Trim().Equals(mParams.CaseCode.Trim()) && m.rName.Trim().Equals("指挥官")).ToList();
                        if (userAdmins != null && userAdmins.Count() > 0)
                        {
                            foreach (var userTemp in userAdmins)
                            {
                                if (!string.IsNullOrEmpty(userTemp.uCode) && !listKeys.Contains(userTemp.uCode.Trim()))
                                {
                                    listKeys.Add(userTemp.uCode.Trim());
                                }
                            }
                        }

                        foreach (var user in listKeys)
                        {

                            if (user == null || string.IsNullOrEmpty(user))
                                continue;
                            if (tbCaseMissionDistributions != null)
                            {
                                var subMissionModel = tbCaseMissionDistributions.Where(m => m.userCode.Trim().Equals(user.Trim())).FirstOrDefault();
                                if (subMissionModel != null)
                                {
                                    mission.messionId = subMissionModel.missionId;
                                    mission.messionPersonId = subMissionModel.ID.ToString();
                                    mission.userCode = subMissionModel.userCode;
                                    mission.caseCode = subMissionModel.caseCode;
                                    msgModel.MsgContent = JsonConvert.SerializeObject(mission);
                                }
                                else
                                {

                                    mission.messionPersonId = "";
                                    mission.userCode = "指挥官";
                                    msgModel.MsgContent = JsonConvert.SerializeObject(mission);
                                }
                            }
                            client.Publish(userListenerTag + user.TrimEnd(), Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(msgModel)));
                        }
                        this.release(client);
                        //client.Disconnect();
                    }
                    catch (Exception ex)
                    {

                    }



                    result.IsSuccess = true;
                    result.Message = "任务发布成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                    result.Entity = null;
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "任务发布失败";
                    result.ResultCode = "-1";
                }

                result.Entity = null;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "删除成功";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "删除失败";

            }


            return result;
        }



        /// <summary>
        /// 更新讨论组的名字
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public ResultEx updateDiscussionName(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {
                var db = new DBLiveVideoEntities();
                var model = db.tbDiscussionGroups.Where(m => m.discussionCode.Equals(mParams.DisscusionCode)).FirstOrDefault();
                if (model != null)
                {
                    //更新
                    model.discussionName = mParams.DisscusionName;
                    if (db.SaveChanges() > 0)
                    {
                        result.IsSuccess = true;
                        result.Message = "更新成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                        result.Entity = model;
                        return result;
                    }

                }
                result.IsSuccess = false;
                result.Message = "更新失败、讨论组不存在。";
                result.ResultCode = "-1";
            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "更新失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "更新失败";

            }
            return result;
        }




        /// <summary>
        /// 搜索历史
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        public List<HistoryModel> searchHistory(ApiParams mParams)
        {
            var _skip = Common.Utils.ToInt(mParams.Take);
            var _take = Common.Utils.ToInt(mParams.Skip);
            _take = _take <= 0 ? 10 : _take;

            var _startTime = Common.Utils.toDatetime(mParams.StartTime);
            var _endTime = Common.Utils.toDatetime(mParams.EndTime);
            _endTime = _endTime.AddDays(1);
            var historyModels = new List<HistoryModel>();
            var db = new DBLiveVideoEntities();

            var searchType = mParams.SearchType.Trim();
            if (searchType.Equals(SearchType.notice.ToString()))
            {
                ///搜素消息通告

                historyModels = (from a in db.tbCaseGoings
                                 join d in db.tbUsers on a.uCode equals d.uCode into d_join
                                 from d in d_join.DefaultIfEmpty()
                                 where a.csCode.Equals(mParams.CaseCode)
                                 && (string.IsNullOrEmpty(mParams.StartTime) || a.cgCreateTime >= _startTime)
                                 && (string.IsNullOrEmpty(mParams.EndTime) || a.cgCreateTime < _endTime)
                                 && (string.IsNullOrEmpty(mParams.SearchText) || (a.uCode.Trim().Contains(mParams.SearchText) || d.uName.Trim().Contains(mParams.SearchText) || a.cgAbstract.Trim().Contains(mParams.SearchText)))
                                 select new HistoryModel()
                                 {
                                     PositionLatitude = a.PositionLatitude.ToString(),
                                     PositionLongitude = a.PositionLongitude.ToString(),
                                     PositionName = a.PositionName,
                                     PositionRemark = a.PositionRemark,
                                     SearchType = SearchType.notice.ToString(),
                                     UserName = d.uName,
                                     UserCode = d.uCode,
                                     NoticeTime = a.cgCreateTime.ToString()
                                 ,
                                     NotcieContent = a.cgAbstract,
                                     NoticeType = a.cgType,
                                     HeadPortrait = d.uHeadPortrait,
                                     CreatedTime = a.cgCreateTime.ToString(),
                                     Id = a.ID.ToString()
                                 }).OrderByDescending(m => m.CreatedTime).ToList();

                var noticeKeys = historyModels.Select(m => m.Id.ToString()).ToList();
                var pics = db.tbFileDetails.Where(m => noticeKeys.Contains(m.virtualId)).OrderByDescending(c => c.recSN).ToList();

                foreach (var model in historyModels)
                {
                    var picmodel = pics.Where(m => m.virtualId.ToString().ToUpper().Equals(model.Id.ToUpper())).ToList();

                    if (picmodel != null && picmodel.Count > 0)
                    {
                        var pic = picmodel.Select(m => m.fRelativePath).ToList();
                        string imgs = string.Empty;
                        foreach (var url in pic)
                        {
                            var img = Utils.toSiteUrl((string.IsNullOrEmpty(url) ? "" : url));
                            imgs = imgs + img + ",";
                        }
                        model.UserPics = imgs;
                    }
                    model.HeadPortrait = Utils.toSiteUrl(model.HeadPortrait);
                    model.CreatedTime = Common.Utils.toDatetime(model.CreatedTime).ToString("yyyy-MM-dd HH:mm:ss");
                }



            }
            else if (searchType.Equals(SearchType.image.ToString()))
            {
                //搜索图片
                var imgType = MsgTypes.Image.ToString();

                var models = (
                    from c in db.tbCaseMessages
                    join a in db.tbCases on c.csCode equals a.csCode
                    join d in db.tbUsers on c.uCode equals d.uCode
                    join i in db.tbDiscussionGroups on c.msgToCode equals i.discussionCode into i_join
                    from i in i_join.DefaultIfEmpty()
                    join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                    from g in e_join.DefaultIfEmpty()
                    where c.msgType.Equals(imgType)
                    && (string.IsNullOrEmpty(mParams.CaseCode) || c.csCode.Equals(mParams.CaseCode))
                    //搜索某个人的图片
                    && (string.IsNullOrEmpty(mParams.StartTime) || c.msgTime >= _startTime)
                    && (string.IsNullOrEmpty(mParams.EndTime) || c.msgTime < _endTime)

                    &&
                    ((string.IsNullOrEmpty(mParams.SearchText) || (d.uCode.Trim().Contains(mParams.SearchText) || d.uName.Trim().Contains(mParams.SearchText)))
                    || (string.IsNullOrEmpty(mParams.SearchText) || i.discussionCode.Trim().Contains(mParams.SearchText.Trim()) || i.discussionName.Trim().Contains(mParams.SearchText.Trim()))
                     )
                    select new ChatModel()
                    {
                        CaseName = a.csName,
                        MsgTime = c.msgTime.ToString(),
                        CreatedTime = c.msgTime,
                        SendUserId = d.ID.ToString(),
                        SendUserCode = d.uCode,
                        SendUserDuty = d.uDuty,
                        SendUserName = d.uName,
                        MsgContent = c.msgAbstract,
                        MsgType = c.msgType,
                        MsgId = c.ID.ToString(),
                        MsgFile = c.msgFile,//
                        CaseId = a.ID.ToString(),
                        CaseCode = a.csCode,
                        SendUserDepartment = g.dName,
                        SendUserHeadPortrait = d.uHeadPortrait,
                        SendUserDepartmentCode = g.dCode,
                        SendUserLatitude = c.uLatitude.ToString(),
                        SendPositionName = c.uPositionName,
                        SendUserLongitude = c.uLongitude.ToString(),
                        MsgFromType = c.msgFromType,
                        MsgToCode = c.msgToCode

                    }
                   )
                   .OrderByDescending(m => m.CreatedTime).ToList();

                var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
                var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
                var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
                var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();
                foreach (var model in models)
                {
                    var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                    tbFileDetail picFile = null;
                    if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                        picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                    model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                    model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                    model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                    if (!string.IsNullOrEmpty(model.MsgTime))
                        model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

                }
                historyModels = (from a in models
                                 select new HistoryModel()
                                 {
                                     ImageModel = a.MsgContent.ToString(),
                                     SearchType = SearchType.image.ToString(),
                                     HeadPortrait = a.SendUserHeadPortrait,
                                     CreatedTime = a.MsgTime,
                                     UserCode = a.SendUserCode,
                                     UserName = a.SendUserName
                                 }).ToList();
            }
            else if (searchType.Equals(SearchType.video.ToString()))
            {
                //搜索案件里相关视频

                var videoType = MsgTypes.Video.ToString();
                var videoModels = Utils.getVideoNames();
                //var caseDeploy = getCaseDeploy(caseCode);
                var camera = DeloyType.Camera.ToString();
                ///案件里的所有设备

                var modelDevices = (
                 from d in db.tbCaseDeployDevs
                 join c in db.tbDevices on d.devCode equals c.devCode into c_join
                 from c in c_join.DefaultIfEmpty()
                 join e in db.tbUsers on d.uCode equals e.uCode into e_join
                 from e in e_join.DefaultIfEmpty()
                 join a in db.tbFileDetails on c.devPhoto equals a.fCode into a_join
                 from a in a_join.DefaultIfEmpty()
                 where d.csCode.Equals(mParams.CaseCode)
                 select new DeployModel()
                 {
                     recSN = d.recSN,
                     DeployType = c.devType,
                     DeployDeviceCode = c.devCode,
                     DeployDeviceName = c.devName,
                     DeployDeviceUserCode = e.uCode,
                     DevSN = c.devSN,
                     DeployDeviceUserName = e.uName,
                     DeployDevicePhoto = a.fRelativePath,
                     DeployUserPhoto = e.uHeadPortrait,
                     Level = 2,

                 }).OrderByDescending(m => m.recSN).ToList();

                var deviceKeys = new List<string>();
                //var tempDevice =
                modelDevices.Where(d => (string.IsNullOrEmpty(mParams.SearchText) || ((d.DeployDeviceUserName != null) && d.DeployDeviceUserName.Contains(mParams.SearchText.Trim()))))
                .Select(m => m.DevSN).ToList().ForEach(s =>
                {
                    deviceKeys.Add(s);

                });
                var models = (
                     from c in db.tbCaseMessages
                     join a in db.tbCases on c.csCode equals a.csCode
                     join d in db.tbUsers on c.uCode equals d.uCode
                     join i in db.tbDiscussionGroups on c.msgToCode equals i.discussionCode into i_join
                     from i in i_join.DefaultIfEmpty()
                     join e in db.tbDepartments on d.dCode equals e.dCode into e_join
                     from g in e_join.DefaultIfEmpty()
                     where c.msgType.Equals(videoType)
                     && (string.IsNullOrEmpty(mParams.CaseCode) || c.csCode.Equals(mParams.CaseCode))
                     //搜索某个人的直播视频
                    && (string.IsNullOrEmpty(mParams.StartTime) || c.msgTime >= _startTime)
                    && (string.IsNullOrEmpty(mParams.EndTime) || c.msgTime < _endTime)
                     &&
                     ((string.IsNullOrEmpty(mParams.SearchText) || (d.uCode.Trim().Contains(mParams.SearchText) || d.uName.Trim().Contains(mParams.SearchText)))
                     || (string.IsNullOrEmpty(mParams.SearchText) || i.discussionCode.Trim().Contains(mParams.SearchText.Trim()) || i.discussionName.Trim().Contains(mParams.SearchText.Trim()))
                      )
                     select new ChatModel()
                     {
                         CaseName = a.csName,
                         MsgTime = c.msgTime.ToString(),
                         CreatedTime = c.msgTime,
                         SendUserId = d.ID.ToString(),
                         SendUserCode = d.uCode,
                         SendUserDuty = d.uDuty,
                         SendUserName = d.uName,
                         MsgContent = c.msgAbstract,
                         MsgType = c.msgType,
                         MsgId = c.ID.ToString(),
                         MsgFile = c.msgFile,//
                         CaseId = a.ID.ToString(),
                         CaseCode = a.csCode,
                         SendUserDepartment = g.dName,
                         SendUserHeadPortrait = d.uHeadPortrait,
                         SendUserDepartmentCode = g.dCode,
                         SendUserLatitude = c.uLatitude.ToString(),
                         SendPositionName = c.uPositionName,
                         SendUserLongitude = c.uLongitude.ToString(),
                         MsgFromType = c.msgFromType,
                         MsgToCode = c.msgToCode
                     }
                    )
                    .OrderByDescending(m => m.CreatedTime).ToList();

                var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
                var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
                var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
                var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();
                foreach (var model in models)
                {
                    var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                    tbFileDetail picFile = null;
                    if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                        picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                    model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                    model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                    model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                    if (!string.IsNullOrEmpty(model.MsgTime))
                        model.MsgTime = Common.Utils.toDatetime(model.MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

                }

                if (videoModels != null)
                {
                    videoModels.Where(m => deviceKeys.Contains(m.StreamName)).ToList().ForEach(s =>
                    {
                        var deployModel = modelDevices.Where(m => m.DevSN != null && m.DevSN.Equals(s.StreamName.Trim())).FirstOrDefault();
                        models.Insert(0, new ChatModel()
                        {
                            MsgTime = s.VideoEndTime,
                            CreatedTime = Utils.toDatetime(s.VideoEndTime),
                            SendUserCode = deployModel == null ? "" : deployModel.DeployDeviceUserCode,
                            SendUserDuty = deployModel == null ? "" : deployModel.DeployUserDuty,
                            SendUserName = deployModel == null ? "" : (string.IsNullOrEmpty(deployModel.DeployDeviceUserName) ? deployModel.DeployDeviceName : deployModel.DeployDeviceUserName),
                            MsgContent = JsonConvert.SerializeObject(s),
                            MsgType = MsgTypes.Video.ToString(),
                            CaseCode = deployModel == null ? "" : deployModel.CaseCode,
                            SendUserHeadPortrait = deployModel == null ? "" : (string.IsNullOrEmpty(deployModel.DeployUserPhoto) ? Bll.Common.Utils.toSiteUrl(deployModel.DeployDevicePhoto) : Bll.Common.Utils.toSiteUrl(deployModel.DeployUserPhoto)),

                            SendUserLatitude = deployModel == null ? "" : deployModel.DeployUserLatitude,
                            SendPositionName = deployModel == null ? "设备暂无定位" : (string.IsNullOrEmpty(deployModel.DeployUserPositionName) ? "设备暂无定位" : deployModel.DeployUserPositionName),
                            SendUserLongitude = deployModel == null ? "" : deployModel.DeployUserLongitude,

                        });
                    });


                }
                historyModels = (from a in models
                                 select new HistoryModel()
                                 {
                                     VideoModel = a.MsgContent.ToString(),
                                     SearchType = SearchType.video.ToString(),
                                     HeadPortrait = a.SendUserHeadPortrait,
                                     CreatedTime = a.MsgTime,
                                     UserCode = a.SendUserCode,
                                     UserName = a.SendUserName
                                 }).ToList();

            }



            return historyModels.OrderByDescending(m => m.CreatedTime).ToList();
        }

        public List<tbFileDetail> getFiles(ApiParams mParams)
        {


            var db = new DBLiveVideoEntities();
            var models = db.tbFileDetails.Where(
                m => m.virtualId.Equals(mParams.virtualId)
                ).OrderByDescending(m => m.recSN).ToList();
            foreach (var model in models)
            {
                model.fRelativePath = Utils.toSiteUrl(model.fRelativePath);

            }
            return models;
        }

        public List<CollectionModel> getMyCollections(ApiParams mParams)
        {


            var db = new DBLiveVideoEntities();
            var models = (
              from e in db.tbCaseCollections
              join a in db.tbCases on e.caseCode equals a.csCode
              join c in db.tbCaseMessages on e.messageId equals c.ID.ToString()
              join d in db.tbUsers on c.uCode equals d.uCode
              where e.uCode.Equals(mParams.UserCode)
              select new CollectionModel()
              {
                  CaseName = a.csName,
                  MsgTime = c.msgTime.ToString(),
                  _MsgTime = c.msgTime,
                  CreatedTime = c.msgTime,
                  SendUserId = d.ID.ToString(),
                  SendUserCode = d.uCode,
                  SendUserDuty = d.uDuty,
                  SendUserName = d.uName,
                  MsgContent = c.msgAbstract,
                  MsgType = c.msgType,
                  MsgId = c.ID.ToString(),
                  MsgFile = c.msgFile,//
                  CaseId = a.ID.ToString(),
                  CaseCode = a.csCode,
                  SendUserHeadPortrait = d.uHeadPortrait,
                  SendUserLatitude = c.uLatitude.ToString(),
                  SendPositionName = c.uPositionName,
                  SendUserLongitude = c.uLongitude.ToString(),
                  MsgFromType = c.msgFromType,
                  MsgToCode = c.msgToCode,
                  CollectionTime = e.createdTime.ToString(),
                  _CollectionTime = e.createdTime,
                  Reamrk = e.remark,
                  MessageId = e.messageId,
                  Id = e.ID.ToString()
              }


              ).OrderByDescending(m => m._CollectionTime).ToList();

            var tbFileKeys = models.Where(s => !string.IsNullOrEmpty(s.MsgFile)).Select(m => m.MsgFile).ToList();
            var tbFiles = db.tbFileDetails.Where(m => tbFileKeys.Contains(m.fCode)).ToList();
            var picKeys = tbFiles.Where(m => !string.IsNullOrEmpty(m.fFirstFrame)).Select(s => s.fFirstFrame).ToList();
            var picFiles = db.tbFileDetails.Where(m => picKeys.Contains(m.fCode)).ToList();

            foreach (var model in models)
            {
                if (!Utils.isConatinMsgType(model.MsgType))
                    model.MsgType = Utils.getDefaultMsgTYpe().ToString();
                var tbfile = tbFiles.Find(m => m.fCode.Equals(model.MsgFile));
                tbFileDetail picFile = null;
                if (tbfile != null && !string.IsNullOrEmpty(tbfile.fFirstFrame))
                    picFile = picFiles.Find(m => m.fCode.Equals(tbfile.fFirstFrame));
                model.MsgContent = GetMsgJson(model.MsgType, model.MsgContent.ToString(), tbfile, picFile);
                model.SendUserHeadPortrait = Bll.Common.Utils.toSiteUrl(model.SendUserHeadPortrait);
                model.MsgType = string.IsNullOrEmpty(model.MsgType) ? "" : model.MsgType.TrimEnd();
                if (!string.IsNullOrEmpty(model.MsgTime))
                    model.MsgTime = ((DateTime)model._MsgTime).ToString("yyyy-MM-dd HH:mm:ss");

                if (!string.IsNullOrEmpty(model.CollectionTime))
                    model.CollectionTime = ((DateTime)model._CollectionTime).ToString("yyyy-MM-dd HH:mm:ss");
            }

            return models;
        }




        /// <summary>
        /// 删除成员
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ResultEx deleteCollection(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {


                var db = new DBLiveVideoEntities();


                var model = db.tbCaseCollections.Where(m => m.ID.ToString().Equals(mParams.CollectionId.Trim())).FirstOrDefault();

                if (model != null)
                    db.tbCaseCollections.Remove(model);
                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "删除成功";
                    result.Exception = "";
                    result.ResultCode = "1";
                    result.Entity = null;
                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "删除失败";
                    result.ResultCode = "-1";
                }

                result.Entity = null;

            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "删除成功";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "删除失败";

            }


            return result;
        }



        public ResultEx addCollection(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {
                var model = new tbCaseCollection();
                model.ID = Guid.NewGuid().ToString();
                model.recSN = Common.Utils.GetTimeStampBits();
                model.isDel = false;

                model.createdTime = DateTime.Now;
                model.messageId = mParams.MessageId;
                model.remark = mParams.CollectionLabel;
                model.caseCode = mParams.CaseCode;
                model.uCode = mParams.UserCode;
                var db = new DBLiveVideoEntities();

                db.tbCaseCollections.Add(model);

                if (db.SaveChanges() > 0)
                {
                    result.IsSuccess = true;
                    result.Message = "添加成功";
                    result.Exception = "";
                    result.ResultCode = "1";

                }
                else
                {
                    result.IsSuccess = false;
                    result.Message = "添加失败";
                    result.ResultCode = "-1";
                }

                result.Entity = model;



            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "添加失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "添加失败";

            }


            return result;
        }



        public ResultEx updateCollectionLabelName(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {
                var db = new DBLiveVideoEntities();
                var model = db.tbCaseCollections.Where(m => m.ID.ToString().ToUpper().Equals(mParams.CollectionId.Trim().ToUpper())).FirstOrDefault();


                if (model != null)
                {
                    model.remark = mParams.CollectionLabelName;
                    if (db.SaveChanges() > 0)
                    {

                        //}
                        result.IsSuccess = true;
                        result.Message = "修改成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                        result.Entity = model;
                    }
                    else
                    {
                        result.IsSuccess = false;
                        result.Message = "修改失败";
                        result.ResultCode = "-1";
                    }



                }
                else
                {
                    result.Message = "修改失败";
                    return result;

                }


            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "修改失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "修改失败";

            }
            return result;
        }



        public ResultEx cancelPersonMission(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {
                var db = new DBLiveVideoEntities();

                var model = db.tbCaseMissionDistributions.Where(m => m.ID.ToString().ToUpper().Equals(mParams.PersonMissionId.ToUpper().Trim())).FirstOrDefault();
                if (model != null)
                {
                    model.missionPersonStatus = 1;
                    if (db.SaveChanges() > 0)
                    {

                        //}
                        result.IsSuccess = true;
                        result.Message = "修改成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                        result.Entity = model;
                    }
                    else
                    {
                        result.IsSuccess = false;
                        result.Message = "修改失败";
                        result.ResultCode = "-1";
                    }



                }
                else
                {
                    result.Message = "修改失败";
                    return result;

                }


            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "修改失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "修改失败";

            }
            return result;
        }

        public ResultEx cancelMisson(ApiParams mParams)
        {
            var result = new ResultEx();
            try
            {
                var db = new DBLiveVideoEntities();

                var model = db.tbCaseMissions.Where(m => m.ID.ToString().ToUpper().Equals(mParams.MissionId.ToUpper().Trim())).FirstOrDefault();
                if (model != null)
                {
                    model.missionStatus = 1;
                    if (db.SaveChanges() > 0)
                    {

                        //}
                        result.IsSuccess = true;
                        result.Message = "修改成功";
                        result.Exception = "";
                        result.ResultCode = "1";
                        result.Entity = model;
                    }
                    else
                    {
                        result.IsSuccess = false;
                        result.Message = "修改失败";
                        result.ResultCode = "-1";
                    }



                }
                else
                {
                    result.Message = "修改失败";
                    return result;

                }


            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.Message = "修改失败";
                //result.Exception = ex.GetBaseException().ToString();
                //记录错误日志
                result.Exception = "修改失败";

            }
            return result;
        }
        #endregion


        //Added by jason zeng for the event log

        public void logEvent(string account, string name, string eventName, string remark, string type)
        {
            var log = new mpds_log()
            {
                account = account,
                name = name,
                eventName = eventName,
                remark = remark,
                type = type,
                createtime = DateTime.Now
            };
            var db = new DBLiveVideoEntities();
            db.mpds_log.Add(log);
            db.SaveChanges();
        }

        public string getAPPGroup(string pdtGroup)
        {
            var param = new UserGroupModel() { groupid = pdtGroup };
            var result = MyBatisHelper.QueryForObject<UserGroupModel>("select_appgroup_pdt", param);
            if (result == null)
            {
                result = MyBatisHelper.QueryForObject<UserGroupModel>("select_appgroup_app", param);
                if (result == null)
                {
                    return null;
                }
            }
            return result.groupid;
            /*
            var db = new DBLiveVideoEntities();
            var model = db.tbDiscussionGroups.Where(m => m.relativegroupid.ToString().Equals(pdtGroup) && m.type.ToString().Equals("APP")).FirstOrDefault();
            if (model == null)
            {
                model = db.tbDiscussionGroups.Where(m => m.discussionCode.Equals(pdtGroup) && m.type.ToString().Equals("APP")).FirstOrDefault();
                if (model == null)
                {
                    return null;
                }
            }
            return model.discussionCode;
            */
        }

        public string getGroupById(string groupid)
        {
            var param = new UserGroupModel() { groupid = groupid };
            var result = MyBatisHelper.QueryForObject<UserGroupModel>("select_group_by_id", param);
            if (result == null)
            {
                return null;
            }
            return result.groupName;
        }

        public string getAPPGroupWithUser(string pdtGroup)
        {
            var param = new UserGroupModel() { groupid = pdtGroup};
            var result = MyBatisHelper.QueryForObject<UserGroupModel>("select_appgroup_with_user_pdt", param);
            if (result == null)
            {
                result = MyBatisHelper.QueryForObject<UserGroupModel>("select_appgroup_with_user_app", param);
                if (result == null)
                {
                    return null;
                }
            }
            return result.groupid;
        }

        public string getPdtGroup(string appGroup)
        {
            var db = new DBLiveVideoEntities();
            var model = db.tbDiscussionGroups.Where(m => m.discussionCode.ToString().Equals(appGroup) && m.type.ToString().Equals("APP")).FirstOrDefault();
            if (model == null)
            {
                return null;
            }
            return model.relativegroupid;
        }

        public Boolean switchGroup(string account, string group, string groupName)
        {
            var db = new DBLiveVideoEntities();
            var tbUser = db.tbUsers.Where(m => m.uCode.ToString().Equals(account)).FirstOrDefault();
            tbUser.groupid = group;
            tbUser.groupName = groupName;
            if (db.SaveChanges() > 0)
            {
                logEvent(tbUser.uCode, tbUser.uName, "Switch Group", "切换组成功", "APP");
                return true;
            }
            else
            {
                return false;
            }
        }

        public IList<tbUser> searchUser(string usercode, string account, string discussionCode, string dCode, int pageNum, int pageSize, string status)
        {
            UserModel param = new UserModel();
            param.UserCode = usercode;
            param.UserName = account;
            param.discussionCode = discussionCode;
            param.dCode = dCode;
            param.UserStatus = status;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            var db = new DBLiveVideoEntities();
            if (!String.IsNullOrEmpty(discussionCode))
            {
                UserGroupModel groupParam = new UserGroupModel();
                groupParam.groupid = discussionCode;
                UserGroupModel relatedGrp = MyBatisHelper.QueryForObject<UserGroupModel>("select_appgroup_app", groupParam);
                //var relatedGrp = db.tbDiscussionGroups.Where(m => discussionCode.Equals(m.discussionCode) && "APP".Equals(m.type)).FirstOrDefault();
                if (relatedGrp != null)
                {
                    param.discussionName = relatedGrp.groupName;
                }
            }
            
            IList<tbUser> userList = MyBatisHelper.QueryForList<tbUser>("search_user_by_param", param);
            /*
            Expression<Func<tbUser, bool>> expression = PredicateExtensions.True<tbUser>();
            var db = new DBLiveVideoEntities();
            if (!String.IsNullOrEmpty(account))
            {
                expression = expression.And(m => (m.uCode.Contains(account) || m.uName.Contains(account)) || m.deviceid.ToString().Equals(account));
            }
            if (!String.IsNullOrEmpty(discussionCode))
            {
                var relatedGrp = db.tbDiscussionGroups.Where(m => discussionCode.Equals(m.discussionCode) && "APP".Equals(m.type)).FirstOrDefault();
                if (relatedGrp == null)
                {
                    expression = expression.And(m => discussionCode.Equals(m.groupid));
                }
                else
                {
                    expression = expression.And(m => (discussionCode.Equals(m.groupid) || relatedGrp.relativegroupid.Equals(m.groupid)));
                }
            }
            if (!String.IsNullOrEmpty(dCode))
            {
                expression = expression.And(m => m.dCode.Equals(dCode));
            }
            expression = expression.And(m => "0".Equals(m.isDel.ToString()));
            var userList = db.tbUsers.Where(expression.Compile()).OrderBy(m => m.uCode).ToList();
            if (userList != null)
            {
                foreach (var user in userList)
                {
                    var a = db.tbCaseMessages.Where(m => m.uCode.Equals(usercode) && m.msgToCode.Equals(user.uCode) && m.msgFromType.Equals(MsgFromTypes.Person.ToString()) && !m.msgType.Equals(MsgTypes.Command.ToString()) && !m.msgType.Equals(MsgTypes.Recording.ToString())).Count();
                    var b = db.tbCaseMessages.Where(m => m.uCode.Equals(user.uCode) && m.msgToCode.Equals(usercode) && m.msgFromType.Equals(MsgFromTypes.Person.ToString()) && !m.msgType.Equals(MsgTypes.Command.ToString()) && !m.msgType.Equals(MsgTypes.Recording.ToString())).Count();
                    user.uIsUnilt = a + b;
                }
            }*/
            return userList;
        }

        public IList<UserGroupModel> getUserGroup(string account, string isdefault, string discussionCode, string discussionName, int pageNum, int pageSize)
        {
            UserGroupModel param = new UserGroupModel();
            param.account = account;
            param.isdefault = isdefault;
            param.groupid = discussionCode;
            param.groupName = discussionName;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            IList<UserGroupModel> result = MyBatisHelper.QueryForList<UserGroupModel>("select_user_group", param);
            return result;
            /*
            //List<UserGroupModel> list = new List<UserGroupModel>();
            var db = new DBLiveVideoEntities();

            var sql = from a in db.mpds_group_user
                      join b in db.tbDiscussionGroups
                      on a.groupid equals b.discussionCode
                      where a.account.Equals(account) && "0".Equals(b.isDel.ToString()) && b.type.Equals("APP") && a.isdefault.Equals(isdefault)
                      select new UserGroupModel()
                      {
                          groupid = a.groupid,
                          groupName = a.groupName,
                          type = a.type,
                          account = a.account,
                          name = a.name,
                          dept = a.dept,
                          deptid = a.deptid,
                          createtime = a.createtime,
                          updatetime = a.updatetime,
                          related = b.relativegroupid,
                          isdefault = a.isdefault
                      };
            if (String.IsNullOrEmpty(isdefault))
            {
                sql = from a in db.mpds_group_user
                      join b in db.tbDiscussionGroups
                      on a.groupid equals b.discussionCode
                      where a.account.Equals(account) && "0".Equals(b.isDel.ToString()) && b.type.Equals("APP")
                      select new UserGroupModel()
                      {
                          groupid = a.groupid,
                          groupName = a.groupName,
                          type = a.type,
                          account = a.account,
                          name = a.name,
                          dept = a.dept,
                          deptid = a.deptid,
                          createtime = a.createtime,
                          updatetime = a.updatetime,
                          related = b.relativegroupid,
                          isdefault = a.isdefault
                      };
            }
            //var sql = from a in db.mpds_group_user where account.Equals(account) select a;
            var userGroups = sql.ToList();
            foreach (var userGrp in userGroups)
            {
                var userCount = db.tbUsers.Where(m => m.groupid.Equals(userGrp.groupid) && "0".Equals(m.isDel.ToString()) && m.status.Equals("在线") && m.devicetype.Equals("APP")).Count();
                var pdtCnt = from a in db.tbDiscussionGroups
                             join b in db.tbUsers
                             on a.relativegroupid equals b.groupid
                             where a.discussionCode.Equals(userGrp.groupid) && a.type.Equals("APP") && b.devicetype.Equals("PDT") && b.status.Equals("在线")
                             select b;
                userCount = userCount + pdtCnt.Count();
                var msgCount = db.tbCaseMessages.Where(m => m.msgToCode.Equals(userGrp.groupid) && m.msgFromType.Equals(MsgFromTypes.Group.ToString()) && !m.msgType.Equals(MsgTypes.Command.ToString()) && !m.msgType.Equals(MsgTypes.Recording.ToString())).Count();
                userGrp.userCount = userCount;
                userGrp.uIsUnilt = msgCount;
                userGrp.related = String.IsNullOrEmpty(userGrp.related) ? "0" : "1";
            }
            return userGroups;
            */
        }

        public IList<RankGroupModel> getRank(string account, string isdefault, string discussionCode, string rankno, string discussionName, int pageNum, int pageSize)
        {
            UserGroupModel param = new UserGroupModel();
            param.account = account;
            param.isdefault = isdefault;
            param.groupid = discussionCode;
            param.rankNo = rankno;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            IList<RankGroupModel> result = MyBatisHelper.QueryForList<RankGroupModel>("select_rank_clazz", param);
            return result;
        }

        public IList<RankGroupModel> getRankUserGroup(string account, string isdefault, string discussionCode, string rankno, string discussionName, int pageNum, int pageSize)
        {
            UserGroupModel param = new UserGroupModel();
            param.account = account;
            param.isdefault = isdefault;
            param.groupid = discussionCode;
            param.groupName = discussionName;
            param.rankNo = rankno;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            IList<RankGroupModel> result = MyBatisHelper.QueryForList<RankGroupModel>("select_rank_user_group_clazz", param);
            return result;
        }

        public DeptTree getDepts(string dCode)
        {
            DeptTree tree = new DeptTree();
            var db = new DBLiveVideoEntities();
            //var dept = db.tbDepartments.Where(m => m.dCode.Equals(dCode)).FirstOrDefault();
            DeptModel param = new DeptModel();
            param.dCode = dCode;
            DeptModel dept = MyBatisHelper.QueryForObject<DeptModel>("select_dept", param);
            if (dept == null)
            {
                return tree;
            }
            //int all = db.tbUsers.Where(m => m.dCode.Equals(dCode)).Count();
            //int online = db.tbUsers.Where(m => m.dCode.Equals(dCode) && m.status.Equals("在线")).Count();
            tree.allUser = dept.allcount;
            tree.olUser = dept.onlinecount;
            tree.dept = dept;
            tree.children = getDeptChildren(db, dept.dCode, 1);
            return tree;
        }

        private int deptLevel = Convert.ToInt32(ConfigurationManager.AppSettings["deptlevel"].ToString());

        private List<DeptTree> getDeptChildren(DBLiveVideoEntities db, string dCode, int level)
        {
            List<DeptTree> treeList = new List<DeptTree>();
            if (level >= deptLevel)
            {
                return treeList;
            }
            level++;
            //var children = db.tbDepartments.Where(m => m.dFather.Equals(dCode)).ToList();
            DeptModel param = new DeptModel();
            param.dFather = dCode;
            IList<DeptModel> children = MyBatisHelper.QueryForList<DeptModel>("select_child_dept", param);
            if (children == null || children.Count < 1)
            {
                return treeList;
            }
            foreach (var dept in children)
            {
                DeptTree tree = new DeptTree();
                tree.dept = dept;
                //int all = db.tbUsers.Where(m => m.dCode.Equals(dept.dCode)).Count();
                //int online = db.tbUsers.Where(m => m.dCode.Equals(dept.dCode) && m.status.Equals("在线")).Count();
                tree.allUser = dept.allcount;
                tree.olUser = dept.onlinecount;
                tree.children = getDeptChildren(db, dept.dCode, level);
                treeList.Add(tree);
            }
            return treeList;
        }

        public IList<GpsModel> getUserGps(string name, string dCode, string discussionCode, int pageNum, int pageSize)
        {
            GpsModel param = new GpsModel();
            param.CaseName = name;
            param.dCode = dCode;
            param.DiscussionCode = discussionCode;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            var db = new DBLiveVideoEntities();
            var relatedGrp = db.tbDiscussionGroups.Where(m => discussionCode.Equals(m.discussionCode) && "APP".Equals(m.type)).FirstOrDefault();
            if (relatedGrp != null)
            {
                param.CaseCode = relatedGrp.relativegroupid;
            }

            IList<GpsModel> result = MyBatisHelper.Instance.QueryForList<GpsModel>("select_gps_data", param);
            return result;

            /*
            if (String.IsNullOrEmpty(name))
            {
                var sql = from a in db.tbUserIMDatas
                        join b in db.tbUsers on a.uCode equals b.uCode
                        select new GpsModel() { ID = a.ID.ToString(), UserCode = a.uCode, Longitude = a.longitude.ToString(), Latitdue = a.latitude.ToString(), UserName = b.uName, dCode = b.dCode, dName = b.dName, DiscussionName = b.groupName, DiscussionCode = b.groupid, GpsTargetType = b.devicetype, status = b.status, GpsTime = a.LastModifyTime.ToString() };
                Expression<Func<GpsModel, bool>> expression = PredicateExtensions.True<GpsModel>();
                if (!String.IsNullOrEmpty(discussionCode))
                {
                    //expression = expression.And(m => discussionCode.Equals(m.DiscussionCode));
                    var relatedGrp = db.tbDiscussionGroups.Where(m => discussionCode.Equals(m.discussionCode) && "APP".Equals(m.type)).FirstOrDefault();
                    if (relatedGrp == null)
                    {
                        expression = expression.And(m => discussionCode.Equals(m.DiscussionCode));
                    }
                    else
                    {
                        expression = expression.And(m => (discussionCode.Equals(m.DiscussionCode) || relatedGrp.relativegroupid.Equals(m.DiscussionCode)));
                    }
                }
                if (!String.IsNullOrEmpty(dCode))
                {
                    expression = expression.And(m => m.dCode.Equals(dCode));
                }
                var gpsList = sql.Where(expression.Compile()).OrderBy(m => m.UserName).Skip(pageNum * pageSize).Take(pageSize).ToList();
                return gpsList;
            }
            else
            {
                var sql = from a in db.tbUserIMDatas
                        join b in db.tbUsers on a.uCode equals b.uCode
                        where b.uCode.Contains(name) || b.groupName.Contains(name) || b.uName.Contains(name) || b.dName.Contains(name)
                        select new GpsModel() { ID = a.ID.ToString(), UserCode = a.uCode, Longitude = a.longitude.ToString(), Latitdue = a.latitude.ToString(), UserName = b.uName, dCode = b.dCode, dName = b.dName, DiscussionName = b.groupName, DiscussionCode = b.groupid, GpsTargetType = b.devicetype, status = b.status, GpsTime = a.LastModifyTime.ToString() };
                Expression<Func<GpsModel, bool>> expression = PredicateExtensions.True<GpsModel>();
                if (!String.IsNullOrEmpty(discussionCode))
                {
                    //expression = expression.And(m => m.DiscussionCode.Equals(discussionCode));
                    var relatedGrp = db.tbDiscussionGroups.Where(m => discussionCode.Equals(m.discussionCode) && "APP".Equals(m.type)).FirstOrDefault();
                    if (relatedGrp == null)
                    {
                        expression = expression.And(m => discussionCode.Equals(m.DiscussionCode));
                    }
                    else
                    {
                        expression = expression.And(m => (discussionCode.Equals(m.DiscussionCode) || relatedGrp.relativegroupid.Equals(m.DiscussionCode)));
                    }
                }
                if (!String.IsNullOrEmpty(dCode))
                {
                    expression = expression.And(m => m.dCode.Equals(dCode));
                }
                var gpsList = sql.Where(expression.Compile()).OrderBy(m => m.UserName).Skip(pageNum * pageSize).Take(pageSize).ToList();
                return gpsList;
            }
            */
        }

        public IList<RecordingModel> getAudoMsg(string msgFromType, string name, DateTime startDt, DateTime endDt, int pageNum, int pageSize)
        {
            if (String.IsNullOrEmpty(name))
            {
                name = "";
            }
            //var db = new DBLiveVideoEntities();
            RecordingModel param = new RecordingModel();
            param.sender = name;
            param.startDt = startDt;
            param.endDt = endDt;
            param.page = pageNum * pageSize;
            param.pageSize = pageSize;
            if (MsgFromTypes.Person.ToString().Equals(msgFromType))
            {
                return MyBatisHelper.QueryForList<RecordingModel>("select_audio_msg_person", param);
                /*
                var sql = from a in db.tbCaseMessages
                          join b in db.tbUsers on a.uCode equals b.uCode
                          join d in db.tbUsers on a.msgToCode equals d.uCode
                          join c in db.tbFileDetails on a.msgFile equals c.fCode
                          where a.msgFromType.Equals(msgFromType) && a.msgType.Equals(MsgTypes.Recording.ToString())
                                && (b.uName.Contains(name) || b.uCode.Contains(name))
                          select new RecordingModel()
                          {
                              uuid = c.virtualId,
                              FileCode = c.fCode,
                              FileName = c.fName,
                              RecordingUrl = c.fRelativePath,
                              RecordingTime = a.msgTime,
                              sender = b.uName,
                              second = a.msgAbstract,
                              dName = b.dName,
                              fromType = a.msgFromType,
                              receiver = d.uName
                          };
                var msgList = sql.Where(m => m.RecordingTime >= startDt && m.RecordingTime <= endDt).OrderByDescending(m => m.RecordingTime).Skip(pageNum * pageSize).Take(pageSize).ToList();
                return msgList;
                */
            }
            if (MsgFromTypes.Group.ToString().Equals(msgFromType))
            {
                return MyBatisHelper.QueryForList<RecordingModel>("select_audio_msg_group", param);
                /*
                var sql = from a in db.tbCaseMessages
                          join b in db.tbDiscussionGroups on a.msgToCode equals b.discussionCode
                          join d in db.tbUsers on a.uCode equals d.uCode
                          join c in db.tbFileDetails on a.msgFile equals c.fCode
                          where a.msgFromType.Equals(msgFromType) && a.msgType.Equals(MsgTypes.Recording.ToString()) 
                                && (b.discussionName.Contains(name) || b.discussionCode.Contains(name))
                          select new RecordingModel()
                          {
                              uuid = c.virtualId,
                              FileCode = c.fCode,
                              FileName = c.fName,
                              RecordingUrl = c.fRelativePath,
                              RecordingTime = a.msgTime,
                              receiver = b.discussionName,
                              second = a.msgAbstract,
                              fromType = a.msgFromType,
                              sender = d.uName
                          };
                var msgList = sql.Where(m => m.RecordingTime >= startDt && m.RecordingTime <= endDt).OrderByDescending(m => m.RecordingTime).Skip(pageNum * pageSize).Take(pageSize).ToList();
                return msgList;
                */
            }
            return new List<RecordingModel>();
        }

        public int pushRecording(string userCode, string msgType, string msgFile, string msgFromType, string msgToCode, string second, string virtualId)
        {
            var db = new DBLiveVideoEntities();
            tbCaseMessage msg = new tbCaseMessage()
            {
                //ID = Guid.NewGuid().ToString(),
                uCode = userCode,
                msgType = msgType,
                msgFile = msgFile,
                msgTime = DateTime.Now,
                msgFromType = msgFromType,
                msgToCode = msgToCode,
                msgAbstract = second
            };
            db.tbCaseMessages.Add(msg);
            var tbFile = db.tbFileDetails.Where(m => m.fCode.Equals(msgFile)).FirstOrDefault();
            if (tbFile != null)
            {
                tbFile.virtualId = virtualId;
                tbFile.fduration = second;
            }
            return db.SaveChanges();
        }

        public List<tbUser> getUserIds(string userid)
        {
            var db = new DBLiveVideoEntities();
            if (!String.IsNullOrEmpty(userid))
            {
                var userIds = db.tbUsers.Where(m => "0".Equals(m.isDel.ToString()) && m.uCode.Equals(userid)).ToList();
                return userIds;
            }
            else
            {
                //var userIds = db.tbUsers.Where(m => "0".Equals(m.isDel.ToString())).ToList();
                //return userIds;
                return new List<tbUser>();
            }
        }

        public int updateUserStatus(string account, string status)
        {
            using (var db = new DBLiveVideoEntities())
            {
                var tbUser = db.tbUsers.Where(m => m.uCode.Equals(account)).FirstOrDefault();
                tbUser.status = status;
                
                return db.SaveChanges();
            }
        }

        public tbUser getPdtUser(string account)
        {
            var db = new DBLiveVideoEntities();
            var user = db.tbUsers.Where(m => m.uCode.Equals(account) && m.devicetype.Equals("PDT") && "0".Equals(m.isDel.ToString())).FirstOrDefault();
            return user;
        }

        public List<RoleValueModel> getPermission(int roleid)
        {
            var db = new DBLiveVideoEntities();
            var role = db.mpds_role.Where(m => m.id.Equals(roleid)).FirstOrDefault();
            if (role.role_type == 1)
            {
                var sql = from a in db.mpds_menu
                        where a.nav_type.Equals("APP") && a.action_type.Contains("Show") && "0".Equals(a.is_lock.ToString())
                          select new RoleValueModel()
                        {
                            roleId = roleid.ToString(),
                            navName = a.name,
                            actionType = a.action_type
                        };
                var permissions = sql.ToList();
                return permissions;
            }
            else
            {
                var sql = from a in db.mpds_role_value
                          join b in db.mpds_menu on a.nav_name equals b.name
                          where roleid == a.role_id && b.nav_type.Equals("APP") && a.action_type.Contains("Show") && "0".Equals(b.is_lock.ToString())
                          select new RoleValueModel()
                          {
                              roleId = roleid.ToString(),
                              navName = a.nav_name,
                              actionType = a.action_type
                          };
                var permissions = sql.ToList();
                return permissions;
            }
        }

        public int userLogout(string account)
        {
            var db = new DBLiveVideoEntities();
            var user = db.tbUsers.Where(m => m.uCode.Equals(account) && m.devicetype.Equals("APP")).FirstOrDefault();
            if (user != null)
            {
                user.status = "离线";
            }
            var token = db.tbApiTokens.Where(m => m.uCode.Equals(account)).FirstOrDefault();
            if (token != null)
            {
                db.tbApiTokens.Remove(token);
            }
            return db.SaveChanges();
        }

        public tbUser getUserByDeviceId(string deviceId)
        {
            if (String.IsNullOrEmpty(deviceId))
            {
                return null;
            }
            var db = new DBLiveVideoEntities();
            return db.tbUsers.Where(m => deviceId.Equals(m.deviceid.ToString()) && "APP".Equals(m.devicetype)).FirstOrDefault();
        }

        public ResultEx resetPwd(string account, string oldPwd, string newPwd)
        {
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "重置密码失败" };
            var db = new DBLiveVideoEntities();
            var model = db.tbUsers.Where(m => m.uCode.Equals(account) && m.devicetype.Equals("APP") && "0".Equals(m.isDel.ToString()) && !m.status.Equals("禁用")).FirstOrDefault();
            if (model == null || string.IsNullOrEmpty(account))
            {
                result.Message = "账号不存在";
                return result;
            }
            if (model.status.Equals("禁用"))
            {
                result.Message = "账号被禁用";
                return result;
            }
            string encryptPwd = DESEncrypt.Encrypt(oldPwd.TrimEnd(), model.uSalt);
            if (model.uPassword.TrimEnd().Equals(encryptPwd))
            {
                model.uPassword = DESEncrypt.Encrypt(newPwd.TrimEnd(), model.uSalt);
            }
            else
            {
                result.Message = "密码不正确";
                return result;
            }
            if (db.SaveChanges() > 0)
            {
                result.IsSuccess = true;
                result.ResultCode = "0";
                result.Message = "密码修改成功";
            }

            return result;
        }

        public void updateHeartbeat(string account)
        {
            var db = new DBLiveVideoEntities();
            var tbuser = db.tbUsers.Where(m => m.uCode.Equals(account)).FirstOrDefault();
            if (tbuser != null)
            {
                tbuser.LYCID = GetTimestamp(DateTime.Now).ToString();
                if (tbuser.status.Equals("离线"))
                {
                    tbuser.status = "在线";
                }
                db.SaveChanges();
            }
        }

        public bool chargeEnable(string creator, string charger)
        {
            var db = new DBLiveVideoEntities();
            var sql1 = from a in db.mpds_role
                      join b in db.tbUsers on a.id equals b.roleid
                      where b.uCode.Equals(creator) && b.devicetype.Equals("APP")
                      select a;
            var createUser = sql1.FirstOrDefault();
            if (createUser == null)
            {
                return true;
            }
            var sql2 = from a in db.mpds_role
                       join b in db.tbUsers on a.id equals b.roleid
                       where b.uCode.Equals(charger) && b.devicetype.Equals("APP")
                       select a;
            var chargeUser = sql2.FirstOrDefault();
            if (chargeUser == null)
            {
                return false;
            }
            if (chargeUser.role_level > createUser.role_level)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public List<AudioDetailModel> getAudioDetail(string uuid)
        {
            var db = new DBLiveVideoEntities();
            var sql = from a in db.mpds_audio_detail
                      join b in db.tbUsers on a.startor equals b.deviceid.ToString() into temp
                      from t in temp.DefaultIfEmpty()
                      where a.conference_uuid.Equals(uuid) orderby a.id, a.event_time ascending
                      select new AudioDetailModel()
                      {
                          conference_uuid = a.conference_uuid,
                          conference_name = a.conference_name,
                          startor = t.uName == null ? "PDT":t.uName,
                          listener = a.listener,
                          event_name = a.event_name,
                          event_time = a.event_time,
                          content = a.content
                      };
            var details = sql.ToList();
            var resultList = new List<AudioDetailModel>();
            if (details != null)
            {
                string key = "";
                foreach (var detail in details)
                {
                    string temp = detail.startor + detail.event_name;
                    if (key.Equals(temp))
                    {
                        continue;
                    }
                    resultList.Add(detail);
                    key = temp;
                }
            }
            return resultList;
        }

        public long GetTimestamp(DateTime d)
        {
            TimeSpan ts = d.ToUniversalTime() - new DateTime(1970, 1, 1);
            return Convert.ToInt64(ts.TotalMilliseconds);     //精确到毫秒
        }

        public int imStatus()
        {
            MqttClient client;
            try
            {
                client = this.mqttConnect();
                client.Publish("im/status", Encoding.UTF8.GetBytes("Try Connection"));
                this.release(client);
                return 0;
            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(BaseManage), "IM Connect Error", ex);
                return -1;
            }
        }

        public tbUser getAppUserID(string pdtSSI, string type)
        {
            if (String.IsNullOrEmpty(pdtSSI))
            {
                return null;
            }
            var db = new DBLiveVideoEntities();
            if ("2".Equals(type))
            {
                return db.tbUsers.Where(m => pdtSSI.Equals(m.deviceid.ToString()) && "APP".Equals(m.devicetype)).FirstOrDefault();
            }
            else if ("1".Equals(type))
            {
                return db.tbUsers.Where(m => pdtSSI.Equals(m.deviceid.ToString()) && "PDT".Equals(m.devicetype)).FirstOrDefault();
            }
            else
            {
                return db.tbUsers.Where(m => pdtSSI.Equals(m.deviceid.ToString())).FirstOrDefault();
            }
        }

        public int setDefaultGroup(string account, string group, string isdefault)
        {
            try
            {
                UserGroupModel userGroup = new UserGroupModel();
                userGroup.account = account;
                userGroup.groupid = group;
                userGroup.type = "APP";
                userGroup.createtime = new DateTime();
                if (String.Equals("0", isdefault))
                {
                    MyBatisHelper.DeleteObject("delete_user_default_group", userGroup);
                }
                else
                {
                    var db = new DBLiveVideoEntities();
                    var usrGrp = db.mpds_group_user.Where(m => m.groupid.Equals(group) && m.account.Equals(account)).FirstOrDefault();
                    if (usrGrp != null)
                    {
                        return 1;
                    }
                    var user = db.tbUsers.Where(m => m.uCode.Equals(account)).FirstOrDefault();
                    var grp = db.tbDiscussionGroups.Where(m => m.discussionCode.Equals(group)).FirstOrDefault();
                    if (user == null || grp == null)
                    {
                        return 0;
                    }
                    userGroup.name = user.uName;
                    userGroup.groupName = grp.discussionName;
                    userGroup.isdefault = "1";
                    MyBatisHelper.InsertObject("insert_user_default_group", userGroup);
                }
                /*
                var db = new DBLiveVideoEntities();
                var mpds_group_user = db.mpds_group_user.Where(m => m.account.Equals(account) && m.groupid.Equals(group)).FirstOrDefault();
                mpds_group_user.isdefault = isdefault;
                return db.SaveChanges();*/
                //MyBatisHelper.UpdateObject("update_user_default_group", userGroup);
                return 1;
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Update Default Group Error: ", e);
                return 0;
            }
            
        }

        // New api for app operation
        public IList<UserGroupModel> getGroupByType(string type, string disscussionCode, int pageNum, int pageSize)
        {
            try
            {
                UserGroupModel param = new UserGroupModel();
                param.type = type;
                param.groupid = disscussionCode;
                param.page = pageNum * pageSize;
                param.pageSize = pageSize;
                var groupList = MyBatisHelper.QueryForList<UserGroupModel>("select_group_by_type", param);
                return groupList;
            }
            catch (Exception ex)
            {
                LogHelper.WriteError(typeof(BaseManage), "Get Group By Type Error: ", ex);
                return null;
            }
        }

        public String groupMgnt(string userAction, string DisscusionCode, string DisscusionName, string relativegroupid, string relativegroup, string type, string clazz, string status, string deptId, string deptName)
        {
            try
            {
                if ("Add".Equals(userAction))
                {
                    UserGroupModel param = new UserGroupModel();
                    param.type = type;
                    param.groupid = DisscusionCode;
                    var groupModel = MyBatisHelper.QueryForObject<UserGroupModel>("select_group_by_type_and_id", param);
                    if (groupModel != null)
                    {
                        return "通信组已存在，不能重复添加";
                    }
                    tbDiscussionGroup group = new tbDiscussionGroup();
                    group.discussionCode = DisscusionCode;
                    group.discussionName = DisscusionName;
                    group.relativegroupid = relativegroupid;
                    group.relativegroup = relativegroup;
                    group.type = type;
                    group.csCode = clazz;
                    group.status = Convert.ToInt32(status);
                    group.deptid = deptId;
                    group.dept = deptName;
                    group.createdTime = DateTime.Now;
                    group.updatetime = DateTime.Now;
                    MyBatisHelper.InsertObject("insert_group", group);
                }
                if ("Delete".Equals(userAction))
                {
                    tbDiscussionGroup group = new tbDiscussionGroup();
                    group.discussionCode = DisscusionCode;
                    MyBatisHelper.DeleteObject("delete_group", group);
                }
                if ("Update".Equals(userAction))
                {
                    tbDiscussionGroup group = new tbDiscussionGroup();
                    group.discussionCode = DisscusionCode;
                    group.discussionName = DisscusionName;
                    group.relativegroupid = relativegroupid;
                    group.relativegroup = relativegroup;
                    group.type = type;
                    group.csCode = clazz;
                    group.status = Convert.ToInt32(status);
                    group.deptid = deptId;
                    group.dept = deptName;
                    group.updatetime = DateTime.Now;
                    MyBatisHelper.UpdateObject("update_group", group);
                }
                return "";
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Group Manager Error: " + userAction, e);
                return "对通信组的管理操作失败";
            }
        }
        
        public String rankMgnt(string userAction, string rankNo, string rankName)
        {
            try
            {
                if ("Add".Equals(userAction))
                {
                    RankGroupModel param = new RankGroupModel();
                    param.rank = rankNo;
                    var rankModel = MyBatisHelper.QueryForObject<RankGroupModel>("select_rank_by_id", param);
                    if (rankModel != null)
                    {
                        return "通信组群已存在，不能重复添加";
                    }
                    RankGroupModel rank = new RankGroupModel();
                    rank.rank = rankNo;
                    rank.rankName = rankName;
                    MyBatisHelper.InsertObject("insert_rank", rank);
                }
                if ("Delete".Equals(userAction))
                {
                    RankGroupModel rank = new RankGroupModel();
                    rank.rank = rankNo;
                    MyBatisHelper.DeleteObject("delete_rank", rank);
                }
                if ("Update".Equals(userAction))
                {
                    RankGroupModel rank = new RankGroupModel();
                    rank.rank = rankNo;
                    rank.rankName = rankName;
                    MyBatisHelper.UpdateObject("update_rank", rank);
                }
                return "";
            }
            catch(Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Rank Manager Error: " + userAction, e);
                return "对通信组群的管理操作失败";
            }
        }

        
        public IList<UserGroupModel> rankGroup(string userAction, string rankNo, string DisscusionCode, int pageNum, int pageSize)
        {
            try
            {
                UserGroupModel param = new UserGroupModel();
                param.rankNo = rankNo;
                param.groupid = DisscusionCode;
                param.page = pageNum * pageSize;
                param.pageSize = pageSize;
                if ("0".Equals(userAction))
                {
                    var groupList = MyBatisHelper.QueryForList<UserGroupModel>("select_group_with_rank", param);
                    return groupList;
                }
                if ("1".Equals(userAction))
                {
                    var groupList = MyBatisHelper.QueryForList<UserGroupModel>("select_group_no_rank", param);
                    return groupList;
                }
                return null;
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Rank Group Search Error: " + userAction, e);
                return null;
            }
        }

        
        public String rankGroupMgnt(string userAction, string rankNo, string rankName, string discussionCode, string discussionName)
        {
            try
            {
                if ("Add".Equals(userAction))
                {

                    UserGroupModel param = new UserGroupModel();
                    param.rankNo = rankNo;
                    param.rankName = rankName;
                    param.groupid = discussionCode;
                    param.groupName = discussionName;
                    param.createtime = DateTime.Now;
                    var rankGroup = MyBatisHelper.QueryForObject<UserGroupModel>("select_rank_group", param);
                    if (rankGroup == null)
                    {
                        MyBatisHelper.InsertObject("insert_rank_group", param);
                    }
                }
                if ("Delete".Equals(userAction))
                {
                    UserGroupModel param = new UserGroupModel();
                    param.rankNo = rankNo;
                    param.groupid = discussionCode;
                    MyBatisHelper.DeleteObject("delete_rank_group", param);
                }
                return "";
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Rank Group Manager Error: " + userAction, e);
                return "维护组群组列表操作失败";
            }
        }

        
        public IList<UserGroupModel> getDefaultGroup(string usercode, int pageNum, int pageSize)
        {
            try
            {
                UserGroupModel param = new UserGroupModel();
                param.account = usercode;
                param.page = pageNum * pageSize;
                param.pageSize = pageSize;
                var groupList = MyBatisHelper.QueryForList<UserGroupModel>("select_group_user", param);
                return groupList;
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Search Default Group Error: ", e);
                return null;
            }
        }

        
        public String defaultGroupMgnt(string usercode, string discussionCode, string userAction)
        {
            try
            {
                UserGroupModel param = new UserGroupModel();
                param.account = usercode;
                param.groupid = discussionCode;
                param.isdefault = "1";
                if ("Add".Equals(userAction))
                {
                    MyBatisHelper.DeleteObject("delete_group_user", param);
                    var group = MyBatisHelper.QueryForObject<UserGroupModel>("select_group_by_id", param);
                    if (group != null)
                    {
                        param.dept = group.dept;
                        param.deptid = group.deptid;
                        param.type = group.type;
                        param.groupName = group.groupName;
                    }
                    else
                    {
                        return "无相关组信息";
                    }
                    UserModel user = new UserModel();
                    user.UserCode = usercode;
                    tbUser resultUser = MyBatisHelper.QueryForObject<tbUser>("select_app_user_by_ucode", user);
                    if (resultUser != null)
                    {
                        param.name = resultUser.uName;
                    }
                    else
                    {
                        return "无相关用户信息";
                    }
                    MyBatisHelper.InsertObject("insert_group_user", param);
                }
                if ("Delete".Equals(userAction))
                {
                    MyBatisHelper.DeleteObject("delete_group_user", param);
                }
                return "";
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(BaseManage), "Default Group Managerment Error: ", e);
                return "维护常用组失败";
            }
        }
        // New api for app operation

        private MqttClientHelper helper = MqttClientHelper.getInstance();

        private MqttClient mqttConnect()
        {
            MqttClient client = helper.getConnection();
            return client;
        }

        private void release(MqttClient client)
        {
            helper.release(client);
        }
    }
}
