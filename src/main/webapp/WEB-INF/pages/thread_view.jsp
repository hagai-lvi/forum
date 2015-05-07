<%--
  User: hagai_lvi
  Date: 4/29/15
  Time: 9:00 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Thread ${thread.getRootMessage().getMessageTitle()}</h1>


<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3><c:out value="${node.data.getMessageTitle()}"/> :</h3>
<c:out value="${node.data.getMessageText()}"/>
<form action="reply_to_message" method="post">
    <input hidden name="messageID" value=${node.data.getId()}>
    <input type="submit" value="Add reply"/>
</form>
<myTags:threadTree node="${root}"/>


</body>
</html>
