package MonsterQuest;

/**
 * Created by razoriii on 06.03.14.
 */

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;

public class GameMap {

   public static char[][] worldMap;

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
      } catch (Throwable e) {

      }
   }


}
