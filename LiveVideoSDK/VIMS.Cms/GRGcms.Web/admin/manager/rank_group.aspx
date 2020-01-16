<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="rank_group.aspx.cs" Inherits="GRGcms.Web.admin.manager.rank_group" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <title>组群管理</title>
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
             $("#btnAdd").click(function () {
                 var rank = $("#hidUcode").val();
                 parent.layer.open({
                     type: 2,
                     area: ['700px', '600px'],
                     title: '选择组',
                     btn: ["确认", "取消"],
                     yes: function (index, layero) {
                         var iframeWin = layero.find('iframe')[0];
                         iframeWin.contentWindow.selectGroupForm.save();
                     },
                     end: function () {
                         window.location.reload();
                     },
                     content: ["manager/rank_group_select.aspx?rank=" + rank, 'no']
                 });
             });
             //删除
             $("#btnDelete").click(function () {
                 var Ids = [];
                 $.each($('table input:checkbox'), function () {
                     if (this.checked) {
                         Ids.push($(this).val());
                     }
                 });
                 if (Ids.length > 0) {
                     $.ajax({
                         url: '/tools/admin_ajax.ashx',
                         type: 'GET', //GET                       
                         data: {
                             action: "rank_group_delete", groups: Ids.join(",")
                         },
                         timeout: 5000,    //超时时间
                         dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text                      
                         success: function (data, textStatus, jqXHR) {
                             window.location.reload();
                         }
                     })
                 }
             });

         })
    </script>
</head>

<body class="mainbody" style="padding: 0px 10px 10px 10px!important;">
    
    <form id="form1" runat="server">
        <asp:HiddenField ID="hidUcode"  runat="server" />
        <!--工具栏-->
        <div id="floatHead" class="toolbar-wrap">
            <div class="toolbar">
                <div class="box-wrap">
                    <a class="menu-btn"><i class="iconfont icon-more"></i></a>
                    <div class="l-list">
                        <ul class="icon-list">
                            <li><a href="javascript:void(0);" id="btnAdd" ><i class="iconfont icon-close"></i><span>新增</span></a></li>
                            <li><a href="javascript:void(0);" onclick="checkFrontAll(this);"><i class="iconfont icon-check"></i><span>全选</span></a></li>
                            <li><a href="javascript:void(0);" id="btnDelete"><i class="iconfont icon-delete"></i><span>删除</span></a></li>
                        </ul>
                    </div>
                    <div class="r-list">
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
                        <thead>
                            <tr>
                                <th width="8%">选择</th>
                                <th align="left" width="30%">组编号</th>
                                <th align="left" width="30%">通话组名</th>
                            </tr>
                        </thead>
                </HeaderTemplate>
                <ItemTemplate>
                    <tbody>
                        <tr>
                            <td align="center">
                                <input type="checkbox" ID="chkId_<%#Eval("ID")%>" value="<%#Eval("ID")%>" class="checkall" Style="vertical-align: middle;" />
                            </td>
                            <td><%#Eval("discussionCode")%></td>
                            <td><%#Eval("discussionName")%></td>
                        </tr>
                    </tbody>
                </ItemTemplate>
                <FooterTemplate>
                    <%#rptList.Items.Count == 0 ? "<tr><td align=\"center\" colspan=\"5\">暂无记录</td></tr>" : ""%>
  </table>
                </FooterTemplate>
            </asp:Repeater>
        </div>
        <!--/列表-->

        <!--内容底部-->
        <div class="pagelist">
            <div class="l-btns">
                <span>显示</span><asp:TextBox Enabled="false" ID="txtPageNum" runat="server" CssClass="pagenum" onkeydown="return checkNumber(event);"
                    OnTextChanged="txtPageNum_TextChanged" AutoPostBack="True"></asp:TextBox><span>条/页</span>
            </div>
            <div id="PageContent" runat="server" class="default"></div>
        </div>
        <!--/内容底部-->
    </form>
</body>
</html>
