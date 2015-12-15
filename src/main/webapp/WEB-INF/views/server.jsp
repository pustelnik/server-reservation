<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title><c:out value="${servers.host_name}"></c:out></title>
</head>
<body>
<h3><td><c:out value="${servers.host_name}"></c:out></td></h3>
<table class="table table-striped">
    <tr>
        <th></th>
        <th>IP</th>
        <th>Login</th>
        <th>Password</th>
    </tr>
    <tr>
        <th>iRMC</th>
        <td><c:out value="${servers.irmc_ip}"></c:out></td>
        <td><c:out value="${credentialsIrmc.login}"></c:out></td>
        <td><c:out value="${credentialsIrmc.password}"></c:out></td>
    </tr>
        <th>OS</th>
    <td><c:out value="${servers.os_ip}"></c:out></td>
        <td><c:out value="${credentialsOS.login}"></c:out></td>
        <td><c:out value="${credentialsOS.password}"></c:out></td>
</table>
<form method="post" action="/servers/reserve/resign" name="serverForm">
    <input type="hidden", name="host_name", value="<c:out value="${servers.host_name}" />">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}" />
    <input class="btn btn-success"  type="submit", value="Resign">
</form>
</body>
</html>
