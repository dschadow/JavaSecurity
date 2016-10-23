<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>Intercept Me</title>
</head>
<body>
	<h1>Intercept Me</h1>

    <p>Your task is to send the word <strong>inject</strong> (lowercase) to the backend so that the servlet returns
        <strong>SUCCESS</strong> (uppercase).</p>

    <form action="form" id="InjectForm" method="post">
        <fieldset>
            <legend>Enter Text</legend>
            <label for="name">Text</label>
            <input type="text" id="name" name="name" pattern="^(?!.*inject).*$" />
            <input type="submit" value=" Send" />
        </fieldset>
    </form>
</body>
</html>
