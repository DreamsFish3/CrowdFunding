<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/page/include-head.jsp" %>

<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowdjs/my-menu.js"></script>
<script type="text/javascript">

    $(function () {

        // 1、调用专门封装好的函数初始化树形结构的菜单维护
        generateTree();

        // 2、给按钮组的添加子节点按钮绑定单击响应函数
        $("#treeDemo").on("click", ".addBtn", function () {

            // 把当前节点的id作为新添加的节点的pid保存到全局变量中
            window.pid = this.id;

            // 打开模态框
            $("#menuAddModal").modal("show");

            return false;
        });

        // 3、给添加子节点的模态框中的保存按钮绑定单击响应函数
        $("#menuSaveBtn").click(function () {

            // 获取表单中对象属性的值
            var name = $("#menuAddModal [name=name]").val();
            var url = $("#menuAddModal [name=url]").val();
            var icon = $("#menuAddModal [name=icon]:checked").val();

            // 发送Ajax请求
            $.ajax({
                url: "menu/save.json",
                type: "post",
                data: {
                    "pid": window.pid,
                    "name": name,
                    "url": url,
                    "icon": icon
                    },
                dataType: "json",
                success:function (response) {
                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        // 刷新页面
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (respnse) {
                    layer.msg(respnse.status + " " + respnse.statusTest);
                }
            });

            // 关闭模态框
            $("#menuAddModal").modal("hide");

            // 清空上一次添加的模态框上残留的数据,点击重置按钮
            $("#menuResetBtn").click();
        });

        // 4、给按钮组的更新节点按钮绑定单击响应函数
        $("#treeDemo").on("click",".editBtn",function () {

            // 把当前节点的id保存到全局变量中
            window.id = this.id;

            // 打开模态框
            $("#menuEditModal").modal("show");

            // 获取zTreeObj对象（使用ztree的js函数来获取当前节点对象，从而回显数据）
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            //key：用来搜索节点的属性名
            var key = "id";
            //value：用来搜索节点的属性值
            var value = window.id;
            // 根据id获取当前节点对象
            var currentNode = zTreeObj.getNodeByParam(key, value);

            // 回显数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);
            // radio回显的本质是遍历所有的icon，找到icon中所有跟数组中值对应的radio，然后选中
            // radio被选中是根据数组中的值决定的，所以传入的值要为数组形式
            $("#menuEditModal [name=icon]").val([currentNode.icon]);

            return false;
        });

        // 5、给更新节点的模态框中的更新按钮绑定单击响应函数
        $("#menuEditBtn").click(function () {

            // 获取表单中对象属性的值
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();

            // 发送Ajax请求
            $.ajax({
                url: "menu/update.json",
                type: "post",
                data: {
                    "id": window.id,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                dataType: "json",
                success:function (response) {
                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        // 刷新页面
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (respnse) {
                    layer.msg(respnse.status + " " + respnse.statusTest);
                }
            });

            // 关闭模态框
            $("#menuEditModal").modal("hide");
        });

        // 6、给按钮组的删除按钮绑定单击响应函数
        $("#treeDemo").on("click", ".removeBtn", function () {

            // 把当前节点的id保存到全局变量中
            window.id = this.id;

            // 打开模态框
            $("#menuConfirmModal").modal("show");

            // 获取zTreeObj对象（使用ztree的js函数来获取当前节点对象，从而回显数据）
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            //key：用来搜索节点的属性名
            var key = "id";
            //value：用来搜索节点的属性值
            var value = window.id;
            // 根据id获取当前节点对象
            var currentNode = zTreeObj.getNodeByParam(key, value);

            // 回显菜单图标和名称确认文本上
            $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'></i>" + currentNode.name+"】");

            return false;
        });

        // 7、给删除节点的模态框中的确认按钮绑定单击响应函数
        $("#confirmBtn").click(function () {

            // 发送Ajax请求
            $.ajax({
                url: "menu/remove.json",
                type: "post",
                data: {
                    "id": window.id
                },
                dataType: "json",
                success:function (response) {
                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        // 刷新页面
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (respnse) {
                    layer.msg(respnse.status + " " + respnse.statusTest);
                }
            });

            // 关闭模态框
            $("#menuConfirmModal").modal("hide");
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
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <%--这个ul标签是zTree动态生成的节点所依附的静态节点--%>
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal/modal-menu-add.jsp"%>
<%@include file="/WEB-INF/modal/modal-menu-edit.jsp"%>
<%@include file="/WEB-INF/modal/modal-menu-confirm.jsp"%>
</body>
</html>