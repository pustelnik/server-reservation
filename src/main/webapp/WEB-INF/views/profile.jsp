<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
  <head>
    <title>User profile</title>
  </head>
  <body>
    <h3>Your profile</h3>
    Login: <c:out value="${users.username}" /><br/>
    Name: <c:out value="${users.firstName}" /> <c:out value="${users.lastName}" /><br/>
    E-mail: <c:out value="${users.email}" /><br />
    Reserved servers: <c:out value="${users.host_names}" /><br />
  </body>
</html>
