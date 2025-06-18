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
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
@Service
public class TokenService {
    private WebSocketSession tcpWebsocketSession;
    @Autowired
    IUserRepository userRepository;
    //removes token when expired and notifies frontend
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void removeTokenFromUsers() throws IOException {
        List<ShopUser> shopUsers = userRepository.findAll();
        for (ShopUser user: shopUsers) {
            Duration duration;
            if (user.getToken_expire() != null) {
                duration = Duration.between(user.getToken_expire(), OffsetDateTime.now());
                if (duration.getSeconds() > 0) {
                    user.setToken(null);
                    user.setToken_expire(null);
                    tcpWebsocketSession.sendMessage(new TextMessage("Conditional notification: Sent because condition is true"));
                }
            }
        }
    }
    public void setWebSocketSession(WebSocketSession session) {
        this.tcpWebsocketSession = session;
    }
}
