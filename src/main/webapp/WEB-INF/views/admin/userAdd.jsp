
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
      <link href="/znz-web/resources/css/clean.css" rel="stylesheet" type="text/css" />
      <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/jquery.columns.min.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/gotopage.js"></script>
      <script type="text/javascript" src="${basePath}/resources/js/ajaxpaging.js"></script>
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
      <label for="lastname" class="col-sm-2 col-md-2 control-label">公司名称</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="company" name="company" placeholder="公司名称" maxlength="20">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 col-md-2 control-label">用户名</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入用户名" maxlength="20">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 col-md-2 control-label">密码</label>
      <div class="col-sm-10 col-md-10">
         <input type="password" class="form-control" id="pwd" name="pwd" placeholder="请输入密码" maxlength="16">
      </div>
   </div>

    <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">IP策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="limitIpFlag" value="1"> 限制IP  <input type="text" class="form-control" id="limitIps" name="limitIps"
            placeholder="请输入IP" maxlength="60">
      </div>
   </div>

   <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">访问策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="accessFlag" value="1"> 单台电脑访问
      </div>
   </div>

   <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">每天限制下载次数</label>
      <div class="col-sm-10 col-md-10">
        <input type="text" class="form-control" name="maxDownloadTimes" id="maxDownloadTimes" value="999999" maxlength="6"/>
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
	})
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
                                                    {"header":"选择权限", "key":"authName",
                                                     "template":'<input type="checkbox" name="auths" value={{authName}}> {{authName}}'
                                                     }
                                         ]
                            });
                        }
                    });
</script>


</body>
</html>
