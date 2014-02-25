package UserData;

/**
 * Created by razoriii on 25.02.14.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import org.json.simple.JSONObject;

import java.io.PrintWriter;


public class User {
    public static boolean validateLogin(String login) {
        if (login.length() < 6) return false;
        return true;
    }

    public static boolean validatePassword(String pass) {
        if (pass.length() < 6) return false;
        return true;
    }

    public static boolean loginExists(String login) throws ServletException {
        return DBConnect.checkLoginExists(login);
    }

    public static void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String action = request.getParameter("action");

        PrintWriter printout = response.getWriter();

        JSONObject JObject = new JSONObject();

        if (action.equals("doRegistration")) {
            String message = "ok";

            if (!validateLogin(login)) {
                message = "bad login";
            }

            if (!validatePassword(password)) {
                message = "bad password";
            }

            if (loginExists(login)) {
                message = "login exists";
            }

            if (message.equals("ok")) {
                DBConnect.insertNewUser(login, password);
            }

            JObject.put("result", message);
        } else if (action.equals("doLogin")) {

        } else {
            JObject.put("result", "error");
        }

        printout.print(JObject);
        printout.flush();
    }

}
