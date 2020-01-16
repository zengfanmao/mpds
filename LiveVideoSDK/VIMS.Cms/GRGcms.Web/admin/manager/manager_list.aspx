<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="manager_list.aspx.cs" Inherits="GRGcms.Web.admin.manager.manager_list" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <title>用户列表</title>
    <link rel="stylesheet" type="text/css" href="../../scripts/artdialog/ui-dialog.css" />
    <link rel="stylesheet" type="text/css" href="../../css/pagination.css" />
    <link rel="stylesheet" type="text/css" href="../skin/icon/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="../skin/default/style.css" />
    <script type="text/javascript" src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="../../../scripts/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="../../scripts/artdialog/dialog-plus-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/laymain.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/common.js"></script>
    <script type="text/javascript">
        function showGroupUser(ucode)
        {
            parent.layer.open({
                type: 2,
                area: ['1000px', '560px'],
                title:'关联组管理',
                content: ["manager/group_user.aspx?keywords=" + ucode, 'no']
            });
        }
        function batchSelectGroup() {
            var ucode = "12";
            var names = [];
            var ids = [];
            $.each($('table input:checkbox'), function () {
                if (this.checked) {
                    var currenItem = $(this).parent().prev();
                    names.push($(currenItem).attr("name"));
                    ids.push($(currenItem).val());
                }
            });
            if (ids.length > 0) {
                parent.layer.open({
                    type: 2,
                    area: ['1000px', '550px'],
                    title: '批量关联组',
                    btn: ["确认", "取消"],
                    yes: function (index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.selectGroupForm.save();
                    },
                    end: function () {
                        window.location.reload();
                    },
                    content: ["manager/group_select_batch.aspx?names=" + names.join(",")+"&ids="+ids.join(","), 'no']
                });
            }
            else {
                parent.layer.alert('请先选择用户!', { icon: 0 });
            }
        }
    </script>
</head>

