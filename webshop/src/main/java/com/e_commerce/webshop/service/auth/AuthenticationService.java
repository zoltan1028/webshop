package com.e_commerce.webshop.service.auth;
import com.e_commerce.webshop.dto.AuthUserDTO;
import com.e_commerce.webshop.dto.AuthUserLoginDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import org.hibernate.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthenticationService {
    @Autowired
    IUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final char[] CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private final Random RND = new Random();
    public String GenerateToken() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 6; i++)
            sb.append(CHARS[RND.nextInt(CHARS.length)]);
        return sb.toString();
    }
    public String GenerateAdminToken() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 7; i++)
            sb.append(CHARS[RND.nextInt(CHARS.length)]);
        return sb.toString();
    }
    public boolean isAdmin(String token) {
        return token.length() == 7;
    }
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    public boolean isPasswordValid(String rawPw, String hashedPassword) {
        return passwordEncoder.matches(rawPw, hashedPassword);
    }
    public void isUserNameTaken(AuthUserDTO userDTO) {
        userRepository.findByUsername(userDTO.getUsername())
                .ifPresent(u -> {
                    throw new IllegalStateException();
                });

    }
    public ShopUser getUserIfHasValidToken(String token) {
        return userRepository.findByToken(token).get();
    }
    public boolean isUserLoggedInWithToken(String token) {
        userRepository.findByToken(token).get();
        return true;
    }
    public void logout(String token) {
        ShopUser shopUser = getUserIfHasValidToken(token);
        shopUser.setToken(null);
    }
    public AuthUserLoginDTO login(AuthUserDTO dtoUser) {
        ShopUser shopUser = userRepository.findByUsername(dtoUser.getUsername()).get();
        if (!isPasswordValid(dtoUser.getPassword(), shopUser.getPassword())) {throw new BadCredentialsException("The provided username or password was not found.");}
        if (shopUser.getToken() != null) { throw new SessionException("User already logged in.");}
        if (shopUser.getUserRight().equals(ShopUser.UserRight.ADMIN)) {
            shopUser.setToken(GenerateAdminToken());
        } else {
            shopUser.setToken(GenerateToken());
        }
        AuthUserLoginDTO dto = new AuthUserLoginDTO();
        dto.setToken(shopUser.getToken());
        dto.setUserRight(shopUser.getUserRight().toString());
        return dto;
    }
}
