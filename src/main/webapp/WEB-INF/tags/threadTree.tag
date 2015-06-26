<%@ attribute name="node" type="data_structures.Node" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/my-list.css' />" />
<c:if test="${!empty node}">
    <ul>
        <c:forEach var="node" items="${node.children}">
            <%--<li><h3><c:out value="${node.data.getMessageTitle()}"/> :</h3>--%>
            <%--<c:out value="${node.data.getMessageText()}"/></li>--%>
            <li>
                <a><c:out value="${node.data.getMessageText()}"/></a>

                <form action="reply_to_message" method="post">
                    <input hidden name="messageID" value=${node.data.getId()}>
                    <input type="submit" value="Add reply" />
                </form>
            </li>
            <myTags:threadTree node="${node}"/>
        </c:forEach>
    </ul>
</c:if>