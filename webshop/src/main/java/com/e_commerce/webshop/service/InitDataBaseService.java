package com.e_commerce.webshop.service;

import com.e_commerce.webshop.dto.OrderProductDTO;
import com.e_commerce.webshop.model.*;
import com.e_commerce.webshop.repository.*;
import com.e_commerce.webshop.service.auth.AuthenticationService;
import com.e_commerce.webshop.service.storage.PictureService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InitDataBaseService {
    @Autowired
    PictureService pictureService;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    IProductQuantityRepository productQuantityRepository;
    @Transactional
    public void initH2() throws IOException {
        pictureService.deleteAllFilesFromPicturesDirectory();
        ProductCategory fruit = new ProductCategory(ProductCategory.ShopProductCategory.FRUIT);
        ProductCategory vegetable = new ProductCategory(ProductCategory.ShopProductCategory.VEGETABLE);
        categoryRepository.save(fruit);
        categoryRepository.save(vegetable);
        String hashedPassword = authenticationService.hashPassword("admin");
        ShopUser user = new ShopUser("admin", hashedPassword, ShopUser.UserRight.ADMIN);
        userRepository.save(user);
        String[] names = {
                "alma", "korte", "banan", "cseresznye", "avokado",
                "mango", "dio", "mandula", "grapefruit", "szilva",
                "meggy", "paradicsom", "uborka", "retek", "hagyma",
                "fokhagyma", "cekla", "kivi", "narancs", "padlizsan"};
        productRepository.deleteAll();

        for (int i = 0; i < 20; i++) {
            Product testProduct;
            if (ThreadLocalRandom.current().nextBoolean()) {
                testProduct = new Product(names[i], BigDecimal.valueOf(i), i, fruit);
                fruit.addProductToCategory(testProduct);
            } else {
                testProduct = new Product(names[i], BigDecimal.valueOf(i), i, vegetable);
                vegetable.addProductToCategory(testProduct);
            }
            testProduct.setDescription(String.valueOf(i));
            productRepository.save(testProduct);
        }
        List<Product> productList = productRepository.findAll();
        String dummyPic = pictureService.readPictureFromResources("logo.txt");
        for (Product p: productList) {pictureService.writePicturesToFile(p.getId(), dummyPic);}
        List<Map<String, Object>> rawList = List.of(
                Map.of("id", 2, "pieces", 2),
                Map.of("id", 3, "pieces", 3),
                Map.of("id", 1, "pieces", 1)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderProductDTO> orderDataCart = objectMapper.convertValue(rawList, new TypeReference<>(){});
        ShopOrder newOrder = new ShopOrder(userRepository.findByUsername("admin").get());
        shopOrderRepository.save(newOrder);
        for (OrderProductDTO dtoItem : orderDataCart) {
            Product product = productRepository.findById(dtoItem.getId()).get();
            ProductQuantity quantity = new ProductQuantity(newOrder,product,dtoItem.getPieces());
            newOrder.addProductQuantityToOrder(quantity);
            newOrder.setStatus(ShopOrder.OrderStatus.COMPLETED);
            productQuantityRepository.save(quantity);
        }
    }
}
