package com.e_commerce.webshop.controller;

import com.e_commerce.webshop.dto.MainPageProductDTO;
import com.e_commerce.webshop.dto.NewProductDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.service.AuthenticationService;
import com.e_commerce.webshop.service.PictureService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products/")
public class ProductController {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    PictureService pictureService;
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("getProducts")
    public ResponseEntity<Page<MainPageProductDTO>> getProductsByPage(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageResult = productRepository.findAll(pageable);
        Page<MainPageProductDTO> dtoPageResult = pageResult.map(MainPageProductDTO::new);
        dtoPageResult.getContent().forEach(product ->
                product.setPicture(pictureService.getProductPictureById(product.getId()))
        );
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(dtoPageResult);
    }
    @PostMapping("newProduct")
    @Transactional
    public ResponseEntity<String> saveNewProductToDataBase(@RequestBody NewProductDTO productDTO, @RequestHeader String token) {
        if (!(authenticationService.isLoggedInWithToken(token) && authenticationService.isAdmin(token))) {
            return ResponseEntity.badRequest().build();
        }
        String base64String = productDTO.getPicture();

        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setStock(productDTO.getStock());
        newProduct.setDescription(productDTO.getDescription());

        productRepository.save(newProduct);
        //save product pic with generated id
        Optional<Product> managedProduct = productRepository.findById(newProduct.getId());
        managedProduct.ifPresent(value -> pictureService.writePicturesToFile(value.getId(), base64String));
        return ResponseEntity.ok("New product was registered.");
    }
}
