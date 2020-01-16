<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="manager_edit.aspx.cs" Inherits="GRGcms.Web.admin.manager.manager_edit" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑用户</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link rel="stylesheet" type="text/css" href="../../scripts/artdialog/ui-dialog.css" />
    <link rel="stylesheet" type="text/css" href="../skin/icon/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="../skin/default/style.css" />
    <script type="text/javascript" charset="utf-8" src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/jquery/Validform_v5.3.2_min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/artdialog/dialog-plus-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/uploader.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/common.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/laymain.js"></script>
    <script type="text/javascript">
        $(function () {
            //初始化表单验证
            $("#form1").initValidform();
            //初始化上传控件
            $(".upload-img").InitUploader({ sendurl: "../../tools/upload_ajax.ashx", swf: "../../scripts/webuploader/uploader.swf" });
        });
    </script>
</head>

<body class="mainbody">
    <form id="form1" runat="server">
        <!--导航栏-->
        <div class="location">
            <a href="manager_list.aspx" class="back"><i class="iconfont icon-up"></i><span>返回列表页</span></a>
            <a href="../center.aspx"><i class="iconfont icon-home"></i><span>首页</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <a href="manager_list.aspx"><span>用户列表</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <span>编辑用户</span>
        </div>
        <div class="line10"></div>
        <!--/导航栏-->

        <!--内容-->
        <div id="floatHead" class="content-tab-wrap">
            <div class="content-tab">
                <div class="content-tab-ul-wrap">
                    <ul>
                        <li><a class="selected" href="javascript:;">用户信息</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="tab-content">
            <dl>
                <dt>用户角色</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlRoleId" runat="server" datatype="*" errormsg="请选择用户角色" sucmsg=" "></asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>是否启用</dt>
                <dd>
                    <div class="rule-single-checkbox">
                        <asp:CheckBox ID="cbIsLock" runat="server" Checked="True" />
                    </div>
                    <span class="Validform_checktip">*不启用则无法使用该账户登录</span>
                </dd>
            </dl>
            <%--  <dl>
    <dt>信息待审</dt>
    <dd>
      <div class="rule-single-checkbox">
          <asp:CheckBox ID="cbIsAudit" runat="server" />
      </div>
      <span class="Validform_checktip">*发布的文章是否需要审核才显示</span>
    </dd>
  </dl>--%>
            <dl>
                <dt>用户名</dt>
                <dd>
                    <asp:TextBox ID="txtUserName" runat="server" CssClass="input normal" datatype="/^[a-zA-Z0-9\-\_]{2,50}$/" sucmsg=" " ajaxurl="../../tools/admin_ajax.ashx?action=manager_validate"></asp:TextBox>
                    <span class="Validform_checktip">*字母、下划线，不可修改</span></dd>
            </dl>
            <dl>
                <dt>所属机构</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlDept" runat="server" datatype="*" errormsg="请选择用户机构"></asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>登录密码</dt>
                <dd>
                    <asp:TextBox ID="txtPassword" Text="admin888" Enabled="false" runat="server" CssClass="input normal" TextMode="Password" datatype="*6-20" nullmsg="请设置密码" errormsg="密码范围在6-20位之间" sucmsg=" "></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
            </dl>
            <dl>
                <dt>确认密码</dt>
                <dd>
                    <asp:TextBox ID="txtPassword1" Text="admin888" Enabled="false" runat="server" CssClass="input normal" TextMode="Password" datatype="*" recheck="txtPassword" nullmsg="请再输入一次密码" errormsg="两次输入的密码不一致" sucmsg=" "></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
            </dl>
            <dl>
                <dt>头像</dt>
                <dd>
                    <asp:TextBox ID="txtAvatar" runat="server" CssClass="input normal upload-path" />
                    <div class="upload-box upload-img"></div>
                    <div>文件格式：jpg,jpeg,png,gif</div>
                </dd>
            </dl>
            <dl>
                <dt>姓名</dt>
                <dd>
                    <asp:TextBox ID="txtRealName" runat="server" CssClass="input normal"></asp:TextBox></dd>
            </dl>
            <dl>
                <dt>用户性别</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlSex" runat="server" datatype="*" errormsg="请选择用户性别" sucmsg=" ">
                            <asp:ListItem Selected="True" Value="男">男</asp:ListItem>
                            <asp:ListItem Value="女">女</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>警种</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlAccountType" runat="server" datatype="*" errormsg="请选择帐户类型" sucmsg=" ">
                            <asp:ListItem Selected="True" Value="治安警察">治安警察</asp:ListItem>
                            <asp:ListItem Value="刑事警察">刑事警察</asp:ListItem>
                            <asp:ListItem Value="交通警察">交通警察</asp:ListItem>
                            <asp:ListItem Value="巡逻警察">巡逻警察</asp:ListItem>
                            <asp:ListItem Value="特种警察">特种警察</asp:ListItem>
                            <asp:ListItem Value="武装警察">武装警察</asp:ListItem>
                            <asp:ListItem Value="空中警察">空中警察</asp:ListItem>
                            <asp:ListItem Value="中国海警">中国海警</asp:ListItem>
                            <asp:ListItem Value="其他">其他</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>用户职务</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlDuty" runat="server" datatype="*" errormsg="请选择职位" sucmsg=" ">
                            <asp:ListItem Value="总警监、副总警监">总警监、副总警监</asp:ListItem>
                            <asp:ListItem Value="警监">警监</asp:ListItem>
                            <asp:ListItem Value="警督">警督</asp:ListItem>
                            <asp:ListItem Value="警司">警司</asp:ListItem>
                            <asp:ListItem Selected="True" Value="警员">警员</asp:ListItem>
                            <asp:ListItem Value="其他">其他</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>设备用途</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="purpose" runat="server" datatype="*" errormsg="请选择用途" sucmsg=" ">
                            <asp:ListItem Value="公用">公用</asp:ListItem>
                            <asp:ListItem Value="私用">私用</asp:ListItem>
                            <asp:ListItem Value="备用">备用</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>

                <dt>联系电话</dt>
                <dd>
                    <asp:TextBox ID="txtTelephone" runat="server" CssClass="input normal"></asp:TextBox></dd>
            </dl>
            <dl>

                <dt>警员编号</dt>
                <dd>
                    <asp:TextBox ID="txtPcNum" runat="server" CssClass="input normal"></asp:TextBox></dd>
            </dl>
            <dl>
                <dt>移动台类别</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="deviceType" runat="server" datatype="*" errormsg="请选择类别" sucmsg=" ">
                            <asp:ListItem Selected="True" Value="APP">APP</asp:ListItem>
                            <asp:ListItem Value="PDT">PDT</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>移动台号</dt>
                <dd>
                    <asp:TextBox ID="txtDeviceId" runat="server" CssClass="input small" nullmsg="不能为空"  datatype="n" sucmsg=" " OnKeyPress="if (event.keyCode < 48 || event.keyCode >57) event.returnValue = false;"></asp:TextBox>

                    <span class="Validform_checktip">*台号只能为数字</span>
                </dd>
                
            </dl>
            <dl>
                <dt>移动台ESN</dt>
                <dd>
                    <asp:TextBox ID="txtDeviceESN" runat="server" CssClass="input normal"></asp:TextBox></dd>
            </dl>
            <!--
            <dl>
                <dt>当前所在组</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlGroup" runat="server"></asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>当前状态</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlStatus" runat="server" datatype="*" errormsg="请选择状态" sucmsg=" ">
                            <asp:ListItem Selected="True" Value="离线">离线</asp:ListItem>
                            <asp:ListItem Value="在线">在线</asp:ListItem>
                            <asp:ListItem Value="遥晕">遥晕</asp:ListItem>
                            <asp:ListItem Value="摇毙">摇毙</asp:ListItem>
                            <asp:ListItem Value="禁用">禁用</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>
            -->
        </div>
        <!--/内容-->

        <!--工具栏-->
        <div class="page-footer">
            <div class="btn-wrap">
                <asp:Button ID="btnSubmit" runat="server" Text="提交保存" CssClass="btn" OnClick="btnSubmit_Click" />
                <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript: history.back(-1);" />
            </div>
        </div>
        <!--/工具栏-->

    </form>
</body>
</html>
