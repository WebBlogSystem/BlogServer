package WebSocketInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.LoginCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;
import utils.SessionData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Component
public class UserLoginInterceptors implements HandshakeInterceptor {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpServletRequest req=((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
        int userid=Integer.parseInt(req.getParameter("userid"));
        if(SessionData.getUser_session().containsKey(userid)){
            map.put("userid",userid);
        }else{
            return false;
        }
        if(req.getParameter("letterid")!=null){
            Integer letterid=Integer.parseInt(req.getParameter("letterid"));
            map.put("letterid",letterid);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
