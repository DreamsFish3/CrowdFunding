<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/page/include-head.jsp" %>

<script type="text/javascript">

    $(function () {

        $("#toRightBtn").click(function () {

            // 把选中的角色移到右边选择框中
            /*
            :eq(0)：表示页面上第一个
            >：表示选择子元素
            :selected：被选中的
            appendTo：把jQuery对象追加到指定的位置
            */
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        });

        $("#toLeftBtn").click(function () {

            // 把选中的角色移到左边选择框中
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        });

        $("#submitBtn").click(function () {

            // 因为提交表单只会把被选中的option提交
            // 所以在提交表单前把所有的“已分配”部分的option全部选中
            $("select:eq(1)>option").prop("selected", "selected");
        });
    });
</script>
<body>

<%@include file="/WEB-INF/page/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/page/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}"/>
                        <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" name="keyword" value="${param.keyword}"/>
                        <div class="form-group">
                            <label for="unAssignedRole">未分配角色列表</label><br>
                            <select id="unAssignedRole" class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                            <br/>
                            <button id="submitBtn" style="margin-left: 18px" type="submit" class="btn btn-lg btn-success btn-block">保存</button>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="assignedRole">已分配角色列表</label><br>
                            <select id="assignedRole" name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>