package MonsterQuest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alexander on 4/16/14.
 */
public enum Blow {
   //
   _10d10,
   _10d12,
   _10d14,
   _10d2,
   _10d5,
   _10d8,
   _11d10,
   _11d11,
   _12d12,
   _12d13,
   _12d6,
   _13d13,
   _15d1,
   _1d1,
   _1d10,
   _1d12,
   _1d2,
   _1d3,
   _1d4,
   _1d5,
   _1d6,
   _1d7,
   _1d8,
   _1d9,
   _20d1,
   _20d10,
   _20d2,
   _2d10,
   _2d12,
   _2d2,
   _2d3,
   _2d4,
   _2d5,
   _2d6,
   _2d8,
   _3d10,
   _3d12,
   _3d3,
   _3d4,
   _3d5,
   _3d6,
   _3d7,
   _3d8,
   _3d9,
   _4d10,
   _4d12,
   _4d14,
   _4d3,
   _4d4,
   _4d5,
   _4d6,
   _4d8,
   _4d9,
   _5d10,
   _5d12,
   _5d14,
   _5d2,
   _5d4,
   _5d5,
   _5d6,
   _5d8,
   _6d10,
   _6d12,
   _6d14,
   _6d5,
   _6d6,
   _6d7,
   _6d8,
   _7d10,
   _7d12,
   _7d14,
   _7d6,
   _7d7,
   _7d8,
   _7d9,
   _8d10,
   _8d12,
   _8d14,
   _8d4,
   _8d5,
   _8d6,
   _8d7,
   _8d8,
   _9d10,
   _9d12,
   _9d9,
   //
   ACID,
   BEG,
   BITE,
   BLIND,
   BUTT,
   CLAW,
   COLD,
   CONFUSE,
   CRAWL,
   CRUSH,
   DROOL,
   EAT_FOOD,
   EAT_GOLD,
   EAT_ITEM,
   EAT_LIGHT,
   ELEC,
   ENGULF,
   EXP_10,
   EXP_20,
   EXP_40,
   EXP_80,
   FIRE,
   GAZE,
   HALLU,
   HIT,
   HURT,
   INSULT,
   KICK,
   LOSE_ALL,
   LOSE_CON,
   LOSE_DEX,
   LOSE_INT,
   LOSE_STR,
   LOSE_WIS,
   MOAN,
   PARALYZE,
   POISON,
   SHATTER,
   SPIT,
   SPORE,
   STING,
   TERRIFY,
   TOUCH,
   UN_BONUS,
   UN_POWER,
   WAIL;

   private static final ConcurrentHashMap<String, Blow> blows = new ConcurrentHashMap(){{
      put("10d10", _10d10);
      put("10d12", _10d12);
      put("10d14", _10d14);
      put("10d2", _10d2);
      put("10d5", _10d5);
      put("10d8", _10d8);
      put("11d10", _11d10);
      put("11d11", _11d11);
      put("12d12", _12d12);
      put("12d13", _12d13);
      put("12d6", _12d6);
      put("13d13", _13d13);
      put("15d1", _15d1);
      put("1d1", _1d1);
      put("1d10", _1d10);
      put("1d12", _1d12);
      put("1d2", _1d2);
      put("1d3", _1d3);
      put("1d4", _1d4);
      put("1d5", _1d5);
      put("1d6", _1d6);
      put("1d7", _1d7);
      put("1d8", _1d8);
      put("1d9", _1d9);
      put("20d1", _20d1);
      put("20d10", _20d10);
      put("20d2", _20d2);
      put("2d10", _2d10);
      put("2d12", _2d12);
      put("2d2", _2d2);
      put("2d3", _2d3);
      put("2d4", _2d4);
      put("2d5", _2d5);
      put("2d6", _2d6);
      put("2d8", _2d8);
      put("3d10", _3d10);
      put("3d12", _3d12);
      put("3d3", _3d3);
      put("3d4", _3d4);
      put("3d5", _3d5);
      put("3d6", _3d6);
      put("3d7", _3d7);
      put("3d8", _3d8);
      put("3d9", _3d9);
      put("4d10", _4d10);
      put("4d12", _4d12);
      put("4d14", _4d14);
      put("4d3", _4d3);
      put("4d4", _4d4);
      put("4d5", _4d5);
      put("4d6", _4d6);
      put("4d8", _4d8);
      put("4d9", _4d9);
      put("5d10", _5d10);
      put("5d12", _5d12);
      put("5d14", _5d14);
      put("5d2", _5d2);
      put("5d4", _5d4);
      put("5d5", _5d5);
      put("5d6", _5d6);
      put("5d8", _5d8);
      put("6d10", _6d10);
      put("6d12", _6d12);
      put("6d14", _6d14);
      put("6d5", _6d5);
      put("6d6", _6d6);
      put("6d7", _6d7);
      put("6d8", _6d8);
      put("7d10", _7d10);
      put("7d12", _7d12);
      put("7d14", _7d14);
      put("7d6", _7d6);
      put("7d7", _7d7);
      put("7d8", _7d8);
      put("7d9", _7d9);
      put("8d10", _8d10);
      put("8d12", _8d12);
      put("8d14", _8d14);
      put("8d4", _8d4);
      put("8d5", _8d5);
      put("8d6", _8d6);
      put("8d7", _8d7);
      put("8d8", _8d8);
      put("9d10", _9d10);
      put("9d12", _9d12);
      put("9d9", _9d9);
      put("ACID", ACID);
      put("BEG", BEG);
      put("BITE", BITE);
      put("BLIND", BLIND);
      put("BUTT", BUTT);
      put("CLAW", CLAW);
      put("COLD", COLD);
      put("CONFUSE", CONFUSE);
      put("CRAWL", CRAWL);
      put("CRUSH", CRUSH);
      put("DROOL", DROOL);
      put("EAT_FOOD", EAT_FOOD);
      put("EAT_GOLD", EAT_GOLD);
      put("EAT_ITEM", EAT_ITEM);
      put("EAT_LIGHT", EAT_LIGHT);
      put("ELEC", ELEC);
      put("ENGULF", ENGULF);
      put("EXP_10", EXP_10);
      put("EXP_20", EXP_20);
      put("EXP_40", EXP_40);
      put("EXP_80", EXP_80);
      put("FIRE", FIRE);
      put("GAZE", GAZE);
      put("HALLU", HALLU);
      put("HIT", HIT);
      put("HURT", HURT);
      put("INSULT", INSULT);
      put("KICK", KICK);
      put("LOSE_ALL", LOSE_ALL);
      put("LOSE_CON", LOSE_CON);
      put("LOSE_DEX", LOSE_DEX);
      put("LOSE_INT", LOSE_INT);
      put("LOSE_STR", LOSE_STR);
      put("LOSE_WIS", LOSE_WIS);
      put("MOAN", MOAN);
      put("PARALYZE", PARALYZE);
      put("POISON", POISON);
      put("SHATTER", SHATTER);
      put("SPIT", SPIT);
      put("SPORE", SPORE);
      put("STING", STING);
      put("TERRIFY", TERRIFY);
      put("TOUCH", TOUCH);
      put("UN_BONUS", UN_BONUS);
      put("UN_POWER", UN_POWER);
      put("WAIL", WAIL);
   }};

