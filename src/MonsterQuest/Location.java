package MonsterQuest;

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
            if (GameMap.canEnderTitle((int)x, (int)(y - velocity)))
                return new Location(x, y - velocity);
            else
                return new Location(x, y);
         case SOUTH:
             if (GameMap.canEnderTitle((int)x, (int)(y + velocity)))
                 return new Location(x, y + velocity);
             else
                 return new Location(x, y);
         case EAST:
             if (GameMap.canEnderTitle((int)(x + velocity), (int)y))
                 return new Location(x + velocity, y);
             else
                 return new Location(x, y);
         case WEST:
             if (GameMap.canEnderTitle((int)(x - velocity), (int)y))
                 return new Location(x - velocity, y);
             else
                 return new Location(x, y);
         case NONE:
            // fall through
         default:
            return this;
      }
   }
}
