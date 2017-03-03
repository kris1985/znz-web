<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title>类别列表</title>
    <%@ include file="../common/common.jsp" %>

    <link href="${basePath}/resources/css/jquery-ui-1.8.24.custom.css" rel="stylesheet"  />
    <link href="${basePath}/resources/css/jquery.contextMenu.css" rel="stylesheet">
    <link href="${basePath}/resources/css/aqy.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/artDialog.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.contextmenu.r2.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/iframeTools.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jqPaginator.js"></script>

    <script>
        var basePath = getContextPath();
        function openDialog(url) {
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
        }

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
            //栏目排序
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



            $('.noLeaf').contextMenu('myMenu1', {
                bindings: {
                    'add': function (t) {
                        var url = basePath + "/admin/subCategory/add";
                        var id = $("#" + t.id);

                        $("#categoryLevel").val(id.attr("categoryLevel"));
                        $("#parentId").val(id.attr("parentId"));
                        console.log($("#parentId").val()+"--i---"+$("#categoryLevel").val());
                        var sortId = id.parent().children().length + 1;
                        $("#sortId").val(sortId);
                        console.log( $("#dialog"));
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
                                        if(!isValid($("#categoryName").val())){
                                            return;
                                        }
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
                                        if(!isValid($("#categoryName").val())){
                                            return;
                                        }
                                        $("#categoryForm").attr("action", url);
                                        $("#categoryForm").submit();
                                    }
                                }]
                        });
                    },

                    'rename': function (t) {
                        var url = basePath + "/admin/subCategory/update";
                        var name = $.trim($("#" + t.id) .text());
                        $("#id").val(t.id);
                        $("#categoryName").val(name);
                        $("#dialog").dialog({
                            height: 150,
                            modal: true,
                            title:"重命名",
                            position: {my: "left top", at: "left bottom", of: "#" + t.id},
                            buttons: [
                                {
                                    text: "提交",
                                    icons: {
                                        primary: "ui-icon-heart"
                                    },
                                    click: function () {
                                        if(!isValid($("#categoryName").val())){
                                            return;
                                        }
                                        $("#categoryForm").attr("action", url);
                                        $("#categoryForm").submit();
                                    }
                                }]
                        });

                    }

                }

            });

            $('.leaf_item').contextMenu('myMenu2', {
                bindings: {
                    'add1': function (t) {
                        var url = basePath + "/admin/subCategory/add";
                        var id = $("#" + t.id);

                        $("#categoryLevel").val(id.attr("categoryLevel"));
                        $("#parentId").val(id.attr("parentId"));
                        console.log($("#parentId").val()+"--i---"+$("#categoryLevel").val());
                        var sortId = id.parent().children().length + 1;
                        $("#sortId").val(sortId);
                        console.log( $("#dialog"));
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
                                        if(!isValid($("#categoryName").val())){
                                            return;
                                        }
                                        $("#categoryForm").attr("action", url);
                                        $("#categoryForm").submit();
                                    }
                                }]
                        });
                    },
                    'rename1': function (t) {
                        var url = basePath + "/admin/subCategory/update";
                        var name = $.trim($("#" + t.id) .text());
                        $("#id").val(t.id);
                        $("#categoryName").val(name);
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
                                        if(!isValid($("#categoryName").val())){
                                            return;
                                        }
                                        $("#categoryForm").attr("action", url);
                                        $("#categoryForm").submit();
                                    }
                                }]
                        });

                    }

                }
            });

//叶子节点
            $(".leaf_item").click(function () {
                if ($(this).hasClass("selected")) {
                    $(this).removeClass("selected");
                    if($(this).parent().children().length  == $(this).parent().children().not(".selected").length){
                        $(this).parent().children().first().addClass("selected");//都没选中，全选选中
                    }
                } else {
                    $(this).addClass("selected");
                }
                    if($(this).attr("id").indexOf("all") == -1){
                        $(this).parent().children().first().removeClass("selected") //如果选择了非全选，全选去掉选中
                    }
                    var selected = getAllSelected();
                    //console.log("temp:"+temp)
                    var url = "${basePath}/admin/subCategory/showCategory"
                    $("#fourthSelectedId").val(selected);
                    $("#categoryForm").attr("action", url);
                    $("#categoryForm").submit();


            });

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
                console.log(temp);
                if(temp.indexOf(",") !=-1){
                    temp = temp.substring(0,temp.length-1);
                }
                //console.log(temp);
                var url = "${basePath}/admin/file/toUpload?category=" + temp
                art.dialog.open(url,
                    {
                        "id": "2345",
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
                $(".mod_sear_list").last().css("border-bottom","0")
           /**/ $(".all_item").each(function (i) {
               // console.log($(this).parent().children().length -1+ "----------------------" + $(this).parent().children().filter(".selected").length)
             //   console.log($(this).parent().children());
              //  console.log($(this).parent().children().filter(".selected"));
                if ($(this).parent().children().length  == $(this).parent().children().not(".selected").length) {
                    $(this).addClass("selected");//如果都没选中，全选 选中
                }
            })


            $.jqPaginator('#pagination1', {
                    totalPages: ${totalPage},
                    visiblePages: 10,
                   first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                              prev: '<li class="prev"><a href="javascript:void(0);"><i class="arrow arrow2"><\/i>上一页<\/a><\/li>',
                              next: '<li class="next"><a href="javascript:void(0);">下一页<i class="arrow arrow3"><\/i><\/a><\/li>',
                              last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                              page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',

                    currentPage: ${currentPage},
                    onPageChange: function (num, type) {
                            if(type!="init"){
                               var selected = getAllSelected();
                               var url = "${basePath}/admin/subCategory/showCategory"
                               $("#fourthSelectedId").val(selected);
                               $("#currentPage").val(num);
                               $("#categoryForm").attr("action", url);
                               $("#categoryForm").submit();
                            }
                    }
                });

        })


        function isValid(name){
            var valid = false;
            $.ajax({
                type: "POST",
                url: basePath + "/admin/subCategory/validName",
                data: "name=" + name,
                cache: false,
                async:false,
                success: function (ret) {
                    if(ret.code ==0){
                        valid = true;
                    }else{
                        alert(ret.msg);
                    }
                },
                error: function (msg) {
                    alert("服务器出错了");
                }
            });
            return valid;
        }

    </script>

