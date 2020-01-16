<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="casemessage_list.aspx.cs" Inherits="GRGcms.Web.admin.manager.casemessage_list" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <title>管理员列表</title>
    <link rel="stylesheet" type="text/css" href="../../scripts/artdialog/ui-dialog.css" />
    <link rel="stylesheet" type="text/css" href="../../css/pagination.css" />
    <link rel="stylesheet" type="text/css" href="../skin/icon/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="../skin/default/style.css" />
    <script type="text/javascript" src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="../../scripts/artdialog/dialog-plus-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/laymain.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/common.js"></script>
    <script type="text/javascript">
        $(function () {

            $(".btnPlay").click(function () {
                var url = $(this).attr("url");
                var time = $(this).attr("data-time");
                var duration = $(this).attr("data-duration");
                var uuid = $(this).attr("data-uuid");
                parent.layer.open({
                    type: 2,
                    area: ['600px', '300px'],
                    title: '播放录音  ' + time + "/" + duration,
                    content: ["manager/player.aspx?url=" + url + "&uuid=" + uuid, 'no']
                });
            });
        })
       
    </script>
</head>

<body class="mainbody">
    <form id="form1" runat="server">
        <!--导航栏-->
        <div class="location">
            <a href="javascript:history.back(-1);" class="back"><i class="iconfont icon-up"></i><span>返回上一页</span></a>
            <a href="../center.aspx" class="home"><i class="iconfont icon-home"></i><span>首页</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <span>语言回放</span>
        </div>
        <!--/导航栏-->

        <!--工具栏-->
        <div id="floatHead" class="toolbar-wrap">
            <div class="toolbar">
                <div class="box-wrap">
                    <a class="menu-btn"><i class="iconfont icon-more"></i></a>
                    <div class="l-list">
                        <ul class="icon-list">
                            <li><a href="javascript:;" onclick="checkAll(this);"><i class="iconfont icon-check"></i><span>全选</span></a></li>
                            <!--
                            <li>
                                <asp:LinkButton ID="btnDelete" runat="server" OnClientClick="return ExePostBack('btnDelete');" OnClick="btnDelete_Click"><i class="iconfont icon-delete"></i><span>删除</span></asp:LinkButton>
                            </li>
                            -->
                        </ul>
                    </div>
                    <div class="r-list">
                        <asp:TextBox ID="txtKeywords" runat="server" CssClass="keyword" placeholder="请输入用户/组名/组编号"/>
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
                            <th width="10%">选择</th>
                            <th align="left">发送人</th>
                            <th align="left" width="15%">接收群组/个人</th>
                            <th align="left" width="12%">时间</th>
                            <th align="left" width="6%">时长</th>
                            <th align="left" width="15%">类型</th>
                            <th align="left" width="15%">操作</th>

                        </tr>
                </HeaderTemplate>
                <ItemTemplate>
                    <tr>
                        <td align="center">
                            <asp:CheckBox ID="chkId" CssClass="checkall" runat="server" Style="vertical-align: middle;" />
                            <asp:HiddenField ID="hidId" Value='<%#Eval("ID")%>' runat="server" />
                        </td>
                        <td><%# Eval("uName") %></td>
                        <td><%# Eval("receiver") %></td>
                        <td><%#string.Format("{0:g}",Eval("msgTime"))%></td>
                        <td><%# Eval("fduration") %></td>
                        <td><%# Eval("msgFromType").ToString().Trim() == "Group" ? "群组" : "个人"%></td>
                        <td align="left">
                            <a href="javascript:void(0)" url='<%# ConfigurationManager.AppSettings["recordingUrl"].ToString() + Eval("fRelativePath") %>' class="btnPlay" data-time='<%#string.Format("{0:g}",Eval("msgTime"))%>' data-duration='<%# Eval("fduration") %>' data-uuid='<%# Eval("virtualId") %>'>播放</a>
                            <a href="<%# ConfigurationManager.AppSettings["recordingUrl"].ToString() + Eval("fRelativePath") %>">下载</a>
                        </td>
                    </tr>
                </ItemTemplate>
                <FooterTemplate>
                    <%#rptList.Items.Count == 0 ? "<tr><td align=\"center\" colspan=\"7\">暂无记录</td></tr>" : ""%>
                </table>
                </FooterTemplate>
            </asp:Repeater>
        </div>
        <!--/列表-->

        <!--内容底部-->
        <div class="line20"></div>
        <div class="pagelist">
            <div class="l-btns">
                <span>显示</span><asp:TextBox ID="txtPageNum" runat="server" CssClass="pagenum" onkeydown="return checkNumber(event);"
                    OnTextChanged="txtPageNum_TextChanged" AutoPostBack="True"></asp:TextBox><span>条/页</span>
            </div>
            <div id="PageContent" runat="server" class="default"></div>
        </div>
        <!--/内容底部-->
    </form>
</body>
</html>
