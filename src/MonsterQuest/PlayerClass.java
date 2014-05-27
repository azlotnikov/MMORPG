package MonsterQuest;

import static MonsterQuest.PlayerClassType.*;

public class PlayerClass {
   private final PlayerClassType classType;

   private final Stat stat = new Stat();

   public PlayerClass(PlayerClassType classType, int level) {
      this.classType = classType;
      calcClassForLevel(level);
   }

   public void calcClassForLevel(int level) {
      stat.strength = level;
      stat.agility = level;
      stat.intelligence = level;
      switch (classType) {
         case FIGHTER:
            stat.strength *= 2;
            break;
         case ROGUE:
            stat.agility *= 2;
            break;
         case WIZARD:
            stat.intelligence *= 2;
            break;
      }

      stat.regenHp = stat.strength * 0.05;
      stat.regenMana = stat.intelligence * 0.04;
      stat.attackDelay = stat.agility * 0.1;

      stat.maxHp = stat.strength * 5;
      stat.maxMana = stat.intelligence * 4;
      stat.damage = stat.agility * 3;

   }


   public Stat getStat() {
      return stat;
   }
}
