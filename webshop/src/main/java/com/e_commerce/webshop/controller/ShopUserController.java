package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class ShopUserController {
    @PostMapping("login")
    public ResponseEntity<String> LoginUser(@RequestBody UserDTO dtoUser) {
        String username = "test";
        String password = "test";
        if (!(username.equals(dtoUser.getUsername()) && password.equals(dtoUser.getPassword()))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("tokenxyz");
    }
}
