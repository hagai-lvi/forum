<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>

  <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<ul>
  <c:forEach var="entry" items="${sessionEntries}">
    <li>
      "${entry}"
    </li>
  </c:forEach>
</ul>
</body>
</html>
