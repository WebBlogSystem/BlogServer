package Listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.LoginCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import utils.SessionData;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("会话建立.....");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("session 开始销毁");
        HttpSession session = httpSessionEvent.getSession();
        int flag=(Integer) session.getAttribute("flag");
        if(flag==2) {
            Map<Integer, HttpSession> admin_session = SessionData.getAdmin_session();
            Iterator<Integer> iterator = admin_session.keySet().iterator();
            ObjectMapper mapper= (ObjectMapper) WebApplicationContextUtils.getRequiredWebApplicationContext(httpSessionEvent.getSession().getServletContext()).getBean("mapper");
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (admin_session.get(next) == session) {
                    WebSocketSession adminLogin = SessionData.getAdmin_websocketsession().get(next);
                    LoginCheck loginCheck = new LoginCheck(false, "账号已退出");
                    if (adminLogin != null) {
                        try {
                            adminLogin.sendMessage(new TextMessage(mapper.writeValueAsString(loginCheck)));
                            adminLogin.close();
                            SessionData.getAdmin_websocketsession().remove(next);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    admin_session.remove(next);
                }
            }
        }
    }
}
