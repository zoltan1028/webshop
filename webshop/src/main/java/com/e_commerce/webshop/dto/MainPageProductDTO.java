package com.e_commerce.webshop.dto;

import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductCategory;
import com.e_commerce.webshop.model.ProductQuantity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MainPageProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    private String description;
    private String picture;
    public MainPageProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.description = product.getDescription();
        this.picture = product.getPicture();
    }
}
