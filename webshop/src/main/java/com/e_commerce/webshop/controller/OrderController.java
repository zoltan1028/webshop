package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.ProductDTO;
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
    public ResponseEntity<String> sendOrder(@RequestBody String orderListWithGoogleTokenData, @RequestHeader String Token) {
        System.out.println(orderListWithGoogleTokenData);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode orderData;
        try {orderData = objectMapper.readTree(orderListWithGoogleTokenData);} catch (JsonProcessingException e) {throw new RuntimeException(e);}
        List<ProductDTO> orderDataCart = objectMapper.convertValue(orderData.get("cart"), new TypeReference<List<ProductDTO>>(){});

        Optional<ShopUser> user = userRepository.findByToken(Token);
        if (user.isEmpty()) {return ResponseEntity.badRequest().body("Token was not found on submiting new order.");}
        String googleTokenData = orderData.get("google_tokenData").toString();
        if(!payPalGatewayService.isPaymentSuccessful(googleTokenData)) {return ResponseEntity.badRequest().body("Payment failed.");}

        ShopUser shopUser = user.get();
        ShopOrder newOrder = new ShopOrder();
        shopOrderRepository.save(newOrder);
        newOrder.setUser(shopUser);
        for (ProductDTO dtoItem : orderDataCart) {
            Optional<Product> optProduct = productRepository.findById(dtoItem.getId());
            Product product = null;
            if(optProduct.isPresent()) {
                product = optProduct.get();
            }
            ProductQuantity quantity = new ProductQuantity();
            quantity.setProduct(product);
            quantity.setShoporder(newOrder);
            quantity.setQuantity(dtoItem.getPieces());
            newOrder.addProductQuantityToOrder(quantity);
            productQuantityRepository.save(quantity);
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
}
