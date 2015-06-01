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
                <input type="submit" value=${forum.getForum()} name="forum" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>

<%-- TODO should be available to super-admin only --%>
<form:form action="addForum" method="POST" >
    <h1>Create new Forum:</h1><br/>
    <input type="text" placeholder="Forum Name"  id="forumName" name="forumName" /><br/>
    <input type="number" placeholder="Maximal Number Of Moderators" name="numOfModerators"/><br/>
    <input type="text" placeholder="Password Regex"  name="passRegex"/><br/>
    <input type="submit" value="Create forum" /><br/>
</form:form>

</body>
</html>