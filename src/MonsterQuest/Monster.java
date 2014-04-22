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
   protected double speed;
   protected Location location;
   protected Direction direction = Dice.getDirection();
   protected final ArrayList<ArrayList<Blow>> blows;
   protected final ArrayList<Flag> flags;
   protected int alertness;
   protected Monster aim;

   public Monster(
         long id,
         String name,
         String type,
         int hp,
         int alertness,
         double speed,
         ArrayList<ArrayList<Blow>> blows,
         ArrayList<Flag> flags,
         Location location
   ) {
      this.location = location;
      this.name = name;
      this.type = type;
      this.id = id;
      this.hp = hp;
      this.speed = speed;
      this.blows = blows;
      this.flags = flags;
      this.alertness = alertness;
   }

   public void move() {
      }
      }
      Location newLocation = location.getNewLocation(direction, speed);
      if (newLocation.equal(location) || newLocation.isActiveObjectInFront(direction, 0)){
         direction = Dice.getDirection();
      } else {
         location = newLocation;
      }
      Game.setIdInLocation(this);
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

   public double getSpeed() {
      return speed;
   }
}