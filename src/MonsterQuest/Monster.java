package MonsterQuest;

/**
 * Created by Alexander on 3/18/14.
 */
public abstract class Monster {
   protected final MonsterType type;
   protected final String name;
   protected final long id;
   protected Location location;

   public Monster(MonsterType type, Location location, String name, long id) {
      this.type = type;
      this.location = location;
      this.name = name;
      this.id = id;
   }

   public void saveStateToBD() {
      MonsterDB.saveMonsterStateToDB(this);
   }

   public abstract void move();

   public Location getLocation() {
      return location;
   }

   public String getName() {
      return name;
   }

   public long getId() {
      return id;
   }
}