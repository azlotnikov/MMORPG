var wsUri = "ws://localhost:8080/MMORPG_war_exploded/chat";
var websocket = new WebSocket(wsUri);

var username;
websocket.onopen = function(evt) { onOpen(evt) };
websocket.onmessage = function(evt) { onMessage(evt) };
websocket.onerror = function(evt) { onError(evt) };
var output = document.getElementById("output");
var sid = getUrlVars()["sid"];


function join() {
    username = textField.value;
    websocket.send(username + " joined");
}

function send_message() {
    websocket.send(username + ": " + textField.value);
}

function onOpen() {
    writeToScreen("Connected to " + wsUri);
    writeToScreen(sid);
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function(m,key,value) {
            vars[key] = value;
        });
    return vars;
}


function onMessage(evt) {
    console.log("onMessage: " + evt.data);
    if (evt.data.indexOf("joined") != -1) {
        userField.innerHTML += evt.data.substring(0, evt.data.indexOf(" joined")) + "\n";
    } else {
        chatlogField.innerHTML += evt.data + "\n";
    }
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    output.innerHTML += message + "<br>";

}

