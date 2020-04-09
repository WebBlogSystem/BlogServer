package utils;

import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

public class SessionData {
    // 绑定登录用户和登录session
    private static Map<Integer, HttpSession> user_session=new HashMap<Integer, HttpSession>();
    //绑定登录管理员与当前登录session
    public static Map<Integer, HttpSession> admin_session= new HashMap<Integer, HttpSession>();
    //绑定登录管理员与当前管理员登录校验的websocket的session
    public static  Map<Integer, WebSocketSession> admin_websocketsession=new HashMap<Integer, WebSocketSession>();
    // 绑定登录用户和websocketsession 登录成功则保存socketsession,踢出上次的session.
    private static Map<Integer, WebSocketSession> user_websocketSession=new HashMap<>();
    //绑定用户和socket,用于显示当前用户的所有未读数
    private static Map<Integer,WebSocketSession> noRead_user_socket=new HashMap<>();
    //绑定letter和socket用于显示letter未读消息数
    private static Map<String,WebSocketSession> noRead_letter_socket=new HashMap<>();
    //用于消息来往记录
    private static Map<Integer,WebSocketSession> lettermsg_socket=new HashMap<>();

    public static Map<Integer, WebSocketSession> getNoRead_user_socket() {
        return noRead_user_socket;
    }

    public static Map<String, WebSocketSession> getNoRead_letter_socket() {
        return noRead_letter_socket;
    }

    public static Map<Integer, WebSocketSession> getLettermsg_socket() {
        return lettermsg_socket;
    }

    public static Map<Integer, WebSocketSession> getUser_websocketSession() {
        return user_websocketSession;
    }

    public static Map<Integer, HttpSession> getUser_session() {
        return user_session;
    }

    public static Map<Integer, HttpSession> getAdmin_session() {
        return admin_session;
    }

    public static Map<Integer, WebSocketSession> getAdmin_websocketsession() {
        return admin_websocketsession;
    }
}
