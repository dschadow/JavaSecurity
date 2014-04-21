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

	<h2>OWASP Java Encoder</h2>
	
	<form name="javaEncoderForm" method="post" action="javaEncoder.jsp">
		<table>
			<tr>
				<td><label for="javaEncoder" title="Name">Name</label></td>
				<td><input type="text" id="javaEncoder" name="javaEncoder" class="text-input" /></td>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

	<h2>Content Security Policy</h2>

	<form name="cspForm" method="post" action="CSPServlet">
		<table>
			<tr>
				<td><label for="csp" title="Name">Name</label></td>
				<td><input type="text" id="csp" name="csp" class="text-input" /></td>
				<td><input type="submit" name="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

    <h2>Content Security Policy Reporty Only</h2>

    <form name="cspReportingForm" method="post" action="CSPReportingServlet">
        <table>
            <tr>
                <td><label for="cspReporting" title="Name">Name</label></td>
                <td><input type="text" id="cspReporting" name="cspReporting" class="text-input" /></td>
                <td><input type="submit" name="submit" value="Submit" /></td>
            </tr>
        </table>
    </form>
</body>
</html>
