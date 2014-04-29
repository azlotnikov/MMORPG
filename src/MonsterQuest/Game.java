package MonsterQuest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Game {

   // TODO Убрать static (может быть много карт)

   private static final AtomicInteger globalId = new AtomicInteger(0);

   private static final HashMap<String, Long> playerIdsBySid = new HashMap<>();

   private static final ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();

   private static final ConcurrentHashMap<Long, Monster> monsters = new ConcurrentHashMap<>();

   private static final ArrayList<MonsterDB> monsterTypes = new ArrayList<>();

   private static final ArrayList<ItemDB> itemTypes = new ArrayList<>();

   private static final ArrayList<SpawnPoint> spawnPoints = new ArrayList<>();

   private static final Inventory droppedItems = new Inventory();

   private static Timer gameTimer = null;

   private static long tickValue = 1;

   private static boolean started = false;

   private static int saveToDBTick = 1;

   private static final int DB_SAVE_DELAY = 20;

   private static final long TICK_DELAY = 50;

   private static Monster[][] actorsMap;

   public static JSONArray getDroppedItemsJSON() {
      return droppedItems.inventoryToJSON();
   }

   public static void addDroppedItem(Item item) {
      droppedItems.addItem(item);
   }

   public static void deleteDroppedItem(Long itemId) {
      droppedItems.removeItem(itemId);
   }

   public static Item getDroppedItem(Long itemId) {
      return droppedItems.getItem(itemId);
   }

   private static void initializeActorsMap(int height, int width) {
      actorsMap = new Monster[height][];
      for (int i = 0; i < height; i++) {
         Monster[] line = new Monster[width];
         Arrays.fill(line, null);
         actorsMap[i] = line;
      }
   }

   public static long getTicksPerSecond() {
      return (long) 1000 / TICK_DELAY;
   }

   public static void setMonsterInLocation(Monster monster) {
      actorsMap[(int) monster.getLocation().y][(int) monster.getLocation().x] = monster;
   }

   public static void unsetMonsterInLocation(Location location) {
      actorsMap[(int) location.y][(int) location.x] = null;
   }

   public static Monster getActors(int x, int y) {

      return x > 0 && x < GameMap.getWidth()
              && y > 0 && y < GameMap.getHeight()
              ? actorsMap[y][x] : null;
   }

   protected static synchronized void addPlayer(Player player) {
      if (!started) {
         startTimer();
      }
      Game.setMonsterInLocation(player);
      players.put(player.getId(), player);
   }

   protected static synchronized void addMonster(Monster monster) {
      Game.setMonsterInLocation(monster);
      monsters.put(monster.getId(), monster);
   }

   protected static synchronized void addSpawnPoint(SpawnPoint spawnPoint) {
      spawnPoints.add(spawnPoint);
   }

   protected static Monster createMonster(MonsterDB monsterType, Location location) {
      return new Monster
              (getNextGlobalId()
                      , monsterType.getName()
                      , monsterType.getType()
                      , monsterType.getHp()
                      , monsterType.getAlertness()
                      , monsterType.getSpeed()
                      , monsterType.getBlows()
                      , monsterType.getFlags()
                      , location.getFreeLocation()
                      , true
              );
   }

   protected static Player findPlayerBySid(String sid) {
      for (Player player : getPlayers()) {
         if (player.getSid().equals(sid)) {
            return player;
         }
      }
      return null;
   }

   protected static Collection<Player> getPlayers() {
      return Collections.unmodifiableCollection(players.values());
   }

   protected static Collection<Monster> getMonsters() {
      return Collections.unmodifiableCollection(monsters.values());
   }

   protected static ArrayList<MonsterDB> getMonsterTypes() {
      return monsterTypes;
   }

   protected static ArrayList<ItemDB> getItemTypes() {
      return itemTypes;
   }

   protected static JSONArray getActors(Location location) {
      JSONArray jsonAns = new JSONArray();
      for (int j = -GameMap.SIGHT_RADIUS_Y; j < GameMap.SIGHT_RADIUS_Y; j++)
         for (int i = -GameMap.SIGHT_RADIUS_X; i < GameMap.SIGHT_RADIUS_X; i++) {
            Monster monster = Game.getActors((int) location.x - i, (int) location.y - j);
            if (monster != null) {
               JSONObject jsonActor = new JSONObject();
               jsonActor.put("type", monster.type);
               jsonActor.put("id", monster.getId());
               jsonActor.put("x", monster.getLocation().x);
               jsonActor.put("y", monster.getLocation().y);
               jsonAns.add(jsonActor);
            }
         }
      return jsonAns;
   }

   protected static Player ExaminePlayer(long playerId) {
      return players.get(playerId);
   }

   protected static Monster ExamineMonster(long monsterId) {
      return monsters.get(monsterId);
   }

   protected static synchronized void removePlayer(Player player) {
      players.remove(player.getId());
   }

   protected static synchronized void removeMonster(Monster monster) {
      unsetMonsterInLocation(monster.location);
      monsters.remove(monster.getId());
   }

   protected static void tick() {
      JSONObject jsonAns = new JSONObject();
      tickValue++;
      saveToDBTick++;
      jsonAns.put("tick", tickValue);
      if (saveToDBTick >= DB_SAVE_DELAY) {
         for (Player player : getPlayers()) {
            player.saveStateToBD();
         }
         saveToDBTick = 0;
      }

      for (SpawnPoint spawnPoint : spawnPoints) {
         boolean f = Dice.getBool(7);
         if (f) {
            spawnPoint.spawnMonster();
         }
      }

      for (Player player : getPlayers()) {
         player.move();
      }
      broadcast(jsonAns);

      for (Monster monster : getMonsters()) {
         if (monster.isLive())
            monster.move();
         else {
            monster.dropInventory();
            Game.removeMonster(monster);
         }
      }
      broadcast(jsonAns);
   }

   protected static void broadcast(JSONObject message) {
      for (Player player : getPlayers()) {
         try {
            player.sendMessage(message);
         } catch (IllegalStateException ise) {
         }
      }
   }

   private static void loadMonsterTypes() {
      for (MonsterDB monsterDB : MonsterDB.loadMonstersFromDB()) {
         monsterTypes.add(monsterDB);
      }
   }

   private static void loadItemTypes() {
      for (ItemDB itemDB : ItemDB.loadItemFromDB()) {
         itemTypes.add(itemDB);
      }
   }


   public static void startTimer() {
      started = true;
      GameMap.saveToBdDemoMap();
      GameMap.loadWorldMap();
      GameDictionary.loadDictionary();
      Game.loadMonsterTypes();
      Game.loadItemTypes();
      initializeActorsMap(GameMap.getHeight(), GameMap.getWidth());
      addSpawnPoint(new SpawnPoint(new Location(13, 6))); // TODO много точек с разной глубиной
      gameTimer = new Timer(Game.class.getSimpleName() + " Timer");
      gameTimer.scheduleAtFixedRate(new TimerTask() {
         @Override
         public void run() {
            try {
               tick();
            } catch (RuntimeException e) {
            }
         }
      }, TICK_DELAY, TICK_DELAY);
   }

   public static int GetCountItemTypes() {
      return itemTypes.size();
   }

   public static int GetCountMonsterTypes() {
      return monsterTypes.size();
   }

   public static void stopTimer() {
      if (gameTimer != null) {
         gameTimer.cancel();
      }
   }

   public static long getCurrentTick() {
      return tickValue;
   }

   public static synchronized long getNextGlobalId() {
      return globalId.getAndIncrement();
   }

   public static long getPlayerIdBySid(String sid) {
      return playerIdsBySid.get(sid);
   }

   public static void setPlayerIdBySid(String sid, long id) {
      playerIdsBySid.put(sid, id);
   }

}



