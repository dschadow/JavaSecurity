<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Cross-Site Scripting (XSS)</title>
</head>
<body>
<h1>Cross-Site Scripting (XSS)</h1>

<h2>Instructions</h2>

<p>Enter some attack data to show the user's session id in an alert box. You will not be successful with every form.
    Can you explain why? Can you secure the validated form (<strong>With Input Validation</strong>) with output escaping
    and the Context Security Policy?</p>

<form action="unprotected" id="unprotectedForm" method="post">
    <fieldset>
        <legend>Unprotected</legend>
        <label for="unprotectedName">Name</label>
        <input type="text" id="unprotectedName" name="unprotectedName"/>
        <input type="submit" value="Send"/>
    </fieldset>
</form>

<form action="validated" id="validatedForm" method="post">
    <fieldset>
        <legend>With Input Validation</legend>
        <label for="inputValidatedName">Name</label>
        <input type="text" id="inputValidatedName" name="inputValidatedName" pattern="^[^<>]+$"/>
        <input type="submit" value="Send"/>
    </fieldset>
</form>

<form action="escaped" id="escapedForm" method="post">
    <fieldset>
        <legend>With Output Escaping</legend>
        <label for="outputEscapedName">Name</label>
        <input type="text" id="outputEscapedName" name="outputEscapedName"/>
        <input type="submit" value="Send"/>
    </fieldset>
</form>

<form action="escaped.jsp" id="escapedJSPForm" method="post">
    <fieldset>
        <legend>With JSP Output Escaping</legend>
        <label for="outputEscapedJSPName">Name</label>
        <input type="text" id="outputEscapedJSPName" name="outputEscapedJSPName"/>
        <input type="submit" value="Send"/>
    </fieldset>
</form>

<form action="csp" id="cspForm" method="post">
    <fieldset>
        <legend>With Content Security Policy (CSP)</legend>
        <label for="cspName">Name</label>
        <input type="text" id="cspName" name="cspName"/>
        <input type="submit" value="Send"/>
    </fieldset>
</form>
</body>
</html>
