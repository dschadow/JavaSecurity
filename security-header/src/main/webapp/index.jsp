<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Security Header</title>
</head>
<body>
<h1>Security Header</h1>

<p>Each response header can be called in an unprotected and in a protected version. Every header is added by a
    filter. There are no special pages for HSTS since this header is only active or inactive for the whole domain.
    Content Security Policy and especially Content Security Policy Level 2 and Level 3 may not work in your browser at
    all, other headers may vary (a little) depending on the selected browser.</p>

<h2>X-Content-Type-Options</h2>

<p>
    <a href="x-content-type-options/unprotected.txt">Unprotected</a> |
    <a href="x-content-type-options/protected.txt">Protected</a>
</p>

<h2>Cache-Control</h2>

<p>
    <a href="cache-control/unprotected.jsp">Unprotected</a> |
    <a href="cache-control/protected.jsp">Protected</a>
</p>

<h2>X-Frame-Options</h2>

<p>
    <a href="x-frame-options/unprotectedForm.jsp">Original Form</a> |
    <a href="x-frame-options/unprotected.jsp">Unprotected</a> |
    <a href="x-frame-options/protected.jsp">Protected</a>
</p>

<h2>Content Security Policy Level 1</h2>

<p>
    <a href="csp/unprotected.jsp?name=<script>alert('XSS')</script>">Unprotected</a> |
    <a href="csp/protected.jsp?name=<script>alert('XSS')</script>">Protected</a> |
    <a href="csp/reporting.jsp?name=<script>alert('XSS')</script>">Report Only</a>
</p>

<h2>Content Security Policy Level 2</h2>

<p>
    <a href="csp2/unprotectedForm.jsp">Original Form</a> |
    <a href="csp2/unprotected.jsp">Unprotected</a> |
    <a href="csp2/protected.jsp">Protected</a>
</p>

<h2>X-XSS-Protection</h2>

<p>
    <a href="x-xss-protection/unprotected.jsp?name=<script>alert('XSS')</script>">Unprotected</a> |
    <a href="x-xss-protection/protected.jsp?name=<script>alert('XSS')</script>">Protected</a>
</p>

<h2>All Header and Meta Tags</h2>

<p>
    <a href="all/all.jsp">Protected</a>
</p>
</body>
</html>
