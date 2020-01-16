var mapControl = {
    map: null,
    convertor: {},
    markers: [],
    initMap: function () {
        // 百度地图API功能
        mapControl.map = new BMap.Map("allmap"); // 创建Map实例
        mapControl.map.centerAndZoom(new BMap.Point(113.343606442737, 23.1639971698391), 10); // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        mapControl.map.addControl(new BMap.MapTypeControl({
            mapTypes: [BMAP_NORMAL_MAP, BMAP_HYBRID_MAP]
        }));
        var buttom_right_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_BOTTOM_LEFT }); // 右下角，添加比例尺
        var buttom_right_navigation = new BMap.NavigationControl({ anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM, offset: new BMap.Size(20, 50) }); //左上角
        mapControl.map.addControl(buttom_right_control);
        mapControl.map.addControl(buttom_right_navigation);
        mapControl.map.setCurrentCity("广州"); // 设置地图显示的城市 此项是必须设置的
        mapControl.map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
    },
    initPagination: function (count) {
        mapControl.map.clearOverlays();
        $(".pagination").pagination(count, {
            next_text: ">",
            prev_text: "<",
            num_edge_entries: 2,
            num_display_entries: 2,
            callback: mapControl.pageTurn
        });
    },
    pageTurn: function (currentPage, e) {
        mapControl.getData($(".keyword").val(), currentPage + 1);
    },
    initDataAction: function () {
        $(".btn-search").off("click").on("click", function () {
            mapControl.getData($(".keyword").val(), 1);
            mapControl.getDataCount($(".keyword").val());
        });
    },
    initAction: function () {
        $(".clear-result").off("click").on("click", function () {
            mapControl.removeResult();
            $(".keyword").val("");
            mapControl.map.clearOverlays();
        });
        $(".result-item").off("click").on("click", function () {
            var icon = $(this).find("i")[0];
            var longtitude = $(icon).attr("longtitude");
            var latitude = $(icon).attr("latitude");
            var index = $(icon).attr("index");
            mapControl.locate(index, longtitude, latitude);
        });
        $(".pdt-stop").off("click").on("click", function (e) {
            var param = $(this).attr("param");
            mapControl.updateUser("stop", param);
        });
        $(".pdt-kill").off("click").on("click", function (e) {
            var param = $(this).attr("param");
            mapControl.updateUser("kill", param);
        });
        $(".app-disable").off("click").on("click", function (e) {
            var param = $(this).attr("param");
            mapControl.updateUser("disable", param);
        });
    },
    updateUser: function (action, account) {
        $.ajax({
            type: "post",
            url: "../tools/map_ajax.ashx?action=" + action + "&account=" + account,
            dataType: "json",
            success: function (data, textStatus) {
                if (data.status == 0) {
                    layer.msg('用户状态更新成功!');
                    $(".btn-search").trigger("click");
                } else {
                    layer.msg('用户状态更新失败、请联系管理员!');
                }
            }
        });
    },
    getDataCount: function (keywords) {
        $.ajax({
            type: "post",
            url: "../tools/map_ajax.ashx?action=user_count&keywords=" + keywords,
            dataType: "json",
            success: function (data, textStatus) {
                if (data > 0) {
                    mapControl.initPagination(data);
                }
            }
        });
    },
    getData: function (keywords, page) {
        $.ajax({
            type: "post",
            url: "../tools/map_ajax.ashx?action=user&keywords=" + keywords + "&page=" + page,
            dataType: "json",
            success: function (data, textStatus) {
                mapControl.renderResult(data);
                mapControl.addMarker(data);
                mapControl.initAction();
            }
        });
    },
    addMarker: function (dt) {
        var pointArr = [];
        $.each(dt, function (index, obj) {
            //var iconIdx = index+1;
            //var markerIcon = mapControl["icon_" + iconIdx];
            var pt = new BMap.Point(obj.longtitude, obj.latitude);
            pointArr.push(pt);
            //var marker = new BMap.Marker(pt, { icon: markerIcon });
            //mapControl.map.addOverlay(marker);
        });
        mapControl.convertor.translate(pointArr, 1, 5, mapControl.translateCallback);
    },
    translateCallback: function (data) {
        if (data.status === 0) {
            mapControl.markers = [];
            for (var i = 0; i < data.points.length; i++) {
                var iconIdx = i + 1;
                var markerIcon = mapControl["icon_" + iconIdx];
                var marker = new BMap.Marker(data.points[i], { icon: markerIcon });
                mapControl.markers.push(marker);
                mapControl.map.addOverlay(marker);
            }
        }
    },
    locate: function (index, x, y) {
        var marker = mapControl.markers[index];
        //var point = new BMap.Point(x, y);
        mapControl.map.centerAndZoom(marker.getPosition(), 19);
        //marker.setTop(true);
    },
    renderResult: function (data) {
        //mapControl.removeResult();
        ReactDOM.render(React.createElement(Mapresult, { items: data }), document.getElementById('map-result'));
    },
    removeResult: function () {
        ReactDOM.unmountComponentAtNode(document.getElementById('map-result'));
    },
    icon_1: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(0, -139) }),
    icon_2: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-18, -139) }),
    icon_3: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-36, -139) }),
    icon_4: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-54, -139) }),
    icon_5: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-72, -139) }),
    icon_6: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-90, -139) }),
    icon_7: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-108, -139) }),
    icon_8: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-126, -139) }),
    icon_9: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-144, -139) }),
    icon_10: new BMap.Icon("../../images/markers_map.png", new BMap.Size(18, 27), { imageOffset: new BMap.Size(-162, -139) })
};

