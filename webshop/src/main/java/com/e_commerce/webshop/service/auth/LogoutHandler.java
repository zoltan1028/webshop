package com.e_commerce.webshop.service.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class LogoutHandler extends TextWebSocketHandler {
    private final TokenService tokenService;
    @Autowired
    public LogoutHandler(TokenService tokenService) {
        this.tokenService = tokenService;
        System.out.println("NotificationWebSocketHandler bean created");
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to client: " + session.getId());
        tokenService.setWebSocketSession(session);  // Set the session in the service
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Session closed: " + session.getId());
        tokenService.setWebSocketSession(null);  // Clear the session
    }
}
