function Game() {
    this.view = null;
    this.socket = null;
    this.sid = '';
    this.socketUrl = '';
    this.playerId = '';
    this.tick = -1;
    this.prevTick = -1;
    this.keysDown = {};
}

Game.prototype.initGame = function () {
    this.sid = getUrlVars()['sid'];
    this.socketUrl = getUrlVars()['websocket'];
    this.playerId = getUrlVars()['id'];
    this.view = new View();
    if ('WebSocket' in window) {
        this.socket = new WebSocket(this.socketUrl);
    } else {
        alert('Error: WebSocket is not supported by this browser.');
        exitGame();
        return;
    }

    this.socket.onopen = function () {
        game.getDictionary();
    };

    this.socket.onclose = function () {
        game.view.setActors({});
        game.view.setMap({});
        game.view.updateView(game.playerId);
        exitGame();
    };

    this.socket.onmessage = function (message) {
        game.receiveMsg(JSON.parse(message.data));
    };

    requestAnimFrame(animate);
};

Game.prototype.sendMsg = function (msg) {
    this.socket.send(JSON.stringify(msg));
};

Game.prototype.getDictionary = function () {
    this.sendMsg({
        action: "getDictionary",
        sid: this.sid
    });
};

Game.prototype.look = function () {
    this.sendMsg({
        action: "look",
        sid: this.sid
    });
};

Game.prototype.examine = function (id) {
    this.sendMsg({
        action: "examine",
        id: id,
        sid: this.sid
    });
};

Game.prototype.move = function (direction) {
    this.sendMsg({
        action: "move",
        direction: direction,
        tick: this.tick,
        sid: this.sid
    });
};

Game.prototype.logOut = function () {
    this.sendMsg({
        action: "logout",
        sid: this.sid
    });
};

Game.prototype.checkKeyboard = function () {
    if (this.keysDown[37]) this.move('west');
    if (this.keysDown[38]) this.move('north');
    if (this.keysDown[39]) this.move('east');
    if (this.keysDown[40]) this.move('south');
};

Game.prototype.receiveMsg = function (msg) {
    if (msg.hasOwnProperty('tick')) {
        this.tick = msg.tick;
        this.look();
        return;
    }
    if (!msg.hasOwnProperty('result')) return;
    if (msg.result == 'badSid') {
        alert('Invalid sid!');
        exitGame();
        return;
    }
    switch (msg.action) {
        case 'getDictionary':
            this.view.setDictionary(msg.dictionary);
            break;
        case 'look':
            this.view.setMap(msg.map);
            this.view.setActors(msg.actors);
            this.view.setPlayerLocation(msg.x, msg.y);
            break;
    }
};

game = new Game();

function animate() {
    game.checkKeyboard();

    if (game.tick != game.prevTick) {
        game.prevTick = game.tick;
//        alert(game.prevTick);
        game.view.updateView(game.playerId);
    }

    requestAnimFrame(animate);
}

document.onkeydown = function (e) {
    e = e || event;
    var code = e.keyCode;
    if (code == 65) {
        game.logOut();
    }
    if (code > 36 && code < 41) {
        game.keysDown[code] = true;
        e.preventDefault();
    }
};

document.onkeyup = function (e) {
    e = e || event;
    var code = e.keyCode;
    if (code > 36 && code < 41) {
        game.keysDown[code] = false;
        e.preventDefault();
    }
};

game.initGame();

