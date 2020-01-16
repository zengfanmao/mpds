using System;
using System.IO;
using System.Net;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Web;
using System.Web.SessionState;
using GRGcms.Web.UI;
using GRGcms.Common;

namespace GRGcms.Web.tools
{
    /// <summary>
    /// 管理员AJAX请求处理
    /// </summary>
    public class admin_ajax : IHttpHandler, IRequiresSessionState
    {
        Model.sysconfig sysConfig = new BLL.sysconfig().loadConfig();//系统配置信息
        public void ProcessRequest(HttpContext context)
        {
            //检查管理员是否登录
            if (!new ManagePage().IsAdminLogin())
            {
                context.Response.Write("{\"status\": 0, \"msg\": \"尚未登录或已超时，请登录后操作！\"}");
                return;
            }
            //取得处事类型
            string action = DTRequest.GetQueryString("action");
            switch (action)
            {
              
                case "navigation_validate": //验证导航菜单别名是否重复
                    navigation_validate(context);
                    break;
                case "dept_validate": //验证机构名称是否重复
                    dept_validate(context);
                    break;
                case "manager_validate": //验证管理员用户名是否重复
                    manager_validate(context);
                    break;
                case "get_navigation_list": //获取后台导航字符串
                    get_navigation_list(context);
                    break;
                case "group_validate": //验证组名是否重复
                    group_validate(context);
                    break;
                case "get_remote_fileinfo": //获取远程文件的信息
                    get_remote_fileinfo(context);
                    break;
                case "group_add": //添加关联组
                    group_add(context);
                    break;
                case "group_add_batch": //批量添加关联组
                    group_add_batch(context);
                    break;
                    
                case "group_delete": //删除关联组
                    group_delete(context);
                    break;
                case "group_setting": //设置当前组
                    group_setting(context);
                    break;
                case "group_default": //设置常用组
                    group_default(context);
                    break;
                case "group_cancel_default": //取消常用组
                    group_cancel_default(context);
                    break;
                case "rank_validate":
                    rank_validate(context);
                    break;
                case "rank_group_add":
                    rank_group_add(context);
                    break;
                case "rank_group_delete":
                    rank_group_delete(context);
                    break;
            }
        }

    

