
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
    <title>自由行1</title>
    <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />

    <style>
        #gbox_list2{margin: 0 auto}
         #gbox_list2{margin: 0 auto}
        .price{
            background-color: red;
            color: #ffffff;}
        .price1{
            background-color: green;
            color: #ffffff;}
        .price2{
            background-color:red;
            color: #ffffff;}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>

    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/i18n/grid.locale-cn.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/jquery.jqGrid.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>

    <script type="text/javascript">
        function calPrice(event){
            var selectedId = $("#list2").jqGrid("getGridParam", "selrow");
            var hotel1 = $.trim($('#'+selectedId + "_hotel1").val());
            var hotel2 = $.trim($('#'+selectedId + "_hotel2").val());
            var hotel3 = $.trim($('#'+selectedId + "_hotel3").val());
            var hotel4 = $.trim($('#'+selectedId + "_hotel4").val());
            var hotel5 = $.trim($('#'+selectedId + "_hotel5").val());
            var hotel6 = $.trim($('#'+selectedId + "_hotel6").val());

            var mp1 = $.trim($('#'+selectedId + "_mp1").val());
            var mp2 = $.trim($('#'+selectedId + "_mp2").val());
            var mp3 = $.trim($('#'+selectedId + "_mp3").val());
            var mp4 = $.trim($('#'+selectedId + "_mp4").val());
            var mp5 = $.trim($('#'+selectedId + "_mp5").val());


            var ddjt1 = $.trim($('#'+selectedId + "_ddjt1").val());
            var ddjt2 = $.trim($('#'+selectedId + "_ddjt2").val());
            var ddjt3 = $.trim($('#'+selectedId + "_ddjt3").val());
            var ddjt4 = $.trim($('#'+selectedId + "_ddjt4").val());
            var ddjt5 = $.trim($('#'+selectedId + "_ddjt5").val());
            var jsz = $.trim($('#'+selectedId + "_jsz").val());

            var qtxm1 = $.trim($('#'+selectedId + "_qtxm1").val());
            var qtxm2 = $.trim($('#'+selectedId + "_qtxm2").val());
            var qtxm3 = $.trim($('#'+selectedId + "_qtxm3").val());
            var qtxm4 = $.trim($('#'+selectedId + "_qtxm4").val());
            var qtxm5 = $.trim($('#'+selectedId + "_qtxm5").val());

            var hotel = 0 ;
            var mp = 0 ;
            var ddjt = 0 ;
            var qtxm = 0 ;
            var total = 0 ;
            if(!isNaN(parseInt(hotel1))){
                hotel +=parseInt(hotel1);
            }
            if(!isNaN(parseInt(hotel2))){
                hotel +=parseInt(hotel2);
            }
            if(!isNaN(parseInt(hotel3))){
                hotel +=parseInt(hotel3);
            }
            if(!isNaN(parseInt(hotel4))){
                hotel +=parseInt(hotel4);
            }
            if(!isNaN(parseInt(hotel5))){
                hotel +=parseInt(hotel5);
            }
            if(!isNaN(parseInt(hotel6))){
                hotel +=parseInt(hotel6);
            }
            $('#'+selectedId + "_hotel").val(hotel/2);

            if(!isNaN(parseInt(mp1))){
                mp +=parseInt(mp1);
            }
            if(!isNaN(parseInt(mp2))){
                mp +=parseInt(mp2);
            }
            if(!isNaN(parseInt(mp3))){
                mp +=parseInt(mp3);
            }
            if(!isNaN(parseInt(mp4))){
                mp +=parseInt(mp4);
            }
            if(!isNaN(parseInt(mp5))){
                mp +=parseInt(mp5);
            }
            $('#'+selectedId + "_mp").val(mp);

            if(!isNaN(parseInt(ddjt1))){
                ddjt +=parseInt(ddjt1);
            }
            if(!isNaN(parseInt(ddjt2))){
                ddjt +=parseInt(ddjt2);
            }
            if(!isNaN(parseInt(ddjt3))){
                ddjt +=parseInt(ddjt3);
            }
            if(!isNaN(parseInt(ddjt4))){
                ddjt +=parseInt(ddjt4);
            }
            if(!isNaN(parseInt(ddjt5))){
                ddjt +=parseInt(ddjt5);
            }
            if(!isNaN(parseInt(jsz))){
                ddjt +=parseInt(jsz);
            }
            $('#'+selectedId + "_ddjt").val(ddjt);

            if(!isNaN(parseInt(qtxm1))){
                qtxm +=parseInt(qtxm1);
            }
            if(!isNaN(parseInt(qtxm2))){
                qtxm +=parseInt(qtxm2);
            }
            if(!isNaN(parseInt(qtxm3))){
                qtxm +=parseInt(qtxm3);
            }
            if(!isNaN(parseInt(qtxm4))){
                qtxm +=parseInt(qtxm4);
            }
            if(!isNaN(parseInt(qtxm5))){
                qtxm +=parseInt(qtxm5);
            }
            $('#'+selectedId + "_qtxm").val(qtxm);

            $('#'+selectedId + "_cdj").val(hotel+mp+ddjt+qtxm);
        }
        $(function(){
            pageInit();
        });
        function pageInit(){
            $("#list2").jqGrid(
                    {
                        url : '${basePath}/admin/travelZyx/list',
                        datatype : "json",
                        colNames : ['ID','批次号','产品编码','出发地', '目的地','天数','交通方式','1','2','3','4','5','小计','1','2','3','4','5','小计','1','2','3','接送站','小计','1','2','小计','纯底价','备注'],
                        colModel : [
                            {name : 'lineid',index : 'lineid',editable : true,hidden:true,key:true},
                            {name : 'pch',index : 'pch',align:'center',editable : true,sortable:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:70},
                            {name : 'cpid',index : 'cpid',align:'center',editable : true,sortable:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:100},
                            {name : 'cfd',index : 'cfd',align:'center',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:50},
                            {name : 'mdd',index : 'mdd',align:'center',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:50},
                            {name : 'days',index : 'days',align:'center',editable : true,sortable:true,search:true,editoptions : {maxlength : 2},searchoptions:{sopt:['eq']},width:30},
                            {name : 'jtfs',index : 'jtfs',align:'center',editable : true,sortable:true,search:true,editoptions : {maxlength : 20},searchoptions:{sopt:['eq']},width:80},
                            {name : 'hotel1',index : 'hotel1',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hotel2',index : 'hote2',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hotel3',index : 'hotel3',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hotel4',index : 'hotel4',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hotel5',index : 'hotel5',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hotel',index : 'hotel',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:55},
                            {name : 'mp1',index : 'mp1',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'mp2',index : 'mp2',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'mp3',index : 'mp3',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'mp4',index : 'mp4',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'mp5',index : 'mp5',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'mp',index : 'mp',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:55},
                            {name : 'ddjt1',index : 'ddjt1',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'ddjt2',index : 'ddjt2',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'ddjt3',index : 'ddjt3',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'jsz',index : 'jsz',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'ddjt',index : 'ddjt',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:55},
                            {name : 'qtxm1',index : 'qtxm1',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'qtxm2',index : 'qtxm2',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'qtxm',index : 'qtxm',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:55},
                            {name : 'cdj',index : 'cdj',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},classes:'price2',width:55},
                            {name : 'remark',index : 'remark',editable : true,sortable:false,search:false,editoptions : {maxlength : 40},width:220}
                        ],
                        rowNum : 20,
                        //rowList : [ 25, 40, 60 ],
                        pager : '#pager2',
                        forceFit:true,
                        sortname : 'cjsj',
                        mtype : "post",
                        viewrecords : true,
                        sortorder : "desc",
                        rownumbers: true,
                        //autowidth:true,
                        height : "100%",
                        editurl : "${basePath}/admin/travelZyx/edit",
                        caption : "自由行"
                    });

            $("#list2").jqGrid('setGroupHeaders', {
             useColSpanStyle: true,
             groupHeaders:[
             {startColumnName:'hotel1', numberOfColumns:6, titleText: '酒店'},
             {startColumnName:'mp1', numberOfColumns: 6, titleText: '门票'},
             {startColumnName:'ddjt1', numberOfColumns: 5, titleText: '当地交通'},
             {startColumnName:'qtxm1', numberOfColumns: 3, titleText: '其它项目'}
             ]
             })
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
           // jQuery("#list2").jqGrid('gridResize');
            $("#list2").filterToolbar({ searchOnEnter: true, stringResult: true, defaultSearch: "cn", groupOp: "AND" });
        }
    </script>
</head>
<body>
<table id="list2" style="margin: 0 auto"></table>
<div id="pager2"></div>
</body>

</html>
