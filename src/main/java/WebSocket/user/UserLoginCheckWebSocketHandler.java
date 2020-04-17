package WebSocket.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CurrentUser;
import domain.LoginCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import utils.SessionData;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class UserLoginCheckWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int userid=(Integer) session.getAttributes().get("userid");
        SessionData.getUser_websocketSession().put(userid,session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        int userid=(Integer) session.getAttributes().get("userid");
        SessionData.getUser_websocketSession().remove(userid);
    }
}
