<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Admin-panel || Servers</title>
</head>
<body>
<div class="alert alert-info fade in" id="msg" style="display: none">
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
<div class="alert alert-danger fade in" id="error" style="display: none">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Error!</strong>
</div>
<script>
    var x = $.url().param('error');
    if(x != null) {
        $('#error').append(x);
        $('#error').show();
    }
</script>
<script>
    $(document).ready(function()
            {
                $("#servers-table").tablesorter();
            }
    );

</script>
   <div class="listTitle">
      <h3>Physical machines</h3>
               <table class="table table-striped" id="servers-table">
                   <thead>
                        <tr class="info">
                            <th>Hostname</th>
                            <th>Operating System IP</th>
                            <th>iRMC IP</th>
                            <th>Reservation</th>
                            <th></th>
                        </tr>
                   </thead>
                   <tbody>
            <c:forEach items="${servers}" var="server">

                <tr>
                    <td><a href="/admin/servers/<c:out value="${server.host_name}" />"><c:out value="${server.host_name}" /></a>
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
                    <td><div id="options">
                        <form method="post" action="/admin/testconnection" class="buttons-right">
                            <input type="hidden", name="host_name", value="<c:out value="${server.host_name}" />">
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />
                            <input class="btn btn-primary" type="submit", value="Test connection">
                        </form>
                        <form method="post" action="/admin/removeServer" class="buttons-right">
                            <input type="hidden", name="host_name", value="<c:out value="${server.host_name}" />">
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />
                            <input class="btn btn-primary" type="submit", value="Remove server">
                        </form>
                    </div></td></td>
                </tr>

            </c:forEach>
                   </tbody>
        </table>
    </div>
</body>
</html>
