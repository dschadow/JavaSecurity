<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Security Logging</title>
</head>
<body>
<h1>Security Logging</h1>

<p>After log in, there is nothing more to see here, all action is happening in the log file (or console).</p>

<form name="loginForm" action="LoginServlet" method="post">
    <div>
        <label for="username">Username</label>
        <input type="text" name="username" id="username"/>
    </div>

    <div>
        <label for="password">Password</label>
        <input type="password" name="password" id="password"/>
    </div>

    <div>
        <input type="submit" value="Send"/>
    </div>
</form>
</body>
</html>
