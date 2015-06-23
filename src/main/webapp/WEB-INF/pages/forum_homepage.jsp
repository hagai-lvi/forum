<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
</head>
<body>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<h1>Hello ${user}, welcome to ${forumName}</h1>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<myTags:logout></myTags:logout>
<label>${userStatus}</label>
<a href="<c:url value="facade"/>">Click here to go back to Facade page</a>

<c:if test="${isAdmin}">
    <form:form action="addSubforum" method="post">
        <h1>Create new Sub Forum</h1>
        <label>
            <span>Sub Forum Name :</span>
            <input type="text" name="subforumName" placeholder="Sub Forum Name" />
        </label>

        <label>
            <span>&nbsp;</span>
            <input type="submit" class="button" value="Create" />
        </label>
    </form:form>
    <form:form>
        <h1>Add moderator to sub forum</h1>
        <label>
            <span>Sub forum name:</span>
            <input type="text" name="subforumName" placeholder="Sub Forum Name"/>
        </label>
        <label>
            <span>Moderator name:</span>
            <input type="text" name="moderatorName" placeholder="Moderator Name"/>
        </label>
    </form:form>
</c:if>



<table >
    <tr>

        <c:forEach var="subforum" items="${subforumsList}">
            ${subforum.getTitle()}

            <form action="subforum_homepage" method="POST">
                <input type="submit" value="${subforum.getTitle()}" name="subforumName" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>





</body>
</html>
