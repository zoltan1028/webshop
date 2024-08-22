package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.OrderDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductQuantity;
import com.e_commerce.webshop.model.ShopOrder;
import com.e_commerce.webshop.repository.IProductQuantityRepository;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.repository.IShopOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders/")
public class OrderController {
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IProductQuantityRepository productQuantityRepository;
    @PostMapping("submitOrder")
    public void sendOrder(@RequestBody List<OrderDTO> orderData) {
        ShopOrder newOrder = new ShopOrder();
        System.out.println(newOrder.getId());
        shopOrderRepository.save(newOrder);
        for (var dtoItem : orderData) {
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
            System.out.println(newOrder.getId());
            productQuantityRepository.save(quantity);
        }
        System.out.println(newOrder.getQuantityList().toString());
    }
}
