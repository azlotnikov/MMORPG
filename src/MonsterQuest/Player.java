package MonsterQuest;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player extends Monster{
   private int damage = 50;
   private final Level level = new Level();
   private final String sid;
   private final Session session;

   public Player(long id, String sid, String login, int hp, int maxHp, int exp, Session session, Location location) {
      super(
              id
              , login
              , "player"
              , hp
              , maxHp
              , 0
              , 0.07 //TODO add speed in database
              , null
              , null
              , location
              , false
      );
      this.sid = sid;
      this.session = session;
      this.level.setExp(exp);
   }

   public void saveStateToBD() {
      UserDB.saveGameData(this);
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
      if (getInventoryId() != -1){
         pickUpInventory();
         setInventoryId(-1);
      }
      if (canAttack(aim)) {
         attack(aim);
         aim = null;
      } else {
         Game.unsetMonsterInLocation(location);
         location.move(direction, getSpeed());
         this.setDirection(Direction.NONE);
         Game.setMonsterInLocation(this);
      }
   }

//   public synchronized void update(Collection<Player> players) {
//      saveStateToBD();
//   }

   public synchronized boolean logout() {
      return UserDB.doLogout(sid);
   }

   public int getDamage(){
      return damage;
   }

   public String getSid() {
       return sid;
   }

   public Level getLevel() {
      return level;
   }

   public Session getSession() {
      return session;
   }

   public void setDirection(Direction direction){
      this.direction = direction;
   }

   public void setAim(double x, double y){
      this.aim = Game.getActors((int)x, (int)y);
   }
}
