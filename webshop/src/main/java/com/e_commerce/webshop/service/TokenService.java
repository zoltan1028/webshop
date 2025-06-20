package com.e_commerce.webshop.service;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
@Service
public class TokenService {
    private WebSocketSession tcpWebsocketSession;
    @Autowired
    IUserRepository userRepository;
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void removeExpiredTokens() throws IOException {
        OffsetDateTime now = OffsetDateTime.now();
        List<ShopUser> users = userRepository.findAll();
        for (ShopUser user : users) {
            OffsetDateTime tokenExpire = user.getToken_expire();
            if (tokenExpire != null && tokenExpire.isBefore(now)) {
                user.setToken(null);
                user.setToken_expire(null);
                tcpWebsocketSession.sendMessage(new TextMessage("Token expired. Token was removed."));
            }
        }
    }
    public void setWebSocketSession(WebSocketSession session) {
        this.tcpWebsocketSession = session;
    }
}
