<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 5/7/15
  Time: 2:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<form:form action="thread_view" method="post">
  <h1>Add Reply</h1>
  <label>
    <span>Message Title :</span>
    <input type="text" name="newMsgTitle" placeholder="Title" />
  </label>

  <label>
    <span>Message Body :</span>
    <input type="text" name="newMsgBody" placeholder="Message body" />
  </label>

  <label>
    <span>&nbsp;</span>
    <input type="submit" class="button" value="Reply" />
  </label>

  <input hidden value="${messageID}" name="messageID"/>
</form:form>

</body>
</html>
