package MonsterQuest;

/**
 * Created by razoriii on 19.03.14.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MonsterDB {
   public static void saveMonsterStateToDB(Monster monster) {
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("UPDATE monsters SET posX = ?, posY = ? WHERE game_id = ?");
         stmt.setDouble(1, monster.getLocation().x);
         stmt.setDouble(2, monster.getLocation().y);
         stmt.setLong(2, monster.getId());
         stmt.executeUpdate();
         connector.close();
      } catch (Throwable e) {
      }
   }

   public static void insertManyMonsters() {
      try {
         int gid = 10000;
         Connection connector = DBInfo.createConnection();
         for (int i = 3; i < 40; i++) {
            PreparedStatement stmt = connector.prepareStatement("INSERT INTO monsters(name, pos_x, pos_y, game_id) VALUES " +
                    "('lol', ?, ?, ?)");
            stmt.setDouble(1, 3);
            stmt.setDouble(2, i);
            stmt.setLong(3, gid+i);
            stmt.executeUpdate();
         }
         connector.close();

      } catch (Throwable e) {
      }
   }

   public static ArrayList<Monster> loadMonstersFromDB() {
      ArrayList<Monster> monsters = new ArrayList<>();
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM monsters");
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            Monster monster = new RunningMonster(
                    new Location(rs.getDouble("pos_x"), rs.getDouble("pos_x")),
                    Direction.WEST,
                    rs.getString("name"),
                    rs.getLong("game_id")
            );
            monsters.add(monster);
         }
         connector.close();
      } catch (Throwable e) {
      }
      return monsters;
   }


}
