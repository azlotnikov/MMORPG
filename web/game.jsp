<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 3/3/14
  Time: 2:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Monster Quest</title>
    <link rel="stylesheet" type="text/css" href="styles/style.css"/>
    <link rel="stylesheet" type="text/css" href="styles/game.css"/>
    <script src="js/lib/pixi.dev.js"></script>
    <script src="js/game/utils.js"></script>
    <script src="js/game/textures.js"></script>
    <script src="js/game/view.js"></script>
</head>
<body>
<script src="js/game/game.js"></script>
<div id="examine">

</div>
<div id="stats">
    <div>X: <span id="player_x" class="value"></span></div>
    <div>Y: <span id="player_y" class="value"></span></div>
    <div>Здоровье: <span id="player_hp" class="value"></span> + <span id="player_hp_bonus" class="value green"></span></div>
    <div>Аттака: <span id="player_damage" class="value"></span> + <span id="player_damage_bonus" class="value green"></span></div>
    <div>Скорость: <span id="player_speed" class="value"></span> + <span id="player_speed_bonus" class="value green"></span></div>
</div>
</body>
</html>