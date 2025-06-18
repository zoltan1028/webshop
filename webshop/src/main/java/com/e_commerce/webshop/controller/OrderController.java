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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        ShopUser user = userRepository.findByToken(Token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not found."));
        OrdersByUserUserDTO userDTO = new OrdersByUserUserDTO(user);
        return  ResponseEntity.ok(userDTO);
    }
    @PostMapping("submitOrder")
    @Transactional
    public ResponseEntity<String> sendOrder(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader String Token) {
        ShopUser shopUser = userRepository.findByToken(Token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token was on submitting new order."));
        if(!payPalGatewayService.isPaymentSuccessful(paymentRequest.getGoogleTokenAsJsonString())) {return ResponseEntity.badRequest().body("Payment failed.");}

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
        Long idOfSelectedOrder = paymentRequest.getIdOfSelectedOrder();
        userRepository.findByToken(Token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token was not found on submiting new order."));
        if(!payPalGatewayService.isPaymentSuccessful(paymentRequest.getGoogleTokenAsJsonString())) {return ResponseEntity.badRequest().body("Payment failed.");}
        ShopOrder orderToModify = shopOrderRepository.findById(idOfSelectedOrder).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop order not found."));

        List<ProductQuantity> productQuantities = orderToModify.getQuantityList();
        //Function.identity() maps value for Map
        Map<Long, ProductQuantity> pqs = productQuantities.stream()
                .collect(Collectors.toMap(
                        pq -> pq.getProduct().getId(),
                        Function.identity()));

        for (OrderProductDTO shoppingCartDTO : paymentRequest.getCart()) {
            Long pid = shoppingCartDTO.getId();
            if (pqs.containsKey(pid)) {
                ProductQuantity pq = pqs.get(pid);
                pq.modifyOrderByQuantityChange(shoppingCartDTO.getPieces());
            } else {
                Product product = productRepository.findById(shoppingCartDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));
                ProductQuantity quantity = new ProductQuantity(orderToModify, product, shoppingCartDTO.getPieces());
                orderToModify.addProductQuantityToOrder(quantity);
                productQuantityRepository.save(quantity);
            }
        }
        return ResponseEntity.ok("Transaction was successful.");
    }
    @DeleteMapping("/deleteOrder/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@RequestHeader String Token, @PathVariable Long id) {
        userRepository.findByToken(Token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not found."));
        shopOrderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
