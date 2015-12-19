<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin - panel || Users</title>
</head>
<body>
<div class="alert alert-success" id="msg" style="display: none">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Info!</strong>
</div>
<script>
    var x = $.url().param('msg');
    if(x != null) {
        $('#msg').append(x);
        $('#msg').show();
    }
</script>
<h3>Users</h3>
<table class="table table-striped">
    <tr class="info">
        <th>First name</th>
        <th>Last name</th>
        <th>username</th>
        <th>Reserved servers</th>
        <th>Edit</th>
        <th>Remove</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><c:out value="${user.firstName}"></c:out></td>
            <td><c:out value="${user.lastName}"></c:out></td>
            <td><c:out value="${user.username}"></c:out></td>
            <td><c:forEach items="${user.host_names}" var="host_name">
                <a href="/servers/<c:out value="${host_name}"></c:out>"><c:out value="${host_name}"></c:out></a>,
            </c:forEach></td>
            <td><a class="btn btn-info" href="/admin/users/<c:out value="${user.username}"></c:out>">Edit</a></td>
            <td>
                <form action="/admin/removeUser", method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="hidden" name="username", value="<c:out value="${user.username}"></c:out>" />
                    <input class="btn btn-danger" type="submit", value="Remove user">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
