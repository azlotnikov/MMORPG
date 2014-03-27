package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */
public class Location {

   public static double actorSize = 1.0 - 0.001;

   public double x;
   public double y;

   private double left(){
       return x - actorSize / 2;
   }

   private double right() {
       return x + actorSize / 2;
   }

   private double top() {
       return y - actorSize / 2;
   }

   private double bottom() {
       return y + actorSize / 2;
   }

    public Location(double x, double y) {
      this.x = x;
      this.y = y;
   }



   public boolean equal(Location location) {
      return (location.x == this.x && location.y == this.y);
   }

   public Location getFreeLocation(){
       while (!GameMap.canEnterTile((int)left(), (int)top())
           || !GameMap.canEnterTile((int)right(), (int)top())
           || !GameMap.canEnterTile((int)left(), (int)bottom())
           || !GameMap.canEnterTile((int)right(), (int)bottom())
           || isActiveObjectInFront(Direction.NORTH, 0)){

           x += (Dice.getInt(2, 2) - 2) * (int)Math.pow(-1, Dice.getInt(2, 1));
           y += (Dice.getInt(2, 2) - 2) * (int)Math.pow(-1, Dice.getInt(2, 1));
       }
       return this;
}

   public boolean isActiveObjectInFront(Direction direction, double velocity) {
      switch (direction) {
         case NORTH:
            velocity *= -1;
         case SOUTH:
            for (Player player : Game.getPlayers())
               if ((player.getLocation().x != x || player.getLocation().y != y)
                       && Math.abs(player.getLocation().x - x) < 1.0
                       && Math.abs(player.getLocation().y - y - velocity) < 1.0)
                  return true;
            for (Monster monster : Game.getMonsters())
               if ((monster.getLocation().x != x || monster.getLocation().y != y)
                       && Math.abs(monster.getLocation().x - x) < 1.0
                       && Math.abs(monster.getLocation().y - y - velocity) < 1.0)
                  return true;
            break;
         case WEST:
            velocity *= -1;
         case EAST:
            for (Player player : Game.getPlayers())
               if ((player.getLocation().x != x || player.getLocation().y != y)
                       && Math.abs(player.getLocation().y - y) < 1.0
                       && Math.abs(player.getLocation().x - x - velocity) < 1.0)
                  return true;
            for (Monster monster : Game.getMonsters())
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
      double vFront = bottom();
      double hFront = right();
      int vector = 1;
      switch (direction) {
         case NORTH:
            vector = -1;
            vFront = top();
         case SOUTH:
            if (isActiveObjectInFront(direction, velocity))
               return new Location(x, y);
            if (GameMap.canEnterTile((int) left(), (int) (vFront + velocity * vector))
                    && GameMap.canEnterTile((int) right(), (int) (vFront + velocity * vector)))
               return new Location(x, y + velocity * vector);
            else if (GameMap.canEnterTile((int) (left() + eps), (int) (vFront + velocity * vector))
                    && GameMap.canEnterTile((int) (right() - eps), (int) (vFront + velocity * vector)))
               return new Location((int) ((x + eps) / 0.5) * 0.5, y + velocity * vector);
            else
               return new Location(x, y);
         case WEST:
            vector = -1;
            hFront = left();
         case EAST:
            if (isActiveObjectInFront(direction, velocity))
               return new Location(x, y);
            if (GameMap.canEnterTile((int) (hFront + velocity * vector), (int) top())
                    && GameMap.canEnterTile((int) (hFront + velocity * vector), (int) bottom()))
               return new Location(x + velocity * vector, y);
            else if (GameMap.canEnterTile((int) (hFront + velocity * vector), (int) (top() + eps))
                    && GameMap.canEnterTile((int) (hFront + velocity * vector), (int) (bottom() - eps)))
               return new Location(x + velocity * vector, (int) ((y + eps) / 0.5) * 0.5);
            else
               return new Location(x, y);
         case NONE:
               return new Location(x, y);
         default:
            return this;
      }
   }
}
