requirejs.config({
    baseUrl: '/js/tests',
    paths: {
        jquery: '../lib/jquery',
        auth: '../tests/auth',
        tester: '../tests/tester',
        simpleWalk: '../tests/simpleWalk'
    }
});

requirejs(
    ['jquery', 'tester', 'auth', 'simpleWalk'],
    function($, tester, auth, simpleWalk) {
    $(function() {
        var StartTesting = function(){
            $('#mocha').empty();
            tester.setUrl($("#url").val());

            auth.Test();
            simpleWalk.Test();

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