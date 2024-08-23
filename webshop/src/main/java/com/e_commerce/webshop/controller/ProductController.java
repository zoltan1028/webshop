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
import com.e_commerce.webshop.service.PictureService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products/")
public class ProductController {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IProductQuantityRepository productQuantityRepository;
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    PictureService pictureService;
    @Autowired
    IUserRepository userRepository;

    @GetMapping("init")
    public void initH2() {
        ShopUser user = new ShopUser();
        user.setUsername("test");
        user.setPassword("test");
        user.setAdmin("admin");
        userRepository.save(user);
        String[] names = {"alma", "korte", "banan", "cseresznye", "avokado", "mango"};
        String[] descriptions = {"a", "b", "c", "d", "e", "f"};
        int[] prices = {10,20,30,40,50,60};
        int[] stock = {5, 25, 35,45,55,65};

        productRepository.deleteAll();
        for (int i = 0; i < 6; i++) {
            Product testProduct = new Product();
            testProduct.setName(names[i]);
            testProduct.setDescription(descriptions[i]);
            testProduct.setPrice(prices[i]);
            testProduct.setStock(stock[i]);
            productRepository.save(testProduct);
        }
        List<Product> productList = productRepository.findAll();
        String dummyPic = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMKCMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC";
        for (Product p: productList) {
            pictureService.performFileOperations(p.getId(), dummyPic);
        }
    }
    @GetMapping("getProducts")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> productList = productRepository.findAll();
        for (Product p: productList) {p.setPicture(pictureService.getProductPictureById(p.getId()));}
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productList);
    }
    @PostMapping("newProduct")
    public void saveNewProductToDataBase(@RequestBody Product product) {
        String base64String = product.getPicture();
        product.setPicture(null);
        productRepository.save(product);
        Optional<Product> managedProduct = productRepository.findById(product.getId());
        managedProduct.ifPresent(value -> pictureService.performFileOperations(value.getId(), base64String));
    }

}
