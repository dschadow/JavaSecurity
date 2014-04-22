<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css" />
	<title>Cross-Site Scripting (XSS)</title>
</head>
<body>
	<h1>Cross-Site Scripting (XSS)</h1>

    <p>Use an input like <strong>&lt;script&gt;alert(&quot;XSS&quot;)&lt;/script&gt;</strong> to test the output escaping functionality.</p>

	<h2>OWASP Java Encoder</h2>
	
	<form name="javaEncoderForm" method="post" action="javaEncoder.jsp">
        <label for="name1" title="Name">Name</label>
        <input type="text" id="name1" name="name1" class="text-input" />
        <input type="submit" value="Submit" />
	</form>

	<h2>Content Security Policy</h2>

	<form name="cspForm" method="post" action="CSPServlet">
        <label for="name2" title="Name">Name</label>
        <input type="text" id="name2" name="name2" class="text-input" />
        <input type="submit" name="submit" value="Submit" />
	</form>

    <h2>Content Security Policy Reporty Only</h2>

    <form name="cspReportingForm" method="post" action="CSPReportingServlet">
        <label for="name3" title="Name">Name</label>
        <input type="text" id="name3" name="name3" class="text-input" />
        <input type="submit" name="submit" value="Submit" />
    </form>
</body>
</html>
