<%@ page import="UserData.User" %>
<%--
  Created by IntelliJ IDEA.
  User: razoriii
  Date: 26.02.14
  Time: 2:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TEST</title>
</head>
<script type="text/javascript">
    function sendRequest(login, pass, action, expected_result) {
        var xhr = new XMLHttpRequest();

//        xhr.timeout = 2000;
        xhr.open('POST', '/doaction.jsp', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.send(
                "<%=User._param_login%>=" + encodeURIComponent(login) +
                        "&<%=User._param_password%>=" + encodeURIComponent(pass) +
                        "&<%=User._param_action%>=" + encodeURIComponent(action)
        );
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var obj = JSON.parse(xhr.responseText);
                    var ans = 'FAIL!';
                    if (obj.result == expected_result) {
                        ans = 'OK!';
                    }
                    document.getElementById("test_ans").innerHTML =
                            document.getElementById("test_ans").innerHTML +
                                    "-------------------<br>[" +
                                    ans +
                                    "][Action=" + action + "][Login=" + login + "][Password=" + pass + "]" +
                                    xhr.responseText +
                                    "<br>";

                }
            }
        };
    }

    function buttonClicked() {
        document.title = "Runing";
        var actionLogin = "<%=User._action_login%>";
        var actionRegistr = "<%=User._action_registration%>";
        sendRequest('test', '123', actionRegistr, "<%=User._message_bad_pass%>");
        sendRequest('test', '123123', actionLogin, "<%=User._message_wrong_pass%>");
        sendRequest('', '123123', actionRegistr, "<%=User._message_bad_login%>");
        sendRequest('', '', actionRegistr, "<%=User._message_bad_login%>");
        sendRequest('test', '', actionRegistr, "<%=User._message_bad_pass%>");
        sendRequest('test$%', '123123', actionRegistr, "<%=User._message_bad_login%>");
        sendRequest('test_acc', '123123', actionLogin, "<%=User._message_wrong_pass%>");

        sendRequest('test_acc', '123123', actionRegistr, "<%=User._message_ok%>");
        sendRequest('test_acc', '123123', actionRegistr, "<%=User._message_login_exists%>");

        sendRequest('test_acc', '0000000', actionLogin, "<%=User._message_wrong_pass%>");
        sendRequest('test_acc', '123123', actionLogin, "<%=User._message_ok%>");

        document.title = "Done";
    }
</script>
<body>
<input type="button" value="Start Tests" id="btn3" onclick="buttonClicked();"/>

<div id="test_ans">

</div>
</body>
</html>
