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
   private final String type;
   private final int hp;
   private final double speed;
   private final int armor_class;
   private final int alertness;
   private final int depth;
   private final int rarity;
   private final int expKill;
   private final ArrayList<ArrayList<Blow>> blows = new ArrayList<>();
   private final ArrayList<Flag> flags = new ArrayList<>();
   private final String spells;
   private final String description;

   public MonsterDB(
         String name,
         String temp,
         int depth,
         int rarity,
         int expKill,
         int speed,
         int hit_points,
         int armor_class,
         int alertness,
         String blows,
         String flags,
         String spells,
         String description
   ) {

      this.name = name;
      this.type = temp;
      this.depth = depth;
      this.rarity = rarity;
      this.expKill = expKill;
      this.speed = (double)speed * 5 / 10000;
      this.hp = hit_points;
      this.armor_class = armor_class;
      this.alertness = alertness;
      for (String bb : blows.split("\\@")){
         ArrayList<Blow> blow = new ArrayList<>();
         for (String b : bb.split("\\|")){
            blow.add(Blow.strToBlow(b));
         }
         this.blows.add(blow);
      }
      for(String f : flags.split("\\|")){
         this.flags.add(Flag.strToFlag(f));
      }
      this.spells = spells;
      this.description = description;
   }

   public static ArrayList<MonsterDB> loadMonstersFromDB() {
      ArrayList<MonsterDB> monsters = new ArrayList<>();
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM mobs");
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            MonsterDB monster = new MonsterDB(
                    rs.getString("name"),
                    rs.getString("temp"),
                    rs.getInt("depth"),
                    rs.getInt("rarity"),
                    rs.getInt("expKill"),
                    rs.getInt("speed"),
                    rs.getInt("hit_points"),
                    rs.getInt("armor_class"),
                    rs.getInt("alertness"),
                    rs.getString("blows"),
                    rs.getString("flags"),
                    rs.getString("spells"),
                    rs.getString("description")
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

   public int getHp() {
      return hp;
   }

   public BehaviorType getBehavior() {
      return BehaviorType.BH_SIMPLE;
   }

   public double getSpeed() {
      return speed;
   }

   public String getType() {
      return type;
   }

   public ArrayList<ArrayList<Blow>> getBlows(){
      return blows;
   }

   public ArrayList<Flag> getFlags(){
      return flags;
   }
}

