package MonsterQuest;

import org.json.simple.JSONObject;

/**
 * Created by razoriii on 29.04.14.
 */
public class Item {
   private final Long id;
   private final String name;
   private final String type;
   private final String description;

   private Location location = new Location(0.0, 0.0);

   public Item(Long id, String name, String type, String description) {
      this.description = description;
      this.id = Game.getNextGlobalId();
      this.name = name;
      this.type = type;
   }

   public Item(ItemDB itemDB) {
      this.id = Game.getNextGlobalId();
      this.name = itemDB.getName();
      this.type = itemDB.getType();
      this.description = itemDB.getDescription();
   }

   public Location getLocation() {
      return location;
   }

   public JSONObject examine() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("examineType", "item");
      result.put("id", id);
      result.put("name", name);
      result.put("type", type);
      result.put("description", description);
      result.put("result", "ok");
      return result;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }

   public String getDescription() {
      return description;
   }
}
