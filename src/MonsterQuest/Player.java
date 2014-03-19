package MonsterQuest;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Collection;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player {
   private final long id;
   private final String sid;
   private final String login;
   private final Session session;
   private Location location;

   private double velocity = 0.021000;

   public Player(long id, String sid, String login, Session session, Location location) {
      this.id = id;
      this.sid = sid;
      this.login = login;
      this.session = session;
      this.location = location;
   }

   public void saveStateToBD() {
      UserDB user = new UserDB();
      user.setLocation(location);
      user.setSid(sid);
      user.saveGameData();
   }

   protected void sendMessage(JSONObject msg) {
      try {
         session.getBasicRemote().sendText(msg.toJSONString());
      } catch (IOException ioe) {
         CloseReason cr =
                 new CloseReason(CloseCodes.CLOSED_ABNORMALLY, ioe.getMessage());
         try {
            session.close(cr);
         } catch (IOException ioe2) {
            // Ignore
         }
      }
   }

   public JSONObject examine() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("id", id);
      result.put("type", "player");
      result.put("login", login);
      result.put("x", location.x);
      result.put("y", location.y);
      result.put("result", "ok");
      return result;
   }

   public synchronized void moveTo(Location location) {
      this.location = location;
   }

//   public synchronized void update(Collection<Player> players) {
//      saveStateToBD();
//   }

   public synchronized boolean logout() {
      UserDB user = new UserDB();
      user.setSid(sid);
      return user.doLogout();
   }

   public String getLogin() {
      return login;
   }

   public long getId() {
      return id;
   }

   public String getSid() {
       return sid;
   }

   public double getVelocity() {
      return velocity;
   }

   public Location getLocation() {
      return location;
   }

   public Session getSession() {
      return session;
   }

}
