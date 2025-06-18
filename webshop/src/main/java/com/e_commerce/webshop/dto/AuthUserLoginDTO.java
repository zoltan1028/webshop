package com.e_commerce.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserLoginDTO {
    private String token;
    private String userRight;
}
