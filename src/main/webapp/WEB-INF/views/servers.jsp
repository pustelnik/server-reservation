<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="alert alert-success" id="msg" style="display: none">
  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
  <strong>Info!</strong>
</div>
<div class="alert alert-warning" id="error" style="display: none">
  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
  <strong>Danger!</strong> ${error}
</div>
<script>
  var x = $.url().param('msg');
  if(x != null) {
      $('#msg').append(x);
      $('#msg').show();
  }
  var y = $.url().param('error');
  if(y != null) {
      $('#error').append(y);
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
        <tr>
            <th>Reservation</th>
            <th>Hostname</th>
            <th>Model</th>
            <th>Operating System IP</th>
            <th>iRMC IP</th>
            <th>Rack Position</th>
            <th>LAN</th>
            <th>Operating System</th>
            <th>Comment</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${serversList}" var="server" >
            <tr>
                <td><c:out value="${server.user.firstName}" />
                    <c:if test="${server.user.username == null}">
                        <form method="post" action="/servers/reserve" name="serverForm">
                            <input type="hidden", name="host_name", value="<c:out value="${server.host_name}" />">
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />
                            <input class="btn btn-success" type="submit", value="Reserve it!">
                        </form>
                    </c:if>
                </td>
                <td><a href="/servers/<c:out value="${server.host_name}" />"><c:out value="${server.host_name}" /></td>
                <td><c:out value="${server.model}" /></td>
                <td><c:out value="${server.os_ip}" /></td>
                <td><a href="http://<c:out value="${server.irmc_ip}" />">http://<c:out value="${server.irmc_ip}" /></a></td>
                <td><c:out value="${server.rackPosition}" /></td>
                <td><c:out value="${server.lan}" /></td>
                <td><c:out value="${server.operatingSystem}" /></td>
                <td><c:out value="${server.comment}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>