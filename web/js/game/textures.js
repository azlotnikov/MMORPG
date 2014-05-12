var TILE_SIZE = 32;

function Textures() {
    this.atlas = PIXI.BaseTexture.fromImage('img/tileset.png');
    this.data = {};
    this.loadTexture('grass', 22, 16);
    this.loadTexture('wall', 23, 16);
    this.loadTexture('inventory', 26, 14);
    this.loadTexture('player', 0, 2);
    this.loadTexture('icky thing', 5, 3);
    this.loadTexture('jelly', 3, 2);
    this.loadTexture('yeek', 6, 3);
    this.loadTexture('ghost', 0, 3);
    this.loadTexture('bat', 1, 5);
    this.loadTexture('minor demon', 0, 5);
    this.loadTexture('worm', 7, 2);
    this.loadTexture('mold', 10, 5);
    this.loadTexture('armor1', 0, 6);
    this.loadTexture('armor2', 1, 6);
    this.loadTexture('armor3', 2, 6);
    this.loadTexture('armor4', 3, 6);
    this.loadTexture('axe1', 3, 29);
    this.loadTexture('axe2', 1, 29);
    this.loadTexture('ring1', 6, 25);
    this.loadTexture('ring2', 17, 25);
    this.loadTexture('ring3', 18, 25);
    this.loadTexture('staff1', 0, 27);
    this.loadTexture('staff2', 1, 27);
    this.loadTexture('staff3', 3, 27);
    this.loadTexture('gloves1', 13, 21);

}

Textures.prototype.loadTexture = function (name, x, y) {
    this.data[name] = new PIXI.Texture(this.atlas, new PIXI.Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
};