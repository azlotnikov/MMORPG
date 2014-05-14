package MonsterQuest;

/**
 * Created by razoriii on 13.05.14.
 */
public class Level {
   private int exp;

   public int calcLevel() {
      return exp/1000 + 1;
   }

   public int calcExpNextLevel() {
      return 1000;
   }

   public int calcExpLevel() {
      return exp % 1000;
   }

   public void addExp(int aExp) {
      exp += aExp;
   }

   public int getExp() {
      return exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }
}
