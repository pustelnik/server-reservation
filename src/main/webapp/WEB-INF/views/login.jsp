<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="pl.san.jakub.model.data.Users"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User login page</title>
<link rel="stylesheet"
          type="text/css"
          href="/resources/css/bootstrap.css" >
<style>
.error {
	color: red;
	padding-top: 1em;
	padding-bottom: 1em;
}
.msg {
	color: green;
	padding-top: 1em;
	padding-bottom: 1em;
}
</style>
</head>
<body>
<div class="container" id="data">
<h3>Login</h3>
<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>
<form action="<c:url value='/login' />" method="post" class="form-horizontal" id="login">
	<div class="form-group" class="form-horizontal">
		<label for="username" class="col-sm-2 control-label">Login</label>
		<div class="col-sm-10">
			<input type="text" name="username" class="form-control" placeholder="login">
		</div>
	</div>
	<div class="form-group">
		<label for="password" class="col-sm-2 control-label">Password</label>
		<div class="col-sm-10">
			<input type="password" name="password" class="form-control" placeholder="password">
			<div id="error"></div>
		</div>
	</div>
	<div class="form-group">
    		<div class="col-sm-offset-2 col-sm-10">
    			<div class="checkbox">
    				<label>
    					<input type="checkbox" name="remember-me">Remember me
    				</label>
    			</div>
    		</div>
    	</div>
	<div class="form-group">
    	<div class="col-sm-offset-2 col-sm-10">
			<input type="submit" value="OK" class="btn btn-default">
		</div>
	</div>
	<input type="hidden" name="${_csrf.parameterName}"
    			value="${_csrf.token}" />
</form>
</div>
</body>
</html>