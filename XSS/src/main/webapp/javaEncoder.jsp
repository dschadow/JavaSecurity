<%@ page import="org.owasp.encoder.Encode" %>
<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css" />
	<title>Cross-Site Scripting (XSS): OWASP Java Encoder</title>
</head>
<body>
	<h1>Cross-Site Scripting (XSS): OWASP Java Encoder</h1>

    <p>Without escaping: <strong>Hello</strong> <%= request.getParameter("javaEncoder") %></p>

    <p>HTML escaping: <strong>Hello</strong> <%= Encode.forHtml(request.getParameter("javaEncoder")) %></p>

    <p>HTML EL escaping: <strong>Hello</strong> <e:forHtml value="${param.javaEncoder}"/></p>

</body>
</html>
