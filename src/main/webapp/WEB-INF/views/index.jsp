<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>中旅总社综合旅游中心</title>
    <%@ include file="common/common.jsp"%>
    <link href="${basePath}/resources/css/login.css" rel="stylesheet"  type="text/css" />
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

<form id="loginForm" action="${basePath}/login" method="post">
    <div class="Main">
        <ul>
            <li class="top"></li>
            <li class="top2"></li>
            <li class="topA"></li>
            <li class="topB"><span>
                <img src="${basePath}/resources/images/lock.png" style=" margin-left: 70px"/>

            </span></li>
            <li class="topC"></li>
            <li class="topD">
                <ul class="login">
                    <li><span class="left">用户名：</span> <span style="left">
                        <input  id="userName" name="userName" type="text" class="txt" value="${userName}" />

                    </span></li>
                    <li><span class="left">密 码：</span> <span style="left">
                       <input id="pwd" name="pwd" value="${pwd}"  type="text" class="txt" />
                    </span></li>
                    <li><span class="left">验证码：</span> <span style="left">
                    <input   id="randomCode" name="randomCode"  type="text" class="txtCode" />
                    </span>
                        <img id="codeImg" src="${basePath}/genCode" width="60" height="22" border="0" align="absmiddle"/>
                        <a href="javascript:refreshRandomCode();">看不清点我</a>


                    </li>

                    <li>
                        <span class="left">记住我：</span>

                        <c:if test="${not empty userName}">
                            <input type="checkbox" value="1" id="remember" name="remember" checked="true">
                        </c:if>
                        <c:if test="${ empty userName}">
                            <input type="checkbox" value="0" id="remember" name="remember">
                        </c:if>

                    </li>

                </ul>
            </li>
            <li class="topE"></li>
            <li class="middle_A"></li>
            <li class="middle_B"></li>
            <li class="middle_C">
            <span class="btn">
                <img alt="" src="${basePath}/resources/images/btnlogin.gif" id="loginBtn"/>
            </span>
            </li>
            <li class="middle_D"></li>
            <li class="bottom_A"></li>

        </ul>
    </div>
</form>
</body>
</html>
