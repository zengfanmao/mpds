<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="group_edit.aspx.cs" Inherits="GRGcms.Web.admin.manager.groud_edit" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑通话组</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link rel="stylesheet" type="text/css" href="../../scripts/artdialog/ui-dialog.css" />
    <link rel="stylesheet" type="text/css" href="../skin/icon/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="../skin/default/style.css" />
    <link rel="stylesheet" type="text/css" href="../../scripts/select2/select2.min.css" />
    <script type="text/javascript" charset="utf-8" src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/jquery/Validform_v5.3.2_min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/artdialog/dialog-plus-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../scripts/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/uploader.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/common.js"></script>
    <script type="text/javascript" charset="utf-8" src="../js/laymain.js"></script>    
    <script type="text/javascript" charset="utf-8" src="../../scripts/select2/select2.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //初始化表单验证
            $("#form1").initValidform();
            //初始化上传控件
            $(".upload-img").InitUploader({ sendurl: "../../tools/upload_ajax.ashx", swf: "../../scripts/webuploader/uploader.swf" });
            $('#ddtType').on('change', function () {
                var selectedValue = $(this).val();
                if (selectedValue == "PDT") {
                    $("#dlGroup").hide();
                }
                else {
                    $("#dlGroup").show();
                }              
            });

            $('.groupList').select2({ placeholder: '请选择融合组' });

            $('.groupList').on('change', function () {
                $('#groupText').val($(".groupList").find("option:selected").text());
            });

            $('.groupList').val('<%=relatedGroup%>').select2();
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
            <a href="group_list.aspx"><span>通话组列表</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <span>编辑通话组</span>
        </div>
        <div class="line10"></div>
        <!--/导航栏-->

        <!--内容-->
        <div id="floatHead" class="content-tab-wrap">
            <div class="content-tab">
                <div class="content-tab-ul-wrap">
                    <ul>
                        <li><a class="selected" href="javascript:;">通话组信息</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="tab-content">

            <dl>
                <dt>通话组名</dt>
                <dd>
                    <asp:TextBox ID="txtGroupName" runat="server" CssClass="input normal" sucmsg=" " ajaxurl="../../tools/admin_ajax.ashx?action=group_validate"></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
            </dl>
            <dl>
                <dt>通话组标识</dt>
                <dd>
                    <asp:TextBox ID="txtCode" runat="server" CssClass="input normal" sucmsg=" " ajaxurl="../../tools/admin_ajax.ashx?action=group_validate"></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
            </dl>
            <dl>
                <dt>是否启用</dt>
                <dd>
                    <div class="rule-single-checkbox">
                        <asp:CheckBox ID="cbIsLock" runat="server" Checked="True" />
                    </div>
                    <span class="Validform_checktip">*</span>
                </dd>
            </dl>

            <dl>
                <dt>单位名称</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddlDept" runat="server"></asp:DropDownList>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt>类型</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddtType" runat="server" datatype="*" errormsg="请选择通话组类型" sucmsg=" ">
                            <asp:ListItem  Value="PDT">PDT</asp:ListItem>
                            <asp:ListItem Selected="True" Value="APP">APP</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>

            <dl>
                <dt>分级</dt>
                <dd>
                    <div class="rule-single-select">
                        <asp:DropDownList ID="ddtClass" runat="server" datatype="*" errormsg="请选择通话组类型" sucmsg=" ">
                            <asp:ListItem Selected="True" Value="0">普通组</asp:ListItem>
                            <asp:ListItem Value="1">授权组</asp:ListItem>
                        </asp:DropDownList>
                    </div>
                </dd>
            </dl>

            <dl id="dlGroup">
                <dt>融合组</dt>
                <dd>
                    <select class="groupList" name="selectedGroup" style="width:200px;">
                        <asp:Repeater ID="groupList" runat="server">
                            <ItemTemplate>
                                <option value="<%#Eval("discussionCode")%>"><%#Eval("discussionName")%></option>
                            </ItemTemplate>
                        </asp:Repeater>
                    </select>
                    <input id="groupText" name="groupText" type="hidden" value="<%=relatedGroupTxt%>"/>
                </dd>
            </dl>

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
