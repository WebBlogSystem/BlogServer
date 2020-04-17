package WebSocket.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.LetterMsg;
import domain.LetterNoRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import service.LetterMsgService;
import utils.SessionData;

import java.io.IOException;
@Component
public class LetterMsgNumWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private LetterMsgService letterMsgService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int letterid=(Integer)session.getAttributes().get("letterid");
        int userid=(Integer)session.getAttributes().get("userid");
        String letter_user=""+letterid+"-"+userid;
        SessionData.getNoRead_letter_socket().put(letter_user,session);
        int nums=letterMsgService.queryNoReadLetterMsg(letterid,userid);
        LetterMsg letterMsg=letterMsgService.queryLatestLetterMsgByLetterId(letterid);
        LetterNoRead res=new LetterNoRead(letterid,nums,letterMsg);
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(res)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("LetterMsgNumWebSocketHandler close...");
        int letterid=(Integer)session.getAttributes().get("letterid");
        int userid=(Integer)session.getAttributes().get("userid");
        String letter_user=""+letterid+"-"+userid;
        SessionData.getNoRead_letter_socket().remove(letter_user);
        session.getAttributes().remove("letterid");
    }
}
