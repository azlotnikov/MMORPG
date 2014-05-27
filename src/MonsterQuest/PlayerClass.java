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

      stat.regenHp = stat.strength * 0.01 + stat.agility * 0.004;
      stat.regenMana = stat.intelligence * 0.01 + stat.agility * 0.004;
      stat.attackDelay = stat.agility * 0.1 + stat.intelligence * 0.02;

      stat.maxHp = stat.strength * 5 + stat.intelligence * 1.5;
      stat.maxMana = stat.intelligence * 4 + stat.agility;
      stat.damage = stat.agility * 2 + stat.strength + stat.intelligence * 0.5;

      stat.speed = ((int)(level / 5)) * 0.002;

   }


   public Stat getStat() {
      return stat;
   }

   public PlayerClassType getClassType() {
      return classType;
   }
}
