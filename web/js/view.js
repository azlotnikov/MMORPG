var SIGHT_RADIUS_X = 12;
var SIGHT_RADIUS_Y = 8;
var TILE_SIZE = 32;

function View() {
    this.stage = new PIXI.Stage(0x000000, true);
    this.renderer = PIXI.autoDetectRenderer(SIGHT_RADIUS_X * 2 * TILE_SIZE, SIGHT_RADIUS_Y * 2 * TILE_SIZE);
    // todo чтобы влезал в экран
    this.atlas = PIXI.BaseTexture.fromImage('img/tileset.png');
    this.textures = {
        'grass': new PIXI.Texture(this.atlas, new PIXI.Rectangle(22 * TILE_SIZE, 16 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'wall': new PIXI.Texture(this.atlas, new PIXI.Rectangle(23 * TILE_SIZE, 16 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'player': new PIXI.Texture(this.atlas, new PIXI.Rectangle(0 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'icky thing': new PIXI.Texture(this.atlas, new PIXI.Rectangle(5 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'jelly': new PIXI.Texture(this.atlas, new PIXI.Rectangle(3 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'yeek': new PIXI.Texture(this.atlas, new PIXI.Rectangle(6 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'ghost': new PIXI.Texture(this.atlas, new PIXI.Rectangle(0 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'bat': new PIXI.Texture(this.atlas, new PIXI.Rectangle(1 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'minor demon': new PIXI.Texture(this.atlas, new PIXI.Rectangle(0 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'worm': new PIXI.Texture(this.atlas, new PIXI.Rectangle(7 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'mold': new PIXI.Texture(this.atlas, new PIXI.Rectangle(10 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'armor1': new PIXI.Texture(this.atlas, new PIXI.Rectangle(0 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'armor2': new PIXI.Texture(this.atlas, new PIXI.Rectangle(1 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'armor3': new PIXI.Texture(this.atlas, new PIXI.Rectangle(2 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'armor4': new PIXI.Texture(this.atlas, new PIXI.Rectangle(3 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'axe1': new PIXI.Texture(this.atlas, new PIXI.Rectangle(3 * TILE_SIZE, 29 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'axe2': new PIXI.Texture(this.atlas, new PIXI.Rectangle(1 * TILE_SIZE, 29 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'ring1': new PIXI.Texture(this.atlas, new PIXI.Rectangle(6 * TILE_SIZE, 25 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'ring2': new PIXI.Texture(this.atlas, new PIXI.Rectangle(17 * TILE_SIZE, 25 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'ring3': new PIXI.Texture(this.atlas, new PIXI.Rectangle(18 * TILE_SIZE, 25 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'staff1': new PIXI.Texture(this.atlas, new PIXI.Rectangle(0 * TILE_SIZE, 27 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'staff2': new PIXI.Texture(this.atlas, new PIXI.Rectangle(1 * TILE_SIZE, 27 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'staff3': new PIXI.Texture(this.atlas, new PIXI.Rectangle(3 * TILE_SIZE, 27 * TILE_SIZE, TILE_SIZE, TILE_SIZE)),
        'gloves1': new PIXI.Texture(this.atlas, new PIXI.Rectangle(13 * TILE_SIZE, 21 * TILE_SIZE, TILE_SIZE, TILE_SIZE))
    };

    this.map = {};
    this.actors = {};
    this.dictionary = {};
    this.items = {};

    document.body.appendChild(this.renderer.view);
    this.renderer.view.style.position = "absolute";
    this.renderer.view.style.top = "0px";
    this.renderer.view.style.left = "0px";
}

View.prototype.drawTile = function (x, y, textureName) {
    var tile = new PIXI.Sprite(this.textures[textureName]);
    tile.anchor.x = 0.5;
    tile.anchor.y = 0.5;
    tile.position.x = x + TILE_SIZE / 2;
    tile.position.y = y + TILE_SIZE / 2;
    this.stage.addChild(tile);
};

View.prototype.updateView = function (playerId) {
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
        if (this.textures[this.actors[t].type] == undefined ) {
            this.actors[t].type = 'icky thing';
        }
        this.drawTile(
                (this.actors[t].x - this.x + SIGHT_RADIUS_X) * TILE_SIZE - TILE_SIZE / 2,
                (this.actors[t].y - this.y + SIGHT_RADIUS_Y) * TILE_SIZE - TILE_SIZE / 2,
            this.actors[t].type //TODO Make a lot of textures
        );
    }
    this.renderer.render(this.stage);
};

View.prototype.clearView = function () {
    for (var c = this.stage.children.length - 1; c >= 0; c--) {
        this.stage.removeChild(this.stage.children[c]);
    }
    this.stage = new PIXI.Stage(0x000000, true);
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
