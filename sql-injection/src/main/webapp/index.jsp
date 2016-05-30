<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>SQL Injection</title>
</head>
<body>
	<h1>SQL Injection</h1>

	<h2>Instructions</h2>

    <p>Try to attack the database with the following input fields and to return more than one result or manipulating its entries.</p>

    <p><strong>Valid customers are:</strong> Arthur Dent, Ford Prefect, Tricia Trillian McMillan, Zaphod Beeblebrox,
        Marvin, Slartibartfast.</p>

	<form name="stmt" method="post" action="StatementServlet">
		<fieldset>
			<legend>Using a simple Statement</legend>
				<label for="stmt" title="Name">Name</label>
				<input type="text" id="stmt" name="name" class="text-input" />
				<input type="submit" value="Search" />
		</fieldset>
	</form>

    <form name="stmtEsc" method="post" action="StatementEscapingServlet">
        <fieldset>
			<legend>Using an escaped Statement</legend>
                <label for="stmtEsc" title="Name">Name</label>
                <input type="text" id="stmtEsc" name="name" class="text-input" />
                <input type="submit" value="Search" />
        </fieldset>
    </form>

	<form name="pstmt" method="post" action="PreparedStatementServlet">
		<fieldset>
			<legend>Using a Prepared Statement</legend>
				<label for="pstmt" title="Name">Name</label>
				<input type="text" id="pstmt" name="name" class="text-input" />
				<input type="submit" name="submit" value="Search" />
		</fieldset>
	</form>

    <form name="hql" method="post" action="HQLServlet">
        <fieldset>
			<legend>Using the Hibernate Query Language (HQL)</legend>
               <label for="hql" title="Name">Name</label>
               <input type="text" id="hql" name="name" class="text-input" />
               <input type="submit" name="submit" value="Search" />
        </fieldset>
    </form>
</body>
</html>
