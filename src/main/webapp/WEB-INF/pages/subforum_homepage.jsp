<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/28/15
  Time: 6:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title><%--TODO--%></title>
</head>
<body>


<%--TODO--%>
<form:form action="addThread" method="post">
    <h1>Create New Thread</h1>
    <label>
        <span>Title :</span>
        <input type="text" name="srcMsgTitle" placeholder="Title" />
    </label>

    <label>
        <span>Message Body :</span>
        <input type="text" name="srcMsgBody" placeholder="Message body" />
    </label>

    <label>
        <span>&nbsp;</span>
        <input type="submit" class="button" value="Create" />
    </label>
</form:form>


<%--TODO--%>
<table >
    <tr>

        <c:forEach var="thread" items="${threadsList}">
            ${thread.getRootMessage().getMessageTitle()}

            <form action="thread_view" method="GET">
                <input type="submit" value="${thread.getTitle()}" name="threadID" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>



</body>
</html>
