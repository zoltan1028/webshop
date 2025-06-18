package com.e_commerce.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.*;
@Getter
@Setter
@Entity
public class ProductQuantity {
    public ProductQuantity(){}
    public ProductQuantity(ShopOrder order, Product product, int productQuantity) {
        this.shoporder = order;
        this.product = product;
        BigDecimal price   = getProduct().getPrice();
        BigDecimal qty     = BigDecimal.valueOf(productQuantity);
        this.sum = price.multiply(qty).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.quantity = productQuantity;
    }
    @GeneratedValue
    @Id
    private Long id;
    private BigDecimal sum;
    private int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ProductId")
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ShoporderId")
    private ShopOrder shoporder;
    public void modifyOrderByQuantityChange(int qtyFromDto) {
        BigDecimal productPrice   = this.getProduct().getPrice();
        BigDecimal plusAmountToTotal = productPrice.multiply(BigDecimal.valueOf(qtyFromDto));
        this.shoporder.setOrderTotal(shoporder.getOrderTotal().add(plusAmountToTotal));
        this.quantity += qtyFromDto;
        this.sum = productPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
