package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.OrderProductDTO;
import com.e_commerce.webshop.dto.PaymentRequestDTO;
import com.e_commerce.webshop.dto.ordersbyuser.OrdersByUserUserDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductQuantity;
import com.e_commerce.webshop.model.ShopOrder;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IProductQuantityRepository;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.repository.IShopOrderRepository;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.PayPalGatewayService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
    @GetMapping("getOrdersOfUser")
    public ResponseEntity<OrdersByUserUserDTO> getOrdersOfUserByToken(@RequestHeader String Token) {
        Optional<ShopUser> testUser = userRepository.findByToken(Token);
        if(testUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        OrdersByUserUserDTO userDTO = new OrdersByUserUserDTO(testUser.get());
        return  ResponseEntity.ok(userDTO);
    }
    @PostMapping("submitOrder")
    @Transactional
    public ResponseEntity<String> sendOrder(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader String Token) {
        Optional<ShopUser> user = userRepository.findByToken(Token);
        if (user.isEmpty()) {return ResponseEntity.badRequest().body("Token was not found on submiting new order.");}
        if(!payPalGatewayService.isPaymentSuccessful(paymentRequest.getGoogleTokenAsJsonString())) {return ResponseEntity.badRequest().body("Payment failed.");}

        ShopUser shopUser = user.get();
        ShopOrder newOrder = new ShopOrder(shopUser);
        shopOrderRepository.save(newOrder);
        for (OrderProductDTO dtoItem : paymentRequest.getCart()) {
            Optional<Product> optProduct = productRepository.findById(dtoItem.getId());
            if (optProduct.isEmpty()) {return ResponseEntity.badRequest().body("Product not found.");}
            Product product = optProduct.get();
            ProductQuantity quantity = new ProductQuantity(newOrder, product, dtoItem.getPieces());
            newOrder.addProductQuantityToOrder(quantity);
            productQuantityRepository.save(quantity);
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
    @PutMapping("modifyOrder")
    @Transactional
    public ResponseEntity<?> modifyOrder(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader String Token) {
        Optional<ShopUser> user = userRepository.findByToken(Token);
        if (user.isEmpty()) {return ResponseEntity.badRequest().body("Token was not found on submiting new order.");}
        if(!payPalGatewayService.isPaymentSuccessful(paymentRequest.getGoogleTokenAsJsonString())) {return ResponseEntity.badRequest().body("Payment failed.");}
        Long idOfSelectedOrder = paymentRequest.getIdOfSelectedOrder();

        List<ProductQuantity> productQuantities = productQuantityRepository.findByShoporderId(idOfSelectedOrder);

        Optional<ShopOrder> orderToModify = shopOrderRepository.findById(idOfSelectedOrder);
        if(orderToModify.isEmpty()) {return ResponseEntity.badRequest().body("Order was not found.");}
        //paymentRequest.getCart()
        for (OrderProductDTO dtoItem : paymentRequest.getCart()) {
            boolean isProductFound = false;
            for (ProductQuantity pq: productQuantities) {
                if (Objects.equals(pq.getProduct().getId(), dtoItem.getId())) {
                    pq.modifyOrderByQuantityChange(dtoItem.getPieces());
                    isProductFound = true;
                }
            }
            if (!isProductFound) {
                Optional<Product> optProduct = productRepository.findById(dtoItem.getId());
                if (optProduct.isEmpty()) {return ResponseEntity.badRequest().body("Product not found.");}
                Product product = optProduct.get();
                ProductQuantity quantity = new ProductQuantity(orderToModify.get(), product, dtoItem.getPieces());
                orderToModify.get().addProductQuantityToOrder(quantity);
                productQuantityRepository.save(quantity);
            }
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
    @DeleteMapping("/deleteOrder/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@RequestHeader String Token, @PathVariable Long id) {
        Optional<ShopUser> shopUserOptional = userRepository.findByToken(Token);
        if(shopUserOptional.isEmpty()) {return ResponseEntity.badRequest().build();}
        shopOrderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
