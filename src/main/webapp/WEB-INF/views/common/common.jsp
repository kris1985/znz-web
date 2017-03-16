
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
 pageContext.setAttribute("basePath", request.getContextPath());
 pageContext.setAttribute("ossPath", "http://znz.oss-cn-shenzhen.aliyuncs.com/");
%>
<link rel="shortcut icon" type="image/x-icon" href="${basePath}/resources/img/favicon.ico" media="screen" /> 
<script>
 var basePath = '<%= request.getContextPath()%>';

 function getContextPath(){
    return basePath;
 }

</script>