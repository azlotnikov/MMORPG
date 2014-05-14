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

Game.prototype.attack = function (x, y) {
    this.sendMsg({
        action: "attack",
        x: x,
        y: y,
        sid: this.sid
    });
};

Game.prototype.pickUp = function (itemID) {
    this.sendMsg({
        action: "pickUp",
        id: itemID,
        sid: this.sid
    });
};

Game.prototype.dropItem = function (itemID) {
    this.sendMsg({
        action: "drop",
        id: itemID,
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
            this.view.setItems(msg.items);
            if (JSON.stringify(msg.player.inventory) != JSON.stringify(this.view.inventory)) {
//                console.log(JSON.stringify(msg.inventory));
                this.view.setInventory(msg.player.inventory);
                this.view.setUpdateInventory();
//                console.log('!');
            }
            this.view.setActors(msg.actors);
            this.view.setPlayerLocation(msg.x, msg.y);
            document.getElementById('exp_progress').max = msg.player.expNextLevel;
            document.getElementById('exp_progress').value = msg.player.expLevel;
            document.getElementById('level').innerHTML = msg.player.level;
            document.getElementById('player_x').innerHTML = msg.x;
            document.getElementById('player_y').innerHTML = msg.y;
            document.getElementById('player_hp').innerHTML = msg.player.hp;
            document.getElementById('player_hp_bonus').innerHTML = msg.player.hpBonus;
            document.getElementById('player_damage').innerHTML = msg.player.damage;
            document.getElementById('player_damage_bonus').innerHTML = msg.player.damageBonus;
            document.getElementById('player_speed').innerHTML = msg.player.speed;
            document.getElementById('player_speed_bonus').innerHTML = msg.player.speedBonus;
            break;
        case 'examine':
            if (msg.examineType == 'monster') {
                document.getElementById('examine').innerHTML =
                    '<div>ID: <span class="value">' + msg.id + '</span></div>' +
                    '<div>Имя: <span class="value">' + msg.name + '</span></div>' +
                    '<div>Тип: <span class="value">' + msg.type + '</span></div>' +
                    '<div>Опасность: <span class="value red">' + msg.alertness + '</span></div>' +
                    '<div>Скорость: <span class="value red">' + msg.speed + '</span></div>' +
                    '<div>Аттака: <span class="value red">' + msg.damage + '</span></div>' +
                    '<div>Здоровье: <span class="value red">' + msg.hp + '</span></div>' +
                    '<div>X: <span class="value">' + msg.x + '</span></div>' +
                    '<div>Y: <span class="value">' + msg.y + '</span></div>';
            }

            if (msg.examineType == 'item') {
                document.getElementById('examine').innerHTML =
                    '<div>ID: <span class="value">' + msg.id + '</span></div>' +
                    '<div>Имя: <span class="value">' + msg.name + '</span></div>' +
                    '<div>Тип: <span class="value">' + msg.type + '</span></div>' +
                    '<div>Описание: <span class="value">' + msg.description + '</span></div>';

            }

            break;
    }
};

game = new Game();

function animate() {
    game.checkKeyboard();

    if (game.tick != game.prevTick) {
        game.prevTick = game.tick;
        game.view.updateView();
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