        #region 验证导航菜单别名是否重复========================
        private void navigation_validate(HttpContext context)
        {
            string navname = DTRequest.GetString("param");
            string old_name = DTRequest.GetString("old_name");
            if (string.IsNullOrEmpty(navname))
            {
                context.Response.Write("{ \"info\":\"该导航别名不可为空！\", \"status\":\"n\" }");
                return;
            }
            if (navname.ToLower() == old_name.ToLower())
            {
                context.Response.Write("{ \"info\":\"该导航别名可使用\", \"status\":\"y\" }");
                return;
            }
            //检查保留的名称开头
            if (navname.ToLower().StartsWith("channel_"))
            {
                context.Response.Write("{ \"info\":\"该导航别名系统保留，请更换！\", \"status\":\"n\" }");
                return;
            }
            BLL.menu bll = new BLL.menu();
            if (bll.Exists(navname))
            {
                context.Response.Write("{ \"info\":\"该导航别名已被占用，请更换！\", \"status\":\"n\" }");
                return;
            }
            context.Response.Write("{ \"info\":\"该导航别名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 验证机构是否重复========================
        private void dept_validate(HttpContext context)
        {
            string navname = DTRequest.GetString("param");
            string old_name = DTRequest.GetString("old_name");
            if (string.IsNullOrEmpty(navname))
            {
                context.Response.Write("{ \"info\":\"机构名不可为空！\", \"status\":\"n\" }");
                return;
            }
            if (navname.ToLower() == old_name.ToLower())
            {
                context.Response.Write("{ \"info\":\"该机构名可使用\", \"status\":\"y\" }");
                return;
            }

            BLL.dept bll = new BLL.dept();
            if (bll.Exists(navname))
            {
                context.Response.Write("{ \"info\":\"该机构名已被占用，请更换！\", \"status\":\"n\" }");
                return;
            }
            context.Response.Write("{ \"info\":\"该机构名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 验证管理员用户名是否重复========================
        private void manager_validate(HttpContext context)
        {
            string user_name = DTRequest.GetString("param");
            if (string.IsNullOrEmpty(user_name))
            {
                context.Response.Write("{ \"info\":\"请输入用户名\", \"status\":\"n\" }");
                return;
            }
            BLL.user bll = new BLL.user();
            if (bll.Exists(user_name))
            {
                context.Response.Write("{ \"info\":\"用户名已被占用，请更换！\", \"status\":\"n\" }");
                return;
            }
            context.Response.Write("{ \"info\":\"用户名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 添加关联组========================
        private void group_add(HttpContext context)
        {
            string ucode = DTRequest.GetString("ucode");
            string strGroups= DTRequest.GetString("groups");
   
            BLL.user bll = new BLL.user();
            Model.user user = bll.GetModel(ucode);
            if (user != null)
            {
                string[] groups = strGroups.Split(',');
                foreach (string group in groups)
                {
                    BLL.group_user gubll = new BLL.group_user();
                    Model.group_user model = new Model.group_user();

                    model.account = user.uCode;
                    model.name = user.uName;
                    model.createtime = DateTime.Now;

                    BLL.group gbll = new BLL.group();
                    Model.group gmodel = gbll.GetModel(int.Parse(group.Trim()));
                    if (gmodel != null)
                    {
                        if (!gubll.Exists(gmodel.discussionCode.Trim(), user.uCode))
                        {
                            model.dept = gmodel.dept;
                            model.deptid = gmodel.deptid;
                            model.groupid = gmodel.discussionCode;
                            model.groupName = gmodel.discussionName;
                            model.type = gmodel.type;
                            model.isdefault = "1";
                            int result = gubll.Add(model);
                        }
                    }
                   

                }
               
            }
            context.Response.Write("{ \"info\":\"组名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 添加组群关联组========================
        private void rank_group_add(HttpContext context)
        {
            string rank = DTRequest.GetString("rank");
            string strGroups = DTRequest.GetString("groups");

            BLL.rank bll = new BLL.rank();
            Model.rankModel rankModel = bll.GetModel(rank);
            if (rankModel != null)
            {
                string[] groups = strGroups.Split(',');
                foreach (string group in groups)
                {
                    BLL.rank_group gubll = new BLL.rank_group();
                    Model.rankGroupModel model = new Model.rankGroupModel();

                    model.rank = rankModel.rank;
                    model.rankName = rankModel.rankName;
                    model.createtime = DateTime.Now;

                    BLL.group gbll = new BLL.group();
                    Model.group gmodel = gbll.GetModel(int.Parse(group.Trim()));
                    if (gmodel != null)
                    {
                        if (!gubll.Exists(rankModel.rank, gmodel.discussionCode.Trim()))
                        {
                            model.discussionCode = gmodel.discussionCode;
                            model.discussionName = gmodel.discussionName;
                            int result = gubll.Add(model);
                        }
                    }
                }
            }
            context.Response.Write("{ \"info\":\"组名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 批量添加关联组========================
        private void group_add_batch(HttpContext context)
        {
            string ucodes = DTRequest.GetString("ucodes");
            string strGroups = DTRequest.GetString("groups");
            if (!string.IsNullOrEmpty(ucodes))
            {

                string[] uArray = ucodes.Split(',');
                foreach (string ucode in uArray)
                {
                    BLL.user bll = new BLL.user();
                    Model.user user = bll.GetModel(ucode);
                    if (user != null)
                    {
                        string[] groups = strGroups.Split(',');
                        foreach (string group in groups)
                        {
                            BLL.group_user gubll = new BLL.group_user();
                            Model.group_user model = new Model.group_user();

                            model.account = user.uCode;
                            model.name = user.uName;
                            model.createtime = DateTime.Now;
                            BLL.group gbll = new BLL.group();
                            Model.group gmodel = gbll.GetModel(int.Parse(group.Trim()));
                            if (gmodel != null)
                            {
                                if (!gubll.Exists(gmodel.discussionCode.Trim(), user.uCode))
                                {
                                    model.dept = gmodel.dept;
                                    model.deptid = gmodel.deptid;
                                    model.groupid = gmodel.discussionCode;
                                    model.groupName = gmodel.discussionName;
                                    model.type = gmodel.type;
                                    int result = gubll.Add(model);
                                }
                            }
                        }

                    }
                   
                }
                context.Response.Write("{ \"info\":\"批量添加关联组成功\", \"status\":\"y\" }");
                return;
            }
        }
        #endregion
        #region 删除关联组========================
        private void group_delete(HttpContext context)
        {
            //string ucode = DTRequest.GetString("ucode");
            string strGroups = DTRequest.GetString("groups");

            string[] groups = strGroups.Split(',');
            foreach (string group in groups)
            {
                BLL.group_user gubll = new BLL.group_user();
                gubll.Delete(int.Parse(group));
            }
            context.Response.Write("{ \"info\":\"删除成功\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 删除关联组========================
        private void rank_group_delete(HttpContext context)
        {
            //string ucode = DTRequest.GetString("ucode");
            string strGroups = DTRequest.GetString("groups");

            string[] groups = strGroups.Split(',');
            foreach (string group in groups)
            {
                BLL.rank_group gubll = new BLL.rank_group();
                gubll.Delete(int.Parse(group));
            }
            context.Response.Write("{ \"info\":\"删除成功\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 设置当前组========================
        private void group_setting(HttpContext context)
        {
            string ucode = DTRequest.GetString("ucode");
            string strGroups = DTRequest.GetString("groups");
            string[] groups = strGroups.Split(',');
            foreach (string group in groups)
            {
                BLL.group_user gubll = new BLL.group_user();
                Model.group_user guModel =gubll.GetModel(int.Parse(group.Trim()));
                if (guModel != null)
                {
                    BLL.user bll = new BLL.user();
                    Model.user user = bll.GetModel(ucode);
                    if (user != null)
                    {
                        user.groupid = guModel.groupid;
                        user.groupName = guModel.groupName;
                        bll.Update(user);
                    }
                }
            }
            context.Response.Write("{ \"info\":\"设置成功\", \"status\":\"y\" }");
            return;
        }
        #endregion

        #region 设置常用组========================
        private void group_default(HttpContext context)
        {
            string ucode = DTRequest.GetString("ucode");
            string strGroups = DTRequest.GetString("groups");
            string[] groups = strGroups.Split(',');
            foreach (string group in groups)
            {
                BLL.group_user gubll = new BLL.group_user();
                Model.group_user guModel = gubll.GetModel(int.Parse(group.Trim()));
                if (guModel != null && guModel.account.Equals(ucode))
                {
                    guModel.isdefault = "1";
                    gubll.Update(guModel);
                }
            }
            context.Response.Write("{ \"info\":\"设置成功\", \"status\":\"y\" }");
            return;
        }
        #endregion

        #region 取消常用组========================
        private void group_cancel_default(HttpContext context)
        {
            string ucode = DTRequest.GetString("ucode");
            string strGroups = DTRequest.GetString("groups");
            string[] groups = strGroups.Split(',');
            foreach (string group in groups)
            {
                BLL.group_user gubll = new BLL.group_user();
                Model.group_user guModel = gubll.GetModel(int.Parse(group.Trim()));
                if (guModel != null && guModel.account.Equals(ucode))
                {
                    guModel.isdefault = "0";
                    gubll.Update(guModel);
                }
            }
            context.Response.Write("{ \"info\":\"设置成功\", \"status\":\"y\" }");
            return;
        }
        #endregion

        #region 验证组名是否重复========================
        private void group_validate(HttpContext context)
        {
            string group_name = DTRequest.GetString("param");
            if (string.IsNullOrEmpty(group_name))
            {
                context.Response.Write("{ \"info\":\"请输入组名/组编号\", \"status\":\"n\" }");
                return;
            }
            BLL.group bll = new BLL.group();
            if (bll.Exists(group_name, group_name, "APP"))
            {
                context.Response.Write("{ \"info\":\"组名/组编号已被占用，请更换！\", \"status\":\"n\" }");
                return;
            }
            context.Response.Write("{ \"info\":\"组名/组编号可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 验证组群名是否重复========================
        private void rank_validate(HttpContext context)
        {
            string rank = DTRequest.GetString("param");
            if (string.IsNullOrEmpty(rank))
            {
                context.Response.Write("{ \"info\":\"请输入组群编号/组群名\", \"status\":\"n\" }");
                return;
            }
            BLL.rank bll = new BLL.rank();
            if (bll.Exists(rank, rank))
            {
                context.Response.Write("{ \"info\":\"组群编号/组群名已被占用，请更换！\", \"status\":\"n\" }");
                return;
            }
            context.Response.Write("{ \"info\":\"组群编号/组群名可使用\", \"status\":\"y\" }");
            return;
        }
        #endregion
        #region 获取后台导航字符串==============================
        private void get_navigation_list(HttpContext context)
        {
            Model.user adminModel = new ManagePage().GetAdminInfo();//获得当前登录管理员信息
            if (adminModel == null)
            {
                return;
            }
            Model.role roleModel = new BLL.role().GetModel(adminModel.roleid);//获得管理角色信息
            if (roleModel == null)
            {
                return;
            }
            //DataTable dt = new BLL.menu().GetList(0, DTEnums.NavigationEnum.System.ToString());
            DataTable dt = new BLL.menu().GetList(0, DTEnums.NavigationEnum.System.ToString());
            this.get_navigation_childs(context, dt, 0, roleModel.role_type, roleModel.manager_role_values);

        }
        private void get_navigation_childs(HttpContext context, DataTable oldData, int parent_id, int role_type, List<Model.role_value> ls)
        {
            DataRow[] dr = oldData.Select("parent_id=" + parent_id);
            bool isWrite = false;//是否输出开始标签
            for (int i = 0; i < dr.Length; i++)
            {
                //检查是否显示在界面上====================
                bool isActionPass = true;
                if (int.Parse(dr[i]["is_lock"].ToString()) == 1)
                {
                    isActionPass = false;
                }
                //检查管理员权限==========================
                if (isActionPass && role_type > 1)
                {
                    string[] actionTypeArr = dr[i]["action_type"].ToString().Split(',');
                    foreach (string action_type_str in actionTypeArr)
                    {
                        //如果存在显示权限资源，则检查是否拥有该权限
                        if (action_type_str == "Show")
                        {
                            Model.role_value modelt = ls.Find(p => p.nav_name == dr[i]["name"].ToString() && p.action_type == "Show");
                            if (modelt == null)
                            {
                                isActionPass = false;
                            }
                        }
                    }
                }
                //如果没有该权限则不显示
                if (!isActionPass)
                {
                    if (isWrite && i == (dr.Length - 1) && parent_id > 0)
                    {
                        context.Response.Write("</ul>\n");
                    }
                    continue;
                }
                //如果是顶级导航
                if (parent_id == 0)
                {
                    context.Response.Write("<div class=\"list-group\">\n");
                    context.Response.Write("<h1 title=\"" + dr[i]["sub_title"].ToString() + "\">");
                    if (!string.IsNullOrEmpty(dr[i]["icon_url"].ToString().Trim()))
                    {
                        if (dr[i]["icon_url"].ToString().StartsWith("."))
                        {
                            context.Response.Write("<i class=\"iconfont " + dr[i]["icon_url"].ToString().Trim('.') + "\"></i>");
                        }
                        else
                        {
                            context.Response.Write("<img src=\"" + dr[i]["icon_url"].ToString() + "\" />");
                        }
                    }
                    context.Response.Write("</h1>\n");
                    context.Response.Write("<div class=\"list-wrap\">\n");
                    context.Response.Write("<h2>" + dr[i]["title"].ToString() + "<i class=\"iconfont icon-arrow-down\"></i></h2>\n");
                    //调用自身迭代
                    this.get_navigation_childs(context, oldData, int.Parse(dr[i]["id"].ToString()), role_type, ls);
                    context.Response.Write("</div>\n");
                    context.Response.Write("</div>\n");
                }
                else//下级导航
                {
                    if (!isWrite)
                    {
                        isWrite = true;
                        context.Response.Write("<ul>\n");
                    }
                    context.Response.Write("<li>\n");
                    context.Response.Write("<a navid=\"" + dr[i]["name"].ToString() + "\"");
                    if (!string.IsNullOrEmpty(dr[i]["link_url"].ToString()))
                    {
                        context.Response.Write(" href=\"" + dr[i]["link_url"].ToString() + "\" target=\"mainframe\"");
                    }
                    if (!string.IsNullOrEmpty(dr[i]["icon_url"].ToString()))
                    {
                        context.Response.Write(" icon=\"" + dr[i]["icon_url"].ToString() + "\"");
                    }
                    context.Response.Write(" target=\"mainframe\">\n");
                    context.Response.Write("<span>" + dr[i]["title"].ToString() + "</span>\n");
                    context.Response.Write("</a>\n");
                    //调用自身迭代
                    this.get_navigation_childs(context, oldData, int.Parse(dr[i]["id"].ToString()), role_type, ls);
                    context.Response.Write("</li>\n");

                    if (i == (dr.Length - 1))
                    {
                        context.Response.Write("</ul>\n");
                    }
                }
            }
        }
        #endregion

        #region 获取远程文件的信息==============================
        private void get_remote_fileinfo(HttpContext context)
        {
            string filePath = DTRequest.GetFormString("remotepath");
            if (string.IsNullOrEmpty(filePath))
            {
                context.Response.Write("{\"status\": 0, \"msg\": \"没有找到远程附件地址！\"}");
                return;
            }
            if (!filePath.ToLower().StartsWith("http://"))
            {
                context.Response.Write("{\"status\": 0, \"msg\": \"不是远程附件地址！\"}");
                return;
            }
            try
            {
                HttpWebRequest _request = (HttpWebRequest)WebRequest.Create(filePath);
                HttpWebResponse _response = (HttpWebResponse)_request.GetResponse();
                int fileSize = (int)_response.ContentLength;
                string fileName = filePath.Substring(filePath.LastIndexOf("/") + 1);
                string fileExt = filePath.Substring(filePath.LastIndexOf(".") + 1).ToUpper();
                context.Response.Write("{\"status\": 1, \"msg\": \"获取远程文件成功！\", \"name\": \"" + fileName + "\", \"path\": \"" + filePath + "\", \"size\": " + fileSize + ", \"ext\": \"" + fileExt + "\"}");
            }
            catch
            {
                context.Response.Write("{\"status\": 0, \"msg\": \"远程文件不存在！\"}");
                return;
            }
        }
        #endregion


        #region 判断是否登陆以及是否开启静态====================
        private int get_builder_status()
        {
            //取得管理员登录信息
            Model.user adminInfo = new Web.UI.ManagePage().GetAdminInfo();
            if (adminInfo == null)
            {
                return -1;
            }
            else if (!new BLL.role().Exists(adminInfo.roleid, "sys_builder_html", DTEnums.ActionEnum.Build.ToString()))
            {
                return -2;
            }
            else if (sysConfig.staticstatus != 2)
            {
                return -3;
            }
            else
            {
                return 1;
            }
        }
        #endregion

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}