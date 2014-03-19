package MonsterQuest;

import org.json.simple.JSONObject;

/**
 * Created by Alexander on 3/18/14.
 */
public abstract class Monster {
   protected final MonsterType type;
   protected final String name;
   protected final long id;
   protected Location location;

   public Monster(MonsterType type, Location location, String name, long id) {
      this.type = type;
      this.location = location;
      this.name = name;
      this.id = id;
   }

   public void saveStateToBD() {
      MonsterDB.saveMonsterStateToDB(this);
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

   public abstract void move();

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