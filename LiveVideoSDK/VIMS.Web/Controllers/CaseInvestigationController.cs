
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Mvc;
using VIMS.DataModel.CaseInvestigation;
using VIMS.DataModel.SystemManagement;
using VIMS.Services.CaseInvestigation;

namespace VIMS.Web.Controllers
{
    public class CaseInvestigationController : Controller
    {
        private ICasesManageService c = null;

        public CaseInvestigationController(ICasesManageService _c)
        {
            c = _c;
        }
        public class Code
        {
            public string _code { get; set; }
            public int _result { get; set; }
        }  
        //案件管理页面
        public ActionResult Index()
        {
            return View();
        }
        //添加案件信息
        public JsonResult AddCases()
        {
            string csType = Request["_csType"].ToString(),
                       csName = Request.Form["_csName"].ToString(),
                       csNumber = Request.Params["_csNumber"].ToString(),
                       csStates = Request.Params["_csStates"].ToString(),
                       //csAccept = Request.Params["_csAccept"].ToString(),
                       //csTel = Request.Params["_csTel"].ToString(),
                       //csContact = Request.Params["_csContact"].ToString(),
                       //createUser = Request.Params["_createUser"].ToString(),
                       incidenceLocation = Request.Params["_incidenceLocation"].ToString(),
                       incidenceTime = Request.Params["_incidenceTime"].ToString(),
                       csDesc = Request.Params["_csDesc"].ToString();
                Cases cModel = new Cases();
                cModel.Id = Guid.NewGuid();
                cModel.csType = csType;
                cModel.csName = csName;
                cModel.csCode = csNumber;
                cModel.csStatus = csStates;
                //cModel.csAccept = csAccept;
                //cModel.csTel = csTel;
                //cModel.csContact = csContact;
                cModel.csAddress = incidenceLocation;
                cModel.csTime = Convert.ToDateTime(incidenceTime);
                cModel.uCode = JQSoft.AppContext.Current.User.SystemName;
                cModel.csDesc = csDesc;
                c.Insert(cModel);
                List<Cases> codeList = new List<Cases>()
                    {
                        new Cases {
                            Id=cModel.Id}
                    };

                //直接返回此类型JSON类型  
                return Json(codeList);
        }
        //获取案件编码
        public JsonResult GetCode()
        {
            var code = c.GetCode("CAS", "440000000000");
            //添加code数据  
            List<Code> codeList = new List<Code>()  
            {  
                new Code {_code=code}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //加载更新案件数据
        public JsonResult GetUpdateCases()
        {
            var _csId = Request["_csId"];
            Guid myguid = new Guid(_csId);//转换类型id
            var list = c.GetCasesForId(myguid);
            //添加数据  
            List<Cases> codeList = new List<Cases>()  
            {  
                new Cases {
                    Id=myguid,
                    csType=list.csType,
                    csName=list.csName,
                    csCode=list.csCode,
                    csStatus=list.csStatus,
                    csAccept=list.csAccept,
                    csTel=list.csTel,
                    csContact=list.csContact,
                    uCode=list.uCode,
                    csAddress=list.csAddress,
                    csTime=list.csTime,
                    csDesc=list.csDesc,
                    csPlan=list.csPlan
                }
            };
            return Json(codeList);
        }
        //删除案件
        public JsonResult Delete()
        {
            var _csCode = Request.QueryString["_csCode"];
            c.deleteCaseInfo(_csCode);
            List<Cases> codeList = new List<Cases>()
                    {
                        new Cases {
                            csCode=_csCode}
                    };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //结案
        public JsonResult Compelete()
        {
            var _csId = Request.QueryString["_csId"];
            Guid myguid = new Guid(_csId);//转换类型id
            Cases cs = new Cases();
            cs.Id = myguid;
            cs.csStatus = "已结案";
            c.Update(cs);
            List<Cases> codeList = new List<Cases>()
                    {
                        new Cases {
                            Id=cs.Id}
                    };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //根据案件代号加载案件数据
        public JsonResult GetCasesForCode()
        {
            var _csCode = Request.QueryString["_csCode"];
            Cases cs = new Cases();
            cs.csCode = _csCode;
            var list = c.GetCasesForCode(cs);
            //添加数据  
            List<Cases> codeList = new List<Cases>();
            if (list.Count > 0)
            {
                foreach (var _c in list)
                {
                    Cases cases = new Cases();
                       cases.Id=_c.Id;
                    cases.csType=_c.csType;
                    cases.csName=_c.csName;
                    cases.csCode=_c.csCode;
                    cases.csStatus=_c.csStatus;
                    cases.csAccept=_c.csAccept;
                    cases.csTel=_c.csTel;
                    cases.csContact=_c.csContact;
                    cases.uCode=_c.uCode;
                    cases.csDesc=_c.csDesc;
                    cases.csPlan = _c.csPlan;
                    codeList.Add(cases);
                }
            }
            return Json(codeList);
        }
        //添加涉案人员信息
        public JsonResult AddCasesPerson()
        {
            string csNumber = Request.Params["_csNumber"].ToString(),
                   name = Request["_name"].ToString(),
                   byName = Request.Form["_byName"].ToString(),
                   selSet = Request.Params["_selSet"].ToString(),
                   age = Request.Params["_age"].ToString(),
                   height = Request.Params["_height"].ToString(),
                   //photo = Request.Params["_photo"].ToString(),
                   nation = Request.Params["_nation"].ToString(),
                   country = Request.Params["_country"].ToString(),
                   workUnit = Request.Params["_workUnit"].ToString(),
                   post = Request.Params["_post"].ToString(),
                   pId = Request.Params["_id"].ToString(),
                   feature = Request.Params["_feature"].ToString(),
                   dress = Request.Params["_dress"].ToString();
            CaseSuspects cSubModel = new CaseSuspects();
            cSubModel.pCode = "123";
            cSubModel.pName = name;
            cSubModel.pByname = byName;
            cSubModel.pSex = byte.Parse(selSet);
            if (!string.IsNullOrEmpty(age))
            {
                cSubModel.spAge = byte.Parse(age);
            }
            if (!string.IsNullOrEmpty(height))
            {
                cSubModel.spHeight = short.Parse(height);
            }
            //cSubModel.pPhoto = photo;
            cSubModel.pNation = nation;
            cSubModel.pCountry = country;
            cSubModel.pAddress = workUnit;
            cSubModel.pCareer = post;
            cSubModel.pIdNum = pId;
            cSubModel.spFeature = feature;
            cSubModel.pDress = dress;
            cSubModel.csCode = csNumber;
            cSubModel.Id = Guid.NewGuid();
            c.suspectInsert(cSubModel, csNumber);
            List<CaseSuspects> codeList = new List<CaseSuspects>()  
            {  
                new CaseSuspects {csCode=cSubModel.csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //更新案件信息
        public JsonResult UpdateCases()
        {
                string csId = Request["_hideId"].ToString(),
                       csType = Request["_csType"].ToString(),
                       csName = Request.Form["_csName"].ToString(),
                       csNumber = Request.Params["_csNumber"].ToString(),
                       csStates = Request.Params["_csStates"].ToString(),
                       //csAccept = Request.Params["_csAccept"].ToString(),
                       //csTel = Request.Params["_csTel"].ToString(),
                       //csContact = Request.Params["_csContact"].ToString(),
                       //createUser = Request.Params["_createUser"].ToString(),
                       incidenceLocation = Request.Params["_incidenceLocation"].ToString(),
                       incidenceTime = Request.Params["_incidenceTime"].ToString(),
                       csDesc = Request.Params["_csDesc"].ToString();
                Guid hideId = new Guid(csId);
                Cases cModel = new Cases();
                cModel.Id = hideId;
                cModel.csType = csType;
                cModel.csName = csName;
                cModel.csCode = csNumber;
                cModel.csStatus = csStates;
                //cModel.csAccept = csAccept;
                //cModel.csTel = csTel;
                //cModel.csContact = csContact;
                //cModel.uCode = createUser;
                cModel.csAddress = incidenceLocation;
                cModel.csTime = Convert.ToDateTime(incidenceTime);
                cModel.csDesc = csDesc;
                c.Update(cModel);

                List<Cases> codeList = new List<Cases>()
                    {
                        new Cases {
                            Id=cModel.Id}
                    };

                //直接返回此类型JSON类型  
                return Json(codeList);
        }
        //更新涉案人员信息
        public JsonResult UpdateCasesPerson()
        {
            string csNumber = Request.Params["_csNumber"].ToString(),
                   name = Request["_name"].ToString(),
                   byName = Request.Form["_byName"].ToString(),
                   selSet = Request.Params["_selSet"].ToString(),
                   age = Request.Params["_age"].ToString(),
                   height = Request.Params["_height"].ToString(),
                   //photo = Request.Params["_photo"].ToString(),
                   nation = Request.Params["_nation"].ToString(),
                   country = Request.Params["_country"].ToString(),
                   workUnit = Request.Params["_workUnit"].ToString(),
                   post = Request.Params["_post"].ToString(),
                   pId = Request.Params["_id"].ToString(),
                   feature = Request.Params["_feature"].ToString(),
                   dress = Request.Params["_dress"].ToString(),
                   hideId = Request.QueryString["personId"].ToString();
            Guid phideId = new Guid(hideId);
            CaseSuspects cSubModel = new CaseSuspects();
            //cSubModel.pClass = "";
            cSubModel.pName = name;
            cSubModel.pByname = byName;
            cSubModel.pSex = byte.Parse(selSet);
            if (!string.IsNullOrEmpty(age))
            {
                cSubModel.spAge = byte.Parse(age);
            }
            if (!string.IsNullOrEmpty(height))
            {
                cSubModel.spHeight = short.Parse(height);
            }
            //cSubModel.pPhoto = photo;
            cSubModel.pNation = nation;
            cSubModel.pCountry = country;
            cSubModel.pAddress = workUnit;
            cSubModel.pCareer = post;
            cSubModel.pIdNum = pId;
            cSubModel.spFeature = feature;
            cSubModel.pDress = dress;
            cSubModel.csCode = csNumber;
            cSubModel.Id = phideId;
            c.suspectUpdate(cSubModel);
            List<CaseSuspects> codeList = new List<CaseSuspects>()  
            {  
                new CaseSuspects {csCode=cSubModel.csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //删除涉案人员信息
        public JsonResult SuspectDelete()
        {
            string _pId = Request.Form["_pId"].ToString(),
                  csCode = Request.Form["_csCode"].ToString();
            Guid pId = new Guid(_pId);
            CaseSuspects cs = new CaseSuspects();
            cs.Id = pId;
            c.suspectDelete(cs);
            List<Cases> codeList = new List<Cases>()  
            {  
                new Cases {csCode=csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //更新预案信息
        public ActionResult UpdateGoingCases()
        {
            try
            {
                string csId = Request.Form["_hidnId"].ToString();
                string csPlan = HttpUtility.UrlDecode(Request.Form["_csPlan"].ToString());
                Guid hideId = new Guid(csId);
                Cases cModel = new Cases();
                cModel.Id = hideId;
                cModel.csPlan = csPlan;
                c.Update(cModel);

            }
            catch (Exception ex)
            {
            }
            return View("CaseDetails");
        }
        //涉案人员
        public ActionResult GetCasesPerson()
        {
            return View("CaseDetails");
        }
        //加载案件管理页面数据
        public JsonResult GetCases(int limit, int offset, string csType, string csCode, string csName)
        {
            var userCode = JQSoft.AppContext.Current.User.UserId;
            var role = JQSoft.AppContext.Current.User.Roles[0];
            var list = c.GetCaseInfo(csName, csCode, csType, userCode,role);
            //var result = VIMS.Web.Models.JqGridData<Cases>.Create(list, list.Count, 1, 10);
            //return Json(result, JsonRequestBehavior.AllowGet);
            var total = list.Count;
            var rows = list.Skip(offset).Take(limit).ToList();
            return Json(new { total = list.Count, rows = rows }, JsonRequestBehavior.AllowGet);
        }

        //获取案件类别
        public JsonResult GetCaseCategory()
        {
            var list = c.GetCaseCategory();

            List<Nodes> nodeList = new List<Nodes>();

            for (int i = 0; i < list.Count; i++)
            {
                Nodes nm = new Nodes();
                nm.pId = list[i].dicFather;
                nm.name = list[i].dicValue;
                nm.id = list[i].dicCode;
                nodeList.Add(nm);
            }
            return Json(nodeList);
        }

        //根据搜索条件获取案件类别
        public JsonResult GetCaseCategoryWhere(string _strWhere)
        {
            var listResource = c.GetCaseCategory();
            var list = (listResource.Where(e => e.dicValue.Trim().IndexOf(_strWhere) != -1 || e.dicValAbbr.Trim().IndexOf(_strWhere.ToUpper()) != -1)).ToList();
            List<Nodes> nodeList = new List<Nodes>();

            if (list.Count > 0)
            {
                for (int i = 0; i < list.Count; i++)
                {
                    Nodes nm = new Nodes();
                    nm.pId = list[i].dicFather;
                    nm.name = list[i].dicValue;
                    nm.id = list[i].dicCode;
                    nodeList.Add(nm);
                }
            }
            else
            {
                for (int i = 0; i < listResource.Count; i++)
                {
                    Nodes nm = new Nodes();
                    nm.pId = listResource[i].dicFather;
                    nm.name = listResource[i].dicValue;
                    nm.id = listResource[i].dicCode;
                    nodeList.Add(nm);
                }
            }

            return Json(nodeList);
        }

        //添加案件信息页面 
        public ActionResult CaseDetails()
        {
            return View("CaseDetails");
        }
        //编辑案件信息页面 
        public ActionResult Edit()
        {
            ViewData["_id"] = Request.QueryString["_id"];
            ViewData["_type"] = Request.QueryString["type"];
            return View("Edit");
        } 
        public JsonResult GetDictInfo()
        {
            string field = Request.QueryString["field"];
            var list = c.GetDictData(field);
            return Json(list);
        }
        //根据csCode获取涉案人员记录
        public JsonResult GetCasesPersonInfo()
        {
            CaseSuspects entity = new CaseSuspects();
            entity.csCode = Request.QueryString["_caseCode"]; 
            var list = c.GetCasesPersonInfo(entity,1);
            var total = list.Count;
            return Json(new { total = list.Count, rows = list }, JsonRequestBehavior.AllowGet);
        }
        //根据csCode和查询条件获取涉案人员记录
        public JsonResult GetSearchCondPersonInfo(int limit, int offset, string csCode, string search)
        {
            CaseSuspects entity = new CaseSuspects();
            var strWhere = HttpUtility.UrlDecode(search);
            if (!string.IsNullOrEmpty(strWhere))//条件不为空，则是在查找犯罪嫌疑人
            {
                if (Regex.IsMatch(strWhere, @"[\u4e00-\u9fa5]") || Regex.IsMatch(strWhere, "[a-zA-Z]"))//判断输入条件包含中文，则是按姓名搜索
                {
                    entity.pName = strWhere;
                }
                else
                {
                    if (num(strWhere))//判断输入条件包含数字，则是按身份证搜索
                    {
                        entity.pIdNum = strWhere;
                    }
                }
            }
            entity.csCode = csCode;
            var list = c.GetCasesPersonInfo(entity, 1);
            var total = list.Count;
            var rows = list.Skip(offset).Take(limit).ToList();
            return Json(new { total = list.Count, rows }, JsonRequestBehavior.AllowGet);
        }
        //关联案件获取涉案人员记录
        public JsonResult GetLinkCondPersonInfo(string search)
        {
            CaseSuspects entity = new CaseSuspects();
            var strWhere = HttpUtility.UrlDecode(search);
            if (!string.IsNullOrEmpty(strWhere))//条件不为空，则是在查找犯罪嫌疑人
            {
                if (Regex.IsMatch(strWhere, @"[\u4e00-\u9fa5]") || Regex.IsMatch(strWhere, "[a-zA-Z]"))//判断输入条件包含中文，则是按姓名搜索
                {
                    entity.pName = strWhere;
                }
                else
                {
                    if (num(strWhere))//判断输入条件包含数字，则是按身份证搜索
                    {
                        entity.pIdNum = strWhere;
                    }
                }
            }
            var list = c.GetLinkCasesPersonInfo(entity);
            return Json(list);
        }
        //判断字符串是否为数字
        public static bool num(string t)
        {
            int j = t.Length;
            for (int k = 0; k <= j - 1; k++)
            {
                byte tb = Convert.ToByte(t[k]);
                if ((tb < 48) || (tb > 57))
                    return false;
            }
            return true;
        }
        //根据Id获取涉案人员记录
        public JsonResult GetCasesPersonIdInfo()
        {
            CaseSuspects entity = new CaseSuspects();
            var pId = Request.QueryString["_personId"];
            Guid _pId = new Guid(pId);
            entity.Id = _pId;
            var list = c.GetCasesPersonInfo(entity, 0);
            return Json(list);
        }
        //警力部署页面
        public ActionResult PoliceForce()
        {
            return View();
        }
        //获取警力部署信息
        public JsonResult GetCaseDeployRecord(int limit, int offset, string csCode) 
        {
            //string csCode = Request.QueryString["csCode"];
            CaseDeployInfo cs = new CaseDeployInfo();
            cs.csCode = csCode;
            var list = c.GetCaseDeployInfo(cs);
            //给列表加排序号
            List<CaseDeployInfo> listNew = new List<CaseDeployInfo>();
            for (int i = 0; i < list.Count; i++)
            {
                CaseDeployInfo csDe = new CaseDeployInfo();
                csDe.no = i+1;
                csDe.Id = list[i].Id;
                csDe.csCode=list[i].csCode;
                csDe.uCode = list[i].uCode;
                csDe.uName = list[i].uName;
                csDe.uSex = list[i].uSex;
                csDe.dCode = list[i].dCode;
                csDe.uTel = list[i].uTel;
                csDe.uShortNum = list[i].uShortNum;
                csDe.devCode = list[i].devCode;
                csDe.devName = list[i].devName;
                listNew.Add(csDe);
            }
            var total = listNew.Count;
            var rows = listNew.Skip(offset).Take(limit).ToList();
            return Json(new { total = listNew.Count, rows }, JsonRequestBehavior.AllowGet);
        }
        #region zTree 有关于zTree 树绑定方法，现使用
        /// <summary>
        /// 绑定警员树
        /// <returns></returns>
        public JsonResult GetTreeDataNew()
        {
            List<NodeModel> DInfo = c.GetNodeTreeInfo("已立案");

            List<Nodes> nodeList = new List<Nodes>();

            for (int i = 0; i < DInfo.Count; i++)
            {
                Nodes nm = new Nodes();
                    nm.pId = DInfo[i].ParentId;
                    nm.name = DInfo[i].text;
                    nm.id = DInfo[i].DicId;
                    //nm.title = DInfo[i].DicId;
                    if (DInfo[i].have == 1)//判断节点下面是否有警员
                    {
                        nm.desc = "1";
                    }
                    else
                    {
                        nm.desc = "0";
                    }
                    nodeList.Add(nm);
            }
            return Json(nodeList);
        }

        /// <summary>
        /// 绑定设备树
        /// </summary>
        /// <returns></returns>
        public JsonResult GetDevTreeDataNew()
        {
            List<NodeModel> DInfo = c.GetDeviceNodeTreeInfo("领用");

            List<Nodes> nodeList = new List<Nodes>();

            for (int i = 0; i < DInfo.Count; i++)
            {
                Nodes nm = new Nodes();
                nm.pId = DInfo[i].ParentId;
                nm.name = DInfo[i].text;
                nm.id = DInfo[i].DicId;
                if (DInfo[i].have == 1)//判断节点下面是否有设备
                {
                    nm.desc = "1";
                }
                else
                {
                    nm.desc = "0";
                }
                nodeList.Add(nm);
            }
            return Json(nodeList);

        }

        //获取选择负责人-最新
        public JsonResult GetResSelectedNew()
        {
            var csCode = Request.QueryString["_ResCode"].ToString();
            List<NodeModel> DInfo = c.GetResSelectedInfo(csCode);

            List<Nodes> nodeList = new List<Nodes>();

            for (int i = 0; i < DInfo.Count; i++)
            {
                Nodes nm = new Nodes();
                nm.pId = DInfo[i].ParentId;
                nm.name = DInfo[i].text;
                nm.id = DInfo[i].DicId;
               
                nodeList.Add(nm);
            }
            return Json(nodeList);
        }


        //获取选择负责人绑定树
        public JsonResult GetResSelectedTreeDataNew()
        {
            var csCode = Request.QueryString["_ResCode"].ToString();
            List<NodeModel> DInfo = c.GetResSelectedNodeTreeInfo(csCode);

            List<Nodes> nodeList = new List<Nodes>();

            for (int i = 0; i < DInfo.Count; i++)
            {
                Nodes nm = new Nodes();
                nm.pId = DInfo[i].ParentId;
                nm.name = DInfo[i].text;
                nm.id = DInfo[i].DicId;
                if (DInfo[i].have == 1)//判断节点下面是否有警员
                {
                    nm.desc = "1";
                }
                else
                {
                    nm.desc = "0";
                }
                nodeList.Add(nm);
            }
            return Json(nodeList);
        }

        #endregion
        #region 有关于bootstrap treeview 树绑定的方法，停用
        /// <summary>
        /// 绑定警员树
        /// </summary>
        /// <param name="code"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        public JsonResult GetTreeData()
        {
            List<NodeModel> DInfo = c.GetNodeTreeInfo("已立案");
            //创建jsondata对象
            StringBuilder jsonData = new StringBuilder();
            //拼接json字符串 开始{
            jsonData.Append("[");
            //调用GetChildNodes方法，默认传参试为0（0表示根节点菜单选项）
            jsonData.Append(GetChildNodes("0000", DInfo));
            //闭合Node子类数组 ]
            jsonData.Append("]");
            //返回json字符串
            return Json(jsonData.ToString());
        }

        //获取选择负责人绑定树
        public JsonResult GetResSelectedTreeData()
        {
            var csCode = Request.QueryString["_ResCode"].ToString();
            List<NodeModel> DInfo = c.GetResSelectedNodeTreeInfo(csCode);
            //创建jsondata对象
            StringBuilder jsonData = new StringBuilder();
            //拼接json字符串 开始{
            jsonData.Append("[");
            //调用GetChildNodes方法，默认传参试为0（0表示根节点菜单选项）
            jsonData.Append(GetChildNodes("0000", DInfo));
            //闭合Node子类数组 ]
            jsonData.Append("]");
            //返回json字符串
            return Json(jsonData.ToString());
        }
        //根据负责人id取得该信息
        public JsonResult GetResSelectedForId()
        {
            var pId = Request.QueryString["_resPersonId"];
            var list = c.GetResSelectedPersonInfo(pId);
            return Json(list);
        }
        /// <summary>
        /// 绑定设备树
        /// </summary>
        /// <returns></returns>
        public JsonResult GetDevTreeData()
        {
            List<NodeModel> DInfo = c.GetDeviceNodeTreeInfo("领用");
            //创建jsondata对象
            StringBuilder jsonData = new StringBuilder();
            //拼接json字符串 开始{
            jsonData.Append("[");
            //调用GetChildNodes方法，默认传参试为0（0表示根节点菜单选项）
            jsonData.Append(GetChildNodes("0000", DInfo));
            //闭合Node子类数组 ]
            jsonData.Append("]");
            //返回json字符串
            return Json(jsonData.ToString());
        }
        /// <summary>
        /// bootstrap-treeveiw ,GetChildNodes方法，此方法使用递归
        /// </summary>
        /// <param name = "parentId" ></ param >
        /// < returns ></ returns >
        public string GetChildNodes(string parentId, List<NodeModel> DInfo)
        {
            //创建ChiidNodeStr变量
            StringBuilder ChildNodeStr = new StringBuilder();
            //查询符合条件的数据（ParentId=0），DictionaryList接收数据
            List<NodeModel> nodeList = DInfo.Where(e => e.ParentId.Trim() == (parentId == null ? null : parentId.Trim())).ToList();
            //循环nodeList为TreeView所需数据分层级（即子类、父类等节点分开）
            for (int i = 0; i < nodeList.Count; i++)
            {
                //Nodes数组开始 {
                ChildNodeStr.Append("{");
                //实例化NewNode
                NodeModel NewNode = new NodeModel();
                //分别为字段赋值
                NewNode.DicId = nodeList[i].DicId;
                NewNode.text = nodeList[i].text;
                NewNode.ParentId = nodeList[i].ParentId;

                //将要显示的字段拼接
                ChildNodeStr.Append("text:'" + NewNode.text + "',");
                ChildNodeStr.Append("parentId:'" + nodeList[i].ParentId + "',");
                ChildNodeStr.Append("id:'" + nodeList[i].DicId + "',");
                //超链接地址（此处设置为空链接#）
                ChildNodeStr.Append("href:'#parent1',");
                ChildNodeStr.Append("tags:['" + NewNode.DicId + "']");
                //拼接完毕子类分层，去掉最后多余的符号（,）
                string ChildNodes = GetChildNodes(NewNode.DicId, DInfo).Trim(',');
                //判断父类下是否有子类，如果有子类放到Nodes里，如果没有不让他显示为数组形式“[]”
                if (ChildNodes != string.Empty)
                {
                    //拼接Json字符串格式
                    ChildNodeStr.Append(",");
                    ChildNodeStr.Append("nodes:[");
                    ChildNodeStr.Append(ChildNodes);
                    ChildNodeStr.Append("]");
                }
                //结尾加上}, 
                ChildNodeStr.Append("},");
            }
            //返回Json字符串，并将,去掉
            return ChildNodeStr.ToString().Trim(',');
        }
        /// <summary>
        /// 获取警员被选择节点下面所有子节点
        /// </summary>
        /// <returns></returns>
        public JsonResult GetSelectedNodesInfo()
        {
            string pId = Request.QueryString["parentId"];
            List<NodeModel> DInfo = c.GetNodeTreeInfo("已立案");
            List<NodeModel> newList = new List<NodeModel>();
            List<NodeModel> nList = GetSelectedNodes(pId, newList, DInfo);
            return Json(nList);
        }
        /// <summary>
        /// 获取终端设备被选择节点下面所有子节点
        /// </summary>
        /// <returns></returns>
        public JsonResult GetSelectedDevNodesInfo()
        {
            string pId = Request.QueryString["parentId"];
            List<NodeModel> DInfo = c.GetDeviceNodeTreeInfo("领用");
            List<NodeModel> newList = new List<NodeModel>();
            List<NodeModel> nList = GetSelectedNodes(pId, newList, DInfo);
            return Json(nList);
        }
        /// <summary>
        /// 递归算法获取被选择节点下面所有子节点勾选id
        /// </summary>
        /// <param name="parentId">父节点id</param>
        /// <param name="DInfo">数据源</param>
        /// <returns></returns>
        public List<NodeModel> GetSelectedNodes(string parentId, List<NodeModel> newList, List<NodeModel> DInfo)
        {
           
            List<NodeModel> nodeList = DInfo.Where(e => e.ParentId.Trim() == (parentId == null ? null : parentId.Trim())).ToList();
            for (int i = 0; i < nodeList.Count; i++)
            {
                //实例化NewNode
                NodeModel NewNode = new NodeModel();
                //分别为字段赋值
                NewNode.DicId = nodeList[i].DicId;
                NewNode.text = nodeList[i].text;
                NewNode.ParentId = nodeList[i].ParentId;
                newList.Add(NewNode);
                GetSelectedNodes(NewNode.DicId, newList, DInfo);
            }
            return newList;
        }
        #endregion
        /// <summary>
        /// 添加警员和设备记录
        /// </summary>
        /// <returns></returns>
        public JsonResult AddPoliceAndDevInfo()
        {
            var csCode = Request.Form["_ccCode"].ToString();
            var strPerson = Request.Form["_strPerson"].ToString();
            var strDev = Request.Form["_strDev"].ToString();
            c.AddPoliceForceRecord(csCode, strPerson, strDev);
            //添加code数据  
            List<Code> codeList = new List<Code>()  
            {  
                new Code {_code=csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //保存关联选择信息
        public JsonResult UpdateResSelectedInfo()
        {
            var csCode = Request.Form["_cgCode"].ToString();//案件编码
            var devCode = Request.Form["_devCode"].ToString();//设备编码
            var resCode = Request.Form["_resCode"].ToString();//人员编码
            var result = c.ChooseDevP(csCode, devCode, resCode);
            //添加code数据  
            List<Code> codeList = new List<Code>()  
            {  
                new Code {_code=csCode,_result=result}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //取消关联选择
        public JsonResult UnResSelectedInfo()
        {
            var csCode = Request.Form["_cgCode"].ToString();//案件编码
            var devCode = Request.Form["_devCode"].ToString();//设备编码
            var result = c.UnChooseDevP(csCode, devCode);
            //添加code数据  
            List<Code> codeList = new List<Code>()  
            {  
                new Code {_code=csCode,_result=result}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //获取案情备注记录
        public JsonResult GetCaseGoingsRecord()
        {
            string csCode = Request.QueryString["csCode"];
            CaseGoings cs = new CaseGoings();
            cs.csCode = csCode;
            var list = c.GetCasesRemarkInfo(cs);
            List<CaseGoings> listNew = new List<CaseGoings>();
            for (int i = 0; i < list.Count; i++)
            {
                CaseGoings csDe = new CaseGoings();
                csDe.Id = list[i].Id;
                csDe.csCode = list[i].csCode;
                csDe.cgType = list[i].cgType;
                csDe.cgAbstract = list[i].cgAbstract;
                csDe.uCode = list[i].uCode;
                csDe.CreateTime = Convert.ToDateTime(list[i].cgCreateTime).ToString("yyyy-MM-dd HH:mm:ss");
                listNew.Add(csDe);
            }
            return Json(new { total = listNew.Count, rows = listNew }, JsonRequestBehavior.AllowGet);
        }
        //根据案情备注id获取记录
        public JsonResult GetCasesGoingsIdInfo()
        {
            var id = Request.QueryString["_cgId"];
            Guid _pId = new Guid(id);
            CaseGoings cg = c.GetCasesGoingsId(_pId);
            List<CaseGoings> cgList = new List<CaseGoings>();
            cgList.Add(cg);
            return Json(cgList);
        }
        //添加案情记录
        public JsonResult AddCasesGoings()
        {
            string cgType = Request.Params["_cgType"].ToString(),
                   cgAbstract = Request.Form["_cgAbstract"].ToString(),
                   csCode = Request.Form["_cgCode"].ToString();
            CaseGoings cg = new CaseGoings();
            cg.csCode = csCode;
            cg.cgType = cgType;
            cg.cgAbstract = cgAbstract;
            cg.uCode = "chengxinmei";//暂时写死
            cg.cgCreateTime = DateTime.Now;
            cg.Id = Guid.NewGuid();
            c.caseGoingsInsert(cg);
            List<CaseGoings> codeList = new List<CaseGoings>()  
            {  
                new CaseGoings {csCode=cg.csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //更新案情记录
        public JsonResult UpdateCasesGoings()
        {
            string _cgId = Request.Form["_cgEditId"].ToString(),
                   cgType = Request.Form["_cgEditType"].ToString(),
                   cgAbstract = Request.Form["_cgEditAbstract"].ToString(),
                   csCode = Request.Form["_cgCode"].ToString();
            Guid cgId = new Guid(_cgId);
            CaseGoings cg = new CaseGoings();
            cg.csCode = csCode;
            cg.cgType = cgType;
            cg.cgAbstract = cgAbstract;
            cg.cgCreateTime = DateTime.Now;
            cg.Id = cgId;
            c.caseGoingsUpdate(cg);
            List<CaseGoings> codeList = new List<CaseGoings>()  
            {  
                new CaseGoings {csCode=cg.csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //删除案情记录
        public JsonResult DeleteCasesGoings()
        {
            string _cgId = Request.Form["_cgId"].ToString(),
                   csCode = Request.Form["_csCode"].ToString();
            Guid cgId = new Guid(_cgId);
            CaseGoings cg = new CaseGoings();
            cg.Id = cgId;
            c.caseGoingsDelete(cg);
            List<CaseGoings> codeList = new List<CaseGoings>()  
            {  
                new CaseGoings {csCode=csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //预览案件详情
        public ActionResult PreView()
        {
            var a= Request.QueryString["_csId"];
            ViewData["_csId"] = a;
            return View("PreView");
        }
        //警力部署
        public ActionResult PoliceForceManage()
        {
            return View("PoliceForce");
        }
        //快速添加案件
        public ActionResult Quick()
        {
            return View("QuickCreateCase");
        }
        //快速添加警力部署
        public ActionResult QuickPolice()
        {
            ViewData["_csCode"] = Request.QueryString["_csCode"];
            return View("QuickPoliceForce");
        }
        //添加案件信息
        public JsonResult QuickCreateCases()
        {
            string csName = Request.Form["_csName"],
                       csCode = Request.Form["_csCode"],
                       csLocation = Request.Form["_csLocation"],
                       csLongitude = Request.Form["_csLongitude"],
                       csLatitude = Request.Form["_csLatitude"],
                       csTimeS = Request.Form["_csTimeS"],
                       csTimeE = Request.Form["_csTimeE"];
            Cases cModel = new Cases();
            cModel.Id = Guid.NewGuid();
            cModel.csName = csName;
            cModel.csCode = csCode;
            cModel.csAddress = csLocation;
            cModel.csStatus = "已立案";
            cModel.csLongitude = string.IsNullOrEmpty(csLongitude) ? 0 : decimal.Parse(csLongitude);
            cModel.csLatitude = string.IsNullOrEmpty(csLatitude) ? 0 : decimal.Parse(csLatitude);
            cModel.csTime = string.IsNullOrEmpty(csTimeS) ? DateTime.Now : Convert.ToDateTime(csTimeS);
            cModel.csTime_h = string.IsNullOrEmpty(csTimeE) ? DateTime.Now : Convert.ToDateTime(csTimeE);
            cModel.csCreateTime = DateTime.Now;
            cModel.uCode = JQSoft.AppContext.Current.User.SystemName;
            c.Insert(cModel);
            List<Cases> codeList = new List<Cases>()
                    {
                        new Cases {
                            csCode=cModel.csCode}
                    };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
        //删除警力部署信息
        public JsonResult DeleteCasesDeploy(string _cdId, string _csCode, string _devCode) 
        {
            if (_cdId != "null")
            {
                Guid cdId = new Guid(_cdId);
                CaseDeploys cd = new CaseDeploys();
                cd.Id = cdId;
                c.caseDeploysDelete(cd);
            }
            if (_devCode != "null")
            {
                CaseDeployDevs cdd = new CaseDeployDevs();
                cdd.csCode = _csCode;
                cdd.devCode = _devCode;

                c.caseDeployDevsDelete(cdd);
            }
            List<CaseDeploys> codeList = new List<CaseDeploys>()  
            {  
                new CaseDeploys {csCode=_csCode}
            };

            //直接返回此类型JSON类型  
            return Json(codeList);
        }
    }
}
