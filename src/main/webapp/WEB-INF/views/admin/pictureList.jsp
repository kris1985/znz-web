
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title>图片列表1</title>
     <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/css/file.css" rel="stylesheet">

    <style>
        #gbox_list2{margin: 0 auto}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>
<script>
 $(document).ready(function () {
     $("#container").delegate('.img_wrap', 'click', function () {
                $(".img_wrap").removeClass("folder_wrap_selected")
                $(".folder_wrap").removeClass("folder_wrap_selected")
                $(this).addClass("folder_wrap_selected");
            });
});
</script>
</head>
<body>

<div id="container">


<div id="category">

 <div class="input-group">
      <input type="text" class="form-control" placeholder="输入文件名称搜索">
      <span class="input-group-btn">
        <button class="btn btn-default" type="button">搜索</button>
      </span>
    </div>

        <div id ="subcategory" style="margin-top:20px;">
            <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <span>${item.name}</span>
             </c:forEach>
        </div>
</div>
<div style="clear:both"></div>
<!--
  <hr>
        <c:forEach var="item" items="${pictures}" varStatus="status">
        <div class='img_wrap'>
          <img id="${item.id}" class='thumb' src='http://testznz.oss-cn-shanghai.aliyuncs.com/${item.name}?x-oss-process=image/resize,m_pad,h_182,w_256'  style='max-width:256px;max-height:182px' >
          <div class='img_txt'>${item.name}</div>
         </div>
        </c:forEach>
</div>
  <hr>
-->
  <div class="row" style="margin-top:20px">
    <c:forEach var="item" items="${pictures}" varStatus="status">
    <div class="col-xs-2 col-md-2">
      <a href="#" class="thumbnail">
        <img src="http://testznz.oss-cn-shanghai.aliyuncs.com/${item.name}?x-oss-process=image/resize,m_pad,h_182,w_256" alt="...">
      </a>
       <div class='img_txt'>${item.name}</div>
    </div>
    </c:forEach>
  <a href="/znz-web/admin/file/browse">上传</a>
</body>

</html>