<body class="mainbody">
    <form id="form1" runat="server">
        <!--导航栏-->
        <div class="location">
            <a href="javascript:history.back(-1);" class="back"><i class="iconfont icon-up"></i><span>返回上一页</span></a>
            <a href="../center.aspx" class="home"><i class="iconfont icon-home"></i><span>首页</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <span>用户列表</span>
            <asp:Label ID="importResult" runat="server"></asp:Label>
        </div>
        <!--/导航栏-->

        <!--工具栏-->
        <div id="floatHead" class="toolbar-wrap">
            <div class="toolbar">
                <div class="box-wrap">
                    <a class="menu-btn"><i class="iconfont icon-more"></i></a>
                    <div class="l-list">
                        <ul class="icon-list">
                            <li><a href="manager_edit.aspx?action=<%=DTEnums.ActionEnum.Add %>"><i class="iconfont icon-close"></i><span>新增</span></a></li>
                            <li><a href="javascript:;" onclick="checkAll(this);"><i class="iconfont icon-check"></i><span>全选</span></a></li>
                            <li><asp:LinkButton ID="btnDelete" runat="server" OnClientClick="return ExePostBack('btnDelete');" OnClick="btnDelete_Click"><i class="iconfont icon-delete"></i><span>删除</span></asp:LinkButton></li>
                            <li><asp:LinkButton ID="btnStop" runat="server" OnClientClick="return ExeSinglePostBack('btnStop', '您确定执行遥晕操作嘛？');" OnClick="btnStop_Click"><i class="iconfont icon-delete"></i><span>遥晕</span></asp:LinkButton></li>
                            <li><asp:LinkButton ID="btnKill" runat="server" OnClientClick="return ExeSinglePostBack('btnKill', '您确定执行遥毙操作嘛？');" OnClick="btnKill_Click"><i class="iconfont icon-delete"></i><span>遥毙</span></asp:LinkButton></li>
                            <li><asp:LinkButton ID="btnDisable" runat="server" OnClientClick="return ExeSinglePostBack('btnDisable', '您确定执行禁用操作嘛？');" OnClick="btnDisable_Click"><i class="iconfont icon-delete"></i><span>禁用</span></asp:LinkButton></li>
                            <li><asp:LinkButton ID="btnEnable" runat="server" OnClientClick="return ExeSinglePostBack('btnEnable', '您确定执行激活操作嘛？');" OnClick="btnEnable_Click"><i class="iconfont icon-delete"></i><span>激活</span></asp:LinkButton></li>
                            <li><a href="javascript:;" onclick="batchSelectGroup();"><i class="iconfont icon-check"></i><span>批量关联组</span></a></li>
                         </ul>
                        <div class="menu-list">
                            <asp:TextBox ID="startDt" runat="server" CssClass="input rule-date-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></asp:TextBox>
                                -
                            <asp:TextBox ID="endDt" runat="server" CssClass="input rule-date-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></asp:TextBox>
                        </div>
                        <ul class="icon-list">
                            <li>
                                <asp:LinkButton ID="btnImport" runat="server" OnClick="btnImport_Click"><i class="iconfont icon-down"></i><span>PDT数据接入</span></asp:LinkButton>
                            </li>
                        </ul>
                    </div>
                    <div class="r-list">
                        <asp:TextBox ID="txtKeywords" runat="server" CssClass="keyword" />
                        <asp:LinkButton ID="lbtnSearch" runat="server" CssClass="btn-search" OnClick="btnSearch_Click"><i class="iconfont icon-search"></i></asp:LinkButton>
                    </div>
                </div>
            </div>
        </div>
        <!--/工具栏-->

        <!--列表-->
        <div class="table-container">
            <asp:Repeater ID="rptList" runat="server">
                <HeaderTemplate>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ltable">
                        <tr>
                            <th width="5%">选择</th>
                            <th align="left" width="5%">用户名</th>
                            <th align="left" width="5%">姓名</th>
                            <th align="left" width="5%">角色</th>
                            <th align="left" width="4%">警员编号</th>
                            <th align="left" width="4%">职位</th>
                            <th align="left" width="5%">所属部门</th>
                            <th align="left" width="4%">帐户类型</th>
                            <th align="left" width="8%">电话</th>
                            <th align="left" width="5%">当前组</th>
                            <th align="left" width="4%">移动台号</th>
                            <th align="left" width="4%">类别</th>
                            <th align="left" width="8%">添加时间</th>
                            <th width="4%">状态</th>
                            <th width="8%">操作</th>
                        </tr>
                </HeaderTemplate>
                <ItemTemplate>
                    <tr>
                        <td align="center">
                            <input  type="hidden"  value="<%#Eval("uCode")%>" name="<%# Eval("uName") %>" />
                            <asp:CheckBox  ID="chkId" CssClass="checkall" runat="server" Style="vertical-align: middle;" />
                            <asp:HiddenField ID="hidId" Value='<%#Eval("id")%>' runat="server" />
                        </td>
                        <td><a href="manager_edit.aspx?action=<%#DTEnums.ActionEnum.Edit %>&id=<%#Eval("id")%>"><%# Eval("uCode") %></a></td>
                        <td><%# Eval("uName") %></td>
                        <td><%#new GRGcms.BLL.role().GetTitle(Convert.ToInt32(Eval("roleid")))%></td>
                        <td><%# Eval("pcNum") %></td>
                        <td><%# Eval("uDuty") %></td>
                        <td><%# Eval("dName") %></td>
                        <td><%# Eval("accountType") %></td>
                        <td><%# Eval("uTel") %></td>
                        <td><%# Eval("groupName") %></td>
                        <td><%# Eval("deviceid") %></td>
                        <td><%# Eval("devicetype") %></td>
                        <td><%#string.Format("{0:g}",Eval("Createtime"))%></td>

                        <td align="center"><%# Eval("status") %></td>
                        <td align="center">
                            <div style='display:<%# Eval("devicetype").ToString().Trim()=="PDT"?"none":"block" %>'>     
                            <a href="manager_edit.aspx?action=<%#DTEnums.ActionEnum.Edit %>&id=<%#Eval("ID")%>">修改</a>
                            <a href="javascript:void(0)" onclick="showGroupUser('<%# Eval("uCode") %>')">关联组</a>
                                </div>
                        </td>
                    </tr>
                </ItemTemplate>
                <FooterTemplate>
                    <%#rptList.Items.Count == 0 ? "<tr><td align=\"center\" colspan=\"13\">暂无记录</td></tr>" : ""%>
  </table>
                </FooterTemplate>
            </asp:Repeater>
        </div>
        <!--/列表-->

        <!--内容底部-->
        <div class="line20"></div>
        <div class="pagelist">
            <div class="l-btns">
                <span>显示</span><asp:TextBox Enabled="true" ID="txtPageNum" runat="server" CssClass="pagenum" onkeydown="return checkNumber(event);"
                    OnTextChanged="txtPageNum_TextChanged" AutoPostBack="True"></asp:TextBox><span>条/页</span>
            </div>
            <div id="PageContent" runat="server" class="default"></div>
        </div>
        <!--/内容底部-->
    </form>
</body>
</html>
