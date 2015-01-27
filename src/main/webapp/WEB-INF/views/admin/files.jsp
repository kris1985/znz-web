
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
     <%@ include file="../common/common.jsp"%>
	<link rel="stylesheet" href="${basePath}/resources/css/bootstrap.min.css" >
	<link rel="stylesheet" href="${basePath}/resources/css/layout-default-latest.css" />
    <link rel="stylesheet" href="${basePath}/resources/js/themes/default/style.min.css" />
    <link rel="stylesheet" href="${basePath}/resources/css/file.css" rel="stylesheet">
	<link rel="stylesheet" href="${basePath}/resources/css/jquery.mCustomScrollbar.css">
	<link rel="stylesheet" href="${basePath}/resources/css/smartMenu.css">

	<script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/jquery-ui-latest.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/jstree.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/js/jquery-smartMenu-min.js"></script>

	<script type="text/javascript">
var imageMenuData = [
    [{
        text: "图片描边",
        data: [[{
            text: "5像素深蓝",
            func: function() {
                $(this).css("border", "5px solid #34538b");
            }
        }, {
            text: "5像素浅蓝",
            func: function() {
                $(this).css("border", "5px solid #a0b3d6");
            }
        }, {
            text: "5像素淡蓝",
            func: function() {
                $(this).css("border", "5px solid #cad5eb");
            }
        }]]
    }, {
        text: "图片内间距",
        func: function() {
            $(this).css("padding", "10px");
        }
    }, {
        text: "图片背景色",
        func: function() {
            $(this).css("background-color", "#beceeb");
        }
    }],
    [{
        text: "查看原图",
        func: function() {
            var src = $(this).attr("src");
            window.open(src.replace("/s512", ""));
        }
    }]
];

var bodyMenuData = [
    [{
        text: "上传",
        data: [[{
            text: "上传-覆盖",
            func: function() {
                $(this).css("border", "5px solid #34538b");
            }
        }, {
            text: "上传-忽略",
            func: function() {
               // $(this).css("border", "5px solid #a0b3d6");
            }
        }]]
    }, {
        text: "新建文件夹",
        func: function() {
            //$(this).css("padding", "10px");
        }
    }]
];


$(function(){


 $.get("${basePath}/admin/file/tree", function(result){
    alert(result):
  });

		$('#jstree').jstree({
        'plugins': ["wholerow"]

    });
    // 7 bind to events triggered on the tree
    $('#jstree').on("changed.jstree", function (e, data) {
      console.log(data.selected);
    });
    /** 8 interact with the tree - either way is OK
    $('button').on('click', function () {
      $('#jstree').jstree(true).select_node('child_node_1');
      $('#jstree').jstree('select_node', 'child_node_1');
      $.jstree.reference('#jstree').select_node('child_node_1');
    });**/
	var sclHeight = 460;

	//左边滚动条
	$("#left_container").mCustomScrollbar({
					setHeight:sclHeight,
					theme:"inset-2-dark"
	});

	$(".folder_wrap").smartMenu(imageMenuData, {
		name: "image"
	});
	/**$("#file-content").smartMenu(bodyMenuData, {
		name: "image1"
	});
	$("#child_node_1_2").smartMenu(imageMenuData, {
		name: "image1"
	});**/
});

	</script>
	</head>
<body>
	<style>
	html, body {
		background:	#fff;
		width:		100%;
		height:		100%;

		overflow:	auto; /* when page gets too small */
	}
	#container {
		background:url('img/shadow_category.png');
		height:		100%;
		margin:		0 auto;
		width:		100%;

		min-width:	700px;
		_width:		700px; /* min-width for IE6 */
	}
	.pane {
		display:	none; /* will appear when layout inits */
	}
	.jstree-themeicon{width:0px}
	</style>

