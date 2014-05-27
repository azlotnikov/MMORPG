var TILE_SIZE = 32;

function Textures() {
    this.atlas = PIXI.BaseTexture.fromImage('img/tileset.png');
    this.data = {};
    this.loadTexture(22, 16, 'grass')
        .loadTexture(23, 16, 'wall')
        .loadTexture(26, 14, 'inventory')
        .loadTexture(35, 14, 'weapon')
        .loadTexture(10, 0, 'active')
        .loadTexture(0, 2, 'player')
        .loadTexture(5, 3, 'icky thing')
        .loadTexture(3, 2, 'jelly')
        .loadTexture(6, 3, 'yeek')
        .loadTexture(0, 3, 'ghost')
        .loadTexture(1, 5, 'bat')
        .loadTexture(0, 5, 'minor demon')
        .loadTexture(7, 2, 'worm')
        .loadTexture(10, 5, 'mold')
        .loadTexture(0, 6, 'armor1')
        .loadTexture(1, 6, 'armor2')
        .loadTexture(2, 6, 'armor3')
        .loadTexture(3, 6, 'armor4')
        .loadTexture(3, 29, 'axe1')
        .loadTexture(1, 29, 'axe2')
        .loadTexture(6, 25, 'ring1')
        .loadTexture(17, 25, 'ring2')
        .loadTexture(18, 25, 'ring3')
        .loadTexture(0, 27, 'staff1')
        .loadTexture(1, 27, 'staff2')
        .loadTexture(3, 27, 'staff3')
        .loadTexture(13, 21, 'gloves1')
        .loadTexture(9, 43, 'fireball')
        .loadTexture(2, 47, 'fists')
        .loadTexture(2, 47, 'bow')
        .loadTexture(2, 47, 'stones')
        .loadTexture(2, 47, 'flamethrower')
        .loadTexture(2, 47, 'claw');
}

Textures.prototype.loadTexture = function (x, y, name) {
    this.data[name] = new PIXI.Texture(this.atlas, new PIXI.Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
    return this;
};