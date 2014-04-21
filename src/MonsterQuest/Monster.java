package MonsterQuest;

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster {
   protected final long id;
   protected final String name;
   protected final String type;
   protected int hp;
   protected final BehaviorType behavior;
   protected double speed;
   protected Location location;
   protected Direction direction = Dice.getDirection();
   private final ArrayList<ArrayList<Blow>> blows;
   private final ArrayList<Flag> flags;
   private int alertness;

   public Monster(
         long id,
         String name,
         String type,
         int hp,
         int alertness,
         double speed,
         ArrayList<ArrayList<Blow>> blows,
         ArrayList<Flag> flags,
         BehaviorType behavior,
         Location location
   ) {
      this.location = location;
      this.name = name;
      this.type = type;
      this.id = id;
      this.hp = hp;
      this.behavior = behavior;
      this.speed = speed;
      this.blows = blows;
      this.flags = flags;
      this.alertness = alertness;
   }

   public void move() {
      switch (behavior) {
         case BH_SIMPLE:
            Game.unsetIdInLocation(location);
            Location newLocation = location.getNewLocation(direction, speed);
            if (newLocation.equal(location) || newLocation.isActiveObjectInFront(direction, 0)){
               direction = Dice.getDirection();
            } else {
               location = newLocation;
            }
            Game.setIdInLocation(location);
            break;
         case BH_OTHER:
            break;
      }
   }

   public JSONObject examine() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("id", id);
      result.put("name", name);
      result.put("type", type);
      result.put("HP", hp);
      result.put("speed", speed);
      result.put("x", location.x);
      result.put("y", location.y);
      result.put("result", "ok");
      return result;
   }

   public Location getLocation() {
      return location;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }

   public long getId() {
      return id;
   }
}