package com.e_commerce.webshop.service.persistence;

import com.e_commerce.webshop.dto.MainPageProductDTO;
import com.e_commerce.webshop.dto.NewProductDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductCategory;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.service.storage.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    PictureService pictureService;
    public Page<MainPageProductDTO> getMainPage(ProductCategory.ShopProductCategory category, Pageable pageable) {
        Page<Product> pageResult = productRepository.findByCategory_CategoryEnum(category, pageable);
        Page<MainPageProductDTO> dtoPageResult = pageResult.map(MainPageProductDTO::new);
        dtoPageResult.getContent().forEach(dtoProduct -> {
            try {
                dtoProduct.setPicture(pictureService.getProductPictureById(dtoProduct.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return dtoPageResult;
    }
    public void saveNewProduct(NewProductDTO productDTO) {
        String base64String = productDTO.getPicture();
        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setStock(productDTO.getStock());
        newProduct.setDescription(productDTO.getDescription());
        productRepository.save(newProduct);
        Long productId = productRepository.findById(newProduct.getId()).map(Product::getId).get();
        pictureService.writePicturesToFile(productId, base64String);
    }
}
