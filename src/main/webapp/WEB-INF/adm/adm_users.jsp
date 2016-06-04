<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
<script type="text/javascript">
    $(document).ready(function()
            {
                $("#users-table").tablesorter();
            }
    );

</script>
<h3>Users</h3>
<table class="table table-striped" id="users-table">
    <tr>
        <th>First name</th>
        <th>Last name</th>
        <th>username</th>
        <th>Reserved servers</th>
        <th></th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><c:out value="${user.firstName}"/></td>
            <td><c:out value="${user.lastName}"/></td>
            <td><c:out value="${user.username}"/></td>
            <td><c:forEach items="${user.host_names}" var="host_name">
                <a href="/servers/<c:out value="${host_name}"/>"><c:out value="${host_name}"/></a>,
            </c:forEach></td>
            <td>
                <div id="options">
                    <div class="option"><a class="btn btn-info buttons-right" href="/admin/users/<c:out value="${user.username}"/>">Edit</a></div>
                    <form action="/admin/removeUser", method="post" class="buttons-right" style="display: inline-block;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" name="username", value="<c:out value="${user.username}"/>" />
                        <input class="btn btn-danger" type="submit", value="Remove user">
                    </form>
                </div>
            </td>
        </tr>
    </c:forEach>
</table>
