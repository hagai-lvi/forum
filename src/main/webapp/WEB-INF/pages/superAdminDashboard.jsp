<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 6/22/15
  Time: 2:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Super Admin Dashboard</title>
  <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<form:form action="addForum" method="POST" >
  <h1>Create new Forum:</h1><br/>
  <input type="text" placeholder="Forum Name"  id="forumName" name="forumName" /><br/>
  <input type="number" placeholder="Maximal Number Of Moderators" name="numOfModerators"/><br/>
  <input type="text" placeholder="Password Regex"  name="passRegex"/><br/>
  <input type="submit" value="Create forum" /><br/>
</form:form>
</body>
</html>
