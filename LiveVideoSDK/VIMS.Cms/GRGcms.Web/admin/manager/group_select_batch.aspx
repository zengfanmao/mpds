<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="group_select_batch.aspx.cs" Inherits="GRGcms.Web.admin.manager.group_select_batch" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <title>批量选择通讯组</title>
    <link rel="stylesheet" type="text/css" href="../../scripts/artdialog/ui-dialog.css" />
    <link rel="stylesheet" type="text/css" href="../../css/pagination.css" />
    <link rel="stylesheet" type="text/css" href="../skin/icon/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="../skin/default/style.css" />
    <script type="text/javascript" src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="../../scripts/artdialog/dialog-plus-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/laymain.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/common.js"></script>
    <script type="text/javascript">
        (function ($) {

            $.fn.extend($.fn, {              
                getString: function (name) {
                    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
                    var r = window.location.search.substr(1).match(reg);
                    if (r != null) return unescape(r[2]);
                    return null;
                }
            });
        })(jQuery);

        var selectGroupForm = {
            
            save: function () {
                var ucodes = $.fn.getString("ids");
                var groups = [];
                $.each($('table input:checkbox'), function () {
                    if (this.checked) {
                        groups.push($(this).val());
                    }
                });
                if (groups.length > 0) {
                    parent.layer.msg('正在处理中，请稍候!', {
                        icon: 16,
                        shade: 0.01
                    });
                    $.ajax({
                        url: '/tools/admin_ajax.ashx',
                        type: 'GET', //GET                       
                        data: {
                            action:"group_add_batch",ucodes: ucodes, groups: groups.join(",")
                        },
                        timeout: 10000,    //超时时间
                        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text                      
                        success: function (data, textStatus, jqXHR) {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        }
                    })
                }
               
            }
        }

    </script>
</head>

<body class="mainbody" style="padding: 0px 10px 10px 10px!important;">
    <form id="form1" runat="server">
     
        <!--工具栏-->
        <div id="floatHead" class="toolbar-wrap" style="padding:unset;">
            <div class="toolbar" style="padding:unset;">
                <div class="box-wrap">
                    <a class="menu-btn"><i class="iconfont icon-more"></i></a>
                    <div class="l-list">
                        <ul class="icon-list">                           
                            <li><a href="javascript:;" onclick="checkFrontAll(this);"><i class="iconfont icon-check"></i><span>全选</span></a></li>                     
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
                            <th width="8%">选择</th>
                            <th align="left" width="12%">组编号</th>
                            <th align="left">通话组名</th>
                            <th align="left" width="20%">单位名称</th>
                            <th width="12%" align="left">类型</th>
                        </tr>
                </HeaderTemplate>
                <ItemTemplate>
                    <tr>
                        <td align="center">
                            <input type="checkbox" ID="chkId_<%#Eval("ID")%>"  value="<%#Eval("ID")%>"  class="checkall"  Style="vertical-align: middle;" />                          
                        </td>
                        <td><%#Eval("discussionCode")%></td>
                        <td><a href="group_edit.aspx?action=<%#DTEnums.ActionEnum.Edit %>&id=<%#Eval("ID")%>"><%#Eval("discussionName")%></a></td>
                        <td><%#Eval("dept")%></td>
                        <td><%#Eval("type")%></td>
                      
                    </tr>
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
