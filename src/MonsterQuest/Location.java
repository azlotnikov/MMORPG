package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */
public class Location {

   public double actorSize;
   public double x;
   public double y;

   public Location(Location location) {
      this.x = location.x;
      this.y = location.y;
      this.actorSize = 1.0 - 0.001;
   }

   public Location(double x, double y) {
      this.x = x;
      this.y = y;
      this.actorSize = 1.0 - 0.001;
   }

   public Location(Location location, double actorSize) {
      this.x = location.x;
      this.y = location.y;
      this.actorSize = actorSize;
   }

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

   public boolean equal(Location location) {
      return (location.x == this.x && location.y == this.y);
   }

   public boolean move(Direction direction, double length) {
      double min_lenght = 0.02;
      boolean moveForward = false;
      boolean result = false;
      while(length > min_lenght){
         double oldX = x;
         double oldY = y;
         double len = Math.min(1.0, length);
         switch (direction) {
            case NORTH:
               y -= len;
               break;
            case SOUTH:
               y += len;
               break;
            case WEST:
               x -= len;
               break;
            case EAST:
               x += len;
               break;
         }
         boolean canMove = GameMap.canEnterTile(left(), top())
                        && GameMap.canEnterTile(right(),top())
                        && GameMap.canEnterTile(left(), bottom())
                        && GameMap.canEnterTile(right(),bottom());
         if (!canMove){
            double eps = 0.15;
            double vFront = bottom();
            double hFront = right();
            switch (direction) {
               case NORTH:
                  vFront = top();
               case SOUTH:
                  canMove = GameMap.canEnterTile(left() + eps, vFront)
                         && GameMap.canEnterTile(right() - eps, vFront);
                  x = canMove ? (int)((x + eps) / 0.5) * 0.5 : x;
                  break;
               case WEST:
                  hFront = left();
               case EAST:
                  canMove = GameMap.canEnterTile(hFront, top() + eps)
                         && GameMap.canEnterTile(hFront, bottom() - eps);
                  y = canMove ? (int)((y + eps) / 0.5) * 0.5 : y;
            }
         }
         canMove &= !isActiveObjectsIntersect();

         if (canMove)
            length = moveForward ? len / 2 : length - len;
         else {
            x = oldX;
            y = oldY;
            moveForward = true;
            length = len / 2;
         }
         result |= canMove;
      }
      return result;
   }

   public boolean moveProjectiles(double dx, double dy, double length) {
      int maxCountStep = (int)(length / 0.05);
      boolean canMove = true;
      for (int i = 0; i < maxCountStep && canMove; i++){
         x += dx / maxCountStep;
         y += dy / maxCountStep;

         canMove = GameMap.canEnterTile(left(), top())
               && GameMap.canEnterTile(right(),top())
               && GameMap.canEnterTile(left(), bottom())
               && GameMap.canEnterTile(right(),bottom())
               && !isActiveObjectsIntersect();
      }
      return canMove;
   }

   public Location getFreeLocation(){
       while (!GameMap.canEnterTile(left(), top())
           || !GameMap.canEnterTile(right(), top())
           || !GameMap.canEnterTile(left(), bottom())
           || !GameMap.canEnterTile(right(), bottom())
           || isActiveObjectsIntersect()){

           x += (Dice.getInt(2, 2) - 2) * (int)Math.pow(-1, Dice.getInt(2, 1));
           y += (Dice.getInt(2, 2) - 2) * (int)Math.pow(-1, Dice.getInt(2, 1));
       }
       return this;
}

   private static boolean isLocationIntersect(Location l1, Location l2){
      return l1 != null && l2 != null
            && Math.abs(l1.x - l2.x) < (l1.actorSize / 2 + l2.actorSize / 2)
            && Math.abs(l1.y - l2.y) < (l1.actorSize / 2 + l2.actorSize / 2);
   }

   public boolean isActiveObjectsIntersect() {
      boolean result = false;
      for(int i = -1; i <= 1; i++)
         for(int j = -1; j <= 1; j++){
            Monster monster = Game.getActors((int)this.x + i, (int)this.y + j);
            result |= monster != null && isLocationIntersect(monster.getLocation(), this);
         }
//      for(int i = -1; i <= 1 && !result; i++)
//         for(int j = -1; j <= 1 && !result; j++)
//            for(Projectiles projectiles : Game.getProjectiles((int)this.x + i, (int)this.y + j))
//               if (isLocationIntersect(projectiles.getLocation(), this)){
//                  projectiles.setMustBang();
//                  return true;
//               } // TODO исправить пересечение projectile'ов и игроков
      return result;
   } 
}