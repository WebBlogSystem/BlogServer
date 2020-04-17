package WebSocket.admin;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import utils.SessionData;

import java.util.Map;
@Component
public class AdminLoginCheckWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int adminid=(Integer) session.getAttributes().get("adminid");
        SessionData.getAdmin_websocketsession().put(adminid,session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        int adminid=(Integer) session.getAttributes().get("adminid");
        SessionData.getAdmin_websocketsession().remove(adminid);
    }
}
