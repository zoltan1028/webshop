package com.e_commerce.webshop.service;

import com.e_commerce.webshop.dto.OrderProductDTO;
import com.e_commerce.webshop.model.*;
import com.e_commerce.webshop.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public void initH2() {
        ProductCategory fruit = new ProductCategory();
        ProductCategory vegetables = new ProductCategory();
        fruit.setCategory("fruit");
        vegetables.setCategory("vegetables");
        categoryRepository.save(fruit);
        categoryRepository.save(vegetables);

        ShopUser user = new ShopUser();
        user.setUsername("admin");
        user.setPassword(authenticationService.hashPassword("admin"));
        user.setUserRight(ShopUser.UserRight.ADMIN);
        userRepository.save(user);
        String[] names = {
                "alma", "korte", "banan", "cseresznye", "avokado",
                "mango", "dio", "mandula", "grapefruit", "szilva",
                "meggy", "paradicsom", "uborka", "retek", "hagyma",
                "fokhagyma", "cekla", "kivi", "narancs", "padlizsan"};
        String[] descriptions = {"a", "b", "c", "d", "e", "f"};
        int[] prices = {10,20,30,40,50,60};
        int[] stock = {5, 25, 35,45,55,65};
        productRepository.deleteAll();
        for (int i = 0; i < 20; i++) {
            Product testProduct = new Product();
            testProduct.setName(names[i]);
            testProduct.setDescription(String.valueOf(i));
            testProduct.setPrice(i);
            testProduct.setStock(i);
            fruit.addProductToCategory(testProduct);
            productRepository.save(testProduct);
        }
        List<Product> productList = productRepository.findAll();
        String dummyPic = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMKCMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC";
        pictureService.deleteAllFilesFromPicturesDirectory();
        for (Product p: productList) {
            pictureService.writePicturesToFile(p.getId(), dummyPic);
        }
        //test order
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> rawList = List.of(
                Map.of("id", 2, "pieces", 2),
                Map.of("id", 3, "pieces", 3),
                Map.of("id", 1, "pieces", 1)
        );
        List<OrderProductDTO> orderDataCart = objectMapper.convertValue(rawList, new TypeReference<>(){});

        Optional<ShopUser> shopUser = userRepository.findByUsername("admin");
        ShopOrder newOrder = new ShopOrder();
        shopOrderRepository.save(newOrder);
        if (shopUser.isPresent()) {
            ShopUser testUser = shopUser.get();
            newOrder.setUser(testUser);
            //testUser.addShopOrderToShopUser(newOrder);
        }
        System.out.println(newOrder.getUser().getUsername());
        shopOrderRepository.save(newOrder);

        for (OrderProductDTO dtoItem : orderDataCart) {
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

    }
}
