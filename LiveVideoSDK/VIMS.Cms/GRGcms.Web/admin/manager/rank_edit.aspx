﻿<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="rank_edit.aspx.cs" Inherits="GRGcms.Web.admin.manager.rank_edit" ValidateRequest="false" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑组群</title>
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
</head>

<body class="mainbody">
    <form id="form1" runat="server">
        <!--导航栏-->
        <div class="location">
            <a href="manager_list.aspx" class="back"><i class="iconfont icon-up"></i><span>返回列表页</span></a>
            <a href="../center.aspx"><i class="iconfont icon-home"></i><span>首页</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <a href="group_list.aspx"><span>组群列表</span></a>
            <i class="arrow iconfont icon-arrow-right"></i>
            <span>编辑组群</span>
        </div>
        <div class="line10"></div>
        <!--/导航栏-->

        <!--内容-->
        <div id="floatHead" class="content-tab-wrap">
            <div class="content-tab">
                <div class="content-tab-ul-wrap">
                    <ul>
                        <li><a class="selected" href="javascript:;">组群信息</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="tab-content">

            <dl>
                <dt>组群编码</dt>
                <dd>
                    <asp:TextBox ID="txtRank" runat="server" CssClass="input normal" sucmsg=" " ajaxurl="../../tools/admin_ajax.ashx?action=rank_validate"></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
            </dl>
            <dl>
                <dt>组群名称</dt>
                <dd>
                    <asp:TextBox ID="txtRankName" runat="server" CssClass="input normal" sucmsg=" " ajaxurl="../../tools/admin_ajax.ashx?action=rank_validate"></asp:TextBox>
                    <span class="Validform_checktip">*</span></dd>
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
