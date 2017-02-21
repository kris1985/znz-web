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
    <link rel="stylesheet" href="${basePath}/resources/jqgrid/css/ui.jqgrid.css" type="text/css" />
    <link href="${basePath}/resources/css/aqy.css" rel="stylesheet" type="text/css" />
    <style>
        #gbox_list2{margin: 0 auto}
    </style>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-1.8.24.custom.min.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/i18n/grid.locale-cn.js" ></script>
    <script type="text/javascript" src="${basePath}/resources/jqgrid/js/jquery.jqGrid.min.js" ></script>

    <script>
    var basePath = getContextPath();
      $(function() {
        //$( ".mod_category_item" ).sortable();
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





        $("#rootCategoryAddBtn").click(function(){

           var rootCategoryAddBtn = $(this);
           console.log( $(this).parent().siblings().length);
            $( "#dialog" ).dialog({
                 height: 150,
                 modal: true,
                 position: { my: "left top", at: "left bottom", of: "#rootCategoryAddBtn" },
                 buttons: [
                     {
                       text: "提交",
                       icons: {
                         primary: "ui-icon-heart"
                       },
                       click: function() {
                         //$( this ).dialog( "close" );
                         var name = $.trim($("#categoryName").val());
                         //console.log( rootCategoryAddBtn.parent().parent().html());
                         var sortId = rootCategoryAddBtn.parent().parent().children().length +1;
                        console.log("sortId:"+sortId);
                         var parentId = 0;
                          $.ajax({
                                type: "POST",
                                url: basePath+"/admin/subCategory/add",
                                data: "name="+name+"&sortId="+sortId+"&parentId="+parentId+"&categoryLevel=0",
                                cache: false,
                                success: function(msg){
                                    if(msg.code !=0){
                                        alert( msg.msg );
                                    }else{
                                        $("#rootCategoryAddBtn").parent().before("<li id='li_'"+msg.msg+"><a href='/znz-web/admin/subCategory/showCategory?parentId=null&firstSelectedId="+msg.msg+"'>"+msg.msg+"</a></li>");

                                        $("#dialog").dialog( "close" );
                                        $("#categoryName").val("");
                                    }
                                },
                                error:function(msg){
                                    alert( "服务器出错了" );
                                }
                           });
                       }
                     }
                   ]
               });
        })



      $("#secondCategoryAddBtn").click(function(){
                   var parentId = '${firstSelectedId}';
                    if(parentId == null || parentId==""){
                       alert("请选择一级类别");
                       return false;
                    }
                  $( "#dialog" ).dialog({
                       height: 150,
                       modal: true,
                       position: { my: "left top", at: "left bottom", of: "#secondCategoryAddBtn" },
                       buttons: [
                           {
                             text: "提交",
                             icons: {
                               primary: "ui-icon-heart"
                             },
                             click: function() {
                               var name = $.trim($("#categoryName").val());
                               //console.log( rootCategoryAddBtn.parent().parent().html());
                               var sortId = $("#secondCategoryAddBtn").parent().parent().children().length +1;
                              console.log("sortId:"+sortId);


                                $.ajax({
                                      type: "POST",
                                      url: basePath+"/admin/subCategory/add",
                                      data: "name="+name+"&sortId="+sortId+"&parentId="+parentId+"&categoryLevel=1",
                                      cache: false,
                                      success: function(msg){
                                          if(msg.code !=0){
                                              alert( msg.msg );
                                          }else{
                                              $("#secondCategoryAddBtn").parent().before("<li id='li_'"+msg.msg+"><a href='#'>"+name+"</a></li>");
                                              $("#dialog").dialog( "close" );
                                              $("#categoryName").val("");
                                          }
                                      },
                                      error:function(msg){
                                          alert( "服务器出错了" );
                                      }
                                 });
                             }
                           }
                         ]
                     });
              })
});
      </script>

</head>

<body>
    <div id="container">

    <div id="dialog" title="新增类别" style="display:none">
      类别名称：<input type="text" id="categoryName" name="categoryName">
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
           <li>
                <button id="rootCategoryAddBtn" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" style="font-size:10px"><span class="ui-button-icon-primary ui-icon ui-icon-plus"></span><span class="ui-button-text"></span></button>
            </li>
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
           <li>
            <button id="secondCategoryAddBtn" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" style="font-size:10px"><span class="ui-button-icon-primary ui-icon  ui-icon-plus"></span><span class="ui-button-text"></span></button>
            <button id="thirdCategoryAddBtn" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" style="font-size:10px"><span class="ui-button-icon-primary ui-icon ui-icon-carat-1-se"></span><span class="ui-button-text"></span></button>
           </li>
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