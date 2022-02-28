<%--
  Created by IntelliJ IDEA.
  User: welch
  Date: 2022/1/24
  Time: 13:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <a href="logout">退出登录</a>
</div>
<c:forEach items="${myTaskList}" var="usertask">
    <div>
        id:<span>${usertask.id}</span><br>
        taskID:<span>${usertask.taskID}</span><br>
        任务名称：<span>${usertask.taskName}</span><br>
        用户账号：<span>${usertask.userAccount}</span><br>
        当前标注数据index：<span>${usertask.currentAnnoIndex}</span><br>
        <a href="startAnno?userTaskID=${usertask.id}&taskID=${usertask.taskID}&currentIndex=${usertask.currentAnnoIndex}">开始标注</a>
    </div>
</c:forEach>


</body>
</html>
