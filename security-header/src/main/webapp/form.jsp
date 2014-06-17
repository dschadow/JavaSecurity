<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>Security Response Header</title>
</head>
<body>
<form name="contactForm" action="FeedbackServlet" method="post">
    <div>
        <label for="username">Username</label>
        <input type="text" name="username" id="username"/>
    </div>

    <div>
        <label for="subject">Subject</label>
        <input type="text" name="subject" id="subject"/>
    </div>

    <div>
        <label for="message">Message</label>
        <textarea name="message" id="message"></textarea>
    </div>

    <div>
        <input type="submit" value="Send"/>
    </div>
</form>
</body>
</html>
