<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/29/15
  Time: 8:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ERROR</title>
</head>
<body>
<h1> An error has occured</h1>
<h2>Error Message: ${error_message}</h2>
<h2>Error URL: ${url}</h2>

<c:forEach items="${exception.stackTrace}" var="element">
  <c:out value="${element}"/><br/>
</c:forEach>

</body>
</html>
