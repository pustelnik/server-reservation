<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title><t:getAsString name="title"/></title>
    <link rel="stylesheet"
          type="text/css"
          href="/resources/css/bootstrap.css" >
    <script src="http://code.jquery.com/jquery-latest.min.js"
            type="text/javascript"></script>
    <script src="/resources/validator.min.js"
            type="text/javascript"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="/resources/purl.js"
            type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            var matchers = [/login/,/admin\/servers/,/home/,/register/,/admin\/users/,/admin\/addserver/,/servers/];
            var classes = ['login', 'adm_servers', 'home', 'register', 'adm_users', 'adm_addserver', 'servers'];
            var x = window.location.href;
            var res = '#home';
            for(var i = 0; i < matchers.length; i++) {
                if(x.match(matchers[i])) {
                    res = classes[i];
                    res = '#'+res;
                    break;
                }
            }
            $(res).addClass("active");
        });

    </script>
    <style>
        .footer {
            position: fixed;
            padding-left: 2%;
            bottom: 0;
            width: 100%;
            /* Set the fixed height of the footer here */
            height: 40px;
            background-color: #f5f5f5;
        }
        .dropdown:hover .dropdown-menu {
            display: block;
            margin-top: 0; // remove the gap so it doesn't close
        }

        /* Hide for mobile, show later */
        .sidebar {
            display: none;
            width: 150px;
        }
        @media (min-width: 768px) {
            .sidebar {
                position: fixed;
                top: 51px;
                bottom: 0;
                left: 0;
                z-index: 1000;
                display: block;
                padding: 20px;
                overflow-x: hidden;
                overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
                background-color: #f5f5f5;
                border-right: 1px solid #eee;
            }
        }

        /* Sidebar navigation */
        .nav-sidebar {
            margin-right: -21px; /* 20px padding + 1px border */
            margin-bottom: 20px;
            margin-left: -20px;
        }
        .nav-sidebar > li > a {
            padding-right: 20px;
            padding-left: 20px;
        }
        .nav-sidebar > .active > a,
        .nav-sidebar > .active > a:hover,
        .nav-sidebar > .active > a:focus {
            color: #fff;
            background-color: #428bca;
        }
        .main {
            margin-left: 20%;
            margin-top: 75px;
        }
        @media (min-width: 768px) {
            .main {
                padding-right: 40px;
                padding-left: 40px;
            }
        }
        .admin {
            padding-left: 20px;
            color: #1D4152;
        }
        #options {
            display: inline-flex;
        }
        .buttons-right {
            padding-right: 15px;
        }
    </style>
</head>
<body>
<div id="header">
    <t:insertAttribute name="header" />
</div>
<div id="content" class="container main">
    <t:insertAttribute name="body" />
</div>
</body>
</html>
