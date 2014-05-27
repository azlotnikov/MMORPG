package MonsterQuest;

/**
 * Created by razoriii on 19.03.14.
 */

import java.sql.*;
import java.util.ArrayList;

public class MonsterDB {

   private final String name;
   private final String type;
   private final double hp;
   private final double mana;
   private final double regenHp;
   private final double regenMana;
   private final double attackDelay;
   private final double speed;
   private final int armorClass;
   private final int alertness;
   private final int depth;
   private final int rarity;
   private final int expKill;
   private final ArrayList<Blow> blows = new ArrayList<>();
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
      this.mana = 500;
      this.regenHp = 0.1;
      this.regenMana = 0.1;
      this.attackDelay = 13;
      this.armorClass = armor_class;
      this.alertness = (int)alertness + 1;
      for (String b : blows.split("\\@")){
         String[] args = b.split("\\|");
         if (args.length == 3){
            String[] d = args[2].split("d");
            this.blows.add(new Blow(args[0], args[1], Integer.parseInt(d[0]), Integer.parseInt(d[1])));
         } else {
            this.blows.add(new Blow(args[0], args.length == 2 ? args[1] : "", 0, 0));
         }
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

   public double getHp() {
      return hp;
   }
   public double getMana() {
      return mana;
   }
   public double getRegenMana() {
      return regenMana;
   }
   public double getRegenHp() {
      return regenHp;
   }
   public double getAttackDelay() {
      return attackDelay;
   }

   public double getSpeed() {
      return speed;
   }

   public String getType() {
      return type;
   }

   public ArrayList<Blow> getBlows(){
      return blows;
   }

   public ArrayList<Flag> getFlags(){
      return flags;
   }

   public int getAlertness(){
      return alertness;
   }

   public int getExpKill() {
      return expKill;
   }
}

