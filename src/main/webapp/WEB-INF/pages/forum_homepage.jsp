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

<a href="<c:url value="facade"/>">Click here to go back to Facade page</a>

<h2>${possibleMsg}</h2> <%-- if there is a message such as login or register welcome message - display it here --%>
<h2>There are ${numberOfSubforums} subforums</h2>

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
</c:if>



<table >
    <tr>

        <c:forEach var="subforum" items="${subforumsList}">
            ${subforum.getName()}

            <form action="subforum_homepage" method="POST">
                <input type="submit" value=${subforum.getName()} name="subforumName" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>





</body>
</html>
