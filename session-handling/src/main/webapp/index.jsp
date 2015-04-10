<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>Session Handling</title>
</head>
<body>
	<h1>Session Handling</h1>

	<p>Use any data to (fake) login.</p>

	<form name="login" method="post" action="LoginServlet">
		<label for="username" title="Username">Username</label>
		<input type="text" id="username" name="username" class="text-input" />
		<label for="password" title="Password">Password</label>
		<input type="password" id="password" name="password" class="text-input" />
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
