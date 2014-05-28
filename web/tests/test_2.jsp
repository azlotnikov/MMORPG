<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 5/28/14
  Time: 1:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Tests</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/mocha.css" />
    <script type="text/javascript" src="../js/lib/mocha.js"></script>
    <script type="text/javascript">
        mocha.setup('bdd')
    </script>
    <script type="text/javascript" src="../js/lib/chai.js"></script>
    <script data-main="../js/apps/tests_app" src="../js/lib/require.js"></script>
    <script src="../js/lib/jquery.js"></script>
</head>
<body>
<form>
    <label for="url">Введите URL:</label>
    <input id="url" type="text" />
    <input id="urlBtn" type="button" value="Протестировать" />
</form>
    <div id="mocha"></div>
</body>
</html>