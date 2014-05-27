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
    <div>Уровень: <span id="player_level" class="value"></span></div>
    <div>
        <progress id="exp_progress" class="exp" value="10" max="100"></progress>
    </div>
    <div>Здоровье: <span id="player_hp" class="value"></span> / <span id="player_max_hp" class="value green"></span></div>
    <div>
        <progress id="hp_progress" class="hp" value="10" max="100"></progress>
    </div>
    <div>Мана: <span id="player_mana" class="value"></span> / <span id="player_max_mana" class="value green"></span></div>
    <div>
        <progress id="mana_progress" class="mana" value="10" max="100"></progress>
    </div>
    <div>X: <span id="player_x" class="value"></span></div>
    <div>Y: <span id="player_y" class="value"></span></div>
    <div>Аттака: <span id="player_damage" class="value"></span></div>
    <div>Скорость: <span id="player_speed" class="value"></span></div>
    <div>Задержка атаки: <span id="player_attack_delay" class="value"></span></div>
    <div>Восстановление здоровья: <span id="player_regen_hp" class="value"></span></div>
    <div>Восстановление маны: <span id="player_regen_mana" class="value"></span></div>
    <div>Сила: <span id="player_strength" class="value"></span></div>
    <div>Ловкость: <span id="player_agility" class="value"></span></div>
    <div>Интелект: <span id="player_intelligence" class="value"></span></div>
</div>
</body>
</html>