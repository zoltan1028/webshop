package com.e_commerce.webshop.service;

import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.AuthenticationService;
import com.e_commerce.webshop.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void initH2() {
        ShopUser user = new ShopUser();
        user.setUsername("admin");
        user.setPassword(authenticationService.hashPassword("admin"));
        user.setAdmin("admin");
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
            productRepository.save(testProduct);
        }
        List<Product> productList = productRepository.findAll();
        String dummyPic = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMKCMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC";
        for (Product p: productList) {
            pictureService.performFileOperations(p.getId(), dummyPic);
        }
    }
}
