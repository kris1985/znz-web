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
        .brand_ui{    position: absolute;
            position: absolute;
            left: 590px;
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
        $(function(){

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
                prev: '<li class="prev"><a href="javascript:void(0);" style="width:58px"><i class="arrow arrow2"><\/i>上一页<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);" style="width:58px">下一页<i class="arrow arrow3"><\/i><\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);" style="width:40px">{{page}}<\/a><\/li>',
                currentPage: ${currentPage},

                onPageChange: function (num, type) {
                    if(type!="init"){
                        var url = "${basePath}/admin/file/listImg"
                        $("#currentPage").val(num);
                        $("#categoryForm").attr("target", "_self");
                        $("#categoryForm").attr("action", url);
                        $("#categoryForm").submit();
                    }
                }
            });
           // $("#pagination1").append('<li class="next"  ><a href="javascript:void(0);" style="width:68px">共${totalPage}页</a></li><li class="next"  ><a href="javascript:void(0);" style="width:88px">共${totalCount}张</a></li>')
            $("#switchToPage").val('${currentPage}');
            /* $("#switchToPageBtn").live("click",function(evt){
                var currentPage = $.trim($("#switchToPage").val());
                console.log(currentPage);
                if(isNaN(currentPage)){
                    alert("请输入数字")
                    return;
                }else{
                    if(parseInt(currentPage)==0){
                        currentPage = 1;
                    }else if(parseInt(currentPage)>${totalPage}){
                        currentPage = "${totalPage}";
                    }
                    var url = "${basePath}/admin/file/listImg"
                    $("#currentPage").val(currentPage);
                    $("#categoryForm").attr("target", "_self");
                    $("#categoryForm").attr("action", url);
                    $("#categoryForm").submit();
                }
            });*/

            $(".site-piclist_pic a").click(function () {
                var selectedImg = $(this).attr("id");
                var ids="";
                $(".site-piclist_pic a").each(function (i) {
                    ids=ids+$(this).attr("id")+",";
                });
                $("#selectedId").val(selectedImg);
                $("#picIds").val(ids);
                var url = "${basePath}/admin/file/listImg"
                $("#categoryForm").attr("action",url);
                $("#categoryForm").attr("target","_blank");
                $("#categoryForm").submit();
            });

            <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">


            //上传附图 删除
            $('.site-piclist li').contextMenu('menuPic', {
                bindings: {
                    'addAttach': function (t) {
                        var id = $(t).attr("item");
                        var url = "${basePath}/admin/file/toUploadAttach?id="+id+"&secondCategory=${secondSelectedId}";
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
                        var url = "${basePath}/admin/file/delete?pictureId="+id+"&secondSelectedId="+$("#secondSelectedId").val();
                        $.get(url,function (data) {
                            if(data.code ==0 ){
                                $(t).hide();
                            }else{
                                alert(data.msg)
                            }
                        })
                    }
                }
            });
            </c:if>
        })
    </script>
</head>

<body>
<form id="categoryForm" method="POST">
    <input type="hidden" id="sortId" name="sortId">
    <input type="hidden" id="id" name="id">
    <input type="hidden" id="categoryLevel" name="categoryLevel">
    <input type="hidden" id="parentId" name="parentId">
    <input type="hidden" id="firstSelectedId" name="firstSelectedId" value="${firstSelectedId}">
    <input type="hidden" id="secondSelectedId" name="secondSelectedId" value="${secondSelectedId}">
    <input type="hidden" id="thirdSelectedId" name="thirdSelectedId" value="${thirdSelectedId}">
    <input type="hidden" id="fourthSelectedId" name="fourthSelectedId" value="${ids}">
    <input type="hidden" id="currentPage" name = "currentPage" value="${currentPage}">
    <input type="hidden" id="pageSize" name = "pageSize" value="120">
    <input type="hidden" id="totalPage" name = "totalPage" value="${totalPage}">
    <input type="hidden" id="totalCount" name="totalCount" value="${totalCount}">
    <input id="startTime1" name="startTime" type="hidden" />
    <input id="endTime1"    name="endTime" type="hidden"/>
    <!--点击大图选择的图片id-->
    <input type="hidden" name="selectedId" id="selectedId" value="${selectedId}">
    <!--当前页所有图片id-->
    <input type="hidden" name="ids"id="picIds" value="${ids}">
    <input type="hidden" name="recommendId" id="recommendId" value="${recommendId}">
    <input type="hidden" name="brandId" id="brandId" value="${brandId}">
    <input type="hidden" name="brandName" id="brandName" value="${brandName}">
</form>
<div id="container" class="site-main">

    <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">
        <div class="contextMenu" id="menuPic" style="display: none">
            <ul>
                <li id="addAttach"> 上传附图</li>
                <li id="delete"> 删除</li>
            </ul>
        </div>
    </c:if>

    <div style="text-align: center;
    padding: 15px 0 0 0;
    font-size: 24px;
    font-weight: bold;">${fn:substring(selectedName, 0, fn:indexOf(selectedName,'.'))} </div>
    <div  class="ad-wrapper clearfix">
        <div class="divide-green-h"></div>
    </div>


    <div class="wrapper-piclist" style="    margin-left: -10px;">
        <ul class="site-piclist">
            <c:forEach var="item" items="${pictures}" varStatus="status">
                <li item="${item.id}">
                    <div class="site-piclist_pic">
                        <a id="${item.id}" path="${item.filePath}"  title="${item.name}"
                           href="javascript:void(0)" class="site-piclist_pic_link" attach="${item.attach}">
                            <img class="lazy" alt="${item.name}" title="${item.name}" style="border: 0"
                                 src="${ossPath}/${item.filePath}?x-oss-process=image/resize,m_pad,h_288,w_384" width="384" height="288"   data-original="${ossPath}/${item.filePath}?x-oss-process=image/resize,m_pad,h_288,w_384${watermarkParam}">
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
<script type="text/javascript" src="${basePath}/resources/js/top.js"></script>
</body>

</html>