using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using JQSoft.Web.Mvc;
using VIMS.Services.LiveCommand;
using VIMS.DataModel.LiveCommand;
using VIMS.Services.Impl.LiveCommand;
using System.Text;
using VIMS.DataModel.SystemManagement;



namespace VIMS.Web.Controllers
{
    public class PoliceDispatchController : Controller
    {

        private IDispatchService _idispatchservice = null;

        //构造函数
        public PoliceDispatchController(IDispatchService idispatchservice)
        {
            _idispatchservice = idispatchservice;

        }

        //路由信息
        public ActionResult Index()
        {
            return View("PoliceDispatch");
        }

               [HttpPost]
        //获取主页的案件信息
        public JsonResult GetCaseInfo(string csStatec)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            IList<MainCaseInfo> _ilistpolicedispatch = _idispatchservice.GetMainCaseInfoByCaseState(csStatec, role,ucode);
            return Json(_ilistpolicedispatch);
        }

        [HttpPost]
        //拼接请求位置信息人
        public JsonResult GetPositionInfo(List<PoliceDispatch> devinfo)
        {
            string conditionString = null;
            List<PoliceDispatch> devCode = new List<PoliceDispatch>();

            for (int i = 0; i < devinfo.Count; i++)
            {
                if (devinfo[i].devCoded.Split(',').Length == 3)
                {
                    conditionString = conditionString + devinfo[i].devCoded.Split(',')[0] + "," + devinfo[i].devCoded.Split(',')[1] + ";";
                    string a = devinfo[i].devCoded.Split(',')[0];
                    string b = devinfo[i].devCoded.Split(',')[1];
                    string c = devinfo[i].devCoded.Split(',')[2];
                    PoliceDispatch dev = new PoliceDispatch();
                    dev.csCoded = a;
                    dev.uCoded = b;
                    dev.devCoded = c;
                    devCode.Add(dev);
                }
                if (devinfo[i].devCoded.Split(',').Length == 4)
                {
                    conditionString = conditionString + devinfo[i].devCoded.Split(',')[0] + "," + devinfo[i].devCoded.Split(',')[1] + ";";
                    string a = devinfo[i].devCoded.Split(',')[0];
                    string b = devinfo[i].devCoded.Split(',')[1];
                    string c = devinfo[i].devCoded.Split(',')[2];
                    string d = devinfo[i].devCoded.Split(',')[3];
                    PoliceDispatch dev = new PoliceDispatch();
                    dev.csCoded = a;
                    dev.uCoded = b;
                    dev.devCoded = c;
                    devCode.Add(dev);
                }
                if (devinfo[i].devCoded.Split(',').Length == 2)
                {
                    conditionString = conditionString + devinfo[i].devCoded.Split(',')[0] + "," + devinfo[i].devCoded.Split(',')[1] + ";";
                    string a = devinfo[i].devCoded.Split(',')[0];
                    string b = devinfo[i].devCoded.Split(',')[1];
                    string c = devinfo[i].devCoded.Split(',')[1];
                    PoliceDispatch dev = new PoliceDispatch();
                    dev.csCoded = a;
                    dev.uCoded = b;
                    dev.devCoded = c;
                    devCode.Add(dev);
                }
            }
            try
            {
                conditionString = conditionString.Substring(0, conditionString.Length - 1);
                IList<PoliceDispatch> _listpolicedispatch = _idispatchservice.GetDeviceByCaseCodeDevCode(conditionString, devCode);
                return Json(_listpolicedispatch);
            }
            catch (Exception e)
                {
                    return Json("");
                }
           

        }


        [HttpPost]
        //拼接请求位置信息设备
        public JsonResult GetPositionInfodev(List<PoliceDispatch> devinfo)
        {
            string conditionString = null;
            List<PoliceDispatch> devCode = new List<PoliceDispatch>();

            for (int i = 0; i < devinfo.Count; i++)
            {
                if (devinfo[i].devCoded.Split(',').Length == 4)
                {
                    conditionString = conditionString + devinfo[i].devCoded.Split(',')[0] + "," + devinfo[i].devCoded.Split(',')[1] + ";";
                    string a = devinfo[i].devCoded.Split(',')[0];
                    string b = devinfo[i].devCoded.Split(',')[1];
                    string c = devinfo[i].devCoded.Split(',')[2];
                    string d = devinfo[i].devCoded.Split(',')[3];
                    PoliceDispatch dev = new PoliceDispatch();
                    dev.csCoded = a;
                    dev.uCoded = b;
                    dev.devCoded = c;
                    dev.devNamed = d;
                    devCode.Add(dev);
                }
            }
            conditionString = conditionString.Substring(0, conditionString.Length - 1);
            IList<PoliceDispatch> _listpolicedispatch = _idispatchservice.GetDeviceByCaseCodeDevCodeuCode(conditionString, devCode);
            return Json(_listpolicedispatch);
        }


