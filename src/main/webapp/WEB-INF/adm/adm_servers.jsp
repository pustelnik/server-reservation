<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Admin-panel || Servers</title>
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
   <div class="listTitle">
      <h3>Physical machines</h3>
               <table class="table table-striped">
            <tr>
                <th>Hostname</th>
                <th>Operating System IP</th>
                <th>iRMC IP</th>
                <th>Reservation</th>
                <th>Options</th>
            </tr>
            <c:forEach items="${servers}" var="server">

                <tr>
                    <td><a href="/admin/servers/<c:out value="${server.host_name}" />"><c:out value="${server.host_name}" /></td>
                    <td><a href="/admin/servers/<c:out value="${server.os_ip}" />/credentials"><c:out value="${server.os_ip}" /></a></td>
                    <td><a href="/admin/servers/<c:out value="${server.irmc_ip}" />/credentials"><c:out value="${server.irmc_ip}" /></a></td>
                    <td>
                        <c:if test="${server.user.username != null}">
                            <form method="post" action="/admin/cancelReservation" name="serverForm">
                                <input type="hidden", name="host_name", value="<c:out value="${server.host_name}" />">
                                <input type="hidden" name="${_csrf.parameterName}"
                                       value="${_csrf.token}" />

                                <input class="btn btn-success" type="submit", value="Cancel reservation <c:out value="${server.user.firstName}" /> ">
                            </form>
                        </c:if>
                    </td>
                    <td>
                        <form method="post" action="/admin/removeServer">
                            <input type="hidden", name="host_name", value="<c:out value="${server.host_name}" />">
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />
                            <input class="btn btn-danger" type="submit", value="Remove server">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>
