<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>Message : ${message}</h1>
<h1>There are ${size} forums in the system</h1>


<table width="40%" cellpadding="5" bordercolor="#000066"
       bgcolor="#FFFFFF" border="1"   cellspacing="0">
    <tr>


        <c:forEach var="forum" items="${forumList}">
            ${forum}<br/>
        </c:forEach>

    </tr>
</table>

<h1>Create new Forum:</h1><br/>
<form action="addForum" method="POST">
    <input type="text" id="forumName" name="forumName" />
    <input type="submit" value="Submit" />
</form>

</body>
</html>