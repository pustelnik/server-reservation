<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-default navbar-fixed-top ">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Server reservation</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li id="home" ><a href="/">Home</a></li>
                <li id="servers"><a href="/servers">Servers</a></li>
                <li id="register"><a href="/profile/register">Registration</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <%
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        String name = auth.getName();
                        if(!name.contains("anonymous")) {
                            out.print("<a href=\"/logout\">logout</a>");
                        } else {
                            out.print("<a href=\"/login\">login</a>");
                        }

                    %>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>
<div class="col-sm-3 col-md-2 sidebar" <% if(request.isUserInRole("ROLE_ADMIN")) { out.print(""); } else { out.print("style=\"display: none\"");} %>>
    <ul class="nav nav-sidebar">
        <h3 class="admin">Admin</h3>
        <li id="adm_users"><a href="/admin/users">Users</a></li>
        <li id="adm_servers"><a href="/admin/servers">Servers</a></li>
        <li id="adm_addserver"><a href="/admin/addserver">Add server</a></li>
    </ul>

</div>