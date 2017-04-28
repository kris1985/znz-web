
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
 pageContext.setAttribute("basePath", request.getContextPath());
 pageContext.setAttribute("ossPath", "http://znz.oss-cn-shenzhen.aliyuncs.com/");
%>
<link rel="shortcut icon" type="image/x-icon" href="${basePath}/resources/img/favicon.ico" media="screen" /> 
<script>
 var basePath = '<%= request.getContextPath()%>';
<%request.setAttribute("vEnter", "\n"); %>
 function getContextPath(){
    return basePath;
 }

 function getOssPath() {
   return "${ossPath}"
 }
 function getWatermark(){
     <c:choose>
        <c:when test="${ not empty watermarkParam  }">
           return "?x-oss-process=image${fn:replace(watermarkParam,vEnter,"")}";
        </c:when>
         <c:otherwise>
             return "";
        </c:otherwise>
     </c:choose>
 }

 function getUserType(){
     return "${userSession.user.userType}"
 }

</script>