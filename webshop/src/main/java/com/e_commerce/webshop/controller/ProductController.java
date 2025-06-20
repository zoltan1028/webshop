package com.e_commerce.webshop.controller;
import com.e_commerce.webshop.dto.MainPageProductDTO;
import com.e_commerce.webshop.dto.NewProductDTO;
import com.e_commerce.webshop.service.APIService.ProductService;
import com.e_commerce.webshop.service.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/products/")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("getProducts")
    public ResponseEntity<Page<MainPageProductDTO>> getProductsByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getMainPage(page, size));
    }
    @PostMapping("newProduct")
    @Transactional
    public ResponseEntity<String> saveNewProductToDataBase(@RequestBody NewProductDTO productDTO, @RequestHeader String token) {
        try {
            authenticationService.isUserLoggedInWithToken(token) ;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        authenticationService.isAdmin(token);
        productService.saveNewProduct(productDTO);
        return ResponseEntity.ok("New product was registered.");
    }
}
