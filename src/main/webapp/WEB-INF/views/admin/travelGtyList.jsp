
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
    <title>跟团游</title>
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
         /*  $(window).bind('resize', function() {
               $("#list2").setGridWidth($(window).width()*0.9);
           });*/

       });
       function pageInit(){
           $("#list2").jqGrid(
                   {
                       url : '${bathPath}/admin/travelGty/list',
                       datatype : "json",
                       colNames : ['ID','出发地', '目的地','批次','玩法/门票', '产品名称','天数','交通方式','进出港','等级','散拼报价','头尾','接送','其它项','纯底价','利润','提点','去程','返程','售价','供应商','联系方式','产品编码','备注'],
                       colModel : [
                           {name : 'uid',index : 'uid',editable : true,hidden:true},
                           {name : 'cfd',index : 'cfd',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:120},
                           {name : 'mdd',index : 'mdd',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:120},
                           {name : 'pc',index : 'pc',editable : true ,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                           {name : 'wf',index : 'wf',editable : true,sortable:false,search:false,editoptions : {maxlength : 40},width:180},
                           {name : 'cpmc',index : 'cpmc',editable : true,sortable:false,search:true,editoptions : {maxlength : 40},searchoptions:{sopt:['eq']},width:180},
                           {name : 'days',index : 'days',editable : true,sortable:true,search:true,editoptions : {maxlength : 1},searchoptions:{sopt:['eq']},width:90},
                           {name : 'jtfs',index : 'jtfs',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:140},
                           {name : 'jcg',index : 'jcg',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:140},
                           {name : 'dj',index : 'dj',editable : true,sortable:false,search:false,editoptions : {maxlength : 10},width:90},
                           {name : 'spzxbj',index : 'spzxbj',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'tw',index : 'tw',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'js',index : 'js',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'qtx',index : 'qtx',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:120},
                           {name : 'cdjg',index : 'cdjg',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'lr',index : 'lr',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'td',index : 'td',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'qc',index : 'qc',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'hc',index : 'hc',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                           {name : 'sj',index : 'sj',editable : true,sortable:true,search:false,editoptions : {maxlength : 6},editrules : {number : true},width:90},
                           {name : 'gys',index : 'gys',editable : true,sortable:false,search:true,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:140},
                           {name : 'phone',index : 'phone',editable : true,sortable:false,search:false,editoptions : {maxlength : 20},width:140},
                           {name : 'pid',index : 'pid',editable : true,sortable:false,search:false,editoptions : {maxlength : 12},width:140},
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
                       width: 1200,
                       //autowidth:true,
                       height : "100%",
                       editurl : "${bathPath}/admin/travelGty/edit",
                       caption : "线路列表"
                       });
           jQuery("#list2").jqGrid('navGrid', "#pager2", {
               edit : false,

               add : false
           },{},{},{},{multipleSearch:true});
           jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true},{},{},{},{multipleSearch:true});
           /*$("#list2").jqGrid('setGroupHeaders', {
               useColSpanStyle: true,
               groupHeaders:[
                   {startColumnName:'mdd', numberOfColumns:2, titleText: '商品介绍'},
                   {startColumnName:'cdjg', numberOfColumns: 2, titleText: '商品属性'}
               ]
           })*/
         //  jQuery("#list2").jqGrid('inlineNav', "#pager2");
           //设置显示列
         /*  jQuery("#vcol").click(function (){
               jQuery("#setcols").jqGrid('setColumns');
           });*/
           //自定义grid大小
         jQuery("#list2").jqGrid('gridResize',{minWidth:350,maxWidth:1500,minHeight:200, maxHeight:950});


       }



    </script>
</head>
<body>
    <table id="list2" style="margin: 0 auto"></table>
    <div id="pager2"></div>
</body>

</html>
