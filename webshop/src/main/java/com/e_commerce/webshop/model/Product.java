package com.e_commerce.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

import jdk.jfr.Category;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Product {
    @GeneratedValue
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    private String description;
    private String picture;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductQuantity> productQuantities;
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private ProductCategory category;
    public Product() {}
    public Product(String name, BigDecimal price, int stock, ProductCategory category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.setCategory(category);
    }
    public void setCategory(ProductCategory category) {
        this.category = category;
        if (category != null && !category.getProductList().contains(this)) {
            category.addProductToCategory(this);
        }
    }

}
