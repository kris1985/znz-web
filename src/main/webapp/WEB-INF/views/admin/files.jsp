<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <%@ include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${basePath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${basePath}/resources/css/layout-default-latest.css"/>
    <link rel="stylesheet" href="${basePath}/resources/js/themes/default/style.min.css"/>
    <link rel="stylesheet" href="${basePath}/resources/css/file.css" rel="stylesheet">
    <link rel="stylesheet" href="${basePath}/resources/css/jquery.mCustomScrollbar.css">
    <link rel="stylesheet" href="${basePath}/resources/css/jquery.contextMenu.css">
    <link rel="stylesheet" href="${basePath}/resources/css/skins/black.css"/>

    <script type="text/javascript" src="${basePath}/resources/js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery-ui-latest.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.layout-latest.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/artDialog.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/iframeTools.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jstree.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/resources/js/jquery.mCustomScrollbar.concat.min.js"></script>
  <script type="text/javascript" src="${basePath}/resources/js/jquery.contextMenu.js"></script>

    <script type="text/javascript">


        var imageMenuData = [
            [

                {
                    text: "删除",
                    icon: "glyphicon glyphicon glyphicon-floppy-remove",
                    func: function () {
                        //$(this).css("padding", "10px");
                    }
                }
            ]
        ];
    </script>
</head>
<body>
<div id="container">
    <div class="pane ui-layout-center" id="file-content">

    </div>
    <div class="pane ui-layout-north" style="">

        <div style="width:100%;position:relative;height:40px">
            <div class="" style="width:105px;height;28px;position:absolute;left:1px;top:-4px">
                <img src="${basePath}/resources/img/logo2.JPG" height="45">
            </div>
            <div style="position:absolute;left:160px;top:0">
                <div class="nav_bar" style="float:left;width:80%" id="nav_bar">

                </div>
                <div class="" style="float:right;width:19%;">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="搜索" style="height:32px" id="search">

                        <span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
                    </div>
                </div>

            </div>

        </div>


    </div>
    <div class="pane ui-layout-south"><p style="text-align:center">指南针鞋讯版权所有</p></div>

    <div class="pane ui-layout-west">
        <div id="left_container" style="border:0px solid #ccc">
            <div id="jstree"></div>

        </div>
    </div>
</div>
<script type="text/javascript">
    function show(selectedId) {
        //alert(selectedId);
        var folderTemplate = "<div class=folder_wrap><div id={folderId}  class=\"folder_img\"><img  src=\"${basePath}/resources/img/folder.png\" width=\"256\" height=\"256\"></div><div class=\"folder_txt\">{folderName}</div></div>"
        var imgTemplate = "<div class='img_wrap ' ><img class='thumb' src='{thumbUrl}' id='{id}' style='max-width:256px;max-height:256px'><div class='img_txt'>{imgName}</div></div>"
        var navBarTemplate = "<span class='item' id={folderId}>{folderName}</span><span class='path_arrow'><img src='${basePath}/resources/img/path_arrow.png'></span>";
        var folderHtml = "";
        var imgHtml = "";
        var navBarHtml = "";
        $.get("${basePath}/admin/file/chidren/" + selectedId, function (result) {
            $.each(result.parentNodes, function (n, value) {
                tem = navBarTemplate.replace("{folderName}", value.name);
                tem = tem.replace("{folderId}", value.path);
                navBarHtml += tem;
            });
            $("#nav_bar").html(navBarHtml);
            $(".path_arrow:last").hide();
            if (result.fileNodes == null) {
                $("#file-content").html("");
                return;
            }
            $.each(result.fileNodes, function (n, value) {
                //alert(n + ' ' + value.directory);
                if (value.directory == true) {
                    tem = folderTemplate.replace("{folderId}", value.path);
                    tem = tem.replace("{folderName}", value.name);
                    folderHtml += tem;
                } else {
                    tem = imgTemplate.replace("{id}", value.path);
                    tem = tem.replace("{imgName}", value.name);
                    tem = tem.replace("{thumbUrl}", value.thumbUrl);
                    //tem = initImg(tem);
                    imgHtml += tem;
                }
            });
            //$(folderHtml).prependTo("#file-content");
            // $(imgHtml).prependTo("#file-content");

            $("#file-content").html(folderHtml + imgHtml);
            $(".thumb").load(function () {
                initImg($(this));
            });
        });
    }


    maxHeight = 256;
    maxWidth = 256;
    function initImg(img) {

        width = img.width();
        height = img.height();

        if (width > height) {
            maxWidth = maxHeight * (width / height);
        } else {
            maxWidth = maxHeight * (height / width);
        }

        percent = width / height;

        if (width > maxWidth) {
            // 图片按照最大宽度显示（缩放）
            // 图片的高度在maxHeight以内
            width = maxWidth;
            height = width / percent;

            // 按最大宽度缩放后，图片的高度仍超过maxHeight
            // 这是要图片应该是一个纵向高度比较长的图片
            if (height > maxHeight) {
                // 这里要将之前的最大宽度再按图片的长宽比来缩小
                // 这时的图片是高度等于maxHeight的
                // 而宽度是小于maxWidth的缩略图
                percent = maxHeight / height;
                width = width * percent;
                height = maxHeight;
            }
        } else {
            // 图片的原始宽度小于等于maxWidth
            if (width <= maxWidth) {
                // 高度超过maxHeight，再等比调整图片高度
                if (height > maxHeight) {
                    percent = maxHeight / height;
                    width = width * percent;
                    height = maxHeight;
                }
            }
        }

        /**/
        while (width > 256) {
            width = width / 1.1;
            height = height / 1.1
        }
        while (height > 256) {
            height = height / 1.1
            width = width / 1.1;
        }
        img.width(width);
        img.height(height);
        img.css("margin-left", -(width / 2) + "px");
        img.parent().removeClass("hide");
    }


    $(document).ready(function () {
        $('#container').layout({

            north__slidable: false	// OVERRIDE the pane-default of 'slidable=true'
            ,
            north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
            ,
            north__spacing_open: 0		// big resizer-bar when open (zero height)
            ,
            north__resizable: false,
            north__closable: false,
            west__resizable: true,
            west__closable: false,
            south__resizable: false	// OVERRIDE the pane-default of 'resizable=true'
            ,
            south__spacing_open: 0		// no resizer-bar when open (zero height)
            ,
            south__spacing_closed: 20
        });


        $("#file-content").delegate('.folder_wrap', 'click', function () {
            $(".folder_wrap").removeClass("folder_wrap_selected")
            $(".img_wrap").removeClass("folder_wrap_selected")
            $(this).addClass("folder_wrap_selected");
        });


        $("#file-content").delegate('.img_wrap', 'click', function () {
            $(".img_wrap").removeClass("folder_wrap_selected")
            $(".folder_wrap").removeClass("folder_wrap_selected")
            $(this).addClass("folder_wrap_selected");
        });

        var items = {
            upload: {
                label: "上传",
                icon: "glyphicon glyphicon glyphicon-open",
                action: function (data) {
                    var inst = $.jstree.reference(data.reference),
                            obj = inst.get_node(data.reference);
                   // console.log(obj);
                    var url = "${basePath}/admin/file/browse?parentId=" + obj.id
                    art.dialog.open(url, /** 弹出ART窗体*/
                            {
                                "id": obj.id,
                                title: "上传文件",
                                width: 500,
                                height: 400
                            }
                    );
                }
            },
            createDir: {
                label: "创建文件夹",
                icon: "glyphicon glyphicon glyphicon glyphicon-plus",
                action: function (data) {
                    var inst = $.jstree.reference(data.reference),
                    obj = inst.get_node(data.reference);
                    inst.create_node(obj, {'text': '新建文件夹'}, 'first', function (new_node) {
                        setTimeout(function () {
                            inst.edit(new_node);

                        }, 1);

                    });


                }
            },
          /* */ "rename": {
                "separator_before": false,
                "separator_after": false,
                "icon": "glyphicon glyphicon glyphicon glyphicon-edit",
                "_disabled": false, //(this.check("rename_node", data.reference, this.get_parent(data.reference), "")),
                "label": "重命名",
                "action": function (data) {
                    var inst = $.jstree.reference(data.reference),
                            obj = inst.get_node(data.reference);
                    inst.edit(obj);
                }
            },
            delete: {
                label: "删除",
                icon: "glyphicon glyphicon glyphicon-floppy-remove",
                action: function (data) {
                    if (!confirm("确认要删除吗")) {
                        return
                    }
                    var inst = $.jstree.reference(data.reference),
                    obj = inst.get_node(data.reference);
                    $.get("${basePath}/admin/file/delete/" + obj.id, function (res) {
                        if (res.code != 0) {
                            alert(res.msg);
                        } else {
                            if (inst.is_selected(obj)) {
                                inst.delete_node(inst.get_selected());
                            }
                            else {
                                inst.delete_node(obj);
                            }
                            $("#file-content").empty();
                        }
                    });

                }
            }

        };

        //初始化树
       /**/ $.get("${basePath}/admin/file/tree", function (result) {
            var bar = "<span class=\"item\" id=" + result[0].id + ">" + result[0].text + "</span>"
            $("#nav_bar").html(bar);
            $('#jstree').jstree({
                'plugins': ["wholerow", "contextmenu", "search"],

                'contextmenu': {
                    'items': items
                },

                'core': {
                    'data': result,
                    'strings': true,
                    "check_callback": true,
                    'multiple': false
                }
            }).on("rename_node.jstree", function (e, data) {
                //alert(data.node.id);
                 $.get("${basePath}/admin/file/mkdir/"+data.node.id.replace("_anchor", ""),function(data){
                    if(data!="0"){
                        alert(data);
                    }
                })
            });
        });



        // 文件管理器左部文件树点击事件
        $('#jstree').on("changed.jstree", function (e, data) {
            console.log("-----------------------------"+data.selected);
            if (data.selected.length != 0) {
                show(data.selected[0].replace("_anchor", ""));
            }
        });
        $('#jstree').on("rename_node.jstree", function (e, data) {
            //console.log("---------------rename_node--------------"+data.text);
            //console.log("---------------rename_node--------------"+data.old);
            var id = data.node.parent+"FILE_SEPARATOR"+data.text+"_anchor";
            var old = data.node.parent+"FILE_SEPARATOR"+data.old;
            $("#jstree").jstree(true).set_id (data.node.id, id);
            $.get("${basePath}/admin/file/mkdir/"+id.replace("_anchor", "")+"/"+old,function(data){
                if(data!="0"){
                    alert(data);
                }
            })
        });
        //文件管理器上部导航点击
        $(document).delegate('.item', 'click', function () {
            show($(this).attr("id"));
            // $('#jstree').jstree(false).select_node($(this).attr("id")+"_anchor");
            //  $('#jstree').jstree('select_node', $(this).attr("id")+"_anchor");
            //$.jstree.reference('#jstree').select_node($(this).attr("id") + "_anchor");
            //  $('#jstree').jstree().deselect_all();
            //  $.jstree.reference('#jstree').deselect_all();
            $('#jstree').jstree(true).select_node($(this).attr("id") + "_anchor");
        });
        //文件管理器右部文件夹点击
        $(document).delegate('.folder_img', 'dblclick', function () {
            show($(this).attr("id"));
            $('#jstree').jstree(true).deselect_all();
            $('#jstree').jstree(true).select_node($(this).attr("id") + "_anchor");
        });

        //文件管理器右部文件点击
        $(document).delegate('.thumb', 'dblclick', function () {
        id = $(this).attr("id");
           suffix= id.substring(id.indexOf('.')+1);
           path =  id.substring(0,id.indexOf('.'));
            window.open("${basePath}/admin/file/listImg/" + path+"?suffix="+suffix );
        });

        var sclHeight = $(document).height() - 150;

        //左边滚动条
        $("#left_container").mCustomScrollbar({
            setHeight: sclHeight,
            theme: "inset-2-dark"
        });

        $.contextMenu({
            selector: '.thumb',
            callback: function(key, options) {
                path = this.attr("id");
                if (!confirm("确认要删除吗")) {
                    return
                }
                var delImg = $(this);

                $.get("${basePath}/admin/file/delete/"+path+".json",function(data){
                   if(data.code!=0){
                        alert(data.msg);
                    }else{
                       delImg.parent().hide(1000);
                   }
                });

            },
            items: {
                "Delete": {name: "删除", icon: "delete"}
            }
        });

     /*   $.contextMenu({
            selector: '.folder_img',
            callback: function(key, options) {
                path = this.attr("id");
                isdelete = false;
                $.get("${basePath}/admin/file/delete/"+path+".json",function(data){
                    if(data.code!=0){
                        alert(data.msg);
                    }else{
                        isdelete = true;
                    }
                });
                $(this).parent().hide(1000);
            },
            items: {
                "Delete": {name: "删除", icon: "delete"}
            }
        });*/
        /*搜索*/
        var to = false;
        $('#search').keyup(function () {
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                var v = $('#search').val();
                $('#jstree').jstree(true).search(v);
            }, 250);
        });
    });
</script>

</body>
</html>