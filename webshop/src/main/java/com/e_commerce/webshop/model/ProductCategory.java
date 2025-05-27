package com.e_commerce.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String category;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
    public void addProductToCategory(Product product) {
        this.productList.add(product);
        product.setCategory(this);
    }
}
