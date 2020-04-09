package Listeners;

import utils.SessionData;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
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
        int flag=(Integer) session.getAttribute("flag");
        if(flag == 1){
            Map<Integer, HttpSession> user_session = SessionData.getUser_session();
            Iterator<Integer> iterator = user_session.keySet().iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (user_session.get(next) == session) {
                    user_session.remove(next);
                }
            }
        }else if(flag==2) {
            Map<Integer, HttpSession> admin_session = SessionData.getAdmin_session();
            Iterator<Integer> iterator = admin_session.keySet().iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (admin_session.get(next) == session) {
                    admin_session.remove(next);
                }
            }
        }
    }
}
