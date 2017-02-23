<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
     <%@ include file="../common/common.jsp"%>
  <link href="${basePath}/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/css/jquery.contextMenu.css">
    <link href="${basePath}/resources/css/aqy.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${basePath}/resources/css/jquery.contextMenu.css">
    <style>
        #gbox_list2{margin: 0 auto}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>
    <script type="text/javascript" src="http://www.trendskitchens.co.nz/jquery/contextmenu/jquery.contextmenu.r2.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/artDialog.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/iframeTools.js"></script>

    <script>
    var basePath = getContextPath();
function openDialog(url){
              $( "#dialog" ).dialog({
                   height: 150,
                   modal: true,
                   position: { my: "left top", at: "left bottom", of: "#"+t.id },
                   buttons: [
                       {
                         text: "提交",
                         icons: {
                           primary: "ui-icon-heart"
                         },
                         click: function() {
                            $("#categoryForm").attr("action",url);
                            $("#categoryForm").submit();
                         }
                      }]
              });
        }
$(function() {

         $(".mod_category_item").sortable({
                    update: function(event, ui) {
                        var array = $(this).children();
                        var data="";
                        for (var i=0;i<array.length;i++)
                        {
                           if(""== array[i].id){
                            continue;
                           }
                           //console.log(array[i].id.replace("li_",""));
                           data+=array[i].id.replace("li_","") +":"+i +";";
                        }
                        console.log(data);
                        $.ajax({
                           type: "POST",
                           url: basePath+"/admin/subCategory/sort",
                           data: "param="+data,
                           cache: false,
                           success: function(msg){
                             //alert( "Data Saved: " + msg );
                           },
                           error:function(msg){
                               alert( "服务器出错了" );
                           }
                        });

                    }
                 });
                $( ".mod_category_item" ).disableSelection();




        $('.selected').contextMenu('myMenu1', {
              bindings: {
                'add': function(t) {
                        var url = basePath+"/admin/subCategory/add";
                        var id = $("#"+t.id );
                       $("#categoryLevel").val(0);
                       $("#parentId").val(0);
                        var sortId = id.parent().children().length +1;
                        $("#sortId").val(sortId);
                         $( "#dialog" ).dialog({
                                           height: 150,
                                           modal: true,
                                           position: { my: "left top", at: "left bottom", of: "#"+t.id },
                                           buttons: [
                                               {
                                                 text: "提交",
                                                 icons: {
                                                   primary: "ui-icon-heart"
                                                 },
                                                 click: function() {
                                                    $("#categoryForm").attr("action",url);
                                                    $("#categoryForm").submit();
                                                 }
                                              }]
                         });
                },

                'addSub': function(t) {
                   var url = basePath+"/admin/subCategory/add";
                    var id = $("#"+t.id );
                                          var parentId = t.id.replace("li_","");
                                          var sortId = id.parent().children().length +1;
                                          $("#sortId").val(sortId);
                                          $("#parentId").val(parentId);
                                          $("#categoryLevel").val(1);
                                           $( "#dialog" ).dialog({
                                                             height: 150,
                                                             modal: true,
                                                             position: { my: "left top", at: "left bottom", of: "#"+t.id },
                                                             buttons: [
                                                                 {
                                                                   text: "提交",
                                                                   icons: {
                                                                     primary: "ui-icon-heart"
                                                                   },
                                                                   click: function() {
                                                                      $("#categoryForm").attr("action",url);
                                                                      $("#categoryForm").submit();
                                                                   }
                                                                }]
                                           });
                },

                'rename': function(t) {
                  var url = basePath+"/admin/subCategory/update";
                  $("#id").val(t.id.replace("li_",""));
                  $( "#dialog" ).dialog({
                                                             height: 150,
                                                             modal: true,
                                                             position: { my: "left top", at: "left bottom", of: "#"+t.id },
                                                             buttons: [
                                                                 {
                                                                   text: "提交",
                                                                   icons: {
                                                                     primary: "ui-icon-heart"
                                                                   },
                                                                   click: function() {
                                                                      $("#categoryForm").attr("action",url);
                                                                      $("#categoryForm").submit();
                                                                   }
                                                                }]
                                           });

                }

              }

            });

            $(".leaf_item").click(function(){
             if($(this).hasClass("selected")){
               return;
             }else{
                    $(this).addClass("selected");
                    if($(this).hasClass("all_item")){
                        alert("ss");
                        $.each( $(this).siblings(), function(i, n){
                           $(this).removeClass("selected");
                         });
                    }else{
                        $(this).siblings().first().removeClass("selected");
                    }
                   if($(this).parent().children().length-1 ==  $(this).parent().find(".selected").length){
                         $.each( $(this).parent().children(), function(i, n){
                                  $(this).removeClass("selected");
                        });
                        $(this).siblings().first().addClass("selected");
                   }
                    var temp="";
                    $("#leaf_category").find(".selected").each(function(i){
                        console.log($(this).attr("id"));
                        id = $(this).attr("id");
                        temp+=id+","
                   });
                   console.log(temp);
                   var url = "${basePath}/admin/subCategory/showCategory"
                   $("#fourthSelectedId").val(temp);
                  $("#categoryForm").attr("action",url);
                  $("#categoryForm").submit();

                }


            });

             $("#uploadBtn").click(function(){
               var temp="";
               $("#leaf_category").find(".selected").each(function(i){
                      id = $(this).attr("id");
                      if(id.indexOf("all")!=-1){
                         $.each( $(this).siblings(), function(i, n){
                               id = $(this).attr("id").replace("li_","");
                               temp+=id+","
                         });
                      }else{
                             id = id.replace("li_","");
                             temp+=id+","
                      }

                 });
                  console.log(temp);
                var url = "${basePath}/admin/file/toUpload?category="+temp
                                art.dialog.open(url,
                                        {
                                            "id": "2345",
                                            title: "上传文件",
                                            width: 500,
                                            height: 400,
                                            close : function () {
                                              // show(parentId);
                                            }
                                        }
                                );

             })



})
      </script>

