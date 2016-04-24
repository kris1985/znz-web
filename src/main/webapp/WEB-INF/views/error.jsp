<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/4/22
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
  if("9999"=="${param.errorCode}"){
    alert("您的账号在其它地方登陆");
    window.open('<%=request.getContextPath()%>/','_top')
  }else  if("8888"=="${param.errorCode}"){
    alert("会话超时请重新登陆");
    window.open('<%=request.getContextPath()%>/','_top')
  }
</script>