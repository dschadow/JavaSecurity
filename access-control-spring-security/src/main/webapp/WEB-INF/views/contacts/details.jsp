<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/styles.css" />">
    <title>Access Control - Spring Security</title>
</head>
<body>
<h1>Contact Details</h1>

<p>
    <strong>ID:</strong> ${contact.contactId}<br>
    <strong>Firstname:</strong> ${contact.firstname}<br>
    <strong>Lastname:</strong> ${contact.lastname}<br>
    <strong>Comment:</strong> ${contact.comment}
</p>

<p>
    <a href="list">All Contacts</a> | <a href="<c:url value="/logout"/>">Logout</a>
</p>
</body>
</html>
