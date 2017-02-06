
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
    <title>二级类别列表</title>
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
           <c:if test="${userSession.user.userType !=2 and userSession.user.userType !=3 }">
                alert("无权限");
                return;
           </c:if>
           pageInit();
       });
       function pageInit(){
           $("#list2").jqGrid(
                   {
                       url : '${basePath}/admin/subCategory/list',
                       datatype : "json",
                       colNames : ['id','parentId', '类别','排序'],
                       colModel : [
                           {name : 'id',index : 'id',search:false,hidden:true},
                           {name : 'parentId',index :'parentId',search:false,hidden:true},
                           {name : 'name',index : 'name',search:false,align:'center',editable : true,editrules : {required : true},searchoptions:{sopt:['eq']},editoptions : {maxlength : 20},sortable : false},
                           {name : 'sortId',index : 'sortId',search:false,align:'center',editable : true,editrules : {required : true},editoptions : {maxlength : 16},sortable : false},
                       ],
                       rowNum : 20,
                       pager : '#pager2',
                       mtype : "post",
                       viewrecords : true,
                       sortorder : "desc",
                       rownumbers: true,
                       width: 800,
                       height : "100%",
                       // cellEdit:true,
                       editurl : "${basePath}/admin/subCategory/edit",
                       grouping:true,
                       groupingView : {
                            groupField : ['parentId'],
  groupColumnShow : [false],
                                           groupText : ['<b>{0} - {1} Item(s)</b>']
                       },
                       caption : "二级类别列表"
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
          jQuery("#list2").jqGrid('gridResize');
           $("#list2").filterToolbar({ searchOnEnter: true, stringResult: true, defaultSearch: "cn", groupOp: "AND" });
       }

    </script>
</head>
<body>
    <table id="list2" style="margin: 0 auto"></table>
    <div id="pager2"></div>
</div>

</body>

</html>
