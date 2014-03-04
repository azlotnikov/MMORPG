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

<link rel="stylesheet" type="text/css" href="styles/style.css"/>
<script src="js/jquery.js"></script>

<!DOCTYPE html>
<head>
    <title>MMORPG</title>
</head>
<script type="text/javascript">
    function goToGame(url, sid){
<<<<<<< HEAD
        var newGame = window.open(url + "?sid=" + sid);
=======
        var newGame = window.open(url, 'Game', sid);
        newGame.focus;
>>>>>>> 04b605b74555640571c9289f1160bbd0874b1a62
    }

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
                    // make normal url
<<<<<<< HEAD
                    goToGame("http://localhost:8080/MMORPG_war_exploded/game.jsp", data.sid);
=======
                    goToGame('localhost:8080/game_.jsp', data.sid);
>>>>>>> 04b605b74555640571c9289f1160bbd0874b1a62
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
<<<<<<< HEAD
    <input id="server" value="http://localhost:8080/MMORPG_war_exploded/doaction.jsp" /><br/>
=======
    <input id="server" value="http://localhost:8080/MMORPG_war_exploded3/doaction.jsp" /><br/>
>>>>>>> 04b605b74555640571c9289f1160bbd0874b1a62
    <label for="login">Логин:</label>
    <input id="login"/><br/>
    <label for="password">Пароль:</label>
    <input id="password" type="password"/><br/>
    <button class="button red" onclick="registerButtonClick();">Регистрация</button>
    <button class="button green" onclick="loginButtonClick();">Вход</button>
    <button class="button" onclick="javascript:location.assign(window.location.pathname.substr(1) + '/tests/test_1.jsp');">Тесты</button>
</div>
</body>
</html>
