<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>Security Response Header</title>
</head>
<body>
	<h1>Security Response Header</h1>

    <p>Each response header can be called in an unprotected and in a protected version. Every header is added by a
    filter. There are no special pages for HSTS since this header is only active or inactive for the whole domain.</p>

    <p>
        <a href="forms/protectedForm.jsp">Protected form</a><br>
        <a href="forms/unprotectedForm.jsp">Unprotected form</a><br>
    </p>

    <h2>X-Frame-Options</h2>
    <p>
        <a href="x-frame-options/unprotected.jsp">Unprotected</a><br/>
        <a href="x-frame-options/protected.jsp">Protected</a>
    </p>

    <h2>X-Content-Type-Options</h2>
    <p>
        <a href="x-content-type-options/unprotected.txt">Unprotected</a><br>
        <a href="x-content-type-options/protected.txt">Protected</a>
    </p>

    <h2>Content Security Policy</h2>
    <p>
        <a href="csp/unprotected.jsp?name=<script>alert('XSS')</script>">Unprotected</a><br>
        <a href="csp/protected.jsp?name=<script>alert('XSS')</script>">Protected</a><br>
        <a href="csp/reporting.jsp?name=<script>alert('XSS')</script>">Report only</a>
    </p>
</body>
</html>
