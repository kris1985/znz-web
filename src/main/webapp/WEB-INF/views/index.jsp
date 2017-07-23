<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <link rel="icon" href="../../favicon.ico">

    <title>指南针</title>
    <%@ include file="common/common.jsp"%>

      <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
    <link href="${basePath}/resources/css/login.css" rel="stylesheet"  type="text/css" />
      <style>
          body{ background: url("http://znz-resources.oss-cn-shenzhen.aliyuncs.com/indexBg.jpg") center 0px no-repeat; }
      </style>
	<!--
		Used for including CSRF token in JSON requests
		Also see bottom of this file for adding CSRF token to JQuery AJAX requests
	-->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<script src="${basePath}/resources/js/jquery-1.11.2.min.js"> </script>
    <script>
    $(function(){
        $("#loginBtn").click(function(){
            login();
        });
        if($("#message")){
            $(".form-signin").show();
        }else{
            $(".form-signin").show(1000)
        }
        if("9999"=="${param.error}"){
             alert("您的账号在其它地方登陆");
        }else  if("8888"=="${param.error}"){
            alert("会话超时请重新登陆");
        }
        $("#remember").click(function(){
            if($(this).get(0).checked){
                $(this).val("1");
            }else{
                $(this).val("0");
            }
        });


            });
    function refreshRandomCode(){
        $("#codeImg").attr("src", "/genCode?"+Math.random());
    }
        function login(){
            if($.trim($("#userName").val())==""){
                alert("请求输入用户名");
                $("#userName").focus();
                return;
            }
            if($.trim($("#pwd").val())==""){
                alert("请求输入密码");
                $("#pwd").focus();
                return;
            }
            if($.trim($("#randomCode").val())==""){
                alert("请求输入验证码");
                $("#randomCode").focus();
                return;
            }
            $("#loginForm").submit();
        }


    </script>

      <script>
          document.onkeydown=function(event){
              e = event ? event :(window.event ? window.event : null);
              if(e.keyCode==13){
                  login();
              }
          }
      </script>

  </head>
<body>
<%
    String name="";
    String passward="";
    Cookie[] cookies=request.getCookies();
    if(cookies!=null) {
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("znzauth")) {
                if(!"".equals(cookies[i].getValue())){
                    name = cookies[i].getValue().split("-")[0];
                    name = URLDecoder.decode(name, "UTF-8");
                    passward = cookies[i].getValue().split("-")[1];
                    pageContext.setAttribute("userName",name);
                    pageContext.setAttribute("pwd",passward);
                    break;
                 }
            }
        }
    }
%>

<c:if test="${not empty error}">
    <script>alert("${error}")</script>
</c:if>

 <div class="container1" >
     <div class="loginDivWrap">
     <div class="loginDiv" style="width: 1300px">
      <form  action="${basePath}/login"   method="post" id="loginForm">
       用户名： <input type="input" placeholder="请输入用户名" required autofocus id="userName" name="userName" value="${userName}" class="loginInput">
       密码：  <input type="password"  placeholder="请输入密码" required id="pwd" name="pwd" value="${pwd}" class="loginInput">
       验证码： <input type="input"  placeholder="验证码" required id="randomCode" name="randomCode"  class="loginInput" style="width: 60px; margin-right: 2px;">
          <img id="codeImg" src="${basePath}/genCode" width="60" height="22" border="0" align="absmiddle"/>
          <a href="javascript:refreshRandomCode();">看不清点我</a>

            <label class="rememberLab">
              <c:if test="${not empty userName}">
                 <input type="checkbox" value="1" id="remember" name="remember" checked="true">
              </c:if>
              <c:if test="${ empty userName}">
                  <input type="checkbox" value="0" id="remember" name="remember">
              </c:if>
              记住密码
              </label>

        <button class="btn btn-sm btn-primary" type="button" id="loginBtn">登陆</button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF%28%E8%8B%B9%E6%9E%9C%EF%BC%89.dmg">苹果电脑客户端下载</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF64%E4%BD%8D.exe">Windows64位客户端下载</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF32%E4%BD%8D%28XP%29.exe">Windows32客户端位下载</a>
      </form>
</div>
     </div>
    </div> <!-- /container -->

</body>
</html>