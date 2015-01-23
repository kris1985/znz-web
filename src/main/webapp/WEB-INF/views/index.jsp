<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
	<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
    <link href="<c:url value="/resources/css/login.css" />" rel="stylesheet"  type="text/css" />
	<!--
		Used for including CSRF token in JSON requests
		Also see bottom of this file for adding CSRF token to JQuery AJAX requests
	-->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<script src="<c:url value="/resources/js/jquery-1.11.2.min.js" />"> </script>
    <script>
    $(function(){
        $(".form-signin").show(1000)
    })
    </script>
</head>
<body>
 <div class="container1">

      <form class="form-signin" role="form" action="<c:url value="/admin/user/login" />" style="display:none" method="post">
               <c:if test="${not empty error}">
					<div id="message" class="success">${error}</div>
		  		</c:if>
        <input type="input" class="form-control" placeholder="请输入用户名" required autofocus id="userName">
        <input type="password" class="form-control" placeholder="请输入密码" required id="pwd">
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