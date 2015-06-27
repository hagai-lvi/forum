<%@ attribute name="node" type="data_structures.Node" required="true" %>
<%@ attribute name="isGuest" type="java.lang.Boolean" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/my-list.css' />" />
<c:if test="${!empty node}">
    <ul>
        <c:forEach var="node" items="${node.children}">
            <li>
                <b><a><c:out value="${node.data.getMessageTitle()}"/></a></b><br/>
                <a><c:out value="${node.data.getMessageText()}"/></a><br/>
                <a><c:out value="user: ${node.data.getUser()}"/></a>
                <c:if test="${not isGuest}">
                    <form action="reply_to_message" method="post">
                        <input hidden name="messageID" value=${node.data.getId()}>
                        <input type="submit" value="Add reply" />
                    </form>
                    <form action="edit_message" method="post">
                        <input hidden name="messageID" value=${node.data.getId()}>
                        <input type="text" name="newTitle">
                        <input type="text" name="newBody">
                        <input type="submit" value="edit message" />
                    </form>
                </c:if>
            </li>
            <myTags:threadTree node="${node}" isGuest="${isGuest}"/>
        </c:forEach>
    </ul>
</c:if>