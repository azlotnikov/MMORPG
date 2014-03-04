package UserData;

/**
 * Created by razoriii on 04.03.14.
 */

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player {
   private final long id;
   private final String sid;
   private final String login;
   private final Session session;
   private Location location;

   private double velocity = 0.1;

   public Player(long id, String sid, String login, Session session, Location location) {
      this.id = id;
      this.sid = sid;
      this.login = login;
      this.session = session;
      this.location = location;
   }

   private void saveStateToBD() {

   }

   protected void sendMessage(String msg) {
      try {
         session.getBasicRemote().sendText(msg);
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

   public synchronized void moveTo(Location newLocation) {
      location = newLocation;
   }

   public synchronized void update(Collection<Player> players) {
      saveStateToBD();
   }

   public String getLogin() {
      return login;
   }

   public long getId() {
      return id;
   }

   public double getVelocity() {
      return velocity;
   }

   public Location getLocation() {
      return location;
   }

}
