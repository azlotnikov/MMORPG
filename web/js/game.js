function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function (m, key, value) {
            vars[key] = value;
        });
    return vars;
}

var game = {};
var SIGHT_RADIUS = 10;

game.socket = null;
game.socketurl = getUrlVars()["websocket"];
game.sid = getUrlVars()["sid"];
game.playerId = getUrlVars()["id"];
game.tick = 10;
game.dictionary = {};
game.map = {};
game.actors = {};
game.textures = {};
game.stage = null;
game.render = null;
game.updateStage = false;
game.tileSize = 32;

game.init = function () {
// create an new instance of a pixi stage
    game.stage = new PIXI.Stage(0x97c56e, true);

// create a renderer instance
    game.render = PIXI.autoDetectRenderer(SIGHT_RADIUS * 2 * game.tileSize, SIGHT_RADIUS * 2 * game.tileSize);

// add the renderer view element to the DOM
    document.body.appendChild(game.render.view);
    game.render.view.style.position = "absolute";
    game.render.view.style.top = "0px";
    game.render.view.style.left = "0px";

    window.addEventListener('keydown', function (e) {
        var code = e.keyCode;
        if (code > 36 && code < 66) {
            switch (code) {
                case 65:
                    game.logOut();
                    break;
                case 37:
                    game.move('west');
                    break;
                case 38:
                    game.move('north');
                    break;
                case 39:
                    game.move('east');
                    break;
                case 40:
                    game.move('south');
                    break;
            }
            e.preventDefault();
        }
    }, false);
    game.connect(game.socketurl);

};

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
        //game.getPlayerID();
    };

    game.socket.onclose = function () {
        alert('Info: WebSocket closed.');
    };

    game.socket.onmessage = function (message) {
        var packet = JSON.parse(message.data);
        if (packet.result == "badSid") {
            alert('Invalid sid!');
            exit();
        }
        if (packet.hasOwnProperty('tick')) {
            game.tick = packet.tick;
            game.look();
        } else
            switch (packet.action) {
                case 'getDictionary':
                    game.dictionary = packet.dictionary;
                    for (var i in game.dictionary) {
                        game.textures[i] = PIXI.Texture.fromImage("img/" + game.dictionary[i] + ".png", true);
                    }
                    game.textures['p'] = PIXI.Texture.fromImage("img/player.png", true);
                    break;
                case 'look':
                    game.map = packet.map;
                    game.actors = packet.actors;
                    requestAnimFrame(animate);
                    break;
                case 'move':
                    break;
                case 'examine':
                    break;
//                case 'getPlayerID':
//                    if (packet.result != "badSid")
//                        game.playerId = packet.id;
//                    break;
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

//game.getPlayerID = function () {
//    var jsonObj = JSON.stringify({
//        action: "getPlayerID",
//        sid: game.sid
//    });
//    game.socket.send(jsonObj);
//};

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
    game.map = {};
    game.actors = {};
    requestAnimFrame(animate);
};

game.createTexture = function (x, y, texture) {
    var tile = new PIXI.Sprite(texture);
//    tile.interactive = false;
    tile.anchor.x = 0.5;
    tile.anchor.y = 0.5;
    tile.position.x = x + texture.width / 2;
    tile.position.y = y + texture.width / 2;
    game.stage.addChild(tile);
};

function animate() {

    for (var c = game.stage.children.length - 1; c >= 0; c--) {
        game.stage.removeChild(game.stage.children[c]);
    }
    var offsetX;
    var offsetY;
    // TODO Нужно эффективно узнавать координаты своего игрока
    for (var t in game.actors) {
        if (game.actors[t].id == game.playerId) {
            offsetX = game.actors[t].x;
            offsetY = game.actors[t].y;
            break;
        }
    }

    // TODO Определить единый размер tile
    var curHeight = -(offsetY % 1) * game.tileSize;
    for (var i in game.map) {
        var curWidth = -(offsetX % 1) * game.tileSize;
        for (var j in game.map[i]) {
            game.createTexture(curWidth, curHeight, game.textures[game.map[i][j]]);
            curWidth += game.tileSize;
        }
        curHeight += game.tileSize;
    }

    var currentActor = {};

    for (var t in game.actors) {
        if (game.actors[t].id == game.playerId) {
            currentActor = game.actors[t];
            continue;
        }
        game.createTexture(
            (game.actors[t].x - offsetX + SIGHT_RADIUS) * game.tileSize - game.textures['p'].width / 2
            ,(game.actors[t].y - offsetY + SIGHT_RADIUS) * game.tileSize - game.textures['p'].height / 2
            ,game.textures['p']
        );
    }

    game.createTexture(
        (currentActor.x - offsetX + SIGHT_RADIUS) * game.tileSize - game.textures['p'].width / 2
        ,(currentActor.y - offsetY + SIGHT_RADIUS) * game.tileSize - game.textures['p'].height / 2
        ,game.textures['p']
    );

    game.render.render(game.stage);
}

game.init();