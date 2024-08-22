package com.e_commerce.webshop.model;

import jakarta.persistence.*;

import java.util.List;

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
    private int price;
    private int stock;
    private String description;
    private String picture;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductQuantity> productQuantities;
}
