<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/18/15
  Time: 5:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>You tried to login to forum ${forumName}</h1>
<h1>Messgae: ${message}</h1>

<h2>Signup Details</h2>
<form action="facade" method="get">
  <br/>Username:<input type="text" name="username">
  <br/>Password:<input type="password" name="password">
  <br/><input type="submit" value="Submit">
</form>


<h2>Register Details</h2>
<form action="facade" method="get">
  <br/>Username:<input type="text" name="username">
  <br/>Password:<input type="password" name="password">
  <br/>Email:<input type="text" name="email">
  <br/><input type="submit" value="Submit">
</form>

</body>
</html>
