package MonsterQuest;

/**
 * Created by razoriii on 06.03.14.
 */

import org.json.simple.JSONArray;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;

public class GameMap {

   private static char[][] worldMap;

   public static void loadWorldMap() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection connector = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_USER, DBInfo.DB_PASS);
         PreparedStatement stmt = connector.prepareStatement("SELECT map FROM game_data");
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            try (final ObjectInputStream ois = new ObjectInputStream(rs.getBlob(1).getBinaryStream())) {
               worldMap = (char[][]) ois.readObject();
            }
         }

      } catch (Throwable e) {

      }
   }

   public static void saveWorldMap() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection connector = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_USER, DBInfo.DB_PASS);
         PreparedStatement stmt = connector.prepareStatement("UPDATE game_data SET map = ?");
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final ObjectOutputStream oos = new ObjectOutputStream(baos);
         oos.writeObject(worldMap);
         stmt.setBlob(1, new SerialBlob(baos.toByteArray()));
         stmt.executeUpdate();
      } catch (Throwable e) {

      }
   }

   public static JSONArray mapToJson() {
      JSONArray jsonResult = new JSONArray();
      JSONArray jsonLine;
      for (char[] i : worldMap) {
         jsonLine = new JSONArray();
         for(char j : i) {
            jsonLine.add(String.valueOf(j));
         }
         jsonResult.add(jsonLine);
      }
      return jsonResult;
   }

   public static void saveToBdDemoMap() {
      worldMap = new char[][] {
               "#...#..#".toCharArray()
              ,"#...##.#".toCharArray()
              ,"#....###".toCharArray()
              ,"###..###".toCharArray()
              ,"#.##...#".toCharArray()
              ,"#....#.#".toCharArray()
              ,"########".toCharArray()
      };
      saveWorldMap();
   }


}
