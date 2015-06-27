<%--
  User: hagai_lvi
  Date: 4/29/15
  Time: 9:00 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>

<html>
<head>
    <title></title>
    <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<h1>Thread ${node.getData().getMessageText()}</h1>

<myTags:logout></myTags:logout>

<form method="get" action="subforum_homepage">
    <input type="submit" class="button" value="Back to subforum homepage" >
</form>



<h3><c:out value="${node.data.getMessageTitle()}"/> :</h3>
<c:out value="${node.data.getMessageText()}"/><br/>
<c:out value="user: ${node.data.getUser()}"/>
<c:if test="${not isGuest}">
    <form action="reply_to_message" method="post">
        <input hidden name="messageID" value=${node.data.getId()}>
        <input type="submit" class="button" value="Add reply"/>
    </form>
</c:if>
<myTags:threadTree node="${node}" isGuest="${isGuest}"/>


</body>
</html>
