package MonsterQuest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by razoriii on 06.03.14.
 */
public class GameDictionary {
   private static final ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>();

   public static void loadDictionary() {
      dictionary.clear();
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM dictionary");
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            try {
               dictionary.put(rs.getString("char_value"),rs.getString("description"));
            } catch (Throwable e) {

            }
         }
      } catch (Throwable e) {

      }
   }

   public static JSONObject getJsonDictionary() {
      JSONObject jsonAns = new JSONObject();
      for (String key : dictionary.keySet()) {
         jsonAns.put(key, dictionary.get(key));
      }
      return jsonAns;
   }

}
