var mapControl = {
    map: null,
    convertor: new BMap.Convertor(),
    markers: [],
    initMap: function () {
        // 百度地图API功能
        mapControl.map = new BMap.Map("allmap");    // 创建Map实例
        mapControl.map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        mapControl.map.addControl(new BMap.MapTypeControl({
            mapTypes: [
                    BMAP_NORMAL_MAP,
                    BMAP_HYBRID_MAP
                ]
        }));
        var buttom_right_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_BOTTOM_LEFT }); // 右下角，添加比例尺
        var buttom_right_navigation = new BMap.NavigationControl({ anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM, offset: new BMap.Size(20, 50) }); //左上角
        mapControl.map.addControl(buttom_right_control);
        mapControl.map.addControl(buttom_right_navigation);
        mapControl.map.setCurrentCity("成都");          // 设置地图显示的城市 此项是必须设置的
        mapControl.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    },
    initPagination: function (count) {
        mapControl.map.clearOverlays();
        $(".pagination").pagination(count, {
            next_text: ">",
            prev_text: "<",
            num_edge_entries: 2,
            num_display_entries: 2,
            callback:mapControl.pageTurn
        });
    },
    pageTurn: function(currentPage, e) {
        mapControl.getData($(".keyword").val(), currentPage+1);
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
        $(".pdt-stop").off("click").on("click", function (e){
            var param = $(this).attr("param");
            mapControl.updateUser("stop", param);
        });
        $(".pdt-kill").off("click").on("click", function (e){
            var param = $(this).attr("param");
            mapControl.updateUser("kill", param);
        });
        $(".app-disable").off("click").on("click", function (e){
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
        mapControl.convertor.translate(pointArr, 1, 5, mapControl.translateCallback)
    },
    translateCallback: function(data) {
        if(data.status === 0) {
            mapControl.markers = [];
            for (var i = 0; i < data.points.length; i++) {
                var iconIdx = i+1;
                var markerIcon = mapControl["icon_" + iconIdx];
                var marker = new BMap.Marker(data.points[i], { icon: markerIcon });
                mapControl.markers.push(marker);
                mapControl.map.addOverlay(marker);
            }
        }
    },
    locate: function (index, x, y){
        var marker = mapControl.markers[index];
        //var point = new BMap.Point(x, y);
        mapControl.map.centerAndZoom(marker.getPosition(), 19);
        //marker.setTop(true);
    },
    renderResult: function(data) {
        //mapControl.removeResult();
        ReactDOM.render(
            <Mapresult items={data}/>,
            document.getElementById('map-result')
        );
    },
    removeResult: function() {
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
}

$(function () {
    mapControl.initMap();
    mapControl.initDataAction();
});

function Mapclose() {
    return (
        <div class="row result-close">
            <div class="col-xs-12 icon">
                <i class="glyphicon glyphicon-remove clear-result"></i>
            </div>
        </div>
    );
}

function Maplist(props) {
    const items = props.items;
    if (!items || items.length < 1) {
        return (
            <Withoutdata />
        );
    }
    const resultItems = items.map((item, index) => 
        <div class="row result-item">
            <div class="col-xs-2 icon">
                <i class={"marker-"+(index+1)} longtitude={item.longtitude} latitude={item.latitude} index={index}></i>
            </div>
            <div class="col-xs-10 result">
                <div class="value">
                    <span class="title">{item.dept} {item.name}({item.account})</span>
                    <span class="subtitle">组别：{item.group}({item.groupid})</span>
                </div>
                {item.devicetype == 'PDT'? (
                    <div class="operate">
                        <button type="button" class="btn btn-inline-block btn-primary btn-xs pdt-stop" param={item.account}>遥晕</button>
                        <button type="button" class="btn btn-inline-block btn-danger btn-xs pdt-kill" param={item.account}>遥毙</button>
                    </div>
                ) : (
                    <div class="operate">
                        <button type="button" class="btn btn-inline-block btn-danger btn-xs app-disable" param={item.account}>禁用</button>
                    </div>
                )}
            </div>
        </div>
    );
    return(
        <div>{resultItems}</div>
    );
}

function Mappage() {
    return(
        <div class="pagination"></div>
    );
}

function Mapresult(props) {
    return (
        <div id="search-result" class="search-result custom-scroll">
            <Mapclose />
            <Maplist items={props.items}/>
            <Mappage />
        </div>
    );
}

function Withoutdata() {
    return (
        <div class="no-data">
            <span>查找不到相关数据</span>
            <span>请更换查询条件</span>
        </div>
    );
}
