package MonsterQuest;

import java.util.ArrayList;

/**
 * Created by Alexander on 4/30/14.
 */
public class Bonus {
   private Stat stat = new Stat();

   private ArrayList<Flag> flags;

   public Bonus(Stat stat, ArrayList<Flag> flags) {
      this.stat = stat;
      this.flags = flags;
   }

   public Bonus() {
      this.flags = new ArrayList<>();
   }

   public void addBonus(Bonus bonus) {
      this.stat.maxHp += bonus.stat.maxHp;
      this.stat.maxMana += bonus.stat.maxMana;
      this.stat.speed += bonus.stat.speed;
      this.stat.damage += bonus.stat.damage;
      this.stat.regenHp += bonus.stat.regenHp;
      this.stat.regenMana += bonus.stat.regenMana;
      this.stat.attackDelay += bonus.stat.attackDelay;
      this.stat.agility += bonus.stat.agility;
      this.stat.strength += bonus.stat.strength;
      this.stat.intelligence += bonus.stat.intelligence;
      this.flags.addAll(bonus.flags);
   }

   public Stat getStat() {
      return this.stat;
   }


   public ArrayList<Flag> getFlags() {
      return this.flags;
   }
}
