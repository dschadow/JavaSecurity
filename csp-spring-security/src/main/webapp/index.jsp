<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>Content Security Policy - Spring Security</title>
</head>
<body>
	<h1>Content Security Policy - Spring Security</h1>

    <form name="orderForm" action="OrderServlet" method="post">
        <fieldset>
            <legend>Order</legend>
            <label for="product" title="Product">Product</label>
            <input type="text" id="product" name="product" class="text-input" />

            <label for="quantity" title="Quantity">Quantity</label>
            <input type="text" id="quantity" name="quantity" class="text-input" />

            <input type="submit" value="Order" />
        </fieldset>
    </form>
</body>
</html>
