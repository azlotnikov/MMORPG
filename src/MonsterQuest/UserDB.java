package MonsterQuest;

/**
 * Created by razoriii on 04.03.14.
 */

import java.sql.*;
import java.util.UUID;

public class UserDB {

   private static final double defaultPosX = 5;
   private static final double defaultPosY = 5;

   private String login = "";
   private String sid = "-1";
   private String passwordHash = "";
   private double posX = defaultPosX;
   private double posY = defaultPosY;
   private boolean badSid = true;

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

   public void doInsert() {
      sid = "-1";
      try {
         Connection connector = DBInfo.createConnection();
         // TODO need to generate game_id field
         PreparedStatement stmt = connector.prepareStatement("INSERT INTO users (login, password, sid, pos_x, pos_y) " +
                 "VALUES (?,?,?,?,?)");
         stmt.setString(1, login);
         stmt.setString(2, passwordHash);
         sid = UUID.randomUUID().toString();
         stmt.setString(3, sid);
         stmt.setDouble(4, posX);
         stmt.setDouble(5, posY);
         stmt.executeUpdate();
         connector.close();
      } catch (Throwable e) {

      }
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

   public boolean checkLoginExists(String newLogin) {
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

   public boolean doLogin() {
      boolean result = false;
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT * FROM users WHERE LOWER(login) = ? AND password = ?");
         stmt.setString(1, login.toLowerCase());
         stmt.setString(2, passwordHash);
         ResultSet rs = stmt.executeQuery();
         result = rs.next();
         if (result) {
            stmt = connector.prepareStatement("UPDATE users SET sid = ? WHERE LOWER(login) = ?");
            sid = UUID.randomUUID().toString();
            stmt.setString(1, sid);
            stmt.setString(2, login.toLowerCase());
            stmt.executeUpdate();
         }
         connector.close();
      } catch (Throwable e) {

      }
      return result;
   }

   public boolean doLogout() {
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

   public void getDataBySid(String newSid) {
      sid = newSid;
      badSid = true;
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("SELECT login, pos_x, pos_y  FROM users WHERE sid = ?");
         stmt.setString(1, sid);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            login = rs.getString("login");
            posX = rs.getDouble("pos_x");
            posY = rs.getDouble("pos_y");
            badSid = false;
         }
         connector.close();
      } catch (Throwable e) {

      }
   }

   public void saveGameData() {
      try {
         Connection connector = DBInfo.createConnection();
         PreparedStatement stmt = connector.prepareStatement("UPDATE users SET pos_x = ?, pos_y = ? WHERE sid = ?");
         stmt.setDouble(1, posX);
         stmt.setDouble(2, posY);
         stmt.setString(3, sid);
         stmt.executeUpdate();
         connector.close();
      } catch (Throwable e) {

      }
   }

   public void setPasswordMD5(String plainPassword) {
      passwordHash = getMD5(plainPassword);
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public void setSid(String sid) {
      this.sid = sid;
   }

   public void setPosX(long posX) {
      this.posX = posX;
   }

   public void setPosY(long posY) {
      this.posY = posY;
   }

   public String getLogin() {
      return login;
   }

   public String getSid() {
      return sid;
   }

   public boolean isBadSid() {
      return badSid;
   }

   public Location getLocation() {
      return new Location(posX, posY);
   }

   public void setLocation(Location location) {
      this.posX = location.x;
      this.posY = location.y;
   }
}
