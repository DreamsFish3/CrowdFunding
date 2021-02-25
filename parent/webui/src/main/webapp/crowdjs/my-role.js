// 执行分页，生成分页效果，任何时候调用这个函数都会重新加载页面
function generatePage() {

    // 1、获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2、填充表格
    fillTableBody(pageInfo);

}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {

    // 获取ajax请求的对象
    var ajaxResult = $.ajax({
        url: "role/get/page/info.json",
        type: "post",
        data: {
          "pageNum" : window.pageNum,
          "pageSize" : window.pageSize,
          "keyword" : window.keyword
        },
        dataType: "json",
        async:false,
    });

    console.log(ajaxResult);

    /**
     * 分两步从响应中判断本次整个请求过程是否成功
     */
    // 第一步：判断当前响应状态码是否为200
    // ajax请求对象中status属性值就是响应状态码
    if (ajaxResult.status != 200) {
        // 如果发生错误，就显示提示信息，返回null值让当前函数停止执行
        layer.msg("响应失败！状态码为" + ajaxResult.status + "说明信息为" + ajaxResult.statusText);
        return null;
    }

    // 第二步：判断返回的resultEntity中的result属性是否为FAILED
    // 如果请求成功返回，获取返回的ResultEntity对象,
    // ajax请求对象中responseJSON属性值就是JSON数据的响应体
    var resultEntity = ajaxResult.responseJSON;
    // 获取ResultEntity中的result属性
    var result = resultEntity.result;
    // 判断result是否为FAILED
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    // 确认result为SUCCESS后返回pageInfo数据
    var pageInfo = resultEntity.data;
    return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo) {

    // 因为翻页时不停追加数据，需要先清除<tbody>中的旧数据
    $("#rolePageBody").empty();
    // 根据关键字搜索查询没有数据时导航条还在显示，所以也要清除
    $("#Pagination").empty();

    // 判断返回的pageInfo对象是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        // 在<tbody>标签中追加html内容
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜索的数据！</td></tr>");
        return;
    }

    // 遍历pageInfo的list属性来填充<tbody>中的表格内容
    for (var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];

        var roleId = role.id;

        var roleName = role.name;

        var numberId = "<td>" + (i + 1) + "</td>";

        // 通过input标签的id属性(或者其他属性)把roleId值传递到批量删除按钮的单击响应函数中，利用它来获取id值
        var checkboxId = "<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>"

        var roleNameId = "<td>" + roleName + "</td>";
        // 通过button标签的id属性(或者其他属性)把roleId值传递到button按钮的单击响应函数中，利用它来进行回显数据
        var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        // 通过button标签的id属性(或者其他属性)把roleId值传递到button按钮的单击响应函数中,利用它来进行回显数据
        var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        // 通过button标签的id属性(或者其他属性)把roleId值传递到button按钮的单击响应函数中，利用它来获取id值
        var removeBtn = "<button id='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";

        var buttonId = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";

        var tr = "<tr>" + numberId + checkboxId + roleNameId + buttonId + "</tr>";

        // 在<tbody>标签中追加组合起来的html内容
        $("#rolePageBody").append(tr);
    }

    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {

    // 获取总记录数
    var totalRecord = pageInfo.total;

    // 声明一个JSON对象存储Pagination要设置的属性
    var properties = {
        // 边缘页码数
        "num_edge_entries": 3,
        // 主体页码数
        "num_display_entries": 5,
        // 每页显示数
        "items_per_page": pageInfo.pageSize,
        // 当前选中页面(Pagination使用pageIndex管理页码，索引从0开始，而pageNum从1开始)
        "current_page": pageInfo.pageNum - 1,
        // 设置上一页的文本
        "prev_text": "上一页",
        // 设置下一页的文本
        "next_text": "下一页",
        // 回调函数(指定用户点击“翻页”的按钮时跳转页面的回调函数）
        "callback": paginationCallBack,
    };

    // 生成页码导航条
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex,jQuery) {

    // 修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;

    // 调用分页函数,跳转页面
    // 注意：为什么不会这里不会产生递归死循环，因为回调函数只有在点击翻页的时候才会调用
    generatePage();

    // 取消页码的超链接的默认行为
    return false;

}

// 声明专门的函数打开确认模态框，并回显数据
function showConfirmModal(roleArray) {

    // 打开模态框
    $("#confirmModal").modal("show");

    // 先清除旧的数据
    $("#roleNameSpan").empty();

    // 定义一个全局变量用于存储id的数组
    window.roleIdArray = [];

    // 遍历roleArray
    for (var i = 0; i < roleArray.length; i++) {
        // 数组的每个值为role对象
        var role = roleArray[i];
        // 获取role对象的roleName属性
        var roleName = role.roleName;
        // 获取role对象的roleId属性
        var roleId = role.roleId;

        // 把roleName追加到模态框中的<span>标签中
        $("#roleNameSpan").append("角色"+(i+1)+"："+roleName + "<br/>");


        // 把roleId存入全局变量数组
        window.roleIdArray.push(roleId);
    }
}

// 声明专门的函数给分配Auth的模态框回显Auth的树形结构数据
function fillAuthTree() {

    /*
        显示模态框中的角色权限树形结构
    */
    // 1、发送Ajax请求查询Auth数据
    var ajaxReturn = $.ajax({
        url: "assign/get/all/auth.json",
        type: "post",
        dataType: "json",
        async: false
    });

    if (ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + "说明是：" + ajaxReturn.statusText);
        return;
    }

    // 2、从响应结果中获取Auth的JSON数据（数据并不是树形结构，可以交个zTree组装）
    var authList = ajaxReturn.responseJSON.data;

    // 3、创建JSON对象用于存储对zTree所做的设置，每生成一个节点就调用一次里面的设置
    var setting = {
        "data": {
            /*设置JSON数据使用zTree自带的simpleData功能组装成树形结构*/
            "simpleData": {
                // 开启简单JSON功能
                "enable": true,
                // 不使用默认的pid属性作为属性名，而改为categoryId属性
                // 默认为"pIdKey":"pid"
                "pIdKey": "categoryId"
            },
            "key": {
                // 不使用默认的name属性作为属性名，而改为title属性
                // 默认为"name":"name"
                "name": "title"
            }
        },
        // 设置节点前面显示checkBox
        "check": {
            "enable": true
        }
    };

    // 4、初始化树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);

    // 获取zTreeObj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用zTreeObj对象的方法，把节点展开
    zTreeObj.expandAll(true);

    /*
        回显数据，把已经关联的节点勾选
     */
    // 5、发送Ajax请求，根据roleId获取数组AuthIdArray
    ajaxReturn = $.ajax({
        url: "assign/get/assigned/auth/id/by/role/id.json",
        type: "post",
        data: {
            "roleId": window.roleId
        },
        dataType: "json",
        async: false
    });
    if (ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + "说明是：" + ajaxReturn.statusText);
        return;
    }

    var authIdArray = ajaxReturn.responseJSON.data;

    // 6、根据AuthIdArray把树形结构中对应的节点勾选上
    for (var i = 0; i < authIdArray.length; i++) {

        var authId = authIdArray[i];

        //根据id查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);

        // 节点勾选状态
        var checked = true;
        // 节点与父节点是否联动
        var checkTypeFlag = false;
        // 将treeNode设置为勾选
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }

}