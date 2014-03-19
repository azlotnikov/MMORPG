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

   public boolean isActiveObjectInFront(Direction direction, double velocity)
   {
       switch (direction) {
           case NORTH:
           case SOUTH:
               for (Player player : Game.getPlayers())
                   if ((player.getLocation().x != x || player.getLocation().y != y)
                           && Math.abs(player.getLocation().x - x) < 1.0
                           && Math.abs(player.getLocation().y - y - velocity) < 1.0)
                       return true;
               for (Monster monster: Game.getMonsters())
                   if ((monster.getLocation().x != x || monster.getLocation().y != y)
                           && Math.abs(monster.getLocation().x - x) < 1.0
                           && Math.abs(monster.getLocation().y - y - velocity) < 1.0)
                       return true;
               break;
           case WEST:
           case EAST:
               for (Player player : Game.getPlayers())
                   if ((player.getLocation().x != x || player.getLocation().y != y)
                       && Math.abs(player.getLocation().y - y) < 1.0
                       && Math.abs(player.getLocation().x - x - velocity) < 1.0)
                       return true;
               for (Monster monster: Game.getMonsters())
                   if ((monster.getLocation().x != x || monster.getLocation().y != y)
                           && Math.abs(monster.getLocation().y - y) < 1.0
                           && Math.abs(monster.getLocation().x - x - velocity) < 1.0)
                       return true;
               break;
       }
       return false;
   }

   public Location getNewLocation(Direction direction, double velocity) {
      double eps = 0.15;
      double left = x - playerSize / 2;
      double right = x + playerSize / 2;
      double top = y - playerSize / 2;
      double bottom = y + playerSize / 2;
      double vFront = bottom;
      double hFront = right;
      switch (direction) {
         case NORTH:
            velocity *= -1;
            vFront = top;
         case SOUTH:
             if (isActiveObjectInFront(direction, velocity))
                 return new Location(x, y);
             if (GameMap.canEnterTile((int)left, (int) (vFront + velocity))
              && GameMap.canEnterTile((int)right, (int) (vFront + velocity)))
                 return new Location(x, y + velocity);
             else if (GameMap.canEnterTile((int)(left + eps), (int) (vFront + velocity))
                   && GameMap.canEnterTile((int)(right - eps), (int) (vFront + velocity)))
                 return new Location((int)((x + eps) / 0.5 ) * 0.5, y + velocity);
             else
                 return new Location(x, y);
         case WEST:
            velocity *= -1;
            hFront = left;
         case EAST:
            if (isActiveObjectInFront(direction,velocity))
                 return new Location(x, y);
            if (GameMap.canEnterTile((int) (hFront + velocity), (int)top)
             && GameMap.canEnterTile((int) (hFront + velocity), (int)bottom))
                return new Location(x + velocity, y);
            else if (GameMap.canEnterTile((int) (hFront + velocity), (int)(top + eps))
                  && GameMap.canEnterTile((int) (hFront + velocity), (int)(bottom - eps)))
                return new Location(x + velocity, (int)((y + eps) / 0.5) * 0.5);
            else
                return new Location(x, y);
         case NONE:
            // fall through
         default:
            return this;
      }
   }
}
