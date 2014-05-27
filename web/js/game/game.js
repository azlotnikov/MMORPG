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

function toFixed(value, precision) {
    var power = Math.pow(10, precision || 0);
    return String(Math.round(value * power) / power);
}

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
            this.view.setProjectiles(msg.projectiles);
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
            document.getElementById('player_level').innerHTML = msg.player.level;
            document.getElementById('player_x').innerHTML = toFixed(msg.x, 4);
            document.getElementById('player_y').innerHTML = toFixed(msg.y, 4);
            document.getElementById('player_hp').innerHTML = toFixed(msg.player.hp, 2);
            document.getElementById('player_max_hp').innerHTML = toFixed(msg.player.maxHp, 2);
            document.getElementById('hp_progress').max = msg.player.maxHp;
            document.getElementById('hp_progress').value = msg.player.hp;
            document.getElementById('player_regen_hp').innerHTML = toFixed(msg.player.regenHp, 4);
            document.getElementById('player_mana').innerHTML = toFixed(msg.player.mana, 2);
            document.getElementById('player_max_mana').innerHTML = toFixed(msg.player.maxMana, 2);
            document.getElementById('mana_progress').max = msg.player.maxMana;
            document.getElementById('mana_progress').value = msg.player.mana;
            document.getElementById('player_regen_mana').innerHTML = toFixed(msg.player.regenMana, 4);
            document.getElementById('player_damage').innerHTML = toFixed(msg.player.damage, 4);
            document.getElementById('player_speed').innerHTML = toFixed(msg.player.speed, 4);
            document.getElementById('player_strength').innerHTML = msg.player.strength;
            document.getElementById('player_agility').innerHTML = msg.player.agility;
            document.getElementById('player_intelligence').innerHTML = msg.player.intelligence;
            document.getElementById('player_attack_delay').innerHTML = toFixed(msg.player.attackDelay, 5);
            break;
        case 'examine':
            if (msg.examineType == 'monster') {
                document.getElementById('examine').innerHTML =
                    '<div>ID: <span class="value">' + msg.id + '</span></div>' +
                    '<div>X: <span class="value">' + toFixed(msg.x, 4) + '</span> Y: <span class="value">' + toFixed(msg.y, 4) + '</span></div>' +
                    '<div>Имя: <span class="value">' + msg.name + '</span> Тип: <span class="value">' + msg.type + '</span></div>' +
                    '<div>Опасность: <span class="value red">' + msg.alertness + '</span></div>' +
                    '<div>Скорость передвижения: <span class="value red">' + toFixed(msg.speed, 4) + '</span></div>' +
                    '<div>Атака: <span class="value red">' + toFixed(msg.damage, 4) + '</span></div>' +
                    '<div>Здоровье: <span class="value red">' + toFixed(msg.hp, 2) + '</span> / <span class="value"></span> ' + toFixed(msg.maxHp, 2) + '</span></div>' +
                    '<div>Реген здоровья в секунду: <span class="value red">' + toFixed(msg.regenHp, 4) + '</span></div>' +
                    '<div>Скорость атаки: <span class="value red">' + toFixed(msg.attackDelay, 5) + '</span></div>' +
                    '<div>Опыт за убийство: <span class="value red">' + msg.expKill + '</span></div>' +
                    '<div>Мана: <span class="value red">' + toFixed(msg.mana, 2) + '</span> ' + toFixed(msg.maxMana, 2) + '</span></div>' +
                    '<div>Реген маны в секунду: <span class="value red">' + toFixed(msg.regenMana, 4) + '</span></div>';

                if (msg.playerClass != undefined) {
                    document.getElementById('examine_player').innerHTML =
                        '<div>Класс: <span class="value red">' + msg.playerClass + '</span></div>' +
                        '<div>Сила: <span class="value red">' + msg.strength + '</span></div>' +
                        '<div>Ловкость: <span class="value red">' + msg.agility + '</span></div>' +
                        '<div>Интелект: <span class="value red">' + msg.intelligence + '</span></div>';

                }
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

Game.prototype.checkKeyboard = function () {
    if (this.keysDown[65]) this.move('west');
    if (this.keysDown[87]) this.move('north');
    if (this.keysDown[68]) this.move('east');
    if (this.keysDown[83]) this.move('south');
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
    if (code == 81) {
        game.logOut();
    } else {
        game.keysDown[code] = true;
        e.preventDefault();
    }
};

document.onkeyup = function (e) {
    e = e || event;
    var code = e.keyCode;

    game.keysDown[code] = false;
    e.preventDefault();
    
};

game.initGame();