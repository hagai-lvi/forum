<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
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
    <link href="<c:url value="/resources/css/align-text.css" />" rel="stylesheet">
</head>
<body class="basic-grey">
<table>
    <tr>
        <td>
            <myTags:logout></myTags:logout>
        </td>
        <td>

            <form method="get" action="forum_homepage">
                <input type="submit" class="button" value="Back to forum homepage" >
            </form>
        </td>
    </tr>
</table>
<h1>Welcome to subforum ${subforumName}</h1>
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


<c:if test="${isAdmin}">
    <form id="addModerator" action="addModerator" method="post">
        <h1>Add moderator to sub forum</h1>
        <label>
            <span>Moderator name:</span>
            <input type="text" name="moderatorName" placeholder="Moderator Name"/>
            <input type="submit" class="button" value="Add moderator"/>
        </label>
    </form>
</c:if>
<%--TODO--%>
<hr/>
<table >
    <h1>Threads in this subforum</h1>
    <tr>

        <c:forEach var="thread" items="${threadsList}">
            ${thread.getRootMessage().getMessageTitle()}

            <form action="thread_view" method="GET">
                <input type="submit" class="button" value="${thread.getTitle()}" name="threadID" />
            </form>
            <br/>
        </c:forEach>

    </tr>
</table>
<br/>
<hr/>
<br/>


<form id="reportModerator" action="report_moderator" method="post">
    <h1>Report a moderator</h1>
    <label>
        <span>Moderator name:</span>
        <input type="text" name="moderatorUserName" placeholder="Moderator Name"/>
        <input type="text" name="reportMessage" placeholder="Complaint..."/>
        <input type="submit" class="button" value="Report moderator"/>
    </label>
</form>


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

        $( "#reportModerator" ).
                ajaxForm({

                    success : function (response) {
                        alert("Your feedback is appreciated");
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        alert("Failed to add complaint");//TODO show error message
                    }
                })
    });
</script>
</html>
