package MonsterQuest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alexander on 4/16/14.
 */

public class Blow {

   private final AttackMethod attackMethod;
   private final AttackEffect attackEffect;
   private final int maxDamage;
   private final int countDamage;

   public Blow(String attackMethod, String attackEffect, int countDamage, int maxDamage) {
      this.attackMethod = AttackMethod.strToAttackMethod(attackMethod);
      this.attackEffect = AttackEffect.strToAttackEffect(attackEffect);
      this.maxDamage = maxDamage;
      this.countDamage = countDamage;
   }

   public static enum AttackMethod {
      NONE,
      SPORE,
      HIT,
      GAZE,
      SPIT,
      TOUCH,
      ENGULF,
      BEG,
      INSULT,
      CRAWL,
      CRUSH,
      CLAW,
      BITE,
      KICK,
      DROOL,
      BUTT,
      STING,
      MOAN,
      WAIL;

      private static final ConcurrentHashMap<String, AttackMethod> AttackMethods = new ConcurrentHashMap(){{
         put("", NONE);
         put("BEG", BEG);
         put("BITE", BITE);
         put("BUTT", BUTT);
         put("CLAW", CLAW);
         put("CRAWL", CRAWL);
         put("CRUSH", CRUSH);
         put("DROOL", DROOL);
         put("ENGULF", ENGULF);
         put("GAZE", GAZE);
         put("HIT", HIT);
         put("INSULT", INSULT);
         put("KICK", KICK);
         put("MOAN", MOAN);
         put("SPIT", SPIT);
         put("SPORE", SPORE);
         put("STING", STING);
         put("TOUCH", TOUCH);
         put("WAIL", WAIL);
      }};

      public static AttackMethod strToAttackMethod(String s){
         return AttackMethods.get(s);
      }
   }

   public static enum AttackEffect {
      NONE,
      LOSE_INT,
      LOSE_STR,
      EXP_20,
      COLD,
      ACID,
      LOSE_WIS,
      BLIND,
      LOSE_DEX,
      EAT_LIGHT,
      EAT_FOOD,
      LOSE_ALL,
      TERRIFY,
      EXP_80,
      EAT_ITEM,
      POISON,
      UN_POWER,
      CONFUSE,
      HURT,
      EXP_40,
      FIRE,
      EXP_10,
      SHATTER,
      UN_BONUS,
      LOSE_CON,
      ELEC,
      EAT_GOLD,
      PARALYZE,
      HALLU;

      private static final ConcurrentHashMap<String, AttackEffect> AttackEffects = new ConcurrentHashMap(){{
         put("", NONE);
         put("ACID", ACID);
         put("BLIND", BLIND);
         put("COLD", COLD);
         put("CONFUSE", CONFUSE);
         put("EAT_FOOD", EAT_FOOD);
         put("EAT_GOLD", EAT_GOLD);
         put("EAT_ITEM", EAT_ITEM);
         put("EAT_LIGHT", EAT_LIGHT);
         put("ELEC", ELEC);
         put("EXP_10", EXP_10);
         put("EXP_20", EXP_20);
         put("EXP_40", EXP_40);
         put("EXP_80", EXP_80);
         put("FIRE", FIRE);
         put("HALLU", HALLU);
         put("HURT", HURT);
         put("LOSE_ALL", LOSE_ALL);
         put("LOSE_CON", LOSE_CON);
         put("LOSE_DEX", LOSE_DEX);
         put("LOSE_INT", LOSE_INT);
         put("LOSE_STR", LOSE_STR);
         put("LOSE_WIS", LOSE_WIS);
         put("PARALYZE", PARALYZE);
         put("POISON", POISON);
         put("SHATTER", SHATTER);
         put("TERRIFY", TERRIFY);
         put("UN_BONUS", UN_BONUS);
         put("UN_POWER", UN_POWER);
      }};

      public static AttackEffect strToAttackEffect(String s){
         return AttackEffects.get(s);
      }
   }

   public int getDamage(){
      return Dice.getInt(maxDamage, countDamage);
   }

   public AttackMethod getAttackMethod(){
      return attackMethod;
   }

   public AttackEffect getAttackEffect(){
      return attackEffect;
   }
}

