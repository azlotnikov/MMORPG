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
   protected final ArrayList<Blow> blows;
   protected final ArrayList<Flag> flags;
   protected int alertness;
   protected Monster aim;
   protected int timeToRefresh;

   public Monster(
         long id,
         String name,
         String type,
         int hp,
         int alertness,
         double speed,
         ArrayList<Blow> blows,
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
      if (aim == null || !aim.isLive()){
         findAim();
      }
      if (timeToRefresh == 0){
         direction = Dice.getDirection();
         timeToRefresh = Dice.getInt(2, 250);
      }
      timeToRefresh--;
      if (canAttack(aim)){
         attack(aim);
      } else {
         direction = aim == null ? direction :
               Dice.getBool(1) ?
               aim.location.x - location.x < 0 ? Direction.WEST : Direction.EAST:
               aim.location.y - location.y < 0 ? Direction.NORTH : Direction.SOUTH ;
         Game.unsetMonsterInLocation(location);
         Location newLocation = location.getNewLocation(direction, speed);
         if (newLocation.equal(location) || newLocation.isActiveObjectInFront(direction, 0)){
            direction = Dice.getDirection();
         } else {
            location = newLocation;
         }
         Game.setMonsterInLocation(this);
      }
   }

   public void findAim() {
      aim = null;
      for(int i = -alertness; i <= alertness; i++)
         for(int j = -alertness; j <= alertness; j++){
            Monster monster = Game.getActors((int)location.x + i,(int)location.y + j);
            if (monster != null && isHate(monster) && (aim == null || distance(monster.location) < distance(aim.location)))
               aim = monster;
         }
   }

   private void damage(int damage){
      hp -= damage;
   }

   public void attack(Monster monster){
      int damage = Dice.getInt(2, 10);
//      int i = Dice.getInt(blows.size(), 1);
//      if (blows.get(i - 1).size() == 3){ //TODO Проверить
//         String[] d = Blow.BlowToStr(blows.get(i).get(2)).split("d");
//         damage = Dice.getInt(Integer.parseInt(d[1]), Integer.parseInt(d[0]));
//      }
      monster.damage(damage);
   }

   public boolean canAttack(Monster monster){
      return monster!= null && id != monster.id && distance(monster.location) < 1.1;     //TODO 1 + расстояние атаки
   }

   private boolean isHate(Monster monster){
      return this.type != monster.type;// && monster.type != "player";
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
      result.put("aim", aim.id);
      result.put("alertness", alertness);
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

   public int getHP() {
      return hp;
   }

   public double getSpeed() {
      return speed;
   }

   public void setSpeed(double speed) {
      this.speed = speed;
   }

   public boolean isLive() {
      return hp > 0;
   }
}