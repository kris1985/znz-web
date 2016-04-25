
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">


    <title>主题设置</title>
     <%@ include file="../common/common.jsp"%>
<!--
<link href="/znz-web/resources/css/swf.css" rel="stylesheet" type="text/css" />
<link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
<script type="text/javascript" src="${basePath}/resources/js/swfupload.js" /></script>
<script type="text/javascript" src="${basePath}/resources/js/swfupload.queue.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/fileprogress.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/handlers.js"></script>
-->
<link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
<link href="${basePath}/resources/css/docs-min.css" rel="stylesheet" type="text/css" />
<link href="${basePath}/resources/css/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery.uploadify.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery.uploadify.min.js"></script>
<script type="text/javascript">

	     $(function() {
			 <c:if test="${userSession.user.userType !=2 and userSession.user.userType !=3 }">
			 alert("无权限");
			 return;
			 </c:if>
			 setTimeout(function(){
         			$('#file_upload').uploadify({
         				'formData'     : {
         					'timestamp' : '123'

         				},
         			//	'auto':false,
         				'fileTypeExts':'*.jpg',
         				'fileObjName':'file',//重要
         				'buttonText' : '选择文件',
         				'swf'      : '${basePath}/resources/uploadify.swf',
         				'uploader' : '${basePath}/admin/file/uploadIndexBg',
         				'onUploadSuccess' : function(file, data, response) {
                            alert("上传成功");
         				              //  var htm = '文件 ' + file.name + ' 已经成功上传 ' + response + ':' + data
         				               // $("#result").show();
         				               // $("#result").append("<p>"+htm+'</p>');
         				               //  $("#result").hide(5000)
                                       // alert('The file ' + file.name + ' was successfully uploaded with a response of ' + response + ':' + data);
                                            },
         				'onFallback':function(){
                                                alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
                                            },
                                            onSelectError:function(file, errorCode, errorMsg){

                                                switch(errorCode) {

                                                    case -100:

                                                        alert("上传的文件数量已经超出系统限制的"+$('#uploadFile').uploadify('settings','queueSizeLimit')+"个文件！");

                                                        break;

                                                    case -110:

                                                        alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#uploadFile').uploadify('settings','fileSizeLimit')+"大小！");

                                                        break;

                                                    case -120:

                                                        alert("文件 ["+file.name+"] 大小异常！");

                                                        break;

                                                    case -130:

                                                        alert("文件 ["+file.name+"] 类型不正确！");

                                                        break;

                                                }
                                           }
         			});
					 },10);
         		});
	</script>
</head>
<body>


<div id="content">

<form>
		<div id="queue"></div>
<c:if test="${userSession.user.userType ==2 or userSession.user.userType ==3 }">
		<input id="file_upload" name="file_upload" type="file" multiple="false">
	</c:if>
<div id="result" >
</div>
<!--
		<button class="btn btn-primary" onclick="javascript:$('#uploadFile').uploadify('upload','*')" >开始上传

                </button>

<button class="btn btn-primary" onclick="javascript:$('#uploadFile').uploadify('cancel','*')" >取消上传

                </button>
-->
	</form>

	<!--
	<form id="form1"  method="post" enctype="multipart/form-data">

		<div class="fieldset flash" id="fsUploadProgress">
			<span class="legend">快速上传</span>
	  </div>
		<div id="divStatus">0 个文件已上传</div>
			<div>
				<span id="spanButtonPlaceHolder"></span>

				<input id="btnCancel" type="button" class="btn btn-primary" value="取消所有上传" onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;" />
			</div>

	</form>
	-->
</div>

</body>
</html>
