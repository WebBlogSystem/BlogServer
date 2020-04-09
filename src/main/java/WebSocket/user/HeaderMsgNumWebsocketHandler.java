package WebSocket.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.UserNoRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import service.LetterMsgService;
import utils.SessionData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HeaderMsgNumWebsocketHandler extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LetterMsgService letterMsgService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int userId=(Integer)session.getAttributes().get("userid");
        int nums=letterMsgService.queryNoReadLetterMsgByUserId(userId);
        SessionData.getNoRead_user_socket().put(userId,session);
        try {
            session.sendMessage(new TextMessage(nums+""));
            System.out.println("HeaderMsgNumWebsocketHandler afterConnectionEstablished.....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("HeaderMsgNumWebsocketHandler afterConnectionClosed close.....");
        int userId=(Integer)session.getAttributes().get("userid");
        SessionData.getNoRead_user_socket().remove(userId);
    }
}
