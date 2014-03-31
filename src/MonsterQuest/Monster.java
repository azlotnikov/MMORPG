package MonsterQuest;

import org.json.simple.JSONObject;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster {
   protected final long id;
   protected final String name;
   protected final String type;
   protected final double hp;
   protected final BehaviorType behavior;
   protected final double speed;
   protected Location location;
   protected Direction direction = Dice.getDirection();

   public Monster(long id, String name, String type, double hp, BehaviorType behavior, double speed, Location location) {
      this.location = location;
      this.name = name;
      this.type = type;
      this.id = id;
      this.hp = hp;
      this.behavior = behavior;
      this.speed = speed;
   }

   public void move() {
      switch (behavior) {
         case BH_SIMPLE:
            Location newLocation = location.getNewLocation(direction, speed);
            if (newLocation.equal(location) || newLocation.isActiveObjectInFront(direction, 0)){
               direction = Dice.getDirection();
            } else {
               Game.unsetIdInLocation(location);
               Game.setIdInLocation(newLocation);
               location = newLocation;
            }
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