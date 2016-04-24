
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
            $('#'+selectedId + "_hotel").val(hotel);
            $('#'+selectedId + "_hoteldiff").val(hotel/2);

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
            /*  $(window).bind('resize', function() {
             $("#list2").setGridWidth($(window).width()*0.9);
             });*/

        });
        function pageInit(){
            $("#list2").jqGrid(
                    {
                        url : '${bathPath}/admin/travelZyx/list',
                        datatype : "json",
                        colNames : ['ID','批次号','产品编码','出发地', '目的地','天数','交通方式','1','2','3','4','5','6','住宿合计','房差合计','1','2','3','4','5','门票合计','1','2','3','4','5','接送站','交通合计','1','2','3','4','5','其它合计','纯底价','备注'],
                        colModel : [
                            {name : 'lineid',index : 'lineid',editable : true,hidden:true},
                            {name : 'pch',index : 'pch',editable : true,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                            {name : 'cpid',index : 'cpid',editable : true,sortable:false,editoptions : {maxlength : 10},searchoptions:{sopt:['eq']},width:120},
                            {name : 'cfd',index : 'cfd',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:120},
                            {name : 'mdd',index : 'mdd',editable : true,sortable:false,editoptions : {maxlength : 6},searchoptions:{sopt:['eq']},width:120},
                            {name : 'days',index : 'days',editable : true,sortable:false,search:true,editoptions : {maxlength : 1},searchoptions:{sopt:['eq']},width:90},
                            {name : 'jtfs',index : 'jtfs',editable : true,sortable:true,search:true,editoptions : {maxlength : 20},searchoptions:{sopt:['eq']},width:180},
                            {name : 'hotel1',index : 'hotel1',editable : true,sortable:false,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel2',index : 'hote2',editable : true,sortable:false,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel3',index : 'hotel3',editable : true,sortable:false,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel4',index : 'hotel4',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel5',index : 'hotel5',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel6',index : 'hotel6',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'hotel',index : 'hotel',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'hoteldiff',index : 'hoteldiff',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'mp1',index : 'mp1',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'mp2',index : 'mp2',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'mp3',index : 'mp3',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'mp4',index : 'mp4',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'mp5',index : 'mp5',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'mp',index : 'mp',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'ddjt1',index : 'ddjt1',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'ddjt2',index : 'ddjt2',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'ddjt3',index : 'ddjt3',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'ddjt4',index : 'ddjt4',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'ddjt5',index : 'ddjt5',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'jsz',index : 'jsz',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'ddjt',index : 'ddjt',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'qtxm1',index : 'qtxm1',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'qtxm2',index : 'qtxm2',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'qtxm3',index : 'qtxm3',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'qtxm4',index : 'qtxm4',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'qtxm5',index : 'qtxm5',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:90},
                            {name : 'qtxm',index : 'qtxm',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
                            {name : 'cdj',index : 'cdj',editable : true,sortable:true,search:false,editoptions : {maxlength : 6,dataEvents:[{type: 'keyup', fn: function(e) { calPrice(e)}}]},editrules : {number : true},width:140},
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
                        width: 1600,
                        //autowidth:true,
                        height : "100%",
                        editurl : "${bathPath}/admin/travelZyx/edit",
                        caption : "线路列表"
                    });
            jQuery("#list2").jqGrid('navGrid', "#pager2", {
                edit : false,

                add : false
            },{},{},{},{multipleSearch:true});
            jQuery("#list2").jqGrid('inlineNav', '#pager2', {edit : true,add : true,del : true},{},{},{},{multipleSearch:true});
            $("#list2").jqGrid('setGroupHeaders', {
             useColSpanStyle: true,
             groupHeaders:[
             {startColumnName:'hotel1', numberOfColumns:6, titleText: '酒店'},
             {startColumnName:'mp1', numberOfColumns: 5, titleText: '门票'},
             {startColumnName:'ddjt1', numberOfColumns: 5, titleText: '当地交通'},
             {startColumnName:'qtxm1', numberOfColumns: 5, titleText: '其它项目'}
             ]
             })
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
