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

  </head>
<body>

<c:if test="${not empty error}">
    <script>alert("${error}")</script>
</c:if>
 <div class="container1" >
     <div class="loginDivWrap" style="line-height: 25px;min-height: 32px">
     <div class="loginDiv" style="width: 1300px;font-weight: bold">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a style="color: white" href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF%28%E8%8B%B9%E6%9E%9C%EF%BC%89.dmg">苹果电脑客户端下载</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a style="color: white" href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF64%E4%BD%8D.exe">Windows64位客户端下载</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a style="color: white" href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF32%E4%BD%8D%28XP%29.exe">Windows32客户端位下载</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <a style="color: white" href="http://znz-resources.oss-cn-shenzhen.aliyuncs.com/%E6%8C%87%E5%8D%97%E9%92%88%E9%9E%8B%E8%AE%AF.apk">Android APP下载</a>

</div>
     </div>


    </div> <!-- /container -->

    <div id="footer" style=" height: 30px;
    position: absolute;
    bottom: 0px;
    background: black;
    opacity: 0.5;
    color: white;
    width: 100%;
    line-height: 30px;">
        <div class="inner" style="text-align: center">广州铁玉兰贸易有限公司版权所有 <a href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备17037183号</a></div>
    </div>
</body>
</html>