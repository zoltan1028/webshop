package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.OrderDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductQuantity;
import com.e_commerce.webshop.model.ShopOrder;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IProductQuantityRepository;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.repository.IShopOrderRepository;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.PayPalGatewayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders/")
public class OrderController {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IProductQuantityRepository productQuantityRepository;
    @Autowired
    PayPalGatewayService payPalGatewayService;

    @PostMapping("submitOrder")
    @Transactional
    public ResponseEntity<String> sendOrder(@RequestBody String data, @RequestHeader String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode orderData;
        try {
            orderData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<OrderDTO> orderDataCart = objectMapper.convertValue(orderData.get("cart"), new TypeReference<List<OrderDTO>>(){});
        Optional<ShopUser> user = userRepository.findByToken(authToken);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Token was not found on submiting new order.");
        }
        String googleTokenData = orderData.get("google_tokenData").toString();
        if(!payPalGatewayService.isPaymentSuccessful(googleTokenData)) {
            return ResponseEntity.badRequest().body("Payment failed.");
        }
        ShopUser shopUser = user.get();
        ShopOrder newOrder = new ShopOrder();
        shopOrderRepository.save(newOrder);
        newOrder.setUser(shopUser);
        for (var dtoItem : orderDataCart) {
            Optional<Product> optProduct = productRepository.findById(dtoItem.getId());
            Product product = null;
            if(optProduct.isPresent()) {
                product = optProduct.get();
            }
            ProductQuantity quantity = new ProductQuantity();
            //for pq set prod, order, qty, sum
            quantity.setProduct(product);
            quantity.setShoporder(newOrder);
            quantity.setQuantity(dtoItem.getPieces());
            //add product to order
            newOrder.addProductQuantityToOrder(quantity);
            productQuantityRepository.save(quantity);
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
}
