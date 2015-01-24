
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
</head>
<body>
<div class="container">
    <div class="row">
	<div class="col-sm-3"></div>
	<div class="col-sm-6">
     <form class="form-horizontal" role="form" method="post" action="${basePath}/admin/user/add">
	 <div style="border:1px solid #ddd;border-radius: 4px 4px 0 0;padding:20px;margin-top:20px">
	<div class="step1">
	<div style="color:red">${error}</div>
	 <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">公司名称</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="company" name="company" placeholder="公司名称">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 col-md-2 control-label">用户名</label>
      <div class="col-sm-10 col-md-10">
         <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入用户名">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 col-md-2 control-label">密码</label>
      <div class="col-sm-10 col-md-10">
         <input type="password" class="form-control" id="pwd" name="pwd" placeholder="请输入密码">
      </div>
   </div>

    <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">IP策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="limitIpFlag" value="1"> 限制IP  <input type="text" class="form-control" id="limitIps" name="limitIps"
            placeholder="请输入IP">
      </div>
   </div>

   <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">访问策略</label>
      <div class="col-sm-10 col-md-10">
        <input type="checkbox" name="accessFlag" value="1"> 单台电脑访问
      </div>
   </div>

   <div class="form-group">
      <label for="lastname" class="col-sm-2 col-md-2 control-label">每天下载次数</label>
      <div class="col-sm-10 col-md-10">
        <input type="text" class="form-control" name="maxDownloadTimes">
      </div>
   </div>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10 col-md-10">
         <button type="button" class="btn btn-primary" id="next-setp-btn" >下一步</button>
      </div>
   </div>
  </div>
  <div class="step2 " style="display:none">
   <div class="form-group">

   <div class="input-group">
						  <input type="text" class="form-control" placeholder="搜索 权限">

						  <span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
						</div>

      <div class="col-sm-12 col-md-12" style="margin-top:20px">

         <table class="table">


      <tbody>
        <tr>
          <td><input type="checkbox" name="auths" value="znz"></td>
          <td>国内橱窗</td>

        </tr>
        <tr>
          <td><input type="checkbox" name="auths" value="znz1"></td>
          <td>国外女鞋VIP</td>
        </tr>
		 <tr>
          <td><input type="checkbox" name="auths" value="znz2"></td>
          <td>国内女鞋VIP</td>
        </tr>

      </tbody>
    </table>

   </div>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10 col-md-10">
		 <button type="button" class="btn btn-default " id="pre-setp-btn" >上一步</button>
		 <button type="submit" class="btn btn-primary " id="submit-btn" >提交</button>

      </div>
   </div>
  </div>

   </div>
</form>

 </div>
 <div class="col-sm-3"></div>
  </div>
    </div> <!-- /container -->


<script type="text/javascript">
$(function(){
		$("#next-setp-btn").click(function(){
			$(".step1").hide();
			$(".step2 ").show();
		});
		$("#pre-setp-btn").click(function(){
			$(".step2").hide();
			$(".step1").show();
		});


	})
</script>


</body>
</html>
