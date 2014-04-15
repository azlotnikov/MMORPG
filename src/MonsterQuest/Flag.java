package MonsterQuest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alexander on 4/15/14.
 */
public enum Flag {
   INVISIBLE,
   COLD_BLOOD,
   NEVER_BLOW,
   NEVER_MOVE,
   DROP_40,
   DROP_60,
   DROP_1,
   DROP_2,
   DROP_3,
   DROP_4,
   DROP_GOOD,
   DROP_GREAT,
   DROP_20,
   REGENERATE,
   PASS_WALL,
   KILL_WALL,
   TAKE_ITEM,
   KILL_ITEM,
   ORC,
   TROLL,
   GIANT,
   DRAGON,
   DEMON,
   RAND_25,
   RAND_50,
   UNDEAD,
   EVIL,
   ANIMAL,
   METAL,
   HURT_LIGHT,
   HURT_ROCK,
   HURT_FIRE,
   HURT_COLD,
   IM_ACID,
   IM_ELEC,
   IM_FIRE,
   IM_COLD,
   IM_POIS,
   IM_WATER,
   NO_FEAR,
   NO_STUN,
   NO_CONF,
   NO_SLEE;

   private static final ConcurrentHashMap<String, Flag> flags = new ConcurrentHashMap(){{
      put("INVISIBLE", INVISIBLE);
      put("COLD_BLOOD", COLD_BLOOD);
      put("NEVER_BLOW", NEVER_BLOW);
      put("NEVER_MOVE", NEVER_MOVE);
      put("DROP_40", DROP_40);
      put("DROP_60", DROP_60);
      put("DROP_1", DROP_1);
      put("DROP_2", DROP_2);
      put("DROP_3", DROP_3);
      put("DROP_4", DROP_4);
      put("DROP_GOOD", DROP_GOOD);
      put("DROP_GREAT", DROP_GREAT);
      put("DROP_20", DROP_20);
      put("REGENERATE", REGENERATE);
      put("PASS_WALL", PASS_WALL);
      put("KILL_WALL", KILL_WALL);
      put("TAKE_ITEM", TAKE_ITEM);
      put("KILL_ITEM", KILL_ITEM);
      put("TAKE_ITEM", TAKE_ITEM);
      put("KILL_ITEM", KILL_ITEM);
      put("ORC", ORC);
      put("TROLL", TROLL);
      put("GIANT", GIANT);
      put("DRAGON", DRAGON);
      put("DEMON", DEMON);
      put("RAND_25", RAND_25);
      put("RAND_50", RAND_50);
      put("UNDEAD", UNDEAD);
      put("EVIL", EVIL);
      put("ANIMAL", ANIMAL);
      put("METAL", METAL);
      put("HURT_LIGHT", HURT_LIGHT);
      put("HURT_ROCK", HURT_ROCK);
      put("HURT_FIRE", HURT_FIRE);
      put("HURT_COLD", HURT_COLD);
      put("IM_ACID", IM_ACID);
      put("IM_ELEC", IM_ELEC);
      put("IM_FIRE", IM_FIRE);
      put("IM_COLD", IM_COLD);
      put("IM_POIS", IM_POIS);
      put("IM_WATER", IM_WATER);
      put("NO_FEAR", NO_FEAR);
      put("NO_STUN", NO_STUN);
      put("NO_CONF", NO_CONF);
      put("NO_SLEEP", NO_SLEE);
   }};

   public static Flag strToFlag(String s){
      return flags.get(s);
   };
}