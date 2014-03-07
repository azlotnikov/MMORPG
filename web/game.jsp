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
    <!-- Load the Paper.js library -->
    <script type="text/javascript" src="paperjs/dist/paper.js"></script>
    <!-- Load external PaperScript and associate it with myCanvas -->
    <script type="text/paperscript" src="js/game.js" canvas="myCanvas"></script>
</head>
<body>
<canvas id="myCanvas" resize></canvas>
<img id="wall" style="display: none;" src="img/wall.png">
<img id="grass" style="display: none;" src="img/grass.png">
<img id="player" style="display: none;" src="img/player.png">
</body>
</html>