package WebSocket;

import WebSocket.admin.AdminLoginCheckWebSocketHandler;
import WebSocket.user.HeaderMsgNumWebsocketHandler;
import WebSocket.user.LetterMsgNumWebSocketHandler;
import WebSocket.user.LetterMsgTransportWebSocketHandler;
import WebSocket.user.UserLoginCheckWebSocketHandler;
import WebSocketInterceptor.AdminLoginInterceptors;
import WebSocketInterceptor.UserLoginInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Component
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private HeaderMsgNumWebsocketHandler headerMsgNumWebsocketHandler;
    @Autowired
    private LetterMsgNumWebSocketHandler letterMsgNumWebSocketHandler;
    @Autowired
    private LetterMsgTransportWebSocketHandler letterMsgTransportWebSocketHandler;
    @Autowired
    private UserLoginCheckWebSocketHandler userLoginCheckWebSocketHandler;
    @Autowired
    private UserLoginInterceptors userLoginInterceptors;
    @Autowired
    private AdminLoginCheckWebSocketHandler adminLoginCheckWebSocketHandler;
    @Autowired
    private AdminLoginInterceptors adminLoginInterceptors;
//    @Bean
//    public HeaderMsgNumWebsocketHandler getHeaderMsgNumWebsocketHandler() {
//        return new HeaderMsgNumWebsocketHandler();
//    }
//    @Bean
//    public LetterMsgNumWebSocketHandler getLetterMsgNumWebSocketHandler() {
//        return new LetterMsgNumWebSocketHandler();
//    }
//    @Bean
//    public LetterMsgTransportWebSocketHandler getLetterMsgTransportWebSocketHandler() {
//        return new LetterMsgTransportWebSocketHandler();
//    }
//    @Bean
//    public LoginCheckWebSocketHandler getLoginCheckWebSocketHandler() {
//        return new LoginCheckWebSocketHandler();
//    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println(headerMsgNumWebsocketHandler == null);
        registry.addHandler(headerMsgNumWebsocketHandler,"/getHeaderNums").setAllowedOrigins("*").addInterceptors(userLoginInterceptors);
        registry.addHandler(letterMsgNumWebSocketHandler,"/getLetterMsgNums").setAllowedOrigins("*").addInterceptors(userLoginInterceptors);
        registry.addHandler(letterMsgTransportWebSocketHandler,"/sendLetterMsg").setAllowedOrigins("*").addInterceptors(userLoginInterceptors);
        registry.addHandler(userLoginCheckWebSocketHandler,"/loginCheck").setAllowedOrigins("*").addInterceptors(userLoginInterceptors);
        registry.addHandler(adminLoginCheckWebSocketHandler, "/adminLoginCheck").setAllowedOrigins("*").addInterceptors(adminLoginInterceptors);
    }
}
