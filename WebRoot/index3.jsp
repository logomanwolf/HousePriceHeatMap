<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="css/sliderStyle.css">
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/switch.css">
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=ielFb8cb0CKLGW2nDuWHGBTbejm5fusE"></script>
<script type="text/javascript" src="js/heatmap.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>
<script src="js/jquery-3.3.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="js/spin.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style type="text/css">
body,html {
	width: 100%;
	height: 100%;
	font-family: "微软雅黑";
	font-size: 14px;
}

* {
	margin: 0;
	padding: 0;
}

.left {
	float: left;
}

.right {
	float: right;
}

.clearfix {
	clear: both;
}

.hide {
	display: none;
}

#map {
	height: 628px;
	width: calc(100% - 305px);
	border: 1px solid #dadada;
}

#result {
	height: 520px;
	font-size: 13px;
	line-height: 20px;
	overflow: auto;
	color: #666;
}

#result ul {
	list-style: outside none none;
}

#result ul li {
	border-bottom: 1px solid #dadada;
	padding: 10px;
}

#result ul li:hover {
	background-color: #f0f0f0;
	cursor: pointer;
}

#result .res-data {
	background: url(img/ico_marker.png) no-repeat -1px 15px;
}

#result .res-marker {
	width: 30px;
	height: 58px;
	line-height: 58px;
	text-align: center;
	color: rgb(255, 255, 255);
	font-weight: bold;
}

#result .res-address {
	width: 235px;
}

#result .title {
	font-size: 16px;
	color: #000000;
}

.area-right {
	width: 303px;
}

.area-right .search {
	border-bottom: 1px solid #dadada;
	padding: 8px 0;
	box-shadow: 8px 8px 8px #888888;
	margin-bottom: 8px;
}

.area-right .search .s-address {
	margin-bottom: 5px;
	position: relative;
	border-bottom: 1px solid #DADADA;
	padding: 0 10px;
	height: 32px;
	line-height: 32px;
}

.area-right .search .s-address .btn {
	position: absolute;
	right: 10px;
	top: 5px;
	cursor: pointer;
}

.area-right .search .s-address .btn img {
	width: 22px;
}

.area-right .search .address {
	height: 28px;
	line-height: 28px;
	border: none;
	outline: medium; /*去掉鼠标点击后的边框*/
}

.area-right .search .cur_point {
	color: #1E90FF;
	padding: 0 10px;
	font-size: 13px;
}

.area-right .search .point {
	border: none;
}
</style>

<title>本地搜索的数据接口</title>
</head>
<body>
	<div>
		<div class="left" id="map"></div>
		<div class="left area-right">
			<div class="search">
				<div class="s-address">
					检索地址：<input type="text" class="address" id="address"
						placeholder="望京SOHO" />
					<div onclick="doSearch();" class="btn btn-info">
						<img src="img/ico_btn_search.png" />
					</div>
				</div>
				<div class="cur_point">
					当前坐标：<span id="s-point"></span><br /> 当前检索城市：<span id="s-city"></span>
				</div>
			</div>
			<div id="result">
				<ul>
					<li>
						<div class="res-data">
							<div class="left res-marker">
								<span>A</span>
							</div>
							<div class="left res-address">
								<div class="title">望京</div>
								<div>
									地址：<span class="rout">地铁14号线; 地铁15号线</span>
								</div>
								<div>
									坐标：<span class="point">116.475304,40.004532</span>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
