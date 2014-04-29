package MonsterQuest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by razoriii on 29.04.14.
 */
public class Inventory {
   private final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();

   protected Collection<Item> getItems() {
      return Collections.unmodifiableCollection(items.values());
   }

   protected void removeItem(Item item) {
      items.remove(item.getId());
   }

   protected void removeItem(Long itemId) {
      items.remove(itemId);
   }

   protected Item getItem(Long itemId) {
      return items.get(itemId);
   }

   protected void addItem(Item item) {
      items.put(item.getId(), item);
   }

   public JSONArray inventoryToJSON() {
      JSONArray result = new JSONArray();
      for (Item item : getItems()) {
         JSONObject itemData = new JSONObject();
         itemData.put("name", item.getName());
         itemData.put("type", item.getType());
         result.add(itemData);
      }
      return result;
   }

}
