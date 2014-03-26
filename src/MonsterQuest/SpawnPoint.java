package MonsterQuest;

/**
 * Created by razoriii on 26.03.14.
 */
public class SpawnPoint {
   private Location location;

   public SpawnPoint(Location location) {
      this.location = location;
   }

   public void spawnMonster() {
      int monsterTypeIndex = 0;
      if (Dice.shoot(4, 4) > 8) {
         monsterTypeIndex = 1;
      }
      Game.addMonster(Game.createMonster(Game.getMonsterTypes().get(monsterTypeIndex), location));
   }

   public Location getLocation() {
      return location;
   }
}
