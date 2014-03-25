package MonsterQuest;

/**
 * Created by razoriii on 19.03.14.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MonsterDB {

   private final String name;
   private final double hp;
   private final BehaviorType behavior;
   private final double speed;

   public MonsterDB(String name, double hp, BehaviorType behavior, double speed) {
      this.name = name;
      this.hp = hp;
      this.behavior = behavior;
      this.speed = speed;
   }

   public static ArrayList<MonsterDB> loadMonstersFromDB() {
      ArrayList<MonsterDB> monsters = new ArrayList<>();
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM monster_types");
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            MonsterDB monster = new MonsterDB(
                    rs.getString("name"),
                    rs.getDouble("hit_points"),
                    BehaviorType.BH_SIMPLE,
                    rs.getDouble("speed")
            );
            monsters.add(monster);
         }
         connector.close();
      } catch (Throwable e) {
      }
      return monsters;
   }


   public String getName() {
      return name;
   }

   public double getHp() {
      return hp;
   }

   public BehaviorType getBehavior() {
      return behavior;
   }

   public double getSpeed() {
      return speed;
   }
}
