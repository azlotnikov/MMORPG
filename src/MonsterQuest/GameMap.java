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

   private static char[][] worldMap;

   public static void loadWorldMap() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection connector = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_USER, DBInfo.DB_PASS);
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
         Class.forName("com.mysql.jdbc.Driver");
         Connection connector = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_USER, DBInfo.DB_PASS);
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
      for (int i = y - PlayerAnnotation.SIGHT_RADIUS; i <= y + PlayerAnnotation.SIGHT_RADIUS; i++) {
         jsonLine = new JSONArray();
          for (int j = x - PlayerAnnotation.SIGHT_RADIUS; j <= x + PlayerAnnotation.SIGHT_RADIUS; j++) {
              String symbol = String.valueOf('#');
              if (i > 0 && i < worldMap.length && j > 0 && j < worldMap[0].length) {
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
              ,"#...###..............#.....#.#".toCharArray()
              ,"#....................#.....#.#".toCharArray()
              ,"#######..............#.....#.#".toCharArray()
              ,"#.....#.........#########..#.#".toCharArray()
              ,"#.....#.........#..........#.#".toCharArray()
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
   }

}
