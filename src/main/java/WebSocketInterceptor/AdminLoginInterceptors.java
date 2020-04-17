package WebSocketInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import utils.SessionData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Component
public class AdminLoginInterceptors implements HandshakeInterceptor {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpServletRequest req=((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
        int adminid=Integer.parseInt(req.getParameter("adminid"));
        if(SessionData.getAdmin_session().containsKey(adminid)){
            map.put("adminid",adminid);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
