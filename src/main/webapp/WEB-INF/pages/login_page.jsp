<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
  <script src="http://malsup.github.com/jquery.form.js"></script>
  <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<h1>Welcome to forum ${forumName}</h1>

<form action="forum_homepage" method="post">
  <h1>Sign In</h1>

  <label>
    <input hidden type="text" name="forumName" value="${forumName}">
  </label>

  <label>
    <span>Username :</span>
    <input type="text" name="username" placeholder="username" />
  </label>

  <label>
    <span>Password :</span>
    <input type="password" name="password" placeholder="Password" />
  </label>


  <label>
    <span>&nbsp;</span>
    <input type="submit" class="button" value="Login" />
  </label>
</form>
<hr/>




<form action="register" method="post">
  <h1>Sign UP</h1>

  <label>
    <input hidden type="text" name="forumName" value="${forumName}">
  </label>

  <label>
    <span>Username :</span>
    <input type="text" name="username" placeholder="username" />
  </label>

  <label>
    <span>Password :</span>
    <input type="password" name="password" placeholder="Password" />
  </label>

  <label>
    <span>Email :</span>
    <input type="email" name="email" placeholder="Valid Email Address" />
  </label>

  <label>
    <span>&nbsp;</span>
    <input type="submit" class="button" value="Register" on />
  </label>
</form>
<hr/>

<form id="auth" action="auth" method="post">
  <h1>Enter Auth String</h1>

  <input hidden type="text" name="forumName" value="${forumName}">


  <label>
    <span>User Name:</span>
    <input type="text" name="username" placeholder="User name to auth" />
  </label>


  <label>
    <span>Auth String:</span>
    <input type="text" name="auth_string" placeholder="Auth string goes here" />
  </label>

  <label>
    <span>&nbsp;</span>
    <input type="submit" class="button" value="authenticate" on />
  </label>
</form>
<hr/>


<form action="guest_forum_homepage" method="post">

  <label>
    <input hidden type="text" name="forumName" value="${forumName}">
  </label>

  <label>
    <span>&nbsp;</span>
    <input type="submit" class="button" value="Enter as guest" on />
  </label>
</form>
<hr/>

<form method="get" action="facade">
  <input type="submit" class="button" value="Back to Facade" >
</form>
<hr/>

</body>
<script>
  $(document).ready(function() {
    $("#auth").
            ajaxForm({

              success: function (response) {
                alert("auth successful");
              },
              error: function (xhr, ajaxOptions, thrownError) {
                alert("auth failed");//TODO show error message
              }
            })
  });
</script>
</html>
