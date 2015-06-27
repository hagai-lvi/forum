<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/25/15
  Time: 2:40 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${forumName}</title>
    <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<h1>Hello ${user}, welcome to ${forumName}</h1>
<table>
    <tr>
        <td>
            <myTags:logout></myTags:logout>
        </td>
        <td>
            <label>User role:${userStatus}</label>
            <a href="<c:url value="facade"/>">Click here to go back to Facade page</a>
        </td>
    </tr>
</table>
<%-- Forum admin dashboard --%>
<br/>
<br/>
<hr/>
<br/>
<br/>
<c:if test="${isAdmin}">
    <form:form action="addSubforum" method="post">
        <h1>Create new Sub Forum</h1>
        <label>
            <span>Sub Forum Name:</span>
            <input type="text" name="subforumName" placeholder="Sub Forum Name" />
        </label>

        <label>
            <span>&nbsp;</span>
            <input type="submit" class="button" value="Create" />
        </label>
    </form:form>
</c:if>

<br/>
<br/>
<hr/>
<br/>
<br/>

<table >
    <tr>

        <c:forEach var="subforum" items="${subforumsList}">
            ${subforum.getTitle()}

            <form action="subforum_homepage" method="POST">
                <input type="submit" class="button" value="${subforum.getTitle()}" name="subforumName" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>


</body>
</html>
