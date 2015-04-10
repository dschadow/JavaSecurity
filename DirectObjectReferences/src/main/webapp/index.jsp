<%@page import="java.util.Set" %>
<%@page import="de.dominikschadow.javasecurity.ReferenceUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Direct Object References</title>
</head>
<body>
<h1>Direct Object References</h1>

<h2>RandomAccessReferenceMap</h2>

<ul>
    <%
        Set<String> indirectReferences = ReferenceUtil.getAllIndirectReferences();
        for (String indirectReference : indirectReferences) {
    %>
    <li>
        <a href="download?file=<%= indirectReference %>">
            <%= ReferenceUtil.getFileNameByIndirectReference(indirectReference) %>
        </a>
    </li>
    <%
        }
    %>
</ul>

<h2>Direct Download</h2>
<ul>
    <li><a href="download?file=secret.txt">secret.txt</a></li>
</ul>
</body>
</html>