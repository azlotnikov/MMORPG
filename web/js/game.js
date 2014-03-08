
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function (m, key, value) {
            vars[key] = value;
        });
    return vars;
}

var game = {};
var updateCanvas = false;

game.socket = null;
game.socketurl = getUrlVars()["websocket"];
game.sid = getUrlVars()["sid"];
game.tick = 10;
game.playerId = -1;
game.dictionary = {};
game.tileSize = 100;
game.playerTileSize = 80;
game.map = {};
game.actors = {};

//paper.install(window);
//
//window.onload = function() {
//    paper.setup('myCanvas');
//    alert('1');
//    // Create a simple drawing tool:
//    var gameTool = new Tool();
//
//    // Define a mousedown and mousedrag handler
//    gameTool.onMouseDown = function(event) {
//        // some code
//    }
//
//    gameTool.onMouseDrag = function(event) {
//         // some code
//    }
//
//
//
//};

function onKeyDown(event) {
    if(event.key == 'q') {
        game.logOut();
    }

    if(event.key == 'd') {
        game.move('west');
    }

    if(event.key == 'a') {
        game.move('east');
    }

    if(event.key == 'w') {
        game.move('north');
    }

    if(event.key == 's') {
        game.move('south');
    }
}

game.connect = (function (host) {
    if ('WebSocket' in window) {
        game.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        game.socket = new MozWebSocket(host);
    } else {
        alert('Error: WebSocket is not supported by this browser.');
        return;
    }

    game.socket.onopen = function () {
        //alert('Info: WebSocket connection opened.');
        game.getDictionary();
        game.getPlayerID();
    };

    game.socket.onclose = function () {
        alert('Info: WebSocket closed.');
    };

    game.socket.onmessage = function (message) {
        var packet = JSON.parse(message.data);
        if (packet.hasOwnProperty('tick')) {
            game.tick = packet.tick;
            game.look();
        } else
            switch (packet.action) {
                case 'getDictionary':
                    game.dictionary = packet.dictionary;
                    break;
                case 'look':
                    game.map = packet.map;
                    game.actors = packet.actors;
                    updateCanvas = true;
                    break;
                case 'move':
                    break;
                case 'examine':
                    break;
                case 'getPlayerID':
                    if (packet.result != "badSid")
                        game.playerId = packet.id;
                    break;
            }
    };
});

game.look = function () {
    var jsonObj = JSON.stringify({
        action: "look",
        sid: game.sid
    });
    game.socket.send(jsonObj);
};

game.examine = function (id) {
    var jsonObj = JSON.stringify({
        action: "examine",
        id: id
    });
    game.socket.send(jsonObj);
};

//TODO Внести в протокол получение ID текущего игрока
game.getPlayerID = function () {
    var jsonObj = JSON.stringify({
        action: "getPlayerID",
        sid: game.sid
    });
    game.socket.send(jsonObj);
};

game.move = function (direction) {
    var jsonObj = JSON.stringify({
        action: "move",
        sid: game.sid,
        direction: direction,
        tick: game.tick
    });
    game.socket.send(jsonObj);
};

game.getDictionary = function () {
    var jsonObj = JSON.stringify({
        action: "getDictionary",
        sid: game.sid
    });
    game.socket.send(jsonObj);
};

game.logOut = function () {
    var jsonObj = JSON.stringify({
        action: "logout",
        sid: game.sid
    });
    game.socket.send(jsonObj);
};

function drawImg(imgId, posX, posY) {
//    alert(imgId + " X: " + posX + " Y: " + posY);
    var raster = new Raster(imgId);
    raster.onLoad = function() {
        raster.position.x = posX;
        raster.position.y = posY;
    };
}

function onFrame(event) {
    if(updateCanvas){
//        paper.project.activeLayer.removeChildren();
        updateCanvas = false;
        var curHeight = 0;
        for (var i in game.map) {
            var curWidth = 0;
            for (var j in game.map[i]) {
                drawImg(game.dictionary[game.map[i][j]], curWidth, curHeight);
                curWidth += game.tileSize;
            }
            curHeight += game.tileSize;
        }

        for (var t in game.actors) {
            drawImg('player',
                game.actors[t].x * game.tileSize - game.playerTileSize / 2, game.actors[t].y * game.tileSize - game.playerTileSize / 2)
        }
    }
}

game.connect(game.socketurl);