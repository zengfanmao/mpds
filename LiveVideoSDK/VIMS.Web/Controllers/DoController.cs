using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using VIMS.LiveVideoSDK.Model;
using VIMS.LiveVideoSDK.Core;
using VIMS.LiveVideoSDK.Bll;
using VIMS.LiveVideoSDK.Bll.Entity;
using VIMS.LiveVideoSDK.Bll.Common;
using System.Threading.Tasks;
using System.Web;
using System.IO;
using System.Text;
using System.Configuration;
using System.Xml;

namespace VIMS.Web.Controllers
{
    public class DoController : ApiController
    {

        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx userLogin([FromBody]ApiParams mParams)
        {
            try
            {
                LogHelper.WriteInfo(typeof(DoController), "User Login : " + JsonHelper.ObjectToJSON(mParams));
                if (mParams == null)
                    return new ResultEx("参数错误");
                var model = new UserModel();
                var basemanage = new BaseManage();
                var result = basemanage.Login(mParams.LoginName, mParams.UserPwd);
                LogHelper.WriteInfo(typeof(DoController), "User Login Return: " + JsonHelper.ObjectToJSON(result));
                return result;
            } catch (Exception e)
            {
                LogHelper.WriteError(typeof(DoController), "User Login Error: ", e);
                return new ResultEx("登陆错误");
            }
            
        }
        /// <summary>
        /// 获取个人消息，包括每个人发个自己的最新一条消息，和每个讨论组内最新的一条消息 ，及消息计数
        /// </summary>
        /// <param name="UserCode">用户编号</param>
        /// <param name="ApiToken">秘钥</param>
        /// <param name="CaseCode">案件编号</param>
        /// <returns>json</returns>
        [HttpPost]
        public ResultEx getMyChatLists([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }

            var models = basemanage.GetMyChatLists(mParams.UserCode, mParams.CaseCode, mParams.MsgFromType);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = models;
            }
            else { result.Entity = null; result.IsSuccess = false; result.Message = "没有数据"; }
            return result;
        }
        /// <summary>
        /// 推送案件消息
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx pushMsg([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Push Msg : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            if (!MsgTypes.Recording.ToString().Equals(mParams.MsgType))
            {
                var pdtUser = basemanage.getPdtUser(mParams.UserCode);

                if (pdtUser == null)
                {
                    //验证秘钥
                    var myToken = basemanage.GetMyToke(mParams.UserCode);
                    if (string.IsNullOrEmpty(myToken))
                    {
                        result.Message = "验证失败，请重新登录";
                        result.IsSuccess = false;
                        return result;
                    }
                    if (!myToken.Equals(mParams.ApiToken))
                    {
                        result.Message = "秘钥不正确，请重新登录";
                        result.IsSuccess = false;
                        return result;

                    }
                }
            }
            
            if (string.IsNullOrEmpty(mParams.MsgType))
            {
                result.Message = "验证失败，类型不能为空";
                result.IsSuccess = false;
                return result;
            }
            if (string.IsNullOrEmpty(mParams.MsgContent) && !string.IsNullOrEmpty(mParams.MsgType) && mParams.MsgType.ToLower().Equals(MsgTypes.Text.ToString().ToLower()))
            {
                result.Message = "验证失败，消息不能为空";
                result.IsSuccess = false;
                return result;
            }

            //推送消息
            result = basemanage.PushMsg(mParams.UserCode, mParams.CaseCode, mParams.MsgType, mParams.MsgContent, mParams.MsgFile,
                mParams.UserLatitude, mParams.UserLongitude, mParams.UserPositionName, mParams.MsgFromType, mParams.MsgToCode
                );
            LogHelper.WriteInfo(typeof(DoController), "Push Msg Return : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx pushCommand([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Push Command : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "指令失败" };
            var basemanage = new BaseManage();
            result = basemanage.PushCommand(mParams.UserCode, mParams.MsgType, mParams.MsgContent, mParams.MsgFromType, mParams.MsgToCode);
            LogHelper.WriteInfo(typeof(DoController), "Push Command Return : " + JsonHelper.ObjectToJSON(result));
            return result;
        }


        /// <summary>
        /// 获服务器配置
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx getServerConfigs([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "获取失败" };

            try
            {
                var model = new ServerConfigModel()
                {
                    RtmpUrl = Utils.GetAppSetting("LiveVideoSDK_RtmpUrl"),
                    ImIp = "tcp://" + Utils.GetAppSetting("LiveVideoSDK_IM_Ip") + ":1883",
                    CallIp = Utils.GetAppSetting("LiveVideoSDK_Call_Ip"),
                    ApiUrl = Utils.GetAppSetting("LiveVideoSDK_ApiUrl")

                };


                if (!string.IsNullOrEmpty(model.RtmpUrl) || !string.IsNullOrEmpty(model.ImIp))
                {
                    result.Entity = model;
                    result.IsSuccess = true;
                    result.Message = "获取成功";
                    result.ResultCode = "1";
                }

            }
            catch
            {

            }
            return result;
        }
        /// <summary>
        /// 推送案件消息
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String pushMsgCase([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件信息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.MsgType))
            {
                result.Message = "验证失败，类型不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.MsgContent) && !string.IsNullOrEmpty(mParams.MsgType) && mParams.MsgType.ToLower().Equals(MsgTypes.Text.ToString().ToLower()))
            {
                result.Message = "验证失败，消息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            //推送消息
            result = basemanage.PushMsgCase(mParams.UserCode, mParams.CaseCode, mParams.MsgType, mParams.MsgContent, mParams.MsgFile,
                mParams.UserLatitude, mParams.UserLongitude, mParams.UserPositionName
                );


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 推送案件消息
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String pushMsgCaseNew([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件信息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.MsgType))
            {
                result.Message = "验证失败，类型不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.MsgContent) && !string.IsNullOrEmpty(mParams.MsgType) && mParams.MsgType.ToLower().Equals(MsgTypes.Text.ToString().ToLower()))
            {
                result.Message = "验证失败，消息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            //推送消息
            result = basemanage.PushMsgCaseNew(mParams.UserCode, mParams.CaseCode, mParams.MsgType, mParams.MsgContent, mParams.MsgFile,
                mParams.UserLatitude, mParams.UserLongitude, mParams.UserPositionName, mParams.MsgFromType, mParams.MsgToCode
                );


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 获取个人案件
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getMyCases([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            var models = basemanage.GetMyCases(mParams.UserCode, mParams.Skip, mParams.Take, mParams.CaseStatus,
                mParams.CaseName, mParams.CaseType, mParams.StartTime, mParams.EndTime
                );
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 获取
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getMyChats([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            var models = basemanage.GetMyChats(mParams.CaseCode, mParams.Skip, mParams.Take, mParams.FirstMsgId);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }
        [HttpPost]
        public async Task<ResultEx> PostFile()
        {
            LogHelper.WriteInfo(typeof(DoController), "Post file : ");
            var result = new ResultEx() { Message = "上传失败", IsSuccess = false, ResultCode = "-1" };
            var basemanage = new BaseManage();
            ResultEx addFileResult = null;
            // 检查是否是 multipart/form-data
            if (!Request.Content.IsMimeMultipartContent("form-data"))
            {
                result.Message = "数据传输方式不正确，mine类型没有指定";
                return result;
            }
            try
            {
                HttpContext context = HttpContext.Current;
                var formatStr = Utils.GetAppSetting("LiveVideoSDK_FileFormat");
                var root = Utils.GetAppSetting("LiveVideoSDK_FilePath");
                var path = context.Server.MapPath(root + Utils.Format("", formatStr));
                if (!Directory.Exists(Path.GetDirectoryName(path)))
                {
                    Directory.CreateDirectory(Path.GetDirectoryName(path));
                }
                //Bll.Common.Utils.Format("",formatStr);

                // 设置上传目录
                ReNameMultipartFormDataStreamProvider provider = null;
                try
                {
                    provider = new ReNameMultipartFormDataStreamProvider(path);

                }
                catch (Exception ex)
                {

                    result.Exception = ex.GetBaseException().ToString();
                    result.Message = "保存失败。";
                    return result;

                }

                // 接收数据，并保存文件
                //var bodyparts = await Request.Content.ReadAsMultipartAsync(provider);




                try
                {

                    var data = await Request.Content.ReadAsMultipartAsync(provider);
   
                }
                catch (FileAllowException ex)
                {
                    result.Exception = ex.msg;
                    result.Message = "保存失败" + ex.msg;
                    return result;
                }
                catch (Exception ex)
                {
                    if (!string.IsNullOrEmpty(provider.ExceptionMsg))
                    {
                        result.Exception = provider.ExceptionMsg;
                        result.Message = "保存失败。"+provider.ExceptionMsg;
                        return result;
                    }

                    if (ex.Message.Contains("MIME ") || ex.Message.Contains("意外的结尾"))
                    {
                        //do nothing, this is bug in Web API (http://aspnetwebstack.codeplex.com/discussions/354215)
                    }
                    else
                    {
                        result.Exception = ex.GetBaseException().ToString();
                        result.Message = "保存失败。";
                        return result;
                    }
                }
                //var files = new List<string>();  
                //foreach (MultipartFileData file in provider.FileData)
                //{

                //    files.Add(Guid.NewGuid()+".gif");
                //}

                // Send OK Response along with saved file names to the client.  
                //var response= Request.CreateResponse(HttpStatusCode.OK,result);  

                var siteRoot = context.Request.Url.GetLeftPart(UriPartial.Authority).ToString();
                var fileUrl = root.Replace(@"~", "") + Utils.Format("", formatStr) + provider.FileName;
                var response = Request.CreateResponse(HttpStatusCode.Accepted);
                var fileinfo = new FileInfo(path + provider.FileName);

                var virtualId = context.Request.QueryString["virtualId"];
                //var fileId = context.Request.QueryString["FileId"];
                virtualId = string.IsNullOrEmpty(virtualId) ? "" : virtualId;
                addFileResult = basemanage.AddFile(new tbFileDetail()
                {
                    virtualId = virtualId,
                    fName = provider.FileName,
                    fSize = fileinfo.Length / 1024,
                    fRelativePath = fileUrl,
                    fCode = RandomIdGenerator.GetBase62(14),
                });
                if (addFileResult.IsSuccess)
                {
                    result.ResultCode = "1";
                    result.IsSuccess = true;
                    result.Message = "上传成功";
                    if (addFileResult.Entity != null)
                        result.Entity = addFileResult.Entity;
                }
                else
                {

                    result.IsSuccess = false;
                    result.Message = "上传失败，新增记录失败";
                }



            }
            catch (Exception ex)
            {

                result.Exception = ex.GetBaseException().ToString();
                if (addFileResult != null)
                    result.Exception = result.Exception + ", addFileResult ：" + result.Exception;

                result.Message = "保存失败";
                return result;
                //throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
            LogHelper.WriteInfo(typeof(DoController), "Post file Return : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        public class ReNameMultipartFormDataStreamProvider : MultipartFormDataStreamProvider
        {
            /// <summary>
            /// 扩展名
            /// </summary>
            public string Extend { get; set; }
            /// <summary>
            /// 文件名字
            /// </summary>
            public string FileName { get; set; }
            /// <summary>
            /// 上传文件的大概大小
            /// </summary>
            public string FileSize { get; set; }
            public ReNameMultipartFormDataStreamProvider(string root) : base(root) { }

            public String ExceptionMsg;
            public override string GetLocalFileName(System.Net.Http.Headers.HttpContentHeaders headers)
            {


                //截取文件扩展名
                this.Extend = Path.GetExtension(headers.ContentDisposition.FileName.TrimStart('"').TrimEnd('"'));
                this.FileSize = Utils.toString(headers.ContentDisposition.Size);
                this.FileName = base.GetLocalFileName(headers);
                this.FileName = Utils.Format(this.Extend, "{yyyy}{mm}{dd}{rand:6}");


                var listArrowExtends = new List<string>();
                var extens= Utils.GetAppSetting("LiveVideoSDK_Upload_Extends"); ;
                if(String.IsNullOrEmpty(extens))
                {
                    ExceptionMsg = "暂无配置文件上传格式";
                    throw new FileAllowException(ExceptionMsg);
                }
               String [] exts= extens.Split(',');
                if (exts != null&&exts.Length>0)
                {
                    for(int i = 0; i < exts.Length;i++)
                    {
                        if (!String.IsNullOrEmpty(exts[i]))
                        {
                            listArrowExtends.Add(exts[i]);
                        }
                    }
                }
                if (listArrowExtends.Count<=0)
                {
                    ExceptionMsg = "暂无配置文件上传格式";
                    throw new FileAllowException(ExceptionMsg);
                }

                if (string.IsNullOrEmpty(this.Extend))
                {
                    ExceptionMsg = "不支持文件格式："+ this.Extend;
                    throw new FileAllowException(ExceptionMsg);
                }

                if (!listArrowExtends.Contains(this.Extend.ToLower()))
                {
                    ExceptionMsg = "不支持文件格式：" + this.Extend;
                    throw new FileAllowException(ExceptionMsg);
                }
                ExceptionMsg = "";



                return this.FileName;

            }
        }

        public class FileAllowException : Exception
        {
            public String msg;
            public FileAllowException(String msg)
            {
                msg = msg;
              
            }



        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String pushLivingCase([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件信息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.RtmpUrl))
            {
                result.Message = "验证失败，流媒体文件地址不能呢为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }


            //推送消息
            result = basemanage.pushLivingCase(mParams.UserCode, mParams.CaseCode, mParams.RtmpUrl, mParams.SourceType, mParams.DeviceCode, mParams.FirstFrameCode);


            return JsonConvert.SerializeObject(result);
        }



        /// <summary>
        /// 直播列表
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getMyLivings([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.GetMyLivings(mParams.UserCode, mParams.Skip, mParams.Take, mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }

        [HttpPost]
        public String getMyLivingCounts([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var livingCounts = basemanage.getMyLivingCounts(mParams.UserCode);
            result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
            result.Entity = livingCounts;
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 更改直播视频的状态是否在线
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>

        [HttpPost]
        public string updateLiving([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.LiveId))
            {
                result.Message = "直播视频id不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (!string.IsNullOrEmpty(mParams.LiveState) && mParams.LiveState.ToLower().Trim().Equals("true"))
            {

                result = basemanage.updateLiving(mParams.LiveId, true);
            }
            else
            {

                result = basemanage.updateLiving(mParams.LiveId, false);
            }


            return JsonConvert.SerializeObject(result);
        }
        [HttpPost]
        public String getUsers([FromBody]ApiParams mParams)
        {
            return getCaseDeployUsers(mParams);
        }
        [HttpPost]
        public String getCaseDeployUsers([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.GetCaseDeployUsers(mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }


        /// <summary>
        ///  虚拟添加直播后的视频文件
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public string addLivingHistoryVideoFile([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.RtmpUrl))
            {
                result.Message = "视频地址不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            result = basemanage.addLivingHistoryVideoFile(mParams.RtmpUrl, mParams.PicCode, mParams.StartTime, mParams.EndTime, mParams.CaseCode);

            return JsonConvert.SerializeObject(result);
        }


        /// <summary>
        /// 推送公告消息
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String pushNoticeCase([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件信息不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.NoticeType))
            {
                result.Message = "验证失败，公告类型不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //if (string.IsNullOrEmpty(mParams.Notice))
            //{
            //    result.Message = "验证失败，公告消息不能为空";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}
            if (string.IsNullOrEmpty(mParams.NoticeId))
            {
                result.Message = "验证失败，消息id不能为空，唯一主键";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }


            //推送消息
            result = basemanage.pushNoticeCase(mParams);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 更新视频文件字段，如展示图，开始结束时间
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx updateVideoFile([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }
            if (string.IsNullOrEmpty(mParams.MsgFile))
            {
                result.Message = "验证失败，文件编码不能为空";
                result.IsSuccess = false;
                return result;
            }


            //推送消息
            result = basemanage.updateVideoFile(mParams.MsgFile, mParams.PicCode, mParams.StartTime, mParams.EndTime,mParams.Duration);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return result;
        }


        /// <summary>
        /// 添加gps行程记录
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String addGpsHistory([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "GPS recording : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            //if (string.IsNullOrEmpty(mParams.ApiToken))
            //{
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}
            ////验证秘钥
            //var myToken = basemanage.GetMyToke(mParams.UserCode);
            //if (string.IsNullOrEmpty(myToken))
            //{
            //    result.Message = "验证失败，请重新登录";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}
            //if (!myToken.Equals(mParams.ApiToken))
            //{
            //    result.Message = "秘钥不正确，请重新登录";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);

            //}

            var gpsModel = new tbCaseGp()
            {
                csCode = mParams.CaseCode,
                devCode = mParams.DevCode,
                uCode = mParams.UserCode,
                gpsTargetType = mParams.GpsTargetType,
                gpsLatitude = Utils.ToDecimal(mParams.UserLatitude),
                gpsLongitude = Utils.ToDecimal(mParams.UserLongitude),
                //海拔速度，加速度
                //altitude = Utils.ToDecimal(mParams.Altitude),
                speed = Utils.ToDecimal(mParams.Speed),
                accelerationX = Utils.ToDecimal(mParams.AccelerationX),
                accelerationY = Utils.ToDecimal(mParams.AccelerationY),
                accelerationZ = Utils.ToDecimal(mParams.AccelerationZ)

            };
            if (gpsModel.gpsLatitude <= 0 || gpsModel.gpsLongitude <= 0)
            {
                result.Message = "经纬度不正确，更新失败";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //推送消息
            result = basemanage.AddGps(gpsModel);

            if (mParams.GpsTargetType.Trim().Equals("user"))
            {
                basemanage.updateUserGps(mParams.CaseCode, mParams.UserCode, mParams.UserLatitude, mParams.UserLongitude, mParams.UserPositionName,
                       mParams.Altitude, mParams.Speed, mParams.AccelerationX, mParams.AccelerationY, mParams.AccelerationZ
                    );
            }
            else
            {
                if (!string.IsNullOrEmpty(mParams.DeviceSN))
                    basemanage.updateDeviceGps(mParams.CaseCode, mParams.DeviceSN, mParams.UserLatitude, mParams.UserLongitude, mParams.UserPositionName,
                        mParams.Altitude, mParams.Speed, mParams.AccelerationX, mParams.AccelerationY, mParams.AccelerationZ
                        );
            }
            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            LogHelper.WriteInfo(typeof(DoController), "GPS recording return : " + JsonHelper.ObjectToJSON(result));
            return JsonConvert.SerializeObject(result);
        }



        /// <summary>
        /// 历史视频列表
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getMyVideoHistory([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.GetMyVideoHistory(mParams.UserCode, mParams.Skip, mParams.Take);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public String getMyDevice([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.getMyDevice(mParams.UserCode, mParams.Skip, mParams.Take);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }



        /// <summary>
        ///添加设备部署设备
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String addDevice([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.DeviceSN))
            {
                result.Message = "添加失败，设备序列码不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }



            result = basemanage.AddDevice(new tbDevice()
            {
                devGPeriod = 1,
                devCode = VIMS.LiveVideoSDK.Bll.Common.RandomIdGenerator.GetBase62(12),
                uCode = mParams.UserCode,
                devPhoto = mParams.PicCode,
                devStatus = mParams.DevStatus,
                devPDate = DateTime.Now,
                devSN = mParams.DeviceSN,
                devName = mParams.DevName,
                devType = mParams.DevType,

            });


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public String removeDevice([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.DevCode))
            {
                result.Message = "删除失败，设备编码不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }



            result = basemanage.removeDevice(mParams.DevCode);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public String useDevice([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.DeviceSN))
            {
                result.Message = "领用失败，设备编码不能为空。";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }



            result = basemanage.useDevice(mParams.UserCode, mParams.DeviceSN, mParams.CaseCode);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }

        [HttpPost]
        public String unUseDevice([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.DevCode))
            {
                result.Message = "领用失败，设备编码不能为空。";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }



            result = basemanage.unUseDevice(mParams.UserCode, mParams.DevCode, mParams.CaseCode);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }


        /// <summary>
        /// 获取案件部署，有成员也有设备
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCaseDeploy([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            var models = basemanage.getCaseDeploy(mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }




        /// <summary>
        /// 获取案预案信息
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCasePlan([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result.Message;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result.Message;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result.Message;

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件编号不能为空";
                result.IsSuccess = false;
                return result.Message;
            }


            var data = basemanage.getCasePlan(mParams.CaseCode);

            return data;
        }

        /// <summary>
        /// 获取最新版本 是android 还是ios
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx getNewestVersion([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get APP Latest Version: " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { Message = "" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.AppType))
            {
                result.IsSuccess = false;
                result.Message = "App版本类型不能为空";
                return result;
            }
            result.Entity = basemanage.getNewestVersion(mParams.AppType);
            if (result.Entity != null)
            {
                result.IsSuccess = true;
                result.ResultCode = "0";
                result.Message = "获取APP版本成功";
            }
            LogHelper.WriteInfo(typeof(DoController), "Get APP Latest Version Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }



        /// <summary>
        /// 错误日志  AppApiUrl： 接口地址 ，类型是android还是ios，AppVersionCode： app版本编号， MsgFile：日志文件fcode 编号
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        public string AddAppLog([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.AppVersionCode))
            {
                result.Message = "App版本编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.MsgFile))
            {
                result.Message = "错误日志文件编号不能为空，请先上传错误日志";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            var appLog = new tbAppLog()
            {
                appApiUrl = mParams.AppApiUrl,
                appType = mParams.AppType,
                appVersionCode = mParams.AppVersionCode,
                fCode = mParams.MsgFile,
            };

            result = basemanage.AddAppLog(appLog);

            return JsonConvert.SerializeObject(result);
        }




        /// <summary>
        /// v1.3获取案件部署的人及设备信息、
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCaseUsers([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "验证失败，案件编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            var models = basemanage.getCaseUsers(mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = true; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
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
        [HttpPost]
        public ResultEx getMyChatDetails([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }

            if (string.IsNullOrEmpty(mParams.MsgFromType))
            {
                result.Message = "MsgFromType 不能为空";
                result.IsSuccess = false;
                return result;
            }



            var models = basemanage.GetMyChatDetails(mParams.CaseCode, mParams.Skip, mParams.Take, mParams.FirstMsgId, mParams.MsgFromType, mParams.MsgToCode, mParams.MsgFromUserCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = models;
            }
            else { result.Entity = null; result.IsSuccess = true; result.Message = "没有数据"; }
            return result;
        }






        /// <summary>
        ///  新增讨论组
        /// </summary>
        /// <param name="uCode">用户编号</param>
        /// <param name="ApiToken">秘钥</param>
        /// <param name="CaseCode">案件编号</param>
        /// <param name="DisscusionName">讨论组名字</param>
        /// <param name="UserMenbers">讨论组成员</param>
        /// <returns></returns>
        public ResultEx addDiscussionGroup([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }


            //if (string.IsNullOrEmpty(mParams.CaseCode))
            //{
            //    result.Message = "案件编号不能为空";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}

            if (string.IsNullOrEmpty(mParams.DisscusionName))
            {
                result.Message = "讨论组的名字不能为空";
                result.IsSuccess = false;
                return result;

            }
            if (string.IsNullOrEmpty(mParams.UserMenbers))
            {
                result.Message = "讨论组成员不能为空";
                result.IsSuccess = false;
                return result;

            }

            result = basemanage.AddDiscussionGroup(mParams.UserCode, mParams.DisscusionName, mParams.CaseCode, mParams.UserMenbers);

            return result;
        }


        [HttpPost]
        public ResultEx getMyDiscussionGroup([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }

            var models = basemanage.GetMyDiscussionGroup(mParams.UserCode, mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = models;
            }
            else { result.Entity = ""; result.IsSuccess = true; result.Message = "没有数据"; }
            return result;
        }




        /// <summary>
        /// 搜索历史轨迹 输入用名字或者编号和查询的时间
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String searchGpsList([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            //if (string.IsNullOrEmpty(mParams.UserName))
            //{
            //    result.Message = "搜索用户名不能为空";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}
            //if (string.IsNullOrEmpty(mParams.StartTime))
            //{
            //    result.Message = "请输入开始shijain";
            //    result.IsSuccess = false;
            //    return JsonConvert.SerializeObject(result);
            //}

            var models = basemanage.SearchGpsList(mParams.UserName, mParams.StartTime, mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public String searchChatLists([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            var models = basemanage.SearchChatLists(mParams.UserCode, mParams.CaseCode, mParams.UserName, mParams.DisscusionName, mParams.StartTime, mParams.EndTime);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }






        /// <summary>
        /// 历史视频列表
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String searchVideoHistory([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.searchVideoHistory(mParams.UserCode, mParams.Skip, mParams.Take, mParams.UserName, mParams.DisscusionName, mParams.StartTime, mParams.EndTime, mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }




        [HttpPost]
        public ResultEx getDiscussionMenbers([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }
            if (string.IsNullOrEmpty(mParams.DisscusionCode))
            {
                result.Message = "讨论组编码不能为空";
                result.IsSuccess = false;
                return result;
            }


            var models = basemanage.getDiscussionMenbers(mParams.DisscusionCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = models;
            }
            else { result.Entity = ""; result.IsSuccess = true; result.Message = "没有数据"; }
            return result;
        }



        [HttpPost]
        public ResultEx deleteDiscussionMenber([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }
            if (string.IsNullOrEmpty(mParams.DisscusionCode))
            {
                result.Message = "讨论组编码不能为空";
                result.IsSuccess = false;
                return result;
            }

            //if (string.IsNullOrEmpty(mParams.DisscusionUserCode))
            //{
            //    result.Message = "删除人员编号不能为空";
            //    result.IsSuccess = false;
            //    return result;
            //}

            result = basemanage.deleteDiscussionMenber(mParams.DisscusionUserCode, mParams.DisscusionCode);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return result;
        }


        [HttpPost]
        public ResultEx addDiscussionMenbers([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }
            if (string.IsNullOrEmpty(mParams.DisscusionCode))
            {
                result.Message = "讨论组编码不能为空";
                result.IsSuccess = false;
                return result;
            }

            if (string.IsNullOrEmpty(mParams.DisscusionUserCode))
            {
                result.Message = "人员编号不能为空";
                result.IsSuccess = false;
                return result;
            }

            //if (string.IsNullOrEmpty(mParams.CaseCode))
            //{
            //    result.Message = "案件编号不能为空";
            //    result.IsSuccess = false;
            //    return result;
            //}

            result = basemanage.AddDiscussionMenbers(mParams.DisscusionUserCode, mParams.DisscusionCode, mParams.UserCode, mParams.CaseCode);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return result;
        }




        /// <summary>
        /// 获取所有案件的
        /// </summary>
        /// <param name="status">案件状态，如已立案，已结案， 空的话忽略查询所有</param>
        /// <param name="caseName">案件名称，  空的话忽略查询所有</param>
        /// <param name="caseType">案件类别，  空的话忽略查询所有</param>
        /// <returns></returns>
        [HttpPost]
        public String getAllCases([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            var models = basemanage.GetAllCases(mParams.UserCode, mParams.CaseStatus, mParams.CaseName, mParams.CaseType, mParams.RName);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }



        //// v.1.3.版本 新增的方法
        /// <summary>
        /// 获取个人任务任务状态0执行中，1取消任务，2完成任务
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCasePersonMissions([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.getCasePersonMissions(mParams.CaseCode, mParams.UserCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }

        /// <summary>
        /// 获取管理员任务 任务状态0执行中，1取消任务，2完成任务
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCaseAdminMissions([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.getCaseAdminMissions(mParams.CaseCode, mParams.UserCode, mParams.TaskStatus);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }



        /// <summary>
        /// 发布任务
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String publishMission([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "案件编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            if (string.IsNullOrEmpty(mParams.MissionUserCodes))
            {
                result.Message = "任务执行人不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }


            if (string.IsNullOrEmpty(mParams.MissionLimitTime))
            {
                result.Message = "任务限制时间";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }

            if (string.IsNullOrEmpty(mParams.ToLatitude))
            {
                result.Message = "纬度不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (string.IsNullOrEmpty(mParams.ToLongitude))
            {
                result.Message = "经度不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }





            result = basemanage.publishMission(mParams);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }






        /// <summary>
        /// 获取案件部署的所有人
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getCaseAllUsers([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }


            var models = basemanage.getCaseDeployUsers(mParams.CaseCode);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }




        /// <summary>
        /// 更新讨论组名字
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx updateDiscussionName([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return result;
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return result;
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return result;

            }

            if (string.IsNullOrEmpty(mParams.DisscusionCode))
            {
                result.Message = "讨论组编号不能为空";
                result.IsSuccess = false;
                return result;
            }


            if (string.IsNullOrEmpty(mParams.DisscusionName))
            {
                result.Message = "讨论组的名字";
                result.IsSuccess = false;
                return result;
            }





            result = basemanage.updateDiscussionName(mParams);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return result;
        }




        /// <summary>
        /// 搜索历史
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String searchHistory([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.SearchType))
            {
                result.Message = "搜索类型不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "案件编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            var models = basemanage.searchHistory(mParams);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }


        /// <summary>
        /// 根据虚拟id获取文件，可以是图片或者其他后缀的，id为虚拟id VirturalId
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getFiles([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.virtualId))
            {
                result.Message = "virtualId，不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            var models = basemanage.getFiles(mParams);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }




        /// <summary>
        /// 根据虚拟id获取文件，可以是图片或者其他后缀的，id为虚拟id VirturalId
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public String getMyCollections([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            var models = basemanage.getMyCollections(mParams);
            if (models != null && models.Any())
            {
                result.IsSuccess = true; result.ResultCode = "1"; result.Message = "获取成功";
                result.Entity = JsonConvert.SerializeObject(models);
            }
            else { result.Entity = ""; result.IsSuccess = false; result.Message = "没有数据"; }
            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public String deleteCollection([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CollectionId))
            {
                result.Message = "收藏id不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }


            result = basemanage.deleteCollection(mParams);


            //result.Entity = JsonConvert.SerializeObject(result.Entity);
            return JsonConvert.SerializeObject(result);
        }


        /// <summary>
        ///  虚拟添加直播后的视频文件
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public string addCollection([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.MessageId))
            {
                result.Message = "消息id不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CaseCode))
            {
                result.Message = "案件编号不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            result = basemanage.addCollection(mParams);

            return JsonConvert.SerializeObject(result);
        }


        [HttpPost]
        public string updateCollectionLabelName([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.CollectionId))
            {
                result.Message = "收藏的id不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            if (string.IsNullOrEmpty(mParams.CollectionLabelName))
            {
                result.Message = "修改的标签不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }
            result = basemanage.updateCollectionLabelName(mParams);

            return JsonConvert.SerializeObject(result);
        }




        [HttpPost]
        public string cancelPersonMission([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.PersonMissionId))
            {
                result.Message = "cancelPersonMission不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            result = basemanage.cancelPersonMission(mParams);

            return JsonConvert.SerializeObject(result);
        }

        [HttpPost]
        public string cancelMisson([FromBody]ApiParams mParams)
        {
            var result = new ResultEx() { Message = "秘钥过期，请重新登录" };
            var model = new UserModel();
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(mParams.ApiToken))
            {
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(mParams.UserCode);
            if (string.IsNullOrEmpty(myToken))
            {
                result.Message = "验证失败，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);
            }
            if (!myToken.Equals(mParams.ApiToken))
            {
                result.Message = "秘钥不正确，请重新登录";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            if (string.IsNullOrEmpty(mParams.MissionId))
            {
                result.Message = "MissionId不能为空";
                result.IsSuccess = false;
                return JsonConvert.SerializeObject(result);

            }

            result = basemanage.cancelMisson(mParams);

            return JsonConvert.SerializeObject(result);
        }

        [HttpPost]
        public ResultEx getAPPGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get APPGroup : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取APP组成功" };
            var basemanage = new BaseManage();
            result.Entity = basemanage.getAPPGroup(mParams.DisscusionCode);
            if (result.Entity == null)
            {
                result.IsSuccess = false;
                result.ResultCode = "-1";
                result.Message = "无对应APP组信息";
            }
            LogHelper.WriteInfo(typeof(DoController), "Get APPGroup : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getAPPGroupWithUser([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get APPGroup : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取APP组成功" };
            var basemanage = new BaseManage();
            result.Entity = basemanage.getAPPGroupWithUser(mParams.DisscusionCode);
            if (result.Entity == null)
            {
                result.IsSuccess = false;
                result.ResultCode = "-1";
                result.Message = "无对应APP组信息";
            }
            LogHelper.WriteInfo(typeof(DoController), "Get APPGroup : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getPdtGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get PDTGroup : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取PDT组成功" };
            try
            {
                var basemanage = new BaseManage();
                string pdtGrp = basemanage.getPdtGroup(mParams.DisscusionCode);
                if (String.IsNullOrEmpty(pdtGrp))
                {
                    result.IsSuccess = true;
                    result.ResultCode = "0";
                    result.Entity = mParams.DisscusionCode;
                    result.Message = "非融合组，返回APP组号";
                }
                else
                {
                    result.Entity = pdtGrp;
                }
            }
            catch(Exception ex)
            {
                result.Exception = ex.Message;
            }
            LogHelper.WriteInfo(typeof(DoController), "Get PDTGroup return : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx switchGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Switch Group : " + JsonHelper.ObjectToJSON(mParams));
            var basemanage = new BaseManage();
            basemanage.logEvent(mParams.UserCode, mParams.UserCode, "SwitchGroup", "切换组" + mParams.DisscusionCode, "APP");
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "切换组失败" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            Boolean isSuc = basemanage.switchGroup(mParams.UserCode, mParams.DisscusionCode, mParams.DisscusionName);
            if (isSuc)
            {
                result.IsSuccess = true;
                result.ResultCode = "0";
                result.Message = "切换组成功";
            }
            LogHelper.WriteInfo(typeof(DoController), "Switch Group return : " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx searchUser([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Search User : " + JsonHelper.ObjectToJSON(mParams));
            var basemanage = new BaseManage();
            basemanage.logEvent(mParams.UserCode, mParams.UserCode, "SearchUser", "查询用户信息|" + mParams.dCode + "|" +mParams.DisscusionCode, "APP");
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取个人信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            /*
            if (String.IsNullOrEmpty(mParams.dCode))
            {
                result.Message = "请输入部门代码进行查询！";
                result.IsSuccess = false;
                return result;
            }
            */
            try
            {
                var userList = basemanage.searchUser(mParams.UserCode, mParams.UserName, mParams.DisscusionCode, mParams.dCode, mParams.PageNum, mParams.PageSize, mParams.UserStatus);
                result.Entity = userList;
            }
            catch (Exception e)
            {
                LogHelper.WriteError(typeof(DoController), "Search User with Error: ", e);
                result.Message = "Search User Error";
                result.IsSuccess = false;
                return result;
            }
            
            LogHelper.WriteInfo(typeof(DoController), "Search User Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getUserGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get user Group : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取用户组信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            var basemanage = new BaseManage();
            if ("1".Equals(mParams.Isdefault))
            {
                var userGroups = basemanage.getUserGroup(mParams.UserCode, mParams.Isdefault, mParams.DisscusionCode, mParams.DisscusionName, mParams.PageNum, mParams.PageSize);
                result.Entity = userGroups;
            } else
            {
                if ("1".Equals(mParams.Isrank))
                {
                    var userGroups = basemanage.getRank(mParams.UserCode, mParams.Isdefault, mParams.DisscusionCode, mParams.rankNo, mParams.DisscusionName, mParams.PageNum, mParams.PageSize);
                    result.Entity = userGroups;
                }
                else
                {
                    var userGroups = basemanage.getRankUserGroup(mParams.UserCode, mParams.Isdefault, mParams.DisscusionCode, mParams.rankNo, mParams.DisscusionName, mParams.PageNum, mParams.PageSize);
                    result.Entity = userGroups;
                }
            }
            LogHelper.WriteInfo(typeof(DoController), "Get user Group Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getDeptTree([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get Depts : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取部门信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            var basemanage = new BaseManage();
            var depts = basemanage.getDepts(mParams.dCode);
            result.Entity = depts;
            LogHelper.WriteInfo(typeof(DoController), "Get Depts Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getUserGps([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get User Gps : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取用户定位信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            var basemanage = new BaseManage();
            var gpsList = basemanage.getUserGps(mParams.UserName, mParams.dCode, mParams.DisscusionCode, mParams.PageNum, mParams.PageSize);
            result.Entity = gpsList;
            LogHelper.WriteInfo(typeof(DoController), "Get User Gps Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getAudioMsg([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get Audio Msg : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取语音数据成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            var basemanage = new BaseManage();
            //string格式有要求，必须是yyyy - MM - dd hh: mm: ss
            var gpsList = basemanage.getAudoMsg(mParams.MsgFromType, mParams.RName, Convert.ToDateTime(mParams.StartTime), Convert.ToDateTime(mParams.EndTime), mParams.PageNum, mParams.PageSize);
            result.Entity = gpsList;
            LogHelper.WriteInfo(typeof(DoController), "Get Audio Msg Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx pushRecording([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Push Recording : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = false, ResultCode = "-1", Message = "语音记录失败" };
            try
            {
                var basemanage = new BaseManage();
                TimeSpan ts = new TimeSpan(0, 0, mParams.second / 1000);
                string second = ts.Hours.ToString().PadLeft(2, '0') + ":" + ts.Minutes.ToString().PadLeft(2, '0') + ":" + ts.Seconds.ToString().PadLeft(2, '0');
                int isSuc = basemanage.pushRecording(mParams.UserCode, mParams.MsgType, mParams.MsgFile, mParams.MsgFromType, mParams.MsgToCode, second, mParams.virtualId);
                if (isSuc > 0)
                {
                    result.IsSuccess = true;
                    result.ResultCode = "0";
                    result.Message = "语音记录成功";
                }
            }
            catch (Exception ex)
            {
                result.IsSuccess = false;
                result.ResultCode = "-1";
                result.Exception = ex.InnerException.Message;
            }
            LogHelper.WriteInfo(typeof(DoController), "Push Recording Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public void getOneUserId()
        {
            LogHelper.WriteInfo(typeof(DoController), "Get User ID : " + HttpContext.Current.Request.Params.ToString());
            this.getUserId();
        }

        [HttpPost]
        public void getUserId()
        {
            string userId = HttpContext.Current.Request.Headers.Get("user");
            userId = HttpContext.Current.Request.Params["user"];
            if (userId == null)
            {
                userId = "";
            }
            LogHelper.WriteInfo(typeof(DoController), "Get User ID : " + userId);
            StringBuilder sb = new StringBuilder(50);
            sb.Append("<document type=\"freeswitch / xml\">");
            sb.Append("<section name=\"directory\">");
            sb.Append("<domain name=\"$${domain}\">");
            sb.Append("<params>");
            sb.Append("<param name=\"dial-string\" value=\"{^^:sip_invite_domain=${dialed_domain}:presence_id=${dialed_user}@${dialed_domain}}${sofia_contact(*/${dialed_user}@${dialed_domain})},${verto_contact(${dialed_user}@${dialed_domain})}\"/>");
            sb.Append("<param name=\"jsonrpc - allowed - methods\" value=\"verto\"/>");
            sb.Append("</params>");
            sb.Append("<variables>");
            sb.Append("<variable name=\"record_stereo\" value=\"true\"/>");
            sb.Append("<variable name=\"default_gateway\" value=\"$${default_provider}\"/>");
            sb.Append("<variable name=\"default_areacode\" value=\"$${default_areacode}\"/>");
            sb.Append("<variable name=\"transfer_fallback_extension\" value=\"operator\"/>");
            sb.Append("</variables>");
            sb.Append("<groups>");
            sb.Append("<group name=\"default\">");
            sb.Append("<users>");
            
            var basemanage = new BaseManage();
            List<tbUser> userIds = basemanage.getUserIds(userId);
            foreach (tbUser id in userIds)
            {
                sb.Append("<user id=\"").Append(id.uCode).Append("\">");
                sb.Append("<params>");
                sb.Append("<param name=\"type\" value=\"" + id.devicetype + "\"/>");
                sb.Append("<param name=\"pdt-deviceid\" value=\"" + id.deviceid + "\"/>");
                sb.Append("<param name=\"password\" value=\"$${default_password}\"/>");
                sb.Append("</params>");
                sb.Append("</user>");
            }
            sb.Append("</users>");
            sb.Append("</group>");
            sb.Append("</groups>");
            sb.Append("</domain>");
            sb.Append("</section>");
            sb.Append("</document>");
            LogHelper.WriteInfo(typeof(DoController), "Manager User Return: " + sb.ToString());
            HttpContext.Current.Response.ContentType = "application/xml";
            HttpContext.Current.Response.Write(sb.ToString());
            HttpContext.Current.Response.End();
        }

        [HttpPost]
        public ResultEx mgntUser([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Manager User : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "命令发送成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            string action = mParams.userAction;
            string eventName = "";
            switch (action)
            {
                case "stop":
                    stop_user(mParams.UserCode, mParams.MsgToCode);
                    eventName = "遥晕";
                    break;
                case "kill":
                    kill_user(mParams.UserCode, mParams.MsgToCode);
                    eventName = "遥毙";
                    break;
                case "disable":
                    disable_user(mParams.UserCode, mParams.MsgToCode);
                    eventName = "禁用";
                    break;
                case "enable":
                    enable_user(mParams.MsgToCode);
                    eventName = "启用";
                    break;
            }
            var basemanage = new BaseManage();
            basemanage.logEvent(mParams.UserCode, mParams.UserCode, eventName, eventName, "APP");
            LogHelper.WriteInfo(typeof(DoController), "Manager User Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        private string pdtbiscommand = ConfigurationManager.AppSettings["pdtbiscommand"];

        private void stop_user(string userCode, string msgToCode)
        {
            string command = String.Format(pdtbiscommand, msgToCode, 0);
            HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
            WebResponse webResp = webReq.GetResponse();
            string respResult = this.getRespResult(webResp);
            int resp = this.parseResp(respResult);
            if (resp == 0)
            {
                var basemanage = new BaseManage();
                basemanage.updateUserStatus(msgToCode, "遥晕");
            }
        }

        private void kill_user(string userCode, string msgToCode)
        {
            string command = String.Format(pdtbiscommand, msgToCode, 1);
            HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
            WebResponse webResp = webReq.GetResponse();
            string respResult = this.getRespResult(webResp);
            int resp = this.parseResp(respResult);
            if (resp == 0)
            {
                var basemanage = new BaseManage();
                basemanage.updateUserStatus(msgToCode, "遥毙");
            }
        }

        private void disable_user(string userCode, string msgToCode)
        {
            var basemanage = new BaseManage();
            basemanage.updateUserStatus(msgToCode, "禁用");
            ApiParams param = new ApiParams();
            param.UserCode = userCode;
            param.MsgType = MsgTypes.Command.ToString();
            param.MsgContent = "offline";
            param.MsgFromType = MsgFromTypes.Person.ToString();
            param.MsgToCode = msgToCode;
            this.pushCommand(param);
        }

        private void enable_user(string msgToCode)
        {
            var basemanage = new BaseManage();
            var user = basemanage.getPdtUser(msgToCode);
            if (user != null)
            {
                string command = String.Format(pdtbiscommand, msgToCode, 2);
                HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(command);
                WebResponse webResp = webReq.GetResponse();
                string respResult = this.getRespResult(webResp);
                int resp = this.parseResp(respResult);
                if (resp == 0)
                {
                    basemanage.updateUserStatus(msgToCode, "在线");
                }
            }
            else
            {
                basemanage.updateUserStatus(msgToCode, "离线");
            }
        }

        private string getRespResult(WebResponse webResp)
        {
            Stream stream = webResp.GetResponseStream();
            StreamReader sr = new StreamReader(stream);
            string resp = sr.ReadToEnd();
            return resp;
        }

        private int parseResp(string respResult)
        {
            Dictionary<string, string> result = JsonHelper.JSONToObject<Dictionary<string, string>>(respResult);
            if (result == null)
            {
                return -1;
            }
            string status = result["status"];
            return Convert.ToInt32(status);
        }

        [HttpPost]
        public ResultEx getPermission([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get Permission : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取APP权限成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            try
            {
                var basemanage = new BaseManage();
                result.Entity = basemanage.getPermission(Convert.ToInt32(mParams.RName));
            }
            catch (Exception ex)
            {
                result.Message = "获取APP权限失败";
                result.IsSuccess = false;
                result.Exception = ex.InnerException.Message;
                return result;
            }
            LogHelper.WriteInfo(typeof(DoController), "Get Permission Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx userLogout([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "User Logout : " + JsonHelper.ObjectToJSON(mParams));
            var basemanage = new BaseManage();
            basemanage.logEvent(mParams.UserCode, mParams.UserCode, "Logout", "退出登陆", "APP");
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "退出成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                return result;
            }
            var isSuc = basemanage.userLogout(mParams.UserCode);
            if (isSuc < 0)
            {
                result.Message = "退出失败";
                result.IsSuccess = false;
                return result;
            }
            LogHelper.WriteInfo(typeof(DoController), "User Logout Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx getUserByDeviceId([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get User By Device ID : " + JsonHelper.ObjectToJSON(mParams));
            var basemanage = new BaseManage();
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var user = basemanage.getUserByDeviceId(mParams.DeviceId);
            if (user == null)
            {
                result.Message = "获取失败，无对应用户";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            result.Entity = user.uCode;
            LogHelper.WriteInfo(typeof(DoController), "Get User By Device ID Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx resetPwd([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Reset Pwd : " + JsonHelper.ObjectToJSON(mParams));
            var basemanage = new BaseManage();
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "重置密码成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            if (String.IsNullOrEmpty(mParams.UserPwd))
            {
                result.Message = "新密码不能为空";
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            LogHelper.WriteInfo(typeof(DoController), "Reset Pwd Return: " + JsonHelper.ObjectToJSON(result));
            return basemanage.resetPwd(mParams.UserCode, mParams.UserOldPwd, mParams.UserPwd);
        }

        [HttpPost]
        public ResultEx heartbeat([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Heart Beat : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "接收到心跳" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            basemanage.updateHeartbeat(mParams.UserCode);
            LogHelper.WriteInfo(typeof(DoController), "Heart Beat Return: " + JsonHelper.ObjectToJSON(result));
            return result;
        }

        [HttpPost]
        public ResultEx chargeEnable([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Cut Enable : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "允许抢话" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            result.IsSuccess = basemanage.chargeEnable(mParams.Take, mParams.UserCode);
            return result;
        }

        [HttpPost]
        public ResultEx getAudioDetail([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Audio Detail : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取语音详情成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            result.Entity = basemanage.getAudioDetail(mParams.virtualId);
            return result;
        }

        [HttpPost]
        public ResultEx getIMStatus()
        {
            LogHelper.WriteInfo(typeof(DoController), "IM Status ");
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "IM状态成功" };
            var basemanage = new BaseManage();
            if (basemanage.imStatus() < 0)
            {
                result.IsSuccess = false;
                result.ResultCode = "-1";
                result.Message = "IM状态失败";
            }
            return result;
        }

        [HttpPost]
        public ResultEx getAppUserID([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get App User ID " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取UserID成功" };
            var basemanage = new BaseManage();
            var tbUser = basemanage.getAppUserID(mParams.pdtSSI, mParams.type);
            if (tbUser == null)
            {
                result.IsSuccess = false;
                result.ResultCode = "-1";
                result.Message = "获取UserID失败";
            }
            else
            {
                result.IsSuccess = true;
                result.ResultCode = "0";
                result.Entity = tbUser.uCode;
                result.Message = "获取UserID成功";
            }
            return result;
        }

        [HttpPost]
        public ResultEx setDefaultGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Set default group : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "设置/取消常用组成功" };
            var basemanage = new BaseManage();
            basemanage.setDefaultGroup(mParams.UserCode, mParams.DisscusionCode, mParams.Isdefault);
            return result;
        }

        // New api for app operation
        [HttpPost]
        public ResultEx getGroupByType([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get default group by type : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取组列表成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            IList<UserGroupModel> groupList = basemanage.getGroupByType(mParams.type, mParams.DisscusionCode, mParams.PageNum, mParams.PageSize);
            result.Entity = groupList;
            return result;
        }

        [HttpPost]
        public ResultEx groupMgnt([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Group Management : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "维护组信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            resultMsg = basemanage.groupMgnt(mParams.userAction, mParams.DisscusionCode, mParams.DisscusionName, mParams.relativegroupid, mParams.relativegroup, mParams.type, mParams.clazz, mParams.status, mParams.deptId, mParams.deptName);
            if (!String.IsNullOrEmpty(resultMsg))
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            return result;
        }

        [HttpPost]
        public ResultEx rankMgnt([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Rank Management : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "维护组群信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            resultMsg = basemanage.rankMgnt(mParams.userAction, mParams.rankNo, mParams.rankName);
            if (!String.IsNullOrEmpty(resultMsg))
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            return result;
        }

        [HttpPost]
        public ResultEx rankGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Rank Group Searching : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "查询组群组信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            var groupList = basemanage.rankGroup(mParams.userAction, mParams.rankNo, mParams.DisscusionCode, mParams.PageNum, mParams.PageSize);
            if (groupList == null)
            {
                result.Message = "查询组群组信息失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            else
            {
                result.Entity = groupList;
            }
            return result;
        }

        [HttpPost]
        public ResultEx rankGroupMgnt([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Rank Group Management : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "维护组群组信息成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            resultMsg = basemanage.rankGroupMgnt(mParams.userAction, mParams.rankNo, mParams.rankName, mParams.DisscusionCode, mParams.DisscusionName);
            if (!String.IsNullOrEmpty(resultMsg))
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            return result;
        }

        [HttpPost]
        public ResultEx getDefaultGroup([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Get Default Group  : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "获取常用组列表成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            var groupList = basemanage.getDefaultGroup(mParams.DisscusionUserCode, mParams.PageNum, mParams.PageSize);
            if (groupList == null)
            {
                result.Message = "查询查询常用组失败";
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            else
            {
                result.Entity = groupList;
            }
            return result;
        }

        [HttpPost]
        public ResultEx defaultGroupMgnt([FromBody]ApiParams mParams)
        {
            LogHelper.WriteInfo(typeof(DoController), "Default Group Management  : " + JsonHelper.ObjectToJSON(mParams));
            var result = new ResultEx() { IsSuccess = true, ResultCode = "0", Message = "维护常用组列表成功" };
            var resultMsg = this.validateToken(mParams.UserCode, mParams.ApiToken);
            if (resultMsg != null)
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
                return result;
            }
            var basemanage = new BaseManage();
            resultMsg = basemanage.defaultGroupMgnt(mParams.DisscusionUserCode, mParams.DisscusionCode, mParams.userAction);
            if (!String.IsNullOrEmpty(resultMsg))
            {
                result.Message = resultMsg;
                result.IsSuccess = false;
                result.ResultCode = "-1";
            }
            return result;
        }
        // New api for app operation

        private string validateToken(string account, string apiToken)
        {
            var basemanage = new BaseManage();
            if (string.IsNullOrEmpty(apiToken))
            {
                return "密钥为空，请重新登录";
            }
            //验证秘钥
            var myToken = basemanage.GetMyToke(account);
            if (string.IsNullOrEmpty(myToken))
            {
                return "验证失败，请重新登录";
            }
            if (!myToken.Equals(apiToken))
            {
                return "秘钥不正确，请重新登录";
            }
            return null;
        }
    }
}