<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>Cross-Site Scripting (XSS) - JSP Output Escaping</title>
</head>
<body>
	<h1>Cross-Site Scripting (XSS) - JSP Output Escaping</h1>

    <p><strong>For HTML </strong> <e:forHtml value="${param.outputEscapedJSPName}"/></p>

    <p><strong>For CSS </strong> <e:forCssString value="${param.outputEscapedJSPName}"/></p>

    <p><strong>For XML </strong> <e:forXml value="${param.outputEscapedJSPName}"/></p>

    <p><a href="index.jsp">Home</a></p>
</body>
</html>
