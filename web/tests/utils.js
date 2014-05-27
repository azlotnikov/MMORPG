/**
 * Created by Alexander on 5/28/14.
 */
define(function() {
    function getUrlVars() {
        var vars = {};
        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
            function (m, key, value) {
                vars[key] = value;
            });
        return vars;
    }

    function sendRequest(data, callback, url) {
        var url = url || "/json";
        $.ajax({
            method : "POST",
            url : url,
            data : JSON.stringify(data),
            success : callback
        });
    }

    function exitGame() {
        //alert('Game quit!');
        window.close();
    }

    function randString(){
        var text = '';
        var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for( var i=0; i < 10; i++ ) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
    }

    return {
        randString:       randString,
        sendRequest:      sendRequest,
        exitGame:         exitGame,
        getUrlVars:       getUrlVars
    }
})


