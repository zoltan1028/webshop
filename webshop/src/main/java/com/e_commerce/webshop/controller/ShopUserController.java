package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.UserDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
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
    public ResponseEntity<String> LoginUser(@RequestBody UserDTO dtoUser) {
        Optional<ShopUser> user = userRepository.findByUsername(dtoUser.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("The provided username or password was not found.");
        }
        ShopUser shopUser = user.get();
        if (!authenticationService.isPasswordValid(dtoUser.getPassword(), shopUser.getPassword())) {
            return ResponseEntity.badRequest().body("The provided username or password was not found.");
        }
        if (shopUser.getAdmin().equals("admin")) {
            shopUser.setToken(authenticationService.GenerateAdminToken());
        } else {
            shopUser.setToken(authenticationService.GenerateToken());
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("token", shopUser.getToken());
        node.put("userRight", shopUser.getAdmin());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(node.toString());
    }
    @PostMapping("logout")
    @Transactional
    public ResponseEntity<String> Logout(@RequestBody String token) {
        Optional<ShopUser> user = userRepository.findByToken(token);
        if (user.isEmpty()) {return ResponseEntity.badRequest().body("");}
        ShopUser shopUser = user.get();
        shopUser.setToken(null);
        return ResponseEntity.ok(null);
    }
    @PostMapping("register")
    public ResponseEntity<String> Registration(@RequestBody UserDTO newUser) {
        Optional<ShopUser> shopUserOptional = userRepository.findByUsername(newUser.getUsername());
        if (shopUserOptional.isPresent()) {return ResponseEntity.badRequest().body("Username is already taken.");}
        ShopUser newShopUser = new ShopUser();
        newShopUser.setUsername(newUser.getUsername());
        newShopUser.setPassword(authenticationService.hashPassword(newUser.getPassword()));
        newShopUser.setAdmin("user");
        userRepository.save(newShopUser);
        return ResponseEntity.ok().build();
    }
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void removeTokenFromUsers() {
        //find all w token
        List<ShopUser> shopUsers = userRepository.findAll();
        for (ShopUser user: shopUsers) {
            Duration duration;
            if (user.getToken_expire() != null) {
                duration = Duration.between(user.getToken_expire(), OffsetDateTime.now());
                if (duration.getSeconds() > 0) {
                    user.setToken(null);
                    user.setToken_expire(null);
                }
            }
        }
    }
    @GetMapping("getTokenStatus")
    public ResponseEntity<String> getTokenStatus(@RequestHeader String token) {
        Optional<ShopUser> userWithToken = userRepository.findByToken(token);
        if(userWithToken.isEmpty()) {
            return ResponseEntity.ok("tokenIsInvalid");
        } else {
            return ResponseEntity.ok("tokenIsValid");
        }
    }
}