</head>

<body>
    <div id="container" class="site-main">

    <div id="dialog" title="新增类别" style="display:none">
    <form id="categoryForm" method="POST">
      类别名称：<input type="text" id="categoryName" name="name">
      <input type="hidden" id="sortId" name="sortId">
      <input type="hidden" id="id" name="id">
      <input type="hidden" id="categoryLevel" name="categoryLevel" >
      <input type="hidden" id="parentId" name="parentId" >
       <input type="hidden" id="firstSelectedId" name="firstSelectedId" value="${firstSelectedId}">
       <input type="hidden" id="secondSelectedId" name="secondSelectedId" value="${secondSelectedId}">
       <input type="hidden" id="thirdSelectedId" name="thirdSelectedId" value="${thirdSelectedId}">
       <input type="hidden" id="fourthSelectedId" name="fourthSelectedId" value="${fourthSelectedId}">
      </form>
    </div>

     <div class="contextMenu" id="myMenu1">

          <ul>
            <li id="add"><img src="folder.png" /> 新增同类</li>
            <li id="addSub"><img src="email.png" />新增子类</li>
            <li id="rename"><img src="disk.png" /> 重命名</li>
          </ul>

        </div>

     <div class="mod_sear_list" >
      <h3>一级类：</h3>
         <ul class="mod_category_item">
     <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
        <c:if test="${item.categoryLevel == 0 && firstSelectedId == item.id  }">
               <li id="li_${item.id}" class="li_item selected"><a href="/znz-web/admin/subCategory/showCategory?firstSelectedId=${item.id}">${item.name}</a> </li>
        </c:if>
        <c:if test="${item.categoryLevel == 0 && firstSelectedId != item.id  }">
               <li id="li_${item.id}" class="li_item "><a href="/znz-web/admin/subCategory/showCategory?firstSelectedId=${item.id}">${item.name}</a> </li>
        </c:if>
       </c:forEach>

        </ul>
    </div>

    <div class="mod_sear_list" >
          <h3>二级类：</h3>
             <ul class="mod_category_item">
         <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <c:if test="${item.parentId == firstSelectedId && secondSelectedId == item.id  }">
                   <li id="li_${item.id}" class="li_item selected"><a href="/znz-web/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a> </li>
            </c:if>
            <c:if test="${item.parentId == firstSelectedId && secondSelectedId != item.id  }">
                   <li id="li_${item.id}" class="li_item "><a href="/znz-web/admin/subCategory/showCategory?firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a> </li>
            </c:if>
           </c:forEach>
            </ul>
        </div>

    <div id = "leaf_category">
      <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <c:if test="${item.categoryLevel == 2 && item.parentId == secondSelectedId}">
                <div class="mod_sear_list" >
                    <h3>${item.name}</h3>
                    <ul class="mod_category_item ">
                      <li id="all_${item.id}"  class="li_item leaf_item all_item selected"> <a id="aa" href="javascript:void()" class="">全部</a> </li>
                       <c:forEach var="category" items="${item.childrens}" varStatus="status">
                          <li id="li_${category.id}"  class="li_item leaf_item "> <a id="a_${category.id}" href="javascript:void()" class="">${category.name} </a> </li>
                        </c:forEach>
                    </ul>
                </div>
             </c:if>
       </c:forEach>
    </div>
  </div>

<div id="1000000000046" class="ad-wrapper clearfix">
    <div class="divide-green-h"></div>
</div>

  <div>
    <input type="button" id="uploadBtn" value="上传"></button>
  </div>
<div id="1000000000046" class="ad-wrapper clearfix">
    <div class="divide-green-h"></div>
</div>

<div class="wrapper-piclist" style="    margin-left: -20px;">
		<ul class="site-piclist">
		 <c:forEach var="item" items="${pictures}" varStatus="status">
				<li>
                    <div class="site-piclist_pic">
                        <a alt="${item.name}" title="${item.name}" href="#" class="site-piclist_pic_link" target="_blank">
                            <img alt="${item.name}" title="${item.name}" src="${item.filePath}?x-oss-process=image/resize,m_pad,h_199,w_280">
                        </a>
                    </div>
                </li>
          </c:forEach>
		</ul>
</div>

</body>

</html>