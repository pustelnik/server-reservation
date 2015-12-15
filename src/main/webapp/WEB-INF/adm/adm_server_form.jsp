<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Admin - panel || Create new server </title>
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
<h3>Add new server:</h3>
<form class="form-horizontal" action="/admin/addserver" method="post">
    <div class="form-group">
        <label for="host_name" class="col-sm-2 control-label">Hostname</label>
        <div class="col-sm-10">
            <input type="text" name="host_name" class="form-control" placeholder="Enter hostname">
        </div>
    </div>
    <div class="form-group">
        <label for="os_ip" class="col-sm-2 control-label">OS IP</label>
        <div class="col-sm-10">
            <input type="text" name="os_ip" class="form-control" placeholder="Enter OS IP">
        </div>
    </div>
    <div class="form-group">
        <label for="irmc_ip" class="col-sm-2 control-label">iRMC IP</label>
        <div class="col-sm-10">
            <input type="text" name="irmc_ip" class="form-control" placeholder="Enter iRMC IP">
        </div>
    </div>
    <div class="form-group">
        <label for="os_login" class="col-sm-2 control-label">OS login</label>
        <div class="col-sm-10">
            <input type="text" name="osLogin" class="form-control" placeholder="Enter OS login">
        </div>
    </div>
    <div class="form-group">
        <label for="firstName" class="col-sm-2 control-label">OS password</label>
        <div class="col-sm-10">
            <input type="text" name="osPassword" class="form-control" placeholder="Enter OS password">
        </div>
    </div>
    <div class="form-group">
        <label for="irmc_login" class="col-sm-2 control-label">iRMC login</label>
        <div class="col-sm-10">
            <input type="text" name="irmcLogin" class="form-control" placeholder="Enter iRMC login">
        </div>
    </div>
    <div class="form-group">
        <label for="irmc_password" class="col-sm-2 control-label">iRMC password</label>
        <div class="col-sm-10">
            <input type="text" name="irmcPassword" class="form-control" placeholder="Enter iRMC password">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Create server" class="btn btn-default" >
        </div>
    </div>
    <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}" />
</form>
<c:if test="${not empty error}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <strong>Danger!</strong> ${error}
    </div>
</c:if>
</body>
</html>
