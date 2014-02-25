<%--
  Created by IntelliJ IDEA.
  User: razoriii
  Date: 25.02.14
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" type="text/css" href="styles.css"/>

<html>
<head>
    <title>MMORPG</title>
</head>
<body>
<label>Registration</label>
<form method="post" action="doaction.jsp">
    <label for="r_login">Login:</label>
    <input name="login" id="r_login"/><br/>
    <label for="r_password">Password:</label>
    <input name="password" id="r_password" type="password"/><br/>
    <input hidden="hidden" name="action" value="registr">
    <button type="submit">Registr</button>
</form>

<form method="post" action="doaction.jsp">
    <label for="l_login">Login:</label>
    <input name="login" id="l_login"/><br/>
    <label for="l_password">Password:</label>
    <input name="password" id="l_password" type="password"/><br/>
    <input hidden="hidden" name="action" value="login">
    <button type="submit">Login</button>
</form>

</body>
</html>
