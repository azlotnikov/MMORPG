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
<div class="noscript"><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on
    Javascript being enabled. Please enable
    Javascript and reload this page!</h2></div>
<div style="float: left">
    <canvas id="playground" width="640" height="480"/>
</div>
<script type="application/javascript">
"use strict";

var Game = {};

Game.fps = 30;
Game.socket = null;
Game.nextFrame = null;
Game.interval = null;
Game.gridSize = 10;
Game.sid = "";
Game.tick = -1;

function Snake() {
    this.snakeBody = [];
    this.color = null;
}

Snake.prototype.draw = function (context) {
    for (var id in this.snakeBody) {
        context.fillStyle = this.color;
        context.fillRect(this.snakeBody[id].x, this.snakeBody[id].y, Game.gridSize, Game.gridSize);
    }
};

Game.initialize = function () {
    this.entities = [];
    var canvas = document.getElementById('playground');
    if (!canvas.getContext) {
//        Console.log('Error: 2d canvas not supported by this browser.');
        return;
    }
    this.context = canvas.getContext('2d');
    window.addEventListener('keydown', function (e) {
        var code = e.keyCode;
        if (code > 36 && code < 41) {
            switch (code) {
                case 37:
                    Game.moveCharacter('west');
                    break;
                case 38:
                    Game.moveCharacter('north');
                    break;
                case 39:
                    Game.moveCharacter('east');
                    break;
                case 40:
                    Game.moveCharacter('south');
                    break;
            }
        }
    }, false);
    if (window.location.protocol == 'http:') {
        Game.connect('ws://' + window.location.host + window.location.pathname.substr(1) + '/game');
    } else {
        Game.connect('wss://' + window.location.host + window.location.pathname.substr(1) + '/game');
    }
};

Game.moveCharacter = function (direction) {
    var jsonObj = JSON.stringify({
        action: "move",
        sid: Game.sid,
        direction: direction,
        tick: Game.tick
    });
    Game.socket.send(jsonObj);
};

Game.startGameLoop = function () {
    if (window.webkitRequestAnimationFrame) {
        Game.nextFrame = function () {
            webkitRequestAnimationFrame(Game.run);
        };
    } else if (window.mozRequestAnimationFrame) {
        Game.nextFrame = function () {
            mozRequestAnimationFrame(Game.run);
        };
    } else {
        Game.interval = setInterval(Game.run, 1000 / Game.fps);
    }
    if (Game.nextFrame != null) {
        Game.nextFrame();
    }
};

Game.stopGameLoop = function () {
    Game.nextFrame = null;
    if (Game.interval != null) {
        clearInterval(Game.interval);
    }
};

Game.draw = function () {
    this.context.clearRect(0, 0, 640, 480);
    for (var id in this.entities) {
        this.entities[id].draw(this.context);
    }
};

Game.addSnake = function (id, color) {
    Game.entities[id] = new Snake();
    Game.entities[id].color = color;
};

Game.updateSnake = function (id, snakeBody) {
    if (typeof Game.entities[id] != "undefined") {
        Game.entities[id].snakeBody = snakeBody;
    }
};

Game.removeSnake = function (id) {
    Game.entities[id] = null;
    // Force GC.
    delete Game.entities[id];
};

Game.run = (function () {
    var skipTicks = 1000 / Game.fps, nextGameTick = (new Date).getTime();

    return function () {
        while ((new Date).getTime() > nextGameTick) {
            nextGameTick += skipTicks;
        }
        Game.draw();
        if (Game.nextFrame != null) {
            Game.nextFrame();
        }
    };
})();

Game.connect = (function (host) {
    if ('WebSocket' in window) {
        Game.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Game.socket = new MozWebSocket(host);
    } else {
//        Console.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    Game.socket.onopen = function () {
        // Socket open.. start the game loop.
//        Console.log('Info: WebSocket connection opened.');
//        Console.log('Info: Press an arrow key to begin.');
        Game.startGameLoop();
        setInterval(function () {
            // Prevent server read timeout.
//            Game.socket.send('ping');
        }, 5000);
    };

    Game.socket.onclose = function () {
//        Console.log('Info: WebSocket closed.');
        Game.stopGameLoop();
    };

    Game.socket.onmessage = function (message) {
        var packet = JSON.parse(message.data);
//        switch (packet.type) {
//            case 'update':
//                for (var i = 0; i < packet.data.length; i++) {
//                    Game.updateSnake(packet.data[i].id, packet.data[i].body);
//                }
//                break;
//            case 'join':
//                for (var j = 0; j < packet.data.length; j++) {
//                    Game.addSnake(packet.data[j].id, packet.data[j].color);
//                }
//                break;
//            case 'leave':
//                Game.removeSnake(packet.id);
//                break;
//            case 'dead':
////                Console.log('Info: Your snake is dead, bad luck!');
//                Game.direction = 'none';
//                break;
//            case 'kill':
////                Console.log('Info: Head shot!');
//                break;
//        }
    };
});

Game.initialize();


document.addEventListener("DOMContentLoaded", function () {
    // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
    var noscripts = document.getElementsByClassName("noscript");
    for (var i = 0; i < noscripts.length; i++) {
        noscripts[i].parentNode.removeChild(noscripts[i]);
    }
}, false);

</script>
</body>
</html>