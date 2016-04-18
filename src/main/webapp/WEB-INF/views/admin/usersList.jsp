
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
    <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />
    <style>
        #gbox_list2{margin: 0 auto}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/i18n/grid.locale-cn.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/jquery.jqGrid.min.js" ></script>

    <script type="text/javascript">
       $(function(){
           pageInit();
         /*  $(window).bind('resize', function() {
               $("#list2").setGridWidth($(window).width()*0.75);
               $("#list2").setGridHeight($(window).height()*0.68);
           });*/
       });
       function pageInit(){
           $("#list2").jqGrid(
                   {
                       url : '${bathPath}/admin/user/list2',
                       datatype : "json",
                       colNames : ['userId', '用户名','密码', '公司','手机号',  '最后登录时间'],
                       colModel : [
                           {name : 'userId',index : 'userId',key:true,hidden:true,editable : true},
                           {name : 'userName',index : 'userName',editable : true},
                           {name : 'pwd',index : 'pwd',editable : true},
                           {name : 'company',index : 'company',editable : true},
                           {name : 'phone',index : 'phone',editable : true},
                           {name : 'lastLoginTimeStr',index : 'lastLoginTimeStr',sortable : true}
                       ],
                       rowNum : 10,
                       rowList : [ 10, 20, 30 ],
                       pager : '#pager2',
                       sortname : 'id',
                       mtype : "post",
                       viewrecords : true,
                       sortorder : "desc",
                       rownumbers: true,
                       width: 700,
                       height : "100%",
                       // cellEdit:true,
                       editurl : "${bathPath}/admin/user/edit",
                       caption : "用户列表"
                   });
           jQuery("#list2").jqGrid('navGrid', "#pager2", {
               edit : false,
               add : false,
               del : true
           });
           jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true});
         //  jQuery("#list2").jqGrid('inlineNav', "#pager2");
           //设置显示列
         /*  jQuery("#vcol").click(function (){
               jQuery("#setcols").jqGrid('setColumns');
           });*/
           //自定义grid大小
         //jQuery("#list2").jqGrid('gridResize',{minWidth:350,maxWidth:800,minHeight:80, maxHeight:850});
       }

    </script>
</head>
<body>
    <table id="list2" style="margin: 0 auto"></table>
    <div id="pager2"></div>
</div>

</body>

</html>
