<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%--通过base标签设置该页面的绝对路径--%>
    <%--http://localhost:8080/webui/test/ssm.html--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">

        $(function () {
           $("#btn1").click(function () {
                /*使用ajax处理请求*/
               $.ajax({
                   url: "send/array/one.html",
                   type: "post",
                   data: {
                       "array": [5,8,12]
                   },
                   dataType: "text",
                   // 服务端成功处理请求后调用的回调函数
                   // response是响应体数据
                   success:function (response) {
                       alert(response);
                   },
                   // 服务端处理请求失败后调用的回调函数
                   error:function (response) {
                       alert(response);
                   }
               });

           });

            $("#btn2").click(function () {
                $.ajax({
                    url: "send/array/two.html",
                    type: "post",
                    data: {
                        "array[0]":5,
                        "array[1]":8,
                        "array[2]":12
                    },
                    dataType: "text",
                    success:function (response) {
                        alert(response);
                    },
                    error:function (response) {
                        alert(response);
                    }
                });
            });

            $("#btn3").click(function () {
                // 先准备好要发送的数组
                var array = [5, 8, 12];
                // 吧json数组转为字符串
                var requestBody = JSON.stringify(array);
                $.ajax({
                    url: "send/array/three.html",
                    type: "post",
                    data: requestBody,
                    //请求体的内容类型
                    contentType: "application/json;charset=UTF-8",
                    dataType: "text",
                    success:function (response) {
                        alert(response);
                    },
                    error:function (response) {
                        alert(response);
                    }
                });
            });

            $("#btn4").click(function () {
                // 先准备要发送的json对象
                var student = {
                    "stuId":5,
                    "stuName":"tom",
                    "address":{
                        "province":"广东",
                        "city":"广州",
                        "street":"北京路"
                    },
                    "subjectList":[
                        {
                        "subName":"JavaSE",
                        "subScore":90
                        },{
                        "subName":"JavaWeb",
                        "subScore":80
                        }
                    ],
                    "map":{
                        "k1":"v1",
                        "k2":"v2"
                    }
                };

                // 将JSON对象转为JSON字符串
                var requestBody = JSON.stringify(student);

                // 发送ajax请求
                $.ajax({
                    url:"send/compose/object.json",
                    type:"post",
                    data:requestBody,
                    contentType: "application/json;charset=UTF-8",
                    dataType:"json",
                    success:function (response) {
                        console.log(response);
                    },
                    error:function (response) {
                        console.log(response);
                    }
                });
            });

            $("#btn5").click(function () {
                layer.msg("Layer的弹框")
            });
        });

    </script>
</head>
<body>

    <a href="test/ssm.html">测试SSM整合环境</a>

    <br/>

    <button id="btn1">Send [5,8,12] One</button>

    <br/>
    <br/>
    <button id="btn2">Send [5,8,12] Two</button>
    <br/>
    <br/>
    <button id="btn3">Send [5,8,12] Three</button>
    <br/>
    <br/>
    <button id="btn4">Send Compose Object</button>

    <br/>
    <br/>
    <button id="btn5">点我弹layer弹框</button>

    <br/>
    <br/>
    <a href="${pageContext.request.contextPath}/admin/to/login/page.html">登录页面</a>
</body>
</html>
