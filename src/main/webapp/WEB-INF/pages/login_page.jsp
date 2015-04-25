<%-- TODO fix indentation with CSS --%>
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
    <title>${forumName} Login</title>
</head>
<body>
<h1>Welcome to forum ${forumName}</h1>

<h2>Sign In</h2>
<form action="forum_homepage" method="post">
  <br/>Username:<input type="text" name="username" placeholder="username">
  <br/>Password:<input type="password" name="password" placeholder="password">
  <br/><input type="submit" value="Login">
</form>


<h2>Register</h2>
<form action="register" method="post">
  <br/>Username:<input type="text" name="username">
  <br/>Password:<input type="password" name="password">
  <br/>Email:<input type="text" name="email">
  <br/><input type="submit" value="Register">
</form>

</body>
</html>
