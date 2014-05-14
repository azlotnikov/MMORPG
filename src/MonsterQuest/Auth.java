package MonsterQuest;

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
import java.net.InetAddress;


public class Auth {

   // https://github.com/rotanov/fefu-mmorpg-protocol

   public static boolean validateLogin(String login) {
      return !(login.length() < 2 || !login.matches("\\w+") || login.length() > 36);
   }

   public static boolean validatePassword(String pass) {
      return !(pass.length() < 6 || pass.length() > 36);
   }

   public static boolean loginExists(String login) throws ServletException {
      UserDB user = new UserDB();
      return user.checkLoginExists(login);
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
               UserDB.doInsert(login, password);
//               jsonResponse.put("sid", user.getSid());
//               jsonResponse.put("webSocket", webSocketUrl);
//               jsonResponse.put("id", user.getId());
            }

            jsonResponse.put("result", message);
            break;
         }
         case "login": {
            if (!login.isEmpty() && !password.isEmpty()) {
               String sid = UserDB.doLogin(login, password);
               if (!sid.equals("-1")) {
                  long newId = Game.getNextGlobalId();
                  Game.setPlayerIdBySid(sid, newId);
                  jsonResponse.put("result", "ok");
                  jsonResponse.put("sid", sid);
                  jsonResponse.put("webSocket", "ws://" + InetAddress.getLocalHost().getHostAddress() + ":8080/MMORPG_war_exploded/game");
                  jsonResponse.put("id", newId);
               } else {
                  jsonResponse.put("result", "invalidCredentials");
               }
            } else {
               jsonResponse.put("result", "invalidCredentials");
            }
            break;
         }

         case "logout": {
            jsonResponse.put("result", "badSid");
            if (UserDB.doLogout(logout_sid)) {
               jsonResponse.put("result", "ok");
            }
            break;
         }

         default: {
            jsonResponse.put("result", "error");
            break;
         }
      }

      PrintWriter printout = response.getWriter();

      printout.print(jsonResponse);
      printout.flush();
   }

}
