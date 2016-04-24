
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
    <title>计划安排</title>
    <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />

    <style>
        #gbox_list2{margin: 0 auto}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>

    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/i18n/grid.locale-cn.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/jquery.jqGrid.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>

    <script type="text/javascript">

        $(function(){
            pageInit();
        });
        function pageInit(){
            $("#list2").jqGrid(
                    {
                        url : '${bathPath}/admin/plan/list',
                        datatype : "json",
                        colNames : ['ID','计划日期','游客姓名','成人', '小孩','出游日期','目的地','类型','保险','成交额','平台','地接社','是否已发','确认回传','操作员','备注'],
                        colModel : [
                            {name : 'uid',index : 'uid',editable : true,hidden:true,key:true},
                            {name : 'jhrq',index : 'jhrq',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:140},
                            {name : 'name',index : 'name',editable : true,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:140},
                            {name : 'cr',index : 'cr',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:120},
                            {name : 'xh',index : 'xh',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:120},
                            {name : 'hyrq',index : 'hyrq',editable : true,sortable:true,search:false,editoptions : {maxlength : 10},width:140},
                            {name : 'mdd',index : 'mdd',editable : true,sortable:false,search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                            {name : 'lx',index : 'lx',editable : true,sortable:false,search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                            {name : 'bx',index : 'bx',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:120},
                            {name : 'cje',index : 'cje',editable : true,sortable:true,search:false,editoptions : {maxlength : 10},width:120},
                            {name : 'pt',index : 'pt',editable : true,sortable:false,search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                            {name : 'djs',index : 'djs',editable : true,sortable:false,search:false,editoptions : {maxlength : 10}},
                            {name : 'sfyf',index : 'sfyf',editable : true,sortable:true,search:false,editoptions : {maxlength : 10}},
                            {name : 'qrhc',index : 'qrhc',editable : true,sortable:true,search:false,editoptions : {maxlength : 10}},
                            {name : 'czy',index : 'czy',editable : true,sortable:false,search:false,editoptions : {maxlength : 10}},
                            {name : 'remark',index : 'remark',editable : true,sortable:false,search:false,editoptions : {maxlength : 40}}
                        ],
                        rowNum : 10,
                        rowList : [ 10, 20, 30 ],
                        pager : '#pager2',
                        forceFit:true,
                        sortname : 'cjsj',
                        mtype : "post",
                        viewrecords : true,
                        sortorder : "desc",
                        rownumbers: true,
                        width: 1000,
                        //autowidth:true,
                        height : "100%",
                        editurl : "${bathPath}/admin/plan/edit",
                        caption : "计划安排"
                    });
            <c:if test="${user.user.userType==1 }">
            jQuery("#list2").jqGrid('navGrid', "#pager2", {
                edit : false,
                add : false,
                del : false
            },{},{},{},{multipleSearch:true});
            </c:if>
            <c:if test="${user.user.userType==2 or user.user.userType==3}">
            jQuery("#list2").jqGrid('navGrid', "#pager2", {
                edit : false,
                add : false,
                del : true
            },{},{},{},{multipleSearch:true});
            jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true});
            </c:if>
            jQuery("#list2").jqGrid('gridResize');

        }
    </script>
</head>
<body>
<table id="list2" style="margin: 0 auto"></table>
<div id="pager2"></div>
</body>

</html>
