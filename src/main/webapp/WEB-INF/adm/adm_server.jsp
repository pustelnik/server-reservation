<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
<h3><td><c:out value="${servers.host_name}"/></td></h3>
<table class="table table-striped">
    <tr>
        <th></th>
        <th>IP</th>
        <th>Login</th>
        <th>Password</th>
    </tr>
    <form action="/admin/editServer" method="post">
        <tr>
            <th>iRMC</th>
            <td><c:out value="${servers.irmc_ip}"/></td>
            <td><input type="text" name="irmcLogin" class="form-control" placeholder="<c:out value="${credentialsIrmc.login}"/>"></td>
            <td> <input type="text" name="irmcPassword" class="form-control" placeholder="<c:out value="${credentialsIrmc.password}"/>"></td>
        </tr>
            <th>OS</th>
            <td><c:out value="${servers.os_ip}"/></td>
            <td><input type="text" name="osLogin" class="form-control" placeholder="<c:out value="${credentialsOS.login}"/>"></td>
            <td><input type="text" name="osPassword" class="form-control" placeholder="<c:out value="${credentialsOS.password}"/>"></td>
        <tr style="border-top: none">
            <td style="border-top: none">
                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
                <input type="hidden", name="host_name", value="<c:out value="${servers.host_name}" />">
                <input type="submit" value="Apply changes" class="btn btn-primary" >
            </td>
        </tr>

    </form>
</table>
