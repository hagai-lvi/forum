<%-- This is the main page of the forums system --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>

<head>
    <title> Forum system facade</title>
    <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<h1>Welcome to the forums system</h1>



<table >
    <tr>

        <c:forEach var="forum" items="${forumList}">
            ${forum}

            <form action="login_page" method="POST">
                <input type="submit" value=${forum} name="forum" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>

<form:form action="superAdminDashboard" method="post">
    Super admin login:

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

</form:form>
</body>
</html>