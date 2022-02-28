<%--
  Created by IntelliJ IDEA.
  User: welch
  Date: 2022/1/24
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <a href="logout">退出登录</a>
</div>
<div>
<%--    askID:<span>${anno.taskID}</span><br>--%>
<%--    userTaskID:<span>${anno.userTaskID}</span><br>--%>
    数据集:<span>${anno.datasetTableName}</span><br>
<%--    标注结果表:<span>${anno.resultTableName}</span><br>--%>
    第<span>${anno.currentAnnoIndex}</span>条数据<br>
<%--    当前内容id：<span>${anno.textID}</span><br>--%>
    内容：<br>
    <textarea style="height: 350px; width: 700px">
        ${anno.currentText}
    </textarea><br>
    <a href="submitAnnoResult?datasetTableName=${anno.datasetTableName}&resultTableName=${anno.resultTableName}&textID=${anno.textID}&userAccount=${sessionScope.get("user").account}&text=${anno.currentText}&label=这是标注结果&userTaskID=${anno.userTaskID}&currentAnnoIndex=${anno.currentAnnoIndex}&taskID=${anno.taskID}">提交标注结果，并返回下一条</a>
</div>

</body>
</html>
