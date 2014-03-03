package UserData;

/**
 * Created by Alexander on 3/3/14.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/game")
public class Game {

    private static final Logger LOGGER =
            Logger.getLogger(Game.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.log(Level.INFO, "New connection with client: {0}",
                session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        LOGGER.log(Level.INFO, "New message from Client [{0}]: {1}",
                new Object[] {session.getId(), message});
        return "Server received [" + message + "]";
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close connection for client: {0}",
                session.getId());
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}

