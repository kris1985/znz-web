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
                        console.log(id);
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
})
      </script>

</head>

<body>
    <div id="container">

    <div id="dialog" title="新增类别" style="display:none">
    <form id="categoryForm" method="POST">
      类别名称：<input type="text" id="categoryName" name="name">
      <input type="hidden" id="sortId" name="sortId">
      <input type="hidden" id="id" name="id">
       <input type="hidden" id="categoryLevel" name="categoryLevel" value="0">
        <input type="hidden" id="parentId" name="parentId" value="0">
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
               <li id="li_${item.id}" class="li_item selected"><a href="/znz-web/admin/subCategory/showCategory?parentId=${item.id}&firstSelectedId=${item.id}">${item.name}</a> </li>
        </c:if>
        <c:if test="${item.categoryLevel == 0 && firstSelectedId != item.id  }">
               <li id="li_${item.id}" class="li_item "><a href="/znz-web/admin/subCategory/showCategory?parentId=${item.id}&firstSelectedId=${item.id}">${item.name}</a> </li>
        </c:if>
       </c:forEach>

        </ul>
    </div>

    <div class="mod_sear_list" >
          <h3>二级类：</h3>
             <ul class="mod_category_item">
         <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <c:if test="${item.parentId == firstSelectedId && secondSelectedId == item.id  }">
                   <li id="li_${item.id}" class="li_item selected"><a href="/znz-web/admin/subCategory/showCategory?parentId=${item.id}&firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a> </li>
            </c:if>
            <c:if test="${item.parentId == firstSelectedId && secondSelectedId != item.id  }">
                   <li id="li_${item.id}" class="li_item "><a href="/znz-web/admin/subCategory/showCategory?parentId=${item.id}&firstSelectedId=${firstSelectedId}&secondSelectedId=${item.id}">${item.name}</a> </li>
            </c:if>
           </c:forEach>
            </ul>
        </div>

      <c:forEach var="item" items="${subCategoryVOs}" varStatus="status">
            <c:if test="${item.categoryLevel == 2}">
                <div class="mod_sear_list" >
                    <h3>${item.name}</h3>
                    <ul class="mod_category_item">
                       <c:forEach var="category" items="${item.childrens}" varStatus="status">
                          <li id="li_${category.id}"  class="li_item"> <a href="#">${category.name} </a> </li>
                        </c:forEach>
                    </ul>
                </div>
             </c:if>
       </c:forEach>
    </div>
</body>

</html>