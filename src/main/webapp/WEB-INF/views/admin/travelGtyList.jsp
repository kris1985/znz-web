
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
    <title>跟团游</title>
    <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />

    <style>
        #gbox_list2{margin: 0 auto}
        .price{
            background-color: red;
            color: #ffffff;}
        .price1{
            background-color: green;
            color: #ffffff;}
        .price2{
            background-color: #92820A;
            color: #ffffff;}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>

    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/i18n/grid.locale-cn.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/jquery.jqGrid.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>

    <script type="text/javascript">
        function calPrice(event){
            var selectedId = $("#list2").jqGrid("getGridParam", "selrow");
            var spzxbj = $.trim($('#'+selectedId + "_spzxbj").val());
            var tw = $.trim($('#'+selectedId + "_tw").val());
            var js = $.trim($('#'+selectedId + "_js").val());
            var qtx =$.trim( $('#'+selectedId + "_qtx").val());
            var lr = $.trim($('#'+selectedId + "_lr").val());
            var td = $.trim($('#'+selectedId + "_td").val());
            var qc = $.trim($('#'+selectedId + "_qc").val());
            var hc = $.trim($('#'+selectedId + "_hc").val());
            var cdjg = 0 ;
            var sj = 0 ;
            if(!isNaN(parseInt(spzxbj))){
                cdjg +=parseInt(spzxbj);
            }
            if(!isNaN(parseInt(tw))){
                cdjg +=parseInt(tw);
            }
            if(!isNaN(parseInt(js))){
                cdjg +=parseInt(js);
            }
            if(!isNaN(parseInt(qtx))){
                cdjg +=parseInt(qtx);
            }
            $('#'+selectedId + "_cdjg").val(cdjg);

            if(!isNaN(parseInt(cdjg))){
                sj +=parseInt(cdjg);
            }
            if(!isNaN(parseInt(lr))){
                sj +=parseInt(lr);
            }
            if(!isNaN(parseInt(td))){
                sj +=parseInt(td);
            }
            if(!isNaN(parseInt(qc))){
                sj +=parseInt(qc);
            }
            if(!isNaN(parseInt(hc))){
                sj +=parseInt(hc);
            }
            $('#'+selectedId + "_sj").val(sj);
        }
        $(function(){

            pageInit();


        });
        function pageInit(){
            $("#list2").jqGrid(
                    {
                        url : '${basePath}/admin/travelGty/list',
                        datatype : "json",
                        colNames : ['ID','出发地', '目的地','批次','玩法/门票', '产品名称','天数','交通方式','进出港','等级','散拼报价','头尾','接送','其它项','纯底价','利润','提点','去程','返程','售价','供应商','联系方式','产品编码','备注'],
                        colModel : [
                            {name : 'uid',index : 'uid',editable : true,hidden:true,key:true},
                            {name : 'cfd',index : 'cfd',align:'center',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:50},
                            {name : 'mdd',index : 'mdd',align:'center',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:50},
                            {name : 'pc',index : 'pc',align:'center',editable : true ,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:70},
                            {name : 'wf',index : 'wf',editable : true,sortable:false,search:false,editoptions : {maxlength : 40},width:230},
                            {name : 'cpmc',index : 'cpmc',editable : true,sortable:false,search:true,editoptions : {maxlength : 40},searchoptions:{sopt:['eq']},width:280},
                            {name : 'days',index : 'days',align:'center',editable : true,sortable:true,search:true,editoptions : {maxlength : 2},searchoptions:{sopt:['eq']},width:30},
                            {name : 'jtfs',index : 'jtfs',align:'center',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:60},
                            {name : 'jcg',index : 'jcg',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:60},
                            {name : 'dj',index : 'dj',align:'center',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:40},
                            {name : 'spzxbj',index : 'spzxbj',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:50,classes:'price1'},
                            {name : 'tw',index : 'tw',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'js',index : 'js',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'qtx',index : 'qtx',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'cdjg',index : 'cdjg',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:45,classes:'price'},
                            {name : 'lr',index : 'lr',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'td',index : 'td',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'qc',index : 'qc',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'hc',index : 'hc',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:30},
                            {name : 'sj',index : 'sj',align:'center',editable : true,sortable:true,search:false,editoptions : {maxlength : 6},editrules : {number : true},width:40,classes:'price2'},
                            {name : 'gys',index : 'gys',align:'center',editable : true,sortable:false,search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:80},
                            {name : 'phone',index : 'phone',editable : true,sortable:false,search:false,editoptions : {maxlength : 20},width:140},
                            {name : 'pid',index : 'pid',align:'center',editable : true,sortable:false,search:false,editoptions : {maxlength : 12},width:100},
                            {name : 'remark',index : 'remark',editable : true,sortable:false,search:false,editoptions : {maxlength : 40},width:200}
                        ],
                        rowNum : 10,
                        rowList : [ 25, 40, 60 ],
                        pager : '#pager2',
                        forceFit:true,
                        sortname : 'cjsj',
                        mtype : "post",
                        viewrecords : true,
                        sortorder : "desc",
                        rownumbers: true,
                        // shrinkToFit:false,
                        //autoScroll: true,

                        height : "100%",
                        editurl : "${basePath}/admin/travelGty/edit",
                        caption : "跟团游"
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
            // jQuery("#list2").jqGrid('gridResize');

        }



    </script>
</head>
<body>
<table id="list2" style="margin: 0 auto"></table>
<div id="pager2"></div>
</body>

</html>
