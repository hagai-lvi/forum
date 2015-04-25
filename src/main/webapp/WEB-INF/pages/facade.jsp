<%-- This is the main page of the forums system --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>
<h1>Welcome to the forums system</h1>


<table width="40%" cellpadding="5" bordercolor="#000066"
       bgcolor="#FFFFFF" border="1"   cellspacing="0">
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

<%-- TODO should be available to super-admin only --%>
<h1>Create new Forum:</h1><br/>
<form:form modelAttribute="forumList" action="addForum" method="POST" >
    <input type="text" placeholder="forum name"  id="forumName" name="forumName" />
    <input type="submit" value="Create forum" />
</form:form>

</body>
</html>