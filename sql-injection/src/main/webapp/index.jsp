<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="resources/css/styles.css" />
	<title>SQL Injection</title>
</head>
<body>
	<h1>SQL Injection</h1>

    <p><strong>Valid customers are:</strong> Arthur Dent, Ford Prefect, Tricia Trillian McMillan, Zaphod Beeblebrox, Marvin, Slartibartfast</p>

	<h2>Using Statement</h2>
	
	<form name="stmt" method="post" action="StatementServlet">
		<table>
			<tr>
				<td><label for="stmt" title="Name">Name</label></td>
				<td><input type="text" id="stmt" name="name" class="text-input" /></td>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

    <h2>Using Statement with Escaping</h2>

    <form name="stmtEsc" method="post" action="StatementEscapingServlet">
        <table>
            <tr>
                <td><label for="stmtEsc" title="Name">Name</label></td>
                <td><input type="text" id="stmtEsc" name="name" class="text-input" /></td>
                <td><input type="submit" value="Submit" /></td>
            </tr>
        </table>
    </form>

	<h2>Using Prepared Statement</h2>
	
	<form name="pstmt" method="post" action="PreparedStatementServlet">
		<table>
			<tr>
				<td><label for="pstmt" title="Name">Name</label></td>
				<td><input type="text" id="pstmt" name="name" class="text-input" /></td>
				<td><input type="submit" name="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

    <h2>Using Hibernate Query Language (HQL)</h2>

    <form name="hql" method="post" action="HQLServlet">
        <table>
            <tr>
                <td><label for="hql" title="Name">Name</label></td>
                <td><input type="text" id="hql" name="name" class="text-input" /></td>
                <td><input type="submit" name="submit" value="Submit" /></td>
            </tr>
        </table>
    </form>
</body>
</html>
