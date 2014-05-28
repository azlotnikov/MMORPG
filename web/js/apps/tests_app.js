requirejs.config({
    baseUrl: '/js/tests',
    paths: {
        jquery: '../lib/jquery',
        auth: '../tests/auth',
        tester: '../tests/tester'
    }
});

requirejs(
    ['jquery', 'tester', 'auth'],
    function($, tester, auth) {
    $(function() {
        var StartTesting = function(){
            $('#mocha').empty();
            tester.setUrl($("#url").val());
            auth.Test();
            mocha.run();
        };

        $("#urlBtn").click(StartTesting);
        $("#url").val("ws://192.168.56.1:8080/MMORPG_war_exploded/game").keydown(function(e){
            if(e.which == 13){
                e.preventDefault();
                StartTesting();
            }
        })
    });
});