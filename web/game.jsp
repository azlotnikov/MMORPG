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

var thisURL = "localhost:8080/MMORPG_war_exploded";
var Game = {};

Game.socket = null;
Game.sid = getUrlVars()["sid"];
Game.tick = '10';

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function(m,key,value) {
            vars[key] = value;
        });
    return vars;
};

Game.initialize = function () {
    window.addEventListener('keydown', function (e) {
        var code = e.keyCode;
        if (code > 36 && code < 41) {
            switch (code) {
                case 113:
                    Game.logOut();
                    break;
                case 37:
                    Game.move('west');
                    break;
                case 38:
                    Game.move('north');
                    break;
                case 39:
                    Game.move('east');
                    break;
                case 40:
                    Game.move('south');
                    break;
            }
        }
    }, false);
    if (window.location.protocol == 'http:') {
        Game.connect('ws://' + thisURL + '/game');
    } else {
        Game.connect('wss://' + thisURL + '/game');
    }
};

Game.connect = (function (host) {
    if ('WebSocket' in window) {
        Game.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Game.socket = new MozWebSocket(host);
    } else {
        alert('Error: WebSocket is not supported by this browser.');
        return;
    }

    Game.socket.onopen = function () {
        alert('Info: WebSocket connection opened.');
        Game.getDictionary();
    };

    Game.socket.onclose = function () {
        alert('Info: WebSocket closed.');
    };

    Game.socket.onmessage = function (message) {
        var packet = JSON.parse(message.data);
        if (packet.tick != '') {
            Game.tick = packet.tick;
//            Game.look();
        }
        switch (packet.action) {
            case 'getDictionary':
                Game.dictionary = packet.dictionary;
//                for (var key in packet.dictionary) {
//                    alert(key +" "+ packet.key);
//                }
                break;
            case 'look':
                Game.draw(packet.map, packet.actors);
                break;
        }
    };
});

Game.look = function () {
    var jsonObj = JSON.stringify({
        action: "look",
        sid: Game.sid
    });
    Game.socket.send(jsonObj);
}

Game.move = function (direction) {
    var jsonObj = JSON.stringify({
        action: "move",
        sid: Game.sid,
        direction: direction,
        tick: Game.tick
    });
    Game.socket.send(jsonObj);
};

Game.getDictionary = function () {
    var jsonObj = JSON.stringify({
        action: "getDictionary",
        sid: Game.sid
    });
    Game.socket.send(jsonObj);
};

Game.logOut = function () {
    var jsonObj = JSON.stringify({
        action: "logout",
        sid: Game.sid
    });
    Game.socket.send(jsonObj);
};

Game.draw = function (map, actors) {

}

Game.initialize();

</script>
</body>
</html>