$(function () {
    mapControl.initMap();
    mapControl.initDataAction();
});

function Mapclose() {
    return React.createElement(
        "div",
        { "class": "row result-close" },
        React.createElement(
            "div",
            { "class": "col-xs-12 icon" },
            React.createElement("i", { "class": "glyphicon glyphicon-remove clear-result" })
        )
    );
}

function Maplist(props) {
    const items = props.items;
    if (!items || items.length < 1) {
        return React.createElement(Withoutdata, null);
    }
    const resultItems = items.map((item, index) => React.createElement(
        "div",
        { "class": "row result-item" },
        React.createElement(
            "div",
            { "class": "col-xs-2 icon" },
            React.createElement("i", { "class": "marker-" + (index + 1), longtitude: item.longtitude, latitude: item.latitude, index: index })
        ),
        React.createElement(
            "div",
            { "class": "col-xs-10 result" },
            React.createElement(
                "div",
                { "class": "value" },
                React.createElement(
                    "span",
                    { "class": "title" },
                    item.dept,
                    " ",
                    item.name,
                    "(",
                    item.account,
                    ")"
                ),
                React.createElement(
                    "span",
                    { "class": "subtitle" },
                    "\u7EC4\u522B\uFF1A",
                    item.group,
                    "(",
                    item.groupid,
                    ")"
                )
            ),
            item.devicetype == 'PDT' ? React.createElement(
                "div",
                { "class": "operate" },
                React.createElement(
                    "button",
                    { type: "button", "class": "btn btn-inline-block btn-primary btn-xs pdt-stop", param: item.account },
                    "\u9065\u6655"
                ),
                React.createElement(
                    "button",
                    { type: "button", "class": "btn btn-inline-block btn-danger btn-xs pdt-kill", param: item.account },
                    "\u9065\u6BD9"
                )
            ) : React.createElement(
                "div",
                { "class": "operate" },
                React.createElement(
                    "button",
                    { type: "button", "class": "btn btn-inline-block btn-danger btn-xs app-disable", param: item.account },
                    "\u7981\u7528"
                )
            )
        )
    ));
    return React.createElement(
        "div",
        null,
        resultItems
    );
}

function Mappage() {
    return React.createElement("div", { "class": "pagination" });
}

function Mapresult(props) {
    return React.createElement(
        "div",
        { id: "search-result", "class": "search-result custom-scroll" },
        React.createElement(Mapclose, null),
        React.createElement(Maplist, { items: props.items }),
        React.createElement(Mappage, null)
    );
}

function Withoutdata() {
    return React.createElement(
        "div",
        { "class": "no-data" },
        React.createElement(
            "span",
            null,
            "\u67E5\u627E\u4E0D\u5230\u76F8\u5173\u6570\u636E"
        ),
        React.createElement(
            "span",
            null,
            "\u8BF7\u66F4\u6362\u67E5\u8BE2\u6761\u4EF6"
        )
    );
}