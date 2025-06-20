package com.e_commerce.webshop.service.APIService;

import com.e_commerce.webshop.dto.MainPageProductDTO;
import com.e_commerce.webshop.dto.NewProductDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    PictureService pictureService;
    public Page<MainPageProductDTO> getMainPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageResult = productRepository.findAll(pageable);
        Page<MainPageProductDTO> dtoPageResult = pageResult.map(MainPageProductDTO::new);
        dtoPageResult.getContent().forEach(product -> {
            try {
                product.setPicture(pictureService.getProductPictureById(product.getId()));
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
