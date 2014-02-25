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

    public static String getMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String insertNewUser(String login, String password) throws ServletException {
        Connection con = null;
        String sid = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO users (login, password, sid) VALUES (?,?,?)");
            stmt.setString(1, login);
            stmt.setString(2, getMD5(password));
            sid = UUID.randomUUID().toString();
            stmt.setString(3, sid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        } finally {
        }

        return sid;
    }

    public static boolean checkLoginExists(String login) throws ServletException {
        Connection con = null;
        boolean result = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs  = stmt.executeQuery();
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
        } finally {
        }

        return result;
    }

    public static String doLogin(String login, String password) throws ServletException {
        Connection con = null;
        boolean result = false;
        String sid = "0";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?");
            stmt.setString(1, login);
            stmt.setString(2, getMD5(password));
            ResultSet rs  = stmt.executeQuery();
            int rowNum = 0;
            while (rs.next()) {
                rowNum++;
            }
            if (rowNum > 0) {
                result = true;
            }
            if (result) {
                stmt = con.prepareStatement("UPDATE users SET sid = ? WHERE login = ?");
                sid = UUID.randomUUID().toString();
                stmt.setString(1, sid);
                stmt.setString(2, login);
            }
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        } finally {
        }

        return sid;
    }
}
