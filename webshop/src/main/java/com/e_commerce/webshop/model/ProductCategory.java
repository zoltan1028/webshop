package com.e_commerce.webshop.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class ProductCategory {
    @GeneratedValue
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private ShopProductCategory category;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
    public enum ShopProductCategory {
        FRUIT,
        VEGETABLE;
    }
    public ProductCategory () {}
    public ProductCategory (ShopProductCategory category) {
        this.category = category;
    }
    public void addProductToCategory(Product product) {
        if (product != null && !this.productList.contains(product) ) {
            this.productList.add(product);
            product.setCategory(this);
        }
    }
}