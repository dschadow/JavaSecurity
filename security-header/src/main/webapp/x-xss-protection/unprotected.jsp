<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>X-XSS-Protection: Unprotected</title>
    <link rel="stylesheet" type="text/css" href="../resources/css/styles.css"/>
</head>
<body>
    <h1>X-XSS-Protection: Unprotected</h1>

    <div>Name <%= request.getParameter("name") %></div>
</body>
</html>
