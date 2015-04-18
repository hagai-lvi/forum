<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>Message : ${message}</h1>
<h1>Forum : ${forum}</h1>
<h1>Size : ${size}</h1>


<table width="40%" cellpadding="5" bordercolor="#000066"
       bgcolor="#FFFFFF" border="1"   cellspacing="0">
    <tr>


        <c:forEach var="forum" items="${forumList}">
            ${forum}<br/>
        </c:forEach>

    </tr>
</table>

</body>
</html>