<div id="container">
	<div class="pane ui-layout-center" id="file-content">
		<div class="folder_wrap">
				<div class="folder_img"><img src="img/folder.png" width="256" height="256"></div>
				<div class="folder_txt">女凉 14090820VIP 休闲-成行底1111111111111111</div>
			</div>
			<div class="folder_wrap">
				<div class="folder_img"><img src="img/folder.png" width="256" height="256"> </div>
				<div class="folder_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="folder_wrap">
				<div class="folder_img"><img src="img/folder.png" width="256" height="256"> </div>
				<div class="folder_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="folder_wrap">
				<div class="folder_img"><img src="img/folder.png" width="256" height="256"> </div>
				<div class="folder_txt">女凉 14090820VIP 休闲-成行底1</div>
			</div>
			<div class="folder_wrap">
				<div class="folder_img"><img src="img/folder.png" width="256" height="256"> </div>
				<div class="folder_txt">女凉 14090820VIP 休闲-成行底2</div>
			</div>

			<div class="img_wrap">
				<div class="pic_img"><a href="album.html" target="_blank"><img src="img/demo/高靴140324200nVIP时装_017.jpg" width="230" height="220"></a> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_018.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_019.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_020.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_021.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_022.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_023.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
			<div class="img_wrap">
				<div class="pic_img"><img src="img/demo/高靴140324200nVIP时装_024.jpg" width="230" height="220"> </div>
				<div class="img_txt">女凉 14090820VIP 休闲-成行底</div>
			</div>
	</div>
	<div class="pane ui-layout-north" style="">

	<!--
	<div class="input-append input-prepend" style="float:left;">
						<div class="btn-group">
						  <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span></button>
						  <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span></button>
						  <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-right"></span></button>
						</div>
		</div>
		-->





		<div style="width:100%;position:relative;height:40px">
			<div class="" style="width:105px;height;28px;position:absolute;left:1px;top:-4px">
							<img src="img/logo2.jpg" height="45">
			</div>
			<div style="position:absolute;left:160px;top:0">
				<div class="nav_bar" style="float:left;width:80%" >
					<span class="item">指南针鞋讯女鞋</span>
					<span class="path_arrow"><img src="img/path_arrow.png"></span>
					<span class="item">国内女鞋vip</span>
					<span class="path_arrow"><img src="img/path_arrow.png"></span><span class="item">女凉 女拖</span>
				 </div>
				 <div class="" style="float:right;width:19%;">
					<div class="input-group">
					  <input type="text" class="form-control" placeholder="搜索" style="height:32px">

					  <span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
					</div>
				 </div>

		   </div>

		</div>


	</div>
	<div class="pane ui-layout-south"><p style="text-align:center">指南针鞋讯版权所有</p></div>

	<div class="pane ui-layout-west">
	<div id="left_container" style="border:0px solid #ccc">
				<div id="jstree">

					<ul>
					  <li>指南针鞋讯女鞋
						<ul>
						  <li id="child_node_1">国内女鞋vip
							  <ul>
							  <li id="child_node_1_1">女凉 女拖</li>
							  <li id="child_node_1_2">女凉 女拖</li>
							</ul>
						  </li>
						  <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
							  <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
							  <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
							  <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
						   <li >女凉</li>
						    <li >女凉</li>
							 <li >女凉</li>
							 <li >女凉</li>
							  <li >女凉</li>


						</ul>
					  </li>

					</ul>
				</div>

			</div>
	</div>
</div>
<script type="text/javascript">

	$(document).ready(function () {
		$('#container').layout({

			north__slidable:			false	// OVERRIDE the pane-default of 'slidable=true'
		,	north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
		,	north__spacing_open:		0		// big resizer-bar when open (zero height)
		,	north__resizable:			false
		,	north__closable:			false

		,	west__resizable:			true
		,	west__closable:			false

		,	south__resizable:			false	// OVERRIDE the pane-default of 'resizable=true'
		,	south__spacing_open:		0		// no resizer-bar when open (zero height)
		,	south__spacing_closed:		20
		});





		$(".folder_wrap").hover(function() {
			$(this).css({border: "2px solid #b8d6fb",background:"#e1effc","font-weight":"bold"})
		}, function() {

			if(!$(this).hasClass("test")){
			$(this).css({border: "2px solid #fff",background: "#fff","font-weight":"normal"})}
		});
		$(".folder_wrap").click(function(){
		    $(".folder_wrap").css({border: "2px solid #fff",background: "#fff"})
			$(this).css({border: "2px solid #0186e5",background: "#e1effc","font-size":"13px"})
			//$(this).attr("")
			if(!$(this).hasClass("test")){
				$(this).addClass("test");
			}
		});



});

	</script>

	</body>
</html>