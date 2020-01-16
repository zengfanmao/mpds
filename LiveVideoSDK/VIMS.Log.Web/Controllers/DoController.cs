using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using VIMS.LiveVideoSDK.Model;
using VIMS.LiveVideoSDK.Core;
using VIMS.LiveVideoSDK.Bll.Entity;
using VIMS.LiveVideoSDK.Bll.Common;
using System.Threading.Tasks;
using System.Web;
using System.IO;
using System.Collections;

namespace VIMS.Web.Controllers
{
    public class DoController : ApiController
    {
        public class Params
        {
            public String action { get; set; }
            public String message { get; set; }
            public String userCode { get; set; }
            public String userName { get; set; }
            public String android_system { get; set; }
            public String android_version { get; set; }
            public String app_version { get; set; }

        }
        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="mParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ResultEx sendLog([FromBody] Params mParams)
        {
            ResultEx resultEx = new ResultEx();
            try
            {
                if (mParams != null)
                {

                    var formatStr = Utils.GetAppSetting("FileFormat");
                    var root = Utils.GetAppSetting("FilePath");
                    string userCode = String.IsNullOrEmpty(mParams.userCode) ? "noUserCode" : mParams.userCode;
                    var path = HttpContext.Current.Server.MapPath(root + "/" + userCode + "/" + Utils.Format("", formatStr));
                    if (!Directory.Exists(Path.GetDirectoryName(path)))
                    {
                        Directory.CreateDirectory(Path.GetDirectoryName(path));
                    }

                    //StreamWriter类，以流的方式写入

                    using (System.IO.StreamWriter file = new System.IO.StreamWriter(path, true))
                    {

                        file.WriteLine("--------------time::" + DateTime.Now.ToString("yyyyMMdd HH:mm:ss") + "--------------");
                        file.WriteLine("action:" + mParams.action);
                        file.WriteLine("userCode:" + mParams.userCode);
                        file.WriteLine("userName:" + mParams.userName);
                        file.WriteLine("android_version:" + mParams.android_version);
                        file.WriteLine("android_system:" + mParams.android_system);
                        file.WriteLine("app_version:" + mParams.app_version);
                        file.WriteLine("message:" + mParams.message);
                        file.Flush();
                        file.Close();
                    }
                }
                resultEx.IsSuccess=true;
                resultEx.Message = "日志记录成功";
                resultEx.ResultCode = "1";
            }
            catch (Exception ex){
                resultEx.IsSuccess = false;
                resultEx.Message = "日志记录失败："+ ex.Message;
                resultEx.ResultCode = "-1";


            }
            return resultEx;

        }
       
        [HttpPost]
        public async Task<ResultEx> PostFile()
        {
            var result = new ResultEx() { Message = "上传失败", IsSuccess = false, ResultCode = "-1" };
            //var basemanage = new BaseManage();
            ResultEx addFileResult = null;
            // 检查是否是 multipart/form-data
            if (!Request.Content.IsMimeMultipartContent("form-data"))
            {
                result.Message = "数据传输方式不正确，mine类型没有指定";
                return result;
            }
            try
            {
                var formatStr = Utils.GetAppSetting("LiveVideoSDK_FileFormat");
                var root = Utils.GetAppSetting("LiveVideoSDK_FilePath");
                var path = HttpContext.Current.Server.MapPath(root + Utils.Format("", formatStr));
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

                var siteRoot = HttpContext.Current.Request.Url.GetLeftPart(UriPartial.Authority).ToString();
                var fileUrl = root.Replace(@"~", "") + Utils.Format("", formatStr) + provider.FileName;
                var response = Request.CreateResponse(HttpStatusCode.Accepted);
                var fileinfo = new FileInfo(path + provider.FileName);

                var virtualId = HttpContext.Current.Request.QueryString["virtualId"];
                virtualId = string.IsNullOrEmpty(virtualId) ? "" : virtualId;
                /*
                addFileResult = basemanage.AddFile(new tbFileDetail()
                {
                    virtualId = virtualId,
                    fName = provider.FileName,
                    fSize = fileinfo.Length / 1024,
                    fRelativePath = fileUrl,
                    fCode = RandomIdGenerator.GetBase62(14),
                });
                */
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

            return result; ;
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
                var extens= Utils.GetAppSetting("LiveVideoSDK_Uplaod_Extends"); ;
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


      
    }
}