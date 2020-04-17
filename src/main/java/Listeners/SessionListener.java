package Listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.LoginCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import utils.SessionData;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("会话建立.....");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("session 开始销毁");
        HttpSession session = httpSessionEvent.getSession();
        ObjectMapper mapper= (ObjectMapper) WebApplicationContextUtils.getRequiredWebApplicationContext(httpSessionEvent.getSession().getServletContext()).getBean("mapper");
        int flag=(Integer) session.getAttribute("flag");
        if(flag == 1){
            Map<Integer, HttpSession> user_session = SessionData.getUser_session();
            Iterator<Integer> iterator = user_session.keySet().iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (user_session.get(next) == session) {
                    WebSocketSession userLogin = SessionData.getUser_websocketSession().get(next);
                    LoginCheck loginCheck = new LoginCheck(false, "账号已退出");
                    if (userLogin != null) {
                        try {
                            userLogin.sendMessage(new TextMessage(mapper.writeValueAsString(loginCheck)));
                            userLogin.close();
                            SessionData.getUser_websocketSession().remove(next);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    user_session.remove(next);
                }
            }
        }
    }
}
