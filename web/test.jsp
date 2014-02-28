<%@ page import="UserData.Auth" %>
<%--
  Created by IntelliJ IDEA.
  Auth: razoriii
  Date: 26.02.14
  Time: 2:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TEST</title>
</head>
<script src="js/jquery.js"></script>
<script type="text/javascript">
    function sendRequest(login, password, action, expected_result) {
        var jsonObj = JSON.stringify({
            action: action,
            login: login,
            password: password
        });
        $.ajax({
            type: 'POST',
            url: 'doaction.jsp',
            data: jsonObj,
            success: function (data) {
                var test_ans = $('#test_ans');
                var data_text = '|' + data.action + '|' + login + '|' + password;
                if (data.result == expected_result) {
                    test_ans.text(test_ans.text() + 'OK!' + data_text + '|' + data.sid + '<br/>');
                } else {
                    test_ans.text(test_ans.text() + 'FAIL!' + data_text + '<br/>');
                }
            },
            contentType: 'application/json',
            mimeType: 'application/json',
            dataType: 'json'
        });
    }

    function buttonClicked() {
        document.title = 'Running';
        sendRequest('test', '123', 'register', 'badPassword');
        sendRequest('test', '123123', 'login', 'invalidCredentials');
        sendRequest('', '123123', 'register', 'badLogin');
        sendRequest('', '', 'register', 'badLogin');
        sendRequest('test', '', 'register', 'badPassword');
        sendRequest('test$%', '123123', 'register', 'badLogin');
        sendRequest('test_acc', '123123', 'login', 'invalidCredentials');

        sendRequest('test_acc', '123123', 'register', 'ok');
        sendRequest('test_acc', '123123', 'register', 'loginExists');

        sendRequest('test_acc', '0000000', 'login', 'invalidCredentials');
        sendRequest('test_acc', '123123', 'login', 'ok');

        document.title = 'Done';
    }
</script>
<body>
<input type="button" value="Start Tests" id="btn3" onclick="buttonClicked();"/>

<p id="test_ans">

</p>
</body>
</html>
