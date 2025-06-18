package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.AuthUserDTO;
import com.e_commerce.webshop.dto.AuthUserLoginDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.APIService.ShopUserService;
import com.e_commerce.webshop.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class ShopUserController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ShopUserService shopUserService;

    @PostMapping("login")
    @Transactional
    public ResponseEntity<AuthUserLoginDTO> LoginUser(@RequestBody AuthUserDTO dtoUser) {
        AuthUserLoginDTO authObject = authenticationService.login(dtoUser);
        return ResponseEntity.ok().body(authObject);
    }
    @PostMapping("logout")
    @Transactional
    public ResponseEntity<String> Logout(@RequestBody String token) {
        authenticationService.logout(token);
        return ResponseEntity.ok().build();
    }
    @PostMapping("register")
    @Transactional
    public ResponseEntity<String> Registration(@RequestBody AuthUserDTO newUser) {
        authenticationService.isUserNameTaken(newUser);
        shopUserService.saveUser(newUser);
        return ResponseEntity.ok().body("Registration was successful.");
    }
}
