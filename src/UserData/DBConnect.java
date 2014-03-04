package UserData;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.UUID;

/**
 * Created by razoriii on 25.02.14.
 */
public class DBConnect {
   private static String DB_URL = "jdbc:mysql://localhost:3306/mmorpg";
   private static String DB_USER = "root";
   private static String DB_PASS = "123456";

   public static double defaultPosX = 5;
   public static double defaultPosY = 5;

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

   public static String insertNewUser(String login, String password) throws ServletException {
      Connection con = null;
      String sid = "-1";
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         // need to generate game_id field
         PreparedStatement stmt = con.prepareStatement("INSERT INTO users (login, password, sid, pos_x, pos_y, game_id) " +
                 "VALUES (?,?,?,?,?,?)");
         stmt.setString(1, login);
         stmt.setString(2, getMD5(password));
         sid = UUID.randomUUID().toString();
         stmt.setString(3, sid);
         stmt.setDouble(4, defaultPosX);
         stmt.setDouble(5, defaultPosY);
         stmt.setInt(6, 1);
         stmt.executeUpdate();
      } catch (SQLException e) {
         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
         throw new ServletException("JDBC Driver not found.", e);
      }
      return sid;
   }

   public static boolean checkLoginExists(String login) throws ServletException {
      Connection con = null;
      boolean result = false;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE LOWER(login) = ?");
         stmt.setString(1, login.toLowerCase());
         ResultSet rs = stmt.executeQuery();
         int rowNum = 0;
         while (rs.next()) {
            rowNum++;
         }
         if (rowNum > 0) {
            result = true;
         }
      } catch (SQLException e) {
         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
         throw new ServletException("JDBC Driver not found.", e);
      }
      return result;
   }

   public static boolean doLogout(String sid) throws ServletException {
      Connection con = null;
      boolean result = false;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE sid = ?");
         stmt.setString(1, sid);
         ResultSet rs = stmt.executeQuery();
         int rowNum = 0;
         while (rs.next()) {
            rowNum++;
         }
         if (rowNum > 0) {
            result = true;
         }
         if (result) {
            stmt = con.prepareStatement("UPDATE users SET sid = ? WHERE sid = ?");
            sid = UUID.randomUUID().toString();
            stmt.setString(1, "");
            stmt.setString(2, sid);
         }
      } catch (SQLException e) {
         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
         throw new ServletException("JDBC Driver not found.", e);
      }

      return result;
   }

   public static String doLogin(String login, String password) throws ServletException {
      Connection con = null;
      boolean result = false;
      String sid = "-1";
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE LOWER(login) = ? AND password = ?");
         stmt.setString(1, login.toLowerCase());
         stmt.setString(2, getMD5(password));
         ResultSet rs = stmt.executeQuery();
         int rowNum = 0;
         while (rs.next()) {
            rowNum++;
         }
         if (rowNum > 0) {
            result = true;
         }
         if (result) {
            stmt = con.prepareStatement("UPDATE users SET sid = ? WHERE LOWER(login) = ?");
            sid = UUID.randomUUID().toString();
            stmt.setString(1, sid);
            stmt.setString(2, login.toLowerCase());
         }
      } catch (SQLException e) {
         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
         throw new ServletException("JDBC Driver not found.", e);
      }
      return sid;
   }

   public static PlayerDB getPlayerDBbySid(String sid) {
      PlayerDB playerDB = new PlayerDB();
      playerDB.sid = sid;
      Connection con = null;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = con.prepareStatement("SELECT login, game_id, pos_x, pos_y  FROM users WHERE sid = ?");
         stmt.setString(1, sid);
         ResultSet rs = stmt.executeQuery();
         int rowNum = 0;
         while (rs.next()) {
            rowNum++;
            playerDB.login = rs.getString("login");
            playerDB.id = rs.getInt("game_id");
            playerDB.posX = rs.getDouble("pos_x");
            playerDB.posY = rs.getDouble("pos_y");
         }
         if (rowNum > 0) {
            playerDB.badSid = false;
         }
      } catch (SQLException e) {
//         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
//         throw new ServletException("JDBC Driver not found.", e);
      }

      return playerDB;
   }

   public static void savePlayerDB(PlayerDB playerDB) {
      Connection con = null;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = con.prepareStatement("UPDATE users SET pos_x = ?, pos_y = ? WHERE sid = ?");
         stmt.setDouble(1, playerDB.posX);
         stmt.setDouble(2, playerDB.posY);
         stmt.setString(3, playerDB.sid);
         stmt.executeQuery();
      } catch (SQLException e) {
//         throw new ServletException("Servlet Could not display records.", e);
      } catch (ClassNotFoundException e) {
//         throw new ServletException("JDBC Driver not found.", e);
      }
   }

}
