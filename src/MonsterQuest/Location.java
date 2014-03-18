package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */
public class Location {

   public static double playerSize = 1.0 - 0.001;

   public double x;
   public double y;

   public Location(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public boolean equal(Location location) {
      return (location.x == this.x && location.y == this.y);
   }

   private static boolean nearCenter(double x, double eps)
   {
       return (x + eps > (int) x + 0.5 && x - eps < (int) x + 0.5);
   }

   public Location getNewLocation(Direction direction, double velocity) {
      double eps = 0.15;
      switch (direction) {
         case NORTH:
            if (GameMap.canEnterTile((int) (x - playerSize / 2), (int) (y - velocity - playerSize / 2))
             && GameMap.canEnterTile((int) (x + playerSize / 2), (int) (y - velocity - playerSize / 2)))
                return new Location(x, y - velocity);
            else if (nearCenter(x, eps))
                return new Location((int) x + 0.5, y);
            else
                return new Location(x, y);
         case SOUTH:
             if (GameMap.canEnterTile((int) (x - playerSize / 2), (int) (y + velocity + playerSize / 2))
              && GameMap.canEnterTile((int) (x + playerSize / 2), (int) (y + velocity + playerSize / 2)))
                 return new Location(x, y + velocity);
             else if (nearCenter(x, eps))
                 return new Location((int) x + 0.5, y);
             else
                 return new Location(x, y);
         case EAST:
             if (GameMap.canEnterTile((int) (x + velocity + playerSize / 2), (int) (y - playerSize / 2))
              && GameMap.canEnterTile((int) (x + velocity + playerSize / 2), (int) (y + playerSize / 2)))
                 return new Location(x + velocity, y);
             else if (nearCenter(y, eps))
                 return new Location(x, (int) y + 0.5);
             else
                 return new Location(x, y);
         case WEST:
             if (GameMap.canEnterTile((int) (x - velocity - playerSize / 2), (int) (y - playerSize / 2))
              && GameMap.canEnterTile((int) (x - velocity - playerSize / 2), (int) (y + playerSize / 2)))
                 return new Location(x - velocity, y);
             else if (nearCenter(y, eps))
                 return new Location(x, (int) y + 0.5);
             else
                 return new Location(x, y);
         case NONE:
            // fall through
         default:
            return this;
      }
   }
}
