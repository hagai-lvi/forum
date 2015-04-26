<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/18/15
  Time: 4:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Forum ${forumName} added</h1>
<a href="<c:url value="facade"/>">Click here to go back to facade page</a>



<h1>Forum list: ${forumList} </h1>
</body>
</html>
