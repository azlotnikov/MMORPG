package MonsterQuest;

public enum Direction {
   NONE, NORTH, SOUTH, EAST, WEST;

   public static Direction strToDirection(String direction){
      switch (direction) {
         case "west":
            return Direction.WEST;
         case "north":
            return Direction.NORTH;
         case "east":
            return Direction.EAST;
         case "south":
            return Direction.SOUTH;
         default:
            return Direction.NONE;
      }
   }
}
