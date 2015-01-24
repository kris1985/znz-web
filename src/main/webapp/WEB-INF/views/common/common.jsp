
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
 pageContext.setAttribute("basePath", request.getServletContext().getContextPath());
%>
<script>
 var basePath = '<%= request.getServletContext().getContextPath()%>';

 function getContextPath(){
    return basePath;
 }
</script>