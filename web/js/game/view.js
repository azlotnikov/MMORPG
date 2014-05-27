var SIGHT_RADIUS_X = 12;
var SIGHT_RADIUS_Y = 8;
var INVENTORY_SIZE_X = 8;
var INVENTORY_SIZE_Y = 15;
var WEAPON_STAGE_SIZE_X = 3;
var WEAPON_STAGE_SIZE_Y = 1;

function View() {
    this.gameStage = new PIXI.Stage(0x000000, true);
    this.inventoryStage = new PIXI.Stage(0x000000, true);
    this.weaponStage = new PIXI.Stage(0x000000, true);
    this.gameRenderer = PIXI.autoDetectRenderer(SIGHT_RADIUS_X * 2 * TILE_SIZE, SIGHT_RADIUS_Y * 2 * TILE_SIZE);
    this.inventoryRenderer = PIXI.autoDetectRenderer(TILE_SIZE * INVENTORY_SIZE_X, TILE_SIZE * INVENTORY_SIZE_Y);
    this.weaponRenderer = PIXI.autoDetectRenderer(TILE_SIZE * WEAPON_STAGE_SIZE_X, TILE_SIZE * WEAPON_STAGE_SIZE_Y);
    this.textures = new Textures();
    // todo чтобы влезал в экран
    this.map = {};
    this.actors = {};
    this.dictionary = {};
    this.items = {};
    this.inventory = {};
    this.updateInventory = true;
    this.x = 0.0;
    this.y = 0.0;

    document.body.appendChild(this.gameRenderer.view);
    this.gameRenderer.view.style.position = "absolute";
    this.gameRenderer.view.style.top = "0px";
    this.gameRenderer.view.style.left = "0px";
    this.gameRenderer.view.onclick = gameRendererClick;
    this.gameRenderer.view.oncontextmenu = gameRendererClick;

    document.body.appendChild(this.weaponRenderer.view);
    this.weaponRenderer.view.style.position = "absolute";
    this.weaponRenderer.view.style.top = "0px";
    this.weaponRenderer.view.style.left = this.gameRenderer.width + "px";
//    this.inventoryRenderer.view.onclick = inventoryRendererClick;
//    this.inventoryRenderer.view.oncontextmenu = inventoryRendererClick;

    document.body.appendChild(this.inventoryRenderer.view);
    this.inventoryRenderer.view.style.position = "absolute";
    this.inventoryRenderer.view.style.top = "32px";
    this.inventoryRenderer.view.style.left = this.gameRenderer.width + "px";
    this.inventoryRenderer.view.onclick = inventoryRendererClick;
    this.inventoryRenderer.view.oncontextmenu = inventoryRendererClick;
}

View.prototype.getActor = function (x, y) {
    var i;
    for (i in this.actors) {
        if (Math.abs(x - this.actors[i].x + this.x - SIGHT_RADIUS_X) < 0.5
            && Math.abs(y - this.actors[i].y + this.y - SIGHT_RADIUS_Y) < 0.5) {
            return this.actors[i];
        }
    }
};

View.prototype.getItem = function (x, y) {
    var i;
    var item;
    for (i in this.items) {
        if (Math.abs(x - this.items[i].x + this.x - SIGHT_RADIUS_X) < 0.5
            && Math.abs(y - this.items[i].y + this.y - SIGHT_RADIUS_Y) < 0.5) {
            item = this.items[i];
        }
    }
    return item;
};

View.prototype.getInventoryItem = function (x, y) {
    x = Math.floor(x);
    y = Math.floor(y);
    if (this.inventory[y * INVENTORY_SIZE_X + x]) {
        return this.inventory[y * INVENTORY_SIZE_X + x];
    }
};

function gameRendererClick(e) {
    e = e || window.event;
    e.preventDefault();
    var x;
    var y;
    if (e.pageX || e.pageY) {
        x = e.pageX;
        y = e.pageY;
    }
    else {
        x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
        y = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
    }
    x -= game.view.gameRenderer.view.offsetLeft;
    y -= game.view.gameRenderer.view.offsetTop;
    var actor = game.view.getActor(x / TILE_SIZE, y / TILE_SIZE);
    var item = game.view.getItem(x / TILE_SIZE, y / TILE_SIZE);
    if (actor) {
        e.preventDefault();
        if (e.which == 3) {
            game.examine(actor.id);
        } else {
            game.attack(actor.x, actor.y);
        }
    } else if (item) {
        if (e.which == 3) {
            game.examine(item.id);
        } else {
            game.pickUp(item.id);
        }
    } else {
        document.getElementById('examine').innerHTML = "";
        document.getElementById('examine_player').innerHTML = "";
        game.attack(x / TILE_SIZE + game.view.x - SIGHT_RADIUS_X
            , y / TILE_SIZE + game.view.y - SIGHT_RADIUS_Y);
    }
}

