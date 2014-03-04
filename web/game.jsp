<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 3/3/14
  Time: 2:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Monster Quest</title>
    <style type="text/css">
        #playground {
            width: 640px;
            height: 480px;
            background-color: #000;
        }
    </style>
</head>
<body>
<div class="noscript"><h2 style="color: #ff0000">
    Seems your browser doesn't support Javascript!
    Websockets rely on Javascript being enabled.
    Please enable Javascript and reload this page!</h2></div>
<div style="float: left">
    <canvas id="playground" width="640" height="480"/>
</div>

<script type="application/javascript">
"use strict";

var Game = {};

Game.socket = null;
Game.sid = getUrlVars()["sid"];

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function(m,key,value) {
            vars[key] = value;
        });
    return vars;
};

Game.initialize = function () {
    alert(window.location.host + window.location.pathname.substr(1));
    if (window.location.protocol == 'http:') {
        Game.connect('ws://' + window.location.host + window.location.pathname.substr(1) + '/game');
    } else {
        Game.connect('wss://' + window.location.host + window.location.pathname.substr(1) + '/game');
    }
};


</script>
</body>
</html>