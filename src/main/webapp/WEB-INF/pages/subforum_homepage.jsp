<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: hagai_lvi
  Date: 4/28/15
  Time: 6:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subforum</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
    <script src="http://malsup.github.com/jquery.form.js"></script>
</head>
<body>

<h1>Welcome to subforum ${subforumName}</h1>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<myTags:logout></myTags:logout>
<%--TODO--%>
<form:form action="addThread" method="post">
    <h1>Create New Thread</h1>
    <label>
        <span>Title :</span>
        <input type="text" name="srcMsgTitle" placeholder="Title" />
    </label>

    <label>
        <span>Message Body :</span>
        <input type="text" name="srcMsgBody" placeholder="Message body" />
    </label>

    <label>
        <span>&nbsp;</span>
        <input type="submit" class="button" value="Create" />
    </label>
</form:form>

<form method="get" action="forum_homepage">
    <input type="submit" value="Back to forum homepage" >
</form>

<c:if test="${isAdmin}">
    <form id="addModerator" action="addModerator" method="post">
        <h1>Add moderator to sub forum</h1>
        <label>
            <span>Moderator name:</span>
            <input type="text" name="moderatorName" placeholder="Moderator Name"/>
            <input type="submit" value="Add moderator"/>
        </label>
    </form>
</c:if>
<%--TODO--%>
<table >
    <tr>

        <c:forEach var="thread" items="${threadsList}">
            ${thread.getRootMessage().getMessageTitle()}

            <form action="thread_view" method="GET">
                <input type="submit" value="${thread.getTitle()}" name="threadID" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<br/>



</body>
<script>
//    $(document).ready(function(){
//        $( "#addModerator" ).submit(function( event ) {
//            event.preventDefault();
//            alert( "Handler for .submit() called." );
//            $.post($(this).attr('action'), $(this).serialize());
//        });
//    });
    $(document).ready(function(){
        $( "#addModerator" ).
                ajaxForm({

                    success : function (response) {
                        alert("Moderator added successfuly");
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        alert("Failed to add moderator");//TODO show error message
                    }
                })
    });
</script>
</html>
