<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title>相册</title>
     <%@ include file="../common/common.jsp"%>
    <link href="${basePath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/docs-min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/pure-min.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/blue.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/grids.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/album.css" rel="stylesheet" type="text/css" media="all" />
    <link href="${basePath}/resources/css/base.css" rel="stylesheet" type="text/css" media="all" />
</head>
<body class="trs-tags" style="background:black;overflow-x:hidden;overflow-y:hidden" sroll="no">
<div class="content1">

    <div class="album" id="album" >
        <div class="skin">
            <ul>
                <li id="skin1" style="background:#FFFFFF">1</li>
                <li id="skin2" style="background:#F1F1F1">2</li>
                <li id="skin3" style="background:#202020">3</li>
                <li  id="skin4" style="background:#652121">4</li>
                <li id="skin5" style="background:#000000">5</li>
            </ul>
        </div>
        <p class="album-image-ft" id="album-image-ft"></p>
        <div class="album-image-md" id="album-image-md" >
            <div class="album-image-bd" id="album-image-bd" ><img src="${ossPath}/${selectedImg}?x-oss-process=image${watermarkParam}" class="" id="album-image" alt="" onmousewheel="return bbimg(this)"/></div>
            <a href="#prev-image" class="album-image-btn-prev" id="album-image-btn-prev">‹</a>
            <a href="#next-image" class="album-image-btn-next" id="album-image-btn-next">›</a>
            <p class="album-image-loading-overlay hide" id="album-image-loading-overlay"><img src="${basePath}/resources/img/loading.gif" alt="loading..." width="100" height="100" /></p>
            <!--
            <div class="attachs" style="position: absolute; right: 50px;position: absolute;
right: 150px;
border: 1px solid #ccc;
width: 100px;
top: 50px;background: transparent;z-index: 9999">
                    <c:forEach var="item" items="${attachs}">
                       <img src="${ossPath}/${item}?x-oss-process=image/resize,m_pad,h_80,w_100"
                            origin_src="${ossPath}/${item}" style="display: block;width: 100px;height: 71px;border: 0px solid #ccc;margin-bottom: 10px;">
                    </c:forEach>
            </div>
            -->
        </div>

    </div>

    <div class="album-carousel hide" id="album-carousel">
        <a href="#prev-group" class="album-carousel-btn-prev" id="album-carousel-btn-prev">‹</a>
        <div class="album-carousel-zone" id="album-carousel-zone">
            <ul class="album-carousel-list" id="album-carousel-list">


                <c:forEach items="${imgs}" var="img">
                    <c:choose>
                        <c:when test="${img eq selectedImg}">
                            <li class="album-carousel-thumb album-carousel-thumb-selected"><a href="${ossPath}/${img}?x-oss-process=image${watermarkParam}"></a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="album-carousel-thumb"><a href="${ossPath}/${img}?x-oss-process=image${watermarkParam}"></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>


            </ul>


        </div>
        <a href="#next-group" class="album-carousel-btn-next" id="album-carousel-btn-next">›</a>
    </div>
    <div class="fixedBtns" style="height:45px;background-color:black; z-index:999; position:fixed; bottom:0; left:0; width:100%; _position:absolute;">
        <div style="width:414px; margin:0 auto;height:45px;" >
            <ul class="bs-glyphicons-list" style="height:45px;margin-top:1px">
                <li id="preBtn">
                    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span><br/>
                    <span class="glyphicon-class">上一张</span>
                </li>
                <li id="nextBtn">
                    <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span><br/>
                    <span class="glyphicon-class">下一张</span>
                </li>
                <li id="addSizeBtn">
                    <span class="glyphicon glyphicon-plus"></span><br/>
                    <span class="glyphicon-class">放大</span>
                </li>
                <li id="subSizeBtn">
                    <span class="glyphicon glyphicon-minus" ></span><br/>
                    <span class="glyphicon-class">缩小</span>
                </li>
                <li id="downloadBtn">
                    <span class="glyphicon glyphicon-download-alt"></span><br/>
                    <span class="glyphicon-class">下载</span>
                </li>
				<!--
                <li id="thumbBtn">
                    <span class="glyphicon glyphicon-picture"></span><br/>
                    <span class="glyphicon-class">缩略图</span>
                </li>
				-->

            </ul>

        </div>
    </div>
</div>
<script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/carousel.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/album.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery-ui-latest.js"></script>
<script type="text/javascript" src="${basePath}/resources/js/jquery.cookie.js"></script>
<script type="text/javascript">


    //无级缩放图片大小
    function bbimg(o){
//alert("s");
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
            maxHeight = $(document).height() - 90;
            maxWidth = $(document).width();
            w = $("#album-image").width();
            h = $("#album-image").height();
            console.log("$(#album-image):" + $("#album-image").attr("src"));
            console.log(w + "-" + h + "-" + maxWidth + "-" + maxHeight);
            if (w > h) {
                maxWidth = maxHeight * (w / h);
            } else {
                maxWidth = maxHeight * (h / w);
            }
            Album = new jQuery.Album({
                // 当前显示图片在缩略图的中索引值
                curIndex: ${currentIndex},
                // 大图片显示区域的最大宽度
                maxWidth: maxWidth,
                // 大图片显示区域的最高宽度
                maxHeight: maxHeight
            });
        };

        $(".attachs img").click(function () {
          $("#album-image-bd img").attr("src", $(this).attr("origin_src") );
        })
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
        $("#album-carousel-zone").width($(document).width()-100);

        $(".album-image-bd").height($(document).height()-0);
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
            $(this).css("color","#057BDF");
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
            window.open("${basePath}/admin/file/download?imgPath=" + imgpath+"&fileName=是浪费大家.jpg")
        });

     //   document.oncontextmenu=function(){return false;}

    });

    function setTitle(){
        document.title = '${parentName}';
    }
</script>



</body>
</html>