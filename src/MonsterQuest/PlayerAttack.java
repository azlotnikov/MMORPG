package MonsterQuest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by razoriii on 28.05.14.
 */
public class PlayerAttack {
   private int attackId;

   public PlayerAttack() {

   }

   public boolean attack(Monster aim, Monster attacker) {
      switch (attackId) {
         case 1: {
            if (attacker.canAttack(aim)) {
               aim.gotHit(attacker.getDamage(), attacker);
               attacker.currentAttackDelay = attacker.getAttackDelay();
               return true;
            }
            return false;
         }
         case 2: {
            if ((attacker.aimX != 0 || attacker.aimY != 0) && (attacker.mana >= 2 * attacker.getLevel().calcLevel())) { //TODO - 50 - кол-во маны за фаерболл
               attacker.mana -= 2 * attacker.getLevel().calcLevel();
               double a = attacker.aimX - attacker.location.x;
               double b = attacker.aimY - attacker.location.y;
               Game.addProjectiles(new Projectiles(AttackMethod.FIREBALL
                       , attacker.location
                       , 0.5
                       , attacker.aimX - attacker.location.x
                       , attacker.aimY - attacker.location.y
                       , 0.2
                       , 2.0
                       , attacker.getDamage()
                       , attacker
               ));
               attacker.aimX = 0;
               attacker.aimY = 0;
               return true;
            }
            return false;

         }
         case 3: {

         }
      }
      return false;
   }

   public static JSONArray getAllAttackTypes() {
      JSONArray result = new JSONArray();
      JSONObject attack1 = new JSONObject();
      JSONObject attack2 = new JSONObject();
      JSONObject attack3 = new JSONObject();
      attack1.put("id", "1");
      attack2.put("id", "2");
      attack3.put("id", "3");
      attack1.put("name", "sword");
      attack2.put("name", "fireball");
      attack3.put("name", "bow");
      result.add(attack1);
      result.add(attack2);
      result.add(attack3);
      return result;
   }

   public int getAttackId() {
      return attackId;
   }

   public void setAttackId(int attackId) {
      this.attackId = attackId;
   }
}
