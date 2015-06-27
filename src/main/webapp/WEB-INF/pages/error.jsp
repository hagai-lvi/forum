<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ERROR</title>
  <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<h1> An error has occured</h1>
<h2>Error Message: ${error_message}</h2>
<h2>Error URL: ${url}</h2>

<c:forEach items="${exception.stackTrace}" var="element">
  <c:out value="${element}"/><br/>
</c:forEach>

</body>
</html>
