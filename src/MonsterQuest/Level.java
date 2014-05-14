package MonsterQuest;

/**
 * Created by razoriii on 13.05.14.
 */
public class Level {
   private static int expPerLevel = 1000;
   private int exp;

   public int calcLevel() {
      return exp/expPerLevel + 1;
   }

   public int calcExpNextLevel() {
      return expPerLevel;
   }

   public int calcExpLevel() {
      return exp % expPerLevel;
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
