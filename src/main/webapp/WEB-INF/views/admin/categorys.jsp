
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
    <title>类型列表</title>
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
  <div id="categorys"></div>
<script type="text/javascript">
            $.ajax({
                    url:'${basePath}/admin/category/list',
                    dataType: 'json',
                    cache:false,
                    success: function(json) {
                    var data = JSON.stringify(json);
                    var myobj=eval(data);
                        users = $('#categorys').columns({
                            data:myobj,
							templateFile: '${basePath}/resources/templates/category.mst',
                             schema: [
                                                {"header":"类型名称", "key":"name" },
                                                {"header":"排序", "key":"sort_id"},
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
