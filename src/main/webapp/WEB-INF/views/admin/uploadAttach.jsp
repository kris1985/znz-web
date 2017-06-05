
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
    <title>指南针</title>
    <%@ include file="../common/common.jsp"%>

    <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
    <link href="${basePath}/resources/css/uploadify.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.uploadify.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.uploadify.min.js"></script>
    <script type="text/javascript">

        $(function() {

            setTimeout(function(){
                $('#file_upload').uploadify({
                    'formData'     : {
                        'timestamp' : new Date().getTime(),
                        'pictureId': ${param.id},
                        'secondCategory': ${param.secondCategory}
                    },
                    //	'auto':false,
                    'fileTypeExts':'*.jpg;*.jpge;*.gif;*.png;*.bmp;*.wbmp;',
                    'fileObjName':'file',
                    'buttonText' : '选择文件',
                    'swf'      : '${basePath}/resources/uploadify.swf',
                    'uploader' : '${basePath}/admin/file/uploadChild',
                    'onUploadSuccess' : function(file, data, response) {
                        var htm = '文件 ' + file.name + ' 已经成功上传 ' + response + ':' + data
                        $("#result").show();
                        // window.opener.show("${param.parentId}")
                        $("#result").append("<p>"+htm+'</p>');
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
        <input id="file_upload" name="file_upload" type="files" multiple="true">
        	<input type = "hidden" name="pictureId" id="pictureId" value="${param.id}">
        <div id="result" >
        </div>

    </form>

</div>

</body>
</html>
