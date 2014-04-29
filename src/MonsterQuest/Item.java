package MonsterQuest;

/**
 * Created by razoriii on 29.04.14.
 */
public class Item {
   private Location location;
   private Long id;
   private String name;
   private String type;

   public void drop(Location newLocation) {
      //TODO проверять куда падает
      location = newLocation;
      Game.addDroppedItem(this); //TODO спорное решение ??
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

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }
}
