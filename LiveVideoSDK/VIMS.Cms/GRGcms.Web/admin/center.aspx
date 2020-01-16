<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="center.aspx.cs" Inherits="GRGcms.Web.admin.map" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>管理首页</title>
<link rel="stylesheet" type="text/css" href="skin/icon/iconfont.css" />
<link rel="stylesheet" type="text/css" href="skin/default/style.css" />
<link rel="stylesheet" type="text/css" href="../scripts/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="../scripts/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="../scripts/jquery/pagination.css">
<link rel="stylesheet" type="text/css" href="../css/map-marker.css">
<script type="text/javascript" charset="utf-8" src="../scripts/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" charset="utf-8" src="../scripts/jquery/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="../scripts/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="../scripts/react/react.production.min.js"></script>
<script type="text/javascript" charset="utf-8" src="../scripts/react/react-dom.production.min.js"></script>
<script type="text/javascript" charset="utf-8" src="../scripts/layer/layer.js"></script>
<script type="text/javascript" charset="utf-8" src="js/layindex.js"></script>
<script type="text/javascript" charset="utf-8" src="js/common.js"></script>
<script type="text/javascript" charset="utf-8" src="offlinemap/map.js?v=2.1" ></script>
<script type="text/javascript" charset="utf-8" src="offlinemap/map_plus.js?v=2.1" ></script>
<script type="text/javascript" charset="utf-8" src="offlinemap/map_city.js?v=2.1" ></script>
<!--
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=sCTT547lTGSQBMSpB1hQK81uIS8CpCGm"></script>
-->
<script type="text/javascript" charset="utf-8" src="js/map.js"></script>
</head>
<body class="mainbody">
<form id="form1" runat="server">
<!--导航栏-->
<div class="location" style="height:40px; margin-bottom: 5px;">
  <a href="javascript:history.back(-1);" class="back"><i class="iconfont icon-up"></i><span>返回上一页</span></a>
  <a href="javascript:;"><i class="iconfont icon-home"></i><span>首页</span></a>
  <i class="arrow iconfont icon-arrow-right"></i>
  <span>管理首页</span>
</div>
<!--/导航栏-->

<!--内容-->
<div id="allmap"></div>

<div class="instance-search">
    <input id="txtKeywords" class="keyword" type="text" placeholder="请输入用户/群组/机构"/>
    <span id="lbtnSearch" class="btn-search" >
        <i class="iconfont icon-search"></i>
    </span>
</div>

<div id="map-result" class="map-result">
</div>
<!--/内容-->

<script type="text/javascript" src="js/map.js" ></script>
</form>
</body>
</html>
