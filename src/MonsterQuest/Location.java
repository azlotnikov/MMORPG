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

   private static boolean isLocationIntersect(Location l1, Location l2, double deltaX, double deltaY){
      return l1 != null && l2 != null
            && Math.abs(l1.x - l2.x) - deltaX < 1.0
            && Math.abs(l1.y - l2.y) - deltaY < 1.0;
   }

   public boolean isActiveObjectInFront(Direction direction, double velocity) {
      boolean result = false;
      int deltaX = 1;
      int deltaY = 1;
      switch (direction) {
         case NORTH:
            deltaY = -1;
         case SOUTH:
            deltaX = x % 1 > 0.5 ? 1 : -1; // point lies to the right or left of tile's center
            result |= isLocationIntersect(Game.getActors((int)this.x, (int)this.y + deltaY), this, 0, velocity);
            result |= isLocationIntersect(Game.getActors((int)this.x + deltaX, (int)this.y + deltaY), this, 0, velocity);

            break;
         case WEST:
            deltaX = -1;
         case EAST:
            deltaY = y % 1 > 0.5 ? 1 : -1; //point lies to the above or below of tile's center
            result |= isLocationIntersect(Game.getActors((int)this.x + deltaX, (int)this.y), this, velocity, 0);
            result |= isLocationIntersect(Game.getActors((int)this.x + deltaX, (int)this.y + deltaY), this, velocity, 0);
            break;
      }  
      return result;
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
