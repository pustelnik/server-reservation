<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $('form').on('submit',function(){
        if($('#pass').val()!=$('#passwordConfirm').val()){
            alert('Password not matches');
            return false;
        }
        return true;
    });
</script>
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
<h3><strong><c:out value="${user.username}"/></strong></h3>

<form action="<c:url value='/admin/users' />" method="post" class="form-horizontal" id="login" data-toggle="validator">
    <div class="form-group">
        <label for="firstName" class="col-sm-2 control-label">First name</label>
        <div class="col-sm-10">
            <input type="text" name="firstName" maxlength="15" class="form-control" placeholder="<c:out value="${user.firstName}"/>">
            <div class="help-block with-errors"></div>
        </div>
    </div>
    <div class="form-group">
        <label for="lastName" class="col-sm-2 control-label">Last name</label>
        <div class="col-sm-10">
            <input type="text" name="lastName" maxlength="15" class="form-control" placeholder="<c:out value="${user.lastName}"/>">
            <div class="help-block with-errors"></div>
        </div>
    </div>
    <div class="form-group">
        <label for="pass" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-10">
            <input type="password" name="password" maxlength="30" id="pass"
                   class="form-control" placeholder="Enter password">
            <div class="help-block with-errors"></div>
        </div>
    </div>
    <div class="form-group">
        <label for="passwordConfirm" class="col-sm-2 control-label">Confirm password</label>
        <div class="col-sm-10">
            <input type="password" id="passwordConfirm"  maxlength="30"data-match="#pass"
                   class="form-control" placeholder="Confirm password">
            <div class="help-block with-errors"></div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="enabled">Is enabled
                </label>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Apply changes" class="btn btn-default">
        </div>
    </div>
    <input type="hidden" name="username" value="<c:out value="${user.username}"/>">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}" />
</form>