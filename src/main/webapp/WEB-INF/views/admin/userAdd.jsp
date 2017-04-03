
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
    <title>新增用户</title>
     <%@ include file="../common/common.jsp"%>
     <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
      <link href="${basePath}/resources/css/clean.css" rel="stylesheet" type="text/css" />
      <link href="${basePath}/resources/css/uploadify.css" rel="stylesheet" type="text/css" />
      <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/jquery.columns.min.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/gotopage.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/ajaxpaging.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.uploadify.min.js"></script>
      <style>
      #addUserBtn{display:none}
      </style>
</head>
<body>
<div class="container1" style="width:600px;margin:0 auto">

     <form class="form-horizontal" role="form" method="post" action="${basePath}/admin/user/add" id="userForm">
	 <div style="margin-top:20px">
	<div class="step1">
	<div style="color:red">${error}</div>
	 <div class="form-group">
      <label for="company" class="col-sm-2 col-md-2 control-label">公司名称</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="company" name="company" placeholder="公司名称" maxlength="20">
      </div>
   </div>
   <div class="form-group">
      <label for="userName" class="col-sm-2 col-md-2 control-label">用户名</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入用户名" maxlength="20">
      </div>
   </div>
   <div class="form-group">
      <label for="pwd" class="col-sm-2 col-md-2 control-label">密码</label>
      <div class="col-sm-10 col-md-10">
         <input type="password" class="form-control" id="pwd" name="pwd" placeholder="请输入密码" maxlength="16">
      </div>
   </div>

    <div class="form-group">
      <label for="limitIps" class="col-sm-2 col-md-2 control-label">IP策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="limitIpFlag" value="1"> 限制IP  <input type="text" class="form-control" id="limitIps" name="limitIps"
            placeholder="请输入IP" maxlength="60">
      </div>
   </div>

   <div class="form-group">
      <label for="accessFlag" class="col-sm-2 col-md-2 control-label">访问策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="accessFlag" value="1"> 单台电脑访问
      </div>
   </div>

   <div class="form-group">
      <label for="maxDownloadTimes" class="col-sm-2 col-md-2 control-label">每天限制下载次数</label>
      <div class="col-sm-10 col-md-10">
        <input type="text" class="form-control" name="maxDownloadTimes" id="maxDownloadTimes" value="999999" maxlength="6"/>
      </div>
   </div>

    <div class="form-group">
        <label for="device" class="col-sm-2 col-md-2 control-label">使用设备</label>
        <div class="col-sm-10 col-md-10">
            <input type="radio"  name="device" id="device1" value="web" checked/> WEB
            <input type="radio"  name="device" id="device2" value="app"/> APP
        </div>
    </div>

    <div class="form-group">
        <label for="recommendFlag" class="col-sm-2 col-md-2 control-label">推荐权限</label>
        <div class="col-sm-10 col-md-10">
            <input type="radio"  name="recommendFlag" id="recommendFlag1" value="0" checked /> 不推荐
            <input type="radio"  name="recommendFlag" id="recommendFlag2" value="1" /> 推荐
        </div>
    </div>

        <div class="form-group">
            <label for="file_upload" class="col-sm-2 col-md-2 control-label">水印图片</label>
            <div class="col-sm-10 col-md-10">
                <input id="file_upload" name="file_upload" type="files" multiple="false">
            </div>
        </div>

        <div id="watermark" style="display: none;margin-left: 50px"></div>

    <div class="form-group">
        <label for="watermarkVO.g" class="col-sm-2 col-md-2 control-label">水印位置</label>
        <div class="col-sm-10 col-md-10">
            <select id="watermarkVO.g" name="watermarkVO.g">
                <option value="nw">左上</option>
                <option value="north">中上</option>
                <option value="ne">右上</option>
                <option value="west">左中</option>
                <option value="center">正中</option>
                <option value="east">右中</option>
                <option value="sw">左下</option>
                <option value="ne">中下</option>
                <option value="south" selected>右下</option>
            </select>
        </div>
    </div>

        <div class="form-group">
            <label for="watermarkVO.t" class="col-sm-2 col-md-2 control-label">透明度</label>
            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" name="watermarkVO.t" id="watermarkVO.t" value="100" maxlength="6"/>100表示（不透明） 取值范围: [0-100]
            </div>
        </div>

        <div class="form-group">
            <label for="watermarkVO.p" class="col-sm-2 col-md-2 control-label">缩放倍数</label>
            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" name="watermarkVO.p" id="watermarkVO.p" value="10" maxlength="6"/>10, 当主图是100x100, 水印图片大小就为10x10, 当主图变成了200x200，水印图片大小就为20x20,MG会根据主图的大小来动态调整水印图片的大小
            </div>
        </div>

    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10 col-md-10">
         <button type="button" class="btn btn-default "  onclick="window.history.back()">返回</button>
         <button type="button" class="btn btn-primary" id="next-setp-btn" >下一步</button>
      </div>
   </div>
  </div>
  <div class="step2 " style="display:none">

   <div id="auths"></div>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10 col-md-10">
		 <button type="button" class="btn btn-default " id="pre-setp-btn" >上一步</button>
		 <button type="button" class="btn btn-primary " id="submit-btn" >提交</button>
      </div>
   </div>
  </div>

   </div>
         <input type="hidden" name="watermarkVO.image" id="watermarkImg">
