package MonsterQuest;

import java.util.ArrayList;

/**
 * Created by Alexander on 4/30/14.
 */
public class Bonus {
   private int hp;
   private double speed;
   private ArrayList<Flag> flags;

   public Bonus(int hp, double speed, ArrayList<Flag> flags) {
      this.hp = hp;
      this.speed = speed;
      this.flags = flags;
   }

   public Bonus() {
      this.hp = 0;
      this.speed = 0;
      this.flags = new ArrayList<>();
   }

   public void addBonus(Bonus bonus){
      this.hp += bonus.hp;
      this.speed += bonus.speed;
      this.flags.addAll(bonus.flags);
   }

   public int getHP(){
      return this.hp;
   }

   public double getSpeed(){
      return this.speed;
   }

   public ArrayList<Flag> getFlags(){
      return this.flags;
   }
}
