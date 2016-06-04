<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<div class="alert alert-danger" id="error_msg" style="display: none">
  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
  <strong>Error!</strong>
</div>
<script>
  var x = $.url().param('error');
  if(x != null) {
    $('#error_msg').append(x);
    $('#error_msg').show();
  }
</script>
<h3>Registration</h3>
<form action="<c:url value="/profile/register" />" method="post" class="form-horizontal" id="register" data-toggle="validator">
<div class="form-group">
  <label for="firstName" class="col-sm-2 control-label">First name</label>
  <div class="col-sm-10">
    <input type="text" maxlength="15" name="firstName" class="form-control" placeholder="First name" required>
    <div class="help-block with-errors"></div>
  </div>
</div>
<div class="form-group">
  <label for="lastName" class="col-sm-2 control-label">Last name</label>
  <div class="col-sm-10">
    <input type="text" maxlength="15" name="lastName" class="form-control" placeholder="Last name" required>
    <div class="help-block with-errors"></div>
  </div>
</div>
<div class="form-group">
  <label for="username" class="col-sm-2 control-label">Username</label>
  <div class="col-sm-10">
    <input type="text" maxlength="15" name="username" class="form-control" placeholder="Username" required>
    <div class="help-block with-errors"></div>
  </div>
</div>
<div class="form-group">
  <label for="password" class="col-sm-2 control-label">Password</label>
  <div class="col-sm-10">
    <input type="password" maxlength="20" data-minlength="6" name="password" id="passwordOrg" class="form-control" placeholder="Password" required>
    <div class="help-block with-errors"></div>
  </div>
</div>
  <div class="form-group">
    <label for="confirmPassword" class="col-sm-2 control-label">Confirm password</label>
    <div class="col-sm-10">
      <input type="password" maxlength="20" data-match="#passwordOrg" name="confirmPassword" class="form-control" placeholder="Confirm password" required>
      <div class="help-block with-errors"></div>
    </div>
  </div>
<div class="form-group">
  <label for="email" class="col-sm-2 control-label">e-mail</label>
  <div class="col-sm-10">
    <input type="text" maxlength="25" name="email" class="form-control"placeholder="e-mail" required>
    <div class="help-block with-errors"></div>
  </div>
</div>
<div class="form-group">
  <div class="col-sm-offset-2 col-sm-10">
    <input type="submit" value="Register" class="btn btn-default" required>
  </div>
</div>
<input type="hidden" name="${_csrf.parameterName}"
       value="${_csrf.token}" />
</form>