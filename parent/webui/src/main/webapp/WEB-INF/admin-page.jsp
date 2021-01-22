<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>

<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript">

    $(function () {

        // 调用函数，对页面导航条进行初始化
        initPagination();

    });

    // 声明一个函数用于初始化 Pagination
    function initPagination() {

        // 获取总记录数
        var totalRecord = ${requestScope.pageInfo.total}

        // 声明一个JSON对象存储Pagination要设置的属性
        var properties = {
            // 边缘页码数
            num_edge_entries: 3,
            // 主体页码数
            num_display_entries: 5,
            // 每页显示数
            items_per_page: ${requestScope.pageInfo.pageSize},
            // 当前选中页面(Pagination使用pageIndex管理页码，索引从0开始，而pageNum从1开始)
            current_page: ${requestScope.pageInfo.pageNum - 1},
            // 设置上一页的文本
            prev_text: "上一页",
            // 设置下一页的文本
            next_text: "下一页",
            // 回调函数(指定用户点击“翻页”的按钮时跳转页面的回调函数）
            callback: pageselectCallback,
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

    //使用Ajax请求处理数据的删除
    function remove(id,pageNum,keyword) {
        // 如果搜索索引未定义，则转为空串
        if (typeof (keyword) == "undefined") {
            var keyword = "";
        }
        // 弹出确认提示框
        if (confirm("确定要删除该用户信息吗？")) {

            $.ajax({
                url: "admin/remove/page.html",
                type: "post",
                data: {"id":id},
                dataType:"text",
                success:function () {
                    window.location.href = "admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
                    alert("删除成功！");
                },
                error:function () {
                    alert("删除失败！");
                }
            });
        }
    }

</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/include-sidebar.jsp" %>

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
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <%--<button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='add.html'"><i class="glyphicon glyphicon-plus"></i> 新增</button>--%>
                    <a href="admin/to/add/page.html" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</a>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
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
                                        <td><input type="checkbox"></td>
                                        <td>${admin.loginAcct}</td>
                                        <td>${admin.userName}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                            <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>

                                            <%--<button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>--%>
                                            <a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>

                                            <button type="button" onclick="remove(${admin.id},${requestScope.pageInfo.pageNum},${param.keyword})" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
                                            <%--<a href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></a>--%>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <%--这里显示分页--%>
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