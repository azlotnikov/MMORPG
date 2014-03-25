package MonsterQuest;

import org.json.simple.JSONObject;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster {
   protected final long id;
   protected final String name;
   protected final double hp;
   protected final BehaviorType behavior;
   protected final double speed;
   protected Location location;

   public Monster(long id, String name, double hp, BehaviorType behavior, double speed, Location location)  {
      this.location = location;
      this.name = name;
      this.id = id;
      this.hp = hp;
      this.behavior = behavior;
      this.speed = speed;
   }

   public void move(){
//      Location newLocation = location.getNewLocation(direction, speed);
//      if (super.location.equal(newLocation))
//         switch (direction) {
//            case NORTH:
//               direction = Direction.SOUTH;
//               break;
//            case SOUTH:
//               direction = Direction.NORTH;
//               break;
//            case WEST:
//               direction = Direction.EAST;
//               break;
//            case EAST:
//               direction = Direction.WEST;
//               break;
//         }
//      else
//         super.location = newLocation;
   }

   public JSONObject examine() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("id", id);
      result.put("type", "monster");
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

   public long getId() {
      return id;
   }
}