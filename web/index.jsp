<%@ page import="UserData.User" %>
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
<div id="floater">&nbsp;</div>
<div id="center_block">
    <%--<p>Registration</p>--%>

    <form method="post" action="doaction.jsp">
        <label for="login">Логин:</label>
        <input name="<%=User._param_login%>" id="login"/><br/>
        <label for="password">Пароль:</label>
        <input name="<%=User._param_password%>" id="password" type="password"/><br/>
        <label for="action">Действие:</label>
        <select class="select" name="<%=User._param_action%>" id="action">
            <option value="<%=User._action_registration%>">Регистрация</option>
            <option value="<%=User._action_login%>">Вход</option>
        </select>
        <button class="button green" type="submit">Выполнить действие</button>
    </form>
</div>
</body>
</html>
