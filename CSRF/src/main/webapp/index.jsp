<%@ page import="de.dominikschadow.javasecurity.csrf.esapi.EsapiTokenHandler" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css" />
	<title>Cross-Site Request Forgery (CSRF)</title>
</head>
<body>
	<h1>Cross-Site Request Forgery (CSRF)</h1>

    <h2>Spring Security</h2>

    <form name="springSecurityOrder" action="SpringSecurityOrderServlet" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label for="product1" title="Product">Product</label>
        <input type="text" id="product1" name="product1" class="text-input" />

        <label for="quantity1" title="Quantity">Quantity</label>
        <input type="text" id="quantity1" name="quantity1" class="text-input" />

        <input type="submit" value="Order" />
    </form>

    <h2>Enterprise Security API</h2>

    <form name="esapiOrder" action="ESAPIOrderServlet" method="post">
        <input type="hidden" name="<%=EsapiTokenHandler.CSRF_TOKEN%>" value="<%=EsapiTokenHandler.getToken(request.getSession(false))%>">

        <label for="product2" title="Product">Product</label>
        <input type="text" id="product2" name="product2" class="text-input" />

        <label for="quantity2" title="Quantity">Quantity</label>
        <input type="text" id="quantity2" name="quantity2" class="text-input" />

        <input type="submit" value="Order" />
    </form>

</body>
</html>
