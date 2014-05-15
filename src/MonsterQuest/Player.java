package MonsterQuest;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player extends Monster{
   private int damage = 50;
   private final String sid;
   private final Session session;
   private double aimX;
   private double aimY;

   public Player(long id, String sid, String login, int hp, int maxHp, int exp, Session session, Location location) {
      super(
              id
              , login
              , "player"
              , hp
              , maxHp
              , 400
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
      } else if(aimX != 0 || aimY != 0){
         double a = aimX - location.x;
         double b = aimY - location.y;
         Game.addProjectiles(new Projectiles(AttackMethod.FIREBALL
                                ,location
                                ,0.5
                                ,aimX - location.x
                                ,aimY - location.y
                                ,0.2
                                ,2.0
                                ,this
         ));
         aimX = 0;
         aimY = 0;
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
      this.aimX = x;
      this.aimY = y;
   }
}
