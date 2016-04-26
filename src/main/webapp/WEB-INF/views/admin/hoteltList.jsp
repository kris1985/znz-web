
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
    <title>酒店列表</title>
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
       });
       function pageInit(){
           $("#list2").jqGrid(
                   {
                       url : '${basePath}/admin/hotel/list',
                       datatype : "json",
                       colNames : ['产品ID','酒店名称', '地区','星级', '淡季','旺季',  '黄金周','春节','五一','重要政策','采购方式','采购电话','备注'],
                       colModel : [
                           {name : 'id',index : 'id',key:true,editable : true,hidden:true},
                           {name : 'hotelName',index : 'hotelName',editable : true,sortable:false,editoptions : {maxlength : 40},editrules : {required : true},searchoptions:{sopt:['eq']},width:300,searchoptions:{sopt:['eq']}},
                           {name : 'areaName',index : 'areaName',editable : true,sortable:false,editoptions : {maxlength : 10},editrules : {required : true},width:100,searchoptions:{sopt:['eq']}},
                           {name : 'xj',index : 'xj',editable : true ,sortable:false,searchoptions:{sopt:['eq']},editoptions : {maxlength : 10},searchoptions:{sopt:['eq']}},
                           {name : 'dj',index : 'dj',editable : true,sortable:false,editrules : {number : true},search:false,searchoptions:{sopt:['eq']},editoptions : {maxlength : 10}},
                           {name : 'wj',index : 'wj',editable : true,sortable : true,editrules : {number : true},search:false,editoptions : {maxlength : 10}},
                           {name : 'hjz',index : 'hjz',editable : true,sortable : true,editrules : {number : true},search:false,editoptions : {maxlength : 10}},
                           {name : 'cj',index : 'cj',editable : true,sortable : true,editrules : {number : true},search:false,editoptions : {maxlength : 10}},
                           {name : 'wy',index : 'wy',editable : true,sortable : true,editrules : {number : true},search:false,editoptions : {maxlength : 10}},
                           {name : 'zyzc',index : 'zyzc',editable : true,sortable : false,search:false,editoptions : {maxlength : 10}},
                           {name : 'cgfs',index : 'cgfs',editable : true,sortable : false,search:false,editoptions : {maxlength : 10}},
                           {name : 'cgdh',index : 'cgdh',editable : true,sortable : false,search:false,editoptions : {maxlength : 10}},
                           {name : 'remark',index : 'remark',editable : true,sortable:false,search:false,editoptions : {maxlength : 40}}
                       ],
                       rowNum : 10,
                       rowList : [ 10, 20, 30 ],
                       pager : '#pager2',
                       sortname : 'create_time',
                       mtype : "post",
                       viewrecords : true,
                       sortorder : "desc",
                       rownumbers: true,
                       width: 1000,
                       height : "100%",
                       // cellEdit:true,
                       editurl : "${basePath}/admin/hotel/edit",
                       caption : "酒店列表"
                   });

           <c:if test="${userSession.user.userType==1 }">
           jQuery("#list2").jqGrid('navGrid', "#pager2", {
               edit : false,
               add : false,
               del : false
           },{},{},{},{multipleSearch:true});
           </c:if>
           <c:if test="${userSession.user.userType==2 or userSession.user.userType==3}">
           jQuery("#list2").jqGrid('navGrid', "#pager2", {
               edit : false,
               add : false,
               del : true
           },{},{},{},{multipleSearch:true});
           jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true});
           </c:if>
           jQuery("#list2").jqGrid('gridResize');
           $("#list2").filterToolbar({ searchOnEnter: true, stringResult: true, defaultSearch: "cn", groupOp: "AND" });

       }

    </script>
</head>
<body>
    <table id="list2" style="margin: 0 auto"></table>
    <div id="pager2"></div>
</body>

</html>
