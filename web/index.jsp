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
    <label for="login">Login:</label>
    <input name="login" id="login"/><br/>
    <label for="password">Password:</label>
    <input name="password" id="password"/><br/>
    <input hidden="hidden" name="action" value="doRegistration">
    <button type="submit">Submit</button>
</form>
</body>
</html>
