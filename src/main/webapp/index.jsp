<%--
  Created by IntelliJ IDEA.
  User: welch
  Date: 2022/1/17
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<%--    <base href="${pageContext.request.contextPath}">--%>
</head>
<body>
当前路径：<p>${pageContext.request.contextPath}</p>
<a href="login.jsp">用户登录</a>
<a href="">创建任务配置</a>
<a href="">创建任务</a>
<a href="">分配任务</a>
<a href="">查看任务</a>
<a href="">开始标注任务</a>
<a href="">提交标注结果</a>
</body>
</html>