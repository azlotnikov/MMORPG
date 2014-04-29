package MonsterQuest;

/**
 * Created by razoriii on 29.04.14.
 */
public class Item {
   private Location location;
   private Long id;

   public void drop(Location newLocation) {
      //TODO check location for drop
      location = newLocation;
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
}
