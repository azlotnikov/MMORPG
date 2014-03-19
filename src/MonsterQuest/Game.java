package MonsterQuest;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Game {

   private static Timer gameTimer = null;

   private static long tickValue = 1;

   private static int saveToDBTick = 1;

   private static int DB_SAVE_DELAY = 20;

   private static final long TICK_DELAY = 50;

   private static final ConcurrentHashMap<Long, Player> players =
           new ConcurrentHashMap<>();
   private static final ConcurrentHashMap<Long, Monster> monsters =
           new ConcurrentHashMap<>();

   protected static synchronized void addPlayer(Player player) {
      if (players.size() == 0) {
         startTimer();
      }
      players.put(player.getId(), player);
   }

   protected static synchronized void addMonster(Monster monster) {
      monsters.put(monster.getId(), monster);
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

   protected static JSONArray getActors(double x, double y) {
      JSONArray jsonAns = new JSONArray();
      for (Player player : getPlayers()) {
         if (Math.abs(player.getLocation().x - x) > GameMap.SIGHT_RADIUS
                 && Math.abs(player.getLocation().y - y) > GameMap.SIGHT_RADIUS)
            continue;
         JSONObject jsonPlayer = new JSONObject();
         jsonPlayer.put("type", "player");
         jsonPlayer.put("id", player.getId());
         jsonPlayer.put("x", player.getLocation().x);
         jsonPlayer.put("y", player.getLocation().y);
         jsonAns.add(jsonPlayer);
      }
      for (Monster monster : getMonsters()) {
         if (Math.abs(monster.getLocation().x - x) > GameMap.SIGHT_RADIUS
                 && Math.abs(monster.getLocation().y - y) > GameMap.SIGHT_RADIUS)
            continue;
         JSONObject jsonPlayer = new JSONObject();
         jsonPlayer.put("type", "monster");
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
      if (players.size() == 0) {
         stopTimer();
      }
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
         for (Monster monster : getMonsters()) {
            monster.saveStateToBD();
         }
         saveToDBTick = 0;
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

   private static void loadMonsters() {
      for (Monster monster : MonsterDB.loadMonstersFromDB()) {
         Game.addMonster(monster);
      }
   }

   public static void startTimer() {
      GameMap.saveToBdDemoMap();
      GameMap.loadWorldMap();
      GameDictionary.loadDictionary();
      Game.loadMonsters();
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

}



