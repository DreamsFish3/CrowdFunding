// 生成树形结构的函数
function generateTree() {
    // 1、准备生成树形结构的JSON数据,通过Ajax请求获取数据
    $.ajax({
        url: "menu/get/whole/tree.json",
        type: "post",
        dataType: "json",
        success:function (response) {

            var result = response.result;
            if (result == "SUCCESS") {
                // 2、创建JSON对象用于存储对zTree所做的设置，每生成一个节点就调用一次里面的设置
                var setting = {
                    // 修改节点的视图效果
                    "view": {
                        // 修改默认图标为自定义图标，调用myAddDiyDom()自定义函数
                        "addDiyDom": myAddDiyDom,
                        // 设置节点添加和删除按钮组，调用下面的自定义函数
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    // 设置点击节点不跳转页面的默认行为，把url属性的值设置为不存在属性的值
                    "data": {
                        "key": {
                            "url": "nothing"
                        }
                    }
                };
                // 3、从响应体中获取用来生成树形结构的JSON数据
                var zNodes = response.data;

                // 4、初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if (result == "FAILED") {
                layer.msg("操作失败！" + response.message);
            }
        }
    });
}


// 把默认图标该为自定义图标
// treeId是整个树形结构附着的ul标签的id，
// treeNode是树形结构各个节点，对应每一个menu对象，包括里面自带的属性
function myAddDiyDom(treeId,treeNode) {

    // 图标样式是由其中的span标签设置的
    // 获取节点图标对应span标签的id
    // id是由“ul标签的id_当前节点的序号_ico”组成，
    // 其中“ul标签的id_当前节点的序号”可以通过treeNode的tId属性获取
    var spanId = treeNode.tId + "_ico";

    // 根据id删除span标签的class的默认样式，再添加menu对象的icon属性的样式
    $("#" + spanId).removeClass().addClass(treeNode.icon);

}

// 设置鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {

    // 设置按钮组的span标签的id，也方便后面删除按钮组
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 因为鼠标进入节点范围，会多次触发该添加函数，
    // 而删除函数只会触发一次，所以需要判断，设置只触发一次
    if ($("#" + btnGroupId).length > 0) {
        return;
    }

    // 设置按钮组各个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 获取当前节点的级别
    var level = treeNode.level;

    // 声明一个空字符串用于存储组合好的按钮代码
    var btnHTML = "";
    // 根据节点的级别来添加按钮,0为根节点，1为父节点，2为子节点
    if (level == 0) {
        btnHTML = addBtn;
    }
    if (level == 1) {
        btnHTML = addBtn + " " + editBtn;
        // 判断当前节点是否有子节点,没有子节点就可以添加删除按钮
        if (treeNode.children.length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }
    if (level == 2) {
        btnHTML = editBtn + " " + removeBtn;
    }


    // 获取节点对应的超链接
    // 节点对应超链接的id为“ul标签的id_当前节点的序号_a”组成
    var anchoId = treeNode.tId + "_a";

    //在节点所在的超链接的后面添加span标签
    $("#"+anchoId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");
}

// 设置鼠标移入节点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {

    // 获取按钮组的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 移除对应的元素
    $("#" + btnGroupId).remove();
}