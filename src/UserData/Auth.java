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

      String login = (String) jsonRequest.get("login");
      String password = (String) jsonRequest.get("password");
      String action = (String) jsonRequest.get("action");
      String logout_sid = (String) jsonRequest.get("sid");

      JSONObject jsonResponse = new JSONObject();

      jsonResponse.put("action", action);

      switch (action) {
         case "register": {
            String message = "ok";

            if (!validateLogin(login)) {
               message = "badLogin";
            } else if (!validatePassword(password)) {
               message = "badPassword";
            } else if (loginExists(login)) {
               message = "loginExists";
            } else {
               jsonResponse.put("sid", DBConnect.insertNewUser(login, password));
            }

            jsonResponse.put("result", message);
         }
         case "login": {
            String sid = "-1";
            if (!login.isEmpty() && !password.isEmpty()) {
               sid = DBConnect.doLogin(login, password);
            }
            if (sid.equals("-1")) {
               jsonResponse.put("result", "invalidCredentials");
            } else {
               jsonResponse.put("result", "ok");
               jsonResponse.put("sid", sid);
            }
         }

         case "logout": {
            jsonResponse.put("result", "badSid");
            if (DBConnect.doLogout(logout_sid)) {
               jsonResponse.put("result", "ok");
            }
         }

         default: {
            jsonResponse.put("result", "error");
         }
      }

      PrintWriter printout = response.getWriter();

      printout.print(jsonResponse);
      printout.flush();
   }

}
