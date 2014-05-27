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
}
