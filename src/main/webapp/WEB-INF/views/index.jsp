<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%//@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <%@ include file="common/common.jsp"%>
	<link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
    <link href="${basePath}/resources/css/login.css" rel="stylesheet"  type="text/css" />
	<!--
		Used for including CSRF token in JSON requests
		Also see bottom of this file for adding CSRF token to JQuery AJAX requests
	-->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<script src="${basePath}/resources/js/jquery-1.11.2.min.js"> </script>
    <script>
    $(function(){
        if($("#message")){
            $(".form-signin").show();
        }else{
            $(".form-signin").show(1000)
        }
        if("9999"=="${param.error}"){
             alert("您的账号在其它地方登陆");
        }
    })
    </script>
</head>
<body>
 <div class="container1">

      <form class="form-signin" role="form" action="${basePath}/login"  style="display:none" method="post">
               <c:if test="${not empty error}">
					<div id="message" class="success" style="font-size:12px;margin-bottom:6px;">${error}</div>
		  		</c:if>
        <input type="input" class="form-control" placeholder="请输入用户名" required autofocus id="userName" name="userName">
        <input type="password" class="form-control" placeholder="请输入密码" required id="pwd" name="pwd">
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me" id="pwdCbox"> 记住密码
          </label>
        </div>
        <button class="btn btn-sm btn-primary btn-block" type="submit" id="loginBtn">登陆</button>
      </form>

    </div> <!-- /container -->

</body>
</html>