</form>

 </div>

    </div> <!-- /container -->


<script type="text/javascript">
$(function(){
		$("#next-setp-btn").click(function(){
            if($.trim($("#company").val())==""){
                alert("请输入公司名称");
                $("#company").focus();
                return;
            }
            if($.trim($("#userName").val())==""){
                alert("请输入用户名");
                $("#userName").focus();
                return;
            }
            if($.trim($("#pwd").val())==""){
                alert("请输入密码");
                $("#pwd").focus();
                return;
             }
             if($.trim($("#maxDownloadTimes").val())==""){
                 alert("请输入每天限制下载次数");
                 $("#maxDownloadTimes").focus();
                 return;
             }
             var reg = /^[0-9]*$/;
             if(!reg.test($.trim($("#maxDownloadTimes").val()) ) ){
                  alert("每天限制下载次数只能为数字");
                  $("#maxDownloadTimes").focus();
                  return;
              }
			$(".step1").hide();
			$(".step2 ").show();
		});

		$("#submit-btn").click(function(){
          $("#userForm").submit();
		});
		$("#pre-setp-btn").click(function(){
			$(".step2").hide();
			$(".step1").show();
		});

        setTimeout(function(){
            $('#file_upload').uploadify({
                'formData'     : {
                    'timestamp' : new Date().getTime()
                },
                //	'auto':false,
                'fileTypeExts':'*.jpg;*.jpge;*.gif;*.png;*.bmp;*.wbmp;',
                'fileObjName':'file',
                'buttonText' : '选择文件',
                'swf'      : '${basePath}/resources/uploadify.swf',
                'uploader' : '${basePath}/admin/file/uploadWatermark',
                'formData'      : {'category' : "${category}" },
                'onUploadSuccess' : function(file, data, response) {
                   // var htm = '文件 ' + file.name + ' 已经成功上传 ' + response + ':' + data;
                    var src = "watermark_"+file.name;
                    $("#watermark").show();
                    $("#watermark").html("<img src='${ossPath}/"+src + "' />");
                    $("#watermarkImg").val(src);

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
	}



	)
	  $.ajax({
                        url:'${basePath}/admin/user/auths',
                        dataType: 'json',
                        success: function(json) {
                        var data = JSON.stringify(json);
                        var myobj=eval(data);
                            auths = $('#auths').columns({
                                data:myobj,
    							templateFile: '${basePath}/resources/templates/default.mst',
                                 schema: [
                                     {"header":"ID", "key":"authId" },
                                                    {"header":"选择权限", "key":"authName",
                                                     "template":'<input type="checkbox" name="auths" value={{authId}}> {{authName}}'
                                                     }
                                         ]
                            });
                        }
                    });
</script>


</body>
</html>
