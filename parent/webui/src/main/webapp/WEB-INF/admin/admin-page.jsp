<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/page/include-head.jsp" %>

<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript">

    $(function () {

        // 调用函数，对页面导航条进行初始化
        initPagination();

        $("#summaryBox").click(function () {

            // 获取当前全选框的选取状态（true或者false）
            var currentStatus = this.checked;

            // 设置当前页数据的复选框跟全选框统一状态
            $(".itemBox").prop("checked", currentStatus);

        });

        $(".itemBox").click(function () {

            // 获取当前所有数据的复选框的数量
            var totalBoxCount = $(".itemBox").length;

            // 获取当前所有数据的复选框选中的数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 设置全选框的状态只有全选状态才选上，有一个不选中，全选框也不选
            $("#summaryBox").prop("checked", totalBoxCount == checkedBoxCount);
        });

        $("#batchRemoveBtn").click(function () {

            if (confirm("确认删除所选用户信息吗？")){
                // 创建一个空数组
                var adminIdArray = [];

                // 遍历当前选中的复选框
                $(".itemBox:checked").each(function () {

                    // 获取当前复选框的id
                    var adminId = this.id;

                    // 把遍历到的role对象放入数组
                    adminIdArray.push(adminId);
                });

                // 检查adminArray数组的长度是否为0
                if (adminIdArray.length == 0) {
                    layer.msg("请至少选择一个执行删除");
                    return;
                }

                // 把全局变量数组的josn对象转为json字符串
                var requestBody = JSON.stringify(adminIdArray);

                $.ajax({
                    url: "admin/remove/by/admin/id/array.json",
                    type: "post",
                    data: requestBody,
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success: function (response) {

                        var result = response.result;

                        if (result == "SUCCESS") {


                            // 当使用全选框进行批量删除后还保留选中状态，所以去掉选中
                            $("#summaryBox").prop("checked", false);

                            // 重新加载分页
                            window.location.href = "admin/get/page.html?pageNum=${param.pageNum}&keyword=${param.keyword}";
                            alert("操作成功！");

                        }
                        if (result == "FAILED") {
                            layer.msg("操作失败！"+response.message);
                        }
                    },
                    error: function (response) {

                        layer.msg(response.status + " " + response.statusText);
                    }
                });

            }

        });
    });


    // 声明一个函数用于初始化 Pagination
    function initPagination() {

        // 获取总记录数
        var totalRecord = ${requestScope.pageInfo.total}

        // 声明一个JSON对象存储Pagination要设置的属性
        var properties = {
            // 边缘页码数
            "num_edge_entries": 3,
            // 主体页码数
            "num_display_entries": 5,
            // 每页显示数
            "items_per_page": ${requestScope.pageInfo.pageSize},
            // 当前选中页面(Pagination使用pageIndex管理页码，索引从0开始，而pageNum从1开始)
            "current_page": ${requestScope.pageInfo.pageNum - 1},
            // 设置上一页的文本
            "prev_text": "上一页",
            // 设置下一页的文本
            "next_text": "下一页",
            // 回调函数(指定用户点击“翻页”的按钮时跳转页面的回调函数）
            "callback": pageselectCallback,
        };

        // 生成页码导航条
        $("#Pagination").pagination(totalRecord,properties);
    }

    // 用户点击“上一页，1，2，3...”这样的页码时调用这个函数进行页面跳转
    // pageIndex是Pagination传过来的索引从0开始的页码
    function pageselectCallback(pageIndex,jQuery) {

        // 根据PageIndex计算得到pageNum
        var pageNum = pageIndex + 1;

        // 跳转页面
        window.location.href = "admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";

        // 由于每个页码按钮都是超链接，所以要取消超链接的默认行为
        return false;
    }


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
                    <form action="admin/get/page.html" method="post" class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input name="keyword" class="form-control has-success" type="text" value="${param.keyword}" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" id="batchRemoveBtn" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <%--<button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='add.html'"><i class="glyphicon glyphicon-plus"></i> 新增</button>--%>
                    <a href="admin/to/add/page.html" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</a>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${empty requestScope.pageInfo.list}">
                                <tr>
                                    <td colspan="6" align="center">抱歉！没有查询到你要的数据！</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty requestScope.pageInfo.list}">
                                <c:forEach items="${requestScope.pageInfo.list}" var="admin" varStatus="myStatus">
                                    <tr>
                                        <td>${myStatus.count}</td>
                                        <td><input type="checkbox" id="${admin.id}" class="itemBox"></td>
                                        <td>${admin.loginAcct}</td>
                                        <td>${admin.userName}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                            <%--<button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>--%>
                                            <a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></a>

                                            <%--<button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>--%>
                                            <a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>

                                            <%--<button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>--%>
                                            <a href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html" onclick="return confirm('确认删除该用户信息吗？')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                            <tfoot>
                            <tr >
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
</body>
</html>