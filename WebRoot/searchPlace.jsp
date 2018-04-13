<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>

<head>
<title>根据地址查询经纬度</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
</head>

<body style="background:#CBE1FF">
	<div style="width:1200px;margin:auto;">
		<form action="process.do" method="post">
			<input id="text_" type="text" style="width:100%;height:300px" value="${addresses}" /> 
			<input id="lng" name="lng" type="hidden" />
			<input id="lat" name="lat" type="hidden" /> 
			<input type="button" value="查询" onclick="searchByStationName();" /> 
			<input type="submit" value="提交"/>
		</form>
		<table id="result_"></table>
		<div id="container"></div>
	</div>
</body>
<script type="text/javascript">
	var map = new BMap.Map("container");
	var localSearch = new BMap.LocalSearch(map);
	var houses = "${houses}";
//
	function searchByStationName() {

		$("#result_").html('<tr><td>查找名称</td><td>经度</td><td>维度</td></tr>')

		var keyword = $("#text_").val();
		var list = keyword.split('#');

		for (var i = 0; i < list.length; i++) {
			map.clearOverlays(); //清空原来的标注
			localSearch.setSearchCompleteCallback(function(searchResult) {
				var poi = searchResult.getPoi(0);
				var html = '';
				var lat = '';
				var lng = '';
				if (poi) {
					html += '<tr><td>' + searchResult.keyword;
					html += '</td><td>' + poi.point.lng;
					html += '</td><td>' + poi.point.lat + '</td></tr>';
					lat = poi.point.lat + '#';
					lng = poi.point.lng + '#';
				} else {
					html += '<tr><td>' + searchResult.keyword;
					html += '</td><td>——';
					html += '</td><td>——</td></tr>';
					lat = '0#';
					lng = '0#';
				}
				$("#result_").append(html);
				document.getElementById("lng").value+=lng;
				document.getElementById("lat").value+=lat;
			});
			localSearch.search(list[i]);
		}
	}
</script>

</html>