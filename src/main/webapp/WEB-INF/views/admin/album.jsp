<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
     <meta name="renderer" content="webkit"/>
    <link rel="icon" href="../../favicon.ico">
    <title>指南针鞋讯-大图 ${watermarkParam}</title>
     <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/docs-min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/pure-min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/blue.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/grids.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/album.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/base.css" rel="stylesheet" type="text/css" media="all" />

   <c:set var="watermarkParamProcess" value="" />
                 <c:if test="${ not empty watermarkParam  }">
                      <c:set var="watermarkParamProcess" value="?x-oss-process=image${watermarkParam}" />
                  </c:if>
</head>
<body class="trs-tags" style="background:black;overflow-x:hidden;overflow-y:hidden" sroll="no">
<div class="content1">

    <div class="album" id="album" >
        <div class="ite_btn" >
            <span id="addSizeBtn" class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span>
            <span id="subSizeBtn" class="glyphicon glyphicon-zoom-out" aria-hidden="true"></span>
			<span id="downloadBtn" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
        </div>

        <div class="page_bar"><span id="totalIndex">${totalIndex}</span>/<span id="totalCounts">${totalCount}</span></div>

        <p class="album-image-ft" id="album-image-ft"></p>
        <div class="album-image-md" id="album-image-md" >
            <div class="album-image-bd" id="album-image-bd" ><img src="${ossPath}/${selectedImg}${watermarkParamProcess}" id="album-image" alt="${selectedName}" onmousewheel="return bbimg(this)"/></div>
            <a href="#prev-image" class="album-image-btn-prev" id="album-image-btn-prev">‹</a>
            <a href="#next-image" class="album-image-btn-next" id="album-image-btn-next">›</a>
            <p class="album-image-loading-overlay hide" id="album-image-loading-overlay" style="display: block">
                <!--<img src="${basePath}/resources/img/loading.gif" alt="loading..." width="100" height="100" /></p>-->
            <div class="attachs" id="attachs" >

            </div>
        </div>

    </div>

    <div class="album-carousel hide" id="album-carousel">
        <a href="#prev-group" class="album-carousel-btn-prev" id="album-carousel-btn-prev">‹</a>
        <div class="album-carousel-zone" id="album-carousel-zone">
            <ul class="album-carousel-list" id="album-carousel-list">

                <c:forEach items="${pictures}" var="img">

                    <c:choose>
                        <c:when test="${img.filePath eq selectedImg}">
                            <li class="album-carousel-thumb album-carousel-thumb-selected"><a href="${ossPath}/${img.filePath}${watermarkParamProcess}"
                                                                                              title="${img.name}" attachs="${img.attach}" id="${img.id}"></a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="album-carousel-thumb"><a href="${ossPath}/${img.filePath}${watermarkParamProcess}" title="${img.name}" attachs="${img.attach}" id="${img.id}"></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>


            </ul>


        </div>
        <a href="#next-group" class="album-carousel-btn-next" id="album-carousel-btn-next">›</a>
    </div>



</div>
<form id="albumForm" method="post" action = "${basePath}/admin/file/reloadListImg">
             <input type="hidden" id="secondSelectedId" name="secondSelectedId" value="${secondSelectedId}">
            <input type="hidden" id="fourthSelectedId" name="fourthSelectedId" value="${fourthSelectedId}">
            <input type="hidden" id="currentPage" name = "currentPage" value="${currentPage}">
            <input type="hidden" id="pageSize" name = "pageSize" value="80">
            <input type="hidden" id="totalPage" name = "totalPage" value="${totalPage}">
            <input type="hidden" id="moveFlag" name = "moveFlag">
            <input type="hidden" id="totalCount" name = "totalCount" value="${totalCount}">
            <input type="hidden" name="recommendId" id="recommendId" value="${recommendId}">
