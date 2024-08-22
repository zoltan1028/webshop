package com.e_commerce.webshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductQuantity {
    public void setQuantity(int quantity) {
        this.sum = quantity * this.getProduct().getPrice();
        this.quantity = quantity;
    }
    @GeneratedValue
    @Id
    private Long id;
    private int sum;
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ProductId")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ShoporderId")
    private ShopOrder shoporder;
}
