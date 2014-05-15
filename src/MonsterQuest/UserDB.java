package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */

import javax.websocket.Session;
import java.sql.*;
import java.util.UUID;

public class UserDB {

   private static final double defaultPosX = 5;
   private static final double defaultPosY = 5;
   private static final int defaultMaxHp = 1000;

   public static String getMD5(String md5) {
      try {
         java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
         byte[] array = md.digest(md5.getBytes());
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
         }
         return sb.toString();
      } catch (java.security.NoSuchAlgorithmException e) {
      }
      return null;
   }

   public static String doInsert(String login, String password) {
      String sid = "-1";
      try {
         Connection connector = DBInfo.createConnection();
         // TODO need to generate game_id field
         PreparedStatement stmt = connector.prepareStatement("INSERT INTO users (login, password, sid, pos_x, pos_y, hp, exp) " +
                 "VALUES (?,?,?,?,?,?,0)");
         stmt.setString(1, login);
         stmt.setString(2, getMD5(password));
         sid = UUID.randomUUID().toString();
         stmt.setString(3, sid);
         stmt.setDouble(4, defaultPosX);
         stmt.setDouble(5, defaultPosY);
         stmt.setDouble(6, defaultMaxHp);
         stmt.executeUpdate();
         connector.close();
      } catch (Throwable e) {

      }
      return sid;
   }

   private static int getResultSetRowCount(ResultSet rs) {
      int rowNum = 0;
      try {
         while (rs.next()) {
            rowNum++;
         }
      } catch (Throwable e) {

      }
      return rowNum;
   }

   public static boolean checkLoginExists(String newLogin) {
      boolean result = false;
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM users WHERE LOWER(login) = ?");
         stmt.setString(1, newLogin.toLowerCase());
         ResultSet rs = stmt.executeQuery();
         result = (getResultSetRowCount(rs) > 0);
         connector.close();
      } catch (Throwable e) {

      }
      return result;
   }

   public static String doLogin(String login, String password) {
      String sid = "-1";
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM users WHERE LOWER(login) = ? AND password = ?");
         stmt.setString(1, login.toLowerCase());
         stmt.setString(2, getMD5(password));
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            stmt = connector.prepareStatement("UPDATE users SET sid = ? WHERE LOWER(login) = ?");
            sid = UUID.randomUUID().toString();
            stmt.setString(1, sid);
            stmt.setString(2, login.toLowerCase());
            stmt.executeUpdate();
         }
         connector.close();
      } catch (Throwable e) {

      }
      return sid;
   }

   public static Player getPlayerBySid(String sid, Session session) {
      Player result = null;
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT login, pos_x, pos_y, hp, exp FROM users WHERE sid = ?");
         stmt.setString(1, sid);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            result = new Player(
                    Game.getPlayerIdBySid(sid),
                    sid,
                    rs.getString("login"),
                    rs.getInt("hp"),
                    defaultMaxHp,
                    rs.getInt("exp"),
                    session,
                    new Location(rs.getDouble("pos_x"), rs.getDouble("pos_y"))
            );
         }
         connector.close();
      } catch (Throwable e) {

      }
      return result;
   }

   public static void saveGameData(Player player) {
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("UPDATE users SET pos_x = ?, pos_y = ?, hp = ?, exp = ? WHERE sid = ?");
         stmt.setDouble(1, player.getLocation().x);
         stmt.setDouble(2, player.getLocation().y);
         stmt.setInt(3, player.getHp());
         stmt.setInt(4, player.getLevel().getExp());
         stmt.setString(5, player.getSid());
         stmt.executeUpdate();
         connector.close();
      } catch (Throwable e) {

      }
   }

   public static boolean doLogout(String sid) {
      boolean result = false;
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM users WHERE sid = ?");
         stmt.setString(1, sid);
         ResultSet rs = stmt.executeQuery();
         result = (getResultSetRowCount(rs) > 0);
         if (result) {
            stmt = connector.prepareStatement("UPDATE users SET sid = ? WHERE sid = ?");
            stmt.setString(1, "-1");
            stmt.setString(2, sid);
            stmt.executeUpdate();
         }
         connector.close();
      } catch (Throwable e) {

      }

      return result;
   }

}
