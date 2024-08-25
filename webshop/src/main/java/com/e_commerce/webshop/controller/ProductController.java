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
import com.e_commerce.webshop.service.AuthenticationService;
import com.e_commerce.webshop.service.PictureService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products/")
public class ProductController {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    PictureService pictureService;

    @GetMapping("getProducts")
    public ResponseEntity<Page<Product>> getProductsByPage(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageResult = productRepository.findAll(pageable);
        List<Product> pageResultContent = pageResult.getContent().stream().map(product -> {
            product.setPicture(pictureService.getProductPictureById(product.getId()));
            return product;
        }).toList();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("getProductsPlaceHolder")
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
