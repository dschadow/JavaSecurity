<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Security Response Header</title>
</head>
<body>
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
