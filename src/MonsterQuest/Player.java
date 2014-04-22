package MonsterQuest;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player extends Monster{
   private final String sid;
   private final Session session;

   public Player(long id, String sid, String login, Session session, Location location) {
      super(
              id
            , login
            , "player"
            , 100 //TODO add HP in database
            , 0
            , 0.021000 //TODO add speed in database
            , null
            , null
            , location
      );
      this.sid = sid;
      this.session = session;
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

   public synchronized void move() {
      Game.unsetMonsterInLocation(location);
      Location newLocation = location.getNewLocation(direction, speed);
      location = newLocation.isActiveObjectInFront(direction, 0) ? location : newLocation;
      Game.setMonsterInLocation(this);
   }

//   public synchronized void update(Collection<Player> players) {
//      saveStateToBD();
//   }

   public synchronized boolean logout() {
      UserDB user = new UserDB();
      user.setSid(sid);
      return user.doLogout();
   }

   public String getSid() {
       return sid;
   }

   public Session getSession() {
      return session;
   }

   public void setDirection(Direction direction){
      this.direction = direction;
   }

}
