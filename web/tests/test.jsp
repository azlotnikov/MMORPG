<%--
  Created by IntelliJ IDEA.
  User: razoriii
  Date: 23.04.14
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>MonsterQuest - Tests!</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/styles/mocha.css" />
    <script type="text/javascript" src="/js/utils/mocha.js"></script>
    <script type="text/javascript">
        mocha.setup('bdd')
    </script>
    <script type="text/javascript" src="/js/utils/chai.js"></script>
    <script data-main="/js/apps/tests_app" src="/js/require.js"></script>
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
