package UserData;

/**
 * Created by razoriii on 04.03.14.
 */
public class Location {

   public double x;
   public double y;

   public Location(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public Location getAdjacentLocation(Direction direction, double velocity) {
      switch (direction) {
         case NORTH:
            return new Location(x, y + velocity);
         case SOUTH:
            return new Location(x, y + velocity);
         case EAST:
            return new Location(x + velocity, y);
         case WEST:
            return new Location(x - velocity, y);
         case NONE:
            // fall through
         default:
            return this;
      }
   }
}
