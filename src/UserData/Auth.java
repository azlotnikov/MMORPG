package UserData;

/**
 * Created by razoriii on 25.02.14.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.PrintWriter;


public class Auth {

   // https://github.com/rotanov/fefu-mmorpg-protocol

   public static String _action_login = "login";
   public static String _action_registration = "register";
   public static String _action_logout = "logout";

   public static String _param_action = "action";
   public static String _param_login = "login";
   public static String _param_password = "password";
   public static String _param_sid = "sid";
   public static String _param_result = "result";

   public static String _message_ok = "ok";
   public static String _message_wrong_pass = "invalidCredentials";
   public static String _message_bad_pass = "badPassword";
   public static String _message_bad_login = "badLogin";
   public static String _message_login_exists = "loginExists";
   public static String _message_error = "error";
   public static String _message_bad_sid = "badSid";

   public static boolean validateLogin(String login) {
      return !(login.length() < 2 || !login.matches("\\w+") || login.length() > 36);
   }

   public static boolean validatePassword(String pass) {
      return !(pass.length() < 6 || pass.length() > 36);
   }

   public static boolean loginExists(String login) throws ServletException {
      return DBConnect.checkLoginExists(login);
   }

   public static void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
      response.setContentType("application/json; charset=UTF-8");

      StringBuffer jb = new StringBuffer();
      String line = null;
      try {
         BufferedReader reader = request.getReader();
         while ((line = reader.readLine()) != null)
            jb.append(line);
      } catch (Exception e) { /*report an error*/ }

      JSONObject jsonRequest;

      try {
         JSONParser jsonParser = new JSONParser();
         jsonRequest = (JSONObject) jsonParser.parse(jb.toString());
      } catch (ParseException e) {
         // crash and burn
         throw new IOException("Error parsing JSON request string");
      }

      String login = (String) jsonRequest.get(_param_login);
      String password = (String) jsonRequest.get(_param_password);
      String action = (String) jsonRequest.get(_param_action);
      String logout_sid = (String) jsonRequest.get(_param_sid);

      JSONObject jsonResponse = new JSONObject();

      jsonResponse.put(_param_action, action);

      if (action.equals(_action_registration)) {
         String message = _message_ok;

         if (!validateLogin(login)) {
            message = _message_bad_login;
         } else if (!validatePassword(password)) {
            message = _message_bad_pass;
         } else if (loginExists(login)) {
            message = _message_login_exists;
         } else {
            jsonResponse.put(_param_sid, DBConnect.insertNewUser(login, password));
         }

         jsonResponse.put(_param_result, message);
      } else if (action.equals(_action_login)) {
         String sid = "-1";
         if (!login.isEmpty() && !password.isEmpty()) {
            sid = DBConnect.doLogin(login, password);
         }
         if (sid.equals("-1")) {
            jsonResponse.put(_param_result, _message_wrong_pass);
         } else {
            jsonResponse.put(_param_result, _message_ok);
            jsonResponse.put(_param_sid, sid);
         }

      } else if (action.equals(_action_logout)) {
         jsonResponse.put(_param_result, _message_bad_sid);
         if (DBConnect.doLogout(logout_sid)) {
            jsonResponse.put(_param_result, _message_ok);
         }
      } else {
         jsonResponse.put(_param_result, _message_error);
      }

      PrintWriter printout = response.getWriter();

      printout.print(jsonResponse);
      printout.flush();
   }

}
