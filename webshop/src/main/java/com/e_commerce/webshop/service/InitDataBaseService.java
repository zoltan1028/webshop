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

import java.math.BigDecimal;
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

    @Transactional
    public void initH2() {
        ProductCategory fruit = new ProductCategory();
        ProductCategory vegetables = new ProductCategory();
        fruit.setCategory(ProductCategory.ShopProductCategory.FRUIT);
        vegetables.setCategory(ProductCategory.ShopProductCategory.VEGETABLE);
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
            testProduct.setPrice(BigDecimal.valueOf(i));
            testProduct.setStock(i);
            fruit.addProductToCategory(testProduct);
            productRepository.save(testProduct);
        }
        List<Product> productList = productRepository.findAll();
        String dummyPic = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAACGUlEQVR4nO2cW1LCQBQFE8vdsCM3I27GHbGe+BFK8UGgmXtnbqzu8seA5KQ9M0xhnHlZlknu42l0gD2hLICyAMoCKAugLICyAMoCKAvwPDrAmffDYfsJL6dTnyQb2CyAsgDKAigLoCyAsgDKAigLoCxAlRX8Y8xv8+W3y2vu3xOGNmuev774k3+Y+vNILINk3Sno+o9f85Lqq6+s+3u0/TLHzUfTfHWU1exoOF0m+P1rWslv1n8xNaXL+kemplxZaaaW4+OPtpAmK7lT14ycj+ecPUdWl9H329e3IwkZ5vhbjkrNU6FXF92sUqam4DyhsqqZWolL5Uc0gDhZNWu1EpQtSFZlUysRCR2GAGUBImTVH4MrzTltFqBZ1l5qtdKW1mYBlAVQFqBN1r4mrJWGzDYLoCyAsgDKAigLoCyAsgDKAigLoCxAm6w97gnRkNlmAZQFUBagWda+pq22tDYLECFrL+VqzmmzAMoCBMmqPxIjEsY1q7KvoGwOQ0CorJrliksV3axqvkrfrTxV8hWdJGfOquArIUPaBD/WV87ZM98NR/lKO2/y0qG/r8wz5q+zevpKPleXf/tdryH1/qQuv5KOK/i86+lV3r47hnxeVUjLuk+Ig7ZXaRyYg95nh+5Fc3nNN8UVWOiW2bingIub+BENQFkAZQGUBVAWQFkAZQGUBVAWQFkAZQGUBVAWQFkAZQGUBVAWQFmAD/EIZONML+6GAAAAAElFTkSuQmCC";
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
        ShopOrder newOrder;
        if (shopUser.isEmpty()) {
            newOrder = new ShopOrder(new ShopUser());
        } else {
            ShopUser testUser = shopUser.get();
            newOrder = new ShopOrder(testUser);
        }
        shopOrderRepository.save(newOrder);

        for (OrderProductDTO dtoItem : orderDataCart) {
            Optional<Product> optProduct = productRepository.findById(dtoItem.getId());
            Product product = null;if(optProduct.isPresent()) {product = optProduct.get();}
            ProductQuantity quantity = new ProductQuantity(newOrder,product,dtoItem.getPieces());
            newOrder.addProductQuantityToOrder(quantity);
            newOrder.setStatus(ShopOrder.OrderStatus.COMPLETED);
            productQuantityRepository.save(quantity);
        }

    }
}
