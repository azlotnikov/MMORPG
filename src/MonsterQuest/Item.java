package MonsterQuest;

import java.util.ArrayList;

/**
 * Created by razoriii on 29.04.14.
 */
public class Item {
   private final Long id;
   private final String name;
   private final String type;
   private final String description;
   private int hp = 43;
   private double speed = 0.1;
   private ArrayList<Flag> flags = new ArrayList<>();
   private boolean isEquipped = true;

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

   public void setLocation(Location location) {
      this.location = location;
   }

   public Long getId() {
      return id;
   }

   public void equipped(){
      this.isEquipped = true;
   }

   public void unEquipped(){
      this.isEquipped = false;
   }

   public boolean isEquipped(){
      return this.isEquipped;
   }


   public Bonus getBonus(){
      return new Bonus(hp, speed, flags);
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
