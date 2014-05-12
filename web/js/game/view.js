var SIGHT_RADIUS_X = 12;
var SIGHT_RADIUS_Y = 8;
var INVENTORY_SIZE_X = 8;
var INVENTORY_SIZE_Y = 16;

function View() {
    this.stage = new PIXI.Stage(0x000000, true);
    this.inventoryStage = new PIXI.Stage(0x000000, true);
    this.renderer = PIXI.autoDetectRenderer(SIGHT_RADIUS_X * 2 * TILE_SIZE, SIGHT_RADIUS_Y * 2 * TILE_SIZE);
    this.inventoryRenderer = PIXI.autoDetectRenderer(TILE_SIZE * INVENTORY_SIZE_X, TILE_SIZE * INVENTORY_SIZE_Y);
    this.textures  = new Textures();
    // todo чтобы влезал в экран
    this.map = {};
    this.actors = {};
    this.dictionary = {};
    this.items = {};
    this.inventory = {};
    this.updateInventory = true;

    document.body.appendChild(this.renderer.view);
    this.renderer.view.style.position = "absolute";
    this.renderer.view.style.top = "0px";
    this.renderer.view.style.left = "0px";

    document.body.appendChild(this.inventoryRenderer.view);
    this.inventoryRenderer.view.style.position = "absolute";
    this.inventoryRenderer.view.style.top = "0";
    this.inventoryRenderer.view.style.left = this.renderer.width + "px";
}

View.prototype.drawTile = function (x, y, textureName) {
    var tile = new PIXI.Sprite(this.textures.data[textureName]);
    tile.anchor.x = 0.5;
    tile.anchor.y = 0.5;
    tile.position.x = x + TILE_SIZE / 2;
    tile.position.y = y + TILE_SIZE / 2;
    this.stage.addChild(tile);
};

View.prototype.drawInventoryItem = function (x, y, textureName) {
    var tile = new PIXI.Sprite(this.textures.data[textureName]);
    tile.anchor.x = 0;
    tile.anchor.y = 0;
    tile.position.x = x;
    tile.position.y = y;
    this.inventoryStage.addChild(tile);
};

View.prototype.updateView = function () {
    this.clearView();
    var curHeight = -(this.y % 1) * TILE_SIZE;
    var curWidth;
    var i;
    var j;
    for (i in this.map) {
        curWidth = -(this.x % 1) * TILE_SIZE;
        for (j in this.map[i]) {
            this.drawTile(curWidth, curHeight, this.dictionary[this.map[i][j]]);
            curWidth += TILE_SIZE;
        }
        curHeight += TILE_SIZE;
    }
    var t;
    for (t in this.items) {
        this.drawTile(
                (this.items[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.items[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            this.items[t].type
        );
    }
    for (t in this.actors) {
        if (this.textures.data[this.actors[t].type] == undefined ) {
            this.actors[t].type = 'icky thing';
        }
        this.drawTile(
                (this.actors[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.actors[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            this.actors[t].type //TODO Make a lot of textures
        );
    }
    this.renderer.render(this.stage);
    //TODO need to rewrite this shit
    if (this.updateInventory) {
        for (var c = this.inventoryStage.children.length - 1; c >= 0; c--) {
            this.inventoryStage.removeChild(this.inventoryStage.children[c]);
        }
        this.inventoryStage = new PIXI.Stage(0x000000, true);
        var h = 0;
        var w = 0;
        var x = 0;
        var y = 0;
        for (x = 0; x <= INVENTORY_SIZE_X; x++) {
            for (y = 0; y <= INVENTORY_SIZE_Y; y++) {
                this.drawInventoryItem(x * TILE_SIZE, y * TILE_SIZE, 'inventory');
            }
        }
        for (i in this.inventory) {
//            console.log(JSON.stringify(this.inventory[i]));
            this.drawInventoryItem(w * TILE_SIZE, h * TILE_SIZE, this.inventory[i].type);
            w++;
            if (w > INVENTORY_SIZE_X) {
                w = 0;
                h++;
            }
        }

        this.inventoryRenderer.render(this.inventoryStage);
        this.updateInventory = false;
    }
};

View.prototype.clearView = function () {
    for (var c = this.stage.children.length - 1; c >= 0; c--) {
        this.stage.removeChild(this.stage.children[c]);
    }
//    this.stage = new PIXI.Stage(0x000000, true);
};

View.prototype.setActors = function (actors) {
    this.actors = actors;
};

View.prototype.setItems = function (items) {
    this.items = items;
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

View.prototype.setUpdateInventory = function() {
    this.updateInventory = true;
};