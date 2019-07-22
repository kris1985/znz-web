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
        .city-info{display: none;}
        /*
            .city-tabs{position: relative}
        */
        .brand_item{padding: 5px 7px 5px 7px;
            border-radius: 1px;
            display: inline-block;
            /* height: 20px; */
            margin-left: 20px;
            font-size: 14px;
            cursor: pointer;}
    </style>
    <script>
        var basePath = getContextPath();
        var  nav ;
        var pics ;
        $(function(){
            $("img.lazy").lazyload({
                threshold : 600
            }).removeClass("lazy");
            $(document).ajaxStop(function(){
                $("img.lazy").lazyload({
                    threshold : 600
                }).removeClass("lazy");
            });

            $.ajax({
                type:'post',
                url:basePath+"/categorys",
                contentType :'application/json;charset=utf-8',
                data:'{"token":"6910514dea127287198790ee76cbb3b1"}',
                success:function(data){
                    nav = data.result;
                //循环获取数据
                    var res = "";
                    $.each(nav, function(i, item) {
                        var id = item.id;
                        var name = item.name;
                        var sortId = item.sortId;
                        var selectClass = "";
                        if(i==0){
                            selectClass = " selected";
                        }
                        res +="<li id="+id+" class='li_item  noLeaf" +selectClass+"'>  <a href=\"#\">" + name+"</a></li>";
                    });
                    $(".navbar-ul").html(res);
                    //二级栏目
                    showNav2(0);
                    //三级栏目
                    showNav3(0,0);
                    //显示图片
                    showPic(null);
             }
        });

            $(".openBtn").live("click",function(evt){
                if( $(this).find("em").text()=="更多"){
                    $(this).parent().css("height","auto");
                    $.cookie($(this).parent().attr("id"), "auto");
                    $(this).find("em").text("收起");
                    $(this).find("i").css("background-position", "0 -10px")
                }else{
                    $(this).parent().css("height","29px");
                    $.cookie($(this).parent().attr("id"), "29px");
                    $(this).find("em").text("更多");
                    $(this).find("i").css("background-position", "-19px -10px")
                }
            })

        //点击栏目
        $(".li_item").live("click",function(evt){
                if ($(this).hasClass("selected")) {
                    return;
                }
                $(this).addClass("selected");
                $(this).siblings().removeClass("selected");
                //一级栏目
                if($(this).parent().hasClass("navbar-ul")){
                    $(".city-info span").attr("data-id","");//切换栏目时清空品牌id
                    $(".BB").hide();
                    $("#brandSelected").hide();
                    var id = parseInt($(this).attr("id"));
                    $.each(nav, function(i, item) {
                        if(item.id == id){
                            showNav2(i);
                            showNav3(i,0);
                        }
                    });
                }
                //二级栏目
                if($(this).parent().hasClass("no_leaf_item")){
                    $(".city-info span").attr("data-id","");//切换栏目时清空品牌id
                    $(".BB").hide();
                    $("#brandSelected").hide();
                    var rootId = parseInt($(".navbar-ul").find(".selected").attr("id"));
                    var id = parseInt($.trim($(this).attr("id")));
                    var hasFlag = $(this).attr("brandFlag");
                    console.log(rootId+"_"+id);
                    var index ;
                    var subIndex ;
                    $.each(nav, function(i, item) {
                        if(item.id == rootId){
                            index = i;
                        }
                    });
                    $.each(nav[index].childrens, function(i, item) {
                        if(item.id == id){
                            subIndex = i;
                        }
                    });
                    if(hasFlag=="true"){
                        $(".BB").show();
                    }else{
                        $(".BB").hide();
                    }
                    showNav3(index,subIndex);
                }
                showPic(null);
                if( $(this).parent().children().length>15){
                    //location.hash="piclist"; //解决栏目太多看不到图片切换
                }
        });

            $("#searchTxtBtn").click(function(){
                var ppid = parseInt($("#no_leaf_item").find(".selected").attr("id"));//选中的二级目录
                $.get("${basePath}/admin/subCategory/brand/"+ppid,function(data){
                    var singleSelect1 = $('#single-select-1').citySelect({
                        dataJson: data,
                        multiSelect: false,
                        whole: true,
                        shorthand: true,
                        search: true,
                        onInit: function () {
                            console.log("----"+this);
                        },
                        onTabsAfter: function (target) {
                            console.log("1111111:"+target)
                        },
                        onCallerAfter: function (target, values) {
                            //console.log("22222222:"+JSON.stringify(values));
                            var brandId = values.id;
                            var brandName = values.name;
                            showPic(brandId);
                            $("#brandAll").removeClass("selected");
                            $('.city-info').hide();
                            $('.city-pavilion').hide();
                            // $('.city-list').show();
                            $('.city-cont').hide();
                            $("#brandSelected").show(brandName);
                            $("#brandSelected").text(brandName);
                        }
                    });
                   $('.city-info').show();
                    $('.city-pavilion').show();
                    // $('.city-list').show();
                    $('.city-cont').show();
                    $(this).hide();
                    $('.input-search').click();
                    $(".city-z dt").text("全部");
                })
            });

            $("#brandAll").click(function () {
                $(".city-info span").attr("data-id","");
                $(".city-info span").text("")
               // $("#single-select-1").hide()
                showPic(null);
                $(this).addClass("selected");
                $("#brandSelected").hide();
            });

        });
          function  showNav2(index){
            var res2 = "";
            var len = 0;
            var hasFlag="";
            $.each(nav[index].childrens, function(i, item) {
                var id = item.id;
                var name = item.name;
                len +=name.length;
                var brandFlag = item.brandFlag;
                var selectClass = "";
                if(i==0){
                    hasFlag = brandFlag;
                    selectClass = " selected";
                }
                res2 +='<li brandFlag="'+brandFlag+'" id=\"'+id+'\" class="li_item  noLeaf' +selectClass+'">  <a href=\"#\">' + name+'</a></li>';
            });
            $("#no_leaf_item").html(res2);
            if(len>40){
                $("#no_leaf_item +.openBtn").show();
            }else{
                $("#no_leaf_item +.openBtn").hide();
            }
            console.log("hasFlag:"+hasFlag)
            if(hasFlag==true){
                $(".BB").show();
            }else{
                $(".BB").hide();
            }
        }

        function  showNav3(index,subIndex) {
            var res3 = "";
            $.each(nav[index].childrens[subIndex].childrens, function(i, item) {
                var id = item.id;
                var name = item.name;
                var sortId = item.sortId;
                var selectClass = "";
                res3 +='<div class="mod_sear_list" > <h3 id="4758" class="choice_item"><span>'+name+'</span>：</h3>';
                res3+='<ul class="mod_category_item  ui-sortable">';
                res3+='<li id="all_4758" class="li_item leaf_item all_item  selected" categorylevel="3" parentid="4758">' +
                    '<a id="aa" href="javascript:void()" class="">全部</a></li>';
                var len=0;
                $.each(item.childrens, function(j, item) {
                    var id = item.id;
                    var name = item.name;
                    len += name.length;
                    res3+=' <li id='+id+' class=" li_item leaf_item"> <a id="a_4759" href="javascript:void()">'+name+'</a></li>';
                });
                var display = "none";
                if(len>40){
                    display = "block";
                }
                res3+= '</ul><div class="openBtn" style="display:'+display+ '">'+
                    '                            <a class="openBtn-txt" href="javascript:;" j-delegate="action"><em class="vm-inline">更多</em><i class="site-icons ico-explain-b"></i></a>\n' +
                    '                        </div></div>';
            });
            $("#leaf_category").html(res3);
        }

        function getAllSelected() {
            var selected = "";
            $(".all_item").each(function (i) {
                if(!$(this).hasClass("selected")){ //如果非全选，获取所有兄弟节点的id
                    $.each($(this).parent().children().filter(".selected"), function (i, n) {
                        id = $(this).attr("id");
                        selected += id + ","
                    });

                }
            });
            if(selected!=""){
                selected = selected.substring(0,selected.length-1);
            }
            return selected;
        }

        function  showPic(brandId) {
           var brandId = $(".city-info span").attr("data-id")
           var selected = getAllSelected();
           var secondCategoryId = $("#no_leaf_item").find(".selected").attr("id");
           if(brandId!=null &&brandId!=undefined && brandId!="" ){
               if(selected == ""){
                   selected = brandId;
               }else{
                   selected = selected+","+brandId;
               }
             }
            $.ajax({
                type:'post',
                url:basePath+"/pictures",
                contentType :'application/json;charset=utf-8',
                data:'{"token":"6910514dea127287198790ee76cbb3b1","data":{\n' +
                    '\t"categoryIds":\"'+selected+'\",\n"secondCategoryId":\"' +secondCategoryId+"\""+
                    ' ,   "currentPage":1,\n' +
                    '    "pageSize" :120\n' +
                    '\t}}',
                success:function(data){
                    //循环获取数据
                    var res = "";
                    pics = data;
                    console.log("data:"+data);
                    if(data.result==null){
                        $(".site-piclist").empty();
                        return;
                    }
                    $.each(data.result.pictures, function(i, item) {
                        var id = item.id;
                        var name = item.name;
                        var filePath= item.filePath;
                        res +='<li item="953551" id="pic_itme_953551" gid="">  ' +
                                '<div class="site-piclist_pic" style="border: 2px solid white;">' +
                                    ' <a id="953551" path="" title="" href="javascript:void(0)" class="site-piclist_pic_link" attach="">' +
                                        '<img class="lazy" alt="" title="" style="border: 0px; display: inline;" src="/resources/img/grey.gif" width="384" height="288" ' +
                                        'data-original="${ossPath}/'+filePath+'?x-oss-process=image/resize,m_pad,h_288,w_384'+data.result.pictureProperty.waterMark+'">'
                        +' </a></div></li>';
                    });
                    $(".site-piclist").html(res);

                }
            });

        }

        function closeSelect(){
            $("#close_btn").click(function(){
                $('.city-info').hide();
                $('.city-pavilion').hide();
                if($('.city-list').css("display")=="block"){
                    $('.city-list').hide();
                }
                $("#searchTxtBtn").show();
                $(".input-search").val("");
            })
        }
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
                <ul class="mod_category_item ui-sortable no_leaf_item" id="no_leaf_item">

                </ul>
                <div class="openBtn" style="display: none;">
                    <a class="openBtn-txt" href="javascript:;" j-delegate="action"><em class="vm-inline">更多</em><i class="site-icons ico-explain-b"></i></a>
                </div>
            </div>
            <div class="BB" style="padding: 4px 0 4px 1px;height: 30px; display: none">
                <div class="CC" style="color: #999;font-size: 12px;POSITION: absolute;height: 30px;top:144px">
                    <span>品牌</span>：
                    <a id="brandAll" class="selected brand_item" >全部</a>
                    <span id="searchTxtBtn">搜索品牌</span>
                    <a id="brandSelected"  class="selected brand_item" style="display: none" ></a>
                </div>
                <div style="LEFT: 126PX;" class="city-select mod_category_item ui-sortable" id="single-select-1"></div>
            </div>
            <div id="leaf_category" class="ui-sortable">
                <!--三级栏目-->

            </div>
        </div>

      <div class="ad-wrapper clearfix">
          <div class="divide-green-h"></div>
      </div>
      <a name="piclist"></a>

      <div class="wrapper-piclist" style="    margin-left: -10px;">
          <ul class="site-piclist">
              <!--图片列表-->
          </ul>
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