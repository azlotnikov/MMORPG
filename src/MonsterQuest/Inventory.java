package MonsterQuest;

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

   protected void addItem(Item item) {
      items.put(item.getId(), item);
   }


}
