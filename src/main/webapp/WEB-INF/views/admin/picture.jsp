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

    </style>
    <script>
        var basePath = getContextPath();
        var  nav ;
        var pics ;
        var gtotalPage =0;
        var gtotalCount =0;
        var gpageSize = 0;
        $(function(){
            $("img.lazy").lazyload({
                threshold : 800
            }).removeClass("lazy");
            $(document).ajaxStop(function(){
                $("img.lazy").lazyload({
                    threshold : 800
                }).removeClass("lazy");
            });

            $.ajax({
                type:'post',
                url:basePath+"/categorys",
                contentType :'application/json;charset=utf-8',
                data:'{"token":"d745de2ca149ac5460262bcfb4528a18"}',
                success:function(data){
                    if(!data.success){
                        if(data.errorCode=="1008"){
                            alert(data.errorMsg);
                            window.location.href="/";
                        }else if(data.errorMsg!=null &&data.errorMsg!=""){
                            alert(data.errorMsg);
                            return;
                        }
                    }
                    nav = data.result;
                //循环获取数据
                    var res = "";
                    $.each(nav, function(i, item) {
                        var id = item.id;
                        var name = item.name;
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
                    showPic(1);
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
        });

        //点击栏目
        $(".li_item").live("click",function(evt){
                if ($(this).hasClass("selected")) {
                    return;
                }
                $("#cpage").attr("data-value","1");//每次点击后页码从第一页开始
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
                showPic(1);
                if( $(this).parent().children().length>15){
                    //location.hash="piclist"; //解决栏目太多看不到图片切换
                }
        });

        //品牌搜索
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
                    },
                    onTabsAfter: function (target) {
                    },
                    onCallerAfter: function (target, values) {
                        showPic(1);
                        var brandName = values.name;
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
        //品牌全部
        $("#brandAll").click(function () {
            $(".city-info span").attr("data-id","");
            $(".city-info span").text("")
           // $("#single-select-1").hide()
            showPic(1);
            $(this).addClass("selected");
            $("#brandSelected").hide();
        });

        //点击图片看大图
         $(".site-piclist_pic a").live("click",function(evt){
            var selectedImg = $(this).attr("id");
            var ids="";
            $(".site-piclist_pic a").each(function (i) {
                ids=ids+$(this).attr("id")+",";
            });
            $("#selectedId").val(selectedImg);
            $("#picIds").val(ids);
            var selected = getAllSelected();
            $("#fourthSelectedId").val(selected);
            var secondSelectedId = parseInt($("#no_leaf_item").find(".selected").attr("id"));
            $("#secondSelectedId").val(secondSelectedId);
            var currentPage = $("#cpage").attr("data-value");
             $("#currentPage").val(currentPage);
            var url = "${basePath}/admin/file/listImg";
            $("#categoryForm").attr("action",url);
            $("#categoryForm").attr("target","_blank");
            $("#categoryForm").submit();
        });


        $('.site-piclist_pic').live('hover',function(event){
            if(event.type=='mouseenter'){
                $(this).css("border","2px solid #699f00");
            }else{
                $(this).css("border","2px solid white");
            }
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
            var brandId = $(".city-info span").attr("data-id");
            if(brandId!=null &&brandId!=undefined && brandId!="" ){
                if(selected == ""){
                    selected = brandId;
                }else{
                    selected = selected+","+brandId;
                }
            }
            return selected;
        }

        function  showPic(currentPage) {
           if(currentPage == null || currentPage==""){
               currentPage = 1;
           }
           var selected = getAllSelected();
           var secondCategoryId = $("#no_leaf_item").find(".selected").attr("id");
            $.ajax({
                type:'post',
                url:basePath+"/pictures",
                contentType :'application/json;charset=utf-8',
                data:'{"data":{\n' +
                    '\t"categoryIds":\"'+selected+'\",\n"secondCategoryId":\"' +secondCategoryId+"\""+
                    ' ,   "currentPage":'+currentPage+'\n,' +
                    '    "pageSize" :120\n' +
                    '\t}}',
                success:function(data){
                    var res = "";
                    pics = data;
                    console.log("data:"+data);
                    if(data.result==null){
                        $(".site-piclist").empty();
                        return;
                    }
                    if(!data.success){
                        if(data.errorCode=="1008"){
                            alert(data.errorMsg);
                            window.location.href="/";
                        }else if(data.errorMsg!=null &&data.errorMsg!=""){
                            alert(data.errorMsg);
                            return;
                        }
                    }
                    $(".site-piclist").empty();
                    //$('html,body').animate({scrollTop:$('#piclist').offset().top}, 0);
                    var totalPage =  data.result.totalPage;
                    $("#totalPage").val( data.result.totalPage);//用于查看大图时传递参数
                    $("#totalCount").val( data.result.totalCount);
                    $("#pageSize").val(data.result.pageSize);
                    var picType = data.result.pictureProperty.picType;
                    var liStyle="";
                    if(picType ==1){
                        liStyle ="margin-bottom: 35px; height: 320px;"
                    }
                    $.each(data.result.pictures, function(i, item) {
                        var id = item.readId;
                        var name = item.name;
                        var filePath= item.filePath;
                        res +='<li item=""'+id+' id="pic_itme_'+id+'" style="' +liStyle+ '">' +
                                '<div class="site-piclist_pic" style="border: 2px solid white;">' +
                                    ' <a id="'+id+'" path="" title="" href="javascript:void(0)" class="site-piclist_pic_link" attach="">' +
                                        '<img class="lazy"  title="'+name+'" style="border: 0px; display: inline;" src="/resources/img/grey.gif" width="384" height="288" ' +
                                        'data-original="${ossPath}/'+filePath+'?x-oss-process=image/resize,m_pad,h_288,w_384'+data.result.pictureProperty.waterMark+'">'
                        +' </a>';
                        if(picType ==1){
                            //书封面增加文字说明
                            res+='<div class="picTxt">'+name.split("_")[1]+'</div>';
                            res+=' <div class="picTxt" style="padding: 3px 3px 3px 5px;" gid="'+id+'">'+name.split("_")[0]+'</div>';
                        }
                        res+='</div></li>';
                    });

                    $(".site-piclist").html(res);
                   /* var cpage =  $("#cpage").attr("data-value");
                    if(cpage==undefined || cpage==null || cpage=="" ){
                        cpage = 1;
                    }*/
                    //分页
                    var  currentPage = parseInt($("#cpage").attr("data-value"));
                    var if_firstime = true;
                    $('#pagination1').jqPaginator({
                        //pageCount为查询结果数量经过计算后的总页数
                        totalPages: totalPage,
                        visiblePages: 10,
                        currentPage: currentPage,
                        prev: '<li class="prev"><a href="javascript:void(0);" style="width: 58px">上一页</a></li>',
                        next: '<li class="next"><a href="javascript:void(0);" style="width: 58px">下一页</a></li>',
                        page: '<li class="page"><a href="javascript:void(0);" >{{page}}</a></li>',
                        onPageChange: function (currentPage) {
                            if(if_firstime){
                                if_firstime = false;
                                return;
                            }else  {
                                $("#cpage").attr("data-value",currentPage);
                                showPic(currentPage);
                                if_firstime = true;
                            }
                           // $('html,body').animate({scrollTop:'0'},500);
                            $('html,body').animate({scrollTop:$('#piclist').offset().top}, 0);
                           // <li class="next"><a href="javascript:void(0);">共{{totalPages}}页</a> </li>
                        }
                    });
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
      <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==3  or userSession.user.userType ==0}">
          <div class="operation">
              <input type="button" id="userManagerBtn"
              class="ui-state-default ui-corner-all ui-button" value="跳转到老版本"
                     onclick="javascript:window.open('/admin/subCategory/showCategory','_blank') ">
           </div>
      </c:if>

      <div class="ad-wrapper clearfix">
          <div class="divide-green-h"></div>
      </div>
      <a name="piclist" id="piclist"></a>

      <div class="wrapper-piclist" style="    margin-left: -10px;">
          <ul class="site-piclist">
              <!--图片列表-->
          </ul>
      </div>
        <div  class="ad-wrapper clearfix">
            <div class="divide-green-h"></div>
        </div>

 <%--     <div class="mod-page" style="position: sticky;padding: 0;top:0;z-index: 999">
          <ul class="pagination" id="pagination1" style="margin: 0"></ul>
      </div>--%>
      <div class="mod-page" >
          <ul class="pagination" id="pagination1"></ul>
      </div>
      <div style="display: none" id ="cpage"  data-value="1"></div>
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

    <form id="categoryForm" method="POST">
        <input type="hidden" id="secondSelectedId" name="secondSelectedId" value="">
        <input type="hidden" id="fourthSelectedId" name="fourthSelectedId" value="">
        <input type="hidden" id="currentPage" name = "currentPage" value="">
        <input type="hidden" id="pageSize" name = "pageSize" value="120">
        <input type="hidden" id="totalPage" name = "totalPage" value="">
        <input type="hidden" id="totalCount" name="totalCount" value="">
        <!--点击大图选择的图片id-->
        <input type="hidden" name="selectedId" id="selectedId">
        <!--当前页所有图片id-->
        <input type="hidden" name="ids"id="picIds">
    </form>
<script type="text/javascript" src="${basePath}/resources/js/top.js"></script>
</body>

</html>