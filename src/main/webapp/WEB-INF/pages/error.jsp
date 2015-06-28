<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ERROR</title>
  <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body bgcolor="#f08080" >
<h1 style="color: darkblue"> An error has occured</h1>
<h2 style="color: darkblue">Error Message: ${error_message}</h2>
<h2 style="color: darkblue">Error URL: ${url}</h2>
<img src="http://www.paulcastro.com/wp-content/uploads/2010/06/404_yo.jpg" alt="Error occured" align="middle">
<%--<c:forEach items="${exception.stackTrace}" var="element">--%>
  <%--<c:out value="${element}"/><br/>--%>
<%--</c:forEach>--%>

</body>
</html>
