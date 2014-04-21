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
      return l1 != null && l2 != null && !l1.equal(l2)
            && Math.abs(l1.x - l2.x - deltaX) < 1.0
            && Math.abs(l1.y - l2.y - deltaY) < 1.0;
   }

   public boolean isActiveObjectInFront(Direction direction, double velocity) {
      boolean result = false;
      double deltaX = 0;
      double deltaY = 0;
      switch (direction) {
         case NORTH:
            deltaY = -velocity;
            break;
         case SOUTH:
            deltaY = velocity;
            break;
         case WEST:
            deltaX = -velocity;
            break;
         case EAST:
            deltaX = velocity;
            break;
      }
      for(int i = -1; i <= 1; i++)
         for(int j = -1; j <= 1; j++){
            Monster monster = Game.getActors((int)this.x + i, (int)this.y + j);
            result |= monster != null && isLocationIntersect(monster.getLocation(), this, deltaX, deltaY);
         }
      return result;
   }

   public Location getNewLocation(Direction direction, double velocity) {
      double eps = 0.15;
      double vFront = bottom();
      double hFront = right();
      switch (direction) {
         case NORTH:
            velocity *= -1;
            vFront = top();
         case SOUTH:
            if (GameMap.canEnterTile((int) left(), (int) (vFront + velocity))
                  && GameMap.canEnterTile((int) right(), (int) (vFront + velocity)))
               return new Location(x, y + velocity);
            else if (GameMap.canEnterTile((int) (left() + eps), (int) (vFront + velocity))
                  && GameMap.canEnterTile((int) (right() - eps), (int) (vFront + velocity)))
               return new Location((int) ((x + eps) / 0.5) * 0.5, y + velocity);
            else
               return new Location(x, y);
         case WEST:
            velocity *= -1;
            hFront = left();
         case EAST:
            if (GameMap.canEnterTile((int) (hFront + velocity), (int) top())
                  && GameMap.canEnterTile((int) (hFront + velocity), (int) bottom()))
               return new Location(x + velocity, y);
            else if (GameMap.canEnterTile((int) (hFront + velocity), (int) (top() + eps))
                  && GameMap.canEnterTile((int) (hFront + velocity), (int) (bottom() - eps)))
               return new Location(x + velocity, (int) ((y + eps) / 0.5) * 0.5);
            else
               return new Location(x, y);
         case NONE:
            return new Location(x, y);
         default:
            return this;
      }
   }
}
