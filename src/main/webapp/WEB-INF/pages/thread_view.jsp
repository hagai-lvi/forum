<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/29/15
  Time: 9:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
</head>
<body>
<h1>Thread ${thread.getRootMessage().getMessageTitle()}</h1>


<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3><c:out value="${node.data.getMessageTitle()}"/> :</h3>
<c:out value="${node.data.getMessageText()}"/>
<myTags:threadTree node="${root}"/>


</body>
</html>
