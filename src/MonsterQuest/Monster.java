package MonsterQuest;

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster {
   private static final int refresh = 100;
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
   protected int timer_refresh;

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
      this.aim = null;
   }

   public void move() {
      Game.unsetIdInLocation(location);
      if (aim == null || !aim.isLive() || timer_refresh == 0){
         timer_refresh = refresh;
         findAim();
      }
      timer_refresh--;
      if (aim != null){
         if (distance(aim.location) < 1.1){ //TODO 1 + расстояние атаки
            aim.damage(10);
            return;
         } else {
            direction = Dice.getBool(1) ?
                  aim.location.x - location.x < 0 ? Direction.WEST : Direction.EAST:
                  aim.location.y - location.y < 0 ? Direction.NORTH : Direction.SOUTH ;
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

   public void findAim() {
      aim = null;
      for(int i = -alertness; i <= alertness; i++)
         for(int j = -alertness; j <= alertness; j++){
            Monster monster = Game.getActors((int)location.x + i,(int)location.y + j);
            if (monster != null && (aim == null || isHate(monster) && distance(monster.location) < distance(aim.location)))
               aim = monster;
         }
   }

   private void damage(int damage){
      hp -= damage;
   }

   private boolean isHate(Monster monster){
      return true;
   }

   private double distance(Location location){
      return Math.sqrt(Math.pow(location.x - this.location.x, 2) + Math.pow(location.y - this.location.y, 2));
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

   public boolean isLive() {
      return hp > 0;
   }
}