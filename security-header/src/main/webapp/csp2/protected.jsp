<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Content Security Policy Level 2: Protected</title>
</head>
<body>
<iframe src="protectedForm.jsp" id="form" style="border: 0; width: 100%; height: 100%">
</iframe>
<div style="position: absolute; top: 0; left: 0; width: 50%; height: 50%; border: 5pt solid orange;">
    <form name="fakeLoginForm" action="FakeServlet" method="post">
        <div style="position: absolute; top: 30; left: 190;">
            <input style="width: 175px; height: 25px;" type="text" name="username" id="username"/>
        </div>

        <div style="position: absolute; top: 72; left: 190;">
            <input style="width: 175px; height: 25px;" type="password" name="password" id="password"/>
        </div>
        <div id="hiddenButton" style="cursor: pointer; position: absolute; top: 105; left: 275; z-index: 1; width: 90; height: 40; border: 5pt solid red;">
            <input type="submit" value="Send" style="position: relative; top: 5; left: 5px; width: 80px; height: 30px; cursor: pointer; "/>
        </div>
    </form>
</div>
</body>
</html>
