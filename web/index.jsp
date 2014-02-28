<%--
  Created by IntelliJ IDEA.
  Auth: razoriii
  Date: 25.02.14
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST");
%>

<link rel="stylesheet" type="text/css" href="styles.css"/>
<script src="js/jquery.js"></script>

<html>
<head>
    <title>MMORPG</title>
</head>
<script type="text/javascript">
    function sendRequest(action) {
        var login = $('#login').val();
        var password = $('#password').val();
        var server = $('#server').val();
        var jsonObj = JSON.stringify({
            action: action,
            login: login,
            password: password
        });
        $.ajax({
            type: 'POST',
            url: server,
            data: jsonObj,
            success: function (data) {
                var ans_field = $('#ans_field');
                var act_field = $('#act_field');
                if (data.result == 'ok') {
                    ans_field.attr('class', 'green');
                    act_field.attr('class', 'green');
                } else {
                    ans_field.attr('class', 'red');
                    act_field.attr('class', 'red');
                }
                ans_field.text('Результат: ' + data.result);
                act_field.text('Действие: ' + data.action)
            },
            contentType: 'application/json',
            mimeType: 'application/json',
            dataType: 'json'
        });
    }

    function registerButtonClick() {
        sendRequest('register');
    }

    function loginButtonClick() {
        sendRequest('login');
    }
</script>
<body>
<div id="floater">&nbsp;</div>
<div id="center_block">
    <div><p id="act_field"></p></div>
    <div><p id="ans_field"></p></div>
    <label for="server">Сервер:</label>
    <input id="server" value="http://localhost:8080/doaction.jsp" /><br/>
    <label for="login">Логин:</label>
    <input id="login"/><br/>
    <label for="password">Пароль:</label>
    <input id="password" type="password"/><br/>
    <button class="button red" onclick="registerButtonClick();">Регистрация</button>
    <button class="button green" onclick="loginButtonClick();">Вход</button>
</div>
</body>
</html>
