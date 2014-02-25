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
    //protocol consts
    public static String _action_login = "login";
    public static String _action_registration = "registr";
    public static String _param_action = "action";
    public static String _param_login = "login";
    public static String _param_password = "password";
    public static String _param_sid = "sid";
    public static String _param_result = "result";
    public static String _message_ok = "ok";
    public static String _message_wrong_pass = "wrong password";
    public static String _message_bad_pass = "bad password";
    public static String _message_bad_login = "bad login";
    public static String _message_login_exists = "login exists";
    public static String _message_error = "error";

    public static boolean validateLogin(String login) {
        if (login.length() < 3 || !login.matches("\\w+")) return false;
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

        String login = request.getParameter(_param_login);
        String password = request.getParameter(_param_password);
        String action = request.getParameter(_param_action);

        JSONObject JObject = new JSONObject();

        JObject.put(_param_action, action);

        if (action.equals(_action_registration)) {
            String message = _message_ok;

            if (!validateLogin(login)) {
                message = _message_bad_login;
            }

            if (!validatePassword(password)) {
                message = _message_bad_pass;
            }

            if (loginExists(login)) {
                message = _message_login_exists;
            }

            if (message.equals(_message_ok)) {
                JObject.put(_param_sid, DBConnect.insertNewUser(login, password));
            }

            JObject.put(_param_result, message);
        } else if (action.equals(_action_login)) {
            String sid = DBConnect.doLogin(login, password);
            if (sid.equals("-1")) {
                JObject.put(_param_result, _message_wrong_pass);
            } else {
                JObject.put(_param_result, _message_ok);
                JObject.put(_param_sid, sid);
            }
        } else {
            JObject.put(_param_result, _message_error);
        }

        PrintWriter printout = response.getWriter();

        printout.print(JObject);
        printout.flush();
    }

}
