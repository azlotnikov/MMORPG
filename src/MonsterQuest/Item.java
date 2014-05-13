package MonsterQuest;

import org.json.simple.JSONObject;
import java.util.ArrayList;
/**
 * Created by razoriii on 29.04.14.
 */
public class Item extends ActiveObj{
   private final String type;
   private final String description;
   private int hp = 40;
   private double speed = 0.001;
   private double damage = 10;
   private ArrayList<Flag> flags = new ArrayList<>();
   private boolean isEquipped = true;

   public Item(Long id, String name, String type, String description) {
      super(Game.getNextGlobalId(), name, new Location(0, 0));
      this.description = description;
      this.type = type;
   }

   public Item(ItemDB itemDB) {
      super(Game.getNextGlobalId(), itemDB.getName(), new Location(0, 0));
      this.type = itemDB.getType();
      this.description = itemDB.getDescription();
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

   public void equip(){
      this.isEquipped = true;
   }

   public void unEquip(){
      this.isEquipped = false;
   }

   public boolean isEquipped(){
      return this.isEquipped;
   }


   public Bonus getBonus(){
      return new Bonus(hp, speed, damage, flags);
   }

   public String getType() {
      return type;
   }

   public String getDescription() {
      return description;
   }
}
