package MonsterQuest;

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster extends ActiveObj{
   protected final String type; //TODO enum
   protected final Inventory inventory = new Inventory();
   protected final Level level = new Level();
   private long inventoryId = -1;
   protected int maxHp;
   protected int hp;
   protected int expKill;
   protected double speed;
   protected Direction direction = Dice.getDirection();
   protected final ArrayList<Blow> blows;
   protected final ArrayList<Flag> flags;
   protected int alertness;
   protected Monster aim;
   protected int timeToRefresh;
   protected Bonus bonus;

   public Monster(
           long id,
           String name,
           String type,
           int hp,
           int maxHp,
           int expKill,
           int alertness,
           double speed,
           ArrayList<Blow> blows,
           ArrayList<Flag> flags,
           Location location,
           boolean generateInventory
   ) {
      super(id, name, location);
      this.type = type;
      this.hp = hp;
      this.maxHp = maxHp;
      this.expKill = expKill;
      this.speed = speed;
      this.blows = blows;
      this.flags = flags;
      this.alertness = alertness;
      this.aim = null;
      if (generateInventory) {
         generateRandomInventory();
      }
      this.bonus = inventory.calcBonus(); 
   }

   public void generateRandomInventory() {
      //TODO сколько раз бросать?
      for (int i = 0; i < Dice.getInt(1, 1); i++) {
         int itemTypeIndex;
         itemTypeIndex = Dice.getInt(Game.GetCountItemTypes(), 1) - 1;

         inventory.addItem(new Item(Game.getItemTypes().get(itemTypeIndex)));
      }
   }

   public Inventory getInventory() {
      return inventory;
   }

   public void dropInventory() {
      inventory.dropAllItems(location, Game.getDroppedItems());
      this.bonus = inventory.calcBonus();
   }

   public void dropItem(Long itemID) {
      inventory.dropItem(itemID, location, Game.getDroppedItems());
   }

   public void pickUpInventory(){
      inventory.pickUpItem(inventoryId, Game.getDroppedItems());
      this.bonus = inventory.calcBonus();
   }

   public void move() {
      if (aim == null || !aim.isLive()) {
         findAim();
      }
      if (timeToRefresh == 0) {
         direction = Dice.getDirection();
         timeToRefresh = Dice.getInt(2, 250);
      }
      timeToRefresh--;
      if (canAttack(aim)) {
         attack(aim);
      } else {
         direction = aim == null ? direction :
                 Dice.getBool(1) ?
                         aim.location.x - location.x < 0 ? Direction.WEST : Direction.EAST :
                         aim.location.y - location.y < 0 ? Direction.NORTH : Direction.SOUTH;
         Game.unsetMonsterInLocation(location);

         if (!this.location.move(direction, getSpeed())) {
            direction = Dice.getDirection();
         }
         Game.setMonsterInLocation(this);
      }
   }

   public void findAim() {
      aim = null;
      for (int i = -alertness; i <= alertness; i++)
         for (int j = -alertness; j <= alertness; j++) {
            Monster monster = Game.getActors((int) location.x + i, (int) location.y + j);
            if (monster != null && isHate(monster) && (aim == null || distance(monster.location) < distance(aim.location)))
               aim = monster;
         }
   }

   public void gotHit(int damage, Monster attacker){
      hp -= damage;
      if (attacker != null && !isLive()) {
         attacker.getLevel().addExp(getExpKill());
      }
   }

   public void attack(Monster monster){
      monster.gotHit(this.getDamage(), this);
   }

   public int getDamage(){
      int damage = 0;
      for (Blow blow : blows){
         damage += blow.getDamage();
      }
      return damage;
   }

   public boolean canAttack(Monster monster) {
      return monster != null && id != monster.id && distance(monster.location) < 1.1;     //TODO 1 + расстояние атаки
   }

   private boolean isHate(Monster monster) {
      return this.type != monster.type;// && monster.type != "player";
   }

   private double distance(Location location) {
      return Math.sqrt(Math.pow(location.x - this.location.x, 2) + Math.pow(location.y - this.location.y, 2));
   }

   public JSONObject examine() {
      JSONObject result = new JSONObject();
      result.put("action", "examine");
      result.put("examineType", "monster");
      result.put("id", id);
      result.put("name", name);
      result.put("type", type);
      result.put("hp", getHp());
      result.put("maxHp", getMaxHp());
      result.put("speed", getSpeed());
      result.put("damage", getDamage());
//      result.put("aim", aim.id);
      result.put("alertness", alertness);
      result.put("x", location.x);
      result.put("y", location.y);
      result.put("result", "ok");
      return result;
   }

   public String getType() {
      return type;
   }

   public int getHp() {
      return hp + bonus.getHp();
   }

   public int getMaxHp() {
      return maxHp;
   }

   public int getExpKill() {
      return expKill;
   }

   public int getBonusHp() {
      return bonus.getHp();
   }

   public Level getLevel() {
      return level;
   }

   public double getBonusDamage() {
      return bonus.getDamage();
   }

   public double getBonusSpeed() {
      return bonus.getSpeed();
   }

   public double getSpeed() {
      return speed + bonus.getSpeed();
   }

   public void setSpeed(double speed) {
      this.speed = speed;
   }

   public boolean isLive() {
      return getHp() > 0;
   }

   public void setInventoryId(long inventoryId){
      this.inventoryId = inventoryId;
   }

   public long getInventoryId(){
      return inventoryId;
   }
}

