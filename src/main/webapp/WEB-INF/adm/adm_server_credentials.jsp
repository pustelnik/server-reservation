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
<h3><c:out value="${servers.host_name}"></c:out></h3>
<table class="table table-striped">
    <tr>
        <th>IP</th>
        <th>Login</th>
        <th>Password</th>
    </tr>
    <tr>
        <td><c:out value="${credentials.ip}"></c:out></td>
        <td><c:out value="${credentials.login}"></c:out></td>
        <td><c:out value="${credentials.password}"></c:out></td>
    </tr>
</table>
<form method="post" action="/admin/servers/restoredefaultcredentials" name="serverForm">
    <input type="hidden", name="ip", value="<c:out value="${credentials.ip}" />">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input class="btn btn-success" type="submit", value="Restore default credentials">
</form>
