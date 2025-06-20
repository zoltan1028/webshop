package com.e_commerce.webshop.service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
//placeholder class for payment gateway implementation
@Service
public class PayPalGatewayService {
    public void processGooglePayRequest(String googlePayToken) {
        if (googlePayToken == null) {
            throw new BadCredentialsException("Invalid token.");
        }
    }
}