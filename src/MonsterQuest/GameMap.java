package MonsterQuest;

/**
 * Created by razoriii on 06.03.14.
 */

import org.json.simple.JSONArray;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;

public class GameMap {

   public static final int SIGHT_RADIUS_X = 12; //TODO Задокументировать облаcть видимости
   public static final int SIGHT_RADIUS_Y = 8;

   private static char[][] worldMap;

   public static int getHeight(){
      return worldMap.length;
   }

   public static int getWidth(){
      return worldMap[0].length;
   }

   public static void loadWorldMap() {
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT map FROM game_data");
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            try (final ObjectInputStream ois = new ObjectInputStream(rs.getBlob(1).getBinaryStream())) {
               worldMap = (char[][]) ois.readObject();
            }
         }

      } catch (Throwable e) {

      }
   }

   public static void saveWorldMap() {
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("UPDATE game_data SET map = ?");
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final ObjectOutputStream oos = new ObjectOutputStream(baos);
         oos.writeObject(worldMap);
         stmt.setBlob(1, new SerialBlob(baos.toByteArray()));
         stmt.executeUpdate();
      } catch (Throwable e) {

      }
   }

   public static JSONArray mapToJson(int x, int y) {
      JSONArray jsonResult = new JSONArray();
      JSONArray jsonLine;
      for (int i = y - SIGHT_RADIUS_Y; i <= y + SIGHT_RADIUS_Y; i++) {
         jsonLine = new JSONArray();
          for (int j = x - SIGHT_RADIUS_X; j <= x + SIGHT_RADIUS_X; j++) {
              String symbol = String.valueOf('#');
              if (i > 0 && i < getHeight() && j > 0 && j < getWidth()) {
                  symbol = String.valueOf(worldMap[i][j]);
              }
              jsonLine.add(symbol);
         }
         jsonResult.add(jsonLine);
      }
      return jsonResult;
   }

   public static void saveToBdDemoMap() {
//      worldMap = new char[][] {
//               "#...#..#".toCharArray()
//              ,"#...##.#".toCharArray()
//              ,"#....###".toCharArray()
//              ,"###..###".toCharArray()
//              ,"#.##...#".toCharArray()
//              ,"#....#.#".toCharArray()
//              ,"########".toCharArray()
//      };
      worldMap = new char[][] {
               "##############################".toCharArray()
              ,"#...#........................#".toCharArray()
              ,"#...#................###.###.#".toCharArray()
              ,"#...###.###..........#.....#.#".toCharArray()
              ,"#.........#..........#.....#.#".toCharArray()
              ,"#..#.....##..........#.....#.#".toCharArray()
              ,"#..##.##........#########..#.#".toCharArray()
              ,"#...............#..........#.#".toCharArray()
              ,"#..####.........#..........#.#".toCharArray()
              ,"#..#............#.....######.#".toCharArray()
              ,"#..#..................#......#".toCharArray()
              ,"#..##########....######......#".toCharArray()
              ,"#............................#".toCharArray()
              ,"#..###...#......#####........#".toCharArray()
              ,"#...#....#......#...#........#".toCharArray()
              ,"#...#....#......#...#........#".toCharArray()
              ,"#...#....#...#..#...#........#".toCharArray()
              ,"#..###...#####..#####........#".toCharArray()
              ,"#............................#".toCharArray()
              ,"#...##......##...####........#".toCharArray()
              ,"#....##....##....#...........#".toCharArray()
              ,"#.....##..##.....##..........#".toCharArray()
              ,"#......####......#...........#".toCharArray()
              ,"#.......##.......####........#".toCharArray()
              ,"#............................#".toCharArray()
              ,"#..####...#.####..####.......#".toCharArray()
              ,"#..#..#...#.#.....#..#.......#".toCharArray()
              ,"#..#####..#.##....#####......#".toCharArray()
              ,"#..#...#..#.#.....#...#......#".toCharArray()
              ,"#..#####..#.####..#####......#".toCharArray()
              ,"#............................#".toCharArray()
              ,"#...####..###................#".toCharArray()
              ,"#...#.....#..#...............#".toCharArray()
              ,"#...##....####...............#".toCharArray()
              ,"#...#.....#..##..............#".toCharArray()
              ,"#...####..#...##.............#".toCharArray()
              ,"#............................#".toCharArray()
              ,"##############################".toCharArray()
       };
      saveWorldMap();
   }

   public static boolean canEnterTile(int x, int y) {
       return worldMap[y][x] != '#';
       // TODO в описании карты добавить пропускную способность
      // TODO перенести огругление сюда, и сделать параметры double
   }

}
