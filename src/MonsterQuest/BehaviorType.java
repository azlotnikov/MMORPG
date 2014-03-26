package MonsterQuest;

/**
 * Created by razoriii on 25.03.14.
 */
public enum BehaviorType {
   BH_SIMPLE, BH_OTHER;

   public static BehaviorType fromInteger(int x) {
      switch(x) {
         case 1:
            return BH_SIMPLE;
         case 2:
            return BH_OTHER;
      }
      return null;
   }
}