function inventoryRendererClick(e) {
    e = e || window.event;
    e.preventDefault();
    var x;
    var y;
    if (e.pageX || e.pageY) {
        x = e.pageX;
        y = e.pageY;
    }
    else {
        x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
        y = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
    }
    x -= game.view.inventoryRenderer.view.offsetLeft;
    y -= game.view.inventoryRenderer.view.offsetTop;
    var item = game.view.getInventoryItem(x / TILE_SIZE, y / TILE_SIZE);
    if (item) {
        if (e.which == 3) {
            game.examine(item.id);
        } else {
            game.dropItem(item.id);
        }
    }
}

View.prototype.drawGameTile = function (x, y, textureName) {
    var tile = new PIXI.Sprite(this.textures.data[textureName]);
    tile.anchor.x = 0.5;
    tile.anchor.y = 0.5;
    tile.position.x = x + TILE_SIZE / 2;
    tile.position.y = y + TILE_SIZE / 2;
    this.gameStage.addChild(tile);
};

View.prototype.drawInventoryTile = function (x, y, textureName) {
    var tile = new PIXI.Sprite(this.textures.data[textureName]);
    tile.anchor.x = 0;
    tile.anchor.y = 0;
    tile.position.x = x;
    tile.position.y = y;
    this.inventoryStage.addChild(tile);
};

View.prototype.updateView = function () {
    this.clearStage(this.gameStage);
    var curHeight = -(this.y % 1) * TILE_SIZE;
    var curWidth;
    var i;
    var j;
    for (i in this.map) {
        curWidth = -(this.x % 1) * TILE_SIZE;
        for (j in this.map[i]) {
            this.drawGameTile(curWidth, curHeight, this.dictionary[this.map[i][j]]);
            curWidth += TILE_SIZE;
        }
        curHeight += TILE_SIZE;
    }
    var t;
    for (t in this.items) {
        this.drawGameTile(
                (this.items[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.items[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            this.items[t].type
        );
    }

    for (t in this.projectiles) {
        this.drawGameTile(
                (this.projectiles[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.projectiles[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            'fireball'
        );
    }

    for (t in this.actors) {
        if (this.textures.data[this.actors[t].type] == undefined) {
            this.actors[t].type = 'icky thing';
        }
        this.drawGameTile(
                (this.actors[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.actors[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            this.actors[t].type //TODO Make a lot of textures
        );
    }
    this.gameRenderer.render(this.gameStage);
    //TODO need to rewrite this shit
    if (this.updateInventory) {
        this.clearStage(this.inventoryStage);
        var h = 0;
        var w = 0;
        var x = 0;
        var y = 0;
        for (x = 0; x <= INVENTORY_SIZE_X; x++) {
            for (y = 0; y <= INVENTORY_SIZE_Y; y++) {
                this.drawInventoryTile(x * TILE_SIZE, y * TILE_SIZE, 'inventory');
            }
        }
        for (i in this.inventory) {
//            console.log(JSON.stringify(this.inventory[i]));
            this.drawInventoryTile(w * TILE_SIZE, h * TILE_SIZE, this.inventory[i].type);
            w++;
            if (w >= INVENTORY_SIZE_X) {
                w = 0;
                h++;
            }
        }

        this.inventoryRenderer.render(this.inventoryStage);
        this.updateInventory = false;
    }
};

View.prototype.clearStage = function (stage) {
    for (var c = stage.children.length - 1; c >= 0; c--) {
        stage.removeChild(stage.children[c]);
    }
//    this.gameStage = new PIXI.Stage(0x000000, true);
};

View.prototype.setActors = function (actors) {
    this.actors = actors;
};

View.prototype.setItems = function (items) {
    this.items = items;
};

View.prototype.setProjectiles = function (projectiles) {
    this.projectiles = projectiles;
};

View.prototype.setDictionary = function (dictionary) {
    this.dictionary = dictionary;
};

View.prototype.setPlayerLocation = function (x, y) {
    this.x = x;
    this.y = y;
};

View.prototype.setMap = function (map) {
    this.map = map;
};

View.prototype.setInventory = function (inventory) {
    this.inventory = inventory;
};

View.prototype.setUpdateInventory = function () {
    this.updateInventory = true;
};