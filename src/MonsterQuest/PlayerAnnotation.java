package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */

import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/game")
public class PlayerAnnotation {
   private static final AtomicInteger playerIds = new AtomicInteger(0);
//   private static final Random random = new Random();

   private Player player;
   private Session openedSession;
   private boolean active = false;

   public PlayerAnnotation() {
//      this.id = playerIds.getAndIncrement();
   }

   public static JSONObject parseJsonString(String str) {
      JSONObject jsonResult = null;
      try {
         JSONParser jsonParser = new JSONParser();
         jsonResult = (JSONObject) jsonParser.parse(str);
      } catch (ParseException e) {
         // crash and burn
      }
      return jsonResult;
   }

   public static JSONObject getDictionary() {
      JSONObject jsonAns = new JSONObject();
      jsonAns.put(".", "grass");
      jsonAns.put("#", "wall");
      return jsonAns;
   }

   @OnOpen
   public void onOpen(Session session) {
      openedSession = session;
   }

   @OnMessage
   public String onMessage(String message) {
      JSONObject jsonMsg = parseJsonString(message);
      UserDB user = new UserDB();
      user.getDataBySid((String) jsonMsg.get("sid"));
      if (user.isBadSid()) {
         jsonMsg.put("result", "badSid");
         return jsonMsg.toJSONString();
      }
      if (!active) {
         player = new Player(user.getId(), user.getSid(), user.getLogin(), openedSession, user.getLocation());
         GameTimer.addPlayer(player);
         active = true;
      }

      switch ((String) jsonMsg.get("action")) {
         case "getDictionary": {
            jsonMsg.put("result", "ok");
            jsonMsg.put("dictionary", getDictionary());
         }

         case "examine": {
            Player examPlayer = GameTimer.ExaminePlayer((long) jsonMsg.get("id"));
            if (examPlayer != null) {
               jsonMsg.put("result", "ok");
               jsonMsg.put("id", examPlayer.getId());
               jsonMsg.put("type", "player");
               jsonMsg.put("login", examPlayer.getLogin());
               jsonMsg.put("x", examPlayer.getLocation().x);
               jsonMsg.put("y", examPlayer.getLocation().y);
            } else {
               jsonMsg.put("result", "badId");
            }
         }

         case "look": {

         }

         case "move": {
            Direction newDirection;
            switch ((String) jsonMsg.get("direction")) {
               case "west":
                  newDirection = Direction.WEST;
               case "north":
                  newDirection = Direction.NORTH;
               case "east":
                  newDirection = Direction.EAST;
               case "south":
                  newDirection = Direction.SOUTH;
               default:
                  newDirection = Direction.NONE;
            }

            long moveStartTickValue = (long) jsonMsg.get("tick");

            Location newStepLocation = player.getLocation();

//            for (int i = 1; i <= GameTimer.getCurrentTick() - moveStartTickValue; i++) {
            newStepLocation = newStepLocation.getAdjacentLocation(newDirection, player.getVelocity());
            //need to check collisions with walls
//            }

            player.moveTo(newStepLocation);
            jsonMsg.put("result", "ok");
         }

         default: {
            jsonMsg.put("result", "error");
         }
      }

      return jsonMsg.toJSONString();
   }

   @OnClose
   public void onClose() {
      GameTimer.removePlayer(player);
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
   }

}