   private static final ConcurrentHashMap<Blow, String> blowToStr = new ConcurrentHashMap(){{
      put(_10d10, "10d10");
      put(_10d12, "10d12");
      put(_10d14, "10d14");
      put(_10d2, "10d2");
      put(_10d5, "10d5");
      put(_10d8, "10d8");
      put(_11d10, "11d10");
      put(_11d11, "11d11");
      put(_12d12, "12d12");
      put(_12d13, "12d13");
      put(_12d6, "12d6");
      put(_13d13, "13d13");
      put(_15d1, "15d1");
      put(_1d1, "1d1");
      put(_1d10, "1d10");
      put(_1d12, "1d12");
      put(_1d2, "1d2");
      put(_1d3, "1d3");
      put(_1d4, "1d4");
      put(_1d5, "1d5");
      put(_1d6, "1d6");
      put(_1d7, "1d7");
      put(_1d8, "1d8");
      put(_1d9, "1d9");
      put(_20d1, "20d1");
      put(_20d10, "20d10");
      put(_20d2, "20d2");
      put(_2d10, "2d10");
      put(_2d12, "2d12");
      put(_2d2, "2d2");
      put(_2d3, "2d3");
      put(_2d4, "2d4");
      put(_2d5, "2d5");
      put(_2d6, "2d6");
      put(_2d8, "2d8");
      put(_3d10, "3d10");
      put(_3d12, "3d12");
      put(_3d3, "3d3");
      put(_3d4, "3d4");
      put(_3d5, "3d5");
      put(_3d6, "3d6");
      put(_3d7, "3d7");
      put(_3d8, "3d8");
      put(_3d9, "3d9");
      put(_4d10, "4d10");
      put(_4d12, "4d12");
      put(_4d14, "4d14");
      put(_4d3, "4d3");
      put(_4d4, "4d4");
      put(_4d5, "4d5");
      put(_4d6, "4d6");
      put(_4d8, "4d8");
      put(_4d9, "4d9");
      put(_5d10, "5d10");
      put(_5d12, "5d12");
      put(_5d14, "5d14");
      put(_5d2, "5d2");
      put(_5d4, "5d4");
      put(_5d5, "5d5");
      put(_5d6, "5d6");
      put(_5d8, "5d8");
      put(_6d10, "6d10");
      put(_6d12, "6d12");
      put(_6d14, "6d14");
      put(_6d5, "6d5");
      put(_6d6, "6d6");
      put(_6d7, "6d7");
      put(_6d8, "6d8");
      put(_7d10, "7d10");
      put(_7d12, "7d12");
      put(_7d14, "7d14");
      put(_7d6, "7d6");
      put(_7d7, "7d7");
      put(_7d8, "7d8");
      put(_7d9, "7d9");
      put(_8d10, "8d10");
      put(_8d12, "8d12");
      put(_8d14, "8d14");
      put(_8d4, "8d4");
      put(_8d5, "8d5");
      put(_8d6, "8d6");
      put(_8d7, "8d7");
      put(_8d8, "8d8");
      put(_9d10, "9d10");
      put(_9d12, "9d12");
      put(_9d9, "9d9");
   }};

   public static Blow strToBlow(String s){
      return blows.get(s);
   };

   public static String BlowToStr(Blow b){
      return blowToStr.get(b);
   };
}
