requirejs.config({
    baseUrl: '/js/game',
    paths: {
        jquery: '../lib/jquery'
    }
});

requirejs(['jquery', 'game'], function($, game) {
    $(function() {
        game.Start();
    })
});