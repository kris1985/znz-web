
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title>磁盘空间</title>
     <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
    <link href="/znz-web/resources/css/clean.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/Chart.min.js"></script>

    <script >
    	var pieData = [
    				{
    					value: ${usedSpace},
    					color:"#F7464A",
    					highlight: "#FF5A5E",
    					label: "已使用"
    				},

    				{
    					value: ${freeSpace},
    					color: "green",
    					highlight: "#616774",
    					label: "可用"
    				}
    			];
    	$(function(){
    		var ctx = document.getElementById("chart-area").getContext("2d");
    		window.myPie = new Chart(ctx).Pie(pieData);
    	})
    	</script>

</head>
<body>
<div class="container1">
		<div id="canvas-holder" style="width:300px;margin:0 auto">
			<canvas id="chart-area" width="300" height="300"/>
		</div>
		<div class="descriptions" style="width:300px;margin:0 auto">
			<table width="60%" border="0" align="center">
				<tr>
					<td height="30">已使用</td>
					<td align="center" ><div style="width:16px;height:16px;background:red;text-indent:-9999"></div></td>
					<td>&nbsp;&nbsp;${usedSpace}GB</td>
				</tr>
				<tr>
					<td height="30">可用</td>
					<td align="center" ><div style="width:16px;height:16px;background:green;text-indent:-9999"></div></td>
					<td>&nbsp;&nbsp;${freeSpace}GB</td>
				</tr>
			</table>
			</div>

    </div> <!-- /container -->v>



</body>
</html>
