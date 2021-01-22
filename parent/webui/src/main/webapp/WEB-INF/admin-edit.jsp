<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>

<script type="text/javascript">
        function update() {

            $.ajax({
                url:"admin/update.html",
                type:"post",
                data:$("#updateForm").serialize(),
                success:function (response) {
                    /*
                    * 判断返回的JSON字符串是否为空，为空表示没有异常信息，更新成功,
                    * 不为空则表示有异常信息返回
                    * */
                    if (response === "") {
                        alert("更新成功");
                        window.location.href = "admin/get/page.html?pageNum=${param.pageNum}&keyword=${param.keyword}";
                    } else {
                        // 把JSON字符串转为对象,并获取message的属性
                        var message = JSON.parse(response).message;
                        var html = "" + message;
                        // 把信息写入<p>标签中显示出来
                        $("#html").html(html);
                    }

                },
                error:function () {
                    alert("更新失败");
                }

            });
        }
</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">更新</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <form id="updateForm" <%--action="admin/update.html" method="post"--%> role="form">
                        <input type="hidden" name="id" value="${requestScope.admin.id}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <p id="html" style="color: #b92c28"></p>
                        <div class="form-group">
                            <label for="exampleInputLoginAcct">登陆账号</label>
                            <input type="text" name="loginAcct" value="${requestScope.admin.loginAcct}" class="form-control" id="exampleInputLoginAcct" placeholder="请输入登陆账号">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputUsername">用户昵称</label>
                            <input type="text" name="userName" value="${requestScope.admin.userName}" class="form-control" id="exampleInputUsername" placeholder="请输入用户昵称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail">邮箱地址</label>
                            <input type="email" name="email" value="${requestScope.admin.email}" class="form-control" id="exampleInputEmail" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <%--<button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 更新</button>--%>
                        <button type="button" onclick="update()" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 更新</button>

                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>