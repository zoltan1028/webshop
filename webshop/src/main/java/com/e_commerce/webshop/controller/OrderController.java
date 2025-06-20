package com.e_commerce.webshop.controller;
import com.e_commerce.webshop.dto.PaymentRequestDTO;
import com.e_commerce.webshop.dto.ordersbyuser.OrdersByUserUserDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IShopOrderRepository;
import com.e_commerce.webshop.service.AuthenticationService;
import com.e_commerce.webshop.service.PayPalGatewayService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/orders/")
public class OrderController {
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    PayPalGatewayService payPalGatewayService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OrderService orderService;

    @GetMapping("getOrdersOfUser")
    public ResponseEntity<OrdersByUserUserDTO> getOrdersOfUserByToken(@RequestHeader String Token) {
        ShopUser shopUser;
        try {
            shopUser = authenticationService.getUserIfHasValidToken(Token);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        OrdersByUserUserDTO userDTO = new OrdersByUserUserDTO(shopUser);
        return  ResponseEntity.ok(userDTO);
    }
    @PostMapping("submitOrder")
    @Transactional
    public ResponseEntity<String> sendOrder(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader String Token) {
        ShopUser shopUser;
        try {
            shopUser = authenticationService.getUserIfHasValidToken(Token);
            payPalGatewayService.processGooglePayRequest(paymentRequest.getGoogleTokenAsJsonString());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid.");
        }
        try {
            orderService.saveNewOrder(paymentRequest, shopUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
    @PutMapping("modifyOrder")
    @Transactional
    public ResponseEntity<String> modifyOrder(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader String Token) {
        ShopUser shopUser;
        try {
            shopUser = authenticationService.getUserIfHasValidToken(Token);
            payPalGatewayService.processGooglePayRequest(paymentRequest.getGoogleTokenAsJsonString());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid.");
        }
        try {
            orderService.modifyExistingOrder(paymentRequest, shopUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop order update failed.");
        }
        return ResponseEntity.ok("Order update was successful.");
    }
    @DeleteMapping("/deleteOrder/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@RequestHeader String Token, @PathVariable Long id) {
        try {
            authenticationService.isUserLoggedInWithToken(Token);
        } catch (NoSuchElementException e) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        shopOrderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
