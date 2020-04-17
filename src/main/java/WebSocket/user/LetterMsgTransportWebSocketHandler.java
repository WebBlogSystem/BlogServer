package WebSocket.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.LetterMsg;
import domain.LetterNoRead;
import domain.ResultLetterMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import service.LetterMsgService;
import utils.SessionData;

@Component
public class LetterMsgTransportWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LetterMsgService letterMsgService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        try{
            LetterMsg letterMsg=mapper.readValue(msg, LetterMsg.class);
            if(!(letterMsg.getMsg().length()>=1&&letterMsg.getMsg().length()<=50)){
                ResultLetterMsg resultLetterMsg=new ResultLetterMsg();
                resultLetterMsg.setSend(false);
                resultLetterMsg.setRes("输入的信息不能为空并且长度不能超过50位");
                session.sendMessage(new TextMessage(mapper.writeValueAsString(resultLetterMsg)));
                return;
            }
            letterMsgService.addChatroomMsg(letterMsg);
            letterMsg = letterMsgService.queryLetterMsgByLetterMsgId(letterMsg.getId());
            WebSocketSession toSession = SessionData.getLettermsg_socket().get(letterMsg.getToUserId());
            ResultLetterMsg fromResultLetterMsg=new ResultLetterMsg();
            fromResultLetterMsg.setSend(true);
            fromResultLetterMsg.setRes(letterMsg);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(fromResultLetterMsg)));
            if(toSession!=null) {
                ResultLetterMsg resultLetterMsg=new ResultLetterMsg();
                resultLetterMsg.setSend(true);
                resultLetterMsg.setRes(letterMsg);
                toSession.sendMessage(new TextMessage(mapper.writeValueAsString(resultLetterMsg)));
            }
            if(SessionData.getNoRead_user_socket().get(letterMsg.getToUserId()) != null){
                int allnums=letterMsgService.queryNoReadLetterMsgByUserId(letterMsg.getToUserId());
                SessionData.getNoRead_user_socket().get(letterMsg.getToUserId()).sendMessage(new TextMessage(allnums+""));
            }
            if(SessionData.getNoRead_letter_socket().get(letterMsg.getLetterId()+"-"+letterMsg.getToUserId())!=null){
                int letterid=letterMsg.getLetterId();
                int userid=letterMsg.getToUserId();
                int nums=letterMsgService.queryNoReadLetterMsg(letterid,userid);
                LetterNoRead res=new LetterNoRead(letterid,nums,letterMsg);
                SessionData.getNoRead_letter_socket().get(letterMsg.getLetterId()+"-"+letterMsg.getToUserId()).sendMessage(new TextMessage(mapper.writeValueAsString(res)));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int userid=(Integer)session.getAttributes().get("userid");
        SessionData.getLettermsg_socket().put(userid,session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        int userid=(Integer)session.getAttributes().get("userid");
        SessionData.getLettermsg_socket().remove(userid);
    }
}
