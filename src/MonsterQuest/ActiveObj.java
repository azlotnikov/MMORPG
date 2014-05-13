package MonsterQuest;

/**
 * Created by Alexander on 5/13/14.
 */
public class ActiveObj {
   protected final long id;
   protected final String name;
   protected Location location;

   public ActiveObj(long id, String name, Location location) {
      this.id = id;
      this.name = name;
      this.location = location;
   }

   public Location getLocation() {
      return location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public String getName() {
      return name;
   }

   public long getId() {
      return id;
   }

}