</head>

<body>
<div class="header">
    <div class="nav_bar">
        <div class="logo">
            <a href="/"><img src="http://testznz.oss-cn-shanghai.aliyuncs.com/logo.png" width="118"> </a>
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
                            href="${bathPath}/admin/subCategory/showCategory?firstSelectedId=${item.id}">${item.name}</a>
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
            <input type="hidden" id="currentPage" name = "currentPage" value="1">
             <input type="hidden" id="pageSize" name = "pageSize" value="4">
        </form>
    </div>

    <div class="contextMenu" id="myMenu1">
        <ul>
            <li id="add"><img src="${basePath}/resources/img/logo2.JPG"/> 新增同类</li>
            <li id="addSub"><img src="${basePath}/resources/img/logo2.JPG"/>新增子类</li>
            <li id="rename"><img src="${basePath}/resources/img/logo2.JPG"/> 重命名</li>
        </ul>
    </div>

    <div class="contextMenu" id="myMenu2">
        <ul>
            <li id="add1"><img src="${basePath}/resources/img/logo2.JPG"/> 新增同类</li>
            <li id="rename1"><img src="${basePath}/resources/img/logo2.JPG"/> 重命名</li>
        </ul>
    </div>


    <div class="mod_sear_menu mt20 " style="margin-bottom: 20px;">
         <div class="mod_sear_list">
        <h3>选择：</h3>
        <ul class="mod_category_item">
            <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
                <c:if test="${item.parentId == firstSelectedId && secondSelectedId == item.id  }">
                    <li id="${item.id}" class="li_item selected noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                          href="${bathPath}/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>
                <c:if test="${item.parentId == firstSelectedId && secondSelectedId != item.id  }">
                    <li id="${item.id}" class="li_item noLeaf" categoryLevel="${item.categoryLevel}" parentId="${item.parentId}"><a
                            href="${bathPath}/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>

         <div id="leaf_category">
        <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <c:if test="${item.categoryLevel == 2 && item.parentId == secondSelectedId}">
                <div class="mod_sear_list">
                    <h3 id="${item.id}">${item.name}：</h3>
                    <ul class="mod_category_item ">
                        <li id="all_${item.id}" class="li_item leaf_item all_item " categoryLevel="${item.categoryLevel +1}" parentId="${item.id}"><a id="aa"
                                                                                               href="javascript:void()"
                                                                                               class="">全部</a></li>

                        <c:forEach var="category" items="${item.childrens}" varStatus="status">
                            <c:set var="iscontain" value="false" />
                            <c:forEach items="${fourthSet}" var="c">
                                <c:if test="${c == category.id}">
                                    <c:set var="iscontain" value="true" />
                                </c:if>
                            </c:forEach>

                            <c:if test="${iscontain}">
                                <li id="${category.id}"  class=" li_item leaf_item selected"
                                categoryLevel="${category.categoryLevel}" parentId="${category.parentId}"> <a id="a_${category.id}" href="javascript:void()"
                                      class="">${category.name}</a> </li>
                            </c:if>
                            <c:if test="${!iscontain}">
                                <li id="${category.id}"  class=" li_item leaf_item
                                "  categoryLevel="${category.categoryLevel}" parentId="${category.parentId}"> <a id="a_${category.id}" href="javascript:void()"
                                      class="">${category.name} </a> </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
        </c:forEach>
    </div>
    </div>


    <div>
        <input type="button" id="uploadBtn" value="上传"></button>
    </div>
    <div  class="ad-wrapper clearfix">
        <div class="divide-green-h"></div>
    </div>

    <div class="wrapper-piclist" style="    margin-left: -20px;">
        <ul class="site-piclist">
            <c:forEach var="item" items="${pictures}" varStatus="status">
                <li>
                    <div class="site-piclist_pic">
                        <a alt="${item.name}" title="${item.name}" href="#" class="site-piclist_pic_link" target="_blank">
                            <img alt="${item.name}" title="${item.name}"
                                 src="http://testznz.oss-cn-shanghai.aliyuncs.com/${item.filePath}?x-oss-process=image/resize,m_pad,h_199,w_280">
                        </a>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>


  <div class="mod-page">
    <ul class="pagination" id="pagination1"></ul>
  </div>
</div>


</body>

</html>