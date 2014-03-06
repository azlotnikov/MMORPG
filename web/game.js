/**
 * Created by Alexander on 3/6/14.
 */

"use strict";

var webSocketUrl = getUrlVars()["websocket"];
var GAME_URL = 'http://localhost:8080/MMORPG_war_exploded';
var Game = {};

Game.socket = null;
Game.sid = getUrlVars()["sid"];
Game.tick = 10;
Game.playerId = -1;
Game.tileSize = 100;
Game.actorHalfSize = 40;
Game.context = document.getElementById('playground').getContext('2d');

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function (m, key, value) {
            vars[key] = value;
        });
    return vars;
}

Game.initialize = function () {
    window.addEventListener('keydown', function (e) {
        var code = e.keyCode;
        if (code > 36 && code < 66) {
            switch (code) {
                case 65:
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

    Game.connect(webSocketUrl);

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
        //alert('Info: WebSocket connection opened.');
        Game.getDictionary();
    };

    Game.socket.onclose = function () {
        alert('Info: WebSocket closed.');
    };

    Game.socket.onmessage = function (message) {
        var packet = JSON.parse(message.data);
        if (packet.hasOwnProperty('tick')) {
            Game.tick = packet.tick;
            Game.look();
        } else
            switch (packet.action) {
                case 'getDictionary':
                    Game.dictionary = packet.dictionary;
                    break;
                case 'look':
                    Game.draw(packet.map, packet.actors);
                    break;
                case 'move':
                    break;
                case 'examine':
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
};


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

function drawImg(imgSrc, posX, posY) {
    var img = new Image();
    img.onload = function () {
        Game.context.drawImage(img, posX, posY);
    };
    img.src = imgSrc;
}


Game.draw = function (map, actors) {
    var curHeight = 0;

    for (var i in map) {
        var curWidth = 0;
        for (var j in map[i]) {
            drawImg(GAME_URL + '/img/' + Game.dictionary[map[i][j]] + '.png', curWidth, curHeight);
            curWidth += Game.tileSize;
        }
        curHeight += Game.tileSize;
    }

    for (var t in actors) {
        drawImg(GAME_URL + '/img/player.png',
            actors[t].x * Game.tileSize - Game.actorHalfSize, actors[t].y * Game.tileSize - Game.actorHalfSize)
    }
}

Game.initialize();