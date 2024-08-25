package com.e_commerce.webshop.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthenticationService {
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
    public String hashPassword(String password) {
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }
    public boolean isPasswordValid(String rawPw, String hashedPassword) {
        boolean isPasswordMatch = passwordEncoder.matches(rawPw, hashedPassword);
        return isPasswordMatch;
    }
}
