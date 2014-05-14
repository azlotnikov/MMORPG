package MonsterQuest;

import java.io.EOFException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/game") // TODO Вынести весь JSON в отдельный файл
// TODO  думал об этом, но туда надо будет передавать player'а и мб что то еще
public class PlayerAnnotation {

   private Player player;
   private Session openedSession;
   private boolean isTesting = false;

   public static JSONObject parseJsonString(String str) {
      JSONObject jsonResult = null;
      try {
         JSONParser jsonParser = new JSONParser();
         jsonResult = (JSONObject) jsonParser.parse(str);
      } catch (ParseException e) {
      }
      return jsonResult;
   }


   public JSONObject getDictionary() {
      JSONObject result = new JSONObject();
      result.put("action", "getDictionary");
      result.put("dictionary", GameDictionary.getJsonDictionary());
      result.put("result", "ok");
      return result;
   }

   public JSONObject getBadId() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("result", "badId");
      return result;
   }

   public JSONObject getBadSid(String action) {
      JSONObject result = new JSONObject();
      result.put("action", action);
      result.put("result", "badSid");
      return result;
   }

   public JSONObject getLogout() {
      JSONObject result = new JSONObject();
      result.put("action", "logout");
      result.put("result", "ok");
      return result;
   }

   public JSONObject getStartTesting() {
      JSONObject result = new JSONObject();
      result.put("action", "startTesting");
      result.put("result", "ok");
      return result;
   }

   public JSONObject getStopTesting() {
      JSONObject result = new JSONObject();
      result.put("action", "stopTesting");
      result.put("result", "ok");
      return result;
   }

   public JSONObject getGetConst() {
      JSONObject result = new JSONObject();
      result.put("action", "getConst");
      result.put("playerVelocity", player.getSpeed());
      result.put("slideThreshold", "0.1"); //TODO fix this
      result.put("ticksPerSecond", Game.getTicksPerSecond());
      result.put("screenRowCount", GameMap.getSightRadiusY());
      result.put("screenColumnCount", GameMap.getSightRadiusX());
      result.put("result", "ok");
      return result;
   }

   public JSONObject getSetUpConst() {
      JSONObject result = new JSONObject();
      result.put("action", "setUpConst");
      result.put("result", "ok");
      return result;
   }

   public JSONObject getSetUpMap() {
      JSONObject result = new JSONObject();
      result.put("action", "setUpMap");
      result.put("result", "ok");
      return result;
   }

   public JSONObject getError() {
      JSONObject result = new JSONObject();
      result.put("result", "error");
      return result;
   }

   public JSONObject getLook() {
      JSONObject result = new JSONObject();
      result.put("action", "look");
      result.put("result", "ok");
      result.put("map", getMap((int) player.getLocation().x, (int) player.getLocation().y));
      result.put("actors", Game.getActors(player.getLocation()));
      result.put("items", Game.getDroppedItems().inventoryToJSON());
      result.put("x", player.getLocation().x);
      result.put("y", player.getLocation().y);
      JSONObject playerData = new JSONObject();
      playerData.put("hp", player.getHp());
      playerData.put("maxHp", player.getMaxHp());
      playerData.put("expLevel", player.getLevel().calcExpLevel());
      playerData.put("expNextLevel", player.getLevel().calcExpNextLevel());
      playerData.put("level", player.getLevel().calcLevel());
      playerData.put("hpBonus", player.getBonusHp());
      playerData.put("damage", player.getDamage());
      playerData.put("damageBonus", player.getBonusDamage());
      playerData.put("speed", player.getSpeed());
      playerData.put("speedBonus", player.getBonusSpeed());
      playerData.put("inventory", player.getInventory().inventoryToJSON());
      result.put("player", playerData);
      return result;
   }

   public JSONArray getMap(int x, int y) {
      return GameMap.mapToJson(x, y);
   }

   @OnOpen
   public void onOpen(Session session) {
      openedSession = session;
   }

   @OnMessage
   public void onMessage(String message) {
      JSONObject jsonMsg = parseJsonString(message);
      JSONObject jsonAns = new JSONObject();

      String sid = (String) jsonMsg.get("sid");
      String action = (String) jsonMsg.get("action");

      player = Game.findPlayerBySid(sid); //TODO перенести логику в Game
      if (player == null) {
         player = UserDB.getPlayerBySid(sid, openedSession);
         if (player == null) {
            jsonAns = getBadSid(action);
            try {
               openedSession.getBasicRemote().sendText(jsonAns.toJSONString());
            } catch (Throwable e) {
            }
            return;
         }
         Game.addPlayer(player);
         player.generateRandomInventory(); //TODO this is for test
         player.generateRandomInventory(); //TODO this is for test
      }

      switch (action) {
         case "startTesting": {
            isTesting = true;
            jsonAns = getStartTesting();
            break;
         }

         case "stopTesting": {
            isTesting = false;
            jsonAns = getStopTesting();
            break;
         }

         case "setUpConst": {
            player.setSpeed((double) jsonMsg.get("playerVelocity"));
            GameMap.setSightRadiusX((int) jsonMsg.get("screenColumnCount"));
            GameMap.setSightRadiusY((int) jsonMsg.get("screenRowCount"));
            jsonAns = getSetUpConst();
            break;
         }

         case "setUpMap": {
            GameMap.setUpMapFromJson((JSONArray) jsonMsg.get("map"));
            jsonAns = getSetUpMap();
            break;
         }

         case "getConst": {
            jsonAns = getGetConst();
            break;
         }

         case "getDictionary": {
            jsonAns = getDictionary();
            break;
         }

         case "examine": {
            Player examPlayer = Game.examinePlayer((long) jsonMsg.get("id"));
            Monster examMonster = Game.examineMonster((long) jsonMsg.get("id"));
            Item examItem = Game.examineItem((long) jsonMsg.get("id"));
            if (examItem == null) {
               examItem = player.getInventory().getItem((long) jsonMsg.get("id"));
            }
            if (examPlayer != null) {
               jsonAns = examPlayer.examine();
            } else if (examMonster != null) {
               jsonAns = examMonster.examine();
            } else if (examItem != null) {
               jsonAns = examItem.examine();
            } else {
               jsonAns = getBadId();
            }
            break;
         }

         case "pickUp": {
            player.setInventoryId((long) jsonMsg.get("id"));
            return;
         }

         case "drop": {
            player.dropItem((long) jsonMsg.get("id"));
            return;
         }

         case "attack": {
            player.setAim((Double) jsonMsg.get("x"), (Double) jsonMsg.get("y"));
            return;
         }

         case "look": {
            jsonAns = getLook();
            break;
         }

         case "move": {
            player.setDirection(Direction.strToDirection((String) jsonMsg.get("direction")));
            return;
         }

         case "logout": {
            if (!player.logout()) {
               jsonAns = getBadSid(action);
            } else
               try {
                  jsonAns = getLogout();
                  player.saveStateToBD();
                  player.getSession().close();
                  Game.unsetMonsterInLocation(player.getLocation());
                  Game.removePlayer(player);
               } catch (Throwable e) {
               }
            break;
         }

         default: {
            jsonAns = getError();
            break;
         }
      }
      player.sendMessage(jsonAns);
   }

   @OnClose
   public void onClose() {
      Game.removePlayer(player);
   }


   @OnError
   public void onError(Throwable t) throws Throwable {
      // Most likely cause is a user closing their browser. Check to see if
      // the root cause is EOF and if it is ignore it.
      // Protect against infinite loops.
      int count = 0;
      Throwable root = t;
      while (root.getCause() != null && count < 20) {
         root = root.getCause();
         count++;
      }
      if (root instanceof EOFException) {
         // Assume this is triggered by the user closing their browser and
         // ignore it.
      } else {
         throw t;
      }
   }

}
