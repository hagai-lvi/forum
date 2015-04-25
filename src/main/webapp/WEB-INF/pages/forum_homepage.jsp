<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<a href="<c:url value="facade"/>">Click here to go back to facade page</a>

<h2>${possibleMsg}</h2> <%-- if there is a message such as login or register welcome message - display it here --%>
<h2>There are ${numberOfSubforums} subforums</h2>



</body>
</html>
