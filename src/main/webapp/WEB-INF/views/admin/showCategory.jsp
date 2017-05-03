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

    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/artDialog.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.contextmenu.r2.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/iframeTools.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jqPaginator.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.lazyload.min.js"></script>
    <script language="javascript" type="text/javascript" src="${basePath}/resources/js/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.cookie.js"></script>
    <script>
        var basePath = getContextPath();

        /*获取所有类别除掉全选，格式1,2,3;4,5,6  不同类别用；分开*/
        function getAllSelected() {
            var selected = "";
            $(".all_item").each(function (i) {
                //console.log("all_item:"+i)
                if(!$(this).hasClass("selected")){ //如果非全选，获取所有兄弟节点的id
                    $.each($(this).parent().children().filter(".selected"), function (i, n) {
                        id = $(this).attr("id");
                        selected += id + ","
                    });
                    if(selected!=""){
                        selected = selected.substring(0,selected.length-1)+";";
                    }
                }
            });
            //console.log("selected："+selected);
            return selected;
        }



        $(function () {
             <c:if test="${currentPage >1}">
              window.location.href = "#piclist";
            </c:if>
             <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0 }">
                //栏目排序 左右
                $(".mod_category_item").sortable({
                    update: function (event, ui) {
                        var array = $(this).children();
                        var data = "";
                        for (var i = 0; i < array.length; i++) {
                            if ("" == array[i].id || array[i].id.indexOf("all")!=-1) {
                                continue;
                            }
                            //console.log(array[i].id);
                            data += array[i].id + ":" + i + ";";
                        }
                        // console.log(data);
                        $.ajax({
                            type: "POST",
                            url: basePath + "/admin/subCategory/sort",
                            data: "param=" + data,
                            cache: false,
                            success: function (msg) {
                                //alert( "Data Saved: " + msg );
                            },
                            error: function (msg) {
                                alert("服务器出错了");
                            }
                        });
                    }
                });
                $(".mod_category_item").disableSelection();

            $( "#leaf_category" ).sortable({
                update: function (event, ui) {
                    var array = $(this).find("h3");
                    var data = "";
                    for (var i = 0; i < array.length; i++) {
                        if ("" == array[i].id ) {
                            continue;
                        }
                        //console.log(array[i].id);
                        data += array[i].id + ":" + i + ";";
                    }
                    //console.log(data);
                    $.ajax({
                        type: "POST",
                        url: basePath + "/admin/subCategory/sort",
                        data: "param=" + data,
                        cache: false,
                        success: function (msg) {
                            //alert( "Data Saved: " + msg );
                        },
                        error: function (msg) {
                            alert("服务器出错了");
                        }
                    });
                }
            });
            $( "#leaf_category" ).disableSelection();

                $('.noLeaf').contextMenu('myMenu1', {
                    bindings: {
                        'add': function (t) {
                            var url = basePath + "/admin/subCategory/add";
                            var id = $("#" + t.id);

                            $("#categoryLevel").val(id.attr("categoryLevel"));
                            $("#parentId").val(id.attr("parentId"));
                            //console.log($("#parentId").val()+"--i---"+$("#categoryLevel").val());
                            var sortId = id.parent().children().length + 1;
                            $("#sortId").val(sortId);
                            //console.log( $("#dialog"));
                            $("#dialog").dialog({
                                height: 150,
                                modal: true,
                                position: {my: "left top", at: "left bottom", of: "#" + t.id},
                                buttons: [
                                    {
                                        text: "提交",
                                        icons: {
                                            primary: "ui-icon-heart"
                                        },
                                        click: function () {
                                            $("#categoryForm").attr("action", url);
                                            $("#categoryForm").submit();
                                        }
                                    }]
                            });
                        },

                        'addSub': function (t) {
                            var url = basePath + "/admin/subCategory/add";
                            var id = $("#" + t.id);
                            var parentId = t.id;
                            var sortId = id.parent().children().length + 1;
                            $("#sortId").val(sortId);
                            $("#parentId").val(parentId);
                            $("#categoryLevel").val(parseInt(id.attr("categoryLevel"))+1);
                            $("#dialog").dialog({
                                height: 150,
                                modal: true,
                                title:"新增子类别",
                                position: {my: "left top", at: "left bottom", of: "#" + t.id},
                                buttons: [
                                    {
                                        text: "提交",
                                        icons: {
                                            primary: "ui-icon-heart"
                                        },
                                        click: function () {
                                            $("#categoryForm").attr("action", url);
                                            $("#categoryForm").submit();
                                        }
                                    }]
                            });
                        },

                        'rename': function (t) {
                            var url = basePath + "/admin/subCategory/update";
                            var id = t.id;
                            var oldName = $.trim($("#" + t.id) .text());
                            $("#categoryName").val(oldName);
                            $("#dialog").dialog({
                                height: 150,
                                modal: true,
                                title:"重命名q",
                                position: {my: "left top", at: "left bottom", of: "#" + t.id},
                                buttons: [
                                    {
                                        text: "提交",
                                        icons: {
                                            primary: "ui-icon-heart"
                                        },
                                        click: function () {
                                           var name =  $.trim($("#categoryName").val());
                                           if(oldName == name){
                                             return;
                                           }
                                            $.ajax({
                                                   type: "POST",
                                                   url: basePath + "/admin/subCategory/update",
                                                    data: { name: name, id: id ,oldName:oldName},
                                                   cache: false,
                                                   async: false,
                                                   success: function (ret) {
                                                       if (ret.code == 0) {
                                                           $("#" + t.id).find("a").text(name);
                                                           $("#dialog").dialog("close");
                                                       } else {
                                                           alert(ret.msg);
                                                       }
                                                   },
                                                   error: function (msg) {
                                                       alert("服务器出错了");
                                                   }
                                             });
                                        }
                                    }]
                            });

                        }
                        ,'delete1':function (t) {
                            var url = basePath + "/admin/subCategory/delete/"+t.id;
                            if(confirm("确认要删除吗？")){
                                $.get(url,function (data) {
                                    if(data.code!=0){
                                        alert(data.msg);
                                    }else{
                                        $("#"+t.id).hide(1000);
                                    }
                                });
                            }
                        }
                    }
                });

                $('.leaf_item').contextMenu('myMenu2', {
                    bindings: {
                        'add2': function (t) {
                            var url = basePath + "/admin/subCategory/add";
                            var id = $("#" + t.id);

                            $("#categoryLevel").val(id.attr("categoryLevel"));
                            $("#parentId").val(id.attr("parentId"));
                            // console.log($("#parentId").val()+"--i---"+$("#categoryLevel").val());
                            var sortId = id.parent().children().length + 1;
                            $("#sortId").val(sortId);
                            $("#dialog").dialog({
                                height: 150,
                                modal: true,
                                position: {my: "left top", at: "left bottom", of: "#" + t.id},
                                buttons: [
                                    {
                                        text: "提交",
                                        icons: {
                                            primary: "ui-icon-heart"
                                        },
                                        click: function () {
                                            $("#categoryForm").attr("action", url);
                                            $("#categoryForm").submit();
                                        }
                                    }]
                            });
                        },
                        'rename2': function (t) {
                            var oldName = $.trim($("#" + t.id) .text());
                            var id = t.id;
                            $("#categoryName").val(oldName);
                            $("#dialog").dialog({
                                height: 150,
                                title:"重命名21",
                                modal: true,
                                position: {my: "left top", at: "left bottom", of: "#" + t.id},
                                buttons: [
                                    {
                                        text: "提交",
                                        icons: {
                                            primary: "ui-icon-heart"
                                        },
                                        click: function () {
                                            var name =  $.trim($("#categoryName").val());
                                            if(oldName == name){
                                                return;
                                            }
                                            $.ajax({
                                                type: "POST",
                                                url: basePath + "/admin/subCategory/update",
                                                data: { name: name, id: id ,oldName:oldName},
                                                cache: false,
                                                async: false,
                                                success: function (ret) {
                                                    if (ret.code == 0) {
                                                        $("#" + t.id).find("a").text(name);
                                                        $("#dialog").dialog("close");
                                                    } else {
                                                        alert(ret.msg);
                                                    }
                                                },
                                                error: function (msg) {
                                                    alert("服务器出错了");
                                                }
                                            });
                                        }
                                    }]
                            });

                        }
                        ,'delete2':function (t) {
                            var url = basePath + "/admin/subCategory/delete/"+t.id;
                            if(confirm("确认要删除吗？")){
                                $.get(url,function (data) {
                                    if(data.code!=0){
                                        alert(data.msg);
                                    }else{
                                        $("#"+t.id).hide(1000);
                                    }
                                });
                            }
                        }
                    }
                });

            $('.choice_item').contextMenu('myMenu3', {
                bindings: {
                    'rename3': function (t) {
                        var oldName = $.trim($("#" + t.id).find("span") .text());
                        var id = t.id;
                        $("#categoryName").val(oldName);
                        $("#dialog").dialog({
                            height: 150,
                            title:"重命名21",
                            modal: true,
                            position: {my: "left top", at: "left bottom", of: "#" + t.id},
                            buttons: [
                                {
                                    text: "提交",
                                    icons: {
                                        primary: "ui-icon-heart"
                                    },
                                    click: function () {
                                        var name =  $.trim($("#categoryName").val());
                                        if(oldName == name){
                                            return;
                                        }
                                        $.ajax({
                                            type: "POST",
                                            url: basePath + "/admin/subCategory/update",
                                            data: { name: name, id: id ,oldName:oldName},
                                            cache: false,
                                            async: false,
                                            success: function (ret) {
                                                if (ret.code == 0) {
                                                    $("#" + t.id).find("span").text(name);
                                                    $("#dialog").dialog("close");
                                                } else {
                                                    alert(ret.msg);
                                                }
                                            },
                                            error: function (msg) {
                                                alert("服务器出错了");
                                            }
                                        });
                                    }
                                }]
                        });
                    }
                    ,'delete3':function (t) {
                        var url = basePath + "/admin/subCategory/delete/"+t.id;
                        if(confirm("确认要删除吗？")){
                            $.get(url,function (data) {
                                if(data.code!=0){
                                    alert(data.msg);
                                }else{
                                    $("#"+t.id).hide(1000);
                                }
                            });
                        }
                    }
                }
            });



                //用户管理
                $("#userManagerBtn").click(function () {
                    var url = "${basePath}/admin/user/users";
                    art.dialog.open(url,
                        {
                            "id": "2347",
                            title: "上传文件",
                            width: 800,
                            height: 800
                        }
                    );
                })


            </c:if>

            <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">

            //上传图片
            $("#uploadBtn").click(function () {
                var temp = "";
                $("#leaf_category").find(".selected").each(function (i) {
                    id = $(this).attr("id");
                    if (id.indexOf("all") != -1) {
                        $.each($(this).siblings(), function (i, n) {
                            id = $(this).attr("id");
                            temp += id + ","
                        });
                    } else {
                        temp += id + ","
                    }
                });
                if(temp.indexOf(",") !=-1){
                    temp = temp.substring(0,temp.length-1);
                }
                var url = "${basePath}/admin/file/toUpload?category=" + temp
                art.dialog.open(url,
                    {
                        title: "上传文件",
                        width: 500,
                        height: 400,
                        close: function () {
                            var selected = getAllSelected();
                            var path = "${basePath}/admin/subCategory/showCategory"
                            $("#fourthSelectedId").val(selected);
                            $("#categoryForm").attr("action", path);
                            $("#categoryForm").submit();
                        }
                    }
                );
            })
            //上传附图 删除
            $('.site-piclist li').contextMenu('menuPic', {
                bindings: {
                    'addAttach': function (t) {
                        var id = $(t).attr("item");
                        var url = "${basePath}/admin/file/toUploadAttach?id="+id;
                        art.dialog.open(url,
                            {
                                "id": "2345",
                                title: "上传文件",
                                width: 500,
                                height: 400,
                                close: function () {

                                }
                            }
                        );
                    },
                    'delete': function (t) {
                        if(!confirm("确认要删除吗？")){
                            return;
                        }
                        var id = $(t).attr("item");
                        var url = "${basePath}/admin/file/delete?pictureId="+id;
                        $.get(url,function (data) {
                            if(data.code ==0 ){
                                $(t).hide();
                            }else{
                                alert(data.msg)
                            }
                        })
                    }
                    <c:if test="${userSession.user.recommendFlag == 1}">
                    ,
                    'rec': function (t) {
                        var id = $(t).attr("item");
                        var url = "${basePath}/admin/subCategory/recommend/"+id;
                        if($(t).find(".my_rec").css("display") =="block"){
                            alert("您已推荐过该图片");
                            return;
                        }
                        $.get(url,function (data) {
                            if(data.code ==0 ){
                                $(t).find(".my_rec").show();
                            }else{
                                alert(data.msg)
                            }
                        })
                    },
                    'cancleRec': function (t) {
                        var id = $(t).attr("item");
                        var url = "${basePath}/admin/subCategory/cancelRecommend/"+id;
                        if($(t).find(".my_rec").css("display") =="none"){
                            alert("您还没推荐过该图片");
                            return;
                        }
                        $.get(url,function (data) {
                            if(data.code ==0 || data.code ==1){
                                $(t).find(".my_rec").hide();
                            } else{
                                alert(data.msg)
                            }
                        })
                    }
                    </c:if>
                }
            });
            </c:if>
            <c:if test="${userSession.user.userType ==1 and userSession.user.recommendFlag == 1}">
                 $('.site-piclist li').contextMenu('menuPic', {
                    bindings: {
                        'rec': function (t) {
                            var id = $(t).attr("item");
                            var url = "${basePath}/admin/subCategory/recommend/"+id;
                            if($(t).find(".my_rec").css("display") =="block"){
                                alert("您已推荐过该图片");
                                return;
                            }
                            $.get(url,function (data) {
                                if(data.code ==0 ||data.code ==1){
                                    $(t).find(".my_rec").show();
                                }else{
                                    alert(data.msg)
                                }
                            })
                        },
                        'cancleRec': function (t) {
                            var id = $(t).attr("item");
                            var url = "${basePath}/admin/subCategory/cancelRecommend/"+id;
                            if($(t).find(".my_rec").css("display") =="none"){
                                alert("您还没推荐过该图片");
                                return;
                            }
                            $.get(url,function (data) {
                                if(data.code ==0 || data.code ==1){
                                    $(t).find(".my_rec").hide();
                                } else{
                                    alert(data.msg)
                                }
                            })
                        }
                    }
                 });
            </c:if>
            //图片懒加载
            $("img.lazy").lazyload({
                threshold : 200
            });

            //叶子节点,点击类别
            $(".leaf_item").click(function () {
                <c:choose>
                    <c:when test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">
                        if ($(this).hasClass("selected")) {
                            $(this).removeClass("selected");
                            if($(this).parent().children().length  == $(this).parent().children().not(".selected").length){
                                $(this).parent().children().first().addClass("selected");//都没选中，全选选中
                            }
                        } else {
                            $(this).addClass("selected");
                        }
                    </c:when>
                    <c:otherwise>
                         if ($(this).hasClass("selected")) {
                             return;
                         }else {
                             $(this).addClass("selected");
                             $(this).siblings().removeClass("selected");
                         }
                     </c:otherwise>
                </c:choose>


                if($(this).attr("id").indexOf("all") == -1){
                    $(this).parent().children().first().removeClass("selected") //如果选择了非全选，全选去掉选中
                }
                var selected = getAllSelected();
                //console.log("temp:"+temp)
                var url = "${basePath}/admin/subCategory/showCategory";
                $("#fourthSelectedId").val(selected);
                url=url+"?firstSelectedId=${firstSelectedId}&secondSelectedId=${secondSelectedId}&fourthSelectedId="+selected+"&currentPage=1&pageSize="+$("#pageSize").val();
                window.location.href= url;
            });

            //查询按钮
            $("#queryBtn").click(function () {
                var startTime = $.trim($("#startTime").val());
                var endTime = $.trim($("#endTime").val());
                if(startTime=="" && endTime=="" ){
                    alert("请输入时间");
                    return;
                }
                var selected = getAllSelected();
                var url = "${basePath}/admin/subCategory/showCategory";
                $("#fourthSelectedId").val(selected);
                url=url+"?firstSelectedId=${firstSelectedId}&secondSelectedId=${secondSelectedId}&fourthSelectedId="+selected+"&currentPage=1&pageSize="+$("#pageSize").val();
                url+="&startTime="+startTime+"&endTime="+endTime;
                window.location.href= url;
            });
            //删除
            $("#delBtn").click(function () {
                var startTime = $.trim($("#startTime").val());
                var endTime = $.trim($("#endTime").val());
                if(startTime=="" && endTime=="" ){
                    alert("请输入时间");
                    return;
                }
                var selected = getAllSelected();
                var url = "${basePath}/admin/subCategory/showCategory";
                $("#fourthSelectedId").val(selected);
                url=url+"?firstSelectedId=${firstSelectedId}&secondSelectedId=${secondSelectedId}&fourthSelectedId="+selected+"&currentPage=1&pageSize="+$("#pageSize").val();
                url+="&startTime="+startTime+"&endTime="+endTime;
                url+="&delFlag=true"
                window.location.href= url;
            })

            $(".mod_sear_list").last().css("border-bottom","0")
            $(".all_item").each(function (i) {
                if ($(this).parent().children().length  == $(this).parent().children().not(".selected").length) {
                    $(this).addClass("selected");//如果都没选中，全选 选中
                }
            })

            //分页
            $.jqPaginator('#pagination1', {
                <c:choose>
                    <c:when test="${totalPage ==0}">
                        totalPages: 1,
                    </c:when>
                    <c:otherwise>
                         totalPages: ${totalPage},
                    </c:otherwise>
                </c:choose>
                  visiblePages: 10,
                  first: '<li class="first"><a href="javascript:void(0);" style="width:40px">首页<\/a><\/li>',
                  prev: '<li class="prev"><a href="javascript:void(0);" style="width:58px"><i class="arrow arrow2"><\/i>上一页<\/a><\/li>',
                  next: '<li class="next"><a href="javascript:void(0);" style="width:58px">下一页<i class="arrow arrow3"><\/i><\/a><\/li>',
                  last: '<li class="last"><a href="javascript:void(0);" style="width:40px">末页<\/a><\/li>',
                  page: '<li class="page"><a href="javascript:void(0);" style="width:40px">{{page}}<\/a><\/li>',
                  currentPage: ${currentPage},

                    onPageChange: function (num, type) {
                            if(type!="init"){
                               var selected = getAllSelected();
                               var url = "${basePath}/admin/subCategory/showCategory"
                               $("#fourthSelectedId").val(selected);
                               $("#currentPage").val(num);
                                $("#categoryForm").attr("target", "_self");
                               $("#categoryForm").attr("action", url);
                               $("#categoryForm").submit();
                            }
                    }
                });
            //共多少页面
            $("#pagination1").append('<li class="next"  ><a href="javascript:void(0);" style="width:68px">共${totalPage}页</a></li><li class="next"  ><a href="javascript:void(0);" style="width:88px">共${totalCount}张</a></li>')

            //点击图片看大图
           $(".site-piclist_pic a").click(function () {
               var selectedImg = $(this).attr("id");
               var ids="";
               $(".site-piclist_pic a").each(function (i) {
                   ids=ids+$(this).attr("id")+",";
               });
               $("#selectedId").val(selectedImg);
               $("#picIds").val(ids);
               var selected = getAllSelected();
               $("#fourthSelectedId").val(selected);
               var url = "${basePath}/admin/file/listImg"
               $("#categoryForm").attr("action",url);
               $("#categoryForm").attr("target","_blank");
               $("#categoryForm").submit();
           })


           $(".openBtn").click(function(){
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

            $(".mod_sear_list").each(function (i) {
                var height = $.cookie($(this).attr("id"));
                if(height!=undefined){
                    $(this).css("height",height);
                    if(height!="auto"){
                        $(this).find("em").text("更多");
                        $(this).find("i").css("background-position", "-19px -10px")
                    }else{
                        $(this).find("em").text("收起");
                        $(this).find("i").css("background-position", "0 -10px")
                    }
                }
                if($(this).find("a").text().length>60){
                    $(this).find(".openBtn").show();
                }
            })

           $(".site-piclist_pic").hover(
               function () {
               $(this).css("border","2px solid #699f00");
           },function () {
                   $(this).css("border","2px solid white");
           })

            //推荐
            $("#recommend_list li").click(function () {
                var selected = getAllSelected();
                var recommendId =  $(this).attr("id");
                if(recommendId.indexOf("all")!=-1){
                    recommendId = "";
                }else{
                    recommendId = recommendId.replace("rec_","");
                }
                $("#fourthSelectedId").val(selected);
                $("#recommendId").val(recommendId);
                var url = "${basePath}/admin/subCategory/showCategory"
                $("#categoryForm").attr("action",url);
                $("#categoryForm").attr("target","_self");
                $("#categoryForm").submit();
            })
        })



    </script>

</head>

<body>
<div class="header">
    <div class="nav_bar">
        <div class="logo">
            <a href="/"><img src="${basePath}/resources/img/logo.png" height="38"> </a>
        </div>
        <div class="nav_warp">
            <ul class="navbar-ul mod_category_item">


            <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
                <c:if test="${item.categoryLevel == 0 && firstSelectedId == item.id  }">
                    <li id="${item.id}" class="li_item selected noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                            href="${bathPath}/admin/subCategory/showCategory?firstSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>
                <c:if test="${item.categoryLevel == 0 && firstSelectedId != item.id  }">
                    <li id="${item.id}" class="li_item noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                            href="${basePath}/admin/subCategory/showCategory?firstSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>
            </c:forEach>
            </ul>
        </div>
    </div>
</div>

<div id="container" class="site-main">
    <div  class="ad-wrapper clearfix">
        <div class="divide-green-h"></div>
    </div>
    <div id="dialog" title="新增类别" style="display:none">
        <form id="categoryForm" method="POST">
            类别名称：<input type="text" id="categoryName" name="name">
            <input type="hidden" id="sortId" name="sortId">
            <input type="hidden" id="id" name="id">
            <input type="hidden" id="categoryLevel" name="categoryLevel">
            <input type="hidden" id="parentId" name="parentId">
            <input type="hidden" id="firstSelectedId" name="firstSelectedId" value="${firstSelectedId}">
            <input type="hidden" id="secondSelectedId" name="secondSelectedId" value="${secondSelectedId}">
            <input type="hidden" id="thirdSelectedId" name="thirdSelectedId" value="${thirdSelectedId}">
            <input type="hidden" id="fourthSelectedId" name="fourthSelectedId" value="${fourthSelectedId}">
            <input type="hidden" id="currentPage" name = "currentPage" value="${currentPage}">
            <input type="hidden" id="pageSize" name = "pageSize" value="80">
            <input type="hidden" id="totalPage" name = "totalPage" value="${totalPage}">
            <input type="hidden" id="totalCount" name="totalCount" value="${totalCount}">
            <input id="startTime1" name="startTime" type="hidden" />
            <input id="endTime1"    name="endTime" type="hidden"/>
            <!--点击大图选择的图片id-->
            <input type="hidden" name="selectedId" id="selectedId">
            <!--当前页所有图片id-->
            <input type="hidden" name="ids"id="picIds">
            <input type="hidden" name="recommendId" id="recommendId" value="${recommendId}">
        </form>
    </div>

    <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0}">
        <div class="contextMenu" id="myMenu1" style="display: none">
            <ul>
                <li id="add"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154104&size=16" width="16"/> 新增同类</li>
                <li id="addSub"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154118&size=16"  width="16"/> 新增子类</li>
                <li id="rename"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"  width="16"/> 重命名</li>
                <li id="delete1"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 删除</li>
            </ul>
        </div>

        <div class="contextMenu" id="myMenu2" style="display: none">
            <ul>
                <li id="add2"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154104&size=16"/> 新增同类</li>
                <li id="rename2"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 重命名</li>
                <li id="delete2"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 删除</li>
            </ul>
        </div>

        <div class="contextMenu" id="myMenu3" style="display: none">
            <ul>
                <li id="rename3"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 重命名</li>
                <li id="delete3"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 删除</li>
            </ul>
        </div>
  </c:if>

    <c:choose>
        <c:when test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">
            <div class="contextMenu" id="menuPic" style="display: none">
                <ul>
                    <li id="addAttach"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154104&size=16"/> 上传附图</li>
                    <li id="delete"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 删除</li>
                    <c:if test="${userSession.user.recommendFlag == 1}">
                        <li id="rec"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 推荐</li>
                        <li id="cancleRec"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 取消推荐</li>
                    </c:if>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <div class="contextMenu" id="menuPic" style="display: none">
                <ul>
                    <c:if test="${userSession.user.recommendFlag == 1}">
                        <li id="rec"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 推荐</li>
                        <li id="cancleRec"><img src="http://www.easyicon.net/api/resizeApi.php?id=1154126&size=16"/> 取消推荐</li>
                    </c:if>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>



    <div class="mod_sear_menu mt20 " style="margin-bottom: 20px;">
         <div class="mod_sear_list" id="mod_${firstSelectedId}">
        <h3>目录：</h3>
        <ul class="mod_category_item">
            <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
                <c:if test="${item.parentId == firstSelectedId && secondSelectedId == item.id  }">
                    <li id="${item.id}" class="li_item selected noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                          href="${basePath}/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>
                <c:if test="${item.parentId == firstSelectedId && secondSelectedId != item.id  }">
                    <li id="${item.id}" class="li_item noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                            href="${basePath}/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>

            </c:forEach>
        </ul>
             <div class="openBtn">
                 <a class="openBtn-txt" href="javascript:;" j-delegate="action"><em class="vm-inline">更多</em><i class="site-icons ico-explain-b"></i></a>
             </div>
    </div>

         <div id="leaf_category">
        <c:forEach var="item" items="${subCategoryVOs}" varStatus="status" >
            <c:if test="${item.categoryLevel == 2 && item.parentId == secondSelectedId}">
                <div class="mod_sear_list" id="${item.id}">
                    <h3 id="${item.id}" class="choice_item"><span>${item.name}</span>：</h3>
                    <ul class="mod_category_item ">
                        <li id="all_${item.id}" class="li_item leaf_item all_item " categoryLevel="${item.categoryLevel +1}" parentId="${item.id}"><a id="aa"
                                                                                               href="javascript:void()"
                                                                                               class="">全部</a></li>

                        <c:forEach var="category" items="${item.childrens}" varStatus="status2">
                            <c:set var="iscontain" value="false" />
                            <c:forEach items="${fourthSet}" var="c">
                                <c:if test="${c == category.id}">
                                    <c:set var="iscontain" value="true" />
                                </c:if>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${iscontain}">
                                    <li id="${category.id}"  class=" li_item leaf_item selected"
                                        categoryLevel="${category.categoryLevel}" parentId="${category.parentId}"> <a id="a_${category.id}" href="javascript:void()"
                                                                                                                      class="">${category.name}</a> </li>
                                </c:when>
                                <c:otherwise>
                                    <li id="${category.id}"  class=" li_item leaf_item
                                "  categoryLevel="${category.categoryLevel}" parentId="${category.parentId}"> <a id="a_${category.id}" href="javascript:void()"
                                                                                                                 class="">${category.name} </a> </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                    <div class="openBtn">
                            <a class="openBtn-txt" href="javascript:;" j-delegate="action"><em class="vm-inline">更多</em><i class="site-icons ico-explain-b"></i></a>
                     </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
        <!--推荐-->
        <c:if test="${not empty users}">
            <div class="mod_sear_list" id="recommend_list">
                <h3 id="recommend_h3"><span>推荐</span>：</h3>
                <ul class="mod_category_item  ui-sortable">
                    <c:choose>
                        <c:when test="${not empty recommendId}">
                            <li id="recommend_all" class="li_item"><a href="javascript:;" class="">全部</a></li>
                        </c:when>
                        <c:otherwise>
                            <li id="recommend_all" class="li_item selected"><a href="javascript:;" class="">全部</a></li>
                        </c:otherwise>
                    </c:choose>

                    <c:forEach var="user" items="${users}">
                        <c:choose>
                            <c:when test="${user.userId eq recommendId}">
                                <li id="rec_${user.userId}" class="li_item selected" ><a href="javascript:;">${user.company}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li id="rec_${user.userId}" class="li_item" ><a href="javascript:;">${user.company}</a></li>
                            </c:otherwise>

                        </c:choose>
                    </c:forEach>
                </ul>
                <div class="openBtn">
                    <a class="openBtn-txt" href="javascript:;"><em class="vm-inline">更多</em><i class="site-icons ico-explain-b"></i></a>
                </div>
            </div>
        </c:if>
        <!--推荐-->
    </div>


    <div class="operation" style="position: relative">
        <c:choose>
            <c:when test="${userSession.user.userType ==2 or userSession.user.userType ==0 }">
                <input type="button" id="userManagerBtn" class="ui-state-default ui-corner-all ui-button" value="用户管理"></button>
                <input type="button" id="uploadBtn" class="ui-state-default ui-corner-all ui-button" value="上传"></button>
                <div style="position: absolute;right: 5px;top: 0;">
                    时间  从<input id="startTime" name="startTime" type="text" value="${startTime}" onClick="WdatePicker({startDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"/>
                    至<input id="endTime"    name="endTime" type="text" value="${endTime}" onClick="WdatePicker({startDate:'%y-%M-01 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"/>
                    <input type="button" id="queryBtn" class="ui-state-default ui-corner-all ui-button" value="查询"></button>
                    <input type="button" id="delBtn" class="ui-state-default ui-corner-all ui-button" value="删除"></button>
                </div>
            </c:when>
            <c:when test="${userSession.user.userType ==3}">
                <input type="button" id="uploadBtn" class="ui-state-default ui-corner-all ui-button" value="上传"></button>
                <div  style="position: absolute;right: 5px;top: 0;">
                    时间  从<input id="startTime" name="startTime" type="text" value="${startTime}" onClick="WdatePicker({startDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"/>
                    至<input id="endTime"    name="endTime" type="text" value="${endTime}" onClick="WdatePicker({startDate:'%y-%M-01 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"/>
                    <input type="button" id="queryBtn" class="ui-state-default ui-corner-all ui-button" value="查询"></button>
                    <input type="button" id="delBtn" class="ui-state-default ui-corner-all ui-button" value="删除"></button>
                </div>
            </c:when>
        </c:choose>
    </div>

    <div  class="ad-wrapper clearfix">
        <div class="divide-green-h"></div>
    </div>
     <a name="piclist"></a>
    <div class="wrapper-piclist" style="    margin-left: -20px;">
        <ul class="site-piclist">

            <c:forEach var="item" items="${pictures}" varStatus="status">
                <c:set var="myRec" value="none" />
                <c:if test="${fn:contains(item.recId,userSession.user.userId)}">
                    <c:set var="myRec" value="block" />
                </c:if>

                <c:set var="recDisplay" value="none" />
                <c:if test="${not empty item.recId}">
                    <c:set var="recDisplay" value="block" />
                </c:if>
                <li item="${item.id}">
                    <div class="site-piclist_pic">
                        <div class="my_rec" style="display: ${myRec}">我推荐的</div>
                        <a id="${item.id}" path="${item.filePath}"  title="${item.name}"
                           href="javascript:void(0)" class="site-piclist_pic_link" attach="${item.attach}">
                            <img class="lazy" alt="${item.name}" title="${item.name}" style="border: 0"
                                 src="${basePath}/resources/img/grey.gif" width="280" height="199"   data-original="${ossPath}/${item.filePath}?x-oss-process=image/resize,m_pad,h_199,w_280${watermarkParam}">
                        </a>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

  <div class="mod-page">
    <ul class="pagination" id="pagination1"></ul>
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

</body>

</html>