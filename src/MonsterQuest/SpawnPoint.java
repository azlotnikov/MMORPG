package MonsterQuest;

/**
 * Created by razoriii on 26.03.14.
 */
public class SpawnPoint {
   private Location location;
   private int maxDepth;

   public SpawnPoint(Location location, int maxDepth) {
      this.location = location;
      this.maxDepth = maxDepth;
   }

   public void spawnMonster() {
      int monsterTypeIndex;
      do {
         monsterTypeIndex = Dice.getInt(Game.GetCountMonsterTypes(), 1) - 1;
      } while (maxDepth <= Game.getMonsterTypes().get(monsterTypeIndex).getDepth());
      Game.addMonster(Game.createMonster(Game.getMonsterTypes().get(monsterTypeIndex), new Location(location)));
   }

   public Location getLocation() {
      return location;
   }

   public int getmaxDepth() {
      return maxDepth;
   }

   public void setmaxDepth(int maxDepth) {
      this.maxDepth = maxDepth;
   }
}
