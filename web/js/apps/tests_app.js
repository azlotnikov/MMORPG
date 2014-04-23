requirejs.config({
    baseUrl: '/js/tests',
    paths: {
        jquery: '../jquery'
    }
});

requirejs(['jquery', 'tester', 'simple_walk', 'test_mode'], function($, tester, simple_walk, test_mode) {
    $(function() {
        var StartTesting = function(){
            $('#mocha').empty();
            tester.setUrl($("#url").val());

            auth.Test();
            simple_walk.Test();
            test_mode.Test();

            mocha.run();
        };
        $("#urlBtn").click(StartTesting);
        $("#url").val('/json').keydown(function(e){
            if(e.which == 13){
                e.preventDefault();
                StartTesting();
            }
        })
    });
});