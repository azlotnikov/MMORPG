package UserData;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Created by razoriii on 25.02.14.
 */
public class DBConnect {
    private static String DB_URL = "jdbc:mysql://localhost:3306/mmorpg";
    private static String DB_USER = "root";
    private static String DB_PASS = "123456";

    public static int insertNewUser(String login, String password) throws ServletException {
        Connection con = null;
        int result = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO users (login, password, sid) VALUES (?,?,?)");
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, "1234567");
            result = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        } finally {
        }

        return result;
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
}
