package com.e_commerce.webshop.service;

import org.springframework.stereotype.Service;

@Service
public class PayPalGatewayService {
    public boolean isPaymentSuccessful(String tokenData) {
        return tokenData != null;
    }
}
