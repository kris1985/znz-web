
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
    <title>用户列表</title>
     <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
    <link href="${basePath}/resources/css/clean.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.columns.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/gotopage.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/ajaxpaging.js"></script>


</head>
<body>
<div class="container">
  <div id="users"></div>
<script type="text/javascript">
            $.ajax({
                    url:'${basePath}/admin/user/list',
                    dataType: 'json',
                    success: function(json) {
                    var data = JSON.stringify(json);
                    var myobj=eval(data);
                        users = $('#users').columns({
                            data:myobj,
							templateFile: '${basePath}/resources/templates/default.mst',
                             schema: [
                                                {"header":"公司名称", "key":"company", },
                                                {"header":"用戶名", "key":"userName"},
                                                {"header":"当日下载", "key":"downloadPerDay"},
                                                {"header":"总下载", "key":"downloadTotal"},
                                                {"header":"操作",
                                                 "key":"userId",
                                                  "template":'<a href="${basePath}/admin/user/delete/{{userId}}" onclick="return confirm(\'删除后无法恢复,确定要删除吗\')" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除</a>&nbsp;&nbsp;<a href="${basePath}/admin/user/update/{{userId}}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;修改</a>'}
                                            ]
                        });
                    }
                });
	</script>
</div>



</body>
</html>
