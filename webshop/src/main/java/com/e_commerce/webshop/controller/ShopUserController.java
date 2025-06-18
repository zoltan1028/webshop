package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.AuthUserDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
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
    IUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("login")
    @Transactional
    public ResponseEntity<String> LoginUser(@RequestBody AuthUserDTO dtoUser) {
        ShopUser shopUser = userRepository.findByUsername(dtoUser.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided username or password was not found."));
        if (!authenticationService.isPasswordValid(dtoUser.getPassword(), shopUser.getPassword())) {return ResponseEntity.badRequest().body("The provided username or password was not found.");}
        if (shopUser.getToken() != null) { return ResponseEntity.badRequest().body("User is already logged in.");}
        if (shopUser.getUserRight().equals(ShopUser.UserRight.ADMIN)) {
            shopUser.setToken(authenticationService.GenerateAdminToken());
        } else {
            shopUser.setToken(authenticationService.GenerateToken());
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("token", shopUser.getToken());
        node.put("userRight", shopUser.getUserRight().toString());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(node.toString());
    }
    @PostMapping("logout")
    @Transactional
    public ResponseEntity<String> Logout(@RequestBody String token) {
        ShopUser shopUser = userRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not found."));
        shopUser.setToken(null);
        return ResponseEntity.ok().build();
    }
    @PostMapping("register")
    @Transactional
    public ResponseEntity<String> Registration(@RequestBody AuthUserDTO newUser) {
        userRepository.findByUsername(newUser.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken."));

        ShopUser newShopUser = new ShopUser();
        newShopUser.setUsername(newUser.getUsername());
        newShopUser.setPassword(authenticationService.hashPassword(newUser.getPassword()));
        newShopUser.setUserRight(ShopUser.UserRight.USER);
        userRepository.save(newShopUser);
        return ResponseEntity.ok().build();
    }
}
