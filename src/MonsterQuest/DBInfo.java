package MonsterQuest;

/**
 * Created by razoriii on 06.03.14.
 */

import java.sql.*;

public class DBInfo {
   public static String DB_URL = "jdbc:mysql://localhost:3306/mmorpg";
   public static String DB_USER = "root";
   public static String DB_PASS = "123456";

   public static Connection createConnection() {
      Connection connection = null;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         connection = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_USER, DBInfo.DB_PASS);
      } catch (Throwable e) {

      }
      return connection;
   }
}
