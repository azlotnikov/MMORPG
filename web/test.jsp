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
    function sendRequest(login, pass, action) {
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
                    document.getElementById("test_ans").innerHTML =
                            document.getElementById("test_ans").innerHTML +
                                    "-------------------<br>[Action=" + action + "][Login=" + login + "][Password=" + pass + "]" +
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
        sendRequest('razor', 'pkjnybrjd', actionLogin);
        sendRequest('razor', 'pkjnybrjd', actionLogin);
        sendRequest('razor', '123456', actionLogin);
        sendRequest('raZoR', 'pkjnybrjd', actionLogin);
        sendRequest('razor', '123', actionLogin);
        sendRequest('ra', '123', actionLogin);
        sendRequest('razor_', '1232123', actionLogin);
        sendRequest('razor_%$', '1232123', actionLogin);
        document.title = "Done";
    }
</script>
<body>
<input type="button" value="Start Tests" id="btn3" onclick="buttonClicked();"/>

<div id="test_ans">

</div>
</body>
</html>
