
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
 pageContext.setAttribute("basePath", request.getContextPath());
%>
<script>
 var basePath = '<%= request.getContextPath()%>';

 function getContextPath(){
    return basePath;
 }

</script>