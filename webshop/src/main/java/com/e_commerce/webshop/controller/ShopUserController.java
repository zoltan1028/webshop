package com.e_commerce.webshop.controller;
import com.e_commerce.webshop.dto.AuthUserDTO;
import com.e_commerce.webshop.dto.AuthUserLoginDTO;
import com.e_commerce.webshop.service.APIService.ShopUserService;
import com.e_commerce.webshop.service.AuthenticationService;
import jakarta.transaction.Transactional;
import org.hibernate.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.NoSuchElementException;

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
        AuthUserLoginDTO authObject;
        try {
            authObject = authenticationService.login(dtoUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided user was not found.");
        } catch (BadCredentialsException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password.");
        } catch (SessionException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "User already logged in.");
        }
        return ResponseEntity.ok().body(authObject);
    }
    @PostMapping("logout")
    @Transactional
    public ResponseEntity<String> Logout(@RequestBody String token) {
        try {
            authenticationService.logout(token);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not logged in.");
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("register")
    @Transactional
    public ResponseEntity<String> Registration(@RequestBody AuthUserDTO newUser) {
        try {
            authenticationService.isUserNameTaken(newUser);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        shopUserService.saveUser(newUser);
        return ResponseEntity.ok().body("Registration was successful.");
    }
}
