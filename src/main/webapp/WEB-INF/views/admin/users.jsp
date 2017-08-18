
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

    <script>
        function  modify(userId) {
            var rangeEnd = parseInt($("#rangeEnd").text());
            var pageSize = parseInt($(".ui-table-size select").val());
            var totalNum = parseInt($("#totalNum").text());
            var pages = totalNum% pageSize == 0 ? (totalNum/pageSize) : ((totalNum/pageSize)+1)
            alert(rangeEnd+"---"+pageSize+"pages");
            var currentPage = rangeEnd % pageSize == 0 ? (rangeEnd/pageSize) : ((rangeEnd/pageSize)+1);
            var url = "${basePath}/admin/user/update/"+userId+"/"+currentPage;
            window.location.href = url;
        }

    </script>

</head>
<body>
<div class="container">
  <div id="users"></div>
<script type="text/javascript">
    var page = "${param.page}";
    if(page==""){
        page = 1;
    }
            $.ajax({
                    url:'${basePath}/admin/user/list',
                    dataType: 'json',
                    cache:false,
                    success: function(json) {
                    var data = JSON.stringify(json);
                    var myobj=eval(data);
                        users = $('#users').columns({
                            page:page,
                            data:myobj,
							templateFile: '${basePath}/resources/templates/user.mst',
                             schema: [
                                                {"header":"公司名称", "key":"company" },
                                                {"header":"用戶名", "key":"userName"},
                                                {"header":"当日下载", "key":"downloadPerDay"},
                                                {"header":"总下载", "key":"downloadTotal"},
                                                {"header":"设备", "key":"device"},
                                                {"header":"IP", "key":"limitIps"},
                                                {"header":"MAC", "key":"imei"},
                                                {"header":"最后登录时间", "key":"lastLoginTimeStr"},
                                                {"header":"操作",
                                                 "key":"userId",
                                                  "template":'<a href="${basePath}/admin/user/delete/{{userId}}" onclick="return confirm(\'删除后无法恢复,确定要删除吗\')" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除</a>&nbsp;&nbsp;<a href="javascript:modify({{userId}})" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;修改</a>'}
                                            ]
                        });
                    }
                });
	</script>
</div>



</body>
</html>
