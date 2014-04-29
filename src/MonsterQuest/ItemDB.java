package MonsterQuest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by razoriii on 29.04.14.
 */
public class ItemDB {
   private final String name;
   private final String type;


   public ItemDB(String name, String type) {
      this.name = name;
      this.type = type;
   }

   public static ArrayList<ItemDB> loadItemFromDB() {
      ArrayList<ItemDB> items = new ArrayList<>();
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM items");
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            ItemDB item = new ItemDB(
                    rs.getString("name"),
                    rs.getString("type")
            );
            items.add(item);
         }
         connector.close();
      } catch (Throwable e) {
      }
      return items;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }
}
