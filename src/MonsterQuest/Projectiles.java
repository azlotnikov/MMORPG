package MonsterQuest;

/**
 * Created by Alexander on 5/13/14.
 */
public class Projectiles extends ActiveObj {
   private final AttackMethod type;
   private final double deltaX;
   private final double deltaY;
   private final double speed;
   private static final double minRadius = 0.001;
   private final double radius;
   private final double radiusBang;
   private final double damage;
   private final Monster parent;
   private boolean mustBang;

   public Projectiles(AttackMethod type
           , Location location
           , double speed
           , double deltaX
           , double deltaY
           , double radius
           , double radiusBang
           , double damage
           , Monster parent
   ) {
      super(Game.getNextGlobalId(), type.toString(), new Location(location, ((radius == 0) ? minRadius : radius) * 2));
      this.type = type;
      this.radius = Math.max(minRadius, radius);
      this.radiusBang = radiusBang;
      this.parent = parent;
      this.speed = speed;
      double k = speed / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
      this.deltaX = deltaX * k;
      this.deltaY = deltaY * k;
      this.mustBang = false;
      this.damage = damage;
   }

   public void move() {
      if (mustBang) return;
      Game.unsetMonsterInLocation(parent.getLocation());
      Game.unsetProjectilesInLocation(this);
      mustBang = !location.moveProjectiles(deltaX, deltaY, speed);
      Game.setProjectilesInLocation(this);
      Game.setMonsterInLocation(parent);

   }

   public void bang() {
      for (int i = -(int) radiusBang; i <= (int) radiusBang; i++)
         for (int j = -(int) radiusBang; j <= (int) radiusBang; j++) {
            Monster monster = Game.getActors((int) this.location.x + i, (int) this.location.y + j);
            if (monster != null && monster != parent)
               monster.gotHit(damage, parent);
         }
   }

   public void setMustBang() {
      mustBang = true;
   }

   public boolean mustBang() {
      return mustBang;
   }

   public AttackMethod getType() {
      return type;
   }

}
