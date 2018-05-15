<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
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



<title>热力图功能示例</title>
<style type="text/css">
</style>
</head>
<body>
	<div class="main-container">
		<div id="container" class="container"></div>
		<div class="container">
			<div class="page-content">
				<div class="row">
					<div class="col-sm-6">
						<p></p>
						<label class="pull-left inline"> <small class="muted">热力图:</small>
							<input id="id-pills-stacked" type="checkbox" hidden="hidden"
							onchange="changeHeatmap()" class="ace ace-switch ace-switch-5" />
							<span class="lbl middle"></span>
						</label>
						<p></p>
						<label class="pull-left inline"> <small class="muted">地铁:</small>
							<input id="subwayAvailable" type="checkbox" hidden="hidden"
							onchange="changeSubway()" class="ace ace-switch ace-switch-5" />
							<span class="lbl middle"></span>
						</label>

					</div>
					<div class="col-sm-6">
						<label for="amount">价格区间:</label> <input type="text" id="amount"
							readonly
							style="border:0; color:#f6931f; font-weight:bold;width:inherit; ">
						<div id="slider-range"></div>
					</div>
				</div>
				<p class="page-header"></p>



			</div>
		</div>
	</div>
	<script type="text/javascript">
		var map = new BMap.Map("container"); // 创建地图实例  

		var point = new BMap.Point(${avg_lng}, ${avg_lat});
		map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别  
		map.enableScrollWheelZoom(); // 允许滚轮缩放  
		map.enableDragging();
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
		/* map.addEventListener("mousemove",function(e){
			document.getElementById("_position").value=e.point.lng+","+e.point.lat;
		}); */

		heatmapOverlay.setDataSet({
			data : points,
			max : ${maxCount}

		});

		closeHeatmap();

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

		function openHeatmap() {
			heatmapOverlay.show();
		}

		function closeHeatmap() {
			heatmapOverlay.hide();
		}

		function changeHeatmap() {
			if ($("#id-pills-stacked").get(0).checked)
				openHeatmap();
			else
				closeHeatmap();

		}
		function changeCenter(lng, lat) {
			//	alert(lng);
			var center = new BMap.Point(lng, lat);
			map.centerAndZoom(center, 15);
		}

		function openMarker() {

			var p;
			for (var i = 0; i < 10; i++) {
				var hotPoint = new BMap.Point(points[i].lng, points[i].lat);
				var marker = new BMap.Marker(hotPoint); // 创建标注
				var _html = "<div>经纬度：";
				p = marker.getPosition();
				_html += p.lng + "," + p.lat;
				_html += "<img src=\"pic/东方君悦.jpg\" style=\"height:200px; \"></img>";
				_html += "</div>";
				map.addOverlay(marker); // 将标注添加到地图中    
				marker.addEventListener("click", function(e) {
					this.openInfoWindow(new BMap.InfoWindow(_html));
				});
			}
		}
		function changeSubway() {
			$.ajax({
				async : false,
				type : "POST",
				url : "selectBySubway.do",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(points),
				/* data : {
					"type" : "ys"
				}, */
				dataType : "json",
				success : function(data) {
					heatmapOverlay.setDataSet({
						data : data,
						max : ${maxCount}

					});
					map.addOverlay(heatmapOverlay);
					heatmapOverlay.show();

					if (!$("#id-pills-stacked").get(0).checked)
						$("#id-pills-stacked").get(0).checked = true;
				},
				error : function(data) {
					alert("failed");
				},
			});
		}
		//单击获取点击的经纬度
		map
				.addEventListener(
						"click",
						function(e) {
							$("#s-point").text(e.point.lng + "," + e.point.lat);

							//	var clickPoint = e.point;
							for (var i = 0; i < points.length; i++) {
								if (Math
										.abs(e.point.lat - points[i].lat < 0.001)
										&& Math
												.abs(e.point.lng
														- points[i].lng) < 0.001) {
									//alert(points[i].lng);
									$
											.ajax({
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
													var msg = JSON
															.stringify(data);
													var hotPoint = new BMap.Point(
															points[i].lng,
															points[i].lat);
													var marker = new BMap.Marker(
															hotPoint);

													map.addOverlay(marker); // 将标注添加到地图中   
													var dataObj = eval("("
															+ msg + ")");
													// alert(dataObj.name);
													var _html = "<div>"
															+ dataObj.name
															+ "</div>"
															+ "<div>房价：";
													_html += dataObj.price
															+ "</div>";

													marker
															.addEventListener(
																	"click",
																	function(e) {
																		this
																				.openInfoWindow(new BMap.InfoWindow(
																						_html));
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
		$(function() {
			$("#slider-range")
					.slider(
							{
								range : true,
								min : 0,
								max : ${maxCount},
								values : section,
								slide : function(event, ui) {
									if (!$("#id-pills-stacked").get(0).checked)
										$("#id-pills-stacked").get(0).checked = true;
									$("#amount").val(
											"$" + ui.values[0] + " - $"
													+ ui.values[1]);
									var minPrice = ui.values[0];
									var maxPrice = ui.values[1];
									$.ajax({
										type : "GET",
										url : "dynamic.do",
										data : {
											minPrice : minPrice,
											maxPrice : maxPrice
										},
										dataType : "json",
										success : function(data) {
											points = data;
											heatmapOverlay.setDataSet({
												data : data,
												max : ${maxCount}

											});
											map.addOverlay(heatmapOverlay);
											heatmapOverlay.show();

											/*  $('#resText').empty();   //清空resText里面的所有内容
											 var html = ''; 
											 $.each(data, function(commentIndex, comment){
											       html += '<div class="comment"><h6>' + comment['username']
											                 + ':</h6><p class="para"' + comment['content']
											                 + '</p></div>';
											 });
											 $('#resText').html(html); */

										}
									});
									$("#subwayAvailable").get(0).checked = false;

								}
							});
			$("#amount").val(
					$("#slider-range").slider("values", 0) + " - "
							+ $("#slider-range").slider("values", 1));
		});
	</script>
	<script>
		
	</script>
</body>
</html>