        [HttpPost]
        //获得案件的地址在地图中显示
        public JsonResult GetCasePosition(string csCode)
        {
            IList<MainCaseInfo> _ilistpolicedispatch = _idispatchservice.GetCasePositionInfo(csCode);
            return Json(_ilistpolicedispatch);
        }
        //获得选择节点的信息

        [HttpPost]
        public JsonResult GetSelectedDevNodesInfo()
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            string pId = Request.QueryString["parentId"];
            List<NodeModel> DInfo = _idispatchservice.GetDeviceNodeTreeInfo("领用",role,ucode);
            List<NodeModel> newList = new List<NodeModel>();
            List<NodeModel> nList = GetSelectedNodes(pId, newList, DInfo);
            return Json(nList);
        }

         [HttpPost]
        //警力调度画面绑定树状结构
        public JsonResult GetDevInfo(string status)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            List<NodeModel> _listpolicedispatch = _idispatchservice.GetDeviceNodeTreeInfo(status,role,ucode);
            //创建jsondata对象
            StringBuilder jsonData = new StringBuilder();
            //拼接json字符串 开始{
            jsonData.Append("[");
            //调用GetChildNodes方法，默认传参试为0（0表示根节点菜单选项）
            jsonData.Append(GetChildNodes("000", _listpolicedispatch));
            //闭合Node子类数组 ]
            jsonData.Append("]");
            //返回json字符串
            return Json(jsonData.ToString());
        }

        public string GetChildNodes(string parentId, List<NodeModel> _listpolicedispatch)
        {
            //创建ChiidNodeStr变量
            StringBuilder ChildNodeStr = new StringBuilder();
            //查询符合条件的数据（ParentId=0），DictionaryList接收数据
            List<NodeModel> nodeList = _listpolicedispatch.Where(e => e.ParentId.Trim() == (parentId == null ? null : parentId.Trim())).ToList();
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
                string ChildNodes = GetChildNodes(NewNode.DicId, _listpolicedispatch).Trim(',');
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



        //警力调度绑定按人物分类的树状结构
        public JsonResult GetPoepleDevInfo(string status)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            List<NodeModel> _listpolicedispatch = _idispatchservice.GetPeopleDeviceNodeTreeInfo(status,role,ucode);
            //创建jsondata对象
            StringBuilder jsonData = new StringBuilder();
            //拼接json字符串 开始{
            jsonData.Append("[");
            //调用GetChildNodes方法，默认传参试为0（0表示根节点菜单选项）
            jsonData.Append(GetChildNodes("000", _listpolicedispatch));
            //闭合Node子类数组 ]
            jsonData.Append("]");
            //返回json字符串
            return Json(jsonData.ToString());
        }


        //获得选择人物节点的信息
        public JsonResult GetSelectedDevNodesInfo_p()
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            string pId = Request.QueryString["parentId"];
            List<NodeModel> DInfo = _idispatchservice.GetPeopleDeviceNodeTreeInfo("领用",role,ucode);
            List<NodeModel> newList = new List<NodeModel>();
            List<NodeModel> nList = GetSelectedNodes(pId, newList, DInfo);
            return Json(nList);
        }

        [HttpPost]
        //获得最新的位置用于计算是否在搜索范围内
        public JsonResult GetDevPosition()
        {
            IList<PoliceDispatch> _ilistpolicedispatch = _idispatchservice.GetDevPositionInfo();
            return Json(_ilistpolicedispatch);
        }

        [HttpPost]
        public JsonResult SearchDevPosition(string devType)
        {
            IList<PoliceDispatch> _ilistpolicedispatch = _idispatchservice.SearchDevPositionInfo(devType);
            return Json(_ilistpolicedispatch);
        }


        [HttpPost]
        public JsonResult zTreeDevice(string status)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            List<NodeModel> _listpolicedispatch = _idispatchservice.GetDeviceNodeTreeInfo(status,role,ucode);
            List<Nodes> nodeList = new List<Nodes>();
            foreach (NodeModel list in _listpolicedispatch)
            {
                Nodes node = new Nodes();
                node.pId = list.ParentId;
                node.id = list.DicId;
                node.name = list.text;
                nodeList.Add(node);
            }
            return Json(nodeList);
        }

        [HttpPost]
        public JsonResult zTreePeople(string status)
        {
            var role = @JQSoft.AppContext.Current.User.Roles[0].Trim();
            var ucode = @JQSoft.AppContext.Current.User.UserId.Trim();
            List<NodeModel> _listpolicedispatch = _idispatchservice.GetPeopleDeviceNodeTreeInfo(status,role,ucode);
            List<Nodes> nodeList = new List<Nodes>();
            foreach (NodeModel list in _listpolicedispatch)
            {
                Nodes node = new Nodes();
                node.pId = list.ParentId;
                node.id = list.DicId;
                node.name = list.text;
                nodeList.Add(node);
            }
            return Json(nodeList);
        }
    }
}
