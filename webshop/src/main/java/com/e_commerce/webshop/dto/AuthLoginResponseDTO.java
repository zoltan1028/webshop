package com.e_commerce.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponseDTO {
    private String token;
    private String userRight;
}