</body>
</html>
<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("map");
	var point = new BMap.Point(${avg_lng}, ${avg_lat});
	map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别 

	map.enableScrollWheelZoom();//启用滚轮放大缩小，默认禁用
	var points = ${usableHouses};
	//#这里面添加房价的经纬度，我的太多了，所以只拿了三个  
	var section = [ 10000, 20000 ];
	if (!isSupportCanvas()) {
		alert('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
	}
	//详细的参数,可以查看heatmap.js的文档 https://github.com/pa7/heatmap.js/blob/master/README.md  
	//参数说明如下:  
	/* visible 热力图是否显示,默认为true  
	 * opacity 热力的透明度,1-100  
	 * radius 势力图的每个点的半径大小  
	 * gradient  {JSON} 热力图的渐变区间 . gradient如下所示  
	 *  {  
	        .2:'rgb(0, 255, 255)',  
	        .5:'rgb(0, 110, 255)',  
	        .8:'rgb(100, 0, 255)'  
	    }  
	    其中 key 表示插值的位置, 0~1.  
	        value 为颜色值.  
	 */

	heatmapOverlay = new BMapLib.HeatmapOverlay({
		"radius" : 15,
		maxOpacity : .99,
		minOpacity : 0,
	});

	/* for (var i = 0; i < 10; i++) {
		var hotPoint = new BMap.Point(points[i].lng, points[i].lat);
		var marker = new BMap.Marker(hotPoint); // 创建标注
		map.addOverlay(marker); // 将标注添加到地图中    

		marker.addEventListener("click", getAttr);
		function getAttr() {
			var p = marker.getPosition(); //获取marker的位置
			p.id = "123";
			alert("点的位置是" + hotPoint.lng + "," + hotPoint.lat);
			alert("marker的位置是" + p.lng + "," + p.lat);
		}
	} */
	map.addOverlay(heatmapOverlay);
	//alert("vvv");
	/* map.addEventListener("mousemove",function(e){
		document.getElementById("_position").value=e.point.lng+","+e.point.lat;
	}); */

	heatmapOverlay.setDataSet({
		data : points,
		max : ${maxCount}

	});
	heatmapOverlay.show();

	//判断浏览区是否支持canvas  
	function isSupportCanvas() {
		var elem = document.createElement('canvas');
		return !!(elem.getContext && elem.getContext('2d'));
	}

	function setGradient() {
		/*格式如下所示:  
		{  
		    0:'rgb(102, 255, 0)',  
		    .5:'rgb(255, 170, 0)',  
		    1:'rgb(255, 0, 0)'  
		}*/
		var gradient = {};
		var colors = document.querySelectorAll("input[type='color']");
		colors = [].slice.call(colors, 0);
		colors.forEach(function(ele) {
			gradient[ele.getAttribute("data-key")] = ele.value;
		});
		heatmapOverlay.setOptions({
			"gradient" : gradient
		});
	}
	//单击获取点击的经纬度
	map.addEventListener("click", function(e) {
		$("#s-point").text(e.point.lng + "," + e.point.lat);

		//	var clickPoint = e.point;
		for (var i = 0; i < points.length; i++) {
			if (Math.abs(e.point.lat - points[i].lat < 0.001)
					&& Math.abs(e.point.lng - points[i].lng) < 0.001) {
				//alert(points[i].lng);
				$.ajax({
					async : false,
					type : "POST",
					url : "evaluate.do",
					//contentType : "charset=utf-8",
					data : {
						lng : points[i].lng,
						lat : points[i].lat
					},
					/* data : {
						"type" : "ys"
					}, */
					dataType : "json",
					success : function(data) {
						//alert("success");
						var msg = JSON.stringify(data);
						var bus = 0;
						var school = 0;
						var supermarket = 0;

						var hotPoint = new BMap.Point(points[i].lng,
								points[i].lat);
						var marker = new BMap.Marker(hotPoint);

						map.addOverlay(marker); // 将标注添加到地图中   
						var dataObj = eval("(" + msg + ")");
						// alert(dataObj.name);
						var _html = "<div>" + dataObj.name + "</div>"
								+ "<div>房价：";
						_html += dataObj.price + "</div>";
						alert("before");///////////////////////////
						searchAndscore(bus, school, supermarket, marker);
						_html += "<div>学校" + school + "</div>";
						_html += "<div>超市" + bus + "</div>";
						_html += "<div>公交站" + supermarket + "</div>";
						marker.addEventListener("click", function(e) {
							this.openInfoWindow(new BMap.InfoWindow(_html));
						});

					},
					error : function(data) {
						alert("fialed");
					},
				});
				break;
			}
		}

	});

	//map展现结果的地图实例
	//autoViewport检索结束后是否自动调整地图视野
	var local = new BMap.LocalSearch(map, {
		renderOptions : {
			map : map,
			autoViewport : true
		}
	});

	//获取坐标周围100m范围内是否有学校，超市等
	function searchAndscore(bus, school, supermarket, marker) {
		map.centerAndZoom(marker.getPosition(), 15);
		var local2 = new BMap.LocalSearch(marker.getPosition());
		local2.setPageCapacity(100);
		local2.searchNearby("公交站", marker.getPosition(), 1000);
		local2.setSearchCompleteCallback(function(results) {
			if (local.getStatus() == BMAP_STATUS_SUCCESS) {

				bus = results.getNumPois();
			}
		});
		local2.searchNearby("超市", marker.getPosition(), 1000);
		local2.setSearchCompleteCallback(function(results) {
			if (local.getStatus() == BMAP_STATUS_SUCCESS) {
				supermarket = results.getNumPois();
				alert( results.getNumPois());
			}
		});
		local2.searchNearby("学校", marker.getPosition(), 1000);
		local2.setSearchCompleteCallback(function(results) {
			if (local.getStatus() == BMAP_STATUS_SUCCESS) {
				school = results.getNumPois();
			}
		});
	}

	local.setPageCapacity(100);
	//地址检索
	function doSearch() {
		var address = document.getElementById("address").value;
		local.search(address);
		//检索结束后的回调方法
		local.setSearchCompleteCallback(function(results) {
			// 判断状态是否正确
			if (local.getStatus() == BMAP_STATUS_SUCCESS) {
				var str = "<ul>";
				var subwayLine = [];
				for (var i = 0; i < results.getCurrentNumPois(); i++) {
					var poi = results.getPoi(i);
					//                    console.log(JSON.stringify(poi));
					var station = {};
					station.name = poi.title;
					station.lat = poi.point.lat;
					station.lng = poi.point.lng;
					subwayLine.push(station);
					str += '<li>';
					str += '<div class="res-data">';
					str += '<div class="left res-marker">';
					str += '<span>' + String.fromCharCode(65 + i) + '</span>';
					str += '</div>';
					str += '<div class="left res-address">';
					str += '<div class="title">' + poi.title + '</div>';
					str += '<div>地址：<span class="rout">' + poi.address
							+ '</span></div>';
					str += '<div>坐标：<span class="point">' + poi.point.lng + ","
							+ poi.point.lat + '</span></div>';
					str += '</div>';
					str += '<div class="clearfix"></div>';
					str += '</div>';
					str += '</li>';
				}
				str += '</ul>';
				$("#result").html(str);
				$("#s-city").text(results.province + results.city);
				$("#s-point").text(
						results.getPoi(0).point.lng + ","
								+ results.getPoi(0).point.lat);
				//subwayLine.serialize();
				//alert(JSON.stringify(subwayLine));
			}
		});
	}
</script>