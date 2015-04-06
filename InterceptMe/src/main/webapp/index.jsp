<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>InterceptMe</title>
</head>
<body>
	<h1>InterceptMe</h1>

    <p>Target is to send the word <i>inject</i> (lowercase) to the backend so that the servlet returns <i>SUCCESS</i> (uppercase).</p>

    <form action="form" id="InjectForm" method="post">
        <fieldset>
            <legend>Enter your name</legend>
            <label for="name">Name</label>
            <input type="text" id="name" name="name" pattern="^(?!.*inject).*$" />
            <input type="submit" value=" Send" />
        </fieldset>
    </form>
</body>
</html>