</form>
<script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/carousel.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/album.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery-ui-latest.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery.mousewheel.min.js"></script>
<script type="text/javascript">


    //无级缩放图片大小
    function bbimg(o){
        var zoom=parseInt(o.style.zoom,10)||100;zoom+=event.wheelDelta/12;
        if(zoom>80&&zoom<=500) o.style.zoom=zoom+'%';
        return false;
    }

    var Album;

    $(function(){
        var image = new Image();
        image.src = $("#album-image").attr("src");
        image.onload = function () {
            var width = image.width;
            var height = image.height;
            maxHeight = $(window).height() - 0;
            maxWidth = $(window).width();
            w = $("#album-image").width();
            h = $("#album-image").height();
           // console.log("$(#album-image):" + $("#album-image").attr("src"));
            //console.log(w + "-" + h + "-" + maxWidth + "-" + maxHeight);
            if (w > h) {
                maxWidth = maxHeight * (w / h);
            } else {
                maxWidth = maxHeight * (h / w);
            }
            Album = new jQuery.Album({
                // 当前显示图片在缩略图的中索引值
                <c:choose>
                    <c:when test="${moveFlag eq 'pre'}">
                         curIndex:${pageSize-1},
                    </c:when>
                <c:otherwise>
                    curIndex: ${currentIndex},
                </c:otherwise>
                </c:choose>
                // 大图片显示区域的最大宽度
                maxWidth: maxWidth,
                // 大图片显示区域的最高宽度
                maxHeight: maxHeight
            });
        };
        <c:if test="${userSession.user.userType ==2 or userSession.user.userType ==0 or userSession.user.userType ==3}">



             $(".del").live("click",function(evt){
                 evt.preventDefault();
                 evt.stopPropagation();
                 var parent = $(this).parent();
                var pictureId = parent.find("img").attr("parentId");
                var attachPath = parent.find("img").attr("path");
                var url = "${basePath}/admin/file/deleteAttach?pictureId="+pictureId+"&attachPath="+attachPath;

                $.get(url,function (data) {
                    if(data.code == 0){
                        parent.hide(500);
                    }else {
                        alert(data.msg);
                    }
                });
            })
        </c:if>

     /*   $(".attachs img").click(function () {
          $("#album-image-bd img").attr("src", $(this).attr("origin_src") );
        })*/

        $(".attachs img").live("click",function(){
            $(".attach_item").css("border"," 2px solid #ECECEC");
            $(this).parent().css("border"," 2px solid #699f00");
            var src = $(this).attr("origin_src");
            console.log("src:"+src);
            $("#album-image-bd img").attr("src", src );
            $("#album-image-bd img").attr("alt", $(this).attr("alt") );
        });
		//setTitle();
        if($.cookie('albumBackground') !=undefined){
            $("body").css("background",$.cookie('albumBackground'));
            $("#album").css("background",$.cookie('albumBackground'));
            $(".fixedBtns").css("background",$.cookie('albumBackground'));
            index = $.cookie('albumColorIndex');
            if(index!=undefined){
                if(index=="1" || index=="2"){
                    $("#album-image-ft").css("color","#000000");
                    $(".fixedBtns li").css("color","#000000");
                }else{
                    $("#album-image-ft").css("color","#FFFFFF");
                    $(".fixedBtns li").css("color","#FFFFFF");
                }
            }
        }
        //alert($(document).width()+"-"+$(document).height());
        $("#album-carousel-zone").width($(window).width()-100);

        $(".album-image-bd").height($(window).height()-0);
        $('#album-image').draggable();
        $(".skin ul li").click(function(){
            //alert($(this).css("background"));
            var text =$(this).text();
            //alert(text);
            if(text=="1" || text=="2"){
                $("#album-image-ft").css("color","#000000");
                $(".fixedBtns li").css("color","#000000");
            }else{
                $("#album-image-ft").css("color","#FFFFFF");
                $(".fixedBtns li").css("color","#FFFFFF");
            }
            $("body").css("background",$(this).css("background"));
            $("#album").css("background",$(this).css("background"));
            $(".fixedBtns").css("background",$(this).css("background"));
            $.cookie('albumBackground', $(this).css("background"));
            $.cookie('albumColorIndex', text);
        });

        $(".fixedBtns li").hover(function(){
            $(this).css("color","#00c40c");
        },function(){
            $(this).css("color",$("#album-image-ft").css("color"));
        });

        $("#subSizeBtn").click(function(){
            width = $("#album-image").width();
            height = $("#album-image").height();
            if(width<500 || height<500){
                return;
            }
            percent = width/height;
            width = width-70;
            $("#album-image").width(width);
            height = width / percent;
            //alert(height);
            $("#album-image").height(height);
            margin_left = -(width / 2) + "PX";
            margin_top = -(height / 2) + "PX";
            $("#album-image").css({"margin-left":margin_left,"margin-top":margin_top});
        });

        $("#addSizeBtn").click(function(){
            percent = $("#album-image").width() / $("#album-image").height();
            width = $("#album-image").width()+70;
            $("#album-image").width(width);
            height = width / percent;
            $("#album-image").height(height);
            margin_left = -(width / 2) + "PX";
            // alert(margin_left);
            $("#album-image").css("margin-left",margin_left);
        });

        $("#preBtn").click(function(evt){
            Album.prev();
            evt.preventDefault();
            evt.stopPropagation();
            back2Normal();
        });
        $("#nextBtn").click(function(evt){
            Album.next();
            evt.preventDefault();
            evt.stopPropagation();
            back2Normal();

        });
        $("#thumbBtn").click(function(){
            // height = $("#album-image-md").height();
            if($("#album-carousel").hasClass("hide")){
                $("#album-carousel").removeClass("hide");
                //$("#album-image-md").height(height+50)
            }else{
                $("#album-carousel").addClass("hide");
                //$("#album-image-md").height(height-50)
            }
        });
        
        $("#downloadBtn").click(function(){
            var imgpath = $("#album-image").attr("src");
            var alt = $("#album-image").attr("alt");
            window.open("${basePath}/admin/file/download?imgPath=" + imgpath+"&fileName="+alt)
        });

     //   document.oncontextmenu=function(){return false;}

        document.onkeydown=function(e){
            e=window.event||e;
            switch(e.keyCode){
                case 37: //左键
                    Album.prev();
                    evt.preventDefault();
                    evt.stopPropagation();
                    back2Normal();
                    break;
                case 39: //右键
                    Album.next();
                    evt.preventDefault();
                    evt.stopPropagation();
                    back2Normal();
                default:
                    break;
            }
        }

        $('body').bind('mousewheel', function(event, delta) {
            var dir = delta > 0 ? 'Up' : 'Down';
            if (dir == 'Up') {
                Album.prev();
                back2Normal();
            }else {
                Album.next();
                back2Normal();
            }
        });

        document.oncontextmenu=function(){return false;}
    });

    function setTitle(){
        document.title = '${parentName}';
    }
</script>



</body>
</html>