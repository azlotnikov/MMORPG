package MonsterQuest;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

public class Player extends Monster {

   private final PlayerClass playerClass;

   private final String sid;
   private final Session session;

   public Player(long id, String sid, String login, int exp, double hp, Session session, Location location, String playerClass) {
      super(
              id
              , login
              , "player"
              , 1000 //Default player max hp
              , 600 //Default max mana
              , 0.2 //Default hp regen
              , 0.1 //Default mana regen
              , 20 //Default attack delay in ticks
              , 400 //Default exp For Kill
              , 0
              , 0.07 //Default speed
              , null
              , null
              , location
              , false
      );
      this.sid = sid;
      this.session = session;
      this.level.setExp(exp);
      this.stat.damage = 100; // Default player damage
      this.playerClass = new PlayerClass(PlayerClassType.strToPlayerClassType(playerClass), this.level.calcLevel());
      this.hp = hp;
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

   @Override
   public synchronized void move() {
      if (getInventoryId() != -1) {
         pickUpInventory();
         setInventoryId(-1);
      }
      if (!playerAttack.attack(aim, this)) {
         Game.unsetMonsterInLocation(location);
         location.move(direction, getSpeed());
         this.setDirection(Direction.NONE);
         Game.setMonsterInLocation(this);

      } else {
         aim = null;
      }

   }

//   public synchronized void update(Collection<Player> players) {
//      saveStateToBD();
//   }

   public synchronized boolean logout() {
      return UserDB.doLogout(sid);
   }

   public String getSid() {
      return sid;
   }

   public Session getSession() {
      return session;
   }

   public void setDirection(Direction direction) {
      this.direction = direction;
   }

   public void setAim(double x, double y) {
      this.aim = Game.getActors((int) x, (int) y);
      this.aimX = x;
      this.aimY = y;
   }

   @Override
   public JSONObject examine() {
      JSONObject result = super.examine();
      result.put("strength", getStrength());
      result.put("agility", getAgility());
      result.put("intelligence", getIntelligence());
      result.put("playerClass", PlayerClassType.toString(playerClass.getClassType()));
      return result;
   }

   @Override
   public int getExpKill() {
      return expKill * level.calcLevel();
   }

   @Override
   public void addExp(int expKill) {
      level.addExp(expKill);
      playerClass.calcClassForLevel(level.calcLevel());
   }

   @Override
   public double getSpeed() {
      return stat.speed + bonus.getStat().speed + playerClass.getStat().speed;
   }

   @Override
   public double getMaxHp() {
      return stat.maxHp + bonus.getStat().maxHp + playerClass.getStat().maxHp;
   }

   @Override
   public double getMaxMana() {
      return stat.maxMana + bonus.getStat().maxMana + playerClass.getStat().maxMana;
   }

   @Override
   public double getRegenHp() {
      return stat.regenHp + bonus.getStat().regenHp + playerClass.getStat().regenHp;
   }

   @Override
   public double getRegenMana() {
      return stat.regenMana + bonus.getStat().regenMana + playerClass.getStat().regenMana;
   }

   @Override
   public double getDamage() {
      return stat.damage + bonus.getStat().damage + playerClass.getStat().damage;
   }

   @Override
   public double getAttackDelay() {
      return stat.attackDelay - (bonus.getStat().attackDelay + playerClass.getStat().attackDelay);
   }

   @Override
   public int getStrength() {
      return stat.strength + bonus.getStat().strength + playerClass.getStat().strength;
   }

   @Override
   public int getAgility() {
      return stat.agility + bonus.getStat().agility + playerClass.getStat().agility;
   }

   @Override
   public int getIntelligence() {
      return stat.intelligence + bonus.getStat().intelligence + playerClass.getStat().intelligence;
   }

}
