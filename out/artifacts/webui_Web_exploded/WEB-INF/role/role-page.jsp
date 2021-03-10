<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/page/include-head.jsp" %>

<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<%--导入自定义的js文件--%>
<script type="text/javascript" src="crowdjs/my-role.js"></script>
<script type="text/javascript">

    $(function () {

        // 1、为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        // 2、调用自定义的js文件中的执行分页的函数，显示分页效果
        generatePage();

        // 3、给查询按钮绑定单击响应函数
        $("#searchBtn").click(function () {

            // 把输入关键字文本数值赋值给对应的全局变量
            window.keyword = $("#keywordInput").val();

            // 调用自定义的分页函数
            generatePage();
        });

        // 4、点击新增按钮打开模态框
        $("#showAddModalBtn").click(function () {

            $("#addModal").modal("show");
        });

        // 5、给新增模态框中的保存按钮绑定单击函数
        $("#saveRoleBtn").click(function () {

            // 获取文本框中的输入的角色名称
            /**
             * #addModal表示找到整个模态框
             * #addModal和[name=roleName]之间的空格表示在后代元素中查找
             * [name=roleName]表示name=roleNaem的匹配元素
             * trim表示去掉文本前后空格
             */
            var roleName = $.trim($("#addModal [name=roleName]").val());

            // 发送Ajax请求
            $.ajax({
                url: "role/save.json",
                type: "post",
                data: {
                    "name": roleName
                },
                dataType: "json",
                success: function (response) {

                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        // 重新加载分页,跳转到最后一页
                        window.pageNum = 999999;
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框
            $("#addModal [name=roleName]").val("");
        });

        // 6、点击更新按钮(铅笔图标)打开模态框
        // 注意：传统的click()点击事件函数只有第一页生效，下一页就失效了
        // 所以使用jQuery对象的on()函数绑定<tbody>标签中的动态元素的点击事件
        // 参数：事件类型，元素选择器，事件的响应函数
        $("#rolePageBody").on("click",".pencilBtn",function () {

            // 打开模态框
            $("#editModal").modal("show");

            // 获取回显数据（"铅笔"按钮的父标签是td,该td的上一个td的文本内容roleName就是回显数据）
            var roleName = $(this).parent().prev().text();

            // 获取当前角色的id，并设置为全局变量(在id属性中就已经设置值为roleId,所以可以直接获取)
            window.roleId = this.id;

            // 回显数据到文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        // 7、给模态框中的更新按钮绑定单击函数
        $("#updateRoleBtn").click(function () {

            // 获取文本框中新的角色名称
            var roleName = $("#editModal [name=roleName]").val();

            // 发送Ajax请求
            $.ajax({
                url: "role/update.json",
                type: "post",
                data: {
                    "id": window.roleId,
                    "name": roleName
                },
                dataType: "json",
                success: function (response) {

                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        // 重新加载分页
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                    }
                },
                error: function (response) {

                    layer.msg(response.status + " " + response.statusText);
                }
            });

            // 关闭模态框
            $("#editModal").modal("hide");
        });

        // 8、点击确认删除模态框中的确认按钮执行删除
        $("#removeRoleBtn").click(function () {

            // 把全局变量数组的josn对象转为json字符串
            var requestBody = JSON.stringify(window.roleIdArray);

            $.ajax({
                url: "role/remove/by/role/id/array.json",
                type: "post",
                data: requestBody,
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (response) {

                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        // 当使用全选框进行批量删除后还保留选中状态，所以去掉选中
                        $("#summaryBox").prop("checked", false);

                        // 重新加载分页
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                    }
                },
                error: function (response) {

                    layer.msg(response.status + " " + response.statusText);
                }
            });



            // 关闭模态框
            $("#confirmModal").modal("hide");


        });

        // 9、给单条删除按钮绑定单击响应函数
        $("#rolePageBody").on("click",".removeBtn",function () {

            // "叉号"按钮的父标签就是td，该td的上一个td的文本内容就是role对象的name值
            var roleName = $(this).parent().prev().text();

            // 把单条数据的role对象放入数组
            var roleArray = [{
                roleId: this.id,
                roleName:roleName
            }];

            // 调用自定义的函数打开模态框
            showConfirmModal(roleArray);
        });

        // 10、给全选框的checkbox绑定单击函数
        $("#summaryBox").click(function () {

            // 获取当前全选框的选取状态（true或者false）
            var currentStatus = this.checked;

            // 设置当前页数据的复选框跟全选框统一状态
            $(".itemBox").prop("checked", currentStatus);

        });

        // 11、给数据的复选框绑定全选框的反向操作函数
        // (数据中有一个复选框不选，全选框就不选，所有数据的复选框都选中，全选框也跟着选上）
        $("#rolePageBody").on("click", ".itemBox", function () {

            // 获取当前所有数据的复选框的数量
            var totalBoxCount = $(".itemBox").length;

            // 获取当前所有数据的复选框选中的数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 设置全选框的状态只有全选状态才选上，有一个不选中，全选框也不选
            $("#summaryBox").prop("checked", totalBoxCount == checkedBoxCount);
        });

        // 12、给批量删除绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 创建一个空数组
            var roleArray = [];

            // 遍历当前选中的复选框
            $(".itemBox:checked").each(function () {

                // 获取当前复选框的id
                var roleId = this.id;

                // 当前复选框的父标签就是td,该td下一个td的文本内容就是role对象的name值
                var roleName = $(this).parent().next().text();

                // 把遍历到的role对象放入数组
                roleArray.push({
                    roleId: roleId,
                    roleName: roleName
                });
            });

            // 检查roleArray数组的长度是否为0
            if (roleArray.length == 0) {
                layer.msg("请至少选择一个执行删除");
                return;
            }

            // 调用自定义的函数打开模态框
            showConfirmModal(roleArray);
        });


        //13、给“绿色勾”分配权限按钮绑定单击响应函数
        $("#rolePageBody").on("click", ".checkBtn", function () {

            // 把当前角色id存入全局变量
            window.roleId = this.id;

            // 打开模态框
            $("#assignModal").modal("show");

            fillAuthTree();
        });

        // 14、给角色分配权限的模态框中的保存按钮添加单击响应函数
        $("#assignBtn").click(function () {

            // 准备一个用于获取树形结构中的被勾选的节点的authId的空数组
            var authIdArray = [];

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // 获取全部被勾选的节点组成的数组
            var checkedNodes = zTreeObj.getCheckedNodes();

            // 遍历节点数组
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];

                // 获取节点的id
                var authId = checkedNode.id;
                // 把节点id存入准备好的数组中
                authIdArray.push(authId);
            }

            // 准备JSON数据
            var requestBody = {
                "authIdArray": authIdArray,
                // 为了统一接收请求，统一类型，把authId也存入数组
                "roleId": [window.roleId]
            };
            requestBody = JSON.stringify(requestBody);

            // 发送Ajax请求执行保存
            $.ajax({
                url: "assign/do/role/assign/auth.json",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                error:function (response) {
                    layer.msg(response.start() + " " + response.statusText);
                }
            });

            // 关闭模态框
            $("#assignModal").modal("hide");
        });
    });
</script>

<body>

<%@include file="/WEB-INF/page/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/page/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;">
                        <i class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <%--这里显示分页导航条--%>
                                    <div id="Pagination" class="pagination"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/modal/modal-role-confirm.jsp"%>
<%@include file="/WEB-INF/modal/modal-role-assign-auth.jsp"%>
</body>
</html>