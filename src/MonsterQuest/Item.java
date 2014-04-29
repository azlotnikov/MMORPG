package MonsterQuest;

/**
 * Created by razoriii on 29.04.14.
 */
public class Item {
   private final Long id;
   private final String name;
   private final String type;

   private Location location;

   public Item(Long id, String name, String type) {
      this.id = Game.getNextGlobalId();
      this.name = name;
      this.type = type;
   }

   public Item(ItemDB itemDB) {
      this.id = Game.getNextGlobalId();
      this.name = itemDB.getName();
      this.type = itemDB.getType();
   }

   public Location getLocation() {
      return location;
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

}
