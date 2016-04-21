
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
    <title>线路列表</title>
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
           $(window).bind('resize', function() {
               $("#list2").setGridWidth($(window).width()*0.75);
               $("#list2").setGridHeight($(window).height()*0.68);
           });
       });
       function pageInit(){
           $("#list2").jqGrid(
                   {
                       url : '${bathPath}/admin/travleLine',
                       postData:{lineType:'zyx'},
                       datatype : "json",
                       colNames : ['ID','产品编号', '散拼中心','行程天数', '交通方式','酒店'],//, '住宿','房差','接送','赠送','散拼中心报价','儿童底价','利润','提点','最终底价','备注'
                       colModel : [
                           {name : 'id',index : 'id',key:true,editable : true,hidden:true},
                           {name : 'prodNo',index : 'prodNo',editable : true,sortable:false,editoptions : {maxlength : 36},editrules : {required : true},width:300,searchoptions:{sopt:['eq']}},
                           {name : 'spzx',index : 'spzx',editable : true,sortable:false,editoptions : {maxlength : 16},width:100,searchoptions:{sopt:['eq']}},
                           {name : 'days',index : 'days',editable : true ,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']}},
                           {name : 'jtfs',index : 'jtfs',editable : true,sortable:false,editrules : {number : true},search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']}},
                           {name : 'hotel',index : 'hotel',editable : true,sortable : true,editrules : {number : true},editoptions : {maxlength : 20},searchoptions:{sopt:['eq']}},
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
                       width: 800,
                       height : "100%",
                       // cellEdit:true,
                       editurl : "${bathPath}/admin/hotel/edit",
                       caption : "线路列表"
                   });
           jQuery("#list2").jqGrid('navGrid', "#pager2", {
               edit : false,

               add : false
           },{},{},{},{multipleSearch:true});
           jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true},{},{},{},{multipleSearch:true});
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
</body>

</html>
