<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="player.aspx.cs" Inherits="GRGcms.Web.admin.manager.player" %>

<%@ Import Namespace="GRGcms.Common" %>

<!DOCTYPE html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width; initial-scale=1.5; user-scalable=no;">
    <title>jPlayer - Circle Player</title>
    <link href="../../scripts/circleplayer/css/not.the.skin.css" rel="stylesheet" />
    <link href="../../scripts/circleplayer/circle.skin/circle.player.css" rel="stylesheet" />
    <link href="../skin/base.css"  rel="stylesheet" />
    <link href="../skin/default/style.css"  rel="stylesheet" />
    <script src="../../scripts/jquery/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="../../scripts/circleplayer/js/jquery.transform2d.js"></script>
    <script type="text/javascript" src="../../scripts/circleplayer/js/jquery.grab.js"></script>
    <script type="text/javascript" src="../../scripts/circleplayer/js/jquery.jplayer.js"></script>
    <script type="text/javascript" src="../../scripts/circleplayer/js/mod.csstransforms.min.js"></script>
    <script type="text/javascript" src="../../scripts/circleplayer/js/circle.player.js"></script>

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
        $(document).ready(function () {
            var url = $.fn.getString("url")
            if (url != null) {
                var myCirclePlayer = new CirclePlayer("#jquery_jplayer",
                {
                    m4a: url
                }, {
                    cssSelectorAncestor: "#cp_container"
                });
               
            }
        });
		</script>
</head>
<body>

    <!-- This is the 2nd instance's jPlayer div -->
    <div id="jquery_jplayer" class="cp-jplayer"></div>

    <div class="prototype-wrapper" style="display:inline-block;">
        <div id="cp_container" class="cp-container">
            <div class="cp-buffer-holder">
                <div class="cp-buffer-1"></div>
                <div class="cp-buffer-2"></div>
            </div>
            <div class="cp-progress-holder">
                <div class="cp-progress-1"></div>
                <div class="cp-progress-2"></div>
            </div>
            <div class="cp-circle-control"></div>
            <ul class="cp-controls">
                <li><a class="cp-play" tabindex="1">play</a></li>
                <li><a class="cp-pause" style="display: none;" tabindex="1">pause</a></li>
            </ul>
        </div>
    </div>
    <div class="table-container" style="display:inline-block;position:absolute;width:60%;overflow:auto;height:250px;">
        <asp:Repeater ID="rptList" runat="server">
            <HeaderTemplate>
                <table style="width:100%;min-width:100px;" border="0" cellspacing="0" cellpadding="0" class="ltable">
                    <tr>
                        <th style="width:50%;">时间</th>
                        <th style="width:25%;">发起人</th>
                        <th style="width:25%;">发起事件</th>
                    </tr>
            </HeaderTemplate>
            <ItemTemplate>
                <tr>
                    <td><%# Eval("event_time")%></td>
                    <td><%# Eval("startor") %></td>
                    <td><%# getEventName(Eval("event_name").ToString()) %></td>
                </tr>
            </ItemTemplate>
            <FooterTemplate>
                <%#rptList.Items.Count == 0 ? "<tr><td align=\"center\" colspan=\"7\">暂无记录</td></tr>" : ""%>
            </table>
            </FooterTemplate>
        </asp:Repeater>
    </div>
</body>
</html>

