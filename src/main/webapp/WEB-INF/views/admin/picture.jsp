<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit"/>
    <link rel="icon" href="../../favicon.ico">
    <title>指南针鞋讯-目录</title>
    <%@ include file="../common/common.jsp" %>

    <link href="${basePath}/resources/css/jquery-ui-1.8.24.custom.css" rel="stylesheet"  />
    <link href="${basePath}/resources/css/jquery.contextMenu.css" rel="stylesheet">
    <link href="${basePath}/resources/css/aqy.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${basePath}/resources/css/skins/black.css"/>
    <link rel="stylesheet" href="${basePath}/resources/css/city-select.css">

    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/artDialog.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.contextmenu.r2.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/iframeTools.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jqPaginator.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.lazyload.min.js"></script>
    <script language="javascript" type="text/javascript" src="${basePath}/resources/js/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/newcitydata.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/citySelect-1.0.3.js"></script>
    <style>
        .BB +div {border-top: 1px dotted #d5d5d5}
        .close_btn{position: absolute;right:20px;top:0px}
        .close_btn a {
            border-bottom: 1px solid transparent;
            font-size: 14px;
            color: #666;
            padding: 0 6px;
            height: 36px;
            line-height: 36px;
            text-align: center;
            position: relative;
            bottom: -1px;
            -moz-transition: all 1s;
            -o-transition: all 1s;
            -webkit-transition: all 1s;
            transition: all 1s;
        }
        #searchTxtBtn{width: 90px;
            cursor: pointer;
            display: inline-block;
            margin-left: 20px;
            font-size: 14px;
            padding: 4px;
            background: url(../../resources/icon/icon_search.png) 62px 50% no-repeat;}
        #searchTxtBtn:hover{color: #699f00;}
        .city-info{display: none;}
        .brand_ui{
            position: absolute;
            left: 330px;
            top: 145px;
            word-wrap: break-word;
            width: 100%;}
        .brand_ui li {
            white-space: nowrap;
            float: left;
            display: inline;
            margin: 0 20px 6px 0;
            line-height: 22px;
            font-size: 12px;
        }
        .brand_ui .selected a {
            background: #699f00;
            padding: 1px 7px 2px;
            color: #fff;
            border-radius: 1px;
        }
        .CC  .selected a {
            background: #699f00;
            padding: 1px 7px 2px;
            color: #fff;
            border-radius: 1px;
        }
        .brand_ui a {
            display: inline-block;
            padding: 1px 7px 2px;
            font-size: 14px;
        }
        .brand_ui .selected a {
            background: #699f00;
            padding: 1px 7px 2px;
            color: #fff;
            border-radius: 1px;
        }
        /*
            .city-tabs{position: relative}
        */
    </style>
    <script>
        var basePath = getContextPath();
            $.ajax({
                type:'post',
                url:basePath+"/categorys",
                contentType :'application/json;charset=utf-8',
                data:'{"token":"8f837a356ef43988d7fc7db4b63f53d3"}',
                success:function(data){
                    console.log(data.result);
                //循环获取数据
                    var res = "";
                    $.each(data.result, function(i, item) {
                       // var id = item.id;
                        var name = item.name;
                        var sortId = item.sortId;
                        var selectClass = "";
                        if(i==0){
                            selectClass = " selected";
                        }
                        res +="<li id=id class='li_item  noLeaf" +selectClass+"'>  <a href=\"#\">" + name+"</a></li>";
                    });
                    $(".navbar-ul").html(res);
                    //二级栏目
                    var res2 = "";
                    $.each(data.result[0].childrens, function(i, item) {
                        var name = item.name;
                        var sortId = item.sortId;
                        var selectClass = "";
                        if(i==0){
                            selectClass = " selected";
                        }
                        res2 +="<li id=id class='li_item  noLeaf" +selectClass+"'>  <a href=\"#\">" + name+"</a></li>";
                    });
                    $("#no_leaf_item").html(res2);
                    //三级栏目
                    var res3 = "";
                    $.each(data.result[0].childrens[0].childrens, function(i, item) {
                        var name = item.name;
                        var sortId = item.sortId;
                        var selectClass = "";
                        res3 +='<div class="mod_sear_list" > <h3 id="4758" class="choice_item"><span>'+name+'</span>：</h3>';
                        res3+='<ul class="mod_category_item  ui-sortable">';
                        res3+='<li id="all_4758" class="li_item leaf_item all_item  selected" categorylevel="3" parentid="4758"><a id="aa" href="javascript:void()" class="">全部</a></li>';
                        $.each(item.childrens, function(j, item) {
                            var name = item.name;
                            res3+=' <li id="4759" class=" li_item leaf_item" > <a id="a_4759" href="javascript:void()">'+name+'</a></li>';
                        });
                        res3+= '</ul></div>';
                    });
                    $("#leaf_category").html(res3);
             }
        });
    </script>

</head>

<body>
    <div class="header">
        <div class="nav_bar">
            <div class="logo">
                <a href="/"><img src="${basePath}/resources/img/logo.png" height="38"> </a>
            </div>
            <!--一级栏目-->
            <div class="nav_warp">
                <ul class="navbar-ul mod_category_item">
                </ul>
            </div>
        </div>
    </div>


  <div id="container" class="site-main">
        <div class="ad-wrapper clearfix">
            <div class="divide-green-h"></div>
        </div>

        <div class="mod_sear_menu mt20 " style="margin-bottom: 20px;">
            <!--二级栏目-->
            <div class="mod_sear_list" id="mod_68">
                <h3>目录：</h3>
                <ul class="mod_category_item ui-sortable" id="no_leaf_item">
                    <li id="1038" class="li_item selected noLeaf" ><a href="#">热门下载</a>
                    </li>
                </ul>
            </div>
            <div id="leaf_category" class="ui-sortable">
                <!--三级栏目-->

            </div>
        </div>

    </div>
        <div  class="ad-wrapper clearfix">
            <div class="divide-green-h"></div>
        </div>

        <div class="mod-page">
            <ul class="pagination" id="pagination1"></ul>
        </div>
        <div id="scrollTop" >
            <div class="level-2"></div>
            <div class="level-3"></div>
        </div>
        <div id="1000000000046" class="ad-wrapper clearfix">
            <div class="divide-green-h"></div>
        </div>
    </div>


<div class="footerN1214">
    <p class="footmenu">
        <a href="#" class="s1"></a>
    </p>
    <p class="fEn">
        <a href="http://tyulan.com/" class="link0"></a>&nbsp;&nbsp;
        2017 TYULAN.COM
    </p>
</div>
<script type="text/javascript" src="${basePath}/resources/js/top.js"></script>
</body>

</html>