package MonsterQuest;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameTimer {

   private static Timer gameTimer = null;

   private static long tickValue = 1;

   private static final long TICK_DELAY = 170;

   private static final ConcurrentHashMap<Long, Player> players =
           new ConcurrentHashMap<>();

   protected static synchronized void addPlayer(Player player) {
      if (players.size() == 0) {
         startTimer();
      }
      players.put(player.getId(), player);
   }

   protected static Player findPlayerBySid(String sid) {
      for (Iterator<Player> iterator = GameTimer.getPlayers().iterator();
           iterator.hasNext(); ) {
         Player player = iterator.next();
         if (player.getSid().equals(sid)) {
            return player;
         }
      }

      return null;
   }

   protected static Collection<Player> getPlayers() {
      return Collections.unmodifiableCollection(players.values());
   }

   protected static JSONArray getActors() {
      JSONArray jsonAns = new JSONArray();
      for (Iterator<Player> iterator = GameTimer.getPlayers().iterator();
           iterator.hasNext(); ) {
         Player player = iterator.next();
         JSONObject jsonPlayer = new JSONObject();
         jsonPlayer.put("type", "player");
         jsonPlayer.put("id", player.getId());
         jsonPlayer.put("x", player.getLocation().x);
         jsonPlayer.put("y", player.getLocation().y);
         jsonAns.add(jsonPlayer);
      }
      return jsonAns;
   }

   protected static Player ExaminePlayer(long playerId) {
      return players.get(playerId);
   }

   protected static synchronized void removePlayer(Player player) {
      players.remove(player.getId());
      if (players.size() == 0) {
         stopTimer();
      }
   }

   protected static void tick() {
      JSONObject jsonAns = new JSONObject();
      tickValue++;
      jsonAns.put("tick", tickValue);
      for (Iterator<Player> iterator = GameTimer.getPlayers().iterator();
           iterator.hasNext(); ) {
         Player player = iterator.next();
         player.update(GameTimer.getPlayers());
         //some things
      }
      broadcast(jsonAns.toJSONString());
   }

   protected static void broadcast(String message) {
      for (Player player : GameTimer.getPlayers()) {
         try {
            player.sendMessage(message);
         } catch (IllegalStateException ise) {
         }
      }
   }

   public static void startTimer() {
      GameMap.saveToBdDemoMap();
      GameMap.loadWorldMap();
      GameDictionary.loadDictionary();
      gameTimer = new Timer(GameTimer.class.getSimpleName() + " Timer");
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



