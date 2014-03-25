package MonsterQuest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Game {

   private static final AtomicInteger globalId = new AtomicInteger(0);

   private static final HashMap<String, Long> playerIdsBySid = new HashMap<>();

   private static Timer gameTimer = null;

   private static long tickValue = 1;

   private static boolean started = false;

   private static int saveToDBTick = 1;

   private static final int DB_SAVE_DELAY = 20;

   private static final long TICK_DELAY = 50;

   private static final ConcurrentHashMap<Long, Player> players =
           new ConcurrentHashMap<>();
   private static final ConcurrentHashMap<Long, Monster> monsters =
           new ConcurrentHashMap<>();

   private static final ArrayList<MonsterDB> monsterTypes = new ArrayList<>();

   protected static synchronized void addPlayer(Player player) {
      if (!started) {
         startTimer();
      }
      players.put(player.getId(), player);
   }

   protected static synchronized void addMonster(Monster monster) {
      monsters.put(monster.getId(), monster);
   }

   protected static Monster createMonster(MonsterDB monsterType, Location location) {
      return new Monster(getNextGlobalId(), monsterType.getName(), monsterType.getType(), monsterType.getHp(),
              monsterType.getBehavior(), monsterType.getSpeed(), location);
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

   protected static JSONArray getActors(Location location) {
      JSONArray jsonAns = new JSONArray();
      for (Player player : Game.getPlayers()) {
         if (Math.abs(player.getLocation().x - location.x) > GameMap.SIGHT_RADIUS_X
                 && Math.abs(player.getLocation().y - location.y) > GameMap.SIGHT_RADIUS_Y
             || location.equal(player.getLocation()))
            continue;
         JSONObject jsonPlayer = new JSONObject();
         jsonPlayer.put("type", "player");
         jsonPlayer.put("id", player.getId());
         jsonPlayer.put("x", player.getLocation().x);
         jsonPlayer.put("y", player.getLocation().y);
         jsonAns.add(jsonPlayer);
      }
      for (Monster monster : getMonsters()) {
         if (Math.abs(monster.getLocation().x - location.x) > GameMap.SIGHT_RADIUS_X
                 || Math.abs(monster.getLocation().y - location.y) > GameMap.SIGHT_RADIUS_Y)
            continue;
         JSONObject jsonPlayer = new JSONObject();
         jsonPlayer.put("type", monster.getType());
         jsonPlayer.put("id", monster.getId());
         jsonPlayer.put("x", monster.getLocation().x);
         jsonPlayer.put("y", monster.getLocation().y);
         jsonAns.add(jsonPlayer);
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
      for (Monster monster : getMonsters()) {
         monster.move();
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

   public static void startTimer() {
      started = true;
      GameMap.saveToBdDemoMap();
      GameMap.loadWorldMap();
      GameDictionary.loadDictionary();
      Game.loadMonsterTypes();
      addMonster(createMonster(monsterTypes.get(0), new Location(13, 3)));
      addMonster(createMonster(monsterTypes.get(1), new Location(13, 6)));
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



