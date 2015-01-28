
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
	</script>
	</head>
<body>
<div id="container">
	<div class="pane ui-layout-center" id="file-content">

	</div>
	<div class="pane ui-layout-north" style="">

		<div style="width:100%;position:relative;height:40px">
			<div class="" style="width:105px;height;28px;position:absolute;left:1px;top:-4px">
							<img src="${basePath}/resources/img/logo2.JPG" height="45">
			</div>
			<div style="position:absolute;left:160px;top:0">
				<div class="nav_bar" style="float:left;width:80%" id="nav_bar">

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
				<div id="jstree"></div>

			</div>
	</div>
</div>
<script type="text/javascript">
function show(selectedId){
	  //alert(selectedId);
      var folderTemplate ="<div class=folder_wrap><div id={folderId}  class=\"folder_img\"><img  src=\"${basePath}/resources/img/folder.png\" width=\"256\" height=\"256\"></div><div class=\"folder_txt\">{folderName}</div></div>"
      var imgTemplate = "<div class='img_wrap'><img class='thumb' src='{imgSrc}' ><div class='img_txt'>{imgName}</div></div>"
      var navBarTemplate ="<span class='item' id={folderId}>{folderName}</span><span class='path_arrow'><img src='${basePath}/resources/img/path_arrow.png'></span>";
      var folderHtml = "";
      var imgHtml = "";
      var navBarHtml = "";
      $.get("${basePath}/admin/file/chidren/"+selectedId, function(result){
			$.each(result.parentNodes, function (n, value) {
				 tem =  navBarTemplate.replace("{folderName}",value.name);
				 tem = tem.replace("{folderId}",value.path);
				 navBarHtml += tem;
		   });
		   $("#nav_bar").html(navBarHtml);
		   $(".path_arrow:last").hide();
		   if(result.fileNodes== null){
		         $("#file-content").html("");
		    	return;
		   }
		   $.each(result.fileNodes, function (n, value) {
				 //alert(n + ' ' + value.directory);
				 if(value.directory == true){
					 tem =  folderTemplate.replace("{folderId}",value.path);
					 tem = tem.replace("{folderName}",value.name);
					 folderHtml += tem;
				 }else{
					 tem =  imgTemplate.replace("{imgSrc}",value.url);
					 tem = tem.replace("{imgName}",value.name);
					 imgHtml+= tem;
				 }
		    });
            $("#file-content").html(folderHtml+imgHtml);
        });
}






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

		$(document).delegate('.folder_wrap', 'hover', function() {
			$(this).css({border: "2px solid #b8d6fb",background:"#e1effc","font-weight":"bold"})
		}, function() {
			if(!$(this).hasClass("test")){
			$(this).css({border: "2px solid #fff",background: "#fff","font-weight":"normal"})}
		});

		 $(document).delegate('.folder_wrap', 'click', function() {
		    $(".folder_wrap").removeClass("folder_wrap_selected")
			$(this).addClass("folder_wrap_selected");
		});

       //初始化树
      	 $.get("${basePath}/admin/file/tree", function(result){
      		var bar = "<span class=\"item\" id="+result[0].id +">"+result[0].text+"</span>"
      		$("#nav_bar").html(bar);
      		$('#jstree').jstree({
      				'plugins': ["wholerow"],
      				 'core': { 'data':result}
      		});
      	  });

          // 文件管理器左部文件树点击事件
          $('#jstree').on("changed.jstree", function (e, data) {
            //console.log(data.selected);
      		show(data.selected[0].replace("_anchor",""));
           });
          //文件管理器上部导航点击
          $(document).delegate('.item', 'click', function() {
               show( $(this).attr("id") );
              // $('#jstree').jstree(false).select_node($(this).attr("id")+"_anchor");
             //  $('#jstree').jstree('select_node', $(this).attr("id")+"_anchor");
                $.jstree.reference('#jstree').select_node( $(this).attr("id")+"_anchor");
           });
          //文件管理器右部文件夹点击
          $(document).delegate('.folder_img', 'dblclick', function() {
             show( $(this).attr("id") );
             $('#jstree').jstree(true).select_node($(this).attr("id")+"_anchor");

          });
          /** 8 interact with the tree - either way is OK
          $('button').on('click', function () {
            $('#jstree').jstree(true).select_node('child_node_1');
            $('#jstree').jstree('select_node', 'child_node_1');
            $.jstree.reference('#jstree').select_node('child_node_1');
          });**/
      	var sclHeight = $(document).height()-150;

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

      	maxHeight = 180;
        maxWidth = 180;
$(document).delegate('.thumb', 'click', function() {
//$(".thumb").each(function(i){
        alert('ss');
        /**
             width = $(this).width();
             height = $(this).height();
             if(width>height){
                maxHeight = maxWidth*(height/width);
             }else{
                maxHeight = maxWidth*(width/height);
             }
             **/
             width = $(this).width();
             	height = $(this).height();
             	if(width>height){
             		maxWidth = maxHeight*(width/height);
             	}else{
             		maxWidth = maxHeight*(height/width);
             	}

             percent = width / height;

             if (width > maxWidth) {
             					// 图片按照最大宽度显示（缩放）
             					// 图片的高度在maxHeight以内
             					width = maxWidth;
             					height = width / percent;

             					// 按最大宽度缩放后，图片的高度仍超过maxHeight
             					// 这是要图片应该是一个纵向高度比较长的图片
             					if (height > maxHeight) {
             						// 这里要将之前的最大宽度再按图片的长宽比来缩小
             						// 这时的图片是高度等于maxHeight的
             						// 而宽度是小于maxWidth的缩略图
             						percent = maxHeight / height;
             						width = width * percent;
             						height = maxHeight;
             					}
             }else {
             					// 图片的原始宽度小于等于maxWidth
             					if (width <= maxWidth) {
             						// 高度超过maxHeight，再等比调整图片高度
             						if (height > maxHeight) {
             						    percent = maxHeight / height;
             							width = width * percent;
             							height = maxHeight;
             						}
             					}
             }


                $(this).width(width);
                $(this).height(height);
                $(this).css("margin-left",-(width / 2)+"px");
                $(this).css("margin-top",-(height / 2)+"px");

        })

});








	</script>

	</body>


<script>



<script>
</html>