package MonsterQuest;

public enum PlayerClassType {
   FIGHTER, ROGUE, WIZARD;

   public static PlayerClassType strToPlayerClassType(String playerClassType) {
      switch (playerClassType) {
         case "fighter":
            return PlayerClassType.FIGHTER;
         case "rogue":
            return PlayerClassType.ROGUE;
         case "wizard":
            return PlayerClassType.WIZARD;
         default:
            return PlayerClassType.FIGHTER;
      }
   }

   public static String toString(PlayerClassType playerClassType) {
      switch (playerClassType) {
         case FIGHTER:
            return "fighter";
         case ROGUE:
            return "rogue";
         case WIZARD:
            return "wizard";
      }
      return "";
   }
}
