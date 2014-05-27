package MonsterQuest;

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 3/18/14.
 */
public class Monster extends ActiveObj {
   protected final String type; //TODO enum
   protected final Inventory inventory = new Inventory();
   private long inventoryId = -1;
   protected Direction direction = Dice.getDirection();
   protected Monster aim;
   protected int timeToRefresh;

   protected final Level level = new Level();

   protected final Stat stat = new Stat();

   protected double hp;
   protected double mana;

   protected double currentAttackDelay;

   protected int expKill;
   protected int alertness;
   protected final ArrayList<Blow> blows;
   protected final ArrayList<Flag> flags;

   protected Bonus bonus;

   public Monster(
           long id,
           String name,
           String type,
           double maxHp,
           double maxMana,
           double regenHp,
           double regenMana,
           double attackDelay,
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
      this.hp = maxHp;
      this.mana = maxMana;
      this.stat.maxHp = maxHp;
      this.stat.maxMana = maxMana;
      this.stat.regenHp = regenHp;
      this.stat.regenMana = regenMana;
      this.stat.attackDelay = attackDelay;
      this.stat.speed = speed;
      this.expKill = expKill;
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
      this.bonus = inventory.calcBonus();
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
      this.bonus = inventory.calcBonus();
   }

   public void pickUpInventory() {
      inventory.pickUpItem(inventoryId, Game.getDroppedItems());
      this.bonus = inventory.calcBonus();
   }

   public void decAttackDelay() {
      if (currentAttackDelay > 0) {
         --currentAttackDelay; // TODO Use min ??
         if (currentAttackDelay < 0) {
            currentAttackDelay = 0;
         }
      }
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

   public void gotHit(double damage, Monster attacker) {
      hp -= damage;
      if (attacker != null && !isLive()) {
         attacker.addExp(getExpKill());
      }
   }

   public void attack(Monster monster) {
      monster.gotHit(this.getDamage(), this);
      currentAttackDelay = getAttackDelay();
   }

   public double getDamage() {
      int damage = 0;
      for (Blow blow : blows) {
         damage += blow.getDamage();
      }
      return damage * 5;
   }

   public boolean canAttack(Monster monster) {
      return monster != null && id != monster.id && currentAttackDelay == 0 && distance(monster.location) < 1.1;     //TODO 1 + расстояние атаки
   }

   private boolean isHate(Monster monster) {
      return !this.type.equals(monster.type);// && monster.type != "player";
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
      result.put("hp", hp);
      result.put("maxHp", getMaxHp());
      result.put("regenHp", getRegenHp() * Game.getTicksPerSecond());
      result.put("mana", mana);
      result.put("maxMana", getMaxMana());
      result.put("regenMana", getRegenMana() * Game.getTicksPerSecond());
      result.put("speed", getSpeed() * Game.getTicksPerSecond());
      result.put("damage", getDamage());
      result.put("attackDelay", getAttackDelay() / Game.getTicksPerSecond());
      result.put("expKill", getExpKill());
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

   public double getHp() {
      return hp;
   }

   public double getMana() {
      return mana;
   }

   public Stat getStat() {
      return stat;
   }

   public int getExpKill() {
      return expKill;
   }

   public Level getLevel() {
      return level;
   }

   public double getSpeed() {
      return stat.speed;
   }

   public double getMaxHp() {
      return stat.maxHp;
   }

   public double getMaxMana() {
      return stat.maxMana;
   }

   public double getRegenHp() {
      return stat.regenHp;
   }

   public double getRegenMana() {
      return stat.regenMana;
   }

   public double getAttackDelay() {
      return stat.attackDelay;
   }

   public int getStrength() {
      return stat.strength;
   }

   public int getAgility() {
      return stat.agility;
   }

   public int getIntelligence() {
      return stat.intelligence;
   }

   public void addExp(int expKill) {
      return;
   }

   public void regenHpAndMana() {
      hp += getRegenHp();
      if (hp > getMaxHp()) {
         hp = getMaxHp();
      }
      //TODO use max ??
      mana += getRegenMana();
      if (mana > getMaxMana()) {
         mana = getMaxMana();
      }
   }

   public boolean isLive() {
      return getHp() > 0;
   }

   public void setInventoryId(long inventoryId) {
      this.inventoryId = inventoryId;
   }

   public long getInventoryId() {
      return inventoryId;
   }
}

