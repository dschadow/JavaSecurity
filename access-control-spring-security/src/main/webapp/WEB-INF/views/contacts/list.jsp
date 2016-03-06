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
<h1>All my contacts (<sec:authentication property="principal.Username"/>)</h1>

<ul>
    <c:forEach var="contact" items="${contacts}">
        <li><a href="${contact.contactId}">${contact.firstname} ${contact.lastname}</a></li>
    </c:forEach>
</ul>

<p>
    <a href="<c:url value="/logout"/>">Logout</a>
</p>
</body>
</html>
